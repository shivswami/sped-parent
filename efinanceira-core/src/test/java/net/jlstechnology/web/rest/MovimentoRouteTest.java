package net.jlstechnology.web.rest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import net.jlstechnology.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.DEFINED_PORT, classes = Application.class)
public class MovimentoRouteTest {

	private static final String REST_URI_ABERTURA = "http://localhost:8080/services/efinanceira";
	
	private WebClient webClient;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	    JacksonJaxbJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider();

	    webClient = WebClient.create( REST_URI_ABERTURA, Arrays.asList(jacksonJsonProvider));
	    WebClient.getConfig( webClient ).getRequestContext()/*.put( LocalConduit.DIRECT_DISPATCH, Boolean.TRUE )*/;
	    webClient.type(MediaType.APPLICATION_JSON);	    
	}
	 
	@Test
	public void gerarXMLTest() throws DatatypeConfigurationException {
		webClient.path("/movimento/xml");
		Response response = webClient.postCollection(getEfinanceiras(), br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.class);
		assertEquals(response.getStatus(), 200);
	}
	
//	@Test
//	public void transmitirXMLTest() throws DatatypeConfigurationException {
//		webClient.path("/abertura/xml/transmissao");
//		Response response = webClient.post(null);
//		assertEquals(response.getStatus(), 500);
//	}
//	
//	@Test
//	public void deletarXMLTest() throws DatatypeConfigurationException {
//		webClient.path("/abertura/xml");
//		Response response = webClient.delete();
//		assertEquals(response.getStatus(), 200);
//	}
	
	private static List<br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira> getEfinanceiras() throws DatatypeConfigurationException {
		List<br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira> eFinanceiras = new ArrayList<>();

		for (int i = 0; i <= 1001; i++) {
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.ObjectFactory factory = new br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.ObjectFactory();
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.PaisEndereco paisEndereco = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoPaisEndereco();
			paisEndereco.setPais("BR");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.NIF nif = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoNIF();
			nif.setNumeroNIF("187");
			nif.setPaisEmissaoNIF("BR");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.PaisNacionalidade paisNacionalidade = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoPaisNacionalidade();
			paisNacionalidade.setPais("BR");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.PaisResid paisResid = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoPaisResid();
			paisResid.setPais("BR");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.Proprietarios.PaisEndereco paisEndereco2 = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoProprietariosPaisEndereco();
			paisEndereco2.setPais("BR");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.Proprietarios.NIF nif2 = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoProprietariosNIF();
			nif2.setNumeroNIF("NFIP");
			nif2.setPaisEmissaoNIF("BR");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.Proprietarios.PaisNacionalidade paisNacionalidade3 = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoProprietariosPaisNacionalidade();
			paisNacionalidade3.setPais("BR");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.Proprietarios.PaisResid paisResid2 = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoProprietariosPaisResid();
			paisResid2.setPais("BR");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.Proprietarios.Reportavel reportavel = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoProprietariosReportavel();
			reportavel.setPais("BR");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.Proprietarios proprietarios = factory.createEFinanceiraEvtMovOpFinIdeDeclaradoProprietarios();
			proprietarios.setDataNasc(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(1980, 1, 1, DatatypeConstants.FIELD_UNDEFINED));
			proprietarios.setEnderecoLivre("Rua Tereza");
			proprietarios.setNIProprietario("NIP");
			proprietarios.setNome("Nome Proprietario");
			proprietarios.setPaisEndereco(paisEndereco2);
			proprietarios.setTpNI((short) 'S');
			proprietarios.getNIF().add(nif2);
			proprietarios.getPaisNacionalidade().add(paisNacionalidade3);
			proprietarios.getPaisResid().add(paisResid2);
			proprietarios.getReportavel().add(reportavel);
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado ideDeclarado = factory.createEFinanceiraEvtMovOpFinIdeDeclarado();
			ideDeclarado.setDataNasc(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(1980, 1, 1, DatatypeConstants.FIELD_UNDEFINED));
			ideDeclarado.setEnderecoLivre("Rua Bispo, 187");
			ideDeclarado.setNIDeclarado("NID");
			ideDeclarado.setNomeDeclarado("Nome declarado");
			ideDeclarado.setPaisEndereco(paisEndereco);
			ideDeclarado.getNIF().add(nif);
			ideDeclarado.getPaisNacionalidade().add(paisNacionalidade);
			ideDeclarado.getPaisResid().add(paisResid);
			ideDeclarado.getProprietarios().add(proprietarios);
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarante ideDeclarante = factory.createEFinanceiraEvtMovOpFinIdeDeclarante();
			ideDeclarante.setCnpjDeclarante("44547845478");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeEvento ideEvento = factory.createEFinanceiraEvtMovOpFinIdeEvento();
			ideEvento.setAplicEmi(1);
			ideEvento.setIndRetificacao(2);
			ideEvento.setTpAmb(2);
			ideEvento.setVerAplic("100");
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa mesCaixa = factory.createEFinanceiraEvtMovOpFinMesCaixa();
			mesCaixa.setAnoMesCaixa("janeiro");
			//mesCaixa.setMovOpFin(value);
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin evtMovOpFin = factory.createEFinanceiraEvtMovOpFin();
			evtMovOpFin.setId(UUID.randomUUID().toString());
			evtMovOpFin.setIdeDeclarado(ideDeclarado);
			evtMovOpFin.setIdeDeclarante(ideDeclarante);
			evtMovOpFin.setIdeEvento(ideEvento);
			evtMovOpFin.setMesCaixa(mesCaixa);
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira eFinanceira = factory.createEFinanceira();
			eFinanceira.setEvtMovOpFin(evtMovOpFin);
			
			eFinanceiras.add(eFinanceira);			
		}
		return eFinanceiras;		
	}

}
