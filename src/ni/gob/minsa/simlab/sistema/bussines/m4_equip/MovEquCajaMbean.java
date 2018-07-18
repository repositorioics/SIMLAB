package ni.gob.minsa.simlab.sistema.bussines.m4_equip;

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
import ni.gob.minsa.simlab.sistema.utileria.SimlabNumberUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Caja;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;

@ManagedBean(name="movEquCajaMbean")
@ViewScoped
public class MovEquCajaMbean extends GenericMbean implements Serializable {

	private static final long serialVersionUID = -8512175166014597605L;
	
	private Integer codeFreezerAct;
	private Integer codeFreezerNew;
	private Integer positionCaja;
	private Integer codeOrigin;
	private Integer codeDestination;
	private Integer mode;
	private Integer number;
	
	private String codeRackAct;
	private String codeRackNew;
	private String codeCaja;
	
		
	public Integer getCodeFreezerAct() {
		return codeFreezerAct;
	}
	public void setCodeFreezerAct(Integer codeFreezerAct) {
		this.codeFreezerAct = codeFreezerAct;
	}
	public Integer getCodeFreezerNew() {
		return codeFreezerNew;
	}
	public void setCodeFreezerNew(Integer codeFreezerNew) {
		this.codeFreezerNew = codeFreezerNew;
	}
	public Integer getPositionCaja() {
		return positionCaja;
	}
	public void setPositionCaja(Integer positionCaja) {
		this.positionCaja = positionCaja;
	}
	public String getCodeRackAct() {
		return codeRackAct;
	}
	public void setCodeRackAct(String codeRackAct) {
		this.codeRackAct = codeRackAct;
	}
	public String getCodeRackNew() {
		return codeRackNew;
	}
	public void setCodeRackNew(String codeRackNew) {
		this.codeRackNew = codeRackNew;
	}
	public String getCodeCaja() {
		return codeCaja;
	}
	public void setCodeCaja(String codeCaja) {
		this.codeCaja = codeCaja;
	}

	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public MovEquCajaMbean() {
		// TODO Auto-generated constructor stub
		super();
		this.setMode(0);
	}
	
