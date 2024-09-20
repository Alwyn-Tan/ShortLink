package org.alwyn.shortlink.admin.dto.req;

import lombok.Data;

@Data
public class UserRegistrationReqDTO {

    private String username;

    private String password;

    private String phone;

    private String mail;

}
