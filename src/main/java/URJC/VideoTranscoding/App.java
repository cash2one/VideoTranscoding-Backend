package URJC.VideoTranscoding;

import javax.servlet.MultipartConfigElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@Configuration
@EnableWebMvc
@ImportResource({"classpath*:xml/ffmpeg-config.xml"})
public class App{
	private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args){
		SpringApplication.run(App.class,args);
		logger.info(" --------- App SpringBoot Started ------- ");
	}

	@Bean
	public WebMvcConfigurerAdapter corsConfigurer(){
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
