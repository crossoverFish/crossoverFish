package com.tyu.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @ClassName: JacksonUtil
 * @Description: Jackson的json操作工具类
 * @author sunjl
 */
public abstract class JacksonCamelUtil {
    /**
     * @Fields EMPTY_JSON : 空json串
     */
    public static final String EMPTY_JSON = "{}";

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * @Fields LOGGER : 日志操作类
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonCamelUtil.class);

    /**
     * @Fields MAPPER : 对象和json的映射器
     */
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_PATTERN));
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    }

    /**
     * @Fields XML_MAPPER : XML映射器
     */
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    /**
     * @Title: getMapper
     * @Description: 获取对象映射器
     * @return 对象映射器
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    /**
     * @Title: fromJson
     * @Description: 将json输入流转换成对象
     * @param inputStream json的输入流
     * @param <T> 需要的对象类型
     * @param clazz 对象类
     * @return json输入流转换成的对象
     */
    public static <T> T fromJson(final InputStream inputStream, final Class<T> clazz) {
        return fromJson(inputStream, clazz, MAPPER);
    }

    /**
     * @Title: fromJson
     * @Description: 将json输入流转换成对象
     * @param inputStream json的输入流
     * @param <T> 需要的对象类型
     * @param clazz 对象类
     * @param objectMapper 自定义的对象映射器
     * @return json输入流转换成的对象
     */
    public static <T> T fromJson(final InputStream inputStream, final Class<T> clazz, final ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(inputStream, clazz);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Title: fromJson
     * @Description: 将json字符串转换成对象
     * @param json json字符串
     * @param <T> 需要的对象类型
     * @param clazz 对象类
     * @return json转换成的对象
     */
    public static <T> T fromJson(final String json, final Class<T> clazz) {
        return fromJson(json, clazz, MAPPER);
    }

    /**
     * @Title: fromJson
     * @Description: 将json字符串转换成对象
     * @param json json字符串
     * @param <T> 需要的对象类型
     * @param clazz 对象类
     * @param objectMapper 自定义对象映射类
     * @return json转换成的对象
     */
    public static <T> T fromJson(final String json, final Class<T> clazz, final ObjectMapper objectMapper) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * @Title: fromJson
     * @Description: 将json字符串转换成json对象
     * @param json json字符串
     * @param objectMapper 自定义对象映射类
     * @return json对象
     */
    public static JsonNode fromJson(final String json, final ObjectMapper objectMapper) {
        try {
            return objectMapper.readTree(json);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Title: fromJson
     * @Description: 将json输入流转换成json对象
     * @param inputStream json输入流
     * @return json对象
     */
    public static JsonNode fromJson(final InputStream inputStream) {
        return fromJson(inputStream, MAPPER);
    }

    /**
     * @Title: fromJson
     * @Description: 将json输入流转换成json对象
     * @param inputStream json输入流
     * @param objectMapper 自定义对象映射类
     * @return json对象
     */
    public static JsonNode fromJson(final InputStream inputStream, final ObjectMapper objectMapper) {
        try {
            return objectMapper.readTree(inputStream);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Title: toJson
     * @Description: 将对象转换成json对象
     * @param object 对象
     * @return json对象
     */
    public static JsonNode toJson(final Object object) {
        return toJson(object, MAPPER);
    }

    /**
     * @Title: toJson
     * @Description: 将对象转换成json对象
     * @param object 对象
     * @param objectMapper 自定义对象映射类
     * @return json对象
     */
    public static JsonNode toJson(final Object object, final ObjectMapper objectMapper) {
        return objectMapper.valueToTree(object);
    }

    /**
     * @Title: toJsonStr
     * @Description: 将对象转换成json对象
     * @param object 对象
     * @return json字符串
     */
    public static String toJsonStr(final Object object) {
        return toJsonStr(object, MAPPER);
    }

    /**
     * @Title: objectToMap
     * @Description: 将对象转换成Map对象
     * @param object 对象
     * @return Map对象
     */
    public static Map objectToMap(final Object object){
        return objectToMap(object,MAPPER);
    }

    /**
     * @Title: toPrettyJsonStr
     * @Description: 将对象转换成美化过的json对象
     * @param object 对象
     * @return json字符串
     */
    public static String toPrettyJsonStr(final Object object) {
        return toPrettyJsonStr(object, MAPPER);
    }

    /**
     * @Title: toJsonStr
     * @Description: 将对象转换成json对象
     * @param object 对象
     * @param objectMapper 自定义对象映射类
     * @return json字符串
     */
    public static String toJsonStr(final Object object, final ObjectMapper objectMapper) {
        if (object == null) {
            return EMPTY_JSON;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map objectToMap(final Object object, final ObjectMapper objectMapper){

        if (object == null) {
            return null;
        }

        try {
            Map map = objectMapper.readValue(objectMapper.writeValueAsString(object), Map.class);
            return map;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @Title: toPrettyJsonStr
     * @Description: 将对象转换成美化过的json对象
     * @param object 对象
     * @param objectMapper 自定义对象映射类
     * @return json字符串
     */
    public static String toPrettyJsonStr(final Object object, final ObjectMapper objectMapper) {
        if (object == null) {
            return EMPTY_JSON;
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Title: fromJson
     * @Description: 将json字符串转换成指定类型对象
     * @param json json字符串
     * @param <T> 指定类型
     * @param typeReference 类型引用
     * @return json转换的对象
     */
    public static <T> T fromJson(final String json, final TypeReference<T> typeReference) {
        return fromJson(json, typeReference, MAPPER);
    }

    /**
     * @Title: fromJson
     * @Description: 将json字符串转换成指定类型对象，use generally List<SomeClass> list = JsonUtils.fromJson(jsonString, new TypeReference<List<SomeClass>>() { }
     * @param json json字符串
     * @param <T> 指定类型
     * @param typeReference 类型引用
     * @param objectMapper 自定义对象映射类
     * @return json转换的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(final String json, final TypeReference<T> typeReference, final ObjectMapper objectMapper) {
        if (json == null || typeReference == null) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(json, typeReference);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Title: getNewArrayNode
     * @Description: 新建jsonArray节点
     * @return jsonArray节点
     */
    public static ArrayNode getNewArrayNode() {
        return getNewArrayNode(MAPPER);
    }

    /**
     * @Title: getNewArrayNode
     * @Description: 新建jsonArray节点
     * @param objectMapper 自定义对象映射类
     * @return jsonArray节点
     */
    public static ArrayNode getNewArrayNode(final ObjectMapper objectMapper) {
        return objectMapper.createArrayNode();
    }

    /**
     * @Title: getNewObjectNode
     * @Description: 新建jsonObject节点
     * @return jsonObject节点
     */
    public static ObjectNode getNewObjectNode() {
        return getNewObjectNode(MAPPER);
    }

    /**
     * @Title: getNewObjectNode
     * @Description: 新建jsonObject节点
     * @param objectMapper 自定义对象映射类
     * @return jsonObject节点
     */
    public static ObjectNode getNewObjectNode(final ObjectMapper objectMapper) {
        return objectMapper.createObjectNode();
    }

    /**
     * @Title: toXml
     * @Description: 将对象输出成xml输出流
     * @param output xml输出流
     * @param value 被转换的对象
     */
    public static void toXml(final OutputStream output, final Object value) {
        toXml(output, XML_MAPPER);
    }

    /**
     * @Title: toXml
     * @Description: 将对象输出成xml输出流
     * @param output xml输出流
     * @param value 被转换的对象
     * @param xmlMapper 自定义xml映射类
     */
    public static void toXml(final OutputStream output, final Object value, final XmlMapper xmlMapper) {
        try {
            xmlMapper.writeValue(output, value);
        } catch (final JsonGenerationException e) {
            throw new RuntimeException(e);
        } catch (final JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Title: toXmlAsString
     * @Description: 将对象转换成xml
     * @param value 对象
     * @return xml字符串
     */
    public static String toXmlAsString(final Object value) {
        return toXmlAsString(value, XML_MAPPER);
    }

    /**
     * @Title: toXmlAsString
     * @Description: 将对象转换成xml
     * @param value 对象
     * @param xmlMapper 自定义xml映射类
     * @return xml字符串
     */
    public static String toXmlAsString(final Object value, final XmlMapper xmlMapper) {
        try {
            return xmlMapper.writeValueAsString(value);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Title: fromXml
     * @Description: 将xml输入流转换成指定类型的对象
     * @param input xml输入流
     * @param clazz 目标对象类
     * @param <T> 目标对象类型
     * @return xml转换成的对象
     */
    public static <T> T fromXml(final InputStream input, final Class<T> clazz) {
        return fromXml(input, clazz, XML_MAPPER);
    }

    /**
     * @Title: fromXml
     * @Description: 将xml输入流转换成指定类型的对象
     * @param inputSteam xml输入流
     * @param clazz 目标对象类
     * @param <T> 目标对象类型
     * @param xmlMapper 自定义xml映射类
     * @return xml输入流转换成的对象
     */
    public static <T> T fromXml(final InputStream inputSteam, final Class<T> clazz, final XmlMapper xmlMapper) {
        try {
            return xmlMapper.readValue(inputSteam, clazz);// xml stream to object
        } catch (final JsonParseException jsonParseEx) {
            throw new RuntimeException(jsonParseEx);
        } catch (final JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Title: fromXml
     * @Description: 将xml转换成指定类型的对象
     * @param input xml
     * @param clazz 目标对象类
     * @param <T> 目标对象类型
     * @return xml转换成的对象
     */
    public static <T> T fromXml(final String input, final Class<T> clazz) {
        return fromXml(input, clazz, XML_MAPPER);
    }

    /**
     * @Title: fromXml
     * @Description: 将xml转换成指定类型的对象
     * @param input xml
     * @param clazz 目标对象类
     * @param <T> 目标对象类型
     * @param xmlMapper 自定义xml映射类
     * @return xml转换成的对象
     */
    public static <T> T fromXml(final String input, final Class<T> clazz, final XmlMapper xmlMapper) {
        try {
            return xmlMapper.readValue(input, clazz);
        } catch (final JsonParseException e) {
            throw new RuntimeException(e);
        } catch (final JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (final IOException ioEx) {
            throw new RuntimeException(ioEx);
        }
    }

    /**
     * @Title: update
     * @Description: 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
     * @param jsonString 覆盖到object的属性
     * @param object 被覆盖的object
     * @param <T> 被覆盖的object类型
     * @return 覆盖后的object
     */
    @SuppressWarnings("unchecked")
    public <T> T update(final String jsonString, final T object) {
        try {
            return (T) MAPPER.readerForUpdating(object).readValue(jsonString);
        } catch (final JsonProcessingException e) {
            LOGGER.warn("update json string:{} to object: {} json error:{}", jsonString, object, e);
        } catch (final IOException e) {
            LOGGER.warn("update json string:{} to object: {} io error:{}", jsonString, object, e);
        }
        return null;
    }

    /**
     * 对象转对象
    * @Title: convertValue
    * @Description:
    * @param obj
    * @param clazz
    * @return
     */
    public static <T> T convertValue(Object obj, Class<T> clazz) {
        return MAPPER.convertValue(obj, clazz);
    }

    /**
     * 复杂类型对象转对象
    * @Title: convertValue
    * @Description:
    * @param obj
    * @param paramTypeReference
    * @return
     */
    public static <T> T convertValue(Object obj, TypeReference<?> paramTypeReference) {
        return MAPPER.convertValue(obj, paramTypeReference);
    }
}
