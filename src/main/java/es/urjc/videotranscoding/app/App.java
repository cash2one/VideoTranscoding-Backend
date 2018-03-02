package es.urjc.videotranscoding.app;

import javax.servlet.MultipartConfigElement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main APP with beans and import xml config
 * 
 * @author luisca
 */
@SpringBootApplication
@Configuration
@EnableScheduling
@ComponentScan("es.urjc.videotranscoding")
@ImportResource({ "classpath*:xml/ffmpeg-config.xml" })
@EntityScan("es.urjc.videotranscoding.entities")
@EnableJpaRepositories("es.urjc.videotranscoding.repository")
public class App {
	private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		logger.info(" --------- App SpringBoot Started ------- ");
		System.out.println(" --------- App SpringBoot Started ------- ");
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("1024MB");
		factory.setMaxRequestSize("1024MB");
		return factory.createMultipartConfig();
	}
}
