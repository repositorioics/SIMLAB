/**
 * 
 */
package ni.gob.minsa.simlab.sistema.bussines.m2_catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabEquipService;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.services.SimlabPatternService;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabNumberUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Freezer;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;


@ManagedBean(name="catEquRackMbean")
public class CatEquRackMbean extends GenericMbean implements Serializable {

private static final long serialVersionUID = -9175672205870950217L;
	
	private Integer mode = null;
	private String codeRack = null;
	private Integer codeFreezer = null;
	private Integer positionFreezer = null;
	private Integer cantAlicuotas = null;
	
	private Date fechaIni;
	private Date fechaFin;
	
	private String titlePanel;
	private String motCloseVigency;
	
	private Rack rackToEdit;
	
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

	public Integer getCodeFreezer() {
		return codeFreezer;
	}

	public void setCodeFreezer(Integer codeFreezer) {
		this.codeFreezer = codeFreezer;
	}

	public Integer getPositionFreezer() {
		return positionFreezer;
	}

	public void setPositionFreezer(Integer positionFreezer) {
		this.positionFreezer = positionFreezer;
	}

	public Integer getCantAlicuotas() {
		return cantAlicuotas;
	}

	public void setCantAlicuotas(Integer cantAlicuotas) {
		this.cantAlicuotas = cantAlicuotas;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getTitlePanel() {
		return titlePanel;
	}

	public void setTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}

	public String getMotCloseVigency() {
		return motCloseVigency;
	}

	public void setMotCloseVigency(String motCloseVigency) {
		this.motCloseVigency = motCloseVigency;
	}

	public Rack getRackToEdit() {
		return rackToEdit;
	}

	public void setRackToEdit(Rack rackToEdit) {
		this.rackToEdit = rackToEdit;
	}

	public CatEquRackMbean() {
		super();
		//Modo Vista Inicial.
		this.setMode(0);
	}
	
	/**
	 * Metodo que me llena el dataTable
	 * @return lista de datos del equipo rack
	 */
	public Collection<Rack> getListRackbyFreezer(){
		Collection<Rack> lisRacks = new ArrayList<Rack>();
		try {
			if (this.getCodeFreezer() == null || this.getCodeFreezer() == 0) {
				lisRacks = SimlabEquipService.getListRackByFreezer(0);
			}else {
				lisRacks = SimlabEquipService.getListRackByFreezer(this.getCodeFreezer());
			}	
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return lisRacks;
	}
	
	/**
	 * Metodo que me maneja las acciones sobre el equipo
	 * @param event
	 */
	public void actionRegisterRack(ActionEvent event){
		Session session = null;
		try {
			if(this.getMode()==1 || this.getMode()==2 || this.getMode()==3){
				validateFields();		
				validateCodeFreezer(this.getCodeFreezer());
				validateEquipRack(this.getCodeRack(), this.getCodeFreezer());
			}
			session = this.openSessionAndBeginTransaction();
			//Modo Registro
			if (this.getMode() == 1) { 
				//Efectuamos el Registro de Rack 
				SimlabEquipService.addEquipRack(session, this.getCodeRack(), this.getCodeFreezer(), this.getCantAlicuotas(), 
						this.getPositionFreezer(), SimlabDateUtil.getCurrentDate(), SimlabDateUtil.MAX_DATE);
				//Seteamos todos lo campos como Vacios para un nuevo Registro
				fieldToNull();
				}
			//Modo Actualizacion
			else if (this.getMode() == 2) { 
				//Efectuamos la Actualizacion del Rack
				SimlabEquipService.updateEquipRack(session, this.getCodeRack(), this.getCodeFreezer(), 
						this.getCantAlicuotas(), this.getPositionFreezer());
				fieldToNull();
				this.setMode(0);
			}
			//Modo Cierre de Vigencia
			else if (this.getMode() == 3) { /*Modo Cierre Vigencia*/
				//Efectuamos el Cierre de Vigencia del Rack
				SimlabEquipService.closeVigencyRack(session, this.getCodeRack());
				fieldToNull();
				this.setMode(0);
			}
			//Cerramos Session y hacemos Commit
			this.closeSessionAndCommitTransaction(session);
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		} finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
	}	
	
	/**
	 * Metodo para cambiar a la vista registrar nuevo equipo de rack
	 * @param event
	 */
	public void actionChangeViewToRegisterNewRack(ActionEvent event){
		try {
			validateCodeFreezer(this.getCodeFreezer());
			this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("register_new_apparatus"));
			this.setMode(1);
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}		
	}
	
