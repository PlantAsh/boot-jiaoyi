package cn.senlin.jiaoyi.util.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 拦截器数据处理工具类
 *
 * @author swu
 * @date 2020-07-09
 */
public class InterceptorUtil {

    /**
     * 返回数据拦截功能不过滤的url
     */
    public static String[] noFilterUrl = new String[] {""};

    /**
     * 列表处理时间
     *
     * @param list
     */
    public static void dateByList(List list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Object> copyList = new ArrayList<>(list);
        list.clear();
        for (Object o : copyList) {
            list.add(dateByObject(o));
        }
    }

    /**
     * 处理时间
     *
     * @param o
     */
    public static Object dateByObject(Object o) {
        if (o == null || StringUtils.isEmpty(JSONObject.toJSONString(o))
                || !JSONObject.toJSONString(o).startsWith("{") || !JSONObject.toJSONString(o).endsWith("}")) {
            return o;
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(o));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String key : object.keySet()) {
            Object o1 = object.get(key);
            String keyContains = key.toLowerCase();
            if ((keyContains.contains("time") || keyContains.contains("date") || keyContains.contains("day"))
                    && o1 instanceof Long) {
                Date date = new Date((Long) o1);
                object.put(key, sf.format(date));
            }
        }
        return object;
    }
}
