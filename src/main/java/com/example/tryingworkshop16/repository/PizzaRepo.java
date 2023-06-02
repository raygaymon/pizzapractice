package com.example.tryingworkshop16.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.tryingworkshop16.model.Order;

@Repository
public class PizzaRepo {
    
    //qualifier helps distinguish whic bean to inject when there are multiple similar beans
    @Autowired @Qualifier("pizza")
    private RedisTemplate<String, String> template;


    //for this case no need model
    public void save(Order o) {
        
        //sets the order details as the value with the order id as the key
        System.out.println(o.getOrderId());
        this.template.opsForValue().set(o.getOrderId(), o.toJSON().toString());
    }

    public Optional<Order> getOrder (String orderID) {

        String json = template.opsForValue().get(orderID);
        if (json == null || json.trim().length() < 0) {
            
            return Optional.empty();
        }

        //get order data from redis and creates an order json 
        return Optional.of(Order.createJSON(json));
    }


}
