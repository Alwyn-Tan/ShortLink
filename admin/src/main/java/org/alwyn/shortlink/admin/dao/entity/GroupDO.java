package org.alwyn.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class GroupDO extends BaseDO {
    private Long id;
    private String gid;
    private String groupName;
    private String username;
    private Integer sortOrder;
}
