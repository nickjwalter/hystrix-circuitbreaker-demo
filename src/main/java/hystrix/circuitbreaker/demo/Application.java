package hystrix.circuitbreaker.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicURLConfiguration;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

/**
 * Spring Boot Configuration Class
 * @author Nick Walter
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run("classpath:META-INF/spring/server.xml", args);

        initialisePropertyFactory();
    }

    /**
     * Configure the Hystrix Metrics Servlet.
     * @return a configured HystrixMetricsStreamServlet
     */
    @Bean
    public ServletRegistrationBean hystrixServletRegistration() {
        HystrixMetricsStreamServlet hystrixMetricsServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean servletRegistration = new ServletRegistrationBean(
            hystrixMetricsServlet, new String[]{"/hystrix.stream"}
        );

        return servletRegistration;
    }

    /**
     * Initialise the Dynamic Property Factory.
     */
    private static void initialisePropertyFactory() {
        final FileSystemResource configProperties = new FileSystemResource("config/config.properties");
        try {
            DynamicURLConfiguration urlConfiguration = new DynamicURLConfiguration(
                500, 500, true, configProperties.getURL().toString()
            );

            DynamicPropertyFactory.initWithConfigurationSource(urlConfiguration);

        } catch (Exception e){
            throw new RuntimeException("Error unable to load Dynamic Properties!", e);
        }
    }

}
