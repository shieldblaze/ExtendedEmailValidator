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

import com.shieldblaze.extendedemailvalidator.core.NetworkConfig;
import com.shieldblaze.extendedemailvalidator.core.ValidationContext;
import com.shieldblaze.extendedemailvalidator.core.Validator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MailServerConnection implements Validator {

    private static final int[] PORTS = {25, 465, 567, 993};

    private final NetworkConfig networkConfig;

    public MailServerConnection(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    @Override
    public boolean isValid(ValidationContext validationContext, String email) {
        boolean hasIpv4Address = !validationContext.mailServerIpv4Addresses().isEmpty();
        boolean hasIpv6Address = !validationContext.mailServerIpv6Addresses().isEmpty();

        if (!hasIpv4Address && !hasIpv6Address) {
            validationContext.markSocketConnectionFailed("No IPv4 or IPv6 address found for mail server");
            return false;
        }

        boolean ipv4ConnectionSuccess = false;

        // Try to connect to the mail server on each IPv4 address and port
        for (String ipAddress : validationContext.mailServerIpv4Addresses()) {

            // If we've already connected, break out of the loop
            if (ipv4ConnectionSuccess) {
                break;
            }

            for (int port : PORTS) {
                if (checkConnection(ipAddress, port)) {
                    ipv4ConnectionSuccess = true;
                    break;
                }
            }
        }

        boolean ipv6ConnectionSuccess = false;

        // Try to connect to the mail server on each IPv6 address and port
        for (String ipAddress : validationContext.mailServerIpv6Addresses()) {

            // If we've already connected, break out of the loop
            if (ipv6ConnectionSuccess) {
                break;
            }

            for (int port : PORTS) {
                if (checkConnection(ipAddress, port)) {
                    ipv6ConnectionSuccess = true;
                    break;
                }
            }
        }

        if (!ipv4ConnectionSuccess && !ipv6ConnectionSuccess) {
            validationContext.markSocketConnectionFailed("Could not connect to mail server via IPv4 or IPv6");
            return false;
        }

        validationContext.markSocketConnectionSuccess();
        return true;
    }

    private boolean checkConnection(String ip, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), networkConfig.socketTimeout());
            return socket.isConnected();
        } catch (IOException e) {
            return false;
        }
    }
}
