package vn.tcl.timviec24h.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object Result;
    @Getter
    @Setter
    public static class Meta{
        private int pageSize;
        private int page;
        private long total;
        private int pages;
    }
}
