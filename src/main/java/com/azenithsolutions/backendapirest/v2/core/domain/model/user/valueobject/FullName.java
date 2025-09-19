package com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject;

public class FullName {
    private String value;

    private FullName(String value) {
        this.value = value;
    }

    public static FullName create(String fullName){
        if(fullName.trim().isBlank()){
            throw new IllegalArgumentException("Full name cannot be empty");
        }else if(fullName.length() > 100){
            throw new IllegalArgumentException("Full name must be less than 100 characters");
        }else if(!fullName.matches("^[a-zA-Z\\s]+$")){
            throw new IllegalArgumentException("Only letters and spaces are allowed");
        }
        return new FullName(fullName);
    }

    public String getValue() {
        return value;
    }
}
