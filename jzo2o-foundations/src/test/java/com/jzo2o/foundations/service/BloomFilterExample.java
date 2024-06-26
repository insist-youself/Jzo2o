package com.jzo2o.foundations.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/10/25 17:10
 */
public class BloomFilterExample {
    public static void main(String[] args) {
        // 创建一个布隆过滤器，预期元素数量为1000，误判率为0.01
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 1000, 0.01);

        // 添加元素到布隆过滤器
        bloomFilter.put("example1");
        bloomFilter.put("example2");
        bloomFilter.put("example3");

        // 测试元素是否在布隆过滤器中
        System.out.println(bloomFilter.mightContain("example1")); // true
        System.out.println(bloomFilter.mightContain("example4")); // false
    }
}
