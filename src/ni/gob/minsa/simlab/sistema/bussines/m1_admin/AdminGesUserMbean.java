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
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.services.SimlabPatternService;
import ni.gob.minsa.simlab.sistema.services.SimlabProfileService;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.services.SimlabSecurityService;
import ni.gob.minsa.simlab.sistema.services.SimlabUserService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Perfil;
import ni.gob.minsa.sistema.hibernate.bussines.UsuaPerf;
import ni.gob.minsa.sistema.hibernate.bussines.Usuario;
import ni.gob.minsa.sistema.hibernate.bussines.UsuarioId;

import org.hibernate.Session;

@ManagedBean(name="adminGesUserMbean")
@ViewScoped

public class AdminGesUserMbean extends GenericMbean implements Serializable{
	
	private static final long serialVersionUID = -7621502147877449620L;
	
	private int mode;
	
	private int switchModeEditOrAdd;
	
	private String   nameUserFirst = null;
	
	private String nameUserSecond = null;
	
	private String codeUser = null;
	
	private String lastNameUserFirst = null;
	
	private String lastNameUserSecond = null;
	
	private String idUser = null;
	
	private String emailUser = null;
	
	private String phoneUser = null;
	
	private String comentUser = null;
	
	private String passUser = null;
	
	private String passUserConf = null;
	
	private Usuario userToEdit = null;
	
	private String labelAddOrEdit = null;
	
	private String profileToAssociate = null;
	
	public String getProfileToAssociate() {
		return profileToAssociate;
	}

	public void setProfileToAssociate(String profileToAssociate) {
		this.profileToAssociate = profileToAssociate;
	}

	public String getLabelAddOrEdit() {
		return labelAddOrEdit;
	}

	public void setLabelAddOrEdit(String labelAddOrEdit) {
		this.labelAddOrEdit = labelAddOrEdit;
	}

	public Usuario getUserToEdit() {
		return userToEdit;
	}

	public void setUserToEdit(Usuario userToEdit) {
		this.userToEdit = userToEdit;
	}

	public String getPassUser() {
		return passUser;
	}

	public void setPassUser(String passUser) {
		this.passUser = passUser;
	}

	public String getPassUserConf() {
		return passUserConf;
	}

	public void setPassUserConf(String passUserConf) {
		this.passUserConf = passUserConf;
	}

	public String getNameUserFirst() {
		return nameUserFirst;
	}

	public void setNameUserFirst(String nameUserFirst) {
		this.nameUserFirst = nameUserFirst;
	}

	public String getNameUserSecond() {
		return nameUserSecond;
	}

	public void setNameUserSecond(String nameUserSecond) {
		this.nameUserSecond = nameUserSecond;
	}

	public String getCodeUser() {
		return codeUser;
	}

	public void setCodeUser(String codeUser) {
		this.codeUser = codeUser;
	}

	public String getLastNameUserFirst() {
		return lastNameUserFirst;
	}

	public void setLastNameUserFirst(String lastNameUserFirst) {
		this.lastNameUserFirst = lastNameUserFirst;
	}

	public String getLastNameUserSecond() {
		return lastNameUserSecond;
	}

