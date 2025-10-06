package vn.tcl.timviec24h.controller;

import java.util.List;
import java.util.Optional;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.tcl.timviec24h.config.SecurityConfiguration;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.domain.dto.ResCreateUserDTO;
import vn.tcl.timviec24h.domain.dto.ResultPaginationDTO;
import vn.tcl.timviec24h.repository.UserRepository;
import vn.tcl.timviec24h.service.UserService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final SecurityConfiguration securityConfiguration;

    public UserController(UserService userService,SecurityConfiguration securityConfiguration) {
        this.userService = userService;
        this.securityConfiguration = securityConfiguration;
    }
    @GetMapping("/users")
    @ApiMessage("Get All User")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> specification,
            Pageable pageable
            ) {

        return ResponseEntity.ok().body(userService.getAllUser(specification,pageable));
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    
    @PostMapping("/users")
    @ApiMessage("Create a new User")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody User newUser) throws IdInvalidException {
        String hasPassword = securityConfiguration.passwordEncoder().encode(newUser.getPassword());
        newUser.setPassword(hasPassword);
        boolean isEmailExist = userService.getUserByEmail(newUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email: "+newUser.getEmail() +" đã tồn tại" );
        }
       User createUser =  userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToResCreateUserDTO(createUser)) ;
    }
    @PutMapping("/users")
    public ResponseEntity<User> updateUserById( @RequestBody User updateUser) {
        //TODO: process PUT request
        return ResponseEntity.ok().body(userService.updateUser(updateUser))   ;
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
