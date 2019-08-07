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
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService;
import ni.gob.minsa.simlab.sistema.services.SimlabPatternService;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabNumberUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;

@ManagedBean(name="racksPbmcMbean")
@ViewScoped
public class RacksPbmcMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 9030495036433856461L;
	
	private String titlePanel;
	private Integer mode;
	
	private Integer tanqueId;
	
	private String codeRack;
	private Integer capacidad;
	private Integer posicion;
	


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
	

	public String getCodeRack() {
		return codeRack;
	}

	public void setCodeRack(String codeRack) {
		this.codeRack = codeRack;
	}

	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	public Integer getTanqueId() {
		return tanqueId;
	}

	public void setTanqueId(Integer tanqueId) {
		this.tanqueId = tanqueId;
	}

	public RacksPbmcMbean() {
		// TODO Auto-generated constructor stub
		super();
		this.setMode(0);
	}
	

	
	/******************************Acciones************************************/
	public void actionChangeViewToRegisterRack(ActionEvent event){
		
		fieldToNull();
		this.setMode(1);
	}
	public void actionCancel(ActionEvent event){
		fieldToNull();
		this.setMode(0);
	}
	public void actionEditInfoRack(ActionEvent event, Object rackCode){
		fieldToNull();
		loadData(rackCode.toString());
		this.setMode(2);
	}
	
	public void actionSaveRack(ActionEvent event){
		Session session   = null;
		try {
			validateDataRack();
			validateEquipRack(this.codeRack, this.tanqueId);
			session = this.openSessionAndBeginTransaction();
			//Modo Registro
			if (this.getMode() == 1) { 
				//Efectuamos el Registro de Rack 
				SimlabEquipService.addEquipRack(session, this.codeRack, this.tanqueId, this.capacidad, 
						this.posicion, SimlabDateUtil.getCurrentDate(), SimlabDateUtil.MAX_DATE);
				//Seteamos todos lo campos como Vacios para un nuevo Registro
				}
			//Modo Actualizacion
			else if (this.getMode() == 2) { 
				//Efectuamos la Actualizacion del Rack
				SimlabEquipService.updateEquipRack(session, this.codeRack, this.tanqueId, this.capacidad, 
						this.posicion);				
			}
			this.closeSessionAndCommitTransaction(session);
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
		this.codeRack=null;
		this.capacidad=null;
		this.posicion=null;
	}
	
	private void loadData(String rackCode){
		Rack rack = SimlabEquipService.getRack(rackCode);
		capacidad = rack.getCapAlm();
		posicion =  rack.getPosFreezer();
		codeRack = rack.getId().getCodRack();
		tanqueId = rack.getId().getRcodFreezer();
		
	}
	
	public void validateDataRack() throws SimlabAppException{
		
		/*validando que los campo no esten vacios*/
		if(SimlabNumberUtil.isNull(this.tanqueId) || this.tanqueId == 0) throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.codeRack)) throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.posicion) || this.posicion == 0) throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.capacidad) || this.capacidad == 0) throw new SimlabAppException(11000);
		/*validando que el rack se encuentre vacio*/
		if (this.getMode() == 3) {
			if(!SimlabEquipService.isRackEmpty(this.tanqueId,this.codeRack))throw new SimlabAppException(11020);
		}
	}
	
	//Validamos los Datos que ingreso el Usuario para el Registro
		private void validateEquipRack(String rackCode, int freezerCode) throws SimlabAppException{
			//Nos encontramos en Modo Registro
			if (this.getMode() == 1) {
				//Validamos que el Formato de Codigo de Rack sea Correcto
				if(!SimlabPatternService.isRightPattern(rackCode, SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 4)))
					throw new SimlabAppException(11003);
				//Validando que no se repitan los codigos de rack
				Rack rack = SimlabEquipService.getRack(rackCode);
//				Rack r = SimlabEquipService.verifCodRack(rackCode, freezerCode);
				if(rack != null) throw new SimlabAppException(11004);			
				//Validando que la posicion donde se coloca el rack este libre
				if (!SimlabEquipService.isPositionAvailableInRack(this.posicion, this.tanqueId)) throw new SimlabAppException(11019);
				}		
			//Validamos que la posicion digitada no se encuentre Fuera de Rango de la Capacidad de Almacenamiento del Freezer.
			if(this.posicion>SimlabEquipService.getObjectFreezer(freezerCode).getCapAlm())
				throw new SimlabAppException(10036);
		}
	
	/***************************Populate ComboBox-DataTable**************************************************/
	public Collection<Object> getListRacks(){
		Collection<Object> listRacks = new ArrayList<Object>();
		listRacks = SimlabEquipService.getListRackPBMC(this.tanqueId);
		return listRacks;
	}
	
	public Collection<Integer> getListTanques(){
		Collection<Integer> listTanques = new ArrayList<Integer>();
		listTanques = SimlabEquipService.getListCodesFreezerPBMC();
		return listTanques;
	}
	
}
