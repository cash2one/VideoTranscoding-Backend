package urjc.videotranscoding.app.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Main APP with beans and import xml config
 * 
 * @author luisca
 */
@SpringBootApplication
@Configuration
@ImportResource({"classpath*:xml/ffmpeg-config-test2.xml"})
public class AppTest{
	private static final Logger logger = LogManager.getLogger(AppTest.class);

	public static void main(String[] args){
		SpringApplication.run(AppTest.class,args);
		logger.info(" --------- App SpringBoot Started ------- ");
		System.out.println(" --------- App SpringBoot Started ------- ");
	}




}
