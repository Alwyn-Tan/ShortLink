package org.alwyn.shortlink.project.common.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.Optional;

import static org.alwyn.shortlink.project.common.constant.LinkConstant.DEFAULT_CACHE_VALID_TIME;

public class LinkUtil {

    public static long getLinkCacheValidTime(Date validDate) {
        return Optional.ofNullable(validDate).map(each -> DateUtil.between(new Date(), each, DateUnit.MS)).orElse(DEFAULT_CACHE_VALID_TIME);
    }
}
