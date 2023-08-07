/*
 *    Copyright (c) 2023, ShieldBlaze
 *
 *    Extended Email Validator licenses this file to you under the
 *    Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.shieldblaze.extendedemailvalidator.core.validators;

import com.sanctionco.jmail.Email;
import com.sanctionco.jmail.JMail;
import com.shieldblaze.extendedemailvalidator.core.NetworkConfig;
import com.shieldblaze.extendedemailvalidator.core.ValidationContext;
import com.shieldblaze.extendedemailvalidator.core.Validator;
import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MXRecordValidator implements Validator {

    private final ExtendedResolver resolver;

    public MXRecordValidator(NetworkConfig networkConfig) throws UnknownHostException {
        resolver = new ExtendedResolver(networkConfig.dnsServers().toArray(new String[0]));
        resolver.setTimeout(Duration.ofMillis(networkConfig.dnsTimeout()));
    }

    @Override
    public boolean isValid(ValidationContext validationContext, String email) throws IOException {
        Optional<Email> optionalEmail = JMail.tryParse(email);
        if (optionalEmail.isEmpty()) {
            return false;
        }

        String domain = optionalEmail.get().domain();

        Lookup mxLookup = new Lookup(domain, Type.MX);
        mxLookup.setResolver(resolver);
        Record[] records = mxLookup.run();

        // If no records found, return false
        if (records.length == 0) {
            validationContext.markMxValidationFailed("DNS Lookup returned no records in MX Record query");
            return false;
        }

        // Get all MX records
        List<MXRecord> mxRecords = Arrays.stream(records)
                .filter(record -> record instanceof MXRecord)
                .map(record -> (MXRecord) record)
                .toList();

        // If no MX records found, return false
        if (mxRecords.isEmpty()) {
            validationContext.markMxValidationFailed("DNS Lookup returned no MX records");
            return false;
        }

        // IPv4 lookup
        for (MXRecord mxRecord : mxRecords) {
            Lookup aRecordLookup = new Lookup(mxRecord.getTarget(), Type.A);
            aRecordLookup.setResolver(resolver);

            Record[] aRecords = aRecordLookup.run();
            if (aRecords == null || aRecords.length == 0) {
                validationContext.markMxValidationFailed("DNS Lookup for MX Record Target returned no records in A Record query");
                return false;
            }

            List<InetAddress> aRecordIps = Arrays.stream(aRecords)
                    .filter(record -> record instanceof ARecord)
                    .map(record -> ((ARecord) record).getAddress())
                    .toList();

            if (aRecordIps.isEmpty()) {
                validationContext.markMxValidationFailed("DNS Lookup for MX Record Target returned no A Records");
                return false;
            }

            validationContext.mailServerIpv4Addresses().add(aRecordIps.get(0).getHostAddress());
        }

        // IPv6 lookup
        for (MXRecord mxRecord : mxRecords) {
            Lookup aaaaRecordLookup = new Lookup(mxRecord.getTarget(), Type.AAAA);
            aaaaRecordLookup.setResolver(resolver);

            Record[] aaaaRecords = aaaaRecordLookup.run();
            boolean alreadyHasIpv4 = !validationContext.mailServerIpv4Addresses().isEmpty();

            if (aaaaRecords == null || aaaaRecords.length == 0) {
                if (alreadyHasIpv4) {
                    break;
                } else {
                    validationContext.markMxValidationFailed("DNS Lookup for MX Record Target returned no records in AAAA Record query");
                    return false;
                }
            }

            List<InetAddress> aaaaRecordIps = Arrays.stream(aaaaRecords)
                    .filter(record -> record instanceof AAAARecord)
                    .map(record -> ((AAAARecord) record).getAddress())
                    .toList();

            if (aaaaRecordIps.isEmpty()) {
                validationContext.markMxValidationFailed("DNS Lookup for MX Record Target returned no AAAA Records");
                return false;
            }

            validationContext.mailServerIpv6Addresses().add(aaaaRecordIps.get(0).getHostAddress());
        }

        validationContext.markMxValidationPassed();
        return true;
    }
}
