package vn.tcl.timviec24h.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tcl.timviec24h.domain.Company;
import vn.tcl.timviec24h.domain.dto.ResultPaginationDTO;
import vn.tcl.timviec24h.repository.CompanyRepository;
import vn.tcl.timviec24h.service.CompanyService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    @GetMapping("/companies")
    @ApiMessage("Fetch company")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@RequestParam("current") Optional<String> currentOptional,
                                                                     @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : null;
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : null;
        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent)-1, Integer.parseInt(sPageSize));
        return ResponseEntity.ok(companyService.getAllCompanies(pageable));
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
    public ResponseEntity<Company> deleteCompany(@Valid @PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
