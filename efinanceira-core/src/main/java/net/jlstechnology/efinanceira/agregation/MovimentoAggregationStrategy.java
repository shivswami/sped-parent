package net.jlstechnology.efinanceira.agregation;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import jersey.repackaged.com.google.common.collect.Lists;

public class MovimentoAggregationStrategy implements AggregationStrategy {

	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		
		final Object value = newExchange.getIn().getBody();
		
        if (oldExchange == null) {
            newExchange.getIn().setBody(Lists.newArrayList(value));
            return newExchange;
        } else {
            oldExchange.getIn().getBody(List.class).add(value);
            return oldExchange;
        }
	}

}
