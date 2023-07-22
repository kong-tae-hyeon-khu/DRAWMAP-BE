package com.umc.drawmap.exception.scrap;

import com.umc.drawmap.dto.scrap.ScrapErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ScrapExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ScrapErrorDto handleNoExistUserOrCourse(NoExistUserOrCourseException noExistUserOrCourseException) {
        return new ScrapErrorDto(noExistUserOrCourseException.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ScrapErrorDto handleDupScrapException(DupScrapException ex) {
        return new ScrapErrorDto(ex.getMessage());
    }


}
