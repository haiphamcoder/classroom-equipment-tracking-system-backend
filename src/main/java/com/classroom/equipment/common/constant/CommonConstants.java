package com.classroom.equipment.common.constant;

public class CommonConstants {
    private CommonConstants() {
        throw new IllegalStateException("Constant class");
    }
    public static final String EMAIL_NOTIFICATION_TEMPLATE = "email-notification";

    public static final String EMAIL_STAFF_ACCOUNT_CREATION_TEMPLATE = "staff-account-creation";

    public static final int OTP_EXPIRATION_TIME = 2; // minutes

    // Other constants
    public static final String SYSTEM_ADMIN = "Admin";
}
