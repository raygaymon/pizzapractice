package com.example.tryingworkshop16.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.tryingworkshop16.model.Delivery;
import com.example.tryingworkshop16.model.Order;
import com.example.tryingworkshop16.model.Pizza;
import com.example.tryingworkshop16.service.PizzaService;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PizzaController {
    
    @Autowired
    private PizzaService ps;

    @GetMapping(path={"/", "/index.html"})
    public String homepage (Model m, HttpSession session){


        session.invalidate();

        //create a new pizza bcs page is to choose the pizza
        //create a new instance of the object that the html is meant to create
        m.addAttribute("pizza", new Pizza());
        return "homepage";
    }

    @PostMapping(path="/pizza")
    public String postPizza(Model m, HttpSession session, @Valid Pizza p, BindingResult br) {

        if (br.hasErrors()){
            return "homepage";
        }

        //add pizza validation errors to binding error
        List<ObjectError> errors = ps.validatePizzaOrder(p); //correct
        //use .isEmpty to check if lists are empty not null
        if (!errors.isEmpty()) {
            for (ObjectError oe : errors) {
                br.addError(oe);
            }
            return "homepage";
        }
        
        //set attributes to session and model
        //session is setattribute not add
        session.setAttribute("pizza", p);

        //create new delivery bcs the next page is to choose delivery options
        m.addAttribute("delivery", new Delivery() );

        //return to the delivery page

        return "fucker";
    }

    //postmap from ordering page to the check order page
    @PostMapping(path="/pizza/fucker")
    public String postDelivery(Model m, @Valid Delivery d, BindingResult br, HttpSession session ) {

        if (br.hasErrors()) {
            return "fucker";
        }

        //get the pizza attribute from the session
        Pizza p = (Pizza) session.getAttribute("pizza");
        //use the pizza from the session and delivery from current page and create new order object
        
        Order o = ps.savePizzaOrder(p, d);
        System.out.println(o.getOrderId());
        //add order to model to send to html
        m.addAttribute("order", o);

        return "taint";
    }

    
    //getmapping to show order page
    @GetMapping(path="/pizza/taint/{orderId}")
    //orderid needs pathvariable to send it into the function
    public String orderDetails(Model m, HttpSession session, @PathVariable String orderId) {

        Optional<Order> finalOrder = ps.getOrderById(orderId);
        //get the order dont just put the order
        m.addAttribute("order", finalOrder.get());

        return "taint";
    }
    //retrive order details
    //add attribute to model

}