	/***************************************Metodos que llena los dataTable***********************************************/
	public Collection<Caja>getListAllCaja(){
		Collection<Caja> listAllCaja = new ArrayList<Caja>();
		try {
			if (simlabStringUtils.isNullOrEmpty(this.getCodeRackAct())) {
				listAllCaja = SimlabEquipService.getListCajaByRack(null);
			}else {
				listAllCaja = SimlabEquipService.getListCajaByRack(this.getCodeRackAct());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAllCaja;
	}
	
	public Collection<Caja>getListAllCajaB(){
		Collection<Caja> listAllCajaB = new ArrayList<Caja>();
		try {
			if (simlabStringUtils.isNullOrEmpty(this.getCodeRackNew())) {
				listAllCajaB = SimlabEquipService.getListCajaByRack(null);
			}else {
				listAllCajaB = SimlabEquipService.getListCajaByRack(this.getCodeRackNew());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAllCajaB;
	}
	/**Esto es para la dataTable de la segunda vista*/
	public Collection<Caja>getListAllCajaByRackFreezer(){
		Collection<Caja> listAllCajaByRackFreezer = new ArrayList<Caja>();
		System.out.println(this.codeOrigin);
		try {
			if (this.codeOrigin == null || this.codeOrigin == 0 || simlabStringUtils.isNullOrEmpty(this.getCodeRackAct())) {
				listAllCajaByRackFreezer = SimlabEquipService.getListCajaByRackAndFreezer(0, null);
			}else {
				listAllCajaByRackFreezer = SimlabEquipService.getListCajaByRackAndFreezer(this.getCodeFreezerAct(),this.getCodeRackAct());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAllCajaByRackFreezer;
	}
	
	public Collection<Caja>getListAllCajaByRackFreezerB(){
		Collection<Caja> listAllCajaByRackFreezerB = new ArrayList<Caja>();
		try {
			if (this.codeDestination == null || this.codeDestination == 0 || simlabStringUtils.isNullOrEmpty(this.getCodeRackNew())) {
				listAllCajaByRackFreezerB = SimlabEquipService.getListCajaByRackAndFreezer(0, null);
			}else {
				listAllCajaByRackFreezerB = SimlabEquipService.getListCajaByRackAndFreezer(this.getCodeFreezerNew(),this.getCodeRackNew());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listAllCajaByRackFreezerB;
	}
	/***************************************Acciones***********************************************/
	/**Este me mueve cajas en el mismo freezer*/
	public void actionMoveCajaToRack(ActionEvent event){
		
		Session session = null;
		try {
			validateFields();
			session = this.openSessionAndBeginTransaction();
			if (this.getMode() == 1) {
				SimlabEquipService.moveCajaToRack(session, this.getCodeRackAct(), this.getCodeRackNew(), this.getCodeCaja(), 
						this.getPositionCaja());
			}else if (this.getMode() == 2) {
				SimlabEquipService.moveCajaToRack(session, this.getCodeRackAct(), this.getCodeRackNew(), this.getCodeCaja(), 
						this.getPositionCaja());
			}
			
			this.closeSessionAndCommitTransaction(session);
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
	}
	/***************************************Validaciones***********************************************/
	private void validateFields() throws SimlabAppException{
		/*Validando campos vacios**/
		if(simlabStringUtils.isNullOrEmpty(this.getCodeRackAct()))throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getCodeCaja()))throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getCodeRackNew()))throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getPositionCaja()) || this.getPositionCaja() == 0)throw new SimlabAppException(11000);
		/*Validando campos vacios**/
		
		Rack r = new Rack();
		/*Validar q los codigos de rack existan*/
		r = SimlabEquipService.getRack(this.getCodeRackAct());
		if(r == null)throw new SimlabAppException(11022);
		
		/*Validar que el codigo de caja exista para el rack elegido*/
		Caja c  = SimlabEquipService.getCaja(this.getCodeCaja(), this.getCodeRackAct());
		if(c == null)throw new SimlabAppException(11021);
		
		/*Validar q los codigos de rack existan*/
		r = SimlabEquipService.getRack(this.getCodeRackNew());
		if(r == null)throw new SimlabAppException(11023);
		
		/*Validar q el rack destino no este lleno*/
		if(SimlabEquipService.isMaxCapcRack(this.getCodeRackNew()))throw new SimlabAppException(11011);
		
		/*validando que la posicion digitada del rack este vacia*/
		if(!SimlabEquipService.isPositionAvailableInCaja(this.getPositionCaja(), this.getCodeRackNew()))throw new SimlabAppException(11005);
		
		if (this.getMode() == 2) {
			
			/*Validando campos vacios**/
			if (SimlabNumberUtil.isNull(this.getCodeFreezerAct()) || this.getCodeFreezerAct() == 0)throw new SimlabAppException(11000);
			if(SimlabNumberUtil.isNull(this.getCodeFreezerNew()) || this.getCodeFreezerNew() == 0)throw new SimlabAppException(11000);	
			/*Validando campos vacios**/
			
			/*Validando que la caja se ingrese en un freezer q coincida con el tipo alicuota q almacena*/
			Caja b  = SimlabEquipService.getCaja(this.getCodeCaja(), this.getCodeRackAct());
			if(!SimlabEquipService.getUsoAlicFreezer(this.getCodeFreezerNew()).contains(b.getUsoAlic()))throw new SimlabAppException(11024);
		}
		
	}
	/***************************************Otros Metodos***********************************************/
	public void setCodFreezerAct(){
		this.codeOrigin = this.getCodeFreezerAct();
	}
	public void setCodFreezerNew(){
		this.codeDestination = this.getCodeFreezerNew();
	}
	public void changeView(){
		this.setMode(this.getNumber());
	}
}
