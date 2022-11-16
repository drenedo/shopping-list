package me.renedo.shopping.app.http.error;

import org.jooq.exception.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.ServletException;
import me.renedo.shopping.shared.uuid.NotAcceptableUUIDException;

@ControllerAdvice
public class ErrorHandler {

    @ResponseStatus(value= HttpStatus.CONFLICT, reason="Data integrity violation")
    @ExceptionHandler(DataAccessException.class)
    public void dataAccessException() {
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="internal error")
    @ExceptionHandler(ServletException.class)
    public void servletException() {
    }

    @ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason="UUID exception not valid")
    @ExceptionHandler(NotAcceptableUUIDException.class)
    public void uuidException() {
    }
}
