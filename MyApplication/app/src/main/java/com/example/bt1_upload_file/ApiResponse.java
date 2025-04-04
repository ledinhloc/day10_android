package com.example.bt1_upload_file;

import java.util.List;
import java.util.Objects;

public class ApiResponse {
    private boolean success;
    private String message;
    private Objects result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Objects getResult() {
        return result;
    }

    public void setResult(Objects result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
