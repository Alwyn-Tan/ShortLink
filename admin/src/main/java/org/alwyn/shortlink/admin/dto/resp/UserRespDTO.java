package org.alwyn.shortlink.admin.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.alwyn.shortlink.admin.common.serialize.PhoneMaskingSerializer;

@Data
public class UserRespDTO {
    private Long id;

    private String username;

    @JsonSerialize(using = PhoneMaskingSerializer.class)
    private String phone;

    private String mail;

}
