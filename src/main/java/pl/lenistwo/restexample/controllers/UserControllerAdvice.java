package pl.lenistwo.restexample.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.lenistwo.restexample.exceptions.UserCannotBeNullException;
import pl.lenistwo.restexample.exceptions.UserNotFoundException;

@ControllerAdvice
public class UserControllerAdvice {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserCannotBeNullException.class)
    public String handleUserCannotBeNull(Exception ex) {
        return ex.getMessage();
    }
}
