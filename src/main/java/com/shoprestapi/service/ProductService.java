package com.shoprestapi.service;

import com.shoprestapi.dao.ProductRepository;
import com.shoprestapi.exception.ProductNotFoundException;
import com.shoprestapi.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll(){
        return repository.findAll();
    }

    public Product getById(Long id){
        return repository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("No such product")
        );
    }

    public Product save(Product product){
        return repository.save(product);
    }
    public Product edit(Long id, Product product){
        Optional<Product> p = repository.findById(id);
        if(p.isPresent()){
            Product product1 = p.get();
            product1.setName(product.getName());
            product1.setDescription(product.getDescription());
            product1.setPrice(product.getPrice());
            product1.setQuantity(product.getQuantity());
            return repository.save(product1);
        }else{
            throw new ProductNotFoundException("No such product");
        }
    }

    public Product editPartial(Long id, Map<String, Object> updates) {
        Optional<Product> optionalProduct = repository.findById(id);
        if(!optionalProduct.isPresent()){
            throw new ProductNotFoundException("No product with id "+id);
        }
        Product product = optionalProduct.get();

        for(Map.Entry<String, Object> entry: updates.entrySet()){
            String name = entry.getKey();
            Object value = entry.getValue();

            switch (name){
                case "name":
                    product.setName((String) value);
                    break;
                case "description":
                    product.setDescription((String) value);
                    break;
                case "price":
                    product.setPrice((Double) value);
                    break;
                case "quantity":
                    product.setQuantity((Integer) value);
                    break;
                default:
                    throw new NoSuchElementException("Unknown field "+name);
            }
        }
        return repository.save(product);
    }

    public void delete(Long id){
        Product p = repository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("No such product")
        );
        repository.delete(p);
    }

}
