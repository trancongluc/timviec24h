package vn.tcl.timviec24h.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tcl.timviec24h.domain.Company;
import vn.tcl.timviec24h.util.constant.GenderEnum;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private Instant updateAt;
    private UserCompnay userCompnay;
    private UserRole role;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserCompnay {
        private long id;
        private String name;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRole{
        private long id;
        private String name;
    }
}
