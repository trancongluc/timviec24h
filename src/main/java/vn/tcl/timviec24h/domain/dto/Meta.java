package vn.tcl.timviec24h.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int pageSize;
    private int page;
    private long total;
    private int pages;
}
