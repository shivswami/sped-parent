package net.jlstechnology.efinanceira.route;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import net.jlstechnology.efinanceira.service.IConsultaEFinanceira;

@Component
public class ConsultaEFinanceiraRoute extends SpringRouteBuilder {
	
	@Inject
	private CxfEndpoint wsConsultaEndpoint;

	@Override
	public void configure() throws Exception {
		
		//wsConsultaEndpoint.setDefaultOperationName("ConsultarInformacoesCadastrais");
		//JaxbDataFormat df = new JaxbDataFormat(br.gov.fazenda.sped.ObjectFactory.class.getPackage().getName());
		//df.setSchema("file:src/main/resources/xsd/consulta/retornoConsultaInformacoesCadastrais-v1_0_1.xsd");
		//df.setFragment(true);
		
		// API
		from("cxfrs:///consulta?"
			+ "resourceClasses=" + IConsultaEFinanceira.class.getName()	
			+ "&bindingStyle=SimpleConsumer"
			+ "&providers=#jacksonJsonProvider"
			/*+ "&schemaLocations=#classpath:xsd/consulta/retornoConsultaInformacoesCadastrais-v1_0_1.xsd"*/)
		.routeId("rota-cxfrs-consulta-informacoes-cadastrais")
		.recipientList(simple("direct:${header.operationName}"));
		
		from("direct:consultarInformacoesCadastrais")
		  .routeId("rota-cxfrs-consultar-informacoes-cadastrais")
		  .setHeader(CxfConstants.OPERATION_NAME, constant("ConsultarInformacoesCadastrais"))
		  .log(LoggingLevel.INFO, "${body}")
		.to(wsConsultaEndpoint);
		
		from("direct:consultarInformacoesIntermediario")
		  .routeId("rota-cxfrs-consultar-informacoes-intermediario")
		  .setHeader(CxfConstants.OPERATION_NAME, constant("ConsultarInformacoesIntermediario"))
		  .log(LoggingLevel.INFO, "${body}")
		.to(wsConsultaEndpoint);
		
		from("direct:consultarInformacoesMovimento")
		  .routeId("rota-cxfrs-consultar-informacoes-movimento")
		  .setHeader(CxfConstants.OPERATION_NAME, constant("ConsultarInformacoesMovimento"))
		  .log(LoggingLevel.INFO, "${body}")
		.to(wsConsultaEndpoint);
		
		from("direct:ConsultarInformacoesPatrocinado")
		  .routeId("rota-cxfrs-consultar-informacoes-patrocinado")
		  .setHeader(CxfConstants.OPERATION_NAME, constant("ConsultarInformacoesPatrocinado"))
		  .log(LoggingLevel.INFO, "${body}")
		.to(wsConsultaEndpoint);
		
	}

}
