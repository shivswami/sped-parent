package net.jlstechnology.efinanceira.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import net.jlstechnology.efinanceira.service.util.EfinanceiraUtil;

@Component
public class AddLoteEventosProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody(EfinanceiraUtil.adicionarLoteEventos(exchange.getIn().getBody()));
	}

}
