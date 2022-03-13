package gp.developer.api.task_3.exception;

import gp.developer.api.task_3.security.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeveloperNotFoundException.class)
    public ResponseEntity handlerDeveloperNotFoundException(DeveloperNotFoundException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeveloperMailNotUniqueException.class)
    public ResponseEntity handlerDeveloperNotFoundException(DeveloperMailNotUniqueException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NameValidateException.class)
    public ResponseEntity handlerDeveloperNotFoundException(NameValidateException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeveloperUpdateBadRequestException.class)
    public ResponseEntity handlerDeveloperNotFoundException(DeveloperUpdateBadRequestException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticateAuthorizationException.class)
    public ResponseEntity handlerDeveloperNotFoundException(AuthenticateAuthorizationException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
//    @ExceptionHandler(JwtAuthenticationException.class)
//    public ResponseEntity handlerDeveloperNotFoundException(JwtAuthenticationException e){
//        return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
//    }
}
