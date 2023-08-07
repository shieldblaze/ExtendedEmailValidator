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

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Jackson {

    /**
     * Jackson {@link ObjectMapper} configured with custom serializers and deserializers.
     */
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert " + object.getClass().getName() + " to Json String", e);
        }
    }
}
