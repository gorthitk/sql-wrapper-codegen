package org.jet.sql.codegen;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * @author tgorthi
 * @since Dec 2019
 */
public class ObjectMapperFactory
{
    public static ObjectMapper create()
    {
        final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }
}
