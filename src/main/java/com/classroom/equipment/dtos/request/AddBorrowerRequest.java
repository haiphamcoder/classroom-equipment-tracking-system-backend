package com.classroom.equipment.dtos.request;

import com.classroom.equipment.common.enums.BorrowerType;
import lombok.Data;

@Data
public class AddBorrowerRequest {
    private String name;
    private BorrowerType type;
    private String email;
    private String telegramId;
}
