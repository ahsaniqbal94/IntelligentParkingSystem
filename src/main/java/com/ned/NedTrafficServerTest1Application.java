package com.ned;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import com.sun.research.ws.wadl.Application;

@SpringBootApplication
public class NedTrafficServerTest1Application
//extends SpringBootServletInitializer
{

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
//		return application.sources(NedTrafficServerTest1Application.class);
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(NedTrafficServerTest1Application.class, args);
	}
	
}
