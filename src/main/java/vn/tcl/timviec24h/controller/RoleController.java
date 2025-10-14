package vn.tcl.timviec24h.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.tcl.timviec24h.domain.Role;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.service.RoleService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @PostMapping("/roles")
    @ApiMessage("Create Role")
    public ResponseEntity<Role> createRole(@RequestBody Role role) throws IdInvalidException {
        if(roleService.existsNameRole(role.getName())) {
            throw new IdInvalidException("Name role đã tồn tại !");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(role));
    }
    @PutMapping("/roles")
    @ApiMessage("Update roles")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws IdInvalidException {
        if(roleService.findById(role.getId())==null) {
            throw new IdInvalidException("Không tìm thấy role với id = "+role.getId());
        }
        return ResponseEntity.ok().body(roleService.updateRole(role));
    }
    @GetMapping("/roles")
    @ApiMessage("Get all roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec, Pageable pageable) {
            return ResponseEntity.ok().body(roleService.getAllRoles(spec, pageable));
    }
    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete role")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws IdInvalidException {
        if(roleService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy role với id = "+id);
        }
        roleService.deleteRole(id);
        return ResponseEntity.ok().body(null);
    }
}
