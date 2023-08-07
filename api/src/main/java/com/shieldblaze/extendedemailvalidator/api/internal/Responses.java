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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.shieldblaze.extendedemailvalidator.api.internal.Jackson.toJson;
import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * This class contains methods to generate {@link ResponseEntity} with JSON body.
 */
public final class Responses {

    /**
     * This method generates {@link ResponseEntity} with status code 200 and an empty body.
     */
    public static ResponseEntity<String> ok() {
        return ResponseEntity.ok().build();
    }

    /**
     * This method generates {@link ResponseEntity} with status code 200 and a JSON body with the given object
     * and transforms it to JSON string.
     */
    public static ResponseEntity<String> ok(Object object) {
        return ResponseEntity.ok(toJson(object));
    }

    /**
     * This method generates {@link ResponseEntity} with status code 200 and a JSON body with the given key and value.
     * <br></br>
     * This method creates a JSON body with the following format:
     * <pre>
     *     {
     *       "key": "value"
     *     }
     * </pre>
     */
    public static ResponseEntity<String> ok(String key, Object value) {
        return ResponseEntity.ok(toJson(singletonMap(key, value)));
    }

    /**
     * This method generates {@link ResponseEntity} with status code 400 and a JSON body with the given object
     * and transforms it to JSON string.
     *
     * <br></br>
     * This method creates a JSON body with the following format:
     * <pre>
     *     {
     *       "error": "object"
     *     }
     * </pre>
     */
    public static ResponseEntity<String> badRequest(Object object) {
        return ResponseEntity.status(BAD_REQUEST).body(toJson(singletonMap("error", object)));
    }

    /**
     * This method generates {@link ResponseEntity} with status code 500 and a JSON body with the given object
     * and transforms it to JSON string.
     *
     * <br></br>
     * This method creates a JSON body with the following format:
     * <pre>
     *     {
     *       "error": "object"
     *     }
     * </pre>
     */
    public static ResponseEntity<String> unauthorized(Object object) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(toJson(singletonMap("error", object)));
    }

    /**
     * This method generates {@link ResponseEntity} with status code 404 and a JSON body with the given message.
     * <br></br>
     * This method creates a JSON body with the following format:
     * <pre>
     *     {
     *       "error": "message"
     *     }
     * </pre>
     * </br>
     */
    public static ResponseEntity<String> notFound(String message) {
        return ResponseEntity.status(NOT_FOUND).body(toJson(singletonMap("error", message)));
    }

    /**
     * This method generates {@link ResponseEntity} with status code 500 and a JSON body with the given message.
     * <br></br>
     * This method creates a JSON body with the following format:
     * <pre>
     *     {
     *       "error": "message"
     *     }
     * </pre>
     */
    public static ResponseEntity<String> internalServerError(String message) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(toJson(singletonMap("error", message)));
    }

    public static ResponseEntity<String> internalServerError(Object message) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(toJson(message));
    }

    /**
     * This method generates {@link ResponseEntity} with the given status code and an empty body.
     */
    public static ResponseEntity<String> response(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).build();
    }

    /**
     * This method generates {@link ResponseEntity} with the given status code and a JSON body with the given object
     * and transforms it to JSON string.
     */
    public static ResponseEntity<String> response(HttpStatus httpStatus, Object object) {
        return ResponseEntity.status(httpStatus).body(toJson(object));
    }

    /**
     * This method generates {@link ResponseEntity} with the given status code and a JSON body with the given message.
     * <br></br>
     * This method creates a JSON body with the following format:
     * <pre>
     *     {
     *       "key": "value"
     *     }
     *     </pre>
     * </br>
     */
    public static ResponseEntity<String> response(HttpStatus httpStatus, String key, String value) {
        return ResponseEntity.status(httpStatus).body(toJson(singletonMap(key, value)));
    }

    /**
     * This method generates {@link ResponseEntity} with the given status code and a JSON body with the given message.
     * <br></br>
     * This method creates a JSON body with the following format:
     * <pre>
     *     {
     *       "message": "value"
     *     }
     *     </pre>
     * </br>
     */
    public static ResponseEntity<String> responseMessage(HttpStatus httpStatus, String value) {
        return ResponseEntity.status(httpStatus).body(toJson(singletonMap("message", value)));
    }

    public static ResponseEntity<String> responseMessage(HttpStatus httpStatus, Object data) {
        return ResponseEntity.status(httpStatus).body(toJson(data));
    }

    private Responses() {
        // Prevent outside initialization
    }
}
