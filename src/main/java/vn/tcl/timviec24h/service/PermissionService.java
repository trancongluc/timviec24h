package vn.tcl.timviec24h.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Permission;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.repository.PermissionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }
    public boolean existsPermission(Permission permission) {
        return permissionRepository.existsPermissionByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule());
    }
    public Permission findPermissionById(Long id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        if (permission.isPresent()) {
            return permission.get();
        }
        return null;
    }
    public Permission updatePermission(Permission permission) {
        Permission existingPermission = findPermissionById(permission.getId());
        if(existingPermission != null) {
            existingPermission.setApiPath(permission.getApiPath());
            existingPermission.setMethod(permission.getMethod());
            existingPermission.setModule(permission.getModule());
            existingPermission.setName(permission.getName());
            return permissionRepository.save(existingPermission);
        }
        return existingPermission;
    }
    public ResultPaginationDTO getAllPermissions(Specification<Permission> specification, Pageable pageable) {
        Page<Permission> permissions = permissionRepository.findAll(specification, pageable);
        ResultPaginationDTO dto = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPages(permissions.getTotalPages());
        meta.setTotal(permissions.getTotalElements());
        meta.setPage(permissions.getNumber() +1);
        meta.setPageSize(permissions.getSize());
        dto.setMeta(meta);
        dto.setResult(permissions.getContent());
        return dto;
    }
    public void deletePermission(Long id) {
        Permission permission = findPermissionById(id);
        permission.getRoles().forEach(role -> {
            role.getPermissions().remove(permission);
        });
        permissionRepository.delete(permission);
    }
}
