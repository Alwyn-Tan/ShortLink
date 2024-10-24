package org.alwyn.shortlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.alwyn.shortlink.project.dao.entity.LinkDO;

import java.util.List;

@Data
public class RecycleBinPageQueryReqDTO extends Page<LinkDO> {
    private List<String> gidList;
}
