package com.example.myapplication;

public class RecycleUnit {

    private String username;
   private String date;
    private String materialType;
    private int quantity;
    private String userId;

    public RecycleUnit() {
    }

    public RecycleUnit( String date,String materialType, int quantity , String username,String userId) {
        this.username=username;
        this.date = date;
        this.materialType = materialType;
        this.quantity = quantity;
        this.userId=userId;
    }
    // Add a getter for the userId
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getMaterialType() {
        return materialType;
    }

    public int getQuantity() {
        return quantity;
    }
}
