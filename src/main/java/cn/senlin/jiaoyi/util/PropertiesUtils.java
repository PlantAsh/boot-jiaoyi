package cn.senlin.jiaoyi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtils {

    /**
     * 文件上传存储目录
     */
    @Value("${file.path}")
    private String filePath;

    /**
     * 文件访问地址
     */
    @Value("${access.url}")
    private String accessUrl;

    public String getFilePath() {
        return filePath;
    }

    public String getAccessUrl() {
        return accessUrl;
    }
}
