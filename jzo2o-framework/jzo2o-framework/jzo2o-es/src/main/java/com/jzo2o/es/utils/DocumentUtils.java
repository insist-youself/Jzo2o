package com.jzo2o.es.utils;

import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;

import java.io.IOException;
import java.util.Map;

public class DocumentUtils {

    /**
     * 不可用于集合
     * @param document
     * @return
     * @param <T>
     * @throws IOException
     */
    public static <T> XContentBuilder convert(T document)  {

        Map<String, Object> map = BeanUtils.beanToMap(document);
        if (CollUtils.isEmpty(map)) {
            return null;
        }
        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                xContentBuilder.field(entry.getKey(), entry.getValue());
            }
            return xContentBuilder.endObject();
        }catch (IOException e){
            return null;
        }

    }

}
