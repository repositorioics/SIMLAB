package ni.gob.minsa.sistema.hibernate.bussines;

// Generated 08-07-2012 11:27:23 AM by Hibernate Tools 3.4.0.CR1

/**
 * UsuaPerfId generated by hbm2java
 */
public class UsuaPerfId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5581007315101610108L;
	private String codUser;
	private String codPerf;

	public UsuaPerfId() {
	}

	public UsuaPerfId(String codUser, String codPerf) {
		this.codUser = codUser;
		this.codPerf = codPerf;
	}

	public String getCodUser() {
		return this.codUser;
	}

	public void setCodUser(String codUser) {
		this.codUser = codUser;
	}

	public String getCodPerf() {
		return this.codPerf;
	}

	public void setCodPerf(String codPerf) {
		this.codPerf = codPerf;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UsuaPerfId))
			return false;
		UsuaPerfId castOther = (UsuaPerfId) other;

		return ((this.getCodUser() == castOther.getCodUser()) || (this
				.getCodUser() != null && castOther.getCodUser() != null && this
				.getCodUser().equals(castOther.getCodUser())))
				&& ((this.getCodPerf() == castOther.getCodPerf()) || (this
						.getCodPerf() != null && castOther.getCodPerf() != null && this
						.getCodPerf().equals(castOther.getCodPerf())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCodUser() == null ? 0 : this.getCodUser().hashCode());
		result = 37 * result
				+ (getCodPerf() == null ? 0 : this.getCodPerf().hashCode());
		return result;
	}

}