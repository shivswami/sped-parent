package net.jlstechnology.efinanceira.converter;

import java.util.UUID;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.FallbackConverter;
import org.apache.camel.TypeConverter;
import org.apache.camel.spi.TypeConverterRegistry;

@Converter
public class LoteEventosConverters {
	
	private static final br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.ObjectFactory FACTORY_ENVIO_LOTES = new br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.ObjectFactory();
	
	 @FallbackConverter
	    public static <T> T convertTo(Class<T> type, Exchange exchange, Object value, TypeConverterRegistry registry) throws ParserConfigurationException, JAXBException {
        br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.TArquivoeFinanceira tArquivoeFinanceira = converterTArquivoeFinanceira(null);
		tArquivoeFinanceira.setId("ID01");
		
		br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira.LoteEventos loteEventos = FACTORY_ENVIO_LOTES.createEFinanceiraLoteEventos();
		loteEventos.getEvento().add(tArquivoeFinanceira);
		
		br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira eFinanceira = FACTORY_ENVIO_LOTES.createEFinanceira();
		eFinanceira.setLoteEventos(loteEventos);
		
		return null;// typeConverter.convertTo(br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira.class, eFinanceira);
    }
    
    public static <T> br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.TArquivoeFinanceira converterTArquivoeFinanceira(T bean) throws ParserConfigurationException, JAXBException  {

		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document document = db.newDocument();
		
		javax.xml.bind.JAXBContext jc = javax.xml.bind.JAXBContext.newInstance(bean.getClass());
		javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
		marshaller.marshal(bean, document);
		
		br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.TArquivoeFinanceira tArquivoeFinanceira = FACTORY_ENVIO_LOTES.createTArquivoeFinanceira();
		tArquivoeFinanceira.setAny(document.getDocumentElement());
		tArquivoeFinanceira.setId(UUID.randomUUID().toString());
		return tArquivoeFinanceira;
	}

}
