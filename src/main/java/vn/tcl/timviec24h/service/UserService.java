package vn.tcl.timviec24h.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.domain.dto.*;
import vn.tcl.timviec24h.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public ResultPaginationDTO getAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = userRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(pageUser.getTotalElements());
        mt.setPages(pageUser.getTotalPages());
        rs.setResult(pageUser.getContent());
        rs.setMeta(mt);
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item ->new ResUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getAge(),
                        item.getGender(),
                        item.getAddress(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()

                )).collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
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
    public boolean getUserByEmail(String email){
        return userRepository.existsUserByEmail(email);

    }
    public User createUser(User newUser){
        return userRepository.save(newUser);
    }
    public User updateUser( User updateUser){
        User existingUser = getUserById(updateUser.getId());
        if(existingUser != null){
            existingUser.setAddress(updateUser.getAddress());
            existingUser.setGender(updateUser.getGender());
            existingUser.setAge(updateUser.getAge());
            existingUser.setName(updateUser.getName());
            return userRepository.save(existingUser);
        }
        return existingUser;
    }
    public void deleteUser(long id){
        userRepository.deleteById(id);
    }
    public ResCreateUserDTO convertToResCreateUserDTO(User user){
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setName(user.getName());
        resCreateUserDTO.setAge(user.getAge());
        resCreateUserDTO.setCreatedAt(user.getCreatedAt());
        resCreateUserDTO.setGender(user.getGender());
        resCreateUserDTO.setAddress(user.getAddress());
        return resCreateUserDTO;
    }
    public ResUserDTO convertToResUserDTO(User user){
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdateAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }
    public ResUpdateUserDTO convertToResUpdateUserDTO(User user){
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdateAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }
}
