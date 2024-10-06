package org.alwyn.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("link_route")
@NoArgsConstructor
@AllArgsConstructor
public class LinkRouteDO {
    private Long id;

    private String gid;

    private String fullShortLink;
}
