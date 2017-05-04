package net.jlstechnology.efinanceira.route;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import net.jlstechnology.efinanceira.processor.LoteEventosProcessor;
import net.jlstechnology.efinanceira.processor.RetornoEventoProcessor;
import net.jlstechnology.efinanceira.service.IFechamentoService;
import net.jlstechnology.efinanceira.signature.AssinaturaDigital;

/**
 * 
 * @author Thiago
 *
 */
@Component
public class FechamentoRoute extends SpringRouteBuilder {
	
	@Inject
	private Endpoint wsRecepcaoEndpoint;
	


	@Override
	public void configure() throws Exception {
		
		JaxbDataFormat dataFormat = new JaxbDataFormat(br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.ObjectFactory.class.getPackage().getName());
		dataFormat.setSchema("file:src/main/resources/xsd/evtFechamentoeFinanceira-v1_0_1.xsd");
		dataFormat.setFragment(true);
		
		// API
		from("cxfrs:///fechamento?"
				+ "resourceClasses=" + IFechamentoService.class.getName()	
				+ "&bindingStyle=SimpleConsumer"
				+ "&providers=#jacksonJsonProvider"
				+ "&schemaLocations=#classpath:xsd/evtFechamentoeFinanceira-v1_0_1.xsd").routeId("rota-cxfrs-fechamento")
		  .recipientList(simple("direct:${header.operationName}"));
		
		// ROTA CRIAR XML FECHAMENTO
		from("direct:criarXmlFechamento").routeId("rota-criar-xml-fechamento")
		.to("file:target/www/xml/fechamento/nao_assinado/?fileName=evtFechamentoeFinanceira.xml&charset=utf-8")
		    .pollEnrich("file:target/www/xml/fechamento/nao_assinado/?fileName=evtFechamentoeFinanceira.xml&charset=utf-8")
		        .unmarshal(dataFormat) 
		        .process(new LoteEventosProcessor())
		            .end()
		        .setHeader("nomeElemento", constant("evtFechamentoeFinanceira"))
		        .bean(AssinaturaDigital.class, "assinar")
		    .to("file:target/www/xml/fechamento/assinado/?fileName=evtFechamentoeFinanceira-ASSINADO.xml&charset=utf-8")
		    .  setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
			  .setBody(simple("null"))
			.to("mock:result");
		
		// ROTA EXCLUIR XML FECHAMENTO
		from("direct:deletarXmlFechamento").routeId("rota-deletar-xml-fechamento")
		  .pollEnrich("file:target/www/xml/fechamento/assinado/?fileName=evtFechamentoeFinanceira-ASSINADO.xml&delete=true")
		    .end()
	        .setBody(simple("null"))
	        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))		    
		.to("mock:result");
		
		// TRANSMITIR
		from("direct:transmitirXmlFechamento?timeout=100000").routeId("rota-transmitir-xml-fechamento")
		  .pollEnrich("file:target/www/xml/fechamento/assinado/?fileName=evtFechamentoeFinanceira-ASSINADO.xml&charset=utf-8")
		  .setHeader("XmlAssinado", simple("${body}"))
		      .process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira loteEvento = exchange.getIn().getBody(br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira.class);
						br.gov.fazenda.sped.ObjectFactory factory = new br.gov.fazenda.sped.ObjectFactory();
						br.gov.fazenda.sped.LoteEventos loteEventos = factory.createLoteEventos();
						loteEventos.getContent().add(loteEvento);
						exchange.getIn().setBody(loteEventos);
					}
				}).to(wsRecepcaoEndpoint)
		        .setBody(bodyAs(br.gov.fazenda.sped.ReceberLoteEventoResult.class))
				.process(new RetornoEventoProcessor())
				.end()
		        .setBody(simple("null"))
		        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))		    
			.to("mock:result");
	}

}
