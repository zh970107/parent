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
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@Configuration//(service2)就是要是使用负载均衡(告诉他)加入的时候
@RestController//一般就是当做原先controller控制层使用,只不过这里分成了web模块了
@EnableDiscoveryClient//启用(使什么能够)发现的客户端,(就是让注册中心发线他)--就是客户端发布服务,并向注册中心注册服务了,然后就是让这个服务能够让用户发现-就使用使用访问这个服务,就是用户访问这个web8082--直接注册中心8080-然后访问服务8081,//就是进本配置这个就要配置注册中心的application.yml.
//@SpringBootApplication //,暂时不用配置数据可以用不找,就是用配置采用数据库才用
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class WebApplication {

    //就是要获取当前当前的端接口
    private  String ip;
    private  String port;


    @Autowired //自动注入让我们调用service服务的名字,
    RestTemplate rest;

    @Autowired
    LoadBalancerClient loadBalancerClient;//就是动态获取自己url



    @Bean   //产生一个bean的方法，并且交给Spring容器管理,可以手动加
    @LoadBalanced //2.(service2)就是要加载的时候要负载均衡,
    public  RestTemplate getRest() {

        return new RestTemplate();

    }

    @Bean //负载均衡随机规则,随机调用集群服务(service2)
    public IRule ribbonRule(){
        return  new RandomRule();
    }


    @RequestMapping(value="/insert",produces = "application/json;charset=UTF-8")
    public  int insert(HttpServletResponse  res){
        res.setHeader("Access-Control-Allow-Origin", "*");//service2-解决ajax跨域问题
        //3.就是动态获取服务器的地址
        ServiceInstance serviceInstance = loadBalancerClient.choose("provider");

        int line =   rest.getForObject("http://provider/insert",Integer.class);
        return line;
    }

    @RequestMapping(value="/getInfo",produces = "application/json;charset=UTF-8")
    public Student myGet(HttpServletResponse  res){//这个也技术HttpServletResponse  res 就是也就是使用跨域问题,
        res.setHeader("Access-Control-Allow-Origin", "*");//service2-解决ajax跨域问题



        //怎么根据service获取?1.根据发布的名字获取,2.就是根据路径获取.?3不一定每次都知道?想要动态的获取
       /* Student stu = rest.getForObject("http://localhost:8081/getInfo",Student.class);
        return  stu;*/

        //3.就是动态获取服务器的地址
        ServiceInstance serviceInstance = loadBalancerClient.choose("provider");
        System.out.println(serviceInstance.getHost());
        System.out.println(serviceInstance.getPort());//获取当前的端口号

     /*   //就是动态拼接端接口号,还有地址
     //就是有一个,服务器是要动态的加载的时候,就是获取端口号的时候,就是使用这个
       String url = String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort())+"/getInfo";
        System.out.println("*******"+url);
        Student stu = rest.getForObject(url,Student.class);
     //   return  stu;*/

        //就是比较暴力,就是自己直接获取提供的端口号
        Student stu1 = rest.getForObject("http://provider/getInfo",Student.class);
        System.out.println(stu1);
        return   stu1;

    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
