package com.classroom.equipment.common.constant;

public class CommonConstants {
    private CommonConstants() {
        throw new IllegalStateException("Constant class");
    }
    public static final String EMAIL_NOTIFICATION_TEMPLATE = "email-notification";

    public static final int OTP_EXPIRATION_TIME = 2; // minutes
}
