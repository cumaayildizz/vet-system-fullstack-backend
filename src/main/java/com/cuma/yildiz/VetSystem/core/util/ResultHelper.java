package com.cuma.yildiz.VetSystem.core.util;

import com.cuma.yildiz.VetSystem.core.result.Result;
import com.cuma.yildiz.VetSystem.core.result.ResultData;
import com.cuma.yildiz.VetSystem.dto.response.CursorResponse;
import org.springframework.data.domain.Page;

public class ResultHelper {
    public static <T> ResultData<T> created(T data){
        return new ResultData<>(true , Messages.CREATED , "201" , data);
    }
    public static <T> ResultData<T> badRequest(T data){
        return new ResultData<>(true , Messages.BAD_REQUEST , "400" , data);
    }

    public static <T> ResultData<T> validateError(T data) {
        return new ResultData<>(false , Messages.VALIDATE_ERROR , "400" , data);
    }

    public static <T> ResultData<T> success(T data){
        return new ResultData<>(true , Messages.OK , "200" , data);
    }

    public static <T> ResultData<T> updated(T data){
        return new ResultData<>(true , Messages.UPDATED , "200" , data);
    }


    public static Result Ok(){
        return new Result(true , Messages.OK , "200");
    }

    public static Result deleted(){
        return new Result(true , Messages.DELETED , "200");
    }

    public static Result ExceptionMessage( String message , String code){
        return new Result(false , message, code);
    }

    public static Result notFoundError(String message){
        return new Result(true , message , "404" );
    }

    public static <T> ResultData <CursorResponse<T>> cursorMethod( Page<T> pageData){
        CursorResponse<T> cursor = new CursorResponse<>();
        cursor.setItems(pageData.getContent());
        cursor.setPageNumber(pageData.getNumber());
        cursor.setPageSize(pageData.getSize());
        cursor.setTotalElements(pageData.getTotalElements());

        return ResultHelper.success(cursor);
    }


}