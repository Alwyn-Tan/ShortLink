package org.alwyn.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDO  extends BaseDO{

    private Long id;

    private String username;

    private String password;

    private String phone;

    private String mail;

    private Long deleteTime;

}