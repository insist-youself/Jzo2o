package com.jzo2o.mysql.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.model.dto.PageQueryDTO;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.StringUtils;

import java.util.List;

/**
 * 底层使用pageHelper实现的分页查询
 *
 */
public class PageHelperUtils {

    /**
     * 分页查询数据
     *
     * @param pageQueryDTO
     * @param condition
     * @return
     */
    public static <T> PageResult<T> selectPage(PageQueryDTO pageQueryDTO, QueryExecutor<T> condition) {

        PageHelper.startPage(pageQueryDTO.getPageNo().intValue(), pageQueryDTO.getPageSize().intValue(), getOrder(pageQueryDTO));
        List<T> data = condition.query();
        if (data instanceof Page) {
            Page page = (Page) data;
            return new PageResult<>(page.getPages() * 1L, page.getTotal(), data);
        }
        long total = CollUtils.size(data);
        long pages = total % pageQueryDTO.getPageSize() == 0 ? total / pageQueryDTO.getPageSize() : total / pageQueryDTO.getPageSize() + 1;
        return new PageResult<>(pages, total, data);
    }

    private static String getOrder(PageQueryDTO pageQueryDTO) {
        if (StringUtils.isEmpty(pageQueryDTO.getOrderBy1()) && StringUtils.isEmpty(pageQueryDTO.getOrderBy2())) {
            return null;
        }
        StringBuffer buffer = new StringBuffer(" ");
        if (StringUtils.isNotEmpty(pageQueryDTO.getOrderBy1())) {
            buffer.append(StringUtils.toSymbolCase(pageQueryDTO.getOrderBy1(), '_'))
                    .append(pageQueryDTO.getIsAsc1() ? " asc " : " desc ");
        }
        if (StringUtils.isNotEmpty(pageQueryDTO.getOrderBy2())) {
            // 两个排序项需要中间添加“，”
            if (StringUtils.isNotEmpty(pageQueryDTO.getOrderBy1())) {
                buffer.append(",");
            }
            buffer.append(StringUtils.toSymbolCase(pageQueryDTO.getOrderBy2(), '_'))
                    .append(pageQueryDTO.getIsAsc2() ? " asc " : " desc ");
        }
        return buffer.toString();
    }

    /**
     * 查询执行器
     *
     * @param <T>
     */
    public interface QueryExecutor<T> {
        List<T> query();
    }
}
