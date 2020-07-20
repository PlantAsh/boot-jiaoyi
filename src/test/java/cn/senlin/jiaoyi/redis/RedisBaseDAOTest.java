package cn.senlin.jiaoyi.redis;

import cn.senlin.jiaoyi.JiaoyiApplicationTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
class RedisBaseDAOTest extends JiaoyiApplicationTests {

    @Resource
    private RedisBaseDAO redisBaseDAO;

    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @BeforeEach
    void setUp() {
        System.out.println("-----------------测试开始-----------------");
    }

    @AfterEach
    void tearDown() {
        System.out.println("-----------------测试结束-----------------");
    }

    @Test
    void get() {
        System.out.println(redisBaseDAO.get("name"));
    }

    @Test
    void set() {
        System.out.println(redisBaseDAO.set("name", "123"));
//        System.out.println(redisBaseDAO.increment("name", 22.3));
        System.out.println(redisBaseDAO.get("name"));
    }

    @Test
    void sendKafka() {
        kafkaTemplate.send("wusen", "boot service");
    }

}