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
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Caja;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;

@ManagedBean(name="boxesPbmcMbean")
@ViewScoped
public class BoxesPbmcMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 9030495036433856461L;
	
	private String titlePanel;
	private Integer mode;
	
	private Integer tanqueId;
	private String codeRack;
	
	public String getPosView(){
		return SimlabParameterService.getParameterCode(CatalogParam.POS_NEG, 1);
	}
	
	public String getNegView(){
		return SimlabParameterService.getParameterCode(CatalogParam.POS_NEG, 2);
	}
	
	public String getNRView(){
		return SimlabParameterService.getParameterCode(CatalogParam.POS_NEG, 3);
	}

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
	
	public Integer getTanqueId() {
		return tanqueId;
	}

	public void setTanqueId(Integer tanqueId) {
		this.tanqueId = tanqueId;
	}

	public String getCodeRack() {
		return codeRack;
	}

	public void setCodeRack(String codeRack) {
		this.codeRack = codeRack;
	}

	

	public BoxesPbmcMbean() {
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

	}
	
	private void loadData(String rackCode){
		Rack rack = SimlabEquipService.getRack(rackCode);
		codeRack = rack.getId().getCodRack();	
	}
	
	public void validateDataRack() throws SimlabAppException{
		
		/*validando que los campo no esten vacios*/
		if(simlabStringUtils.isNullOrEmpty(this.codeRack)) throw new SimlabAppException(11000);
		
	}
	
	
	/***************************Populate ComboBox-DataTable**************************************************/
	public Collection<Caja> getListCaja(){
		Collection<Caja> lisCajas = new ArrayList<Caja>();
		try {
			if (this.getCodeRack() == null) {
				lisCajas = SimlabEquipService.getCajaByRack("");
			}else {
				lisCajas = SimlabEquipService.getCajaByRack(this.getCodeRack());
			}	
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return lisCajas;
	}
	
}
