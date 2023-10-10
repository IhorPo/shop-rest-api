package com.shoprestapi.controller;

import com.shoprestapi.model.Product;
import com.shoprestapi.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        return new ResponseEntity<>(service.save(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable Long id,
                                               @RequestBody Product product){
        return new ResponseEntity<>(service.edit(id,product),HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> editProductPartial(@PathVariable Long id,
                                                      @RequestBody Map<String, Object> updates){
        return new ResponseEntity<>(service.editPartial(id, updates), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        service.delete(id);
        return new ResponseEntity<>("Product was successfully deleted!", HttpStatus.OK);
    }
}
