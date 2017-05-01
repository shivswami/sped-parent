package net.jlstechnology.efinanceira.service;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public interface IAberturaService {
	
	@POST
    @Path("/xml")
    Response criarXmlAbertura(br.gov.efinanceira.schemas.evtaberturaefinanceira.v1_0_1.EFinanceira abertura);
	
	@DELETE
    @Path("/xml")
    Response deletarXmlAbertura();
	
	@POST
    @Path("/xml/transmissao")
	br.gov.efinanceira.schemas.retornoloteeventos.v1_0_1.ObjectFactory transmitirXmlAbertura();

}
