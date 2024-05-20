package com.bakery;

import java.io.IOException;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
    public UserNotFoundException(String message, IOException io){
        super(message, io);
    }
}
