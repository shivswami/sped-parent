package net.jlstechnology.efinanceira.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface IMovimentoService {

	@POST
	@Path("/xml")
	@Produces(value = MediaType.APPLICATION_JSON)
	Response criarXmlMovimento(List<br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira> movimentos);

	@DELETE
	@Path("/xml")
	Response deletarXmlMovimento();

	@POST
	@Path("/xml/transmissao")
	br.gov.efinanceira.schemas.retornoloteeventos.v1_0_1.ObjectFactory transmitirXmlMovimento();

}
