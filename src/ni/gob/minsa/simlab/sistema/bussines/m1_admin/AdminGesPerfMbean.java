package ni.gob.minsa.simlab.sistema.bussines.m1_admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabMenuService;
import ni.gob.minsa.simlab.sistema.services.SimlabProfileService;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.services.SimlabUserService;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Perfil;
import ni.gob.minsa.sistema.hibernate.bussines.Transacs;

import org.hibernate.Session;

@ManagedBean(name="adminGesPerfMbean")
@ViewScoped

public class AdminGesPerfMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = -7330410595167422414L;

	private int switchView;
		
	private String labelAddOrEdit = null;
	
	private String codigoPerfil = null;
	
	private String descriptionPerfil = null;
	
	private Perfil perfilToEdit = null;
	
	private Transacs transactionToAsociate = null;
	
	private String txSelected = null;
	
	private Collection<Transacs> txOfPerfil = new ArrayList<Transacs>();
	
	public Collection<Transacs> getTxOfPerfil() {
		return txOfPerfil;
	}

	public void setTxOfPerfil(Collection<Transacs> txOfPerfil) {
		this.txOfPerfil = txOfPerfil;
	}

	public String getTxSelected() {
		return txSelected;
	}

	public void setTxSelected(String txSelected) {
		this.txSelected = txSelected;
	}

	public Transacs getTransactionToAsociate() {
		return transactionToAsociate;
	}

	public void setTransactionToAsociate(Transacs transactionToAsociate) {
		this.transactionToAsociate = transactionToAsociate;
	}

	public Perfil getPerfilToEdit() {
		return perfilToEdit;
	}

	public void setPerfilToEdit(Perfil perfilToEdit) {
		this.perfilToEdit = perfilToEdit;
	}

	public String getCodigoPerfil() {
		return codigoPerfil;
	}

	public void setCodigoPerfil(String codigoPerfil) {
		this.codigoPerfil = codigoPerfil;
	}

	public String getDescriptionPerfil() {
		return descriptionPerfil;
	}

	public void setDescriptionPerfil(String descriptionPerfil) {
		this.descriptionPerfil = descriptionPerfil;
	}

	public String getLabelAddOrEdit() {
		return labelAddOrEdit;
	}

	public void setLabelAddOrEdit(String labelAddOrEdit) {
		this.labelAddOrEdit = labelAddOrEdit;
	}

	public int getSwitchView() {
		return switchView;
	}

	public void setSwitchView(int switchView) {
		this.switchView = switchView;
	}
	
	public boolean isMode(){
		return getSwitchView()==0;
	}
	
	public AdminGesPerfMbean(){
		super();
		this.setSwitchView(0);
	}

	public Collection<Perfil> getListProfileAble(){
		Collection<Perfil> listProfileAble = new ArrayList<Perfil>();
			try {
				listProfileAble = SimlabProfileService.getListProfile();
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
			}
		return listProfileAble;
	}
	
	//Accion de Cambio de Vista a Formulario de Agregar Perfil
	public void actionAddProfile(ActionEvent event){
		this.setLabelAddOrEdit(SimlabPropertiesService.getPropertiesLabel("pf_add"));
		this.setSwitchView(1);
	}

	//Accion para Habilitar el Cambio de Vista para Editar el Perfil
	public void actionEditProfile(ActionEvent event, Perfil perfil){
		//Seteamos la Etiqueta
		this.setLabelAddOrEdit(SimlabPropertiesService.getPropertiesLabel("pf_edit_data"));
		//Obtenemos el Objeto Perfil a Editar
		this.setPerfilToEdit(perfil);
		//Cargamos los Datos para Actualizar
		this.loadData();
		//Seteamos la Vista a Modo Edicion
		this.setSwitchView(2);
	}
	
	//Accion para obtener el Objeto Perfil y setearlo para ser usado en la Accion de Cierre de Vigencia
	public void actionGetProfile(ActionEvent event, Perfil perfil){
		this.setPerfilToEdit(perfil);
		try {
			//Accion de Cierre de Vigencia
			SimlabProfileService.closeVigencyToProfile(this.getPerfilToEdit().getCodPerf());
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
	}
	
	//Cargamos Datos para ser Editados
	private void loadData(){
		this.setCodigoPerfil(this.getPerfilToEdit().getCodPerf());
		this.setDescriptionPerfil(this.getPerfilToEdit().getDescPerf());
	}
	
	//Cerramos la Vigencia del Perfil
	public void actionCloseVigenciProfile(ActionEvent event){}

	//Procesamos la Informacion agregada
	public void actionProcess(ActionEvent event){
		Session session = null;
		try {
			//Validamos los datos de Perfil Ingresados
			this.validateDataProfile();
			//Verificamos que el Perfil no Exista
			//TODO: Ingresar Mensaje en Base de Datos Indicando que el Perfil ya existe			
			if(SimlabProfileService.getListUserProfile(this.getCodigoPerfil()).size()!=0)throw new SimlabAppException(34);
			session = this.openSessionAndBeginTransaction();
			if(this.getSwitchView()==1)
//				this.addOrUpdateProfile(session, true);
				SimlabProfileService.addOrUpdateProfileObject(session, true, null, this.getCodigoPerfil(), this.getDescriptionPerfil());
			else
//				this.addOrUpdateProfile(session, false);
				SimlabProfileService.addOrUpdateProfileObject(session, false, this.getPerfilToEdit(), this.getCodigoPerfil(), this.getDescriptionPerfil());
			
			this.closeSessionAndCommitTransaction(session);
			this.setSwitchView(0);
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
	}
	
	//Asociar el Perfil con las Transacciones
	public void actionAsociateTransaction(ActionEvent event, Perfil profile) throws SimlabAppException{
		this.setPerfilToEdit(profile);
		this.setTxOfPerfil(this.getListTransactionByProfile());
	}
	
	public void actionDisabledTransaction(ActionEvent event, Transacs transaction){
		
	}
	
	//Asociamos Transaccion al Perfil
	public void actionAddTransactionToProfile(ActionEvent event){
		try {
			SimlabProfileService.asociateProfileWithTransac(true, null, this.getTxSelected(), this.getPerfilToEdit().getCodPerf());
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
	}	
	
	//Volvemos a la vista principal
	public void actionReturn(ActionEvent event){
		//Hacemos null la Lista de Transacciones de Perfil que se Visualia en la Vista
		this.setTxOfPerfil(null);
		//Retornamos la vista Principal.
		this.setSwitchView(0);
	}
	
	//Obtenemos la Lista de Transacciones para la Tabla
	public Collection<Transacs> getListTransactionByProfile(){
		Collection<Transacs> listTransactionAsociateToProfile = new ArrayList<Transacs>();
			try {
				listTransactionAsociateToProfile = SimlabProfileService.getTransProfileList(this.getPerfilToEdit().getCodPerf());
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
			}
		return listTransactionAsociateToProfile;
	}
	
	//Lista de Transacciones para Asociarlas al Usuario
	public Collection<Transacs> getListTx(){
			return SimlabMenuService.getListTx();
	}
	
	//Lista de Transacciones para la Vista en Menu
	public Collection<SelectItem> getMenuTx(){
		Collection<SelectItem> txToMenu = new ArrayList<SelectItem>();
		if(this.getPerfilToEdit()!=null)
			{
			try {
				for (Transacs tx:SimlabProfileService.getListTxNotAsociated(this.getPerfilToEdit().getCodPerf())) {
					//Al Menu de la Vista
					txToMenu.add(new SelectItem(tx.getCodTrans(), tx.getDescTrans()));
				} 
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
			}
		}
		return txToMenu;
	}
	
	public Collection<Transacs> getTransaction() throws SimlabAppException{
		return SimlabUserService.getListTx();
	}
	
	//Cancelamos el Registro y Retornamos 
	public void actionCancel(ActionEvent event){
		this.dataToNull();
		this.setSwitchView(0);
	}
	
	
	//Seteams los p
	private void dataToNull(){
		this.setCodigoPerfil(simlabStringUtils.EMPTY_STRING);
		this.setDescriptionPerfil(simlabStringUtils.EMPTY_STRING);
	}
	
	private void validateDataProfile() throws SimlabAppException{
		if(simlabStringUtils.isNullOrEmpty(this.getCodigoPerfil()))throw new SimlabAppException(11026);
		if(this.getCodigoPerfil().length()>5)throw new SimlabAppException(11027);
		if(simlabStringUtils.isNullOrEmpty(this.getDescriptionPerfil()))throw new SimlabAppException(11021);
		if(this.getCodigoPerfil().length()>50)throw new SimlabAppException(11028);
	}
	
}
