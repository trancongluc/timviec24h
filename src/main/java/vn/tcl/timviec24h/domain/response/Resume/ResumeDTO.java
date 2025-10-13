package vn.tcl.timviec24h.domain.response.Resume;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tcl.timviec24h.domain.Job;
import vn.tcl.timviec24h.domain.User;
import vn.tcl.timviec24h.util.constant.StatusResumeEnum;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDTO {
    private long id;
    private String email;
    private String url;
    private StatusResumeEnum status;
    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private String companyName;
    private UserResume user;
    private JobResume job;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResume {
        private long id;
        private String name;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResume {
        private long id;
        private String name;
    }
}
