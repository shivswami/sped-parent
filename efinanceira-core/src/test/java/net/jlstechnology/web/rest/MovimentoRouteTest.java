package net.jlstechnology.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import net.jlstechnology.Application;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = Application.class)
public class MovimentoRouteTest {
	
	@EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;
	
	@Produce(uri = "direct:criarXmlMovimento")
    protected ProducerTemplate template;
	
	@DirtiesContext
    @Test
    public void gerarXMLTest() throws Exception {
		
		template.sendBody(getEfinanceiras());		
		resultEndpoint.expectedHeaderReceived(Exchange.HTTP_RESPONSE_CODE, 200);
		
	}
	
	private static br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.ObjectFactory FACTORY = new br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.ObjectFactory();
	
	private static List<br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira> getEfinanceiras() throws Exception {

		List<br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira> eFinanceiras = new ArrayList<>();
		
		//br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira eFinanceiraLoteEventos = new br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.ObjectFactory().createEFinanceira();
		//br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.EFinanceira.LoteEventos vLoteEventos = new br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.ObjectFactory().createEFinanceiraLoteEventos();

		for (int i = 0; i <= 1001; i++) {
			
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira eFinanceira = FACTORY.createEFinanceira();
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin vEvtMovOpFin = FACTORY.createEFinanceiraEvtMovOpFin();
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeEvento vIdeEvento = FACTORY.createEFinanceiraEvtMovOpFinIdeEvento();
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarante vIdeDeclarante = FACTORY.createEFinanceiraEvtMovOpFinIdeDeclarante();
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.PaisEndereco vPaisEndereco = FACTORY.createEFinanceiraEvtMovOpFinIdeDeclaradoPaisEndereco();
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.PaisResid vPaisResid = FACTORY.createEFinanceiraEvtMovOpFinIdeDeclaradoPaisResid();
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.PaisNacionalidade vPaisNacionalidade = FACTORY.createEFinanceiraEvtMovOpFinIdeDeclaradoPaisNacionalidade();
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado vIdeDeclarado = FACTORY.createEFinanceiraEvtMovOpFinIdeDeclarado();

			// IdeEvento
			adicionarIdeEvento(vIdeEvento, 1, null);

			// IdeDeclarante
			vIdeDeclarante.setCnpjDeclarante("54868745789");

			// PaisEndereco
			vPaisEndereco.setPais("BR");

			// PaisResid
			vPaisResid.setPais("BR");

			// PaisNacionalidade
			vPaisNacionalidade.setPais("BR");

			// IdeDeclarado
			adicionarIdeDeclarado(vIdeDeclarado, vPaisNacionalidade, vPaisResid, vPaisEndereco);

			// adicionar NIF no IdeDeclarado
			adicionarNIF(vIdeDeclarado);

			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa vMesCaixa = FACTORY.createEFinanceiraEvtMovOpFinMesCaixa();
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin vMovOpFin = FACTORY.createEFinanceiraEvtMovOpFinMesCaixaMovOpFin();

			// MesCaixa
			vMesCaixa.setAnoMesCaixa("201703");

			// adicionar as contas do participante ao mes caixa
			adicionarConta(vMovOpFin);

			// noh principal da movimentacao financeira
			vMesCaixa.setMovOpFin(vMovOpFin);

			// Adicionando OBJETOS no evento: EvtMovOpFin
			vEvtMovOpFin.setId(UUID.randomUUID().toString());
			vEvtMovOpFin.setIdeDeclarado(vIdeDeclarado);
			vEvtMovOpFin.setIdeDeclarante(vIdeDeclarante);
			vEvtMovOpFin.setIdeEvento(vIdeEvento);
			vEvtMovOpFin.setMesCaixa(vMesCaixa);

			// fim do Evento da Movimentacao Financeira
			eFinanceira.setEvtMovOpFin(vEvtMovOpFin);
			eFinanceiras.add(eFinanceira);

			// adicionar evento ao lote de eventos (converter antes de element
			// para objeto)
			//br.gov.efinanceira.schemas.envioloteeventos.v1_0_1.TArquivoeFinanceira vTArquivoeFinanceira = EfinanceiraUtil.converterTArquivoeFinanceira(eFinanceira);
			//vLoteEventos.getEvento().add(vTArquivoeFinanceira);

			// adiciona o lote de eventos ao eFinanceira PAI (inicial)
			//eFinanceiraLoteEventos.setLoteEventos(vLoteEventos);
			
		
		}
		return eFinanceiras;		
	}
	
