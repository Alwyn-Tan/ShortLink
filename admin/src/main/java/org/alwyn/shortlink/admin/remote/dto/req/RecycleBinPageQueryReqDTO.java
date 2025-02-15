package org.alwyn.shortlink.admin.remote.dto.req;

import lombok.Data;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Data
public class RecycleBinPageQueryReqDTO extends Page{
    private List<String> gidList;
}
