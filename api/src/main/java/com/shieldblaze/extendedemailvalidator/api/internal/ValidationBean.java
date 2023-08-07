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
import com.shieldblaze.extendedemailvalidator.core.validators.AddressValidator;
import com.shieldblaze.extendedemailvalidator.core.validators.MXRecordValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.List;

@Component
public class ValidationBean {

    @Bean
    public ValidatingChain validatingChain() throws UnknownHostException {
        NetworkConfig networkConfig = new NetworkConfig(
                List.of("8.8.8.8", "8.8.4.4"),
                2500,
                2500
        );
        return new ValidatingChain(new AddressValidator(), new MXRecordValidator(networkConfig));
    }
}
