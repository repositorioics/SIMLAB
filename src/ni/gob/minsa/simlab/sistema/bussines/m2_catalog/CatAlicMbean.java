/**
 * 
 */
package ni.gob.minsa.simlab.sistema.bussines.m2_catalog;

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
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.services.simlabAlicuotaService;
import ni.gob.minsa.simlab.sistema.services.simlabDescriptionService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabNumberUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.CatAlicuotas;
import ni.gob.minsa.sistema.hibernate.bussines.CatAlicuotasId;
import ni.gob.minsa.sistema.hibernate.bussines.DetParam;

@ManagedBean(name="catAlicMbean")
@ViewScoped
public class CatAlicMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 9030495036433856461L;
	
	private String tipoAlicuota;
	private String estudioAlic;
	private String usoAlic;
	private String tipoMuestra;
	private String titlePanel;
	
	private Integer volumenAlic;
	private Integer temperatura;
	private Integer mode;
	
	private CatAlicuotas catAlicToEdit;
	
	public String getTipoAlicuota() {
		return tipoAlicuota;
	}

	public void setTipoAlicuota(String tipoAlicuota) {
		this.tipoAlicuota = tipoAlicuota;
	}

	public String getEstudioAlic() {
		return estudioAlic;
	}

	public void setEstudioAlic(String estudioAlic) {
		this.estudioAlic = estudioAlic;
	}

	public String getUsoAlic() {
		return usoAlic;
	}

	public void setUsoAlic(String usoAlic) {
		this.usoAlic = usoAlic;
	}

	public String getTipoMuestra() {
		return tipoMuestra;
	}

	public void setTipoMuestra(String tipoMuestra) {
		this.tipoMuestra = tipoMuestra;
	}

	public String getTitlePanel() {
		return titlePanel;
	}

	public void setTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}

	public Integer getVolumenAlic() {
		return volumenAlic;
	}

	public void setVolumenAlic(Integer volumenAlic) {
		this.volumenAlic = volumenAlic;
	}

	public Integer getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Integer temperatura) {
		this.temperatura = temperatura;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
	public CatAlicuotas getCatAlicToEdit() {
		return catAlicToEdit;
	}

	public void setCatAlicToEdit(CatAlicuotas catAlicToEdit) {
		this.catAlicToEdit = catAlicToEdit;
	}

	public CatAlicMbean() {
		// TODO Auto-generated constructor stub
		super();
		this.setMode(0);
	}
	
	/***************************Populate ComboBox-DataTable**************************************************/
	public Collection<CatAlicuotas> getListAlic(){
		Collection<CatAlicuotas> listAlic = new ArrayList<CatAlicuotas>();
		try {
			listAlic = SimlabEquipService.getListAlic();
			for (CatAlicuotas catAlicuotas : listAlic) {
				catAlicuotas.setId(getDescById(catAlicuotas.getId(),1));
				catAlicuotas.setSepUso(getCodeEquipFromType("LIST_USO_ALIC", catAlicuotas.getSepUso(), 3));
				catAlicuotas.setMuestPert(getCodeEquipFromType("LIST_MUEST_PERT", catAlicuotas.getMuestPert(), 3));
			}
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return listAlic;
	}
	public Collection<String> getListEstudioAlic(){
		Collection<String> listEstudioAlic = new ArrayList<String>();
		try {
			listEstudioAlic = simlabDescriptionService.getListEstudioAlic("LIST_EST");
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return listEstudioAlic;
	}
	public Collection<String> getListTypeAlic(){
		Collection<String>lisTypeAlic = new ArrayList<String>();
		try {
			lisTypeAlic = simlabDescriptionService.getLisTypeAlic("LIST_USO_ALIC");
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return lisTypeAlic;
	}
	public Collection<String> getListMuestPertAlic(){
		Collection<String> listMuestPertAlic = new ArrayList<String>();
		try {
			listMuestPertAlic = simlabDescriptionService.getListMuestPertAlic("LIST_MUEST_PERT");
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return listMuestPertAlic;
	}

	/******************************Acciones************************************/
	public void actionChangeViewToRegisterAlic(ActionEvent event){
		
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("register_alic"));
		this.setMode(1);
	}
	public void actionCancel(ActionEvent event){
		
		fieldToNull();
		this.setMode(0);
	}
	public void actionEditInfoAlic(ActionEvent event, CatAlicuotas catAlic){
		
		//this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel(""));
		this.setCatAlicToEdit(catAlic);
		loadData(this.getCatAlicToEdit());
		this.setMode(2);
	}
	public void actionSaveAlic(ActionEvent event){
		Session session = null;
		try {
			validateFields();
			session = this.openSessionAndBeginTransaction();
			String codEstudio = getCodeEquipFromType("LIST_EST", this.getEstudioAlic(), 1);
			String codUso = getCodeEquipFromType("LIST_USO_ALIC", this.getUsoAlic(), 1);
			String codMuestra = getCodeEquipFromType("LIST_MUEST_PERT", this.getTipoMuestra(), 1); 
			if (this.getMode() == 1) {
				
				SimlabEquipService.addAlicuota(session, this.getTipoAlicuota().toLowerCase(), codEstudio, this.getTemperatura(), 
						codUso, this.getVolumenAlic(), codMuestra);
			}else if (this.getMode() == 2) {
				simlabAlicuotaService.updateAlic(session, this.getTipoAlicuota().toLowerCase(), codEstudio, codUso, 
						this.getTemperatura(), this.getVolumenAlic(), codMuestra);
				this.setMode(0);
			}
			this.closeSessionAndCommitTransaction(session);
			this.fieldToNull();
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);			
		}
	}
	//Validaciones
	private void validateFields() throws SimlabAppException{
		if(simlabStringUtils.isNullOrEmpty(this.getTipoAlicuota()))throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getEstudioAlic()))throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getUsoAlic()))throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getTipoMuestra()))throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getVolumenAlic()) || this.getVolumenAlic() == 0)throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getTemperatura()) || this.getTemperatura() == 0)throw new SimlabAppException(11000);
		//if(Integer.toString(this.getTemperatura()).length()>=3)throw new SimlabAppException(11014);
		//if(Integer.toString(this.getVolumenAlic()).length()>=4)throw new SimlabAppException(11015);
	}
	
	//Otros Metodos
	private void loadData(CatAlicuotas _catAlic){
		_catAlic.setId(getDescById(_catAlic.getId(), 2));
		CatAlicuotas catAlic = simlabAlicuotaService.getObjAlic(_catAlic.getId().getTipoAlicuota(), _catAlic.getId().getEstudioPert());
		this.setTipoAlicuota(catAlic.getId().getTipoAlicuota());
		this.setEstudioAlic(getCodeEquipFromType("LIST_EST", _catAlic.getId().getEstudioPert(),3));
		this.setUsoAlic(getCodeEquipFromType("LIST_USO_ALIC", _catAlic.getSepUso(),2));
		this.setTemperatura(catAlic.getTempAlm());
		this.setVolumenAlic(catAlic.getVolPerm());
		this.setTipoMuestra(getCodeEquipFromType("LIST_MUEST_PERT", _catAlic.getMuestPert(),2));
	}
	private String getCodeEquipFromType(String codCat, String campo, int opcion){
		
		String descripcion = null;
		if (opcion == 1) {//codigo
			for (DetParam detParam: SimlabParameterService.getListDetParam(codCat)) {
				if(simlabStringUtils.areEquals(detParam.getDescItem(), campo)){
					descripcion = detParam.getId().getCodItem();
					break;
				}
			}
		}else if (opcion == 2) {//descripcion
			for (DetParam detParam: SimlabParameterService.getListDetParam(codCat)) {
				if(simlabStringUtils.areEquals(detParam.getDescItem(), campo)){
					descripcion = detParam.getDescItem();
					break;
				}
			}
		}else if(opcion == 3){
			for (DetParam detParam: SimlabParameterService.getListDetParam(codCat)) {
				if(simlabStringUtils.areEquals(detParam.getId().getCodItem(), campo.toUpperCase())){
					descripcion = detParam.getDescItem();
					break;
				}
			}
		}
		
		return descripcion;
	}
	private CatAlicuotasId getDescById(CatAlicuotasId cId, int opcion){
		CatAlicuotasId objCat = new CatAlicuotasId();
		if (opcion == 1) {
			objCat.setTipoAlicuota(cId.getTipoAlicuota());
			objCat.setEstudioPert(getCodeEquipFromType("LIST_EST", cId.getEstudioPert(), 3));
		}else if(opcion == 2){
			objCat.setTipoAlicuota(cId.getTipoAlicuota());
			objCat.setEstudioPert(getCodeEquipFromType("LIST_EST", cId.getEstudioPert(), 1));
		}
		
		return objCat;
	}
	private void fieldToNull(){
		this.setTipoAlicuota(simlabStringUtils.EMPTY_STRING);
		this.setEstudioAlic(simlabStringUtils.EMPTY_STRING);
		this.setUsoAlic(simlabStringUtils.EMPTY_STRING);
		this.setTipoMuestra(simlabStringUtils.EMPTY_STRING);
		this.setVolumenAlic(null);
		this.setTemperatura(null);
	}
}
