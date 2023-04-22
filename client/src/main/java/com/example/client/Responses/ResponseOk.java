package com.example.client.Responses;

public class ResponseOk implements Response {
    private String message;

    public ResponseOk() {
        this.message = "";
    }

    public ResponseOk(String message) {
        this.message = message;
    }

}
