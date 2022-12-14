package ni.gob.minsa.sistema.hibernate.bussines;

/**
 * AlicSalida generated by hbm2java
 */
public class AlicSalida implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2513880243643976123L;
	
	private int idalicSalida;
	private String codAlic;
	private String propUso;
	private String volSalida;



	public AlicSalida() {
	}

	public AlicSalida(int idalicSalida) {
		this.idalicSalida = idalicSalida;
	}

	public AlicSalida(int idalicSalida, String codAlic, String propUso,
			String volSalida) {
		this.idalicSalida = idalicSalida;
		this.codAlic = codAlic;
		this.propUso = propUso;
		this.volSalida = volSalida;
	}

	public int getIdalicSalida() {
		return this.idalicSalida;
	}

	public void setIdalicSalida(int idalicSalida) {
		this.idalicSalida = idalicSalida;
	}

	public String getCodAlic() {
		return this.codAlic;
	}

	public void setCodAlic(String codAlic) {
		this.codAlic = codAlic;
	}

	public String getPropUso() {
		return this.propUso;
	}

	public void setPropUso(String propUso) {
		this.propUso = propUso;
	}

	public String getVolSalida() {
		return this.volSalida;
	}

	public void setVolSalida(String volSalida) {
		this.volSalida = volSalida;
	}

}
