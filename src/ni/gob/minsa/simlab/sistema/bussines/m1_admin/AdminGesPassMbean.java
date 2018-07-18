package ni.gob.minsa.simlab.sistema.bussines.m1_admin;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabSecurityService;
import ni.gob.minsa.simlab.sistema.services.SimlabUserService;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;

@ManagedBean(name="adminGesPassMbean")
@ViewScoped

public class AdminGesPassMbean extends GenericMbean implements Serializable{
	
	private static final long serialVersionUID = -7621502112377449620L;
	
	private String newPass = null;
	
	private String confirmNewPass = null;
	
	private String oldPass = null;
	
	private String nickName = null;

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	public String getConfirmNewPass() {
		return confirmNewPass;
	}

	public void setConfirmNewPass(String confirmNewPass) {
		this.confirmNewPass = confirmNewPass;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	//Accion Cambiar Contraseña
	public void actionChangePassword(ActionEvent event){
		Session session = null;
		try {
			session = this.openSessionAndBeginTransaction();
			//Validamos los Campos
			this.validateFieldNull();
			//Validamos las Contraseñas ingresadas
			this.validationPass();
			//Validamos que el Usuario Exista
			SimlabUserService.userIsAbleAndExist(this.getNickName());
			SimlabSecurityService.saveOrUpdatePass(this.getNickName(), this.getNewPass(), true, session);
			this.closeSessionAndCommitTransaction(session);
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
		}finally{HibernateUtil.analizeForRollbackAndCloseSession(session);}	
		}
	
	private void validateFieldNull() throws SimlabAppException{
		//Validamos que el Campo NickName no se encuentre vacio
		if(simlabStringUtils.isNullOrEmpty(this.getNickName()))throw new SimlabAppException(11008);
		//Validamos que el Campo Antigua contraseña no se encuentre nula
		if(simlabStringUtils.isNullOrEmpty(this.getOldPass()))throw new SimlabAppException(11012);
		//Validamos que el Campo Nueva Contraseña no se encuentre nulo
		if(simlabStringUtils.isNullOrEmpty(this.getNewPass()))throw new SimlabAppException(11012);
		//Validamos que el Campo Confirmar Contraseña no se encuentre nulo
		if(simlabStringUtils.isNullOrEmpty(this.getConfirmNewPass()))throw new SimlabAppException(11012);
	}
	
	private void validationPass() throws SimlabAppException{
		//Encriptamos la Contraseña Antigua y Verificamos la Igualdad por medio del Codigo de Usuario
		if(!simlabStringUtils.areEquals(SimlabSecurityService.getPassAsString(this.getNickName()),SimlabSecurityService.generateMd5Key(this.getOldPass())))
			throw new SimlabAppException(11016);
		//V1erificamos la Igualdad de la Nueva Contraseña
		if(!simlabStringUtils.areEquals(SimlabSecurityService.generateMd5Key(this.getNewPass()), SimlabSecurityService.generateMd5Key(this.getConfirmNewPass())))
			throw new SimlabAppException(11016);
	}
	
}
