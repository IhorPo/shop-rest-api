package com.shoprestapi.service;

import com.shoprestapi.dao.OrderRepository;
import com.shoprestapi.dao.ProductRepository;
import com.shoprestapi.dao.UserRepository;
import com.shoprestapi.exception.OrderNotFoundException;
import com.shoprestapi.exception.UserNotFoundException;
import com.shoprestapi.model.Order;
import com.shoprestapi.model.Product;
import com.shoprestapi.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

    public Order getById(Long id){
        return orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("No order with id "+id)
        );
    }

    public Order createOrder(Long userId, List<Long> productsIds){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("No user with id "+userId)
        );

        List<Product> products = productRepository.findAllById(productsIds);

        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setOrderDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order edit(Long id, Order order){
        Optional<Order> o = orderRepository.findById(id);
        if(o.isPresent()){
            Order order1 = o.get();
            order1.setUser(order.getUser());
            order1.setProducts(order.getProducts());
            return orderRepository.save(order1);
        }else{
            throw new OrderNotFoundException("No order with id "+id);
        }
    }

    public Order editPartial(Long id, Map<String, Object> updates) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(!optionalOrder.isPresent()){
            throw new OrderNotFoundException("No order with id "+id);
        }
        Order order = optionalOrder.get();
        for(Map.Entry<String, Object> entry: updates.entrySet()){
            String name = entry.getKey();
            Object value = entry.getValue();

            switch (name){
                case "user":
                    order.setUser((User) value);
                    break;
                case "products":
                    order.setProducts((List<Product>) value);
                    break;
                default:
                    throw new NoSuchElementException("Unknown field "+name);
            }
        }
        return orderRepository.save(order);
    }

    public void delete(Long id){
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("No order with id "+id)
        );
        orderRepository.delete(order);
    }
}
