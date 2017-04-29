package net.jlstechnology.efinanceira.route;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import net.jlstechnology.efinanceira.processor.AddEnvioLoteEventosProcessor;
import net.jlstechnology.efinanceira.processor.AddLoteEventosProcessor;
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
		
		JaxbDataFormat dfAbertura = new JaxbDataFormat(br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.ObjectFactory.class.getPackage().getName());
		dfAbertura.setSchema("file:src/main/resources/xsd/evtAberturaeFinanceira-v1_0_1.xsd");
		dfAbertura.setFragment(true);
		
		// API
		from("cxfrs:///abertura?"
				+ "resourceClasses=" + IAberturaService.class.getName()	
				+ "&bindingStyle=SimpleConsumer"
				+ "&providers=#jacksonJsonProvider"
				+ "&schemaLocations=#classpath:xsd/evtAberturaeFinanceira-v1_0_1.xsd").routeId("rota-cxfrs-abertura")
		  .recipientList(simple("seda:${header.operationName}"));
		
		// ROTA CRIAR XML ABERTURA
		from("seda:criarXmlAbertura").routeId("rota-criar-xml-abertura")
		.to("file:target/www/xml/abertura/nao_assinado/?fileName=evtAberturaeFinanceira.xml&charset=utf-8")
		    .pollEnrich("file:target/www/xml/abertura/nao_assinado/?fileName=evtAberturaeFinanceira.xml&charset=utf-8")
		        .unmarshal(dfAbertura) 
		        .process(new AddLoteEventosProcessor())
		            .end()
		        .setHeader("nomeElemento", constant("evtAberturaeFinanceira"))
		        .bean(AssinaturaDigital.class, "assinar")
		    .to("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&charset=utf-8");
		
		// ROTA EXCLUIR XML ABERTURA
		from("seda:deletarXmlAbertura").routeId("rota-deletar-xml-abertura")
		  .pollEnrich("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&delete=true")
		  .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
		  .setBody(simple("null"))
		.to("log:INFO");
		
		// TRANSMITIR
		from("seda:transmitirXmlAbertura?timeout=100000").routeId("rota-transmitir-xml-abertura")
		  .pollEnrich("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&charset=utf-8")
		  .setHeader("XmlAssinado", simple("${body}"))
		      .process(new AddEnvioLoteEventosProcessor())
		        .to(wsRecepcaoEndpoint)
		        .setBody(bodyAs(br.gov.fazenda.sped.ReceberLoteEventoResult.class))
				.process(new RetornoEventoProcessor())
				  .end()
				  .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
	}

}
