package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.hibernate.Query;
import org.hibernate.Session;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabEquipService;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.sistema.hibernate.bussines.Caja;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;


@ManagedBean(name="busqMuestraMbean")
@ViewScoped
public class BusqMuestraMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 8523255793629321552L;
	
	private Integer mode;
	
	private String codeAlic;
	private String titlePanel;
	private String motCloseVigency;
	private RegAlic alicToEdit;
	private String codeAlicToFind = null;
	

	public String getMotCloseVigency() {
		return motCloseVigency;
	}

	public void setMotCloseVigency(String motCloseVigency) {
		this.motCloseVigency = motCloseVigency;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getCodeAlic() {
		return codeAlic;
	}

	public void setCodeAlic(String codeAlic) {
		this.codeAlic = codeAlic;
	}

	public String getTitlePanel() {
		return titlePanel;
	}

	public void setTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}
	
	

	public String getCodeAlicToFind() {
		return codeAlicToFind;
	}

	public void setCodeAlicToFind(String codeAlicToFind) {
		this.codeAlicToFind = codeAlicToFind;
	}

	public BusqMuestraMbean() {
		// TODO Auto-generated constructor stub
		super();
		this.setMode(0);
	}
	
	public Collection<RegAlic> getListAlicByCode(){
		
		Collection<RegAlic> listAlicByCode = new ArrayList<RegAlic>();
		try {
			if (this.getCodeAlicToFind() == null) {
				listAlicByCode = null;
			}else if (this.getCodeAlicToFind().equals("")){
				listAlicByCode = null;
			}
			else {
				String criterio = this.getCodeAlicToFind();
				listAlicByCode = SimlabEquipService.getListAlicCrit(criterio);
				for(RegAlic ra: listAlicByCode) {
					ra.setCodFreezer(0);
					ra.setCodRack(null);
					
					Caja caja = SimlabEquipService.getCajaByCode(String.valueOf(ra.getCodBox()));
					if(caja!=null) {
						Rack rack = SimlabEquipService.getRack(caja.getId().getCcodRack());
						ra.setCodFreezer(rack.getId().getRcodFreezer());
						ra.setCodRack(rack.getId().getCodRack());
					}
					
				}
			}
			
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listAlicByCode;
	}
	
	
	
	public void actionEditInfoAlic(ActionEvent event, RegAlic alic){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("edit_alic"));
		this.setAlicToEdit(alic);
		this.setMode(1);
	}
	
	public void actionCloseVigencyMuestra(ActionEvent event, RegAlic alic){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("edit_alic"));
		//Habilitamos la Vista para Cerrar la Vigencia del Aparato
		this.setAlicToEdit(alic);
		this.setMode(2);	
	}

	public RegAlic getAlicToEdit() {
		return alicToEdit;
	}

	public void setAlicToEdit(RegAlic alicToEdit) {
		this.alicToEdit = alicToEdit;
	}
	
	public void actionCancel(ActionEvent event){
		
		this.setMode(0);
	}
	
	//Registramos el EncPrincipal
	public void actionRegisterAlicuota(ActionEvent event) throws SimlabAppException{
		Session session   = null;
		try {
			//Hacemos las Respectivas Validaciones para el Registro
			
			session = this.openSessionAndBeginTransaction();
			
		    alicToEdit.setCodUser(this.getLoginMbean().getCodeUser());
		    alicToEdit.setFehorReg(SimlabDateUtil.getCurrentDate());
			session.update(alicToEdit);	
			this.closeSessionAndCommitTransaction(session);
			this.setMode(0);
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,this.getTransactionTitle(), "Realizado"));
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
	}
	
	
	//Registramos el EncPrincipal
	public void actionDeleteAlicuota(ActionEvent event) throws SimlabAppException{
		Session session   = null;
		session = this.openSessionAndBeginTransaction();
		String codAlic = alicToEdit.getId().getCodAlic();
		int posBox = alicToEdit.getId().getPosBox();
		int codFreezer = alicToEdit.getCodFreezer();
		String codRack = alicToEdit.getCodRack();
		int codBox = alicToEdit.getCodBox();
		String negPos = alicToEdit.getNegPos();
		Date fHoraReg = alicToEdit.getFehorReg();
		String codUser = alicToEdit.getCodUser();
		Float volAlic = alicToEdit.getVolAlic();
		Float pesoAlic = alicToEdit.getPesoAlic();
		String motivo = this.getMotCloseVigency();
		String usuario = this.getLoginMbean().getCodeUser();
		String tipo = alicToEdit.getTipo();
		String condicion = alicToEdit.getCondicion();
		String separada = alicToEdit.getSeparada();
		int numDes = alicToEdit.getNumDes();
		Query query = session.createSQLQuery("INSERT INTO `reg_alic_borradas` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
				"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `peso_alic`, `motivo`, `usuario`, `tipo`, `condicion`, `separada`, `num_desc`)" +
				" VALUES ('" + codAlic + "'," + posBox +", " + codFreezer + ", '" + codRack +"', " + codBox + ",  '" + negPos +"', " +
						"'" + fHoraReg +"', '" + codUser +"', " + volAlic + ", " + pesoAlic +",'" + motivo +"', '"+ usuario +"', '"+ tipo +"', '"+ condicion +"', '"+ separada +"'," + numDes +"); ");
		query.executeUpdate();
		query = session.createSQLQuery("Delete from reg_alic " +
				"where cod_alic = '" + alicToEdit.getId().getCodAlic() +"' and " +
				"pos_box = " + alicToEdit.getId().getPosBox());
		query.executeUpdate();
		this.closeSessionAndCommitTransaction(session);
		this.setMode(0);
		//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,this.getTransactionTitle(), "Realizado"));
	}
	
}
