package com.jzo2o.knife4j.response;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SwaggerTransformServletResponse {

    private static final String voidName = String.format("R«%s»", "Void");
    private static final String rName = "R";
    private static final String DEFINITIONS = "definitions";
    private static final String PATHS = "paths";
    private static final String RESPONSES = "responses";
    private static final String SCHEMA = "schema";
    private static final String REF = "$ref";
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private static final String ORIGINAL_REF = "originalRef";
    private static final String REF_PREFIX = "#/definitions/";


    public static byte[] getBody(byte[] bytes) throws IOException {

        JSONObject strData = JSONUtil.createObj()
                .putOnce(TYPE, "string")
                .putOnce("example", "")
                .putOnce("description", "响应数据");
        JSONObject rDefinition = buildDefinition(rName, strData);
        JSONObject voidDefinition = buildDefinition(voidName, null);
        JSONObject voidSchema = buildSchema(voidName);
        JSONObject rSchema = buildSchema(rName);
        // 2.转为json
        String json = new String(bytes, StandardCharsets.UTF_8);
        JSONObject jsonObject = JSONUtil.parseObj(json);
        // 3.获取原始数据
        // 3.1.获取paths
        JSONObject paths = (JSONObject) jsonObject.getObj(PATHS);
        // 3.2.获取definitions
        JSONObject definitions = (JSONObject) jsonObject.getObj(DEFINITIONS);
        // 3.3.遍历路径
        for (Object pathObj : paths.values()) {
            JSONObject path = (JSONObject) pathObj;
            // 3.4.遍历请求方式对应的接口
            for (Object pathValueObj : path.values()) {
                JSONObject pathValue = (JSONObject) pathValueObj;
                // 3.5.获取200响应结果
                JSONObject responses = (JSONObject) pathValue.get(RESPONSES);
                JSONObject resp200 = (JSONObject) responses.get("200");
                // 3.6.获取旧的schema，基于schema做判断进行下一步处理
                Object schemaObj = resp200.get(SCHEMA);
                if (schemaObj != null) {
                    // 有返回值
                    JSONObject schema = (JSONObject) schemaObj;
                    String type = schema.get(TYPE, String.class);
                    if (type == null) {
                        // 对象类型
                        handleObjectSchema(resp200, definitions, schema);
                    } else if (type.equals("array")) {
                        // 数组类型
                        handleArraySchema(resp200, definitions, schema);
                    } else {
                        // 基本类型
                        handleBasicSchema(resp200, definitions, rDefinition,rSchema);
                    }
                } else {
                    // 返回值是void
                    handleVoidSchema(resp200, definitions, voidDefinition, voidSchema);
                }

            }
        }

        return jsonObject.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static void handleVoidSchema(JSONObject resp200, JSONObject definitions,JSONObject voidDefinition,JSONObject voidSchema) {
        // 1.写入definition
        definitions.putIfAbsent(voidName, voidDefinition);
        // 2.写入path
        resp200.putOpt(SCHEMA, voidSchema);
    }

    private static void handleBasicSchema(JSONObject resp200, JSONObject definitions,JSONObject rDefinition,JSONObject rSchema) {
        // 1.写入definition
        definitions.putIfAbsent(rName, rDefinition);
        // 2.写入path
        resp200.putOpt(SCHEMA, rSchema);
    }

    private static void handleArraySchema(JSONObject resp200, JSONObject definitions, JSONObject oldSchema) {
        // 1.写入definition
        String originalRef = oldSchema.getByPath("items." + ORIGINAL_REF, String.class);
        String name = String.format("R«List«%s»»", originalRef);
        definitions.putIfAbsent(name, buildArrayDefinition(originalRef, name));
        // 2.写入path
        resp200.putOpt(SCHEMA, buildSchema(name));
    }

    private static void handleObjectSchema(JSONObject resp200, JSONObject definitions, JSONObject oldSchema) {
        // 1.写入definition
        String originalRef = oldSchema.get(ORIGINAL_REF, String.class);
        String name = String.format("R«%s»", originalRef);
        definitions.putIfAbsent(name, buildDefinition(name, buildSchema(originalRef)));
        // 2.写入path
        resp200.putOpt(SCHEMA, buildSchema(name));
    }

    private static JSONObject buildArrayDefinition(String originalRef, String title) {
        JSONObject data = JSONUtil.createObj()
                .putOnce("description", "响应数据")
                .putOnce("type", "array")
                .putOnce("items",
                        buildSchema(originalRef));
        return buildDefinition(title, data);
    }

    private static JSONObject buildDefinition(String title, Object data) {
        JSONObject code = JSONUtil.createObj()
                .putOnce(TYPE, "integer")
                .putOnce("format", "int32")
                .putOnce("example", 200)
                .putOnce("description", "状态码，200-成功，其它-失败");
        JSONObject msg = JSONUtil.createObj()
                .putOnce(TYPE, "string")
                .putOnce("example", "OK")
                .putOnce("description", "响应消息");
        JSONObject properties = JSONUtil.createObj().putOnce("code", code).putOnce("msg", msg);
        if (data != null) {
            properties.putOnce("data", data);
        }
        return JSONUtil.createObj()
                .putOnce(TYPE, "object")
                .putOnce(TITLE, title)
                .putOnce("properties", properties)
                .putOnce("description", "通用响应结果");
    }

    private static JSONObject buildSchema(String name) {
        return JSONUtil.createObj()
                .putOnce(REF, REF_PREFIX + name)
                .putOnce(ORIGINAL_REF, name);
    }
}
