package com.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseHandler {

    @Autowired
    private MessageSource messageSource;

    private ResponseHandler() {
    }

    public ResponseEntity<Object> generateResponse(Object response, String messageCode, boolean isSuccess,
                                                   HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", response);
        map.put("message", messageCode);
        map.put("isSuccess", isSuccess);
        map.put("timeStamp", new Date().getTime());
        return new ResponseEntity<>(map, httpStatus);
    }
}