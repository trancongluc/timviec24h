package vn.tcl.timviec24h.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tcl.timviec24h.domain.Permission;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.repository.PermissionRepository;
import vn.tcl.timviec24h.service.PermissionService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create permission")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) throws IdInvalidException {
        if (permissionService.existsPermission(permission)) {
            throw new IdInvalidException("Permission đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.createPermission(permission));
    }
    @PutMapping("/permissions")
    @ApiMessage("Update permission")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) throws IdInvalidException {
        if(permissionService.findPermissionById(permission.getId())==null){
            throw new IdInvalidException("Không tìm thấy Permission với id = "+permission.getId());
        }
        //check exist by module, apiPath and method
        if (permissionService.existsPermission(permission)) {
            if(permissionService.existNamePermission(permission)){
                throw new IdInvalidException("Permission đã tồn tại");
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(permissionService.updatePermission(permission));
    }
    @GetMapping("/permissions")
    @ApiMessage("Get All Permission")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(@Filter Specification<Permission> spec, Pageable pageable) {
        return ResponseEntity.ok().body(permissionService.getAllPermissions(spec, pageable));
    }
    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete Permission")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) throws IdInvalidException {
        if(permissionService.findPermissionById(id)==null){
            throw new IdInvalidException("Khoong tìm thấy Permission với id = "+id);
        }
        permissionService.deletePermission(id);
        return ResponseEntity.ok().body(null);
    }
}
