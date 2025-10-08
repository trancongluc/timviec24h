package vn.tcl.timviec24h.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Company;
import vn.tcl.timviec24h.domain.dto.Meta;
import vn.tcl.timviec24h.domain.dto.ResultPaginationDTO;
import vn.tcl.timviec24h.repository.CompanyRepository;
import vn.tcl.timviec24h.util.SecurityUtil;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }
    public ResultPaginationDTO getAllCompanies(Specification<Company> spe, Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(spe,pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPageSize(companyPage.getSize());
        meta.setPage(companyPage.getNumber() +1);
        meta.setTotal(companyPage.getTotalElements());
        meta.setPages(companyPage.getTotalPages());
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(companyPage.getContent());
        return resultPaginationDTO;
    }
    public Company updateCompany(Company company) {
        Optional<Company> getCompanyById = companyRepository.findById(company.getId());
        if(getCompanyById.isPresent()) {
            getCompanyById.get().setName(company.getName());
            getCompanyById.get().setAddress(company.getAddress());
            getCompanyById.get().setDescription(company.getDescription());
            getCompanyById.get().setLogo(company.getLogo());
            return companyRepository.save(getCompanyById.get());
        }
        return null;
    }
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
