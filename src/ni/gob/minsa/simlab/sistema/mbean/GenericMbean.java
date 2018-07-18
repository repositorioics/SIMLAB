package ni.gob.minsa.simlab.sistema.mbean;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabFacesUtils;

import org.hibernate.Session;

@javax.faces.bean.ManagedBean
@SessionScoped

public abstract class GenericMbean implements Serializable {

	private static final long serialVersionUID = 6620349450688900487L;
	
	private String titleTransaction = null;
	
	public String getTitleTransaction() {
		return titleTransaction;
	}

	public void setTitleTransaction(String titleTransaction) {
		this.titleTransaction = titleTransaction;
	}

	@ManagedProperty(value = "#{loginMbean}")
	private LoginMbean loginMbean;
	
	public LoginMbean getLoginMbean() throws SimlabAppException {
		if(this.loginMbean==null||!this.loginMbean.isLoged())throw new SimlabAppException(23);
		return loginMbean;
	}

	public void setLoginMbean(LoginMbean loginMbean) {this.loginMbean = loginMbean;}

	public GenericMbean(){
		super();
		this.loginMbean = (LoginMbean) SimlabFacesUtils.getValueExpression("#{loginMbean}", LoginMbean.class);
	}
	
	/**Acceso Rapido Usuario Logueado*/
	public boolean isLoged() throws SimlabAppException{
		return this.getLoginMbean().isLoged();
	}
	
	 
	/**Acceso Rapido al Codigo de Usuario*/
	public String getUserCode() throws SimlabAppException{
		return this.getLoginMbean().getCodeUser();
	}
	
	public String getTransactionTitle(){
		return SimlabPropertiesService.getPropertiesLabel("system_name_title_tx_"+this.getClass().getSimpleName());
	}
	
	/*Abrimos Session y Transaccion*/
	public Session openSessionAndBeginTransaction(){
		Session session = HibernateUtil.openSesion();
		session.beginTransaction();
		return session;
	}
	
	public void closeSessionAndCommitTransaction(Session session){
		if(session==null || !session.isOpen())return;
			try {
				session.flush();
				session.clear();
				//Hacemos Commit
				session.getTransaction().commit();
				//Cerramos la Session
				session.close(); session = null;
			} catch (Throwable e) {
				//Hacemos Rollback
				HibernateUtil.analizeForRollbackAndCloseSession(session);
				//Registramos el Logger
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(),e);
			}
	}
	
}