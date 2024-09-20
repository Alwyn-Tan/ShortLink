package org.alwyn.shortlink.admin.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PhoneMaskingSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    String maskedPhone = s.substring(0, 3) + "****" + s.substring(7);
    jsonGenerator.writeString(maskedPhone);
    }
}
