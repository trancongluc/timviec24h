package vn.tcl.timviec24h.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.tcl.timviec24h.util.SecurityUtil;
import vn.tcl.timviec24h.util.constant.GenderEnum;
import vn.tcl.timviec24h.util.constant.StatusResumeEnum;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "resumes")
@Getter
@Setter
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String url;
    @Enumerated(EnumType.STRING)
    private StatusResumeEnum status;
    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
    }

}
