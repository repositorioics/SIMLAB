package ni.gob.minsa.simlab.sistema.bussines.m1_admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.services.SimlabTxService;
import ni.gob.minsa.simlab.sistema.services.SimlabUserService;
import ni.gob.minsa.simlab.sistema.services.simlabDescriptionService;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.DetParam;
import ni.gob.minsa.sistema.hibernate.bussines.Transacs;

@ManagedBean(name="adminGesTransMbean")
@ViewScoped

public class AdminGesTransMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 706863022336557806L;
	
	private int switchMode ;
	
	private String labelAddOrEdit = null;
	
	private String codeTx = null;
	
	private String descriptionTx = null;
	
	private String modPert = null;
	
	private String indEntrada = null;
	
	private String indDeveloped = null;
	
	private String indHabilited = null;
	
	private Transacs transact = null;
	
	public Transacs getTransact() {
		return transact;
	}

	public void setTransact(Transacs transact) {
		this.transact = transact;
	}

	public String getModPert() {
	    return modPert;
	}

	public void setModPert(String modPert) {
		this.modPert = modPert;
	}

	public String getIndEntrada() {
		return indEntrada;
	}

	public void setIndEntrada(String indEntrada) {
		this.indEntrada = indEntrada;
	}

	public String getIndDeveloped() {
		return indDeveloped;
	}

	public void setIndDeveloped(String indDeveloped) {
		this.indDeveloped = indDeveloped;
	}

	public String getIndHabilited() {
		return indHabilited;
	}

	public void setIndHabilited(String indHabilited) {
		this.indHabilited = indHabilited;
	}

	public String getDescriptionTx() {
		return descriptionTx;
	}

	public void setDescriptionTx(String descriptionTx) {
		this.descriptionTx = descriptionTx;
	}

	public String getCodeTx() {
		return codeTx;
	}

	public void setCodeTx(String codeTx) {
		this.codeTx = codeTx;
	}

	public String getLabelAddOrEdit() {
		return labelAddOrEdit;
	}

	public void setLabelAddOrEdit(String labelAddOrEdit) {
		this.labelAddOrEdit = labelAddOrEdit;
	}

	public int getSwitchMode() {
		return switchMode;
	}

	public void setSwitchMode(int switchMode) {
		this.switchMode = switchMode;
	}

	public boolean isModePrincipalView(){
		return this.getSwitchMode()==0;
	}

	public boolean isModeAdd(){
		return this.getSwitchMode()==1;
	}
	
	public AdminGesTransMbean(){
		super();
		this.setSwitchMode(0);
	}
	//Obtenemos la lista de Transacciones para la Vista
	public Collection<Transacs> getTransListHab(){
		Collection<Transacs> listTransHab = new ArrayList<Transacs>();
		 try {
			listTransHab = SimlabUserService.getListTx();
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listTransHab;
	}
	
	//Accion de Agregra Transaccion
	public void actionAddTransac(ActionEvent event){ 
		//Seteamos
		this.setLabelAddOrEdit(SimlabPropertiesService.getPropertiesLabel("tx_add"));
		this.setSwitchMode(1);
		
	}
	
	//Accion Editar Transaccion
	public void actionEditTransac(ActionEvent event, Transacs tx){
		this.setLabelAddOrEdit(SimlabPropertiesService.getPropertiesLabel("tx_edit"));
		//Cargamos los datos de la Transaccion
		this.loadData(tx);
		this.setSwitchMode(2);
	}
	
	private void loadData(Transacs tx){
		this.setCodeTx(tx.getCodTrans());
		this.setDescriptionTx(tx.getDescTrans());
		if(simlabStringUtils.areEquals(tx.getIndHab(), "S"))
		this.setIndHabilited(simlabStringUtils.areEquals("S", tx.getIndHab())?"SI":"NO");	
		this.setIndDeveloped(simlabStringUtils.areEquals("S", tx.getIndHab())?"SI":"NO");
		this.setIndEntrada(simlabStringUtils.areEquals("S", tx.getIndHab())?"SI":"NO");
			for (DetParam detParam: SimlabParameterService.getListDetParam("LIST_MOD")) {
				if(simlabStringUtils.areEquals(detParam.getId().getCodItem(), tx.getModpTrans()))
					this.setModPert(detParam.getDescItem());
			}
		this.setTransact(tx);
	}
	
	public void actionCancel(ActionEvent event){
		this.changeFieldToNull();
		this.setSwitchMode(0);
	}
	
	public void actionProcessAddTransaction(ActionEvent event){
		try {
			//Validamos que la informacion ingresada se encuentre sin anomalias
			this.validationsFieldInput();
			if(this.getSwitchMode()==1){
			//Agregamos el Objeto Transaccion
			SimlabTxService.addTx(null, this.getCodeTx(),simlabDescriptionService.getCodeParamDet("LIST_MOD", this.getModPert()),
									this.getDescriptionTx(), 
								 	simlabDescriptionService.getCodeParamDet("SI_NO", this.getIndEntrada()) 
									, simlabDescriptionService.getCodeParamDet("SI_NO", this.getIndHabilited()) , 
									 simlabDescriptionService.getCodeParamDet("SI_NO", this.getIndDeveloped()));
			}
			else if(this.getSwitchMode()==2){
				this.getTransact().setCodTrans(this.getCodeTx());
				this.getTransact().setDescTrans(this.getDescriptionTx());
				this.getTransact().setIndDevelop(simlabDescriptionService.getCodeParamDet("SI_NO", this.getIndDeveloped()));
				this.getTransact().setIndHab(simlabDescriptionService.getCodeParamDet("SI_NO", this.getIndHabilited()));
				this.getTransact().setIndpeTrans(simlabDescriptionService.getCodeParamDet("SI_NO", this.getIndEntrada()));
				this.getTransact().setModpTrans(simlabDescriptionService.getCodeParamDet("LIST_MOD", this.getModPert()));
				HibernateUtil.updateObject(this.getTransact());
				
//				SimlabTxService.updateTx(this.getTransact(),this.getCodeTx(),simlabDescriptionService.getCodeParamDet("LIST_MOD", this.getModPert()),
//						this.getDescriptionTx(), 
//					 	simlabDescriptionService.getCodeParamDet("SI_NO", this.getIndEntrada()) 
//						, simlabDescriptionService.getCodeParamDet("SI_NO", this.getIndHabilited()) , 
//						 simlabDescriptionService.getCodeParamDet("SI_NO", this.getIndDeveloped()));
			}
			this.changeFieldToNull();
			this.setSwitchMode(0);
			} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}		
	}
	
	private void changeFieldToNull(){
		this.setCodeTx(simlabStringUtils.EMPTY_STRING);
		this.setDescriptionTx(simlabStringUtils.EMPTY_STRING);	
		this.setIndHabilited(simlabStringUtils.EMPTY_STRING);
		this.setIndEntrada(simlabStringUtils.EMPTY_STRING);
		this.setIndDeveloped(simlabStringUtils.EMPTY_STRING);
		this.setModPert(simlabStringUtils.EMPTY_STRING);		
	}
	
	//Obtenemos los Elementos SI_NO para la vista
	public Collection<String> getParamSiNo(){
		Collection<String> list = new ArrayList<String>();
		try {
			list = simlabDescriptionService.getListSiNo();
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
	return list;
	}
	
	public Collection<String> getListModule(){
		Collection<String> list = new ArrayList<String>();
			try {
				list = simlabDescriptionService.getListModulesWithEmptyString();
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
			}
		return list;
	}
	
	//Validamos los campos de Relleno del Formulario de Agregar
	private void validationsFieldInput() throws SimlabAppException{
		//Verificamos si el campo Codigo de Transaccion no se encuentra nulo o vacio
		if(simlabStringUtils.isNullOrEmpty(this.getCodeTx()))throw new SimlabAppException(11018);
		if(this.getCodeTx().length()>15)throw new SimlabAppException(11019);
//		Verificamos que se haya seleccionado un modulo de Pertenencia
		if(simlabStringUtils.isNullOrEmpty(this.getModPert()))throw new SimlabAppException(11020);
//		Verificamos que se haya descrito la Transaccion
		if(simlabStringUtils.isNullOrEmpty(this.getDescriptionTx()))throw new SimlabAppException(11021);
		if(this.getDescriptionTx().length()>50)throw new SimlabAppException(11022);
//		Verificamos que se haya seleccionado una opcion en Entrada Habilitada, Entrada o Desarrollada
		if(simlabStringUtils.isNullOrEmpty(this.getIndEntrada()))throw new SimlabAppException(11023);
		if(simlabStringUtils.isNullOrEmpty(this.getIndHabilited()))throw new SimlabAppException(11024);
		if(simlabStringUtils.isNullOrEmpty(this.getIndDeveloped()))throw new SimlabAppException(11025);
	}
	
	
	
}
