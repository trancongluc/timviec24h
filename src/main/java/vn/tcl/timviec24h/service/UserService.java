package vn.tcl.timviec24h.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.tcl.timviec24h.config.SecurityConfiguration;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.repository.UserRepository;
@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    public User getUserById(long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }
     public User getUserByUsername(String username){
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }
    public User createUser(User newUser){
        return userRepository.save(newUser);
    }
    public User updateUser(long id, User updateUser){
        User existingUser = getUserById(id);
        if(existingUser != null){
            existingUser.setName(updateUser.getName());
            existingUser.setEmail(updateUser.getEmail());
            existingUser.setPassword(updateUser.getPassword());
            return userRepository.save(existingUser);
        }
        return existingUser;
    }
    public void deleteUser(long id){
        userRepository.deleteById(id);
    }
}
