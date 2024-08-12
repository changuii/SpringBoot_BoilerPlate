package dev.changuii.project.exception;

import dev.changuii.project.enums.ErrorCode;

public class CustomException extends RuntimeException{

    private ErrorCode errorCode;


    public CustomException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }

    public String getErrorMessage(){
        return this.errorCode.getErrorDescription();
    }



}
