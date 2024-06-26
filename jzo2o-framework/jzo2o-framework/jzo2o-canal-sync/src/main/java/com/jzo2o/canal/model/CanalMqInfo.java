package com.jzo2o.canal.model;

import com.jzo2o.canal.constants.OperateType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author itcast
 */
@Data
public class CanalMqInfo implements Serializable {

    private String database;
    private String table;
    private Boolean isDd1;
    private String type;
    private Long es;
    private Long ts;

    /**
     * 数据列表
     */
    private List<Map<String, Object>> data;

    public boolean getIsSave() {
        return !OperateType.DELETE.equals(type);
    }
}