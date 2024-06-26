package com.jzo2o.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.common.handler.ConvertHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 继承自 hutool 的BeanUtil，增加了bean转换时自定义转换器的功能
 *
 * @author itcast
 */
public class BeanUtils extends BeanUtil {

    /**
     * 将原对象转换成目标对象，对于字段不匹配的字段可以使用转换器处理
     *
     * @param source         原对象
     * @param clazz          目标对象的class
     * @param convertHandler 转换器
     * @param <O>            原对象类型
     * @param <T>            目标对象类型
     * @return 目标对象
     */
    public static <O, T> T copyBean(O source, Class<T> clazz, ConvertHandler<O, T> convertHandler) {
        T target = copyBean(source, clazz);
        if (convertHandler != null) {
            convertHandler.map(source, target);
        }
        return target;
    }

    /**
     * 复制set集合中的属性到目标对象中
     *
     * @param originSet 原始对象集合
     * @param targetType 目标对象类型
     * @param convertHandler 目标对象数据转换器
     * @return
     * @param <O> 原始对象类型
     * @param <T> 目标对象类型
     */
    public static <O, T> List<T> copyToList(Set<O> originSet, Class<T> targetType, ConvertHandler<O, T> convertHandler) {
        if (CollUtils.isEmpty(originSet)) {
            return null;
        }
        return originSet.stream().map(o -> copyBean(o, targetType, convertHandler)).collect(Collectors.toList());
    }

    /**
     * 复制set集合中的属性到目标对象中
     *
     * @param originSet 原始对象集合
     * @param targetType 目标对象类型
     * @return
     * @param <O> 原始对象类型
     * @param <T> 目标对象类型
     */
    public static <O, T> List<T> copyToList(Set<O> originSet, Class<T> targetType) {
        return copyToList(originSet, targetType, (ConvertHandler<O, T>) null);
    }

    /**
     * 复制集合中的Bean属性
     *
     * @param originList     原Bean集合
     * @param targetType     目标Bean类型
     * @param convertHandler 特殊对象类型转换器，可传null，即不进行特殊处理
     * @return 复制后的List
     */
    public static <O, T> List<T> copyToList(List<O> originList, Class<T> targetType, ConvertHandler<O, T> convertHandler) {
        List<T> targetList = cn.hutool.core.bean.BeanUtil.copyToList(originList, targetType);
        //特殊类型转换
        if (CollUtil.isNotEmpty(targetList) && ObjectUtil.isNotEmpty(convertHandler)) {
            for (int i = 0; i < originList.size(); i++) {
                convertHandler.map(originList.get(i), targetList.get(i));
            }
        }
        return targetList;
    }

    /**
     * 将原对象转换成目标对象，对于字段不匹配的字段可以使用转换器处理
     *
     * @param source 原对象
     * @param clazz  目标对象的class
     * @param <R>    原对象类型
     * @param <T>    目标对象类型
     * @return 目标对象
     */
    public static <R, T> T copyBean(R source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        return toBean(source, clazz);
    }

    /**
     * 将列表转换另一种类型的列表
     *
     * @param originList  原列表
     * @param targetClass 目标类型class
     * @param <R>         原列表元素类型
     * @param <T>         目标列表元素类型
     * @return 目标列表
     */
    @Deprecated
    public static <R, T> List<T> copyList(List<R> originList, Class<T> targetClass) {
        if (CollUtils.isEmpty(originList)) {
            return CollUtils.emptyList();
        }
        return copyToList(originList, targetClass);
    }

    @Deprecated
    public static <O, T> List<T> copyList(List<O> list, Class<T> clazz, ConvertHandler<O, T> convertHandler) {
        if (list == null || list.size() == 0) {
            return CollUtils.emptyList();
        }
        return list.stream().map(r -> copyBean(r, clazz, convertHandler)).collect(Collectors.toList());
    }

    public static <T> T copyIgnoreNull(T source,T target, Class<T> clazz){
        //1.源数据和目标数据都转为map
        Map<String, Object> oldData = BeanUtil.beanToMap(target,false,true);
        Map<String, Object> newData = BeanUtil.beanToMap(source,false,true);

        //2.用新数据覆盖旧数据
        oldData.putAll(newData);

        //3.map转为bean返回
        return BeanUtil.mapToBean(oldData, clazz, false, new CopyOptions());
    }
}