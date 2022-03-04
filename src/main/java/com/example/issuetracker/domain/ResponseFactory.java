package com.example.issuetracker.domain;

import static com.example.issuetracker.domain.ResponseMessageEnum.SUCCESS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseFactory {

    private ResponseFactory() {
    }

    public static ResponseEntity success() {
        Response<Object> response = new Response<>();
        response.setMessage(SUCCESS.getMessage());
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity success(Object data, Class clazz) {
        Response<Object> response = new Response<>();
        response.setMessage(SUCCESS.getMessage());
        response.setData(clazz.cast(data));
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity error(HttpStatus httpStatus, ResponseMessageEnum responseMessageEnum) {
        Response<Object> response = new Response<>();
        response.setMessage(responseMessageEnum.getMessage());
        return new ResponseEntity<>(response, httpStatus);
    }
}
