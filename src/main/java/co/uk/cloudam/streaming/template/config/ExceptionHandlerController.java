package co.uk.cloudam.streaming.template.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import lombok.extern.apachecommons.CommonsLog;

@RestControllerAdvice
@CommonsLog
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(HttpServletRequest req,
                                                          MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorHtml = String.join("<br>", errors.values());

        return Map.of("safeErrorsHtml", errorHtml,
                      "rawErrors", errors,
                      "errorMessage", "Validation Errors : " + errorHtml,
                      "url", req.getRequestURL(),
                      "time", LocalDateTime.now(),
                      "type", ex.getClass().toString());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> validationValidationException(HttpServletRequest req, Exception ex) {
        return new ResponseEntity<>(Map.of("safeErrorsHtml", ex.getMessage(),
                                           "errorMessage", ex.getMessage(),
                                           "url", req.getRequestURL(),
                                           "time", LocalDateTime.now(),
                                           "type", ex.getClass().toString()),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
                       ResourceAccessException.class})
    public ResponseEntity<Map<String, Object>> validation(HttpServletRequest req, Exception ex) {
        return new ResponseEntity<>(Map.of("safeErrorsHtml", "Resource error",
                                           "errorMessage", ex.getMessage(),
                                           "url", req.getRequestURL(),
                                           "time", LocalDateTime.now(),
                                           "type", ex.getClass().toString()),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, Object>> handleErrorMisMatch(HttpServletRequest req,
                                                                   Exception ex) {
        log.error("Request: " + req.getRequestURL() + " raised ", ex);

        return new ResponseEntity<>(Map.of("safeErrorsHtml", "General error",
                                           "errorMessage", ex.getMessage(),
                                           "url", req.getRequestURL(),
                                           "time", LocalDateTime.now(),
                                           "type", ex.getClass().toString()),
                                    HttpStatus.BAD_REQUEST);
    }
}
