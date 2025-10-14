package vn.tcl.timviec24h.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Job;
import vn.tcl.timviec24h.domain.Permission;
import vn.tcl.timviec24h.domain.Role;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.repository.JobRepository;
import vn.tcl.timviec24h.repository.PermissionRepository;
import vn.tcl.timviec24h.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    public RoleService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }
    public Role createRole(Role role) {
        if(role.getPermissions()!=null) {
            List<Long> ids = role.getPermissions().stream()
                    .map(x-> x.getId()).toList();
            List<Permission> permissions = permissionRepository.findByIdIn(ids);
            role.setPermissions(permissions);
        }

        return roleRepository.save(role);
    }
    public boolean existsNameRole(String nameRole) {
        return roleRepository.existsRoleByName(nameRole);
    }
    public Role findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if(role.isPresent()) {
            return role.get();

        }
        return null;
    }
    public Role updateRole(Role role) {
        Role currentRole = findById(role.getId());
        if(currentRole!=null) {
            currentRole.setName(role.getName());
            currentRole.setDescription(role.getDescription());
            currentRole.setActive(role.isActive());
            List<Long> idPermission = role.getPermissions().stream()
                    .map(x-> x.getId()).toList();
            List<Permission> permissions = permissionRepository.findByIdIn(idPermission);
            currentRole.setPermissions(permissions);
            roleRepository.save(currentRole);
        }
        return currentRole;
    }
    public ResultPaginationDTO getAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(spec, pageable);
        ResultPaginationDTO dto = new ResultPaginationDTO();
        ResultPaginationDTO.Meta  meta = new ResultPaginationDTO.Meta();
        meta.setTotal(roles.getTotalElements());
        meta.setPage(pageable.getPageNumber() +1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(roles.getTotalPages());
        dto.setMeta(meta);
        dto.setResult(roles.getContent());
        return dto;
    }
    public void deleteRole(Long id) {

        roleRepository.deleteById(id);
    }
}
