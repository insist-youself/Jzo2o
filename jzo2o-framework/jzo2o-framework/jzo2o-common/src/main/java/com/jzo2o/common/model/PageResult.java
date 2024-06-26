package com.jzo2o.common.model;

import cn.hutool.core.collection.CollUtil;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.handler.ConvertHandler;
import com.jzo2o.common.utils.BeanUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApiModel(value = "分页数据消息体", description = "分页数据统一对象")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数", required = true)
    private Long pages = 0L;

    /**
     * 总条数
     */
    @ApiModelProperty(value = "总条数", required = true)
    private Long total;

    /**
     * 数据列表
     */
    @ApiModelProperty(value = "数据列表", required = true)
    private List<T> list = Collections.EMPTY_LIST;


    /**
     * 返回一个分页对象实例
     *
     * @return 分页数据对象
     */
    public static <T> PageResult<T> getInstance() {
        return PageResult.<T>builder().build();
    }


    /**
     * 对items进行类型转换
     *
     * @param origin 源分页数据对象
     * @param clazz  指定items 属性的类型,不能为null
     * @return 目标分页数据对象
     */
    public static <O, T> PageResult<T> of(PageResult<O> origin, Class<T> clazz) {
        return of(origin, clazz, null);
    }

    /**
     * 对items进行类型转换
     *
     * @param origin         源分页数据对象
     * @param clazz          指定items 属性的类型,不能为null
     * @param convertHandler 特殊对象类型转换器，可传null，即不进行特殊处理
     * @return 目标分页数据对象
     */
    public static <O, T> PageResult<T> of(PageResult<O> origin, Class<T> clazz, ConvertHandler<O, T> convertHandler) {
        //断言目标转换类型不为null
        if (null == clazz) {
            throw new CommonException("目标转换类型不能为null!");
        }

        //复制除items外的属性
        PageResult<T> target = PageResult.getInstance();
        BeanUtils.copyProperties(origin, target, "items");

        //items为空，直接返回
        if (CollUtil.isEmpty(origin.getList())) {
            return target;
        }

        //对items进行类型转换
        List<T> targetList = BeanUtils.copyToList(origin.getList(), clazz, convertHandler);
        target.setList(targetList);

        //封装分页数据
        return target;
    }

    /**
     * List{@link List}封装为分页数据对象
     *
     * @param list  item数据
     * @param pages 页尺寸,可不传,数据不为空时默认为1
     * @param total 总条数
     * @return 目标分页数据对象
     */
    public static <T> PageResult<T> of(List<T> list, Integer pageSize, Long pages, Long total) {
        PageResult pageResult = PageResult.<T>builder().pages(Optional.ofNullable(pages).orElse(0L))
                .total(Optional.ofNullable(total).orElse(0L)).build();


        if (CollUtil.isEmpty(list)) {
            pageResult.setList(new ArrayList());
            return pageResult;
        }

        pageResult.setList(list);
        return pageResult;
    }


}
