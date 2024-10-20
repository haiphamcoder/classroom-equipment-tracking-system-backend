package com.classroom.equipment.config.email.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GmailConfigProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean propertiesDebug;
    private String propertiesMailTransportProtocol;
    private boolean propertiesMailSmtpAuth;
    private boolean propertiesMailSmtpStarttlsEnable;
    private boolean propertiesMailSmtpStarttlsRequired;
    private boolean propertiesMailSmtpSocketFactoryFallback;
    private int propertiesMailSmtpConnectionTimeout;
    private int propertiesMailSmtpTimeout;
    private int propertiesMailSmtpWriteTimeout;
}