	/**
	 * Metodo para cambiar a la vista de edicion
	 * @param event
	 * @param rack
	 */
	public void actionEditInfoRack(ActionEvent event, Rack rack){
		
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("edit_apparatus"));
		this.setRackToEdit(rack);
		loadData(rack.getId().getRcodFreezer(),rack.getId().getCodRack());
		this.setMode(2);
	}
	
	/**
	 * Metodo para cambiar a la vista de cierre de vigencia
	 * @param event
	 * @param rack
	 */
	public void actionCloseVigencyRack(ActionEvent event, Rack rack){
		//Seteamos el Titulo de la Transaccion
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("close_vigency_apparatus"));		
		//Seteamos el Codigo de Freezer.
		this.setCodeFreezer(rack.getId().getRcodFreezer());
		//Seteamos el Codigo de Rack
		this.setCodeRack(rack.getId().getCodRack());
		//Seteamos la Posicion dentro del Freezer
		this.setPositionFreezer(rack.getPosFreezer());
		//Seteamos la Cantida de alicuotas que alMacena el Rack.
		this.setCantAlicuotas(rack.getCapAlm());
		this.setMode(3);
	}
	
	/**
	 * Metodo que me regresa a la vista inicial
	 * @param event
	 */
	public void actionCancel(ActionEvent event){
		allFieldToNull();
		this.setMode(0);
	}
	
	/**
	 * Metodo que me llena los campos de texto 
	 * @param codFreezer
	 */
	private void loadData(int codFreezer, String codeRack){
		Rack r = SimlabEquipService.getRack(codFreezer, codeRack);
		this.setCodeFreezer(r.getId().getRcodFreezer());
		this.setCodeRack(r.getId().getCodRack());
		this.setPositionFreezer(r.getPosFreezer());
		this.setCantAlicuotas(r.getCapAlm());
	}
	
	//Validamos el Codigo de Freezer digitado por el Usuario
	private void validateCodeFreezer(int code) throws SimlabAppException{
		//Obtenemos el Objeto de Tipo Freezer
		Freezer f = SimlabEquipService.getObjectFreezer(code);
		/*Validando que el codigo de freezer exista*/
		if (f == null) throw new SimlabAppException(11001);
		/*Validando que el freezer no se encuentre lleno*/
		if(SimlabEquipService.isMaxCapacFreezer(code))
			throw new SimlabAppException(11002);
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
//			Rack r = SimlabEquipService.verifCodRack(rackCode, freezerCode);
			if(rack != null) throw new SimlabAppException(11004);			
			//Validando que la posicion donde se coloca el rack este libre
			if (!SimlabEquipService.isPositionAvailableInRack(this.getPositionFreezer(), this.getCodeFreezer())) throw new SimlabAppException(11019);
			}		
		//Validamos que la posicion digitada no se encuentre Fuera de Rango de la Capacidad de Almacenamiento del Freezer.
		if(this.getPositionFreezer()>SimlabEquipService.getObjectFreezer(freezerCode).getCapAlm())
			throw new SimlabAppException(10036);
	}
	
	private void validateFields() throws SimlabAppException{
		/*validando que los campo no esten vacios*/
		if(SimlabNumberUtil.isNull(this.getCodeFreezer()) || this.getCodeFreezer() == 0) throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getCodeRack())) throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getPositionFreezer()) || this.getPositionFreezer() == 0) throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getCantAlicuotas()) || this.getCantAlicuotas() == 0) throw new SimlabAppException(11000);
		/*validando que el rack se encuentre vacio*/
		if (this.getMode() == 3) {
			if(!SimlabEquipService.isRackEmpty(this.getCodeFreezer(),this.getCodeRack()))throw new SimlabAppException(11020);
		}
	}
	
	private void fieldToNull(){
		this.setCodeRack(null);
		//this.setCodeFreezer(null);
		this.setPositionFreezer(null);
		this.setCantAlicuotas(null);
	}
	private void allFieldToNull(){
		this.setCodeRack(null);
		this.setCodeFreezer(null);
		this.setPositionFreezer(null);
		this.setCantAlicuotas(null);
	}
}
