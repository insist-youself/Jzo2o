package com.jzo2o.redis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyncMessage <T>{
    private String key;
    private Object value;
    private T data;
}
