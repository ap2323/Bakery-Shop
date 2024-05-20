package com.bakery;

import java.io.IOException;

public class OperationFailedException extends RuntimeException{
    public OperationFailedException(String message, IOException e){
        super(message, e);
    }
    public OperationFailedException(String message){
        super(message);
    }
}
