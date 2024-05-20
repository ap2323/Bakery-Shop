package com.bakery;

import java.io.IOException;

public class UserAlreadyFoundException extends RuntimeException{
    public UserAlreadyFoundException(String message){

        super(message);
    }

    public UserAlreadyFoundException(String message, IOException io){
        super(message, io);
    }
}
