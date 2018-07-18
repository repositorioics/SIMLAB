package ni.gob.minsa.sistema.hibernate.bussines;

// Generated 08-07-2012 11:27:23 AM by Hibernate Tools 3.4.0.CR1

/**
 * Alicuotas generated by hbm2java
 */
public class Alicuotas implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5737942946692369284L;
	private AlicuotasId id;
	private String volAlic;
	private String codUso;
	private String obsAlic;

	public Alicuotas() {
	}

	public Alicuotas(AlicuotasId id, String volAlic, String codUso,
			String obsAlic) {
		this.id = id;
		this.volAlic = volAlic;
		this.codUso = codUso;
		this.obsAlic = obsAlic;
	}

	public AlicuotasId getId() {
		return this.id;
	}

	public void setId(AlicuotasId id) {
		this.id = id;
	}

	public String getVolAlic() {
		return this.volAlic;
	}

	public void setVolAlic(String volAlic) {
		this.volAlic = volAlic;
	}

	public String getCodUso() {
		return this.codUso;
	}

	public void setCodUso(String codUso) {
		this.codUso = codUso;
	}

	public String getObsAlic() {
		return this.obsAlic;
	}

	public void setObsAlic(String obsAlic) {
		this.obsAlic = obsAlic;
	}

}