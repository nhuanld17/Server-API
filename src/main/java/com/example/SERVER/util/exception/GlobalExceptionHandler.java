package com.example.SERVER.util.exception;

import com.example.SERVER.domain.pojo.RestResponse;
import com.example.SERVER.util.exception.custom.EmailRegisteredException;
import com.example.SERVER.util.exception.custom.IdInvalidException;
import com.example.SERVER.util.exception.custom.JobNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            IdInvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(IdInvalidException idException){
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(idException.getMessage());
        res.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(e.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect((Collectors.toList()));
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
    
    @ExceptionHandler(EmailRegisteredException.class)
    public ResponseEntity<RestResponse<Object>> handleEmailRegisteredException(EmailRegisteredException e){
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(e.getMessage());
        restResponse.setMessage("Email đã được sử dụng cho tài khoản khác");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
    
    // Xử lí khi thực hiện 1 REQUEST nhưng role không hợp lệ
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestResponse<Object>> handleAccessDeniedException(AccessDeniedException e){
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        restResponse.setError(e.getMessage());
        restResponse.setMessage("Access denied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(restResponse);
    }
    
    // Xử lí ngoại lệ bài đăng không tồn tại
    @ExceptionHandler(JobNotExistException.class)
    public ResponseEntity<RestResponse<Object>> handleJobNotExistException(JobNotExistException e){
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(e.getMessage());
        restResponse.setMessage("Bài đăng không tồn tại nhé");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}
