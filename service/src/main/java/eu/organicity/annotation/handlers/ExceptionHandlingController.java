package eu.organicity.annotation.handlers;

import eu.organicity.annotation.common.exception.BadArgumentsException;
import eu.organicity.annotation.common.exception.ExistsException;
import eu.organicity.annotation.common.exception.NotFoundException;
import eu.organicity.annotation.common.exception.PermissionException;
import eu.organicity.annotation.common.exception.UnknownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionHandlingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlingController.class);

    // 4XX exceptions

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ExistsException.class)
    public String handle(ExistsException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadArgumentsException.class)
    public String handle(BadArgumentsException e) {
        return e.getMessage();
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = PermissionException.class)
    public String handle(PermissionException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    public String handle(NotFoundException e) {
        return e.getMessage();
    }

    // 5XX exceptions

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = UnknownException.class)
    public String handle(UnknownException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RestException.class)
    public String handle(RestException e) {
        return e.getMessage();
    }


    //REST

    @ExceptionHandler(value = Exception.class)
    public String handle(Exception e) {
        return e.getMessage();
    }


}