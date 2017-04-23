package net.jlstechnology.config;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.DataFormat;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import br.gov.fazenda.sped.WsConsultaSoap;
import br.gov.fazenda.sped.WsRecepcaoSoap;

/**
 * 
 * @author Thiago Tenorio
 *
 */
@Configuration
@PropertySource("file:/c:/Windows/DBSpedSGPrev.properties")
public class CxfEndpointConfig {
	
	private static final QName SERVICE_RECEPCAO = new QName("http://sped.fazenda.gov.br/", "WsRecepcao");
	private static final QName SERVICE_CONSULTA = new QName("http://sped.fazenda.gov.br/", "WsConsulta");
	
	private static final String PROPERTIES_WSDL_RECEPCAO = "efinanceira_wsdl_recepcao";
	private static final String PROPERTIES_WSDL_CONSULTA = "efinanceira_wsdl_consulta";
	
	private static final String WSDL_URL_RECEPCAO = "/wsdl/WsRecepcao.wsdl";
	private static final String WSDL_URL_CONSULTA = "/wsdl/WsConsulta.wsdl";
	
	@Inject
	private Environment env;
	
	@Bean
	public CxfEndpoint wsRecepcaoEndpoint() throws Exception {
		CxfEndpoint endpoint = new CxfEndpoint();
		endpoint.setAddress(env.getProperty(PROPERTIES_WSDL_RECEPCAO));
		endpoint.setWsdlURL(WSDL_URL_RECEPCAO);
		endpoint.setServiceName(SERVICE_RECEPCAO);
		endpoint.setServiceClass(WsRecepcaoSoap.class);
		endpoint.setDataFormat(DataFormat.POJO);
		endpoint.setLoggingFeatureEnabled(true);
		endpoint.getOutInterceptors().add(new LoggingOutInterceptor());
		endpoint.getInInterceptors().add(new LoggingInInterceptor());
		return endpoint;
	}
	
	@Bean
	public CxfEndpoint wsConsultaEndpoint() throws Exception {
		CxfEndpoint endpoint = new CxfEndpoint();
		endpoint.setAddress(env.getProperty(PROPERTIES_WSDL_CONSULTA));
		endpoint.setWsdlURL(WSDL_URL_CONSULTA);
		endpoint.setServiceName(SERVICE_CONSULTA);
		endpoint.setServiceClass(WsConsultaSoap.class);
		endpoint.setDataFormat(DataFormat.POJO);
		endpoint.setLoggingFeatureEnabled(true);
		endpoint.getOutInterceptors().add(new LoggingOutInterceptor());
		endpoint.getInInterceptors().add(new LoggingInInterceptor());
		return endpoint;
	}
	
	@Bean
	public JacksonJsonProvider jacksonJsonProvider() {
		return new JacksonJsonProvider();
	}
	
	@Bean
	public SpringBus cxf() {
		return new SpringBus();
	}

}
