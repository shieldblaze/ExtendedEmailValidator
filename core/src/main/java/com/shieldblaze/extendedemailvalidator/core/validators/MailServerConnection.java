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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static java.util.Objects.requireNonNull;

/**
 * Performs validation of mail server connection on TCP ports 25, 465, 587, 993.
 */
public class MailServerConnection implements Validator {

    private static final Logger logger = LogManager.getLogger();
    private static final int[] PORTS = {25, 465, 567, 993};

    private final NetworkConfig networkConfig;

    public MailServerConnection(NetworkConfig networkConfig) {
        this.networkConfig = requireNonNull(networkConfig, "NetworkConfig");
    }

    @Override
    public boolean isValid(ValidationContext validationContext, String email) {
        logger.debug("Validating mail server connection for address: {}", email);

        boolean hasIpv4Address = !validationContext.mailServerIpv4Addresses().isEmpty();
        boolean hasIpv6Address = !validationContext.mailServerIpv6Addresses().isEmpty();

        if (!hasIpv4Address && !hasIpv6Address) {
            logger.debug("No IPv4 or IPv6 address found for mail server");
            validationContext.markSocketConnectionFailed("No IPv4 or IPv6 address found for mail server");
            return false;
        }

        boolean ipv4ConnectionSuccess = false;

        // Try to connect to the mail server on each IPv4 address and port
        for (String ipAddress : validationContext.mailServerIpv4Addresses()) {

            // If we've already connected, break out of the loop
            if (ipv4ConnectionSuccess) {
                logger.debug("Successfully connected to mail server on IPv4 address: {}, returning result", ipAddress);
                break;
            }

            for (int port : PORTS) {
                if (checkConnection(ipAddress, port)) {
                    logger.debug("Successfully connected to mail server on IPv4 address: {} and port: {}", ipAddress, port);
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
                logger.debug("Successfully connected to mail server on IPv6 address: {}, returning result", ipAddress);
                break;
            }

            for (int port : PORTS) {
                if (checkConnection(ipAddress, port)) {
                    logger.debug("Successfully connected to mail server on IPv6 address: {} and port: {}", ipAddress, port);
                    ipv6ConnectionSuccess = true;
                    break;
                }
            }
        }

        if (!ipv4ConnectionSuccess && !ipv6ConnectionSuccess) {
            logger.debug("Could not connect to mail server via IPv4 or IPv6");
            validationContext.markSocketConnectionFailed("Could not connect to mail server via IPv4 or IPv6");
            return false;
        }

        logger.debug("Successfully connected to mail server, returning result");
        validationContext.markSocketConnectionSuccess();
        return true;
    }

    /**
     * Check if a connection can be made to the given IP address and port.
     * @param ip The IP address to connect to
     * @param port The port to connect to
     * @return True if a connection can be made, false otherwise
     */
    private boolean checkConnection(String ip, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), networkConfig.socketTimeout());
            return socket.isConnected();
        } catch (IOException e) {
            logger.debug("Could not connect to mail server on IP address: {} and port: {}, message: {}", ip, port, e.getMessage());
            return false;
        }
    }
}
