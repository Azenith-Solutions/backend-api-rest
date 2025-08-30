package com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject;

public class Email {
    private String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email create(String email){
        if(email.trim().isBlank()){
            throw new IllegalArgumentException("Email cannot be empty");
        }else if(email.length() > 100){
            throw new IllegalArgumentException("Email must be less than 100 characters");
        }else if(!email.matches(".*@.*\\..*")){
            throw new IllegalArgumentException("Email must contain @ and domain");
        }
        return new Email(email);
    }

    public String getValue() {
        return value;
    }
}
