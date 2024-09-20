package org.alwyn.shortlink.admin.common.context;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContextDTO {
    @JSONField(name = "id")
    private String userId;

    private String username;

}
