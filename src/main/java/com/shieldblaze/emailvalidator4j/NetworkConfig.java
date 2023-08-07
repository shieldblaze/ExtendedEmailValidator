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

import java.util.List;

public record NetworkConfig(List<String> dnsServers, int dnsTimeout, int socketTimeout) {

    public NetworkConfig(List<String> dnsServers, int dnsTimeout, int socketTimeout) {
        this.dnsServers = dnsServers;
        this.dnsTimeout = dnsTimeout;
        this.socketTimeout = socketTimeout;

        if (dnsServers == null || dnsServers.isEmpty()) {
            throw new IllegalArgumentException("dnsServers must not be null or empty");
        }

        if (!(dnsTimeout > 0)) {
            throw new IllegalArgumentException("dnsTimeout must be greater than 0");
        }

        if (!(socketTimeout > 0)) {
            throw new IllegalArgumentException("socketTimeout must be greater than 0");
        }
    }
}
