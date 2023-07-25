package com.umc.drawmap.exception.user;


import com.umc.drawmap.dto.scrap.ScrapErrorDto;
import com.umc.drawmap.dto.user.UserErrorDto;
import com.umc.drawmap.exception.scrap.NoExistUserOrCourseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserErrorDto handleDuplicateUserNickNameException(DuplicateUserNickNameException ex) {
        return new UserErrorDto(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserErrorDto handleDuplicateUserEmailException(DuplicateUserEmailException ex) {
        return new UserErrorDto(ex.getMessage());
    }
}
