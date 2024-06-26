package com.jzo2o.es.utils;

import com.jzo2o.common.utils.CollUtils;
import co.elastic.clients.elasticsearch._types.FieldValue;

import java.util.ArrayList;
import java.util.List;

public class TermUtils {



    public static <T> List<FieldValue> parse(List<T> sources) {
        if (CollUtils.isEmpty(sources)) {
            return null;
        }
        List<FieldValue> values = new ArrayList<>();
        sources.stream().forEach(s -> values.add(FieldValue.of(s.toString())));
        return values;
    }
}
