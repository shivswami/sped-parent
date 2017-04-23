package net.jlstechnology.efinanceira.route;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import net.jlstechnology.efinanceira.processor.AddLoteEventosProcessor;
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
	
	@Inject
	private AddLoteEventosProcessor addLoteEventosProcessor;
	
	@Inject
	private Processor retornoEventoProcessor;

	@Override
	public void configure() throws Exception {
		
//		JaxbDataFormat dataFormat2 = new JaxbDataFormat(br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.ObjectFactory.class.getPackage().getName());
//		dataFormat2.setSchema("file:src/main/resources/xsd/envioLoteEventos-v1_0_1.xsd");
//		dataFormat2.setFragment(true);
//		
//		JaxbDataFormat dataFormat = new JaxbDataFormat(ObjectFactory.class.getPackage().getName());
//		dataFormat.setSchema("file:src/main/resources/xsd/evtAberturaeFinanceira-v1_0_1.xsd");
//		dataFormat.setFragment(true);
		
		// API
		from("cxfrs:///abertura?"
				+ "resourceClasses=" + IAberturaService.class.getName()	
				+ "&bindingStyle=SimpleConsumer"
				+ "&providers=#jacksonJsonProvider"
				+ "&schemaLocations=#classpath:xsd/evtAberturaeFinanceira-v1_0_1.xsd").routeId("rota-cxfrs-abertura")
		  .recipientList(simple("seda:${header.operationName}"));
		
		// ROTA CRIAR XML ABERTURA
		from("seda:gerarXmlAbertura").routeId("rota-gerar-abertura-xml")		              
		  .to("file:target/www/xml/abertura/nao_assinado/?fileName=evtAberturaeFinanceira.xml&charset=utf-8")
		  .pollEnrich("file:target/www/xml/abertura/nao_assinado/?fileName=evtAberturaeFinanceira.xml&charset=utf-8")
		    //.unmarshal(dataFormat)
		  .unmarshal().jaxb()
		      .process(addLoteEventosProcessor)
		        .end()
		  .setHeader("nomeElemento", constant("evtAberturaeFinanceira"))
		  .bean(AssinaturaDigital.class, "assinar")
		.to("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&charset=utf-8");
		
		// ROTA EXCLUIR XML ABERTURA
		from("seda:excluirXmlAbertura").routeId("rota-excluir-abertura-xml")
		  .pollEnrich("file:target/www/xml/abertura/assinado/?fileName=${body}&delete=true")
		  .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
		  .setBody(simple("null"))
		.to("log:INFO");
		//.log(LoggingLevel.INFO, simple("${in.header.nomeArquivo}"))
		//.to("log:TEST?showAll=true");
//		from("seda:excluirXmlAbertura").routeId("rota-excluir-abertura-xml")
//		  .pollEnrich("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&delete=true")
//		  .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
//		  .setBody(simple("null"))
//		.to("log:INFO");
		
		// TRANSMITIR
		from("seda:transmitirXmlAbertura?timeout=100000").routeId("rota-transmitir-abertura")
		  .pollEnrich("file:target/www/xml/abertura/assinado/?fileName=evtAberturaeFinanceira-ASSINADO.xml&charset=utf-8")
		  .setHeader("XmlAssinado", simple("${body}"))
		    //.unmarshal(dataFormat2)
		      .unmarshal().jaxb()
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
				.process(retornoEventoProcessor)
				  .end()
				  .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
	}

}
