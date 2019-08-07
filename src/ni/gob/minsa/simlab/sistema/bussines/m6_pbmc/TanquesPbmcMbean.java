/**
 * 
 */
package ni.gob.minsa.simlab.sistema.bussines.m6_pbmc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabEquipService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabNumberUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Freezer;

@ManagedBean(name="tanquesPbmcMbean")
@ViewScoped
public class TanquesPbmcMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 9030495036433856461L;
	
	private String titlePanel;
	private Integer mode;
	
	private Integer codeTanque;
	private Integer capacidad;
	private String usoAlm = "PBMC";
	private String responsable;
	private Integer filas;
	private Integer columnas;
	


	public String getTitlePanel() {
		return titlePanel;
	}

	public void setTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
	public Integer getCodeTanque() {
		return codeTanque;
	}

	public void setCodeTanque(Integer codeTanque) {
		this.codeTanque = codeTanque;
	}

	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public Integer getFilas() {
		return filas;
	}

	public void setFilas(Integer filas) {
		this.filas = filas;
	}

	public Integer getColumnas() {
		return columnas;
	}

	public void setColumnas(Integer columnas) {
		this.columnas = columnas;
	}
	
	public String getUsoAlm() {
		return usoAlm;
	}

	public void setUsoAlm(String usoAlm) {
		this.usoAlm = usoAlm;
	}

	
	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public TanquesPbmcMbean() {
		// TODO Auto-generated constructor stub
		super();
		this.setMode(0);
	}
	

	
	/******************************Acciones************************************/
	public void actionChangeViewToRegisterTanque(ActionEvent event){
		
		fieldToNull();
		this.setMode(1);
	}
	public void actionCancel(ActionEvent event){
		fieldToNull();
		this.setMode(0);
	}
	public void actionEditInfoTanque(ActionEvent event, Object tanqueCode){
		fieldToNull();
		loadData(tanqueCode.toString());
		this.setMode(2);
	}
	
	public void actionSaveTanque(ActionEvent event){
		Session session   = null;
		try {
			validateDataTanque();
			session = HibernateUtil.openSesion();
			Freezer tanque = new Freezer();
			tanque.setCodFreezer(this.codeTanque);
			tanque.setCapAlm(this.capacidad);
			tanque.setEquipModelo("ABCDEF");
			tanque.setEquipSerie("999999");
			tanque.setFefinvig(SimlabDateUtil.MAX_DATE);
			tanque.setFeinivig(SimlabDateUtil.getCurrentDate());
			tanque.setNameResp(this.responsable);
			tanque.setNroColumn(this.capacidad);
			tanque.setNroFila(1);
			tanque.setTempMax(-200);
			tanque.setTempMin(-200);
			tanque.setTipAlm("TAN");
			tanque.setUsoAlm("PBMC");
			SimlabEquipService.saveTanque(session, tanque);
			session.close(); session = null;
			this.setMode(0);
			this.fieldToNull();
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}			
			
	}
	
	private void fieldToNull(){
		this.codeTanque=null;
		this.capacidad=null;
		this.responsable=null;
	}
	
	private void loadData(String tanqueCode){
		Freezer tanque = SimlabEquipService.getObjectFreezer(Integer.valueOf(tanqueCode));
		codeTanque = tanque.getCodFreezer();
		capacidad = tanque.getCapAlm();
		responsable = tanque.getNameResp();
	}
	
	public void validateDataTanque() throws SimlabAppException{
		if(SimlabNumberUtil.isNull(this.codeTanque)) throw new SimlabAppException(11999);
		if(SimlabNumberUtil.isNull(this.capacidad)) throw new SimlabAppException(11999);
		if(simlabStringUtils.isNullOrEmpty(this.responsable))throw new SimlabAppException(11999);
	}
	
	/***************************Populate ComboBox-DataTable**************************************************/
	public Collection<Object> getListTanques(){
		Collection<Object> listTanques = new ArrayList<Object>();
		listTanques = SimlabEquipService.getListFreezerPBMC();
		return listTanques;
	}
	
}
