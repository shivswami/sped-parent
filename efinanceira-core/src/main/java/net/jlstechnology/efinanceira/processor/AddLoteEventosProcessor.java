package net.jlstechnology.efinanceira.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import net.jlstechnology.efinanceira.service.util.EfinanceiraUtil;

public class AddLoteEventosProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody(EfinanceiraUtil.adicionarLoteEventos(exchange.getIn().getBody()));
	}

}
