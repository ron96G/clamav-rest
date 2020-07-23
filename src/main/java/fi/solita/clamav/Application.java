package fi.solita.clamav;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
/**
 * Simple Spring Boot application which acts as a REST endpoint for
 * clamd server.
 */
public class Application {

  @Value("${clamd.maxfilesize}")
  private String maxfilesize;

  @Value("${clamd.maxrequestsize}")
  private String maxrequestsize;

  @Bean
  MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(maxfilesize);
    factory.setMaxRequestSize(maxrequestsize);
    return factory.createMultipartConfig();
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);

    System.out.println("Setting up defaults");
    Map<String, Object> defaults = new HashMap<String, Object>();
    defaults.put("clamd.host", "127.0.0.1");
    defaults.put("clamd.port", 3310);
    defaults.put("clamd.timeout", 500);
    defaults.put("clamd.maxfilesize", "20MB");
    defaults.put("clamd.maxrequestsize", "20MB");
    defaults.put("spring.servlet.multipart.max-file-size", "20MB");
    defaults.put("spring.servlet.multipart.max-request-size", "20MB");
    app.setDefaultProperties(defaults);
    app.run(args);
  }
}
