package cn.senlin.jiaoyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@MapperScan("cn.senlin.jiaoyi.mapper")
@PropertySource(value = "file:D:\\文件\\git\\boot-jiaoyi\\config\\jiaoyi.properties")
//@PropertySource(value = "file:/project/config/jiaoyi.properties")
@SpringBootApplication
public class JiaoyiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JiaoyiApplication.class, args);
	}

}
