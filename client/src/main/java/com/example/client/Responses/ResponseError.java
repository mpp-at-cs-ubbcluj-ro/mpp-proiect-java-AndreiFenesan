package com.example.client.Responses;


public class ResponseError implements Response {
    private String error;

    public ResponseError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
