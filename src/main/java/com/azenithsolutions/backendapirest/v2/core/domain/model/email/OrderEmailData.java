package com.azenithsolutions.backendapirest.v2.core.domain.model.email;

import java.io.Serializable;

public class OrderEmailData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String orderCode;
    private String customerEmail;
    private String customerName;
    private String orderDetails;
    
    public OrderEmailData() {
    }
    
    public OrderEmailData(String orderCode, String customerEmail, String customerName, String orderDetails) {
        this.orderCode = orderCode;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.orderDetails = orderDetails;
    }
    
    public String getOrderCode() {
        return orderCode;
    }
    
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getOrderDetails() {
        return orderDetails;
    }
    
    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }
}
