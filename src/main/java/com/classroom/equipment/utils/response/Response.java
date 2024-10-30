package com.classroom.equipment.utils.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class Response<E> {
    public static final int OK_CODE = 200;
    public static final int INTERNAL_ERROR_CODE = 500;
    public static final int CLIENT_ERROR_CODE = 400;
    public static final int NOT_FOUND_INFO = 204;
    public static final int OK_STATUS = 1;
    public static final int ERROR_STATUS = 0;
    public static final String SUCCESS = "SUCCESS";

    private String message;
    private Integer code;
    private Integer status;
    private E result;
}
