package com.alishkhadka.notificationprocessor.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse {
    String code;
    String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object data;

    protected RestResponse() {
    }

    public static RestResponse success(Object data) {
        RestResponse restResponse = success();
        restResponse.setData(data);
        return restResponse;
    }

    public static RestResponse error(String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode("05");
        restResponse.setMessage(message);
        return restResponse;
    }

    public static RestResponse success() {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode("00");
        restResponse.setMessage("SUCCESS");
        return restResponse;
    }


}
