package es.urjc.videotranscoding.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig{
	private final String TITLE = "VideoTranscoding Api DOC";
	private final String DESCRIPTION = "Documentation for urjc.videotranscoding.es/api/";
	private final String LICENSE = "Apache 2.0";
	private final String LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0";
	private final String VERSION = "0.1";
	private final String BASE_PACKAGE = "urjc.videotranscoding.restController";
	private final String TERMS_OF_SERVICE = "github";

	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
					.paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo(){
		return new ApiInfoBuilder().title(TITLE).description(DESCRIPTION).termsOfServiceUrl(TERMS_OF_SERVICE)
					.license(LICENSE).licenseUrl(LICENSE_URL).version(VERSION).build();
	}
}
