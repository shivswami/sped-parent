package net.jlstechnology.efinanceira.service;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira;

//@Path("/abertura")
public interface IAberturaService {
	
	@POST
    @Path("/xml")
    Response gerarXmlAbertura(EFinanceira abertura);
	
	@DELETE
    @Path("/xml")
    Response excluirXmlAbertura(@QueryParam("nomeArquivo") String nomeArquivo);
	
	@POST
    @Path("/xml/transmissao")
	br.gov.efinanceira.schemas.retornoloteeventos.v1_0_1.ObjectFactory transmitirXmlAbertura();

}
