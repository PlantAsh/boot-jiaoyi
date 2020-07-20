package cn.senlin.jiaoyi.util.interceptor;

import cn.senlin.jiaoyi.util.AgeUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据返回视图前统一处理
 *
 * @author swu
 * @date 2020-07-03
 */
public class MyResponseBody implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 这里直接返回true,表示对任何handler的responsebody都调用beforeBodyWrite方法
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null || StringUtils.isEmpty(JSONObject.toJSONString(body))
                || !JSONObject.toJSONString(body).startsWith("{") || !JSONObject.toJSONString(body).endsWith("}")) {
            return body;
        }
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = servletRequest.getRequestURI();
        boolean filter = true;
        for (String fUrl : InterceptorUtil.noFilterUrl) {
            if (url.contains(fUrl)) {
                filter = false;
                break;
            }
        }
        if (filter) {
            return body;
        }

        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(body));
        if (object.get("models") == null) {
            return body;
        }
        JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(object.get("models")));
        Map<String, Object> map = new HashMap<>();
        for (String key : data.keySet()) {
            Object o = data.get(key);
            String val = JSONObject.toJSONString(o);
            if ((!val.startsWith("{") || !val.endsWith("}")) && (!val.startsWith("[") || !val.endsWith("]"))) {
                map.put(key, o);
                continue;
            }
            if (val.startsWith("[") && val.endsWith("]")) {
                List<Object> list = JSONObject.parseArray(val, Object.class);
                AgeUtils.ageByList(list, "1");
                InterceptorUtil.dateByList(list);
                map.put(key, list);
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(val);
            if (jsonObject.get("pageSize") != null || jsonObject.get("pageNum") != null) {
                //分页数据处理
            } else {
                o = AgeUtils.ageByObject(o, "1");
                o = InterceptorUtil.dateByObject(o);
                map.put(key, o);
            }
        }
        object.put("models", map);
        return object;
    }
}
