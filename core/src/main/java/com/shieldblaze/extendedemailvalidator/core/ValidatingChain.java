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
package com.shieldblaze.extendedemailvalidator.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ValidatingChain {

    private static final Logger logger = LogManager.getLogger();

    private final Validator[] validator;

    public ValidatingChain(Validator... validator) {
        this.validator = validator;
    }

    /**
     * Validate the email address
     *
     * @param email The email address to validate
     * @return The validation context
     * @throws Exception If an error occurs during validation
     */
    public ValidationContext validate(String email) throws Exception {
        ValidationContext validationContext = ValidationContext.create();

        // Run each validator in the chain
        for (Validator validator : this.validator) {
            if (!validator.isValid(validationContext, email)) {
                logger.debug("Validation failed by: {} for email: {}", validator.getClass().getSimpleName(), email);
                break;
            } else {
                logger.debug("Validation passed by: {} for email: {}", validator.getClass().getSimpleName(), email);
            }
        }

        return validationContext;
    }
}
