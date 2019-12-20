package cn.chian.web;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@Configuration  //提示注册中心是负载均衡
@RestController//一般就是当做controller使用
@EnableDiscoveryClient//就是发布以来让他发现,就是自己的发现自己是注册中心,//就是基本配置这个就要配置注册中心的application.yml.
//@SpringBootApplication ,暂时不用数据可以用不找,就是用数据才用
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class WebApplication {

    @Autowired //自动注入让我们调用service服务的名字
    RestTemplate rest;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Bean   //就是把这个bean构造中心,可以手动加
    @LoadBalanced   //均衡,随机获取一个service
    public  RestTemplate getRest(){
        return  new RestTemplate();
    }


    @Bean
    public IRule ribbonRule(){
        return new RandomRule();
    }

    @RequestMapping(value="/getInfo",produces = "application/json;charset=UTF-8")
    public Student myGet(HttpServletResponse res){
        res.setHeader("Access-Control-Allow-Origin", "*");//解决ajax跨域问题
        //获取service的服务名
        ServiceInstance serviceInstance = loadBalancerClient.choose("provider");
        //获取url地址
        String url = String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort())+"/getInfo";
        System.out.println(url+"!!!!!!!!!!!");
        //怎么根据service获取?1.根据发布的名字获取,2.就是根据路径获取

        Student stu = rest.getForObject("http://provider/getInfo",Student.class);
        return  stu;
    }

    public static void main(String[] args) {

        SpringApplication.run(WebApplication.class, args);
    }
}
