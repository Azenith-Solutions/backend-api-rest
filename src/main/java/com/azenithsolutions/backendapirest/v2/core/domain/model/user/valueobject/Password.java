package com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject;

public class Password {
    private String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password create(String password){
        if(password.trim().isBlank()){
            throw new IllegalArgumentException("Password cannot be empty");
        }else if(password.length() < 8 || password.length() > 100){
            throw new IllegalArgumentException("Password has to be between 8-100 characters");
        }else if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+={}:;\"'<>,.?~`-])[A-Za-z\\d!@#$%^&*()_+={}:;\"'<>,.?~`-]{8,}$")){
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long.");
        }
        return new Password(password);
    }
}
