package com.umc.drawmap.dto.userChallenge;

public class UserChallengeErrorDto {
    private String message;
    public UserChallengeErrorDto(String message) {this.message = message;}

    public String getMessage() {return this.message;}
    public void setMessage(String message) {this.message = message;}
}
