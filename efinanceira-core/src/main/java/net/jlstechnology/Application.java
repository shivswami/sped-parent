package net.jlstechnology;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	
	private static final String CXF_URL_MAPPING = "/services/efinanceira/*";
	private static final String CXF_SERVLET_NAME = "CXFServlet";
	
	Application() {}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new CXFServlet(), CXF_URL_MAPPING);
		registration.setName(CXF_SERVLET_NAME);
		return registration;
	}

}
