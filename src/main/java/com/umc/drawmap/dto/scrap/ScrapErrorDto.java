package com.umc.drawmap.dto.scrap;

public class ScrapErrorDto {
    private String msg;
    public ScrapErrorDto(String msg) {
        this.msg = msg;
    }
    public String getMessage() {return this.msg;}
}
