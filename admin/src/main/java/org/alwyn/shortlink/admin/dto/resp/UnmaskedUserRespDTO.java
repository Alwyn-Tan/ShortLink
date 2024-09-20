package org.alwyn.shortlink.admin.dto.resp;

import lombok.Data;

@Data
public class UnmaskedUserRespDTO {
    private Long id;

    private String username;

    private String phone;

    private String mail;

}
