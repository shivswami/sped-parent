package net.jlstechnology;

import java.security.KeyStore;

import javax.net.ssl.TrustManagerFactory;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	
	private static final String CXF_URL_MAPPING = "/services/efinanceira/*";
	private static final String CXF_SERVLET_NAME = "CXFServlet";
	private static final String ALIAS = "NFe - Associacao NF-e:99999090910270";
	
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
	
	/*@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		tomcat.addAdditionalTomcatConnectors(createStandardConnector());
		return tomcat;
	}

	private Connector createStandardConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		connector.setScheme("https");
		connector.setSecure(true);
		connector.setPort(443);
		protocol.setSSLEnabled(true);
		protocol.setKeystoreProvider("SunMSCAPI");
		protocol.setKeystoreType("Windows-MY");
		protocol.setKeystoreFile("");
		//protocol.setKeystorePass(SENHA_CERTIFICADO);
		protocol.setTruststoreFile(ClassLoader.getSystemResource("eficanceira-cacerts").getPath());
		protocol.setTruststoreType(KeyStore.getDefaultType());
		protocol.setTruststoreAlgorithm(TrustManagerFactory.getDefaultAlgorithm());
		protocol.setTruststorePass("changeit");
		protocol.setTruststoreProvider("SUN");
		protocol.setKeyAlias(ALIAS);
		return connector;
	}*/

}
