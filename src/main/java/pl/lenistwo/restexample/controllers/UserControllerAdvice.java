package pl.lenistwo.restexample.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.lenistwo.restexample.exceptions.UserCannotBeNullException;
import pl.lenistwo.restexample.exceptions.UserNotFoundException;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(Exception ex){
        return ex.getMessage();
    }

    @ExceptionHandler(UserCannotBeNullException.class)
    public String handleUserCannotBeNull(Exception ex){
        return ex.getMessage();
    }
}
