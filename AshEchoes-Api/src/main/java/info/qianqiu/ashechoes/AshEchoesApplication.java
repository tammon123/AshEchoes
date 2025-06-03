package info.qianqiu.ashechoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class AshEchoesApplication {


    public static void main(String[] args) {
        SpringApplication.run(AshEchoesApplication.class, args);
    }


}