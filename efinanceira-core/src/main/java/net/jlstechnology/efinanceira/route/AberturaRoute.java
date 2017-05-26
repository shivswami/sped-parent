package net.jlstechnology.efinanceira.route;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import net.jlstechnology.efinanceira.converter.LoteEventosConverters;
import net.jlstechnology.efinanceira.processor.EnvioLoteEventosProcessor;
import net.jlstechnology.efinanceira.processor.RetornoEventoProcessor;
import net.jlstechnology.efinanceira.service.IAberturaService;
import net.jlstechnology.efinanceira.signature.AssinaturaDigital;

/**
 * 
 * @author Thiago
 *
 */
@Component
public class AberturaRoute extends SpringRouteBuilder {
	
	@Inject
	private Endpoint wsRecepcaoEndpoint;

	@Override
	public void configure() throws Exception {
		
		JaxbDataFormat df = new JaxbDataFormat(br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.ObjectFactory.class.getPackage().getName());
		df.setSchema("file:src/main/resources/xsd/evtAberturaeFinanceira-v1_0_1.xsd");
		df.setFragment(true);
		
		//JaxbDataFormat dfLoteEventos = new JaxbDataFormat(br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.ObjectFactory.class.getPackage().getName());
		//dfAbertura.setSchema("file:src/main/resources/xsd/envioLoteEventos-v1_0_1.xsd");
		//dfAbertura.setFragment(true);

		// API
		from("cxfrs:///abertura?"
		  + "resourceClasses=" + IAberturaService.class.getName()	
		  + "&bindingStyle=SimpleConsumer"
		  + "&providers=#jacksonJsonProvider"
		  + "&schemaLocations=#classpath:xsd/evtAberturaeFinanceira-v1_0_1.xsd").routeId("rota-cxfrs-abertura")
		.recipientList(simple("direct:${header.operationName}"));
		
		// ROTA CRIAR XML ABERTURA
		from("direct:criarXmlAbertura").routeId("rota-criar-xml-abertura")
		.to("file:target/www/xml/abertura/nao_assinado/?fileName=evtAberturaeFinanceira.xml&charset=utf-8")
		    .pollEnrich("file:target/www/xml/abertura/nao_assinado/?fileName=evtAberturaeFinanceira.xml&charset=utf-8")
		        .unmarshal(df)
		        .convertBodyTo(LoteEventosConverters.class)
		        //.process(new LoteEventosProcessor())
		            .end()
		        .setHeader("nomeElemento", constant("evtAberturaeFinanceira"))
		        .bean(AssinaturaDigital.class, "assinar")
		    .to("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&charset=utf-8")
	        .setBody(simple("null"))
	        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))		    
		.to("mock:result");
		
		// ROTA EXCLUIR XML ABERTURA
		from("direct:deletarXmlAbertura").routeId("rota-deletar-xml-abertura")
		//.to("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&eagerDeleteTargetFile=true")
		  //.setHeader("diretorio", constant("target/www/xml/abertura/assinado"))
		.pollEnrich("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&delete=true")
		.convertBodyTo(String.class)
		//.unmarshal(dfLoteEventos)
		//.setBody(simple("null"))
	        //.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))		    
		.to("mock:result");
		
		// TRANSMITIR
		from("direct:transmitirXmlAbertura").routeId("rota-transmitir-xml-abertura")
		  .pollEnrich("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&delete=true&charset=utf-8")
		  .setHeader("XmlAssinado", simple("${body}"))
		      .process(new EnvioLoteEventosProcessor())
		        .to(wsRecepcaoEndpoint)
		        .setBody(bodyAs(br.gov.fazenda.sped.ReceberLoteEventoResult.class))
				.process(new RetornoEventoProcessor())
		        .setBody(simple("null"))
		        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))		    
			.to("mock:result");
	}

}
