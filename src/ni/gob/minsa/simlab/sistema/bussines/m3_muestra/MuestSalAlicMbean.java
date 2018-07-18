package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.simlabAlicuotaService;
import ni.gob.minsa.simlab.sistema.services.simlabDescriptionService;

import org.hibernate.Session;

@ManagedBean(name="muestSalAlicMbean")
@ViewScoped
public class MuestSalAlicMbean extends GenericMbean implements Serializable {
	private static final long serialVersionUID = 2343444859478453102L;

	private String  codeAlic;
	private String  volSalida;
	private String  titlePanel;
	private Integer mode;
	
	private String tipo;
	private String  usoAlic;

	public void setTipo(String tipo){
		this.tipo = tipo;
	}
	
	public String getTipo(){
		return tipo;
	}
	
	public void setVolSalida(String volSalida) {
		this.volSalida = volSalida;
	}
	
	public String getVolSalida() {
		return 	volSalida;
	}
	
	public Collection<String> getListUsos(){
		Collection<String> listUso = new ArrayList<String>();
		try{
			listUso = simlabDescriptionService.getListUseAlic();
		}catch(SimlabAppException e){
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return
				listUso;
	}
	
	public Collection<String> getListAlics(){
		Collection<String> listAlic = new ArrayList<String>();
		try{
			listAlic = simlabDescriptionService.getCodListRegAlic();
		}catch(SimlabAppException e){
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return
				listAlic;
	}
	
	public String getUsoAlic(){
		return this.usoAlic;
	}
	
	public void setUsoAlic(String usoAlic){
		this.usoAlic = usoAlic;
	}
	
	public String getCodeAlic()	{
		return 
				codeAlic;
	}
	
	public void setCodeAlic(String codeAlic) {
		this.codeAlic = codeAlic;
	}	
	
	public String setTitlePanel() {
		return
				titlePanel;
	}
	
	public void getTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}

	public int getMode() {
		return
				mode;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	//Contructor
	public MuestSalAlicMbean() {
		super();
		this.setMode(0);
	}
	
	/** * * Eventos* * */
	public void actionRegisterSalida(ActionEvent event) {
		Session session = null;
		try{			
			session = this.openSessionAndBeginTransaction();
			simlabAlicuotaService.addSalAlic(session,  this.getCodeAlic(), this.getVolSalida(), this.getUsoAlic());
			this.closeSessionAndCommitTransaction(session);
		}finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
		try{
			session = this.openSessionAndBeginTransaction();
			simlabAlicuotaService.addDescargo(session, this.getCodeAlic(), this.getVolSalida());
			this.closeSessionAndCommitTransaction(session);
		}finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
			FieldToNull();
		}
	}
	
	public void actionCancel(ActionEvent event) {
		this.FieldToNull();
		this.setMode(1);
	}
	
	/** * * Fin Eventos* * */
	
	/** * * Validaciones* * */
	
	private void FieldToNull() {
		this.setCodeAlic(null);
		this.setVolSalida(null);
		this.setTipo(null);
	}
}