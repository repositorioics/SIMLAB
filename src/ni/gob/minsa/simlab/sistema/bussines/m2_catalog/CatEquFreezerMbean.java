package ni.gob.minsa.simlab.sistema.bussines.m2_catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.services.simlabDescriptionService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabNumberUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.DetParam;
import ni.gob.minsa.sistema.hibernate.bussines.Freezer;

@ManagedBean(name="catEquFreezerMbean")
@ViewScoped

public class CatEquFreezerMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = -9048870001012735485L;
	
	private Integer mode;
	
	private Integer cantRackToStore;
	
	private Integer codeFreezer;
	
	private String[] typeAlicToStore = null;
	
	private Integer tempMin;
	
	private Integer tempMax;
	
	private String tipEquipo;
	
	private Freezer freezerToEdit = null;
	
	private String titlePanel = null;
	
	private String motCloseVigency  = null;
	
	private String serieEquip = null;
	
	private String modeloEquipo = null;
	
	private String indCalibrate = null;
	
	private String nameResp = null;
	
	private Integer nroRow = null;
	
	private  Integer nroCol = null;
	
	private Integer fieldNroRowAndCol = null;
	
	public Integer getFieldNroRowAndCol() {
		return fieldNroRowAndCol;
	}

	public void setFieldNroRowAndCol(Integer fieldNroRowAndCol) {
		this.fieldNroRowAndCol = fieldNroRowAndCol;
	}

	public Integer getNroRow() {
		return nroRow;
	}

	public void setNroRow(Integer nroRow) {
		this.nroRow = nroRow;
	}

	public Integer getNroCol() {
		return nroCol;
	}

	public void setNroCol(Integer nroCol) {
		this.nroCol = nroCol;
	}

	public String getNameResp() {
		return nameResp;
	}

	public void setNameResp(String nameResp) {
		this.nameResp = nameResp;
	}

	public String getIndCalibrate() {
		return indCalibrate;
	}

	public void setIndCalibrate(String indCalibrate) {
		this.indCalibrate = indCalibrate;
	}

	public String getModeloEquipo() {
		return modeloEquipo;
	}

	public void setModeloEquipo(String modeloEquipo) {
		this.modeloEquipo = modeloEquipo;
	}

	public String getSerieEquip() {
		return serieEquip;
	}

	public void setSerieEquip(String serieEquip) {
		this.serieEquip = serieEquip;
	}

	public String getMotCloseVigency() {
		return motCloseVigency;
	}

	public void setMotCloseVigency(String motCloseVigency) {
		this.motCloseVigency = motCloseVigency;
	}

	public String getTitlePanel() {
		return titlePanel;
	}

	public void setTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}

	public Freezer getFreezerToEdit() {
		return freezerToEdit;
	}

	public void setFreezerToEdit(Freezer freezerToEdit) {
		this.freezerToEdit = freezerToEdit;
	}

	public String getTipEquipo() {
		return tipEquipo;
	}

	public void setTipEquipo(String tipEquipo) {
		this.tipEquipo = tipEquipo;
	}
	
	public Integer getCodeFreezer() {
		return codeFreezer;
	}

	public void setCodeFreezer(Integer codeFreezer) {
		this.codeFreezer = codeFreezer;
	}

	public String[] getTypeAlicToStore() {
		return typeAlicToStore;
	}

	public void setTypeAlicToStore(String[] string) {
		this.typeAlicToStore = string;
	}

	public Integer getTempMin() {
		return tempMin;
	}

	public void setTempMin(Integer tempMin) {
		this.tempMin = tempMin;
	}

	public Integer getTempMax() {
		return tempMax;
	}

	public void setTempMax(Integer tempMax) {
		this.tempMax = tempMax;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public Integer getCantRackToStore() {
		return cantRackToStore;
	}

	public void setCantRackToStore(Integer cantRackToStore) {
		this.cantRackToStore = cantRackToStore;
	}

	public CatEquFreezerMbean(){
		super();
		//Configuramos el Modo a Vista Inicial
		this.setMode(0);
	}
	
	public Collection<Freezer> getListFreezer(){
		Collection<Freezer> listFreezer = new ArrayList<Freezer>();
		try {
			listFreezer = SimlabEquipService.getListFreezer();
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listFreezer;
	}
	
	public Collection<Freezer> getListFreezerInactive(){
		Collection<Freezer> listFreezerInactive = new ArrayList<Freezer>();
		try {
			listFreezerInactive = SimlabEquipService.getListFreezerInactive();
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listFreezerInactive;		
	}
	
	public void actionChangeViewToRegisterNewFreezer(ActionEvent event){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("register_new_apparatus"));
		this.setMode(1);
	}
	//Obtenemos la Lista de Tipo de Freezer para la capa de presentacion.	
	public Collection<String> getListTypeFreezer(){
		Collection<String> listTypeFreezer = new ArrayList<String>();
		try {
			listTypeFreezer = simlabDescriptionService.getListTypeFreezer();
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return listTypeFreezer;
	}
	
	//Obtenemos la Lista de Usos para la capa de Presentacion
	public Collection<String> getListUse(){
		Collection<String > listUse = new ArrayList<String>();
			try {
				listUse = simlabDescriptionService.getListUseAlic();
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
			}
		return listUse;	
	}
	
	//Obtenemos Si, No para la capa de Presentacion.
	public Collection<String> getSiNoParameter(){
		Collection<String> siNoParam = new ArrayList<String>();
				try {
					siNoParam = simlabDescriptionService.getListSiNoEmpty();
				} catch (SimlabAppException e) {
					SimlabAppException.addFacesMessageError(e);
				}
		return siNoParam;
	}
	
	//habilitamos los campos Nro. Fila y Nro. Columna, en caso de no ser Tanques.
	public void eventRenderedNroRowAndCol(){
		String typeCode = this.getCodeEquipFromType();
			if(simlabStringUtils.areEquals(typeCode,SimlabParameterService.getParameterCode(CatalogParam.LIST_EQUIP, 3)))
				this.setFieldNroRowAndCol(0);
			else this.setFieldNroRowAndCol(1);
				
	}
	
	//Registramos el Equipo
	public void actionRegisterEquip(ActionEvent event){
		Session session   = null;
		try {
			//Hacemos las Respectivas Validaciones para el Registro
			this.validateDataEquip();
			session = this.openSessionAndBeginTransaction();
			String codeTypeEquipo = this.getCodeEquipFromType();
			if(this.getMode()==1){
					SimlabEquipService.addEquip(session, this.getCodeFreezer(), SimlabDateUtil.getCurrentDate(), SimlabDateUtil.MAX_DATE, 
							this.getTempMin(), this.getTempMax(), Arrays.toString(this.getTypeAlicToStore()).replace("[", "").replace("]", "").replace(" ", "").trim(), this.getCantRackToStore(), codeTypeEquipo, this.getSerieEquip()
							,this.getModeloEquipo(), this.getNameResp(), this.getNroRow(), this.getNroCol());
				}
			else if(this.getMode()==2){
				SimlabEquipService.updateEquip(session, this.getCodeFreezer(), this.getTempMin(), 
							this.getTempMax(), Arrays.toString(this.getTypeAlicToStore()).replace("[", "").replace("]", "").replace(" ", "").trim(), this.getCantRackToStore(), codeTypeEquipo,
							this.getSerieEquip(), this.getModeloEquipo(), this.getNameResp(), this.getNroRow(), this.getNroCol());
				}
				
			else if(this.getMode()==3){
					SimlabEquipService.closeVigency(session, this.getMotCloseVigency(), this.getCodeFreezer());
				}
			//Nulamos los Campos.
			this.fieldToNull();
			this.closeSessionAndCommitTransaction(session);
			this.setMode(0);
			} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
	}
	
	//Obtenemos el Codigo del Equipo en dependencia de su descripcion
	public String getCodeEquipFromType(){
		String codeTypeEquip = null;
		for (DetParam detParam: SimlabParameterService.getListDetParam(CatalogParam.LIST_EQUIP)) {
			if(simlabStringUtils.areEquals(detParam.getDescItem(), this.getTipEquipo())){
				codeTypeEquip = detParam.getId().getCodItem();
				break;
			}
		}
		return codeTypeEquip;
	}
	
	//Validamos los datos ingresados al momento de Registrar el Equipo..
	public void validateDataEquip() throws SimlabAppException{
		//Verificamos que no nos encontramos en el Modo Cierre de Vigencia.
		if(this.getMode()!=3){
		//verificamos que el Campo Codigo de Freezer no se encuentre nulo
		if(SimlabNumberUtil.isNull(this.getCodeFreezer()))throw new SimlabAppException(10007);
		//Verificamos que se encuentre seleccionado un tipo de Equipo de la lista
		if(simlabStringUtils.isNullOrEmpty(this.getTipEquipo()))throw new SimlabAppException(10008);
		//Verificamos que el Campo Uso no se encuentre nulo
		if(simlabStringUtils.isNullOrEmpty(Arrays.toString(this.getTypeAlicToStore())))throw new SimlabAppException(10009);
		//Verificamos que se haya digitado la Serie del Equipo
		if(simlabStringUtils.isNullOrEmpty(this.getSerieEquip()))throw new SimlabAppException(10019);
		//Verificamos que se haya digitado el Modelo
		if(simlabStringUtils.isNullOrEmpty(this.getModeloEquipo()))throw new SimlabAppException(10020);
		//Verificamos si se ha seleccionado un Elemento de la Lista SI_NO
//		if(simlabStringUtils.isNullOrEmpty(this.getIndCalibrate()))throw new SimlabAppException(10021);
		//Verificamos que el Campo Nombre Responsable no se encuentre nulo
		if(simlabStringUtils.isNullOrEmpty(this.getNameResp()))throw new SimlabAppException(10022);
		//Verificamos que el Campo Temp. Minima no se encuentre nulo
		if(SimlabNumberUtil.isNull(this.getTempMin()))throw new SimlabAppException(10010);
		//Verificamos que el Campo Temp. Maxima no se encuentre nulo
		if(SimlabNumberUtil.isNull(this.getTempMax()))throw new SimlabAppException(10011);
		//Verificamos que el Campo Cantidad de Racks no se encuentre nulo
		if(SimlabNumberUtil.isNull(this.getCantRackToStore()))throw new SimlabAppException(10012);
		//Validamos si no estamos tratando con un Tanque.
		if(!simlabStringUtils.areEquals(this.getCodeEquipFromType(), SimlabParameterService.getParameterCode(CatalogParam.LIST_EQUIP, 3)) )
		{
			//Verificamos si el Numero de Filas se encuentra Nulo
			if(SimlabNumberUtil.isNull(this.getNroRow()))throw new SimlabAppException(10023);
			//Varificamos si el Numero de Columnas se encuentra Nulo
			if(SimlabNumberUtil.isNull(this.getNroCol()))throw new SimlabAppException(10024);
			//Validamos que el Numero de Filas no sea 0
			if(!SimlabNumberUtil.isHigherToZero(this.getNroRow()))throw new SimlabAppException(10026);
			//Validamos que el Numero de Columnas no sea 0
			if(!SimlabNumberUtil.isHigherToZero(this.getNroCol()))throw new SimlabAppException(10025);
		}
		//Si estamos en el Formulario de Registro entonces Verificamos que el Freezer no se encuentre Registrado..
		if(this.getMode()==1)
		if(SimlabEquipService.getObjectFreezer(this.getCodeFreezer())!=null)throw new SimlabAppException(10006);
		//Si estamos en el Formulario de Edicion entonces Verificamos que el Freezer se encuentre Registrado..
		if(this.getMode()==2)
		if(SimlabEquipService.getObjectFreezer(this.getCodeFreezer())==null)throw new SimlabAppException(10013);
		//Cambiamos a vista inicial
//		this.setMode(0);
		}
		//Si la vista se encuentra en Vista Cierre de Vigencia
		else{
			
			/*Verificando que el freezer este vacio*/
			if(!SimlabEquipService.isFreezerEmpty(this.getCodeFreezer()))throw new SimlabAppException(11037);
			//Verificamos que el Campo no se encuentre nulo
			if(simlabStringUtils.isNullOrEmpty(this.getMotCloseVigency()))throw new SimlabAppException(10014);
			//Verificamos que el campo no sobrepase los 100 caracteres
			if(this.getMotCloseVigency().length()>100)throw new SimlabAppException(10015);
		}
	}
	
	//Accion para Habilitar la Vista de Edicion
	public void actionEditEquipo(ActionEvent event, Freezer freezer){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("edit_apparatus"));
		this.setFreezerToEdit(freezer);
		this.loadDataApparatus(this.getFreezerToEdit());
		this.setMode(2);
	}
	

	public void actionCloseVigencyApparatus(ActionEvent event, Freezer freezer){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("close_vigency_apparatus"));
		//Habilitamos la Vista para Cerrar la Vigencia del Aparato		
		this.setMode(3);
		//Cargamos el Codigo del Freezer
		this.setCodeFreezer(freezer.getCodFreezer());
		//Cargamos el Tipo de Alicuta que Almacena
		String[] ary = freezer.getUsoAlm().split(",");
		this.setTypeAlicToStore(ary);
		//Cargamos el Tipo de Equipo.
		this.setTipEquipo(freezer.getTipAlm());
		
	}
	
	//Cargamos los datos del Equipo en los input.
	public void loadDataApparatus(Freezer freezer){
		//Codigo de Freezer
		this.setCodeFreezer(freezer.getCodFreezer());
		//Aplicamos Inverso del Codigo
		freezer.getTipAlm();
		for (DetParam detParam: SimlabParameterService.getListDetParam(CatalogParam.LIST_EQUIP)) {
			if(simlabStringUtils.areEquals(freezer.getTipAlm(), detParam.getId().getCodItem()))
				{this.setTipEquipo(detParam.getDescItem());
				break;}
		}
		
		//Tipo de Alicuotas
		String[] ary = freezer.getUsoAlm().split(",");
		this.setTypeAlicToStore(ary);
		//Temperatura Minima
		this.setTempMin(freezer.getTempMin());
		//Tempoeratura Maxima.
		this.setTempMax(freezer.getTempMax());
		//Cantidad de Rack.
		this.setCantRackToStore(freezer.getCapAlm());
		//Serie
		this.setSerieEquip(freezer.getEquipSerie());
		//Modelo
		this.setModeloEquipo(freezer.getEquipModelo());
		//Calibrado
		for (DetParam detparam: SimlabParameterService.getListDetParam(CatalogParam.SI_NO)) {
			if(simlabStringUtils.areEquals(detparam.getId().getCodItem(), freezer.getIndCalib()))
				{
				this.setIndCalibrate(detparam.getDescItem());
				break;
				}
		}
		this.setNameResp(freezer.getNameResp());
		this.setNroCol(freezer.getNroColumn());
		this.setNroRow(freezer.getNroFila());
	}
	
	public void actionCancel(ActionEvent event){
		this.fieldToNull();
		this.setMode(0);
	}
	
	//Nulamos los campos del registro
	public void fieldToNull(){
		this.setCodeFreezer(null);
		this.setTipEquipo(simlabStringUtils.EMPTY_STRING);
		this.setTypeAlicToStore(null);
		this.setTempMin(null);
		this.setTempMax(null);
		this.setCantRackToStore(null);
		this.setIndCalibrate(simlabStringUtils.EMPTY_STRING);
		this.setNroCol(null);
		this.setNroRow(null);
		this.setNameResp(simlabStringUtils.EMPTY_STRING);
		this.setSerieEquip(simlabStringUtils.EMPTY_STRING);
		this.setModeloEquipo(simlabStringUtils.EMPTY_STRING);
}
	
}