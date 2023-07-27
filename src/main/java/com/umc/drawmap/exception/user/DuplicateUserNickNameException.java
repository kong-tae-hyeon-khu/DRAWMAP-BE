package com.umc.drawmap.exception.user;

public class DuplicateUserNickNameException extends RuntimeException {

    public DuplicateUserNickNameException() {
        super("해당 닉네임을 가진 유저가 이미 존재합니다.");
    }
}
