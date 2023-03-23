package pt.ua.tqsenv.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AirQualityServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AirQualityServiceException.class)
    public ResponseEntity<ErrorResponse> handleAirQualityServiceException(AirQualityServiceException ex) {
        String message = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(message);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @AllArgsConstructor
    static class ErrorResponse {
        private String message;
    }
}
