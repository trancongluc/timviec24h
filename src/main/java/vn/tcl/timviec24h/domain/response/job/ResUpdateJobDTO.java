package vn.tcl.timviec24h.domain.response.job;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import vn.tcl.timviec24h.util.constant.LevelEnum;

import java.time.Instant;
import java.util.List;
@Getter
@Setter
public class ResUpdateJobDTO {
    private long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant updateAt;
    private String updateBy;
    private List<String> nameSkills;
}
