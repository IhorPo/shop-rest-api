package com.shoprestapi.service;

import com.shoprestapi.dao.UserRepository;
import com.shoprestapi.exception.UserNotFoundException;
import com.shoprestapi.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAll(){
        return repository.findAll();
    }

    public User getById(Long id){
        return repository.findById(id).orElseThrow(
                () -> new UserNotFoundException("No user with id "+id)
        );
    }

    public User create(User user){
        User user1 = repository.findById(user.getId()).orElseThrow(
                () -> new UserNotFoundException("No user with id "+user.getId())
        );
        return repository.save(user1);
    }
    public User edit(Long id, User user){
        Optional<User> u = repository.findById(id);
        if(u.isPresent()){
            User user1 = u.get();
            user1.setName(user.getName());
            user1.setEmail(user.getEmail());
            user1.setPassword(user.getPassword());
            return repository.save(user1);
        }else{
            throw new UserNotFoundException("No user with id "+id);
        }
    }

    public User editPartial(Long id, Map<String, Object> updates) {
        Optional<User> userOptional = repository.findById(id);
        if(!userOptional.isPresent()){
            throw new UserNotFoundException("No user with id "+id);
        }

        User user = userOptional.get();

        for(Map.Entry<String, Object> entry: updates.entrySet()){
            String name = entry.getKey();
            Object value = entry.getValue();

            switch (name){
                case "name":
                    user.setName((String) value);
                    break;
                case "password":
                    user.setPassword((String) value);
                    break;
                case "email":
                    user.setEmail((String) value);
                    break;
                default:
                    throw new NoSuchElementException("Unknown field "+ name);
            }
        }

        return repository.save(user);
    }

    public void delete(Long id){
        User user = repository.findById(id).orElseThrow(
                () -> new UserNotFoundException("No user with id "+id)
        );
        repository.delete(user);
    }
}
