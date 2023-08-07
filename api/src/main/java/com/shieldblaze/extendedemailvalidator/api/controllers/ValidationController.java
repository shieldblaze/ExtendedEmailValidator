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
import com.shieldblaze.extendedemailvalidator.api.internal.DelegatingValidationContext;
import com.shieldblaze.extendedemailvalidator.api.dto.EmailValidation;
import com.shieldblaze.extendedemailvalidator.core.ValidatingChain;
import com.shieldblaze.extendedemailvalidator.core.ValidationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static com.shieldblaze.extendedemailvalidator.api.internal.Responses.badRequest;
import static com.shieldblaze.extendedemailvalidator.api.internal.Responses.ok;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/validate")
public class ValidationController {

    private final ValidatingChain validatingChain;

    @Autowired
    private ObjectMapper objectMapper;

    public ValidationController(ValidatingChain validatingChain) {
        this.validatingChain = validatingChain;
    }

    @PostMapping(value = "/email", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<String>> validate(@RequestBody EmailValidation emailValidation) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ValidationContext result = validatingChain.validate(emailValidation.emailAddress());
                return ok(new DelegatingValidationContext(result));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(throwable -> badRequest(throwable.getMessage()));
    }
}
