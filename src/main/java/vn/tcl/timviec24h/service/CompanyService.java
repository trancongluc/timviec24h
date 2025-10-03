package vn.tcl.timviec24h.service;

import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Company;
import vn.tcl.timviec24h.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }
}
