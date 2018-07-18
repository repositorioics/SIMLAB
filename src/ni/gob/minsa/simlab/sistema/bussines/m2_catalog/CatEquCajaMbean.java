package ni.gob.minsa.simlab.sistema.bussines.m2_catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabEquipService;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.services.SimlabPatternService;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.services.simlabAlicuotaService;
import ni.gob.minsa.simlab.sistema.services.simlabDescriptionService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabNumberUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.AlicCaja;
import ni.gob.minsa.sistema.hibernate.bussines.Caja;
import ni.gob.minsa.sistema.hibernate.bussines.DetParam;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlicId;

import org.hibernate.Session;

@ManagedBean(name="catEquCajaMbean")
@ViewScoped
public class CatEquCajaMbean extends GenericMbean implements Serializable {

	private static final long serialVersionUID = 5181305988167747987L;
		
	private static final int capAlmBox = 81;
	
	private String codeCaja;
	private String usoAlicuota;
	private String tipoMuestra;
	private String uso;
	private String titlePanel;
	private String motCloseVigency;
	private String posNeg;
	private String estudio;
	private String codeRack;
	private Integer mode;
	private Integer posRack;
	private Integer codigoFreezer;
	private Date fechaIni;
	private Date FechaFin;
	private Integer tempAlm;
	private Caja cajaToEdit;
	private Collection<String> tipAlicuota = new ArrayList<String>();
	private Collection<String> typeAlicByStudy =  new ArrayList<String>();
	
	
	
		
	private Integer codeCajaToFind = null;
	
	int coRack = 0;
	int capAlm = 0;
	
	public String getPosView(){
		return SimlabParameterService.getParameterCode(CatalogParam.POS_NEG, 1);
	}
	
	public String getNegView(){
		return SimlabParameterService.getParameterCode(CatalogParam.POS_NEG, 2);
	}
	
	public String getNRView(){
		return SimlabParameterService.getParameterCode(CatalogParam.POS_NEG, 3);
	}
	
	public Collection<String> getTipAlicuota() {
		return tipAlicuota;
	}

	public void setTipAlicuota(Collection<String> tipAlicuota) {
		this.tipAlicuota = tipAlicuota;
	}

	public String getCodeCaja() {
		return codeCaja;
	}

	public void setCodeCaja(String codeCaja) {
		this.codeCaja = codeCaja;
	}

	public String getUsoAlicuota() {
		return usoAlicuota;
	}

	public void setUsoAlicuota(String usoAlicuota) {
		this.usoAlicuota = usoAlicuota;
	}

	public String getTipoMuestra() {
		return tipoMuestra;
	}

