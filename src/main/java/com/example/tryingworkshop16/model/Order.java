package com.example.tryingworkshop16.model;

import java.io.Serializable;
import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Order implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private double totalCost = 0;
    private String orderId;
    private Pizza pizza;
    private Delivery delivery;

    
    public double getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public Pizza getPizza() {
        return pizza;
    }
    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }
    public Delivery getDelivery() {
        return delivery;
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
    public Order(Pizza pizza, Delivery delivery) {
        this.pizza = pizza;
        this.delivery = delivery;
    }
    public Order() {
    }

    public boolean getRush(){return this.getDelivery().isRush();}
    public String getAddress(){return this.getDelivery().getAddress();}
    public String getName(){return this.getDelivery().getName();}
    public String getPhone(){return this.getDelivery().getPhone();}
    public String getComments(){return this.getDelivery().getComments();}
    public String getPizzaNameString(){return this.getPizza().getPizza();}
    public String getSize(){return this.getPizza().getSize();}
    public int getQuantity(){return this.getPizza().getQuantity();}


    public double getPizzaCost(){

        Double pizzaCost = this.getTotalCost();

        this.setTotalCost(pizzaCost);

        if (this.getRush()) {
            this.setTotalCost(pizzaCost + 2);
        }        

        return pizzaCost;
    }

    //methods for all nested classes need to be here


    public static JsonObject toJSON (String json) {
        JsonReader r = (JsonReader) Json.createReader(new StringReader(json));
        return r.readObject();
    }

    //getting order details

    public static Order createJSON(String JSON) {
        JsonObject job = toJSON(JSON);
        Pizza p = Pizza.fromJSON(job);
        Delivery d = Delivery.fromJSON(job);
        Order o = new Order(p , d);
        o.setOrderId(job.getString("orderId"));
        System.out.println(o.getOrderId());
        System.out.println(job.getString("orderId"));
        o.setTotalCost(job.getJsonNumber("total").doubleValue());
        return o;
    }

    public JsonObject toJSON(){
        return Json.createObjectBuilder()
                .add("orderId", this.getOrderId())
                .add("name", this.getName())
                .add("pizza", this.getPizzaNameString())
                .add("address", this.getAddress())
                .add("phone", this.getPhone())
                .add("size", this.getSize())
                .add("quantity", this.getQuantity())
                .add("rush", this.getRush())
                .build();
    }
}
