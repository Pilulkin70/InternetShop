package ua.garmash.internetshop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Exception exception, Model model) {
        String errorMessage = (exception != null ? exception.getMessage() : "Unknown error");
        String msg = exception.getCause().getMessage();
        if (msg.equals("could not execute statement")){
            errorMessage = "You cannot delete an item that has already been purchased. Mark it as unavailable.";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}