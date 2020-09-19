package com.example.orderapp;

public class OrderObject {
    private String user_Id;
    private String product_Id;
    private String quantity;
    private String address;
    private String latitude;
    private String longitude;

    public OrderObject(String user_Id, String product_Id, String quantity, String address, String latitude, String longitude) {
        this.user_Id = user_Id;
        this.product_Id = product_Id;
        this.quantity = quantity;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
