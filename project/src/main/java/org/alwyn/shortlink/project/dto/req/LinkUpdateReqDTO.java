package org.alwyn.shortlink.project.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LinkUpdateReqDTO {
    private String originLink;

    private String fullShortLink;

    private String gid;

    private Integer validDateType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

}
