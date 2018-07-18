package ni.gob.minsa.simlab.sistema.model;

import java.io.Serializable;
import java.util.Date;


public class AlicuotaModel implements Serializable {

	private static final long serialVersionUID = -7618462827127525509L;
	
	private String codeMuestra;
	private String codeAlic;
	private String codeRack;
	private String codeCaja;
	private String uso;
	
	private Integer codeFreezer;
	private Integer position;
	
	private Date fechaRegistro;

	public String getCodeMuestra() {
		return codeMuestra;
	}

	public void setCodeMuestra(String codeMuestra) {
		this.codeMuestra = codeMuestra;
	}

	public String getCodeAlic() {
		return codeAlic;
	}

	public void setCodeAlic(String codeAlic) {
		this.codeAlic = codeAlic;
	}

	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}

	public Integer getCodeFreezer() {
		return codeFreezer;
	}

	public void setCodeFreezer(Integer codeFreezer) {
		this.codeFreezer = codeFreezer;
	}

	public String getCodeRack() {
		return codeRack;
	}

	public void setCodeRack(String codeRack) {
		this.codeRack = codeRack;
	}

	public String getCodeCaja() {
		return codeCaja;
	}

	public void setCodeCaja(String codeCaja) {
		this.codeCaja = codeCaja;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public AlicuotaModel() {

	}

	public AlicuotaModel(String codeMuestra, String codeAlic, String uso,
			Integer codeFreezer, String codeRack, String codeCaja,
			Integer position, Date fechaRegistro) {
		super();
		this.codeMuestra = codeMuestra;
		this.codeAlic = codeAlic;
		this.uso = uso;
		this.codeFreezer = codeFreezer;
		this.codeRack = codeRack;
		this.codeCaja = codeCaja;
		this.position = position;
		this.fechaRegistro = fechaRegistro;
	}
	
	public AlicuotaModel(String codeAlic, Integer codeFreezer, String codeRack, 
			String codeCaja, Integer position, Date fechaRegistro) {
		super();
		this.codeAlic = codeAlic;
		this.codeFreezer = codeFreezer;
		this.codeRack = codeRack;
		this.codeCaja = codeCaja;
		this.position = position;
		this.fechaRegistro = fechaRegistro;
	}
}
