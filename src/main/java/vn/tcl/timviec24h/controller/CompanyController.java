package vn.tcl.timviec24h.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tcl.timviec24h.domain.Company;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.service.CompanyService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    @GetMapping("/companies")
    @ApiMessage("Fetch company")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Company> spe, Pageable pageable) {
        return ResponseEntity.ok(companyService.getAllCompanies(spe,pageable));
    }
    @GetMapping("/companies/{id}")
    @ApiMessage("Get company by Id")
    public ResponseEntity<Company> companyById(@PathVariable Long id) throws IdInvalidException {
            if (companyService.fetchCompanyById(id) == null) {
                throw new IdInvalidException("Không tồn tại Company vs id =" + id);
            }
            return ResponseEntity.ok().body(companyService.fetchCompanyById(id));
    }
    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company reqCompany) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(reqCompany));
    }
    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
        return ResponseEntity.ok(companyService.updateCompany(reqCompany));
    }
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@Valid @PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(null);
    }
}
