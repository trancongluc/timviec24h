package vn.tcl.timviec24h.service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Job;
import vn.tcl.timviec24h.domain.Resume;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.domain.response.Resume.ResCreateResumeDTO;
import vn.tcl.timviec24h.domain.response.Resume.ResUpdateResumeDTO;
import vn.tcl.timviec24h.domain.response.Resume.ResumeDTO;
import vn.tcl.timviec24h.repository.ResumeRepository;
import vn.tcl.timviec24h.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserService userService;
    private final JobService jobService;
    @Autowired
    FilterBuilder fb;
    @Autowired
    private FilterParser filterParser;
    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;
    public ResumeService(ResumeRepository resumeRepository, UserService userService, JobService jobService) {
        this.resumeRepository = resumeRepository;
        this.userService = userService;
        this.jobService = jobService;

    }

    public Resume createResume(Resume resume) {
//        User user = userService.getUserById(resume.getUser().getId());
//        Job job = jobService.getJobById(resume.getJob().getId());
//        resume.setJob(job);
//        resume.setUser(user);
        return resumeRepository.save(resume);
    }

    public Resume findResumeById(Long id) {
        Optional<Resume> resume = resumeRepository.findById(id);
        if (resume.isPresent()) {
            return resume.get();
        }
        return null;
    }

    public Resume updateResume(Resume resume) {
        Resume existingResume = findResumeById(resume.getId());
        if (existingResume != null) {
            existingResume.setStatus(resume.getStatus());
            return resumeRepository.save(existingResume);
        }
        return existingResume;
    }

    public ResultPaginationDTO findAllResumes(Specification<Resume> spe, Pageable pageable) {
        Page<Resume> page = resumeRepository.findAll(spe, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setTotal(page.getTotalElements());
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        List<ResumeDTO> resumeDTOList = page.getContent().stream()
                .map(item -> this.getResumeDTO(item))
                .toList();

        res.setMeta(meta);
        res.setResult(resumeDTOList);
        return res;
    }

    public ResumeDTO getResumeDTO(Resume resume) {

        ResumeDTO resumeDTO =
                new ResumeDTO(
                        resume.getId(),
                        resume.getEmail(),
                        resume.getUrl(),
                        resume.getStatus(),
                        resume.getCreatedBy(),
                        resume.getUpdatedBy(),
                        resume.getCreatedAt(),
                        resume.getUpdatedAt(),
                        resume.getJob().getCompany().getName(),
                        new ResumeDTO.UserResume(
                                resume.getUser() != null ? resume.getUser().getId() : 0,
                                resume.getUser() != null ? resume.getUser().getName() : null
                        ),
                        new ResumeDTO.JobResume(
                                resume.getJob() != null ? resume.getJob().getId() : 0,
                                resume.getJob() != null ? resume.getJob().getName() : null
                        )
                );

        return resumeDTO;
    }

    public void deleteResume(long id) {
        resumeRepository.deleteById(id);
    }

    public ResumeDTO convertResumeToDTO(Resume resume) {
        ResumeDTO rs = new ResumeDTO();
        ResumeDTO.JobResume jobResume = new ResumeDTO.JobResume();
        ResumeDTO.UserResume userResume = new ResumeDTO.UserResume();
        rs.setId(resume.getId());
        rs.setStatus(resume.getStatus());
        rs.setEmail(resume.getEmail());
        rs.setUrl(resume.getUrl());
        if (resume.getJob() != null) {
            jobResume.setId(resume.getJob().getId());
            jobResume.setName(resume.getJob().getName());
        }
        if (resume.getUser() != null) {
            userResume.setId(resume.getUser().getId());
            userResume.setName(resume.getUser().getName());
        }
        rs.setUpdatedAt(resume.getUpdatedAt());
        rs.setUpdatedBy(resume.getUpdatedBy());
        rs.setCreatedAt(resume.getCreatedAt());
        rs.setCreatedBy(resume.getCreatedBy());
        return rs;
    }

    public ResCreateResumeDTO convertCreateResumeToDTO(Resume resume) {
        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(resume.getId());
        resCreateResumeDTO.setCreatedAt(resume.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(resume.getCreatedBy());
        return resCreateResumeDTO;
    }

    public ResUpdateResumeDTO convertUpdateResumeToDTO(Resume resume) {
        ResUpdateResumeDTO res = new ResUpdateResumeDTO();

        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());
        return res;
    }
    public ResultPaginationDTO fetchResumeByUser(Pageable pageable){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ==true
                ? SecurityUtil.getCurrentUserLogin().get() : "";
        FilterNode node = filterParser.parse("email=''" + email+"'");
        FilterSpecification<Resume>  spec = filterSpecificationConverter.convert(node);
        Page<Resume> resumePage = resumeRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(resumePage.getNumber()+1);
        mt.setPages(resumePage.getTotalPages());
        mt.setTotal(resumePage.getTotalElements());
        mt.setPageSize(resumePage.getSize());
        rs.setMeta(mt);
        rs.setResult(resumePage.getContent());
        return rs;
    }
}
