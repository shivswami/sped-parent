package net.jlstechnology.efinanceira.service;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface IFechamentoService {
	
	@POST
    @Path("/xml")
	@Produces(value = MediaType.APPLICATION_JSON)
    Response criarXmlFechamento(br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira abertura);
	
	@DELETE
    @Path("/xml")
	@Produces(value = MediaType.APPLICATION_JSON)
    Response deletarXmlFechamento();
	
	@POST
    @Path("/xml/transmissao")
	@Produces(value = MediaType.APPLICATION_JSON)
	br.gov.efinanceira.schemas.retornoloteeventos.v1_0_1.ObjectFactory transmitirXmlFechamento();

}
