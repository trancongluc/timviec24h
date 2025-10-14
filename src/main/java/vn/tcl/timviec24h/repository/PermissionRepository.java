package vn.tcl.timviec24h.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.tcl.timviec24h.domain.Permission;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long>, JpaSpecificationExecutor<Permission> {
    boolean existsPermissionByApiPathAndMethodAndModule(String apiPath, String method, String module);
    List<Permission> findByIdIn(List<Long> permissionIds);
}
