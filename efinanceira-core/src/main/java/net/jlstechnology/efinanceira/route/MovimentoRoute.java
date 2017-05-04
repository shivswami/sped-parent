package net.jlstechnology.efinanceira.route;

import javax.inject.Inject;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

import net.jlstechnology.efinanceira.agregation.MovimentoAggregationStrategy;
import net.jlstechnology.efinanceira.processor.EnvioLoteEventosProcessor;
import net.jlstechnology.efinanceira.processor.LoteEventosProcessor;
import net.jlstechnology.efinanceira.processor.RetornoEventoProcessor;
import net.jlstechnology.efinanceira.service.IMovimentoService;
import net.jlstechnology.efinanceira.signature.AssinaturaDigital;

/**
 * 
 * @author Thiago
 *
 */
@Component
public class MovimentoRoute extends SpringRouteBuilder {
	
	@Inject
	private Endpoint wsRecepcaoEndpoint;

	@Override
	public void configure() throws Exception {
		
		JaxbDataFormat dataFormat = new JaxbDataFormat(br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.ObjectFactory.class.getPackage().getName());
		dataFormat.setSchema("file:src/main/resources/xsd/evtMovOpFin-v1_0_1.xsd");
		dataFormat.setFragment(true);
		
		// API
		from("cxfrs:///movimento?"
				+ "resourceClasses=" + IMovimentoService.class.getName()	
				+ "&bindingStyle=SimpleConsumer"
				+ "&providers=#jacksonJsonProvider"
				+ "&schemaLocations=#classpath:xsd/evtMovOpFin-v1_0_1.xsd").routeId("rota-cxfrs-movimento")
		  .recipientList(simple("direct:${header.operationName}"));
		
		// ROTA CRIAR XML MOVIMENTO
		from("direct:criarXmlMovimento").routeId("rota-criar-xml-movimento")
	        .split(body())
	            .aggregate()
	              .constant(true)		
		           .aggregationStrategy(new MovimentoAggregationStrategy())
	                .completionSize(100)
	                  .completionTimeout(100)
	                  .process(new LoteEventosProcessor())
		.to("file:target/www/xml/movimento/nao_assinado/?fileName=evtMovOpFin-${date:now:yyyyMMddHHmmssSSS}.xml&charset=utf-8")
		    .pollEnrich("file:target/www/xml/movimento/nao_assinado/?charset=utf-8")
		        
		        .setHeader("nomeElemento", constant("evtMovOpFin"))
		        .bean(AssinaturaDigital.class, "assinar")
		    .to("file:target/www/xml/movimento/assinado/?fileName=evtMovOpFin-${date:now:yyyyMMddHHmmssSSS}-ASSINADO.xml&charset=utf-8")
		    .end()
		    .setBody(simple("null"))
		    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))		    
			.to("mock:result");
		
		// ROTA EXCLUIR XML MOVIMENTO
		from("seda:deletarXmlMovimento").routeId("rota-deletar-xml-movimento")
		  .pollEnrich("file:target/www/xml/movimento/assinado/?fileName=evtMovOpFin-${date:now:yyyyMMddHHmmssSSS}-ASSINADO.xml&delete=true")
		  .end()
		    .setBody(simple("null"))
		    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))		    
			.to("mock:result");
		
		// TRANSMITIR
		from("seda:transmitirXmlMovimento?timeout=100000").routeId("rota-transmitir-xml-movimento")
		  .pollEnrich("file:target/www/xml/movimento/assinado/?fileName=evtMovOpFin-${date:now:yyyyMMddHHmmssSSS}-ASSINADO.xml&charset=utf-8")
		  .setHeader("XmlAssinado", simple("${body}"))
		      .process(new EnvioLoteEventosProcessor())
		        .to(wsRecepcaoEndpoint)
		        .setBody(bodyAs(br.gov.fazenda.sped.ReceberLoteEventoResult.class))
				.process(new RetornoEventoProcessor())
				.end()
			    .setBody(simple("null"))
			    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))		    
				.to("mock:result");
	}

}
