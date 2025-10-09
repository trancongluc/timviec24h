package vn.tcl.timviec24h.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Company;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.repository.CompanyRepository;
import vn.tcl.timviec24h.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    public CompanyService(CompanyRepository companyRepository,UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }
    public ResultPaginationDTO getAllCompanies(Specification<Company> spe, Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(spe,pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
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
        Optional<Company> company = companyRepository.findById(id);
        if(company.isPresent()) {
            Company existingCompany = company.get();
            List<User> listUser = userRepository.findByCompany(existingCompany);
            userRepository.deleteAll(listUser);
        }
        companyRepository.deleteById(id);
    }
}
