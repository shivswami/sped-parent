package net.jlstechnology.efinanceira.service.util;

public class StatusIntegracaoBean {

	private int registrosIntegrados;
	private int registrosTotal;
	private int registrosComErro;
	private int xmlsGerados;
	private boolean integrando;

	public StatusIntegracaoBean() {
	}

	public StatusIntegracaoBean(int registrosIntegrados, int registrosTotal, int registrosComErro, int xmlsGerados,
			boolean integrando) {
		super();
		this.registrosIntegrados = registrosIntegrados;
		this.registrosTotal = registrosTotal;
		this.registrosComErro = registrosComErro;
		this.xmlsGerados = xmlsGerados;
		this.integrando = integrando;
	}

	public int getRegistrosIntegrados() {
		return registrosIntegrados;
	}

	public void setRegistrosIntegrados(int registrosIntegrados) {
		this.registrosIntegrados = registrosIntegrados;
	}

	public int getRegistrosTotal() {
		return registrosTotal;
	}

	public void setRegistrosTotal(int registrosTotal) {
		this.registrosTotal = registrosTotal;
	}

	public int getRegistrosComErro() {
		return registrosComErro;
	}

	public void setRegistrosComErro(int registrosComErro) {
		this.registrosComErro = registrosComErro;
	}

	public int getXmlsGerados() {
		return xmlsGerados;
	}

	public void setXmlsGerados(int xmlsGerados) {
		this.xmlsGerados = xmlsGerados;
	}

	public boolean isIntegrando() {
		return integrando;
	}

	public void setIntegrando(boolean integrando) {
		this.integrando = integrando;
	}

}
