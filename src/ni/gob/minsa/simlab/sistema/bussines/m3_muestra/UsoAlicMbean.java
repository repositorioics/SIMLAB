package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.Query;
import org.hibernate.Session;

import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabEquipService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.sistema.hibernate.bussines.Caja;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;


@ManagedBean(name="usoAlicMbean")
@ViewScoped
public class UsoAlicMbean extends GenericMbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer mode = null;
	private String codigoAlic;
	private String usoAlic;
	private Float volUsado;
	private RegAlic alicToUse;
	private boolean usoVolumen;
	private String tipo = "";
	
	
	public UsoAlicMbean() {
		super();	
		this.setMode(0);
	}


	public String getCodigoAlic() {
		return codigoAlic;
	}

	public void setCodigoAlic(String codigoAlic) {
		this.codigoAlic = codigoAlic;
	}
	
	
	
	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public void actionCancel(ActionEvent event) {
		this.setCodigoAlic(null);
		this.setUsoAlic(null);
		this.setVolUsado(null);
		this.setUsoVolumen(false);
		this.setMode(0);
	}
	
	public void actionProcess(ActionEvent event) {
		Session session   = null;
		try{
			if (this.usoAlic == null || this.usoAlic.equals("")){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Requerido" ,"Debe entrar el proposito de uso de la alicuota"));
			}
			else if(this.volUsado == null || this.volUsado.equals(0F)||this.volUsado <= 0||this.volUsado>this.alicToUse.getVolAlic()){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Requerido" ,"Volumen invalido"));
			}
			else{
				session = this.openSessionAndBeginTransaction();	
				Query query;
				String codAlic = alicToUse.getId().getCodAlic();
				int posBox = alicToUse.getId().getPosBox();
				int codFreezer = alicToUse.getCodFreezer();
				String codRack = alicToUse.getCodRack();
				int codBox = alicToUse.getCodBox();
				String negPos = alicToUse.getNegPos();
				Date fHoraRegAlic = alicToUse.getFehorReg();
				Timestamp fHoraReg = SimlabDateUtil.getCurrentTimestamp();
				String codUser = alicToUse.getCodUser();
				Float volAlic = alicToUse.getVolAlic();
				Float pesoAlic = alicToUse.getPesoAlic();
				Float volUsado = this.getVolUsado();
				String uso = this.getUsoAlic();
				String usuario = this.getLoginMbean().getCodeUser();
				String tipo = alicToUse.getTipo();
				String condicion = alicToUse.getCondicion();
				String separada = alicToUse.getSeparada();
				int numDes = alicToUse.getNumDes();
				if(this.volUsado.equals(this.alicToUse.getVolAlic())){
					query = session.createSQLQuery("INSERT INTO `reg_alic_uso` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
							"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `vol_usado`, `peso_alic`, `uso`, `usuario`, `tipo`, `condicion`, `separada`, `num_desc`)" +
							" VALUES ('" + codAlic + "'," + posBox +", " + codFreezer + ", '" + codRack +"', " + codBox + ",  '" + negPos +"', " +
									"'" + fHoraRegAlic +"', '" + codUser +"', " + volAlic + ", " + volUsado + ", " + pesoAlic +",'" + uso +"', '"+ usuario +"', '"+ tipo +"', '"+ condicion +"', '"+ separada +"',"+numDes+"); ");
					query.executeUpdate();
					query = session.createSQLQuery("Delete from reg_alic " +
							"where cod_alic = '" + alicToUse.getId().getCodAlic() +"' and " +
							"pos_box = " + alicToUse.getId().getPosBox());
					query.executeUpdate();
				}
				else{
					query = session.createSQLQuery("INSERT INTO `reg_alic_uso` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
							"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `vol_usado`, `peso_alic`, `uso`, `usuario`, `tipo`, `condicion`, `separada`, `num_desc`)" +
							" VALUES ('" + codAlic + "'," + posBox +", " + codFreezer + ", '" + codRack +"', " + codBox + ",  '" + negPos +"', " +
									"'" + fHoraRegAlic +"', '" + codUser +"', " + volAlic + ", " + volUsado + ", " + pesoAlic +",'" + uso +"', '"+ usuario +"', '"+ tipo +"', '"+ condicion +"', '"+ separada +"',"+numDes+"); ");
					query.executeUpdate();
					this.alicToUse.setVolAlic(volAlic-volUsado);
					this.alicToUse.setFehorReg(fHoraReg);
					this.alicToUse.setCodUser(usuario);
					this.alicToUse.setNumDes(numDes+1);
					session.update(this.alicToUse);
				}
				this.closeSessionAndCommitTransaction(session);
				this.setCodigoAlic(null);
				this.setUsoAlic(null);
				this.setVolUsado(null);
				this.setMode(0);
				this.setUsoVolumen(false);
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Finalizado" ,"El proceso se ha finalizado con exito"));
			}
		}
		catch(Exception e){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
													"Ha ocurrido un error" ,e.getLocalizedMessage()));
			e.printStackTrace();
		}
	}

	public String getUsoAlic() {
		return usoAlic;
	}

	public void setUsoAlic(String usoAlic) {
		this.usoAlic = usoAlic;
	}
	
	public void actionRegisterAlicuota(ActionEvent event) {
		Session session   = null;
		try{
			session = this.openSessionAndBeginTransaction();
			Query nueva = session.createQuery("FROM RegAlic reg WHERE lower(reg.id.codAlic) = lower('"+this.codigoAlic+"') and tipo = '"+ this.tipo + "'");
			
			RegAlic result = (RegAlic) nueva.uniqueResult();
			if (result == null){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
														"No encontrado" ,"No hay alicuotas registradas con ese codigo"));
			}
			else{
				Caja caja = SimlabEquipService.getCajaByCode(String.valueOf(result.getCodBox()));
				if(caja!=null) {
					Rack rack = SimlabEquipService.getRack(caja.getId().getCcodRack());
					result.setCodFreezer(rack.getId().getRcodFreezer());
					result.setCodRack(rack.getId().getCodRack());
				}
				this.setAlicToUse(result);
				this.setMode(1);
			}
			this.closeSessionAndCommitTransaction(session);
		}
		catch(Exception e){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
													"Ha ocurrido un error" ,e.getLocalizedMessage()));
			e.printStackTrace();
		}
	}


	public Integer getMode() {
		return mode;
	}


	public void setMode(Integer mode) {
		this.mode = mode;
	}


	public RegAlic getAlicToUse() {
		return alicToUse;
	}


	public void setAlicToUse(RegAlic alicToUse) {
		this.alicToUse = alicToUse;
	}


	public Float getVolUsado() {
		return volUsado;
	}


	public void setVolUsado(Float volUsado) {
		this.volUsado = volUsado;
	}
	
	public void addVolumen() {
        this.setVolUsado(this.alicToUse.getVolAlic());
    }


	public boolean isUsoVolumen() {
		return usoVolumen;
	}


	public void setUsoVolumen(boolean usoVolumen) {
		this.usoVolumen = usoVolumen;
	}

}
