package net.jlstechnology.efinanceira.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.xml.datatype.XMLGregorianCalendar;

public interface IConsultaEFinanceira {
	
	@POST
    @Path("/informacoes/cadastrais/xml")
	br.gov.fazenda.sped.ConsultarInformacoesCadastraisResponse.ConsultarInformacoesCadastraisResult consultarInformacoesCadastrais(String cpf);
	
	@POST
    @Path("/informacoes/intermediario/xml")
	br.gov.fazenda.sped.ConsultarInformacoesIntermediarioResponse.ConsultarInformacoesIntermediarioResult consultarInformacoesIntermediario(String cnpj, String ginn, String tipoNI, String numeroIdentificacao);
	
	@POST
    @Path("/informacoes/movimento/xml")
	br.gov.fazenda.sped.ConsultarInformacoesMovimentoResponse.ConsultarInformacoesMovimentoResult consultarInformacoesMovimento(String cnpj, String situacaoInformacao, String anoMesInicioVigencia, String anoMesTerminoVigencia, String tipoMovimento, String tipoIdentificacao, String identificacao);
	
	@POST
    @Path("/informacoes/patrocinado/xml")
	br.gov.fazenda.sped.ConsultarInformacoesPatrocinadoResponse.ConsultarInformacoesPatrocinadoResult consultarInformacoesPatrocinado(String cnpj,String ginn, String numeroIdentificacao);
	
	@POST
    @Path("/lista/efinanceira/xml")
	br.gov.fazenda.sped.ConsultarListaEFinanceiraResponse.ConsultarListaEFinanceiraResult consultarListaEFinanceira(String cnpj, int situacaoEFinanceira, XMLGregorianCalendar dataInicio, XMLGregorianCalendar dataFim);
}
