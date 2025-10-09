package vn.tcl.timviec24h.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tcl.timviec24h.domain.Job;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.domain.response.job.ResCreateJobDTO;
import vn.tcl.timviec24h.domain.response.job.ResUpdateJobDTO;
import vn.tcl.timviec24h.service.JobService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @PostMapping("/jobs")
    @ApiMessage("Create job")
    public ResponseEntity<ResCreateJobDTO> addJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.getCreateJob(job));
    }
    @PutMapping("/jobs")
    @ApiMessage("Update Job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        Job currentJob = jobService.getJobById(job.getId());
        if (currentJob == null) {
            throw new IdInvalidException("Không tìm thấy Job với id = "+job.getId());
        }
        return ResponseEntity.status(HttpStatus.OK).body(jobService.updateJob(job));
    }
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException {
        if (jobService.getJobById(id) == null) {
            throw new IdInvalidException("Không tồn tại Job với id = "+id);
        }
        jobService.deleteJob(id);
        return ResponseEntity.ok().body(null);
    }
    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> jobById(@PathVariable("id") long id) throws IdInvalidException {
        Job currentJob = jobService.getJobById(id);
        if (currentJob== null) {
            throw new IdInvalidException("Không tồn tại Job với id = "+id);
        }
        return ResponseEntity.ok().body(currentJob);
    }
    @GetMapping("/jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spe, Pageable pageable){
        return ResponseEntity.ok().body(jobService.getAllJobs(spe,pageable));
    }
}
