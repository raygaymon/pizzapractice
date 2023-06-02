package com.example.tryingworkshop16.model;

import java.io.Serializable;

import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Delivery implements Serializable {

    @NotNull(message = "deliver to who sia fuck you")
    //size is only for string length
    //min only for data value and not the type
    @Size(min=3, message="what kind of fucking name is that")
    private String name;

    @NotNull(message = "You homeless fuck")
    //notempty only for collections, arrays etc.
    @NotEmpty(message = "You homeless fuck")
    private String address;

    @NotNull(message = " how to find you la")
    private String phone;
    private boolean rush = false;
    private String comments;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public boolean isRush() {
        return rush;
    }
    public void setRush(boolean rush) {
        this.rush = rush;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public static Delivery fromJSON(JsonObject o) {
        Delivery d = new Delivery();
        d.setAddress(o.getString("address"));
        d.setName(o.getString("name"));
        d.setPhone(o.getString("phone"));
        d.setComments(o.getString("comments"));
        d.setRush(o.getBoolean("rush"));

        return d;
    }
    
}
