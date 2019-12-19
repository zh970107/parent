package cn.china.eurekaregisty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer  //该注解用于启动注册中心(服务),就是注册中心得服务者
//@SpringBootApplication//自动占用数据库资源,会报错,不用数据库建议使用下面的注解
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class EurekaregistyApplication {
//就是启动的时候要第一步要启动注册中心
    public static void main(String[] args) {
        SpringApplication.run(EurekaregistyApplication.class, args);
    }

}
