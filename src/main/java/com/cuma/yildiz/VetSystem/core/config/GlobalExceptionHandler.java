package com.cuma.yildiz.VetSystem.core.config;

import com.cuma.yildiz.VetSystem.core.config.exceptions.NotFoundException;
import com.cuma.yildiz.VetSystem.core.result.Result;
import com.cuma.yildiz.VetSystem.core.result.ResultData;
import com.cuma.yildiz.VetSystem.core.util.ResultHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice //@RestControllerAdvice
public class GlobalExceptionHandler {

    //Kullanim durumunda dogabilecek hatalarda firlatilacak hata
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Result> handleNotFoundException(NotFoundException e){
        return new ResponseEntity<> (ResultHelper.notFoundError(e.getMessage()) , HttpStatus.NOT_FOUND);
    }

    //Valid hatalarini yakalayip kendi istedigimiz sekle gore firlattik
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultData<List<String>>> handleValidationErrors(MethodArgumentNotValidException e){
        List<String> validationErrorList = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        //id bos olamaz // name bos olamaz //maile uygun veri giriniz vb mesajlar verir
        return new ResponseEntity<>(ResultHelper.validateError(validationErrorList), HttpStatus.BAD_REQUEST);

       /* ResultData<List<String>> resultData =
                new ResultData<>(false , Messages.VALIDATE_ERROR , "400" , validationErrorList);
        return new ResponseEntity<>(resultData , HttpStatus.BAD_REQUEST);*/
    }
}