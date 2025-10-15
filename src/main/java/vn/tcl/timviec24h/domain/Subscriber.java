package vn.tcl.timviec24h.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.tcl.timviec24h.util.SecurityUtil;

import java.time.Instant;
import java.util.List;
@Entity
@Table(name = "subscribers")
@Getter
@Setter

public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Name không được để trống")
    private String name;
    @NotBlank(message = "Email không được để trống")
    private String email;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"subscribers"})
    @JoinTable(name = "subscriber_skill", joinColumns = @JoinColumn( name = "subscriber_id")
            ,inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @PrePersist
    public void handleBeforeCreate(){
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"";
    }
    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"";
    }
}
