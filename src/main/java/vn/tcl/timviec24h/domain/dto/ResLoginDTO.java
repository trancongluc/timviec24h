package vn.tcl.timviec24h.domain.dto;

public class ResLoginDTO {
    private String access_token;

    public ResLoginDTO() {
    }

    public ResLoginDTO(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
