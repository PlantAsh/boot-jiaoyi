package cn.senlin.jiaoyi.util.database;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * 注入数据源
 *
 * @author swu
 * @date 2020-03-17
 */
@Configuration
@MapperScan(basePackages = DbConfig.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class DbConfig {
    Logger log = LoggerFactory.getLogger(DbConfig.class);

    static final String PACKAGE = "cn.senlin.jiaoyi.mapper";
    private static final String MAPPER_LOCATION = "classpath:mapper/*.xml";
    private static final String DOMAIN_PACKAGE = "cn.senlin.jiaoyi";

    @Value("${spring.datasource.druid.url}")
    private String dbUrl;

    @Value("${spring.datasource.druid.username}")
    private String username;

    @Value("${spring.datasource.druid.password}")
    private String password;

    @Value("${spring.datasource.druid.connection-properties}")
    private String connectionProperties;

    @Value("${spring.datasource.druid.password-callback}")
    private String passwordCallbackClassName;

    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setConnectionProperties(connectionProperties);
        try {
            datasource.setPasswordCallbackClassName(passwordCallbackClassName);
        } catch (Exception e) {
            log.error("druid configuration initialization passwordCallbackClassName", e);
        }
        return datasource;
    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(DbConfig.MAPPER_LOCATION));
        sessionFactory.setTypeAliasesPackage(DOMAIN_PACKAGE);
        Objects.requireNonNull(sessionFactory.getObject()).getConfiguration().setMapUnderscoreToCamelCase(true);
        return sessionFactory.getObject();
    }
}
