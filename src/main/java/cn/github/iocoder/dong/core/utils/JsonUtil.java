package cn.github.iocoder.dong.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author dong
 * @date 2022/9/5
 */
public class JsonUtil {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static  <T> T toObj(String str, Class<T> clz) {
        try {
            return jsonMapper.readValue(str, clz);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static <T> String toStr(T t) {
        try {
            return jsonMapper.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }


}