	private static void adicionarBalancoConta(br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta.InfoConta vInfoConta) {
		br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta.InfoConta.BalancoConta vBalancoConta = FACTORY.createEFinanceiraEvtMovOpFinMesCaixaMovOpFinContaInfoContaBalancoConta();
		vBalancoConta.setTotCreditos("100");
		vBalancoConta.setTotDebitos("200");
		vBalancoConta.setTotCreditosMesmaTitularidade("5,000");
		vBalancoConta.setTotDebitosMesmaTitularidade("5,655");
		vBalancoConta.setVlrUltDia("200");
		vInfoConta.setBalancoConta(vBalancoConta);
	}

	/**
	 * @param vMovimentacao
	 * @param vMovOpFin
	 */
	private static void adicionarConta(br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin vMovOpFin) throws Exception {

		//String vMes = "03";
		//String vAno = "2017";

		//List<SpOperacoesEFinanceiraConta> vListaSpOperacoesEFinanceiraConta = this.operacoesEFincanceiraContaRepository.consultar(vMes, vAno, vMovimentacao.getnIDeclarado());

		//for (SpOperacoesEFinanceiraConta vContaBD : vListaSpOperacoesEFinanceiraConta) {

		    br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta vConta = FACTORY.createEFinanceiraEvtMovOpFinMesCaixaMovOpFinConta();
		    br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta.InfoConta.Reportavel vReportavel = FACTORY.createEFinanceiraEvtMovOpFinMesCaixaMovOpFinContaInfoContaReportavel();
			vReportavel.setPais("BR");

			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta.InfoConta vInfoConta = adicionarInfoConta(vReportavel);
			adicionarBalancoConta(vInfoConta);
			adicionarPagamentoAcumulado(vInfoConta);

			vConta.setInfoConta(vInfoConta);

			// listas de contas do participante por plano (BD,BS,CD,ASSISTENCIAL)
			vMovOpFin.getConta().add(vConta);
		//}
	}

	/**
	 * @param pIdeDeclarado
	 * @param pPaisNacionalidade
	 * @param pPaisResid
	 * @param pMovimentacao
	 * @param pPaisEndereco
	 * @throws Exception
	 */
	private static void adicionarIdeDeclarado(br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado pIdeDeclarado, 
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.PaisNacionalidade pPaisNacionalidade, 
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.PaisResid pPaisResid, 
			br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado.PaisEndereco pPaisEndereco) throws Exception {
		pIdeDeclarado.setTpNI((short) 'S');
		pIdeDeclarado.setNomeDeclarado("Nome declarado");
		pIdeDeclarado.setEnderecoLivre("Endereco Livre");
		pIdeDeclarado.setNIDeclarado("NID");
		pIdeDeclarado.setPaisEndereco(pPaisEndereco);
		pIdeDeclarado.getPaisResid().add(pPaisResid);
		pIdeDeclarado.getPaisNacionalidade().add(pPaisNacionalidade);
		pIdeDeclarado.setDataNasc(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(1980, 1, 11, DatatypeConstants.FIELD_UNDEFINED));

		// relacionado a MSG1035 quando reportavel (US PERSON) deve ser informado (procedure ja verifica se for US PERSON)
		//if (StringUtils.isNotNullAndBlank(pMovimentacao.getTpDeclarado())) {
			pIdeDeclarado.getTpDeclarado().add("tpdeclarado");
		//}
	}

	/**
	 * @param vIdeEvento
	 * @param vMovimentacao
	 * @param pTipoRetificacao
	 * @param pReciboTela
	 */
	private static void adicionarIdeEvento(br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeEvento vIdeEvento, long pTipoRetificacao, String pReciboTela) {

		vIdeEvento.setAplicEmi(1);
		vIdeEvento.setTpAmb(2);
		vIdeEvento.setVerAplic("100");

		//if (Constants.RETIFICACAO_ARQUIVO_ORIGINAL == pTipoRetificacao) {
			vIdeEvento.setIndRetificacao(1);

//		} else if (Constants.RETIFICACAO_ESPONTANEA == pTipoRetificacao) {
//
//			vIdeEvento.setIndRetificacao(Constants.RETIFICACAO_ESPONTANEA);
//
//			// o recibo pode esta sendo fornecido na interface ou ja retornado pela procedure com relacao ao ultimo envio do registro extraido do log
//			// de envio(transmissoes)
//			if (StringUtils.isNotNullAndBlank(pReciboTela)) {
//				vIdeEvento.setNrRecibo(pReciboTela);
//
//			} else if (StringUtils.isNotNullAndBlank(vMovimentacao.getNumRecibo())) {
//				vIdeEvento.setNrRecibo(vMovimentacao.getNumRecibo());
//			}
//
//		} else if (Constants.RETIFICACAO_APEDIDO == pTipoRetificacao) {
//			vIdeEvento.setIndRetificacao(Constants.RETIFICACAO_APEDIDO);
//			vIdeEvento.setNrRecibo(pReciboTela);
//		}
	}

