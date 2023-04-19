package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.inject.New;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.exception.ConstraintViolationException;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabEquipService;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService;
import ni.gob.minsa.simlab.sistema.services.SimlabPatternService;
import ni.gob.minsa.simlab.sistema.services.simlabAlicuotaService;
import ni.gob.minsa.simlab.sistema.services.simlabDescriptionService;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.AlicCaja;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlicId;

@ManagedBean(name="muestraIngAlicNuevo")
@ViewScoped
public class MuestraIngAlicNuevo extends GenericMbean implements Serializable {

	private static final long serialVersionUID = 5181305988167747987L;
	
	
	private String study = null;
	
	private String indPosNeg = null;
	private String typeAlicSelected = null;
	private Collection<String> typeAlicByStudy =  new ArrayList<String>();
	private Integer mode;
	
	//Campos de la alicuota
	private String codeAlic = null;
	private Integer posicion = null;
	private Integer codeFreezer = null;
	private String codeRack = null;
	private Integer codeCaja = null;
	private String codeRes = null;
	private Float volAlic = null;
	private String tipo = "estudios";
	private String condicion = null;
	private String separada = null;
	private String uso = null;
	

	//Auxiliar
	private Integer nextPosition = null;
	
	
	public Integer getCodeCaja() {
		return codeCaja;
	}

