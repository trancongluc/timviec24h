package vn.tcl.timviec24h.controller;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tcl.timviec24h.domain.Company;
import vn.tcl.timviec24h.domain.Job;
import vn.tcl.timviec24h.domain.Resume;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.domain.response.Resume.ResCreateResumeDTO;
import vn.tcl.timviec24h.domain.response.Resume.ResUpdateResumeDTO;
import vn.tcl.timviec24h.domain.response.Resume.ResumeDTO;
import vn.tcl.timviec24h.service.JobService;
import vn.tcl.timviec24h.service.ResumeService;
import vn.tcl.timviec24h.service.UserService;
import vn.tcl.timviec24h.util.SecurityUtil;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final JobService jobService;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final FilterBuilder filterBuilder;

    public ResumeController(ResumeService resumeService, UserService userService, JobService jobService, FilterSpecificationConverter filterSpecificationConverter, FilterBuilder filterBuilder, FilterBuilder filterBuilder1) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.jobService = jobService;
        this.filterSpecificationConverter = filterSpecificationConverter;
        this.filterBuilder = filterBuilder1;
    }
    @PostMapping("/resumes")
    @ApiMessage("Create Resume")
    public ResponseEntity<ResCreateResumeDTO>  createResume(@RequestBody Resume resume) throws IdInvalidException {

        if(userService.getUserById(resume.getUser().getId())==null || jobService.getJobById(resume.getJob().getId())==null){
            throw new IdInvalidException("User or Job not found!");
        }
        Resume cv = resumeService.createResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.convertCreateResumeToDTO(cv));
    }
    @PutMapping("/resumes")
    @ApiMessage("Update Resume")
    public ResponseEntity<ResUpdateResumeDTO>  updateResume(@RequestBody Resume resume) throws IdInvalidException {
        if (resumeService.findResumeById(resume.getId())==null) {
            throw new IdInvalidException("Resume không tồn tại vơi id = "+resume.getId());
        }
        Resume cv = resumeService.updateResume(resume);
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.convertUpdateResumeToDTO(cv));
    }
    @GetMapping("/resumes")
    @ApiMessage("Get all resume")
    public ResponseEntity<ResultPaginationDTO> allResume(@Filter Specification<Resume> spe, Pageable pageable) {
        List<Long> jobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = userService.getUserByUsername(email);
        if(currentUser != null) {
            Company company = currentUser.getCompany();
            if(company != null) {
                List<Job> jobs = company.getJobs();
                jobIds = jobs.stream().map(x -> x.getId()).toList();
            }
        }
        Specification<Resume> jobInSpe = filterSpecificationConverter.convert(filterBuilder.field("job")
                .in(filterBuilder.input(jobIds)).get());
        Specification<Resume> finalSpec = jobInSpe.and(jobInSpe);

      return   ResponseEntity.ok().body(resumeService.findAllResumes(finalSpec, pageable));
    }
    @GetMapping("/resumes/{id}")
    @ApiMessage("Get resume By Id")
    public ResponseEntity<ResumeDTO> resumeById(@PathVariable("id") Long id) throws IdInvalidException {
        Resume cv = resumeService.findResumeById(id);
        if(cv == null) {
            throw new IdInvalidException("Resume không tồn tại với id = "+id);
        }
        return   ResponseEntity.ok().body(resumeService.getResumeDTO(cv));
    }
    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") Long id) throws IdInvalidException {
        if(resumeService.findResumeById(id) == null) {
            throw new IdInvalidException("Resume không tồn tại với id = "+id);
        }
        resumeService.deleteResume(id);
        return ResponseEntity.ok().body(null);
    }
    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable){
        return ResponseEntity.ok().body(resumeService.fetchResumeByUser(pageable));
    }
}
