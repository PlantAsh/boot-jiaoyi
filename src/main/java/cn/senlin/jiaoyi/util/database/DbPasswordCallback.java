package cn.senlin.jiaoyi.util.database;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * 数据库密码解密
 *
 * @author swu
 * @date 2020-03-17
 */
@Slf4j
public class DbPasswordCallback extends DruidPasswordCallback {
    private static final long serialVersionUID = -2902056230895632227L;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        String pwd = properties.getProperty("password");
        String pbk = properties.getProperty("publickey");
        try {
            String password = ConfigTools.decrypt(pbk, pwd);
            setPassword(password.toCharArray());
        } catch (Exception e) {
            log.error("druid ConfigTools.decrypt:", e);
        }
    }

}
