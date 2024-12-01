package com.classroom.equipment.common.constant;

public class CommonConstants {
    private CommonConstants() {
        throw new IllegalStateException("Constant class");
    }
    public static final String EMAIL_NOTIFICATION_TEMPLATE = "email-notification";

    public static final String EMAIL_STAFF_ACCOUNT_CREATION_TEMPLATE = "staff-account-creation";

    public static final int OTP_EXPIRATION_TIME = 2; // minutes

    // Status values for general use
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_DELETED = 0;

    // Status values for equipment
    public static final int EQUIPMENT_AVAILABLE = 1;
    public static final int EQUIPMENT_BORROWED = 2;
    public static final int EQUIPMENT_DAMAGED = 3;

    // Status values for borrow orders
    public static final int ORDER_BORROWED = 1;
    public static final int ORDER_RETURNED = 2;
    public static final int ORDER_OVERDUE = 3;

    // Status values for return records
    public static final int RETURN_NORMAL = 1;
    public static final int RETURN_DAMAGED = 2;

    // Status values for notifications
    public static final int NOTIFICATION_PENDING = 1;
    public static final int NOTIFICATION_SENT = 2;
    public static final int NOTIFICATION_FAILED = 3;

    // Borrower types
    public static final int BORROWER_TYPE_STUDENT = 1;
    public static final int BORROWER_TYPE_TEACHER = 2;

    // Other constants
    public static final String SYSTEM_ADMIN = "Admin";
}
