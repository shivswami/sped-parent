package net.jlstechnology.efinanceira.service;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.gov.efinanceira.schemas.evtfechamentoefinanceira.v1_0_1.EFinanceira;

public interface IFechamentoService {
	
	@POST
    @Path("/xml")
    Response criarXmlFechamento(EFinanceira abertura);
	
	@DELETE
    @Path("/xml")
    Response deletarXmlFechamento();
	
	@POST
    @Path("/xml/transmissao")
	br.gov.efinanceira.schemas.retornoloteeventos.v1_0_1.ObjectFactory enviarXmlFechamento();

}
