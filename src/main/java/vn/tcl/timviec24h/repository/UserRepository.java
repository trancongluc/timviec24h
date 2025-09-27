package vn.tcl.timviec24h.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tcl.timviec24h.domain.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
