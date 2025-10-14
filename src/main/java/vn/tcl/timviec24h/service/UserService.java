package vn.tcl.timviec24h.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.tcl.timviec24h.domain.Company;
import vn.tcl.timviec24h.domain.Role;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.domain.response.*;
import vn.tcl.timviec24h.repository.CompanyRepository;
import vn.tcl.timviec24h.repository.UserRepository;
import vn.tcl.timviec24h.util.error.IdInvalidException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleService roleService;
    public UserService(UserRepository userRepository,CompanyRepository companyRepository,RoleService roleService) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleService = roleService;
    }
    public ResultPaginationDTO getAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = userRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(pageUser.getTotalElements());
        mt.setPages(pageUser.getTotalPages());
        rs.setResult(pageUser.getContent());
        rs.setMeta(mt);
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> convertToResUserDTO(item)).collect(Collectors.toList());
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
    public User createUser(User newUser)  {
        if(newUser.getCompany() != null){
            Optional<Company> company = companyRepository.findById(newUser.getCompany().getId());
            newUser.setCompany(company.isPresent() ? company.get() : null);
        }
        if(newUser.getRole() != null){
            Role role = roleService.findById(newUser.getRole().getId());
            newUser.setRole(role != null ? role : null);
        }
        return userRepository.save(newUser);
    }
    public User updateUser( User updateUser){
        User existingUser = getUserById(updateUser.getId());

        if(existingUser != null){
            existingUser.setAddress(updateUser.getAddress());
            existingUser.setGender(updateUser.getGender());
            existingUser.setAge(updateUser.getAge());
            existingUser.setName(updateUser.getName());
            if(updateUser.getCompany() != null){
                Optional<Company> comp = companyRepository.findById(updateUser.getCompany().getId());
                existingUser.setCompany(comp.isPresent() ? comp.get() : null);
            }
            if(updateUser.getRole() != null){
                Role role = roleService.findById(updateUser.getRole().getId());
                existingUser.setRole(role);
            }
            return userRepository.save(existingUser);
        }
        return existingUser;
    }
    public void deleteUser(long id){
        userRepository.deleteById(id);
    }
    public ResCreateUserDTO convertToResCreateUserDTO(User user){
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        ResCreateUserDTO.UserCompany userCompany = new ResCreateUserDTO.UserCompany();

        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setName(user.getName());
        resCreateUserDTO.setAge(user.getAge());
        resCreateUserDTO.setCreatedAt(user.getCreatedAt());
        resCreateUserDTO.setGender(user.getGender());
        resCreateUserDTO.setAddress(user.getAddress());
        if(user.getCompany() != null){
            userCompany.setId(user.getCompany().getId());
            userCompany.setName(user.getCompany().getName());
            resCreateUserDTO.setUserCopany(userCompany);
        }
        ResCreateUserDTO.UserRole userRole = new ResCreateUserDTO.UserRole();
        if(user.getRole() != null){
            userRole.setId(user.getRole().getId());
            userRole.setName(user.getRole().getName());
            resCreateUserDTO.setRole(userRole);
        }
        return resCreateUserDTO;
    }
    public ResUserDTO convertToResUserDTO(User user){
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.UserCompnay comp = new ResUserDTO.UserCompnay();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdateAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        if(user.getCompany() != null){
            comp.setId(user.getCompany().getId());
            comp.setName(user.getCompany().getName());
            res.setUserCompnay(comp);
        }
        ResUserDTO.UserRole userRole = new ResUserDTO.UserRole();
        if(user.getRole() != null){
            userRole.setId(user.getRole().getId());
            userRole.setName(user.getRole().getName());
            res.setRole(userRole);
        }
        return res;
    }
    public ResUpdateUserDTO convertToResUpdateUserDTO(User user){
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.UserCompany comp = new ResUpdateUserDTO.UserCompany();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdateAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        if(user.getCompany() != null){
            comp.setId(user.getCompany().getId());
            comp.setName(user.getCompany().getName());
            res.setCompany(comp);
        }
        ResUpdateUserDTO.UserRole userRole = new ResUpdateUserDTO.UserRole();
        if(user.getRole() != null){
            userRole.setId(user.getRole().getId());
            userRole.setName(user.getRole().getName());
            res.setRole(userRole);
        }
        return res;
    }
    public void updateRefreshToken(String token, String email){
        User user = getUserByUsername(email);
        if(user != null){
            user.setRefreshToken(token);
            userRepository.save(user);
        }
    }
    public User getUserByRefreshTokenAndEmail(String token,String email){
        return  userRepository.findByRefreshTokenAndEmail(token,email);
    }
}
