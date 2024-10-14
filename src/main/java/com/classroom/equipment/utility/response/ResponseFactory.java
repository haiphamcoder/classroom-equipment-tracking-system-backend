package com.classroom.equipment.utility.response;

public class ResponseFactory {

    public static Response<Object> getSuccessResponse(String message, Object entity) {
        return new Response<>(message, Response.OK_CODE, Response.OK_STATUS, entity);
    }

    public static Response<Object> getSuccessResponse(String message) {
        return new Response<>(message, Response.OK_CODE, Response.OK_STATUS, ResultResponse.empty());
    }

    public static Response<Object> getServerErrorResponse(String message) {
        return new Response<>(message, Response.INTERNAL_ERROR_CODE, Response.ERROR_STATUS, null);
    }


    public static Response<Object> getClientErrorResponse(String message) {
        return new Response<>(message, Response.CLIENT_ERROR_CODE, Response.ERROR_STATUS, null);
    }

    public static Response<Object> getClientErrorResponse(String message, Integer code) {
        return new Response<>(message, code, Response.ERROR_STATUS, null);
    }
}

