package ni.gob.minsa.simlab.sistema.mbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.services.SimlabMenuService;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.services.SimlabSecurityService;
import ni.gob.minsa.simlab.sistema.services.SimlabUserService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabConstantsUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Usuario;

import org.primefaces.model.MenuModel;

@ManagedBean
@SessionScoped
public class LoginMbean implements Serializable{
	
	private static final long serialVersionUID = -2291997005804447166L;
	//Ruta de Acceso al Sistema
	private static final String ACCES_TO_SYSTEM = "m1_default/start.xhtml";
//	m1_general/systemAcces.xhtml
	private static final String FACES_REDIRECT_TRUE = "?faces-redirect=true";
	
	//Ruta de Salida
	private static final String SYSTEM_ROOTH_PATH = ""+SimlabPropertiesService.getPropertiesLabel("short_system_name");
	
	private static final String LOGIN_FORM = "login.xhtml";
	
	private Collection<String> txHab = null;
	
	private MenuModel menuUser = null;
	
	private String codeUser = null;
	
	private String passUser = null;
	
	private boolean isLoged = false;
	
	private String CurrentTx = null; 
	
	private Usuario usuario = null;
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getCurrentTx() {
		return CurrentTx;
	}

	public void setCurrentTx(String currentTx) {
		CurrentTx = currentTx;
	}

	public Collection<String> getTxHab() {
		return txHab;
	}

	public void setTxHab(Collection<String> txHab) {
		this.txHab = txHab;
	}

	public MenuModel getMenuUser() {
		return menuUser;
	}

	public void setMenuUser(MenuModel menuUser) {
		this.menuUser = menuUser;
	}

	public boolean isLoged() {
		return isLoged;
	}

	public void setLoged(boolean isLoged) {
		this.isLoged = isLoged;
	}

	public String getCodeUser() {
		return codeUser;
	}
	
	public void setCodeUser(String codeUser) {
		this.codeUser = codeUser;
	}

	public String getPassUser() {
		return passUser;
	}

	public void setPassUser(String passUser) {
		this.passUser = passUser;
	}
	
	public LoginMbean(){}
	
	/***/
	public String doLogout(){
		//Hacemos Logued a False
		this.setLoged(false);
		//Limpiamos las lineas de Menu.
		this.setMenuUser(null);
		this.setCodeUser(null);
		this.setUsuario(null);
		//Retornamos la direccion del Formulario Login. SYSTEM_ROOTH_PATH+"/"+ 
		return SYSTEM_ROOTH_PATH+"/"+LOGIN_FORM+FACES_REDIRECT_TRUE;
	}
	
	//Validamos la autenticidad del Usuario
	public String login(){
		String redirectTo = null;
		try {
			boolean exist = initialValidation();
			if(exist==false)throw new SimlabAppException(10000);
			else {
				this.isLogued(exist);
				//Seteamos las Transacciones Habiles del usuario
				this.setTxHab(SimlabUserService.getListTxCode(this.getCodeUser()));
				//Seteamos y generamos las lineas de Menus para el Usuario
				this.generateMenuModel();
				redirectTo = ACCES_TO_SYSTEM+FACES_REDIRECT_TRUE;
				//Setamos la Transaccion o Vista actual
				this.setCurrentTx(SimlabPropertiesService.getPropertiesLabel("system_transaction_title_wellcome"));
				}
			} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			redirectTo = null;
			}
	return redirectTo;	
	}
	
	//Seteamos la bandera, esta logueado.
	public  boolean isLogued(boolean exists){
		boolean login = false;
			if(exists)this.setLoged(true);
		return login;
	}
	
	//Validaciones sobre el Ingreso del Usuario al Sistema
	public boolean initialValidation() throws SimlabAppException{
		boolean exist = false;
		//Validamos que los dos campos no se encuentren vacios
		if(simlabStringUtils.isNullOrEmpty(this.getCodeUser())&&simlabStringUtils.isNullOrEmpty(this.getPassUser()))
			throw new SimlabAppException(10003);
		//Validamos que el campo nombre de Usuario no se encuentre Vacio
		if(simlabStringUtils.isNullOrEmpty(this.getCodeUser()))
			throw new SimlabAppException(10001);
		//Validamos que el campo password no se encuentre Vacio
		if(simlabStringUtils.isNullOrEmpty(this.getPassUser()))
			throw new SimlabAppException(10002);
		String passInMd5 =  SimlabSecurityService.generateMd5Key(this.getPassUser());
		//Validamos si que el usuario exista  y se encuentre Habilitado
		SimlabUserService.userIsAbleAndExist(this.getCodeUser());
		if(SimlabUserService.getUserByPassAndNickName(passInMd5, this.getCodeUser())!=null)exist =true;
		this.setUsuario(SimlabUserService.getUserByPassAndNickName(passInMd5, this.getCodeUser()));		
		return exist;
	}
	
//Obtenemos el Modelo de Menu Programado para la Vista
	public void generateMenuModel() throws SimlabAppException{
		this.setMenuUser(SimlabMenuService.generateMenuModel(SimlabUserService.getListTxUser(this.getCodeUser())));  
		}
	
	
	/*Cuando el usuario quiere ingresar a una página del sistema diferente de la de inicio, se comprueba que éste tenga permisos para hacerlo de acuerdo a los roles
	  vinculados a él.
	 */
	public void redirigirALogInSiNoAutenticadoOAutorizado(ComponentSystemEvent pCse) {
		String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();



		if (!this.isLoged && !viewId.startsWith("/login.xhtml")) {
			String url = (SimlabConstantsUtil.LOGIN_FORM+SimlabConstantsUtil.FACES_REDIRECT_TRUE);
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			try {
				ec.redirect(url);
			} catch (IOException ex) {
				
			}
		}
	}
}