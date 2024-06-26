package com.jzo2o.canal.constants;

/**
 * @author itcast
 */
public class OperateType {
    /**
     * 新增操作
     */
    public static final String INSERT = "INSERT";
    /**
     * 修改操作
     */
    public static final String UPDATE = "UPDATE";
    /**
     * 删除操作
     */
    public static final String DELETE = "DELETE";

    public static boolean isSave(String operateType) {
        return !(!INSERT.equals(operateType) && !UPDATE.equals(operateType));
    }

    public static boolean canHandle(String operateType) {
        return INSERT.equals(operateType) || UPDATE.equals(operateType) || DELETE.equals(operateType);
    }
}
