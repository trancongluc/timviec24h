package vn.tcl.timviec24h.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.service.UserService;
import vn.tcl.timviec24h.service.error.IdInvalidException;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok().body(userService.getAllUser());
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    
    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User newUser) {
       User createUser =  userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser) ;
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable("id") long id, @RequestBody User updateUser) {
        //TODO: process PUT request
        
        return ResponseEntity.ok().body(userService.updateUser(id, updateUser))   ;
    }
   
    // 204 No Content
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if( id > 100){
            throw new IdInvalidException("Id không hợp lệ");
        }

         userService.deleteUser(id);
         return ResponseEntity.noContent().build();
    }
}
