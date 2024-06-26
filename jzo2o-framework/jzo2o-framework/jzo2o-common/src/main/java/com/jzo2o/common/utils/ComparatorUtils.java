package com.jzo2o.common.utils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * 原始比较器无法处理空值问题，特写此工具类
 */
public class ComparatorUtils {


    /**
     * 比较器，可以将空值元素移动到头部
     *
     * @param keyExtractor
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U extends Comparable<? super U>> Comparator<T> nullToFirstComparing(Function<? super T, ? extends U> keyExtractor) {
        return nullComparing(true, keyExtractor);
    }

    /**
     * 比较器，可以将空值元素移动到尾部
     *
     * @param keyExtractor
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U extends Comparable<? super U>> Comparator<T> nullToLastComparing(Function<? super T, ? extends U> keyExtractor) {
        return nullComparing(false, keyExtractor);
    }

    /**
     * 比较器，可以处理空值元素，将空值元素移动到头部或尾部
     *
     * @param nullFirst 空元素是否排到头部
     * @return 比较器
     */
    public static <T, U extends Comparable<? super U>> Comparator<T> nullComparing(boolean nullFirst,
                                                                                   Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);

        return (Comparator<T> & Serializable)
                (c1, c2) -> {
                    boolean c1NUll = ObjectUtils.isNull(c1) || ObjectUtils.isNull(keyExtractor.apply(c1));
                    boolean c2NUll = ObjectUtils.isNull(c2) || ObjectUtils.isNull(keyExtractor.apply(c2));

                    if (c1NUll) {
                        return c2NUll ? 0 : (nullFirst ? -1 : 1);
                    } else if (c2NUll) {
                        return nullFirst ? 1 : -1;
                    }
                    return keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
                };
    }

    /**
     * 判断comparator是否反转过
     *
     * @param comparator
     * @return
     */
    public static boolean isAsc(Comparator<?> comparator) {
        Objects.requireNonNull(comparator);
        return !comparator.getClass().getName().contains("ReverseComparator");
    }
}
