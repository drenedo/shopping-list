package me.renedo.app.http.error;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.exception.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.ServletException;
import me.renedo.shared.exception.NotAcceptableException;
import me.renedo.shared.uuid.NotAcceptableUUIDException;

@ControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LogManager.getLogger();

    @ResponseStatus(value= HttpStatus.CONFLICT, reason="Data integrity violation")
    @ExceptionHandler(DataAccessException.class)
    public void dataAccessException(DataAccessException e) {
        logger.error("DataAccessException", e);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="internal error")
    @ExceptionHandler(ServletException.class)
    public void servletException(ServletException e) {
        logger.error("ServletException", e);
    }

    @ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason="UUID exception not valid")
    @ExceptionHandler(NotAcceptableUUIDException.class)
    public void uuidException(NotAcceptableUUIDException e) {
        logger.error("NotAcceptableUUIDException", e);
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NotAcceptableException.class)
    public ResponseEntity<Object> notAcceptableException(NotAcceptableException nae){
        logger.error("NotAcceptableException", nae);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", nae.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }
}
