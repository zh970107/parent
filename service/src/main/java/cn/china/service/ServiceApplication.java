package cn.china.service;

import entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.StudentService;

@RestController//就是在service掉用给别人一个入口,不认别人也办法使用这个服务

@EnableEurekaClient //让他注册服务,告诉service层可以向注册中心注册服务,不注册中心,注册用户获取不到
//@SpringBootApplication //就是不用数据库,就是用自己的就要下面的配置
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("service")
//@MapperScan("mapper")

public class ServiceApplication {

    @Autowired
    StudentService ss;

@RequestMapping("/getInfo")
public  Student getInfo(){
    return ss.getInfo();
}

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

}