	// /**
	// * @param pDate
	// * @return
	// * @throws Exception
	// */
	// private XMLGregorianCalendar getGregorianCalendar(Date pDate) throws Exception {
	// GregorianCalendar gc = new GregorianCalendar();
	// gc.setTime(pDate);
	// return DatatypeFactory.newInstance().newXMLGregorianCalendarDate(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH)+1, gc.get(Calendar.DATE),
	// DatatypeConstants.FIELD_UNDEFINED);
	// }

	/**
	 * @param vContaBD
	 * @param vReportavel
	 * @return
	 * @throws Exception
	 */
	private static br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta.InfoConta adicionarInfoConta(br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta.InfoConta.Reportavel vReportavel) throws Exception {
		br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta.InfoConta vInfoConta = FACTORY.createEFinanceiraEvtMovOpFinMesCaixaMovOpFinContaInfoConta();
		vInfoConta.setTpConta("Corrente");
		vInfoConta.setSubTpConta("100");
		vInfoConta.setTpNumConta("331");
		vInfoConta.setNumConta("154878787");
		vInfoConta.setTpRelacaoDeclarado(2);
		vInfoConta.setNoTitulares("3");

		//if (StringUtils.isNotNullAndBlank(vContaBD.getDtEncerramentoConta())) {
			//Date data = DataHoraUtils.getDataHoraFormatada(vContaBD.getDtEncerramentoConta(), DataHoraUtils.FORMATO_SQL_YYYY_MM_DD);
			// Date date = formatter
			vInfoConta.setDtEncerramentoConta(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(2017, 1, 1, DatatypeConstants.FIELD_UNDEFINED));
		//}

		vInfoConta.getReportavel().add(vReportavel);
		return vInfoConta;
	}

	/**
	 * @param pIdeDeclarado
	 * @param pMovimentacao
	 */
	public static void adicionarNIF(br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.IdeDeclarado pIdeDeclarado) {

		// sera preenchido quando for FATCA (NIF = codigo da receita estrangeira EUA, EUROPA, etc...)
//		if ("S".equals(StringUtils.toString(pMovimentacao.getUsPerson()))) {
//
//			String vMes = this.getMesCaixa(pMovimentacao);
//			String vAno = this.getAnoCaixa(pMovimentacao);
//
//			// obter os NIFs de cada pais do declarado
//			List<SpOperacoesEFinanceiraNIF> vListaNIF = this.operacoesEFincanceiraNIFRepository.consultarNIF(vMes, vAno, pIdeDeclarado.getNIDeclarado());
//
//			for (SpOperacoesEFinanceiraNIF vNIFDeclarado : vListaNIF) {
//
//				NIF vNIF = MovimentoService.FACTORY.createEFinanceiraEvtMovOpFinIdeDeclaradoNIF();
//
//				vNIF.setNumeroNIF(vNIFDeclarado.getNif());
//				vNIF.setPaisEmissaoNIF(vNIFDeclarado.getPais());
//
//				pIdeDeclarado.getNIF().add(vNIF);
//			}
//		}
	}

	/**
	 * @param vContaBD
	 * @param vInfoConta
	 */
	private static void adicionarPagamentoAcumulado(br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta.InfoConta vInfoConta) {
		br.gov.efinanceira.schemas.evtmovopfin.v1_0_1.EFinanceira.EvtMovOpFin.MesCaixa.MovOpFin.Conta.InfoConta.PgtosAcum vPgtosAcum = FACTORY.createEFinanceiraEvtMovOpFinMesCaixaMovOpFinContaInfoContaPgtosAcum();
		vPgtosAcum.getTpPgto().add("1.000".replace(".", ","));
		vPgtosAcum.setTotPgtosAcum("1.000".replace(".", ","));
		vInfoConta.getPgtosAcum().add(vPgtosAcum);
	}

	/**
	 * @param pMovimentacao
	 * @return
	 */
//	private static String getAnoCaixa(String anoCaixa) {
//		return anoCaixa.substring(0, 4);
//	}

	/**
	 * @param pMovimentacao
	 * @return
	 */
//	private static String getMesCaixa(String mesCaixa) {
//		return mesCaixa.substring(4, 6);
//	}

}
