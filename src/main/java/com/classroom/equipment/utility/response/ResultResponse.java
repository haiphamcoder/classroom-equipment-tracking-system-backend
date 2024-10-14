package com.classroom.equipment.utility.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ResultResponse<T> {
    private List<T> data;
    private Integer total;
    private Integer page;
    private Integer limit;

    public ResultResponse() {
        this.data = new ArrayList<>();
        this.total = 0;
    }

    public ResultResponse(List<T> data) {
        this.data = data;
    }

    public static <T> ResultResponse<T> empty() {
        return new ResultResponse<T>(null);
    }
}
