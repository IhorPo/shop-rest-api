package com.shoprestapi.controller;

import com.shoprestapi.dto.OrderRequest;
import com.shoprestapi.model.Order;
import com.shoprestapi.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Order> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest order) {
        Order createdOrder = service.createOrder(order.getUserId(), order.getProducts());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> edit(@PathVariable Long id,
                                      @RequestBody Order order){
        return new ResponseEntity<>(service.edit(id, order), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Order> editPartial(@PathVariable Long id,
                                             @RequestBody Map<String, Object> updates){
        return new ResponseEntity<>(service.editPartial(id, updates), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        service.delete(id);
        return new ResponseEntity<>("Order was successfully deleted",HttpStatus.OK);
    }

}
