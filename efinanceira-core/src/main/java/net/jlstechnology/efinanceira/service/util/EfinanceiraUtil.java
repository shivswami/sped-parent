package net.jlstechnology.efinanceira.service.util;

/**
 * 
 * @author Thiago Tenorio
 *
 */
public class EfinanceiraUtil {
	
	private EfinanceiraUtil() {}
	
	private static final br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.ObjectFactory FACTORY_ENVIO_LOTES = new br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.ObjectFactory();
	
	/**
	 * Metodo responsavel por adicionar o bean ao lote de eventos
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static <T> br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira adicionarLoteEventos(final T bean) throws Exception {
		
		br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.TArquivoeFinanceira tArquivoeFinanceira = converterTArquivoeFinanceira(bean);
		tArquivoeFinanceira.setId("ID01");
		
		br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira.LoteEventos loteEventos = FACTORY_ENVIO_LOTES.createEFinanceiraLoteEventos();
		loteEventos.getEvento().add(tArquivoeFinanceira);
		
		br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira eFinanceira = FACTORY_ENVIO_LOTES.createEFinanceira();
		eFinanceira.setLoteEventos(loteEventos);
		return eFinanceira;
	}

	public static <T> br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.TArquivoeFinanceira converterTArquivoeFinanceira(T bean) throws Exception {

		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document document = db.newDocument();
		
		javax.xml.bind.JAXBContext jc = javax.xml.bind.JAXBContext.newInstance(bean.getClass());
		javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
		marshaller.marshal(bean, document);
		
		br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.TArquivoeFinanceira tArquivoeFinanceira = FACTORY_ENVIO_LOTES.createTArquivoeFinanceira();
		tArquivoeFinanceira.setAny(document.getDocumentElement());
		return tArquivoeFinanceira;
	}
}
