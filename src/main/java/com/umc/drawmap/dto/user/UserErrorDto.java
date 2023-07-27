package com.umc.drawmap.dto.user;

public class UserErrorDto {
    private String msg;
    public UserErrorDto(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}
