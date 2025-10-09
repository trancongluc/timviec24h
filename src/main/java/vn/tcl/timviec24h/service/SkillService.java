package vn.tcl.timviec24h.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Skill;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.repository.SkillRepository;
import vn.tcl.timviec24h.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }
    public ResultPaginationDTO getAllSkills(Pageable pageable, Specification<Skill> spec) {
        Page<Skill> pageSkill = skillRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageSkill.getNumber()+1);
        meta.setPages(pageSkill.getTotalPages());
        meta.setTotal(pageSkill.getTotalElements());
        meta.setPageSize(pageSkill.getSize());
        res.setMeta(meta);
        res.setResult(pageSkill.getContent());
        return res;
    }
    public Skill getSkillByName(String skillName){
        return skillRepository.findByName(skillName);
    }
    public Skill createSkill(Skill newSkill) {
        return skillRepository.save(newSkill);
    }
    public Skill getSkillById(long id){
        Optional<Skill> skill = skillRepository.findById(id);
        if(skill.isPresent()){
            return skill.get();
        }
        return null;
    }
    public Skill getUpdateSkill(Skill updateSkill) {
        Skill currentSkill = getSkillById(updateSkill.getId());
        if(currentSkill != null){
            currentSkill.setName(updateSkill.getName());
            return skillRepository.save(currentSkill);
        }
        return currentSkill;
    }
    public void deleteSkillById(long id){
        Skill currentSkill = getSkillById(id);
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));
        skillRepository.delete(currentSkill);
    }
}
