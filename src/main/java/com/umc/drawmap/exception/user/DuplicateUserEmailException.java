package com.umc.drawmap.exception.user;

public class DuplicateUserEmailException extends RuntimeException {
    public DuplicateUserEmailException() {
        super("해당 이메일을 가진 유저가 존재합니다.");
    }
}
