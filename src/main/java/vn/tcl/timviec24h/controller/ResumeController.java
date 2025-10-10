package vn.tcl.timviec24h.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tcl.timviec24h.domain.Resume;
import vn.tcl.timviec24h.domain.response.Resume.ResCreateResumeDTO;
import vn.tcl.timviec24h.service.JobService;
import vn.tcl.timviec24h.service.ResumeService;
import vn.tcl.timviec24h.service.UserService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final JobService jobService;
    public ResumeController(ResumeService resumeService, UserService userService, JobService jobService) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.jobService = jobService;
    }
    @PostMapping("/resumes")
    @ApiMessage("Create Resume")
    public ResponseEntity<ResCreateResumeDTO>  createResume(@RequestBody Resume resume) throws IdInvalidException {
        if(userService.getUserById(resume.getUser().getId())==null || jobService.getJobById(resume.getJob().getId())==null){
            throw new IdInvalidException("User or Job not found!");
        }
        Resume cv = resumeService.createResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.convertResumeToDTO(cv));
    }
}
