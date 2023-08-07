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
package com.shieldblaze.emailvalidator4j;

import java.util.ArrayList;
import java.util.List;

public class ValidationContext {

    private boolean addressValidationPassed;
    private boolean mxValidationPassed;
    private boolean mailServerConnectionPassed;
    private final List<String> mailServerIpv4Addresses = new ArrayList<>();
    private final List<String> mailServerIpv6Addresses = new ArrayList<>();
    private String failureReason;

    public static ValidationContext create() {
        return new ValidationContext();
    }

    public void markAddressValidationPassed() {
        this.addressValidationPassed = true;
    }

    public void markAddressValidationFailed(String failureReason) {
        this.addressValidationPassed = false;
        this.failureReason = failureReason;
    }

    public void markMxValidationPassed() {
        this.mxValidationPassed = true;
    }

    public void markMxValidationFailed(String failureReason) {
        this.mxValidationPassed = false;
        this.failureReason = failureReason;
    }

    public List<String> mailServerIpv4Addresses() {
        return mailServerIpv4Addresses;
    }

    public List<String> mailServerIpv6Addresses() {
        return mailServerIpv6Addresses;
    }

    public void markSocketConnectionSuccess() {
        this.mailServerConnectionPassed = true;
    }

    public void markSocketConnectionFailed(String failureReason) {
        this.mailServerConnectionPassed = false;
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        return "ValidationContext{" +
                "addressValidationPassed=" + addressValidationPassed +
                ", mxValidationPassed=" + mxValidationPassed +
                ", mailServerConnectionPassed=" + mailServerConnectionPassed +
                ", mailServerIpv4Addresses=" + mailServerIpv4Addresses +
                ", mailServerIpv6Addresses=" + mailServerIpv6Addresses +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }

    private ValidationContext() {
        // Private constructor
    }
}
