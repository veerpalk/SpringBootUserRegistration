package com.adsologist.adsologist.exceptions;

public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message){
            super(message);
        }
}
