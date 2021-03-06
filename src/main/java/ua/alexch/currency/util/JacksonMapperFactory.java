package ua.alexch.currency.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

public final class JacksonMapperFactory {

    public static <T> ObjectMapper createMapper(Class<T> type, StdDeserializer<? extends T> deserializer) {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule customModule = new SimpleModule("customModule");
        customModule.addDeserializer(type, deserializer);
        mapper.registerModule(customModule);

        return mapper;
    }

    private JacksonMapperFactory() {}
}
