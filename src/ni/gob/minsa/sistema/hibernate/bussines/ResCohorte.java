package ni.gob.minsa.sistema.hibernate.bussines;

// Generated Dec 19, 2012 12:05:15 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * ResCohorte generated by hbm2java
 */
public class ResCohorte implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7617194560549791172L;
	private int codigo;
	private Date fis;
	private String codepi;
	private String resultadof;

	public ResCohorte() {
	}
	
	

	public ResCohorte(int codigo, Date fis, String codepi, String resultadof) {
		super();
		this.codigo = codigo;
		this.fis = fis;
		this.codepi = codepi;
		this.resultadof = resultadof;
	}



	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Date getFis() {
		return fis;
	}

	public void setFis(Date fis) {
		this.fis = fis;
	}

	public String getCodepi() {
		return codepi;
	}

	public void setCodepi(String codepi) {
		this.codepi = codepi;
	}

	public String getResultadof() {
		return resultadof;
	}

	public void setResultadof(String resultadof) {
		this.resultadof = resultadof;
	}

	

}