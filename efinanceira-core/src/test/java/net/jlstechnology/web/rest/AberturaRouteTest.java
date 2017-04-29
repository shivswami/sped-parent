package net.jlstechnology.web.rest;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira;
import br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.ObjectFactory;
import net.jlstechnology.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.DEFINED_PORT, classes = Application.class)
public class AberturaRouteTest {

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
	 

	@DirtiesContext
	@Test
	public void gerarXMLTest() throws DatatypeConfigurationException {
		
		webClient.path("/abertura/xml");
		Response response = webClient.post(getEfinanceira());
		assertEquals(response.getStatus(), 200);
	}
	
	private static EFinanceira getEfinanceira() throws DatatypeConfigurationException {
		
		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.ObjectFactory factory = new br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.ObjectFactory();

		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira.AberturaMovOpFin.RepresLegal.Telefone telefoneRLegal = factory.createEFinanceiraEvtAberturaeFinanceiraAberturaMovOpFinRepresLegalTelefone();
		telefoneRLegal.setDDD("081");
		telefoneRLegal.setNumero("44447501");

		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira.AberturaMovOpFin.RepresLegal represLegal = factory.createEFinanceiraEvtAberturaeFinanceiraAberturaMovOpFinRepresLegal();
		represLegal.setCPF("10088180468");
		represLegal.setSetor("PR");
		represLegal.setTelefone(telefoneRLegal);

		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira.AberturaMovOpFin.ResponsavelRMF.Endereco endereco = factory.createEFinanceiraEvtAberturaeFinanceiraAberturaMovOpFinResponsavelRMFEndereco();
		endereco.setBairro("Boa Vista");
		endereco.setCEP("50050100");
		endereco.setLogradouro("Rua do Bispo");
		endereco.setMunicipio("2611606");
		endereco.setNumero("58");
		endereco.setUF("PE");

		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira.AberturaMovOpFin.ResponsavelRMF.Telefone telefoneRMF = factory.createEFinanceiraEvtAberturaeFinanceiraAberturaMovOpFinResponsavelRMFTelefone();
		telefoneRMF.setDDD("081");
		telefoneRMF.setNumero("32224879");

		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira.AberturaMovOpFin.ResponsavelRMF responsavelRMF = factory.createEFinanceiraEvtAberturaeFinanceiraAberturaMovOpFinResponsavelRMF();
		responsavelRMF.setCPF("44441465468");
		responsavelRMF.setEndereco(endereco);
		responsavelRMF.setNome("JOSE MOURA DE HOLANDA");
		responsavelRMF.setSetor("FGE");
		responsavelRMF.setTelefone(telefoneRMF);

		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira.AberturaMovOpFin aberturaMovOpFin = factory.createEFinanceiraEvtAberturaeFinanceiraAberturaMovOpFin();
		aberturaMovOpFin.setRepresLegal(represLegal);
		aberturaMovOpFin.setResponsavelRMF(responsavelRMF);

		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira.IdeDeclarante ideDeclarante = factory.createEFinanceiraEvtAberturaeFinanceiraIdeDeclarante();
		ideDeclarante.setCnpjDeclarante("4554552000143");

		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira.IdeEvento ideEvento = factory.createEFinanceiraEvtAberturaeFinanceiraIdeEvento();
		ideEvento.setAplicEmi(1);
		ideEvento.setIndRetificacao(1);
		ideEvento.setTpAmb(2);
		ideEvento.setVerAplic("100");
		
		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira.InfoAbertura infoAbertura = factory.createEFinanceiraEvtAberturaeFinanceiraInfoAbertura();
		infoAbertura.setDtInicio(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(2017, 1, 1, DatatypeConstants.FIELD_UNDEFINED));
		infoAbertura.setDtFim(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(2017, 1, 31, DatatypeConstants.FIELD_UNDEFINED));

		br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira.EvtAberturaeFinanceira evtAberturaeFinanceira = factory.createEFinanceiraEvtAberturaeFinanceira();
		evtAberturaeFinanceira.setAberturaMovOpFin(aberturaMovOpFin);
		evtAberturaeFinanceira.setId("ID10000000000");
		evtAberturaeFinanceira.setIdeDeclarante(ideDeclarante);
		evtAberturaeFinanceira.setIdeEvento(ideEvento);
		evtAberturaeFinanceira.setInfoAbertura(infoAbertura);

		EFinanceira eFinanceira = new ObjectFactory().createEFinanceira();
		eFinanceira.setEvtAberturaeFinanceira(evtAberturaeFinanceira);
		return eFinanceira;		
	}

}
