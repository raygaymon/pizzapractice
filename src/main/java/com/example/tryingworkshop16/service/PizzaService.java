package com.example.tryingworkshop16.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.tryingworkshop16.model.Delivery;
import com.example.tryingworkshop16.model.Order;
import com.example.tryingworkshop16.model.Pizza;
import com.example.tryingworkshop16.repository.PizzaRepo;

@Service
public class PizzaService {
    //autowire repo
    @Autowired
    private PizzaRepo repo;

    //add pizza names and sizes as final arrays
    //final static attributes that dont change must have static
    public static final String[] PIZZA_NAMES = { "bella", "margherita", "marinara", "spianatacalabrese", "trioformaggio"};
    public static final String[] PIZZA_SIZES = {"sm" , "md", "lg"};

    //add the pizza api as a url
    //set using the @Value annotation
    @Value("${tryingworkshop16.pizza.api.url}")
    private String pizzaUrl;

    //instantiate the sets here beforehand to let them be final
    private final Set<String> pizzaNames;
    private final Set<String> pizzaSizes;
    
    //final arrays become attributes in service constructor//
    public PizzaService(){
        pizzaNames = new HashSet<String>(Arrays.asList(PIZZA_NAMES));
        pizzaSizes = new HashSet<String>(Arrays.asList(PIZZA_SIZES));
    }

    //saveorder, createorder, and cost calculator

    public Optional<Order> getOrderById (String orderId) {

        return repo.getOrder(orderId);
    }

    //the save pizza order is not the one from repo is a fresh one
    //create the order, calc the cost then save it
    public Order createPizzaOrder(Pizza p, Delivery d) {
        Order o = new Order(p, d);
        //generate a random uuid in a dk smlj format, turn it into a string, then substring
        String id = UUID.randomUUID().toString().substring(0,8);
        o.setOrderId(id);
        return o;
    }

    public double calculateCost(Order o) {

        double totalCost = 0;
        //switch statement for size and pizza
        switch (o.getPizzaNameString()) {
            case "bella":
                totalCost += 22;
                break;
            case "margarhita":
                totalCost += 25;
                break;
            case "marinara":
                totalCost += 30;
                break;
            case "spianatacalabrese":
                totalCost += 30;
                break;
            case "trioformaggio":
                totalCost += 30;
                break;
        }
        switch(o.getSize()) {
            case "md":
                totalCost*=1.2;
                break;
            case "lg":
                totalCost*=1.5;
                break;
        }
        //multiply by quantity
        totalCost *= o.getQuantity();
        //if statement for rush

        if (o.getRush()) {
            totalCost += 2;
        }

        o.setTotalCost(totalCost);
        return totalCost;
    }

    //create new order with the pizza and delivery, calculate the cost, then save it to redis
    public Order savePizzaOrder(Pizza p, Delivery d) {
        //code was messing up bcs orderID was not being generated
        Order o = createPizzaOrder(p, d);
        calculateCost(o);
        repo.save(o);
        return o;
        
    }

    //validate the order and flag up errors
    //input field is the pizza since we are validating the pizza and not just hte order
    public List<ObjectError> validatePizzaOrder(Pizza p) {
        List<ObjectError> errors = new LinkedList<ObjectError>();
        //generate a new fielderror
        FieldError fe;
        if (!pizzaNames.contains(p.getPizza())) {
            //first parameter is the name of the error that connects to the html data-th-object
            //2nd parameter is the field that has the error, in this case its the "pizza" attribute of pizza
            //last one is the message that will display when the error appears
            fe = new FieldError ("pizza","pizza", "no such pizza dickhead we don't have %s".formatted(p.getPizza()));
            errors.add(fe);
        }       
        if (!pizzaSizes.contains(p.getSize())) {
            fe = new FieldError ("pizza","size", "no such pizza size dickhead we don't have %s".formatted(p.getSize()));
            errors.add(fe);
        }

        return errors;
    }


    //get the order details
    public Optional<Order> getOrderDetails(String orderId) {

        //build url with uricomponentsbuilder
        String url = UriComponentsBuilder.fromUriString(this.pizzaUrl + orderId).toUriString();

        //instantiate requestentity
        //no need to add any attribute to request entity
        RequestEntity req = RequestEntity.get(url).build();

        //instantiate resttemplate
        RestTemplate rt = new RestTemplate();
        
        //responseenetiy
        ResponseEntity<String> resp = rt.exchange(req, String.class);
        //create an order from the responseentity
        Order o = Order.createJSON(resp.getBody());
        //return if not null
        if (o != null){
            return Optional.empty();
        }
        return Optional.of(o);
    }
}
