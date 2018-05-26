package es.urjc.videotranscoding.app;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main APP and import xml config
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

}
