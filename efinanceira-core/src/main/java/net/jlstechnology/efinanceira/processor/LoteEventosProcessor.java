package net.jlstechnology.efinanceira.processor;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import net.jlstechnology.efinanceira.service.util.EfinanceiraUtil;

public class LoteEventosProcessor implements Processor {

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		if (exchange.getIn().getBody().getClass().isAssignableFrom(ArrayList.class)) {			
			exchange.getIn().setBody(EfinanceiraUtil.adicionarLoteEventos(exchange.getIn().getBody(ArrayList.class)));
		} else {
			exchange.getIn().setBody(EfinanceiraUtil.adicionarLoteEventos(exchange.getIn().getBody()));
		}
		
	}

}
