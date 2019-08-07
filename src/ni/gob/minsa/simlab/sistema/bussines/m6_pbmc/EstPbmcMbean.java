/**
 * 
 */
package ni.gob.minsa.simlab.sistema.bussines.m6_pbmc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService;
import ni.gob.minsa.simlab.sistema.services.SimlabPatternService;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.DetParam;
import ni.gob.minsa.sistema.hibernate.bussines.DetParamId;
import ni.gob.minsa.sistema.hibernate.bussines.Patron;

@ManagedBean(name="estPbmcMbean")
@ViewScoped
public class EstPbmcMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 9030495036433856461L;
	
	private String titlePanel;
	private Integer mode;
	
	private String codItem;
	private String codCat = "LIST_PBMC";
	private String descItem;
	private Integer nroOrde;
	
	private String codPatron;
	private String patron;
	private String descripcion;


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

	public EstPbmcMbean() {
		// TODO Auto-generated constructor stub
		super();
		this.setMode(0);
	}
	

	public String getCodItem() {
		return codItem;
	}

	public void setCodItem(String codItem) {
		this.codItem = codItem;
	}

	public String getCodCat() {
		return codCat;
	}

	public void setCodCat(String codCat) {
		this.codCat = codCat;
	}

	public String getDescItem() {
		return descItem;
	}

	public void setDescItem(String descItem) {
		this.descItem = descItem;
	}

	public Integer getNroOrde() {
		return nroOrde;
	}

	public void setNroOrde(Integer nroOrde) {
		this.nroOrde = nroOrde;
	}

	public String getCodPatron() {
		return codPatron;
	}

	public void setCodPatron(String codPatron) {
		this.codPatron = codPatron;
	}

	public String getPatron() {
		return patron;
	}

	public void setPatron(String patron) {
		this.patron = patron;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/******************************Acciones************************************/
	public void actionChangeViewToRegisterEstu(ActionEvent event){
		
		fieldToNull();
		this.setMode(1);
	}
	public void actionCancel(ActionEvent event){
		fieldToNull();
		this.setMode(0);
	}
	public void actionEditInfoEstu(ActionEvent event, DetParam estudio){
		fieldToNull();
		loadData(estudio);
		this.setMode(2);
	}
	
	public void actionSaveEstu(ActionEvent event){
		Session session   = null;
		try {
			validateDataEstudio();
			session = HibernateUtil.openSesion();
			DetParam detParam = new DetParam(new DetParamId(this.getCodItem(),this.getCodCat()),this.getDescItem(),this.getNroOrde());
			SimlabParameterService.saveDetParam(session,detParam);
			if(this.codPatron==null) this.setCodPatron(this.codItem);
			if(this.getDescripcion()==null) this.setDescripcion("Patron "+ this.getDescItem());
			Patron pat = new Patron(this.codPatron,this.patron,this.descripcion);
			SimlabPatternService.savePattern(session,pat);
			session.close(); session = null;
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
		this.setCodItem(null);
		this.setDescItem(null);
		this.setNroOrde(null);
		this.setCodPatron(null);
		this.setDescripcion(null);
		this.setPatron(null);
	}
	
	private void loadData(DetParam estudio){
		this.setCodItem(estudio.getId().getCodItem());
		this.setDescItem(estudio.getDescItem());
		this.setNroOrde(estudio.getNroOrde());
		Patron pat =  SimlabPatternService.getPattern(estudio.getId().getCodItem());
		this.setCodPatron(pat.getCodPatron());
		this.setDescripcion(pat.getDescripcion());
		this.setPatron(pat.getPatron());
	}
	
	public void validateDataEstudio() throws SimlabAppException{
		if(simlabStringUtils.isNullOrEmpty(this.getCodItem()))throw new SimlabAppException(11999);
		if(simlabStringUtils.isNullOrEmpty(this.getCodCat()))throw new SimlabAppException(11999);
		if(simlabStringUtils.isNullOrEmpty(this.getDescItem()))throw new SimlabAppException(11999);
		if(simlabStringUtils.isNullOrEmpty(this.getPatron()))throw new SimlabAppException(11999);
		try {
			Pattern.compile(this.getPatron());
		}
		catch (Exception e) {
			throw new SimlabAppException(12000);
		}
		
	}
	
	/***************************Populate ComboBox-DataTable**************************************************/
	public Collection<DetParam> getListStudies(){
		Collection<DetParam> listStudies = new ArrayList<DetParam>();
		listStudies = SimlabParameterService.getListDetParam("LIST_PBMC");
		return listStudies;
	}
	
}
