package cn.senlin.jiaoyi.util;

import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5加密
 *
 * @author swu
 * @date 2020-03-31
 */
public class Md5Utils {

    /**
     * 将string进行md5编码
     *
     * @param s
     * @return
     */
    public static String encode(String s) {
        try {
            if (StringUtils.isEmpty(s)) {
                return "'";
            }
            return new String(toHex(md5(s)).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return s;
        }
    }

    private static byte[] md5(String s) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes(StandardCharsets.UTF_8));
            return algorithm.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static String toHex(byte[] hash) {
        if (hash == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

}

