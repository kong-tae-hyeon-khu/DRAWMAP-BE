package com.umc.drawmap.exception.userChallenge;

import com.nimbusds.oauth2.sdk.ErrorResponse;
import com.umc.drawmap.dto.userChallenge.UserChallengeErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserChallengeExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 해당 유저가 존재하지 않을 때 발생하는 에러.
    public UserChallengeErrorDto handleNoExsistUserException(NoExistUserException ex) {
        return new UserChallengeErrorDto(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 해당 도전코스가 존재하지 않을 때 발생하는 에러.
    public UserChallengeErrorDto handleNoExistChallengeException(NoExistChallengeException ex) {
        return new UserChallengeErrorDto(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 이미 해당 유저가 도전 인증을 완료하였을 때 발생하는 에러.
    public UserChallengeErrorDto handleDuplicateUserChallengeException(DuplicateUserChallengeException ex) {
        return new UserChallengeErrorDto(ex.getMessage());
    }

}