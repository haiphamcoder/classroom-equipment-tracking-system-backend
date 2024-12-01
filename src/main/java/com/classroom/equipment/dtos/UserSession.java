package com.classroom.equipment.dtos;

import com.classroom.equipment.common.constant.CommonConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSession {
    private Long userId;
    private String email;
    private String otp;
    private LocalDateTime otpGeneratedTime;
    private boolean waitingOtp;
    private String telegramId;

    public boolean isOtpValid() {
        return otpGeneratedTime.isAfter(LocalDateTime.now().minusMinutes(CommonConstants.OTP_EXPIRATION_TIME));
    }
}
