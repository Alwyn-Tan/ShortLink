package org.alwyn.shortlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LinkPageQueryRespDTO {
    private Long id;

    private String domain;

    private String shortLink;

    private String fullShortLink;

    private String originLink;

    private String gid;

    private Integer enableStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
