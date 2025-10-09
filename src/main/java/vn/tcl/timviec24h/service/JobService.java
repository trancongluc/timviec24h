package vn.tcl.timviec24h.service;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Job;
import vn.tcl.timviec24h.domain.Skill;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.domain.response.job.ResCreateJobDTO;
import vn.tcl.timviec24h.domain.response.job.ResUpdateJobDTO;
import vn.tcl.timviec24h.repository.JobRepository;
import vn.tcl.timviec24h.repository.SkillRepository;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResCreateJobDTO getCreateJob(Job job) {
        if (job.getSkills() != null) {
            List<Long> listId = job.getSkills().stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skills = skillRepository.findByIdIn(listId);
            job.setSkills(skills);
        }
        Job currentJob = jobRepository.save(job);
        ResCreateJobDTO resCreateJobDTO = new ResCreateJobDTO();
        resCreateJobDTO.setId(currentJob.getId());
        resCreateJobDTO.setName(currentJob.getName());
        resCreateJobDTO.setDescription(currentJob.getDescription());
        resCreateJobDTO.setStartDate(currentJob.getStartDate());
        resCreateJobDTO.setEndDate(currentJob.getEndDate());
        resCreateJobDTO.setLevel(currentJob.getLevel());
        resCreateJobDTO.setLocation(currentJob.getLocation());
        resCreateJobDTO.setQuantity(currentJob.getQuantity());
        resCreateJobDTO.setSalary(currentJob.getSalary());
        resCreateJobDTO.setActive(currentJob.isActive());
        resCreateJobDTO.setCreatedAt(currentJob.getCreatedAt());
        resCreateJobDTO.setCreatedBy(currentJob.getCreatedBy());
        if (currentJob.getSkills() != null) {
            List<String> listNameSkill = job.getSkills()
                    .stream().map(item -> item.getName()).toList();
            resCreateJobDTO.setNameSkills(listNameSkill);
        }
        return resCreateJobDTO;
    }

    public Job getJobById(long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()) {
            return optionalJob.get();
        }
        return null;
    }

    public ResUpdateJobDTO updateJob(Job job) {
        Job updateJob = getJobById(job.getId());
//        updateJob.setName(job.getName());
//        updateJob.setDescription(job.getDescription());
//        updateJob.setStartDate(job.getStartDate());
//        updateJob.setEndDate(job.getEndDate());
//        updateJob.setLevel(job.getLevel());
//        updateJob.setLocation(job.getLocation());
//        updateJob.setSalary(job.getSalary());
//        updateJob.setActive(job.isActive());
        updateJob.setUpdatedAt(job.getUpdatedAt());
        updateJob.setUpdatedBy(job.getUpdatedBy());
//        updateJob.setQuantity(job.getQuantity());

        //cách update ngắn gọn
        BeanUtils.copyProperties(job, updateJob,"id","skills","createdAt","createdBy");
        if (job.getSkills() != null && !job.getSkills().isEmpty()) {
            List<Long> listId = job.getSkills().stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skills = skillRepository.findByIdIn(listId);
            updateJob.setSkills(skills);
        }

        Job currentJob = jobRepository.save(updateJob);
        ResUpdateJobDTO res = new ResUpdateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setDescription(currentJob.getDescription());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setLevel(currentJob.getLevel());
        res.setLocation(currentJob.getLocation());
        res.setQuantity(currentJob.getQuantity());
        res.setSalary(currentJob.getSalary());
        res.setActive(currentJob.isActive());
        res.setUpdateAt(currentJob.getUpdatedAt());
        res.setUpdateBy(currentJob.getUpdatedBy());
        if (currentJob.getSkills() != null) {
            List<String> listNameSkill = updateJob.getSkills()
                    .stream().map(item -> item.getName()).toList();
            res.setNameSkills(listNameSkill);
        }
        return res;

    }
    public void deleteJob(long id) {
        jobRepository.deleteById(id);
    }
    public ResultPaginationDTO getAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> listJobs = jobRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setTotal(listJobs.getTotalElements());
        mt.setPageSize(listJobs.getSize());
        mt.setPages(listJobs.getTotalPages());
        mt.setPage(listJobs.getNumber() +1);
        rs.setMeta(mt);
        rs.setResult(listJobs.getContent());
        return rs;
    }
}
