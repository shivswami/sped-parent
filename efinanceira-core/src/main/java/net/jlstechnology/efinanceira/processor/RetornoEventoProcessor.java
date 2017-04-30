package net.jlstechnology.efinanceira.processor;

//import java.util.Calendar;
//
//import javax.inject.Inject;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Unmarshaller;
//
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
//import org.springframework.stereotype.Component;
//import org.w3c.dom.Node;

//import br.com.fachesf.efinanceira.repository.GravarRetornoRepository;
//import br.com.fachesf.efinanceira.repository.GravarRetornoXmlRepository;
//import br.com.fachesf.efinanceira.service.StatusIntegracaoService;

public class RetornoEventoProcessor implements Processor {
	
	/*@Inject
	private GravarRetornoXmlRepository gravarRetornoXMLRepository;
	
	@Inject
	private GravarRetornoRepository gravarRetornoRepository;
	
	@Inject
	private StatusIntegracaoService statusIntegracaoService;*/

	@Override
	public void process(Exchange exchange) throws Exception {
		
		/*final String xml = exchange.getIn().getHeader("XmlAssinado", String.class);
		final String caminhoXmlAssinado = exchange.getIn().getHeader("CamelFileName", String.class);		
		final br.gov.fazenda.sped.ReceberLoteEventoResult receberLoteEventoResult = exchange.getIn().getBody(br.gov.fazenda.sped.ReceberLoteEventoResult.class);

		final Long codigoProcXml = gravarRetornoXMLRepository.inserir(xml, caminhoXmlAssinado);

		for (Object retornoLoteEventosObj : receberLoteEventoResult.getContent()) {
			br.gov.efinanceira.schemas.retornoloteeventos.v1_0_1.EFinanceira retornoloteEventos = (br.gov.efinanceira.schemas.retornoloteeventos.v1_0_1.EFinanceira) retornoLoteEventosObj;

			for (br.gov.efinanceira.schemas.retornoloteeventos.v1_0_1.TArquivoeFinanceira tArquivoeFinanceira: retornoloteEventos.getRetornoLoteEventos().getRetornoEventos().getEvento()) {

				br.gov.efinanceira.schemas.retornoevento.v1_0_1.ObjectFactory factory = new br.gov.efinanceira.schemas.retornoevento.v1_0_1.ObjectFactory();
				br.gov.efinanceira.schemas.retornoevento.v1_0_1.EFinanceira retornoEvento = factory.createEFinanceira();

				JAXBContext jaxbContext = JAXBContext.newInstance(retornoEvento.getClass());
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				br.gov.efinanceira.schemas.retornoevento.v1_0_1.EFinanceira retorno = (br.gov.efinanceira.schemas.retornoevento.v1_0_1.EFinanceira) unmarshaller.unmarshal((Node) tArquivoeFinanceira.getAny());

				if (retorno.getRetornoEvento().getStatus().getDescRetorno().equals("ERRO")) {					

					if (retorno.getRetornoEvento() != null && retorno.getRetornoEvento().getDadosReciboEntrega() != null && retorno.getRetornoEvento().getDadosReciboEntrega().getNumeroRecibo() != null) {

						gravarRetornoRepository.insert(codigoProcXml,
								retorno.getRetornoEvento().getId().replace("ID", ""),
								retorno.getRetornoEvento().getDadosReciboEntrega().getNumeroRecibo(),
								retorno.getRetornoEvento().getStatus().getCdRetorno(),
								retorno.getRetornoEvento().getStatus().getDadosRegistroOcorrenciaEvento().getOcorrencias().get(0).getDescricao(),
								Calendar.getInstance(),
								retorno.getRetornoEvento().getStatus().getDadosRegistroOcorrenciaEvento().getOcorrencias().get(0).getCodigo(),
								retorno.getRetornoEvento().getDadosRecepcaoEvento().getTipoEvento());
					} else {												
						gravarRetornoRepository.insert(codigoProcXml,
								retorno.getRetornoEvento().getId().replace("ID", ""), 
								"0",
								retorno.getRetornoEvento().getStatus().getCdRetorno(),
								retorno.getRetornoEvento().getStatus().getDadosRegistroOcorrenciaEvento().getOcorrencias().get(0).getDescricao(),
								Calendar.getInstance(),
								retorno.getRetornoEvento().getStatus().getDadosRegistroOcorrenciaEvento().getOcorrencias().get(0).getCodigo(),
								retorno.getRetornoEvento().getDadosRecepcaoEvento().getTipoEvento());					
					}					
					statusIntegracaoService.contarRegistroComErro();
				} else {
					gravarRetornoRepository.insert(codigoProcXml,
							retorno.getRetornoEvento().getId().replace("ID", ""),
							retorno.getRetornoEvento().getDadosReciboEntrega().getNumeroRecibo(),
							retorno.getRetornoEvento().getStatus().getCdRetorno(),
							retorno.getRetornoEvento().getStatus().getDescRetorno(), Calendar.getInstance(), 
							null,
							retorno.getRetornoEvento().getDadosRecepcaoEvento().getTipoEvento());					
					statusIntegracaoService.contarRegistroIntegrado();

				}

			}

		}*/

	}

}
