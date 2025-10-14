package vn.tcl.timviec24h.domain.response;

import lombok.Getter;
import lombok.Setter;
import vn.tcl.timviec24h.util.constant.GenderEnum;

import java.time.Instant;
@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private UserCompany userCopany;
    private UserRole role;
    @Getter
    @Setter
    public static class  UserCompany{
        private long id;
        private String name;
    }

    @Getter
    @Setter
    public static class UserRole{
        private long id;
        private String name;
    }
}