	public void setLastNameUserSecond(String lastNameUserSecond) {
		this.lastNameUserSecond = lastNameUserSecond;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(String emailUser) {
		this.emailUser = emailUser;
	}

	public String getPhoneUser() {
		return phoneUser;
	}

	public void setPhoneUser(String phoneUser) {
		this.phoneUser = phoneUser;
	}

	public String getComentUser() {
		return comentUser;
	}

	public void setComentUser(String comentUser) {
		this.comentUser = comentUser;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getSwitchModeEditOrAdd() {
		return switchModeEditOrAdd;
	}

	public void setSwitchModeEditOrAdd(int switchModeEditOrAdd) {
		this.switchModeEditOrAdd = switchModeEditOrAdd;
	}

	public boolean isModePanelPrincipal(){
		return this.getMode()==0;
	}
	
	public boolean isModePanelAddOrEditUser(){
		return this.getMode()==1;
	}
	
	public boolean isModeAsociate(){
		return this.getMode()==2;
	}
	
	//Manejamos la transaccion de Agregacion o Edicion de Usuario
	public boolean isModeAddOrEdit(){
		return this.getSwitchModeEditOrAdd()==0;
	}
	
	@SuppressWarnings("unused")
	private Collection<Usuario> listUser = new ArrayList<Usuario>();
	
	public Collection<Usuario> getListUser() {
		Collection<Usuario> listUser = new ArrayList<Usuario>();
		try {
			if(SimlabUserService.getListUser()!=null)
				listUser = SimlabUserService.getListUser();
				//Obtenemos la lista de Usuarios y la Retornamos para la vista
			else listUser = null;
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listUser;
	}

	public void setListUser(Collection<Usuario> listUser) {
		this.listUser = listUser;
	}

	public AdminGesUserMbean(){
		super();
		this.setMode(0);
		}
	
	//Accion de Agregar Usuario
	public void actionAddUser(ActionEvent event){
		//Indicamos que la Vista es de Modo Agregar Usuario
		this.setSwitchModeEditOrAdd(0);
		this.setLabelAddOrEdit(SimlabPropertiesService.getPropertiesLabel("admin_user_add"));
		this.setMode(1);
	}
	
	//Validamos los campos Requeridos
	public void validateRequiredField() throws SimlabAppException{
		//Validamos que el Campo codigo de Usuario no se encuentre Nulo
		if(simlabStringUtils.isNullOrEmpty(this.getCodeUser()))throw new SimlabAppException(10052);
		//Validamos que el nombre de Usuario no contenga Espacios
		//TODO: revisar este metodo
//		if(this.getCodeUser().contains(simlabStringUtils.EMPTY_STRING)) throw new SimlabAppException(11015);
		//Verificamos que el Usuario a Ingresar no se encuentre Registrado o Vigente.
		if(this.isModeAddOrEdit()){
		Usuario user = SimlabUserService.getUserByNickName(this.getCodeUser());
		if(user!=null)throw new SimlabAppException(11040);
		//Verificamos que el Numero de Cédula no se encuentre Registrado para Otro Usuario
		for (Usuario usuario : SimlabUserService.getListUser()) {
			if(simlabStringUtils.areEquals(this.getIdUser(), usuario.getUsuaIdenti()))throw new SimlabAppException(11041);
		}
		//Validamos que ninguno de los dos Campos de Contraseña se encuentre Vacio
		if(simlabStringUtils.isNullOrEmpty(this.getPassUser())||simlabStringUtils.isNullOrEmpty(this.getPassUserConf()))throw new SimlabAppException(11042);
		}
		//Validamos que el Campo Primer Nombre no se encuentre Nulo
		if(simlabStringUtils.isNullOrEmpty(this.getNameUserFirst()))throw new SimlabAppException(11043);
		//Validamos que el Campo Primer Apellido no se encuentre Nulo
		if(simlabStringUtils.isNullOrEmpty(this.getLastNameUserFirst()))throw new SimlabAppException(11044);
		//Validamos que el Campo Cedula no se encuentre Nulo
		if(simlabStringUtils.isNullOrEmpty(this.getIdUser()))throw new SimlabAppException(11045);
		if(this.getIdUser().length()<14)throw new SimlabAppException(11014);
		// Validamos si el Formato del Numero de Cedula es Valido.
		if(!SimlabPatternService.isRightPattern(this.getIdUser(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON,2 )))throw new SimlabAppException(10016);
		//Validamos si el Email introducido es valido.
		if(!SimlabPatternService.isRightPattern(this.getEmailUser(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 3)))throw new SimlabAppException(10018);

	}
	
	//Validamos los campos Contraseña
	public void securityValidationPass() throws SimlabAppException{
		//Validamos que sean mayor a 4 caracteres
		if(this.getPassUser().length()<4)throw new SimlabAppException(11039);
		//Validamos que las 2 Contraseñas sean Iguales
		if(!simlabStringUtils.areEquals(this.getPassUser(), this.getPassUserConf()))throw new SimlabAppException(11046);
	}
	
	//Agregamos los datos del Nuevo Usuario a la Base de Datos.
	public void addUserOrEdit(Session session) throws SimlabAppException{
		try {
			String name = SimlabUserService.getCompleteNameUser(this.getNameUserFirst(), this.getNameUserSecond(), this.getLastNameUserFirst(), this.getLastNameUserSecond());
			String afirmation = SimlabParameterService.getParameterCode(CatalogParam.SI_NO, 1);
			//Agregamos un nuevo usuario si estamos en modo agregar 
			if(this.isModeAddOrEdit()){
				Usuario usuario = 
				new Usuario(new UsuarioId(this.getCodeUser(), SimlabDateUtil.MAX_DATE), name, this.getIdUser(), 
							afirmation, this.getEmailUser(), this.getPhoneUser(), this.getComentUser(), SimlabDateUtil.getCurrentDate());
			//Guardamos el Objeto Usuario
			session.save(usuario);
			}
			else{
					this.getUserToEdit().setUsuaName(name);
					this.getUserToEdit().setUsuaIdenti(this.getIdUser());
					this.getUserToEdit().setUsuaEmail(this.getEmailUser());
					this.getUserToEdit().setUsuaComment(this.getComentUser());
					this.getUserToEdit().setUsuaTelf(this.getPhoneUser());
					session.update(this.getUserToEdit());
			}
		} catch (Exception e) {
			throw SimlabAppException.generateExceptionByInsert(AdminGesUserMbean.class, e);
		}	
	}
	
	//Accion de Editar Usuario	
	public void actionEditUser(ActionEvent event, Usuario usuario){
		//Modo Edicion
		this.setSwitchModeEditOrAdd(1);
		this.setLabelAddOrEdit(SimlabPropertiesService.getPropertiesLabel("admin_user_edit"));
		//Setamos los campos con el valor del usuario seleccionado.
		this.setUserToEdit(usuario);
		this.setCodeUser(usuario.getId().getUsuaCode());
		this.setIdUser(usuario.getUsuaIdenti());
		this.setEmailUser(usuario.getUsuaEmail());
		this.setPhoneUser(usuario.getUsuaTelf());
		this.setComentUser(usuario.getUsuaComment());
		this.setMode(1);
	}
	
	//Accion de Cerrar Vigencia
	public void actionGetUser(ActionEvent event, Usuario usuario){
		//Cerramos la vigencia del Usuario
		this.setUserToEdit(usuario);
	}
	
	//Hacemos la Carga de NickName de Usuario
	public void loadCodeUser(ActionEvent event, Usuario usuarioToAsociate)
	{
		this.setCodeUser(usuarioToAsociate.getId().getUsuaCode());
	}
	
	//Asociamos el Perfil al Usuario.
	public void actionAsociationProfile(ActionEvent event){
		Session session  = null;
		try {
			//Abrimos session y Comenzamos Transaccion
			session = this.openSessionAndBeginTransaction();
			String perfilToAsociate = null;
			for (Perfil perfil: SimlabProfileService.getListProfile()) {
				if(simlabStringUtils.areEquals(this.getProfileToAssociate(), perfil.getDescPerf()))
					perfilToAsociate = perfil.getCodPerf();					
			}
			//Hacemos las validaciones para el Perfil y el Usuario.
			this.validateAsociationProfileUser(perfilToAsociate);
			//Guardamos la asociacion
			SimlabUserService.savePerfil(session, this.getCodeUser(), perfilToAsociate);	
			this.closeSessionAndCommitTransaction(session);	
			this.setProfileToAssociate(null);
			this.setCodeUser(null);
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
		
	}
	
	//Validacion de los campos de Asociacion con usuario
	private void validateAsociationProfileUser(String codePerfil) throws SimlabAppException{
		//Validamos que se haya seleccionado un Elemento Perfil de la Lista
		if(simlabStringUtils.isNullOrEmpty(codePerfil))throw new SimlabAppException(11047);
		for (UsuaPerf usuaPerf: SimlabProfileService.getListUserProfile(codePerfil)) {
			//Validamos si el usuario ya tiene una asociacion con el Perfil
			if(simlabStringUtils.areEquals(usuaPerf.getId().getCodUser(), this.getCodeUser()))
				throw new SimlabAppException(11048);
		}
		
	}
	
	
	public void actionCloseVigUser(ActionEvent event){
		SimlabUserService.closeVigToUser(this.getUserToEdit().getId().getUsuaCode());
	}
	
	//Accion Cancelar Registro
	public void actionCancel(ActionEvent event){
		this.madeEmptyFields();
		//Cambiamos de Modo Formulario a Modo Vista Inicial
		this.setMode(0);
	}
	
	//Accion Procesar Registro
	public void actionProcess(ActionEvent event){
		Session session = null;
		//Si es Nuevo Ingreso
		try {
			//Validamos todos los campos
			this.validateRequiredField();
			//Validamos los Campos Contraseña
			if(this.isModeAddOrEdit())
			this.securityValidationPass();
			//Abrimos Session
			session = this.openSessionAndBeginTransaction();
			//Registramos el Nuevo Usuario o lo Editamos
			this.addUserOrEdit(session);
			if(this.isModeAddOrEdit())
				
			//Registramos la Seguridad del Usuario si nos vamos a modo  
			if(this.isModeAddOrEdit())
				SimlabSecurityService.saveOrUpdatePass(this.getCodeUser(), this.getPassUser(), false, session);
			//Cerramos Session y hacemos commit
			this.closeSessionAndCommitTransaction(session);
			this.madeEmptyFields();
			this.setMode(0);
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
				e.printStackTrace();
			}finally{
				HibernateUtil.analizeForRollbackAndCloseSession(session);
			}
		}
	
	//Obtenemos la lista de Perfiles para la Vista de Asociacion
	public Collection<String> getProfileForView(){
		Collection<String> profileForView = new ArrayList<String>();
		try {
			//Recorremos la lista de Perfiles
			for (Perfil perfil: SimlabProfileService.getListProfile()) {
					//Agregamos a la coleccion 
					profileForView.add(perfil.getDescPerf());
				}
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return profileForView;
	}
	
	//Limpiamos los campos 
	private void madeEmptyFields(){
		this.setCodeUser(simlabStringUtils.EMPTY_STRING);
		this.setNameUserFirst(simlabStringUtils.EMPTY_STRING);
		this.setNameUserSecond(simlabStringUtils.EMPTY_STRING);
		this.setLastNameUserFirst(simlabStringUtils.EMPTY_STRING);
		this.setLastNameUserSecond(simlabStringUtils.EMPTY_STRING);
		this.setIdUser(simlabStringUtils.EMPTY_STRING);
		this.setEmailUser(simlabStringUtils.EMPTY_STRING);
		this.setPhoneUser(simlabStringUtils.EMPTY_STRING);
		this.setComentUser(simlabStringUtils.EMPTY_STRING);
	}
}
