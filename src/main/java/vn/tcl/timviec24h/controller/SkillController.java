package vn.tcl.timviec24h.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tcl.timviec24h.domain.Skill;
import vn.tcl.timviec24h.domain.response.ResultPaginationDTO;
import vn.tcl.timviec24h.service.SkillService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills")
    @ApiMessage("Get All Skill")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(@Filter Specification<Skill> specification, Pageable pageable) {
        return ResponseEntity.ok().body(skillService.getAllSkills(pageable, specification));

    }

    @GetMapping("/skills/{id}")
    @ApiMessage("Get Skill By ID")
    public ResponseEntity<Skill> getSkillById(@PathVariable("id") long id
    ) throws IdInvalidException {
        Skill fetchSkill = skillService.getSkillById(id);
        if (fetchSkill == null) {
            throw new IdInvalidException("Không tồn tại Skill với id = " + id);
        }
        return ResponseEntity.ok().body(fetchSkill);
    }

    @PostMapping("/skills")
    @ApiMessage("Create Skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        if (skillService.getSkillByName(skill.getName()) != null) {
            throw new IdInvalidException("Name đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.createSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update Skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {

        if (skillService.getSkillById(skill.getId()) == null) {
            throw new IdInvalidException("Không tồn tại Skill với id = " + skill.getId());
        }
        if (skillService.getSkillByName(skill.getName()) != null) {
            throw new IdInvalidException("Name đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.getUpdateSkill(skill));
    }
    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete Skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) throws IdInvalidException {
        Skill fetchSkill = skillService.getSkillById(id);
        if (fetchSkill == null) {
            throw new IdInvalidException("Không tìm thấy Skill với id = "+id);
        }
        skillService.deleteSkillById(id);
        return ResponseEntity.ok().body(null);
    }
}
