package net.jlstechnology.web.rest;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;

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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = Application.class)
public class ConsultaEFinanceiraTest {

	private static final String REST_URI = "http://localhost:8080/services/efinanceira";

	private WebClient webClient;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	    JacksonJaxbJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider();

	    webClient = WebClient.create( REST_URI, Arrays.asList(jacksonJsonProvider));
	    WebClient.getConfig( webClient ).getRequestContext()/*.put( LocalConduit.DIRECT_DISPATCH, Boolean.TRUE )*/;
	    webClient.accept( "application/json" );	    
	}
	
	@Test
	public void consultarInformacoesCadastraisTest() throws DatatypeConfigurationException {		
		webClient.path("/consulta/informacoes/cadastrais/xml");
		Response response = webClient.post("45454545454");
		assertEquals(response.getStatus(), 200);
	}

	private static br.gov.fazenda.sped.ConsultarInformacoesCadastrais getConsultarInformacoesCadastrais() {
		br.gov.fazenda.sped.ConsultarInformacoesCadastrais consulta = new br.gov.fazenda.sped.ConsultarInformacoesCadastrais();
		consulta.setCnpj("0455698745");
		return consulta;
	}

}
