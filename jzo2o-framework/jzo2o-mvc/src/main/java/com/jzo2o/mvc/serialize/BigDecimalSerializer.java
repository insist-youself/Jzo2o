package com.jzo2o.mvc.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalSerializer extends JsonSerializer {
    public static final BigDecimalSerializer instance = new BigDecimalSerializer();

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (o != null && o instanceof BigDecimal) {
            BigDecimal bigDecomal = (BigDecimal) o;
            jsonGenerator.writeString(bigDecomal.setScale(2, RoundingMode.DOWN).toString());
        }
    }
}
