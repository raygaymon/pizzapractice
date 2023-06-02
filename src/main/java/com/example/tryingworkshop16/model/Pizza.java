package com.example.tryingworkshop16.model;

import java.io.Serializable;

import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotNull;

public class Pizza implements Serializable{
    
    @NotNull(message ="pick a pizza asshole")
    private String pizza;

    @NotNull(message = "you want invisible pizza isit")
    private String size;

    @NotNull(message = "dw pizza come here for fuck")
    private int quantity;

    public String getPizza() {
        return pizza;
    }
    public void setPizza(String pizza) {
        this.pizza = pizza;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public static Pizza fromJSON(JsonObject o) {
        Pizza p = new Pizza();
        p.setPizza(o.getString("pizza"));
        p.setSize(o.getString("size"));
        p.setQuantity(o.getJsonNumber("quantity").intValue());

        return p;
    }
}
