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
package com.shieldblaze.extendedemailvalidator.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shieldblaze.extendedemailvalidator.api.internal.DelegatingValidationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ValidationControllerTest {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validate() throws IOException, InterruptedException {
        ObjectNode json = objectMapper.createObjectNode();
        json.put("emailAddress", "no-reply@shieldblaze.com");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/validate/email"))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, httpResponse.statusCode());

        DelegatingValidationContext delegatingValidationContext = objectMapper.readValue(httpResponse.body(), DelegatingValidationContext.class);
        assertTrue(delegatingValidationContext.addressValidationPassed());
        assertTrue(delegatingValidationContext.mxValidationPassed());
        assertFalse(delegatingValidationContext.mailServerConnectionPassed());
    }
}