	public void setTipoMuestra(String tipoMuestra) {
		this.tipoMuestra = tipoMuestra;
	}

	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}

	public String getTitlePanel() {
		return titlePanel;
	}

	public void setTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}

	public String getMotCloseVigency() {
		return motCloseVigency;
	}

	public void setMotCloseVigency(String motCloseVigency) {
		this.motCloseVigency = motCloseVigency;
	}

	public String getPosNeg() {
		return posNeg;
	}

	public void setPosNeg(String posNeg) {
		this.posNeg = posNeg;
	}

	public String getCodeRack() {
		return codeRack;
	}

	public void setCodeRack(String codeRack) {
		this.codeRack = codeRack;
	}

	public Integer getCodigoFreezer() {
		return codigoFreezer;
	}

	public void setCodigoFreezer(Integer codigoFreezer) {
		this.codigoFreezer = codigoFreezer;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getPosRack() {
		return posRack;
	}

	public void setPosRack(Integer posRack) {
		this.posRack = posRack;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return FechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		FechaFin = fechaFin;
	}

	public Caja getCajaToEdit() {
		return cajaToEdit;
	}

	public void setCajaToEdit(Caja cajaToEdit) {
		this.cajaToEdit = cajaToEdit;
	}

	

	public String getEstudio() {
		return estudio;
	}

	public void setEstudio(String estudio) {
		this.estudio = estudio;
	}

	public CatEquCajaMbean() {
		super();
		/*Modo en vista Inicial*/
		this.setMode(0);
	}
	
	
	
	public Integer getCodeCajaToFind() {
		return codeCajaToFind;
	}

	public void setCodeCajaToFind(Integer codeCajaToFind) {
		this.codeCajaToFind = codeCajaToFind;
	}

	public Collection<Caja> getListCaja(){
		Collection<Caja> lisCajas = new ArrayList<Caja>();
		try {
			if (this.getCodeCajaToFind() == null) {
				lisCajas = SimlabEquipService.getListCajasByCode("");
			}else {
				Integer codigo = this.getCodeCajaToFind();
				lisCajas = SimlabEquipService.getListCajasByCode(codigo.toString());
			}	
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return lisCajas;
	}
	
	public Collection<String> getListTypeAlic(){
		Collection<String>lisTypeAlic = new ArrayList<String>();
		try {
			lisTypeAlic = simlabDescriptionService.getLisTypeAlic("LIST_USO_ALIC");
		} catch (SimlabAppException e){
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return lisTypeAlic;
	}
	
	//Obtenemos la Lista de Usos para la capa de Presentacion
		public Collection<String> getListUse(){
			Collection<String > listUse = new ArrayList<String>();
				try {
					listUse = simlabDescriptionService.getListUseAlicWithEmpty();
				} catch (SimlabAppException e) {
					SimlabAppException.addFacesMessageError(e);
				}
			return listUse;	
		}
		
		public Collection<String> getListStudy(){
			Collection<String > listStudy = new ArrayList<String>();
				try {
					listStudy = simlabDescriptionService.getListStudyCaja();
				} catch (SimlabAppException e) {
					SimlabAppException.addFacesMessageError(e);
				}
			return listStudy;	
		}
		
		//Evento para La solicitud Ajax
		@SuppressWarnings("null")
		public void eventShowTypeAlicByStudy(){
			Collection<String> listAlic = new ArrayList<String>();
			//Obtenemos el codigo Item de la Descripcion.
			String uso = this.getUsoAlicuota();
				for (AlicCaja alicuota : simlabAlicuotaService.getListAlicByBoxByUse(uso)) {
					if(listAlic==null)listAlic.add(simlabStringUtils.EMPTY_STRING);
					//Agregamos a la lista 
					listAlic.add(alicuota.getAlicPerm());
				}
				this.setTypeAlicByStudy(listAlic);this.setTipAlicuota(listAlic);
		}
	
	public Collection<String> getListTypeMuestByAlic(){
		Collection<String>lisTypeMuesByAlic = new ArrayList<String>();		
			for (AlicCaja alicuota: simlabAlicuotaService.getListAlicByBox()) {
				lisTypeMuesByAlic.add(alicuota.getAlicPerm());}
			return lisTypeMuesByAlic;
	}
	
	public Collection<String> getListUsoAlic(){
		Collection<String>listUsoAlic = new ArrayList<String>();
		try {
			listUsoAlic = simlabDescriptionService.getListUsoAlic("LIST_PROPO");
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listUsoAlic;
	}
	public Collection<String> getListPosNeg(){
		Collection<String> listPosNeg = new ArrayList<String>();
		try {
			listPosNeg = simlabDescriptionService.getListPosNeg("POS_NEG");
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return listPosNeg;
	}
	private String getCodeEquipFromType(String codCat, String campo, int opcion){
		String codeTypeEquip = null;
		if (opcion == 1) {
			for (DetParam detParam: SimlabParameterService.getListDetParam(codCat)) {
				if(simlabStringUtils.areEquals(detParam.getDescItem(), campo))
					codeTypeEquip = detParam.getId().getCodItem();
			}
		}else if (opcion == 2) {
			for (DetParam detParam: SimlabParameterService.getListDetParam(codCat)) {
				if(simlabStringUtils.areEquals(detParam.getId().getCodItem(), campo)){
					codeTypeEquip = detParam.getDescItem();
					break;
				}
			}
		}
		
		return codeTypeEquip;
	}
	
	private void loadData(Caja _caja){
		Caja c = SimlabEquipService.getCaja(_caja.getId().getCodCaja(), _caja.getId().getCcodRack());
		this.setCodeCaja(c.getId().getCodCaja());
		this.setCodeRack(c.getId().getCcodRack());	
		this.setPosRack(c.getPosRack());
		this.setUsoAlicuota(getCodeEquipFromType("LIST_USO_ALIC", _caja.getUsoAlic(), 2));
		this.setTipoMuestra(getCodeEquipFromType("LIST_TIP_MUEST", _caja.getTipMues(), 2));
		this.setUso(getCodeEquipFromType("LIST_PROPO", _caja.getUso(), 2));
		this.setTipoMuestra(c.getTipMues());
		this.setPosNeg(getCodeEquipFromType("POS_NEG", _caja.getPosNeg(), 2));
		this.setEstudio(getCodeEquipFromType("LIST_EST_CAJA", _caja.getEstudio(), 2));
		this.setTempAlm(c.getTempAlm());
		
	}
	
	public void actionChangeViewToRegisterNewCaja(ActionEvent event){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("register_new_apparatus"));
		this.setMode(1);
	}
	
	public void actionCancel(ActionEvent event){
		this.fieldToNull();
		this.setMode(0);
	}
	
	public void actionEditInfoCaja(ActionEvent event, Caja caja){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("edit_apparatus"));
		this.setCajaToEdit(caja);
		loadData(this.getCajaToEdit());
		this.setMode(2);
	}
	
	public void actionCloseVigencyCaja(ActionEvent event, Caja caja){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("close_vigency_apparatus"));
		//Habilitamos la Vista para Cerrar la Vigencia del Aparato
		this.setCajaToEdit(caja);
		loadData(this.getCajaToEdit());
		this.setMode(3);		
	}
	
	public void actionRegisterCaja(ActionEvent event){
		Session session = null;
		try {
			//Verificamos si la Variable es de Resguardo
			int tempMax=0;
			boolean isResguard = simlabStringUtils.areEquals(SimlabParameterService.getItemParam(CatalogParam.LIST_USO_CAJA, 2).getId().getCodItem()
					, simlabDescriptionService.getCodeByDescriptionAndEstudy("LIST_PROPO", this.getUso()));
			validateFields(isResguard);
			if (this.getMode()==1||this.getMode()==2){
				if (this.getMode()==1){
					validateEquipCaja(this.getCodeCaja(),this.getUsoAlicuota(),this.getCodeRack(),this.getPosRack(),this.getCodigoFreezer());
					tempMax = SimlabEquipService.getObjectFreezer(this.getCodigoFreezer()).getTempMax();
				}
				if (this.getMode()==2){
					tempMax = this.getTempAlm();
				}
				session = this.openSessionAndBeginTransaction();
				String codUsoAlic = getCodeEquipFromType("LIST_USO_ALIC", this.getUsoAlicuota(), 1);
				//String codTipMues = getCodeEquipFromType("LIST_TIP_MUEST", this.getTipoMuestra(), 1);
				String codUso = getCodeEquipFromType("LIST_PROPO", this.getUso(), 1);
				String codPosNeg = getCodeEquipFromType("POS_NEG", this.getPosNeg(), 1);
				String codEstudio = getCodeEquipFromType("LIST_EST_CAJA", this.getEstudio(), 1);
				
				if (this.getMode()==1){
					SimlabEquipService.addEquipCaja(session, this.getCodeCaja(), this.getCodeRack(), this.getPosRack(), codUsoAlic, this.getTipoMuestra(), 
							SimlabDateUtil.getCurrentDate(), SimlabDateUtil.MAX_DATE,tempMax, codUso, capAlmBox, codPosNeg,codEstudio);
				}
				if (this.getMode()==2){
					SimlabEquipService.updateEquipCaja(session, this.getCodeCaja(), this.getCodeRack(), this.getPosRack(), codUsoAlic, this.getTipoMuestra(), 
						SimlabDateUtil.getCurrentDate(), SimlabDateUtil.MAX_DATE,tempMax, codUso, capAlmBox, codPosNeg,codEstudio);
				}
				this.closeSessionAndCommitTransaction(session);
				this.fieldToNull();
				this.setMode(0);
			}
			if (this.getMode()==3){
				validateCajaVacia(this.getCodeCaja());
				session = this.openSessionAndBeginTransaction();
				SimlabEquipService.deleteCaja(session, this.getCajaToEdit(),this.getUserCode(), this.getMotCloseVigency());
				this.closeSessionAndCommitTransaction(session);
				this.fieldToNull();
				this.setMode(0);
			}
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
	}
	
	private void validateEquipCaja(String codCaja, String tipoAlic, String codRack, int posi, int codFreezer) throws SimlabAppException{
		validateEquipRack(posi,codRack,codFreezer);
		//Validamos que la caja no exista en Registro
		if (this.getMode()==1){
			if(SimlabEquipService.getCaja(codCaja, codRack)!=null)throw new SimlabAppException(10048);
		}
		//Validamos que la posicion no se encuentre fuera del Rango de Posiciones
		if(SimlabEquipService.getRack(codRack).getCapAlm()<this.getPosRack())throw new SimlabAppException(10046);
	}
	
	private void validateCajaVacia(String codCaja) throws SimlabAppException{
		//Validamos que la caja no exista en Registro
		if (this.getMode()==3){
			if(SimlabEquipService.getAlicCaja(codCaja)>0)throw new SimlabAppException(10666);
		}
	}
	
	private void validateEquipRack(int position, String rackCode, int freezerCode) throws SimlabAppException{
		//Validando que el codigo de rack sea de dos digitos
		if (!SimlabPatternService.isRightPattern(rackCode, SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 4)) )throw new SimlabAppException(11003); 
		//Validando que no se repitan los codigos de rack
		Rack r = new Rack(); 
		r = SimlabEquipService.verifCodRack(rackCode,freezerCode);
		if(r == null) throw new SimlabAppException(10049);
		if(SimlabEquipService.isMaxCapcRack(freezerCode, rackCode))throw new SimlabAppException(11011);
		if(this.getMode()==1){
		if(!SimlabEquipService.isPositionAvailableInCaja(position, rackCode))throw new SimlabAppException(11019);
		}
	}
	
	//Hacemos las Validaciones para que los campos Requeridos no se encuentren Vacios
	private void validateFields(boolean isResguard) throws SimlabAppException{
		//Validamos que el Campo Codigo de Freezer no se encuentre nulo
		if (this.getMode()==1){
			if(SimlabNumberUtil.isNull(this.getCodigoFreezer()))throw new SimlabAppException(11000);
		}
		if (this.getMode()==1 || this.getMode()==2){
			//Validamos que el codigo de Caja no se encuentre Vacio.
			if (simlabStringUtils.isNullOrEmpty(this.getCodeCaja()))throw new SimlabAppException(11000);
			//Validamos que el Campo Codigo de Rack no se encuentre nulo
			if(simlabStringUtils.isNullOrEmpty(this.getCodeRack())) throw new SimlabAppException(11000);
			//Validamos que la posicion no sea igual a 0 o no se encuentre nula
			if(SimlabNumberUtil.isNull(this.getPosRack())||SimlabNumberUtil.areEquals(this.getPosRack(), 0))throw new SimlabAppException(11000);
			//Validamos que el Campo Proposito de Uso no se encuentre nulo
			if(simlabStringUtils.isNullOrEmpty(this.getUso()))throw new SimlabAppException(11000);
			//Si la Caja no es Temporal entonces
			if(!isResguard){
				//Validamos que el USO de la caja no se encuentre nulo o Vacio
				if(simlabStringUtils.isNullOrEmpty(this.getUsoAlicuota()))throw new SimlabAppException(11000);
				//Validamos que el USO de la caja no se encuentre nulo o Vacio
				if(simlabStringUtils.isNullOrEmpty(this.getTipoMuestra()))throw new SimlabAppException(11000);
				//Validamos que el Campo Positivo/Negativo no se encuentre Vacio
				if(simlabStringUtils.isNullOrEmpty(this.getPosNeg()))throw new SimlabAppException(11000);
				//Validamos que el Campo Estudio no se encuentre Vacio
				if(simlabStringUtils.isNullOrEmpty(this.getEstudio()))throw new SimlabAppException(11000);
			}
		}
		if (this.getMode()==3){
			if (simlabStringUtils.isNullOrEmpty(this.getMotCloseVigency()))throw new SimlabAppException(11000);
		}
	}
	
	public void setNextCodeCaja(){
		try {
			//Validamos que el Código sea deiferente de 0 o nulo y que el Código de Rack sea diferente de Nulo
			
			if ((!simlabStringUtils.isNullOrEmpty(this.getCodeRack()))) {
				Rack rack = SimlabEquipService.getRack(this.getCodeRack());
				if (rack==null){
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage("Rack no encontrado"));
				}
				else{
					this.setCodigoFreezer(rack.getId().getRcodFreezer());
					this.setCodeCaja(SimlabEquipService.getNextCodeCaja(this.getCodigoFreezer(), this.getCodeRack()));
					this.setPosRack(SimlabEquipService.getPositionInRack(this.getCodeRack()));
				}
			}
			else {
				this.setCodeCaja("");
			}
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
	}
	
	
	private void fieldToNull(){
		this.setCodigoFreezer(null);
		this.setCodeRack(simlabStringUtils.EMPTY_STRING);
		this.setCodeCaja(null);
		this.setPosRack(null);
		this.setUsoAlicuota(simlabStringUtils.EMPTY_STRING);
		this.setTipoMuestra(simlabStringUtils.EMPTY_STRING);
		this.setUso(simlabStringUtils.EMPTY_STRING);
		this.setPosNeg(simlabStringUtils.EMPTY_STRING);
		this.setEstudio(simlabStringUtils.EMPTY_STRING);
		this.setTempAlm(null);
		this.setTipoMuestra(simlabStringUtils.EMPTY_STRING);
		this.setMotCloseVigency(null);
	}
	
	/** HERROLD* * */
	//Todas estas funciones tienen que dar como resultado el seteo automatico de
	//Codigo de Caja, Codigo de Rack y Posicion de la caja en el Rack
	

	public String getLastCajaId(){
		Session session = null;
		Caja caja = null;
		String result = simlabStringUtils.EMPTY_STRING;
		try{
			session = HibernateUtil.openSesion();
			String query = "select * from caja where cod_caja = (select distinct max(cod_caja) from caja)";
			caja = (Caja)session.createSQLQuery(query).addEntity(Caja.class).uniqueResult();
			if(caja != null)
			{
				int temp = Integer.parseInt(caja.getId().getCodCaja()) + 1;
				result = Integer.toString(temp);
			}
		}catch(Exception e){
			e.getCause();
			e.printStackTrace();
		}
		return
				result;
	}
	
	public void setCodCaja() throws SimlabAppException{
		String cC = getLastCajaId();
		this.setCodeCaja(cC);
	}
	
	public void setPosRack() throws SimlabAppException{
		int posRack = Integer.parseInt(SimlabEquipService.setPosRack());
		if(posRack != 0)
			this.setPosRack(posRack);
	}
	
	public void setCodRack() throws SimlabAppException{
		if(this.getCodigoFreezer() != null && this.getCodigoFreezer() != 0)
		{
			SimlabEquipService.getFreezerbyID(this.getCodigoFreezer().toString());
			String cR = SimlabEquipService.setCodigoRack();
			this.setCodeRack(cR);				
		}
	}

	public Integer getTempAlm() {
		return tempAlm;
	}

	public void setTempAlm(Integer tempAlm) {
		this.tempAlm = tempAlm;
	}

	public Collection<String> getTypeAlicByStudy() {
		return typeAlicByStudy;
	}

	public void setTypeAlicByStudy(Collection<String> typeAlicByStudy) {
		this.typeAlicByStudy = typeAlicByStudy;
	}
	
	
	public Collection<RegAlic> getListAlicByCode2(){
		
		Collection<RegAlic> listAlicByCode2 = new ArrayList<RegAlic>();
		try {
			if (this.codeCajaToFind!=null){
				for (int i=1;i<82;i++){
					RegAlic temp = SimlabEquipService.getAlicBoxPos(this.codeCajaToFind,i);
					if(temp!=null){
						listAlicByCode2.add(temp);
					}
					else{
						temp = new RegAlic();
						RegAlicId idA = new RegAlicId();
						idA.setCodAlic("---");
						idA.setPosBox(i);
						temp.setId(idA);
						temp.setPesoAlic(0f);
						listAlicByCode2.add(temp);
					}
				}
			}
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listAlicByCode2;
	}
}