	public void setCodeCaja(Integer codeCaja) {
		this.codeCaja = codeCaja;
	}

	

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public MuestraIngAlicNuevo() {
		super();
		this.setMode(0);
	}
	
	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}
	
	public String getIndPosNeg() {
		return indPosNeg;
	}

	public void setIndPosNeg(String indPosNeg) {
		this.indPosNeg = indPosNeg;
	}

	public String getTypeAlicSelected() {
		return typeAlicSelected;
	}

	public void setTypeAlicSelected(String typeAlicSelected) {
		this.typeAlicSelected = typeAlicSelected;
	}
	
	public Collection<String> getTypeAlicByStudy() {
		return typeAlicByStudy;
	}

	public void setTypeAlicByStudy(Collection<String> typeAlicByStudy) {
		this.typeAlicByStudy = typeAlicByStudy;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
	public String getCodeAlic() {
		return codeAlic;
	}

	public void setCodeAlic(String codeAlic) {
		this.codeAlic = codeAlic;
	}

	public Integer getCodeFreezer() {
		return codeFreezer;
	}

	public void setCodeFreezer(Integer codeFreezer) {
		this.codeFreezer = codeFreezer;
	}

	public String getCodeRack() {
		return codeRack;
	}

	public void setCodeRack(String codeRack) {
		this.codeRack = codeRack;
	}

	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	public String getCodeRes() {
		return codeRes;
	}

	public void setCodeRes(String codeRes) {
		this.codeRes = codeRes;
	}

	public Float getVolAlic() {
		return volAlic;
	}

	public void setVolAlic(Float volAlic) {
		this.volAlic = volAlic;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public String getSeparada() {
		return separada;
	}

	public void setSeparada(String separada) {
		this.separada = separada;
	}

	
	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}
	
	

	public Integer getNextPosition() {
		return nextPosition;
	}

	public void setNextPosition(Integer nextPosition) {
		this.nextPosition = nextPosition;
	}

	//Obtenemos la lista de Positivos o Negativos
	public Collection<String> getListPosOrNeg(){
		Collection<String> listPosOrNeg = new ArrayList<String>();
			try {
				listPosOrNeg = simlabDescriptionService.getListPositiveOrNegativeEmpty();
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
			}
		return listPosOrNeg;
	}
	
	//Obtenemos la lista de Estudio para la vista.
	public Collection<String> getListStudy(){
		Collection<String> listStudy = null;
		try {
			listStudy = simlabDescriptionService.getListEstudy();
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listStudy;
	}
	
	//Evento para La solicitud Ajax
	@SuppressWarnings("null")
	public void eventShowTypeAlicByStudy(){
		Collection<String> listAlic = new ArrayList<String>();
		try {
			//Obtenemos el codigo Item de la Descripcion.
			String itemAlicuotaFromDetParam 
			= simlabDescriptionService.getCodeByDescriptionAndEstudy("LIST_EST", this.getStudy());
				for (AlicCaja alicuota : simlabAlicuotaService.getListAlicPermittedByBox(itemAlicuotaFromDetParam)) {
					if(listAlic==null)listAlic.add(simlabStringUtils.EMPTY_STRING);
					//Agregamos a la lista 
					listAlic.add(alicuota.getAlicPerm());
				}
				this.setTypeAlicByStudy(listAlic);
				if(this.getStudy().matches("Muestreo Anual")||this.getStudy().matches("Muestreo Anual 2016")||this.getStudy().matches("Muestreo Anual 2017")||this.getStudy().matches("Muestreo Anual 2018")||this.getStudy().matches("Muestreo Anual 2019") ||this.getStudy().matches("Muestreo Anual 2020") ||this.getStudy().matches("Muestreo Anual 2021") ||this.getStudy().matches("Muestreo Anual 2022") ||this.getStudy().matches("Muestreo Anual 2023")) {
					this.tipo="muestreoanual";
				}
				else if(this.getStudy().matches("Cohorte Familia MA2017")) {
					this.tipo="muestreoanualchf";
				}
				else {
					this.tipo="estudios";
				}
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
	}
	

	
	public Collection<Object[]> getListBoxesSuggested() throws SimlabAppException{
		
		Collection<Object[]> listBoxesSuggested = new ArrayList<Object[]>();
		if (simlabStringUtils.isNullOrEmpty(this.getIndPosNeg())||simlabStringUtils.isNullOrEmpty(this.getStudy())||simlabStringUtils.isNullOrEmpty(this.getTypeAlicSelected())) {
			listBoxesSuggested = null;
		}
		else {
			//Obtenemos el Codigo positivo o no Positivo. segun lo que ingreso el Usuario.
			String posOrNeg = simlabDescriptionService.getCodeByDescriptionAndEstudy("POS_NEG", this.getIndPosNeg());
			listBoxesSuggested = simlabAlicuotaService.getCajasSugeridas(posOrNeg,typeAlicSelected, tipo);
			if(listBoxesSuggested.isEmpty()) {
				
			}
		}
		return listBoxesSuggested;
	}
	
	public Collection<RegAlic> getListAlicByCode2(){
		
		Collection<RegAlic> listAlicByCode2 = new ArrayList<RegAlic>();
		try {
			if (this.codeCaja!=null){
				for (int i=1;i<82;i++){
					RegAlic temp = SimlabEquipService.getAlicBoxPos(this.codeCaja,i);
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
	
	public void actionSelectBox(ActionEvent event, Object[] caja){
		codeCaja = (Integer) caja[0];
		codeFreezer = (Integer) caja[1];
		codeRack = (String) caja[3];
		codeRes = (String) caja[8];
		uso = (String) caja[6];

		String tipoAl = (String) caja[7];

		if (tipoAl.equals("17PX,17PX1,17PX2")) {
			volAlic = 1500F;
		}

		if (tipoAl.equals("17U")) {
			volAlic = 400F;
		}


		for (int i=1;i<82;i++){
			RegAlic temp = null;
			try {
				temp = SimlabEquipService.getAlicBoxPos(this.codeCaja,i);
			} catch (SimlabAppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(temp==null){
				posicion = i;
				break;
			}
		}
		this.setMode(1);
	}
	
	
	//Accion para Agregar alicuotas a la lista..
	public void actionRegisterAlic(ActionEvent event) throws SimlabAppException {
		if(!validarAlicuota()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Codigo de alicuota no es valido para "+ this.getTypeAlicSelected());
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, message);
		}
		else {
			try {
				RegAlicId regAlicId = new RegAlicId();
				regAlicId.setCodAlic(this.codeAlic);
				regAlicId.setPosBox(this.posicion);
				RegAlic regAlic = new RegAlic();
				regAlic.setId(regAlicId);
				regAlic.setCodBox(this.codeCaja);
				regAlic.setCodFreezer(this.codeFreezer);
				regAlic.setCodRack(this.codeRack);
				regAlic.setCodUser( this.getUserCode());
				regAlic.setFehorReg(SimlabDateUtil.getCurrentTimestamp());
				regAlic.setNegPos(this.codeRes);	
				regAlic.setVolAlic(this.volAlic);
				regAlic.setPesoAlic(null);
				regAlic.setTipo(this.tipo);
				regAlic.setCondicion(simlabDescriptionService.getCodeByDescriptionAndEstudy("LIST_CONDICION", this.getCondicion()));
				regAlic.setSeparada(simlabDescriptionService.getCodeByDescriptionAndEstudy("LIST_LAB", this.getSeparada()));
				simlabAlicuotaService.saveAlicuota(regAlic);

				//limpiar solo si el tipo de alicuota es dif de paxgene o influenza
				if (regAlicId.getCodAlic().toUpperCase().contains("U") || regAlicId.getCodAlic().toUpperCase().contains("PX")) {
					this.setCodeAlic( null );
				} else {
					this.fieldToNull();
				}

				for (int i=1;i<82;i++){
					RegAlic temp = SimlabEquipService.getAlicBoxPos(this.codeCaja,i);
					if(temp==null){
						posicion = i;
						break;
					}
				}
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito","Registro guardado");
	            FacesContext context = FacesContext.getCurrentInstance();
	            context.addMessage(null, message);
			}
			catch(ConstraintViolationException e){
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Error al guardar en base de datos, El codigo esta duplicado");
	            FacesContext context = FacesContext.getCurrentInstance();
	            context.addMessage(null, message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getLocalizedMessage());
	            FacesContext context = FacesContext.getCurrentInstance();
	            context.addMessage(null,message);
				e.printStackTrace();
			}
		}
	}

	
	//Nulamos todos los Campos
	public void fieldToNull(){
		this.setCodeAlic(null);
		this.setVolAlic(null);
	}
	
	
	//Obtenemos la lista de condiciones
	public Collection<String> getListCondicion(){
		Collection<String> listCondicion = new ArrayList<String>();
			try {
				listCondicion = simlabDescriptionService.getListCondicion();
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
			}
		return listCondicion;
	}
	
	//Obtenemos la lista de lab
	public Collection<String> getListLab(){
		Collection<String> listLab = new ArrayList<String>();
			try {
				listLab = simlabDescriptionService.getListLab();
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(e);
			}
		return listLab;
	}

	
	private boolean validarAlicuota() throws SimlabAppException{
		//Separamos en un arreglo de String los elementos de las alicuotas seleccionadas.
		String[] itemTypeAlicSelected = this.getTypeAlicSelected().split(","); 
		String getSufixAlic="";
		if (this.getStudy().matches("Estudio Hospitalario")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 1))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Longitudinal")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 5))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Cohorte")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 6))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Cohorte Muestras C")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 7))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")-2, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Transmision Chikungunya")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 8))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Muestreo Anual")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 9))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			if(this.getCodeAlic().substring(this.getCodeAlic().length()-2, this.getCodeAlic().length()).equalsIgnoreCase("t2")||this.getCodeAlic().substring(this.getCodeAlic().length()-2, this.getCodeAlic().length()).equalsIgnoreCase("px")) {
				getSufixAlic = this.getCodeAlic().substring(this.getCodeAlic().length()-2, this.getCodeAlic().length());
			}
			else {
				getSufixAlic = this.getCodeAlic().substring(this.getCodeAlic().length()-1, this.getCodeAlic().length());
			}
		}
		else if(this.getStudy().matches("Cluster Zika")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 10))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Zika ZIP")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 11))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf("-")+2, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Zika Positivas")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 12))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf("-")+2, this.getCodeAlic().length());
			getSufixAlic = "ZP"+getSufixAlic;
		}
		else if(this.getStudy().matches("Estudio Cohorte Flu")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 13))) {
				return false;
			}

			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			String[] fluCode = this.getCodeAlic().split("\\.");
			if (fluCode.length == 2) {
				getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".") + 3, this.getCodeAlic().length());

			} else {
				getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".") + 1, this.getCodeAlic().length());

			}

		}
		else if(this.getStudy().matches("Muestreo Anual 2017")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 14))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Cohorte Familia MA2017")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 15))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
			getSufixAlic = "CF"+getSufixAlic;
		}
		else if(this.getStudy().matches("Cohorte Familia Transmision")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 16))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			if(this.getCodeAlic().substring(this.getCodeAlic().length()-1, this.getCodeAlic().length()).equals("N")) {
				getSufixAlic = "TXN";
			}
			else {
				getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+4, this.getCodeAlic().length());
				if(getSufixAlic.equals("PX1")||getSufixAlic.equals("PX2")||getSufixAlic.equals("N")) getSufixAlic = "TX"+getSufixAlic;
			}
		}
		else if(this.getStudy().matches("Estudio ZPO")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 17))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf("-")+1, this.getCodeAlic().length());
			String tipoM = this.getCodeAlic().substring(7, 8);
			if (tipoM.equals("0")){
				tipoM = "M";
			}else {
				tipoM = "N";
			}
			getSufixAlic = "ZPO"+tipoM+getSufixAlic;
		}
		else if(this.getStudy().matches("Muestreo Anual 2016")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 18))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Muestreo Anual 2018")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 19))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Muestreo Anual 2019")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 20))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Muestras Superficie")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 21))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+2, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Influenza U01")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 22))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio CNS")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 23))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+3, this.getCodeAlic().length());

		}

		else if(this.getStudy().matches("Muestreo Anual 2020")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 24))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Muestreo Covid 2020")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 25))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Muestreo Anual 2021")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 26))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio Clinico Hospitalario \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 27))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio de Cohorte Dengue Hospitalizado \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 28))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio Longitudinal \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 29))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Cohorte Dengue Centro de Salud \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 30))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio Longitudinal 3M \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 31))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}
		else if(this.getStudy().matches("Estudio Longitudinal 6M \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 32))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio Longitudinal 12M \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 33))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio Longitudinal 18M \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 34))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Plasma Estudio de transmision \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 35))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Respiratoria Estudio de transmisión \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 36))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio Cluster Degue-Chik \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 37))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Muestreo Anual Dengue-Influenza \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 38))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio ZIP \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 39))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf("-")+8, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio ZIKA \\+ \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 40))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf("-")+8, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio UO1 \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 41))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio CEIRS \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 42))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Seroprevalencia SARS COV2 \\(PBMC\\)")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 43))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Muestreo Covid 2021")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 44))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Estudio A2CARES")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 45))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario

			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".") + 1, this.getCodeAlic().length());
		}

        else if(this.getStudy().matches("Estudio MINSA")){
            //Validamos el patron
            if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 46))) return false;
            //Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario

            getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".") -1, this.getCodeAlic().length());
        }

		else if(this.getStudy().matches("Muestreo Anual 2022")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 47))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Muestreo Anual 2023")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 48))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		}

		else if(this.getStudy().matches("Muestreo A2CARES 2023")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 49))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".") + 1, this.getCodeAlic().length());
		}

		//Validamos si el Arreglo contiene elementos
		if(itemTypeAlicSelected.length>0){
			for (String itemAlic : itemTypeAlicSelected) {
				//Verificamos si el Tipo Seleccionado contiene numeros para identificar el tipo de ALicuota
				//if(SimlabNumberUtil.isNumber(simlabStringUtils.cutToLenght(itemAlic.trim(), 0, 0+1))){
					//	Validamos que la cadena si el Elemento es igual a lo digitado por el usuario.
					if(itemAlic.matches("(?i)" +  getSufixAlic)){
						if (getSufixAlic.matches("[1-3]{1}[l|L]{1}[x|X]{1}[1-2]{1}")){
							this.setVolAlic(1500F);
						}
						if (getSufixAlic.matches("AV1")){
							this.setVolAlic(500F);
						}
						if (getSufixAlic.matches("[PC1|PC2]{3}")){
							this.setVolAlic(140F);
						} 
						return true;
					}
			}
		}
		return false;
	}
	
}