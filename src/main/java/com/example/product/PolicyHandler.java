package com.example.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler {

    @StreamListener(Processor.INPUT)
    public void onEventByString(@Payload String productChanged){
        System.out.println(productChanged);
    }

    /*@StreamListener(Processor.INPUT)
    public void onEventByObject(@Payload ProductChanged productChanged){
        if("ProductChanged".equals(productChanged.getEventType())){
            System.out.println("onEventByObject getEventType:" + productChanged.getEventType());
            System.out.println("onEventByObject getProductName:" + productChanged.getProductName());
        }
    }*/

    @Autowired
    ProductRepository productRepository;

    @StreamListener(Processor.INPUT)
    public void onEventByObject(@Payload OrderPlaced orderPlaced){
        if("OrderPlaced".equals(orderPlaced.getEventType())){
            /*Product p = new Product();
            p.setId(orderPlaced.getOrderId());
            p.setStock(orderPlaced.getQty());
            productRepository.save(p);*/

            Optional<Product> productById = productRepository.findById(orderPlaced.getOrderId());

            Product p = productById.get();
            p.setStock(p.getStock()-orderPlaced.getQty());
            productRepository.save(p);
        }
    }

}
