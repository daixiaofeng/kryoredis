package com.feng.util.test;

import com.feng.KryoRedisApplication;
import com.feng.util.RedisUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
class RedisTest {

    @Autowired
    RedisUtils redisUtils;

    @Test
    public void test0() {
        redisUtils.set("user:0:name", "小红");
        String name = redisUtils.get("user:0:name");
        log.info("用户0名称：{}", name);
    }

    @Test
    public void test1() {
        User user = new User("小白", 18, 0, "大明湖畔200号");
        redisUtils.setObject("user:0", user);

        User user0 = redisUtils.getObject("user:0");
        log.info("用户0：{}", user0);
    }
    @Test
    public void test2() {
        User user = new User("小白", 18, 0, "大明湖畔200号");
        redisUtils.setObject("user:0", user);

        User user0 = redisUtils.getObject("user:0");
        log.info("用户0：{}", user0);
    }
    @Data
    @AllArgsConstructor
    public class User {
        private String name;
        private int age;
        private int sex;
        private String address;
    }
}
