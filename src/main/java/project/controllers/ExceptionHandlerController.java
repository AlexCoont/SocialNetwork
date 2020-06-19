package project.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.dto.error.Error;
import project.dto.error.enums.ErrorDescriptionEnum;
import project.dto.error.enums.ErrorEnum;
import project.handlerExceptions.BadRequestException400;
import project.handlerExceptions.DataConsistencyException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

/** контроллер отлавливает ошибку 400*/

    @ExceptionHandler(BadRequestException400.class)
    public ResponseEntity<?> badRequestException() {
        Error error = new Error(ErrorEnum.INVALID_REQUEST.getError(), ErrorDescriptionEnum.BAD_REQUEST.getError());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
            .stream()
            .map(e -> "field: " + e.getPropertyPath() + "; error: " + e.getMessage())
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"errors\":" + errors + "}");
    }

    @ExceptionHandler(DataConsistencyException.class)
    public ResponseEntity<?> handleDataConsistencyException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> "field: " + e.getField() + "; error: " + e.getDefaultMessage())
            .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"errors\":" + errors + "}");
    }

    /* @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handlerNotFound(Exception e){

        return ResponseEntity.status(404).body("Not Found");
    }*/
}