package vn.tcl.timviec24h.domain.response.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tcl.timviec24h.domain.Company;
import vn.tcl.timviec24h.domain.Skill;
import vn.tcl.timviec24h.util.constant.LevelEnum;

import java.time.Instant;
import java.util.List;
@Getter
@Setter
public class ResCreateJobDTO {
    private long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant createdAt;
    private String createdBy;
    private List<String> nameSkills;
    private CompanyJob company;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyJob {
        private long id;
        private String name;
    }
}
