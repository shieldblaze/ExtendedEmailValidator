/*
 *    Copyright (c) 2023, ShieldBlaze
 *
 *    Extended Email Validator licenses this file to you under the
 *     Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.shieldblaze.extendedemailvalidator.api.internal;

import com.shieldblaze.extendedemailvalidator.core.NetworkConfig;
import com.shieldblaze.extendedemailvalidator.core.ValidatingChain;
import com.shieldblaze.extendedemailvalidator.core.Validator;
import com.shieldblaze.extendedemailvalidator.core.validators.AddressValidator;
import com.shieldblaze.extendedemailvalidator.core.validators.MXRecordValidator;
import com.shieldblaze.extendedemailvalidator.core.validators.MailServerConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ValidationBean {

    @Value("${dns-servers}")
    private List<String> dnsServers;

    @Value("${dns-timeout}")
    private Integer dnsTimeout;

    @Value("${socket-timeout}")
    private Integer socketTimeout;

    @Value("${mx-validator-enabled}")
    private Boolean mxValidatorEnabled;

    @Value("${server-connection-validator-enabled}")
    private Boolean serverConnectionValidator;

    @Bean
    public ValidatingChain validatingChain() throws UnknownHostException {
        NetworkConfig networkConfig = new NetworkConfig(dnsServers, dnsTimeout, socketTimeout);
        List<Validator> validators = new ArrayList<>();
        validators.add(new AddressValidator());

        // Add MX validator if enabled
        if (mxValidatorEnabled) {
            validators.add(new MXRecordValidator(networkConfig));
        }

        // Add server connection validator if enabled
        if (serverConnectionValidator) {
            validators.add(new MailServerConnection(networkConfig));
        }

        // Create validating chain with all validators
        return new ValidatingChain(validators.toArray(Validator[]::new));
    }
}
