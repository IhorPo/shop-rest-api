package com.shoprestapi.controller;

import com.shoprestapi.model.User;
import com.shoprestapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        List<User> userList = service.getAll();

        if (userList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        if (user == null || user.getName() == null || user.getName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(service.create(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id,
                         @RequestBody User user){
        return new ResponseEntity<>(service.edit(id,user), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> editUserPartial(@PathVariable Long id,
                                         @RequestBody Map<String, Object> updates){
        return new ResponseEntity<>(service.editPartial(id,updates), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        service.delete(id);
        return new ResponseEntity<>("User was successfully deleted",HttpStatus.OK);
    }
}
