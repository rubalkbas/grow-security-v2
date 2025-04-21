package com.ittiva.generic;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories
public class GenericSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenericSecurityApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
	 @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**")
	                        .allowedOrigins("*")
	                        .allowedMethods("*")
	                        .allowedHeaders("*");
	            }
	        };
	    }
 
}
