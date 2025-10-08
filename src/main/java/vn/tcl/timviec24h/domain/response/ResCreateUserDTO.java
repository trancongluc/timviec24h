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

}
