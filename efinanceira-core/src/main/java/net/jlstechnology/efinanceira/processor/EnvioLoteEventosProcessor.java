package net.jlstechnology.efinanceira.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class EnvioLoteEventosProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira loteEvento = exchange.getIn().getBody(br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira.class);
		br.gov.fazenda.sped.ObjectFactory factory = new br.gov.fazenda.sped.ObjectFactory();
		br.gov.fazenda.sped.LoteEventos loteEventos = factory.createLoteEventos();
		loteEventos.getContent().add(loteEvento);
		exchange.getIn().setBody(loteEventos);
	}

}
