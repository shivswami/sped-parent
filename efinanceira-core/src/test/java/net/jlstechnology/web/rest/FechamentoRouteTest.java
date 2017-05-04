package net.jlstechnology.web.rest;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;

import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;

import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import net.jlstechnology.Application;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.DEFINED_PORT, classes = Application.class)
public class FechamentoRouteTest {

	private static final String REST_URI_ABERTURA = "http://localhost:8080/services/efinanceira";
	
	private WebClient webClient;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	    JacksonJaxbJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider();

	    webClient = WebClient.create( REST_URI_ABERTURA, Arrays.asList(jacksonJsonProvider));
	    WebClient.getConfig( webClient ).getRequestContext()/*.put( LocalConduit.DIRECT_DISPATCH, Boolean.TRUE )*/;
	    webClient.accept( "application/json" );	    
	}
	 
	@Test
	public void gerarXMLTest() throws DatatypeConfigurationException {		
		webClient.path("/fechamento/xml");
		Response response = webClient.post(getEfinanceira());
		assertEquals(response.getStatus(), 200);
	}
	
	@Test
	public void transmitirXMLTest() throws DatatypeConfigurationException {
		webClient.path("/fechamento/xml/transmissao");
		Response response = webClient.post(null);
		assertEquals(response.getStatus(), 500);
	}
	
	@Test
	public void deletarXMLTest() throws DatatypeConfigurationException {
		webClient.path("/fechamento/xml");
		Response response = webClient.delete();
		assertEquals(response.getStatus(), 200);
	}
	
	private static br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira getEfinanceira() throws DatatypeConfigurationException {
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.ObjectFactory factory = new br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.ObjectFactory();
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira.EvtFechamentoeFinanceira.InfoFechamento infoFechamento = factory.createEFinanceiraEvtFechamentoeFinanceiraInfoFechamento();
		infoFechamento.setDtFim(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(2017, 1, 1, DatatypeConstants.FIELD_UNDEFINED));
		infoFechamento.setDtInicio(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(2017, 1, 1, DatatypeConstants.FIELD_UNDEFINED));
		infoFechamento.setSitEspecial(2);
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira.EvtFechamentoeFinanceira.IdeEvento ideEvento = factory.createEFinanceiraEvtFechamentoeFinanceiraIdeEvento();
		ideEvento.setAplicEmi(2);
		ideEvento.setIndRetificacao(1);
		ideEvento.setTpAmb(1);
		ideEvento.setVerAplic("100");
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira.EvtFechamentoeFinanceira.IdeDeclarante ideDeclarante = factory.createEFinanceiraEvtFechamentoeFinanceiraIdeDeclarante();
		ideDeclarante.setCnpjDeclarante("456578456");
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira.EvtFechamentoeFinanceira.FechamentoPP.FechamentoMes fechamentoMesPP = factory.createEFinanceiraEvtFechamentoeFinanceiraFechamentoPPFechamentoMes();
		fechamentoMesPP.setAnoMesCaixa("201701");
		fechamentoMesPP.setQuantArqTrans(BigInteger.valueOf(120));
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira.EvtFechamentoeFinanceira.FechamentoPP fechamentoPP = factory.createEFinanceiraEvtFechamentoeFinanceiraFechamentoPP();
		fechamentoPP.getFechamentoMes().add(fechamentoMesPP);
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira.EvtFechamentoeFinanceira.FechamentoMovOpFin.FechamentoMes fechamentoMesMovOpFin = factory.createEFinanceiraEvtFechamentoeFinanceiraFechamentoMovOpFinFechamentoMes();
		fechamentoMesMovOpFin.setAnoMesCaixa("201701");
		fechamentoMesMovOpFin.setQuantArqTrans(BigInteger.valueOf(120));
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira.EvtFechamentoeFinanceira.FechamentoMovOpFin.ReportavelExterior reportavelExterior = factory.createEFinanceiraEvtFechamentoeFinanceiraFechamentoMovOpFinReportavelExterior();
		reportavelExterior.setPais("US");
		reportavelExterior.setReportavel((short) 1);
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira.EvtFechamentoeFinanceira.FechamentoMovOpFin fechamentoMovOpFin = factory.createEFinanceiraEvtFechamentoeFinanceiraFechamentoMovOpFin();
		fechamentoMovOpFin.getFechamentoMes().add(fechamentoMesMovOpFin);
		fechamentoMovOpFin.getReportavelExterior().add(reportavelExterior);
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira.EvtFechamentoeFinanceira evtFechamentoeFinanceira = factory.createEFinanceiraEvtFechamentoeFinanceira();
		evtFechamentoeFinanceira.setFechamentoMovOpFin(fechamentoMovOpFin);
		evtFechamentoeFinanceira.setFechamentoPP(fechamentoPP);
		evtFechamentoeFinanceira.setId("ID10000000000");
		evtFechamentoeFinanceira.setIdeDeclarante(ideDeclarante);
		evtFechamentoeFinanceira.setIdeEvento(ideEvento);
		evtFechamentoeFinanceira.setInfoFechamento(infoFechamento);
		
		br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira eFinanceira = factory.createEFinanceira();
		eFinanceira.setEvtFechamentoeFinanceira(evtFechamentoeFinanceira);
		return eFinanceira;		
	}

}
