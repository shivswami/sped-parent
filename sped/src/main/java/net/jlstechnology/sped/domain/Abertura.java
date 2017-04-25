package net.jlstechnology.sped.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Abertura implements Serializable {

	static final long serialVersionUID = 1L;
	
	private EvtAberturaeFinanceira evtAberturaeFinanceira;
	
	@Entity
	@Table
	class EvtAberturaeFinanceira implements Serializable {

		private static final long serialVersionUID = 1L;

		private IdeEvento ideEvento;
		
        private IdeDeclarante ideDeclarante;

        private InfoAbertura infoAbertura;

        private AberturaPP aberturaPP;

        private AberturaMovOpFin aberturaMovOpFin;

        private String id;
		
	}
	
	@Entity
	@Table
	class IdeEvento implements Serializable {

		private static final long serialVersionUID = 1L;

		private long indRetificacao;
		
		private String nrRecibo;
		
		private long tpAmb;
		
		private long aplicEmi;
        
		private String verAplic;
	}
	
	@Entity
	@Table
	class IdeDeclarante implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String cnpjDeclarante;
		
	}
	
	@Entity
	@Table
	class InfoAbertura implements Serializable {

		private static final long serialVersionUID = 1L;

		private LocalDate dtInicio;

		private LocalDate dtFim;
		
	}
	
	@Entity
	@Table
	class AberturaPP implements Serializable {

		private static final long serialVersionUID = 1L;

		private List<TpEmpresa> tpEmpresa;
		
	}
	
	@Entity
	@Table
	class AberturaMovOpFin implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private ResponsavelRMF responsavelRMF;

		private RepresLegal represLegal;
		
	}
	
	@Entity
	@Table
	class TpEmpresa implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String tpPrevPriv;
		
	}
	
	@Entity
	@Table
	class ResponsavelRMF implements Serializable {

		private static final long serialVersionUID = 1L;

		private String cpf;
		
		private String nome;

		private String setor;

		private Telefone telefone;

		private Endereco endereco;
		
	}
	
	@Entity
	@Table
	class RepresLegal implements Serializable {

		private static final long serialVersionUID = 1L;

		private String cpf;

		private String setor;

		private Telefone telefone;
		
	}
	
	@Entity
	@Table
	class Telefone implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private Long id;

		private String ddd;

		private String numero;

		private String ramal;
		
	}
	
	@Entity
	@Table
	class Endereco implements Serializable {

		private static final long serialVersionUID = 1L;

		private String logradouro;

		private String numero;

		private String complemento;

		private String bairro;

		private String cep;

		private String municipio;

		private String uf;
		
	}

}
