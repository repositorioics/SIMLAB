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
import ni.gob.minsa.sistema.hibernate.bussines.Freezer;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;

@ManagedBean (name="movEquRackMbean")
@ViewScoped
public class MovEquRackMbean extends GenericMbean implements Serializable {

	
	private static final long serialVersionUID = 7490473015689251408L;
	
	private Integer codeFreezerAct;
	private Integer codeFreezerNew;
	private String codeRack;
	private Integer positionRack;
		
	public Integer getPositionRack() {
		return positionRack;
	}
	public void setPositionRack(Integer positionRack) {
		this.positionRack = positionRack;
	}	
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
	public String getCodeRack() {
		return codeRack;
	}
	public void setCodeRack(String codeRack) {
		this.codeRack = codeRack;
	}

	public MovEquRackMbean() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	/**Metodos que me llenan los ComboBox y DataTable***/
	
//	/**Este me llena el ComboBox que tiene los codigo de freezer*/
//	public Collection<String> getListCodeFreezer(){
//		Collection<String> listCodeFreezer = new ArrayList<String>();
//		try {
//			listCodeFreezer = SimlabEquipService.getListCodeFreezer();
//		} catch (SimlabAppException e) {
//			SimlabAppException.addFacesMessageError(e);
//			e.printStackTrace();
//		}
//		return listCodeFreezer;
//	}	
//	/**Esto me llena el ComboBox que tiene los codigos del rack*/	
//	public void handleChange(){
//		if (this.getCodeFreezerAct() != null && !this.getCodeFreezerAct().equals("")) {			
//			try {
//				this.setListCodeRack(SimlabEquipService.getListCodeRack(Integer.parseInt(this.getCodeFreezerAct())));
//			} catch (NumberFormatException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SimlabAppException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}					
//		}else {
//			this.setListCodeRack(null);
//		}
//	}
	
	/***Este metodo me llena el dataTable de la derecha (Freezer Origen)***/
	public Collection<Rack> getListAllRack(){
		Collection<Rack> listRack = new ArrayList<Rack>();
		try {
			if (this.getCodeFreezerAct() == null || this.getCodeFreezerAct() == 0) {
				listRack = SimlabEquipService.getListRackByFreezer(0);
			}else {
				listRack = SimlabEquipService.getListRackByFreezer(this.getCodeFreezerAct());
			}
			
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return listRack;
	}
	
	/***Este metodo me llena el dataTable de la izquierda (Freezer Destino)***/
	public Collection<Rack> getListAllRackB(){
		Collection<Rack> listRack = new ArrayList<Rack>();
		try {
			if (this.getCodeFreezerAct() == null || this.getCodeFreezerAct() == 0) {
				listRack = SimlabEquipService.getListRackByFreezer(0);
			}else {
				listRack = SimlabEquipService.getListRackByFreezer(this.getCodeFreezerNew());
			}
			
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return listRack;
	}
	
	/***************Acciones**********************/
	public void actionMoveRack(ActionEvent event){
		
		Session session = null;
		try {
			validateFields();
			session = this.openSessionAndBeginTransaction();
			SimlabEquipService.moveRackToFreezer(session, this.getCodeFreezerAct(), this.getCodeFreezerNew(), this.getCodeRack(), this.getPositionRack());
			fieldToNull();
			this.closeSessionAndCommitTransaction(session);
			
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
	}
	/***validaciones***/
	private void validateFields() throws SimlabAppException{
		
		/*Validacion de que los campos no se encuentren en null*/
		if(SimlabNumberUtil.isNull(this.getCodeFreezerAct()) || this.getPositionRack() == 0)throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getCodeFreezerNew()) || this.getPositionRack() == 0)throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getCodeRack()))throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getPositionRack()) || this.getPositionRack() == 0)throw new SimlabAppException(11000);		
		/*Validacion de que los campos no se encuentren en null*/
		
		Freezer f = new Freezer();
		/*Validando que el codigo de freezer exista*/
		f = SimlabEquipService.getObjectFreezer(this.getCodeFreezerAct());
		if (f == null) throw new SimlabAppException(11017);
		
		/*Validando que el codigo de rack exista para el freezer elegido*/
		Rack r = SimlabEquipService.getRack(this.getCodeFreezerAct(), this.getCodeRack());
		if(r == null)throw new SimlabAppException(11016);
		
		/*Validando que el codigo de freezer exista donde se va a mover el rack*/
		f = SimlabEquipService.getObjectFreezer(this.getCodeFreezerNew());
		if (f == null) throw new SimlabAppException(11018);
		
		/*Validando que el freezer no se encuentre lleno*/
		if(SimlabEquipService.isMaxCapacFreezer(this.getCodeFreezerNew()))throw new SimlabAppException(11002);
		
		/*Validando que la posicion donde se coloca el rack este libre*/
		if (!SimlabEquipService.isPositionAvailableInRack(this.getPositionRack(), this.getCodeFreezerNew())) throw new SimlabAppException(11005);		
	}
	
	private void fieldToNull(){
		this.setCodeFreezerAct(null);
		this.setCodeRack(simlabStringUtils.EMPTY_STRING);
		this.setCodeFreezerNew(null);
		this.setPositionRack(null);
	}
}
