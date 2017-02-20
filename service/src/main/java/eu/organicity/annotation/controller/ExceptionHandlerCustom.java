package eu.organicity.annotation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
/**
 * Created by etheodor on 07/10/2016.
 */


@ControllerAdvice
@RestController
public class ExceptionHandlerCustom {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = AccessDeniedException.class)
    public String handleBaseException(AccessDeniedException e){
        return e.getMessage();
    }
}