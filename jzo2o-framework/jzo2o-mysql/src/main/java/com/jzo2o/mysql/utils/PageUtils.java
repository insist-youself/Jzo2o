package com.jzo2o.mysql.utils;

import com.jzo2o.common.handler.ConvertHandler;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.model.dto.PageQueryDTO;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具
 *
 * @ClassName PageUtils
 * @Author wusongsong
 * @Date 2022/6/27 17:19
 * @Version
 **/
public class PageUtils {

    /**
     * mybatis的分页数据是否为空
     *
     * @param page
     * @return
     */
    public static boolean isEmpty(Page page) {
        return page == null || CollUtils.isEmpty(page.getRecords());
    }

    /**
     * 判断mybatis的分页数据不为空
     *
     * @param page
     * @return
     */
    public static boolean isNotEmpty(Page page) {
        return page != null && !CollUtils.isEmpty(page.getRecords());
    }

    /**
     * 分页数据转换，主要场景是从数据库中查出来的数据转换成DTO，或者VO
     *
     * @param originPage     从数据库查询出来的分页数据
     * @param targetClazz    目标对象class
     * @param convertHandler 数据库对象转换DTO或者VO的转换器，用于转换复杂的数据
     * @param <T>            目标对象类型
     * @param <O>            源对象类型
     * @return 用于传递的分页数据
     */
    public static <O, T> PageResult<T> toPage(Page<O> originPage, Class<T> targetClazz, ConvertHandler<O, T> convertHandler) {
        if (isEmpty(originPage)) {
            return new PageResult<>(0L, 0L, new ArrayList<>());
        }

        return new PageResult<>(originPage.getPages(), originPage.getTotal(),
                BeanUtils.copyToList(originPage.getRecords(), targetClazz, convertHandler));
    }

    /**
     * 分页数据转换返给其他微服务，主要场景是从数据库中查出来的数据转换成DTO，或者VO
     *
     * @param originPage  从数据库查询出来的分页数据
     * @param targetClazz 目标对象class
     * @param <X>         目标对象类型
     * @param <Y>         源对象类型
     * @return 用于传递的分页数据
     */
    public static <X, Y> PageResult<Y> toPage(Page<X> originPage, Class<Y> targetClazz) {
        if (isEmpty(originPage)) {
            return new PageResult<>(0L, 0L, new ArrayList<>());
        }

        return new PageResult<>(originPage.getPages(), originPage.getTotal(),
                BeanUtils.copyToList(originPage.getRecords(), targetClazz));
    }


    /**
     * 将前端传来的分页查询条件转换成数据库的查询page,
     * 如果进行排序必须填写targetClazz
     * 该方法支持简单的数据字段排序，不支持统计排序
     *
     * @param pageQueryDTO 前端传来的查询条件
     * @param <T>          查询数据库po
     * @param targetClazz  校验数据库中是否有需要排序的字段
     * @return mybatis-plus 分页查询page
     */
    public static <T> Page<T> parsePageQuery(PageQueryDTO pageQueryDTO, Class<T> targetClazz) {
        Page<T> page = new Page<>(pageQueryDTO.getPageNo(), pageQueryDTO.getPageSize());
        //是否排序
        if (targetClazz != null) {
            List orderItems = getOrderItems(pageQueryDTO, targetClazz);
            if (CollUtils.isNotEmpty(orderItems)) {
                page.addOrder(orderItems);
            }
        } else {
            //如果没有更新时间按照添加逆序排序
            OrderItem orderItem = new OrderItem();
            orderItem.setAsc(false);
            orderItem.setColumn("id");
            page.addOrder(orderItem);

        }
        return page;
    }

    public static <T> List<OrderItem> getOrderItems(PageQueryDTO pageQueryDTO, Class<T> targetClazz) {
        List<OrderItem> orderItems = new ArrayList<>();
        if (ObjectUtils.isEmpty(pageQueryDTO)) {
            return orderItems;
        }
        // 排序字段1
        if (StringUtils.isNotEmpty(pageQueryDTO.getOrderBy1())) {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(StringUtils.toSymbolCase(pageQueryDTO.getOrderBy1(), '_'));
            orderItem.setAsc(pageQueryDTO.getIsAsc1());
            orderItems.add(orderItem);
        }
        // 排序字段2
        if (StringUtils.isNotEmpty(pageQueryDTO.getOrderBy2())) {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(StringUtils.toSymbolCase(pageQueryDTO.getOrderBy2(), '_'));
            orderItem.setAsc(pageQueryDTO.getIsAsc2());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    public static Long pages(Long total, Long pageSize) {
        return total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

}
