package urjc;

import javax.servlet.MultipartConfigElement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Main APP with beans and import xml config
 * 
 * @author luisca
 */
@SpringBootApplication
@Configuration
@EnableScheduling
@ImportResource({"classpath*:xml/ffmpeg-config.xml"})
public class App{
	private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args){
		SpringApplication.run(App.class,args);
		logger.info(" --------- App SpringBoot Started ------- ");
		System.out.println(" --------- App SpringBoot Started ------- ");
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurerAdapter(){
			@Override
			public void addCorsMappings(CorsRegistry registry){
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET","POST","OPTIONS","PUT","DELETE")
							.allowedHeaders("Content-Type","X-Requested-With","accept","Origin",
										"Access-Control-Request-Method","Access-Control-Request-Headers")
							.exposedHeaders("Access-Control-Allow-Origin","Access-Control-Allow-Credentials")
							.allowCredentials(true).maxAge(3600);
			}
		};
	}

	@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("1024MB");
		factory.setMaxRequestSize("1024MB");
		return factory.createMultipartConfig();
	}
}
