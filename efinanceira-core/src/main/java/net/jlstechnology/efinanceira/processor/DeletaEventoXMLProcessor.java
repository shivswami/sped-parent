package net.jlstechnology.efinanceira.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DeletaEventoXMLProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws IOException {
		Files.newDirectoryStream(Paths.get(Paths.get(".").toUri().normalize().getPath().substring(1) + exchange.getIn().getHeader("diretorio")))
		.forEach(file -> {
			try {
				Files.delete(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
