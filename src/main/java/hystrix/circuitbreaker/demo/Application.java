package hystrix.circuitbreaker.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Configuration Class
 * @author Nick Walter
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run("classpath:META-INF/spring/server.xml", args);
    }

}
