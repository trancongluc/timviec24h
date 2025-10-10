package vn.tcl.timviec24h.service;

import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Job;
import vn.tcl.timviec24h.domain.Resume;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.domain.response.Resume.ResCreateResumeDTO;
import vn.tcl.timviec24h.repository.ResumeRepository;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserService userService;
    private final JobService jobService;
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
    public ResCreateResumeDTO convertResumeToDTO(Resume resume) {
        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(resume.getId());
        resCreateResumeDTO.setCreatedAt(resume.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(resume.getCreatedBy());
        return  resCreateResumeDTO;
    }
}
