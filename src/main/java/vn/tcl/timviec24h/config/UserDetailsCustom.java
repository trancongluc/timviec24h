package vn.tcl.timviec24h.config;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.service.UserService;

@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private final UserService userService;
    public UserDetailsCustom(UserService userService) {
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        User user = userService.getUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Username/password không hợp lệ");
        }
        return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(), 
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
    
}
