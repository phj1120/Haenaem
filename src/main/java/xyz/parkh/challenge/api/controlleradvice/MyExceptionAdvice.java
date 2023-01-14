package xyz.parkh.challenge.api.controlleradvice;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.parkh.challenge.api.Result;
import xyz.parkh.challenge.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@RequiredArgsConstructor
public class MyExceptionAdvice {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public Result noSuchElementException(NoSuchElementException e) {
        return Result.builder().error(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result illegalArgumentException(IllegalArgumentException e) {
        return Result.builder().error(e.getMessage()).build();
    }

    // runtimeException 아님
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(IllegalAccessException.class)
//    public Result IllegalAccessException(IllegalAccessException e) {
//        return Result.builder().error(e.getMessage()).build();
//    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result bindingException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<BindingError> bindingErrorList = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            BindingError bindingError = new BindingError(field, message);
            bindingErrorList.add(bindingError);
        }
        return Result.builder().error(bindingErrorList).build();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result noUniqueValueException() {
        return Result.builder().error(ErrorCode.NO_UNIQUE_VALUE.getMessage()).build();
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public Result expiredJwtException() {
        return Result.builder().error(ErrorCode.EXPIRED_JWT.getMessage()).build();
    }
}
