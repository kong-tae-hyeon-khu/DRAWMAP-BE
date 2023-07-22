package com.umc.drawmap.exception.scrap;

import com.umc.drawmap.exception.userChallenge.NoExistUserException;

public class NoExistScrapException extends RuntimeException {
    public NoExistScrapException() {
        super("해당 스크랩이 존재하지 않습니다.");
    }
}
