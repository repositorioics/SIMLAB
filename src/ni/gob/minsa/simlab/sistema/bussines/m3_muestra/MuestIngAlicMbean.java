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
import ni.gob.minsa.simlab.sistema.services.SimlabEquipService;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.services.SimlabPatternService;
import ni.gob.minsa.simlab.sistema.services.simlabAlicuotaService;
import ni.gob.minsa.simlab.sistema.services.simlabDescriptionService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabNumberUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.AlicCaja;
import ni.gob.minsa.sistema.hibernate.bussines.Caja;
import ni.gob.minsa.sistema.hibernate.bussines.CatAlicuotas;
import ni.gob.minsa.sistema.hibernate.bussines.DetParam;
import ni.gob.minsa.sistema.hibernate.bussines.Freezer;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlicId;
import ni.gob.minsa.sistema.hibernate.bussines.ResClinico;
import ni.gob.minsa.sistema.hibernate.bussines.ResCohorte;

import org.hibernate.Session;
import org.primefaces.context.RequestContext;

@ManagedBean(name="muestIngAlicMbean")
@ViewScoped

public class MuestIngAlicMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 1442923938364742051L;
	
	private String codeAlic = null;
	
	private String codeRack = null;
	
	private Integer codeBox = null;
	private Integer codeBoxInUse = null;
	private String alicBoxInUse = null;
	
	private Integer codeFreezer = null;
	
	private Integer mode = null;
	
	private String study = null;
	
	private String typeAlicSelected = null;
	
	private Float volAlic = null;
	
	private Float pesoAlic=null;
	
	private String tipo = "estudios";
	
	private Integer positionInBox = null;
	
	private Caja box = null;
	
	private CatAlicuotas dataAlicToStore = null;
	
	private String indPosNeg = null;
	
	private Caja boxTosuggest = null;
	
	private Caja NegativeBox = null;
	
	private Caja tempBox = null;
	
	private Freezer freezerToSuggest = null;
	
	private Rack rackToSuggest = null;
	
	private boolean indDescarte = false;
	
	private Collection<RegAlic> listAlicRegRec = new ArrayList<RegAlic>();
	private Collection<RegAlic> listAlicRegBox = new ArrayList<RegAlic>();
	
	private static final int capAlmBox = 81;
	
	private String codeCaja;
		
	private String usoAlicuota;
	
	private String tipoMuestra;
	
	private String uso;
	
	private String posNeg;
	
	private String codRack;
	
	private Collection<String> tipAlicuota;
	
	private Integer capAlmac;
	
	private Integer posRack;
	
	private Integer codigoFreezer;
	
	private Integer code;
	
	private boolean isNotWrong = false;
	
	private String typeAlicFromCodeAlic = null;
	
	public String getLabelPosNeg() {
		return SimlabParameterService.getItemParam(CatalogParam.POS_NEG, 1).getId().getCodItem();
	}

	public void setLabelPosNeg(String labelPosNeg) {
		SimlabParameterService.getItemParam(CatalogParam.POS_NEG, 1).getId().getCodItem();
	}

	public String getTypeAlicFromCodeAlic() {
		return typeAlicFromCodeAlic;
	}

	public void setTypeAlicFromCodeAlic(String typeAlicFromCodeAlic) {
		this.typeAlicFromCodeAlic = typeAlicFromCodeAlic;
	}

	public boolean isNotWrong() {
		return isNotWrong;
	}

	public void setNotWrong(boolean isNotWrong) {
		this.isNotWrong = isNotWrong;
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

	public String getPosNeg() {
		return posNeg;
	}

	public void setPosNeg(String posNeg) {
		this.posNeg = posNeg;
	}

	public String getCodRack() {
		return codRack;
	}

	public void setCodRack(String codRack) {
		this.codRack = codRack;
	}
	
	public Integer getCapAlmac() {
		return capAlmac;
	}

	public void setCapAlmac(Integer capAlmac) {
		this.capAlmac = capAlmac;
	}

	public Integer getPosRack() {
		return posRack;
	}

	public void setPosRack(Integer posRack) {
		this.posRack = posRack;
	}

	public Integer getCodigoFreezer() {
		return codigoFreezer;
	}

	public void setCodigoFreezer(Integer codigoFreezer) {
		this.codigoFreezer = codigoFreezer;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
	public Collection<String> getTipAlicuota() {
		return tipAlicuota;
	}

	public void setTipAlicuota(Collection<String> tipAlicuota) {
		this.tipAlicuota = tipAlicuota;
	}
	
	public Collection<RegAlic> getListAlicRegRec() {
		if(listAlicRegRec==null){
			RegAlic regAlic = new RegAlic();
			listAlicRegRec.add(regAlic);
		}
		return listAlicRegRec;
	}

	public void setListAlicRegRec(Collection<RegAlic> listAlicRegRec) {
		this.listAlicRegRec = listAlicRegRec;
	}

	public boolean isIndDescarte() {
		return indDescarte;
	}

	public void setIndDescarte(boolean indDescarte) {
		this.indDescarte = indDescarte;
	}

	public Freezer getFreezerToSuggest() {
		if(freezerToSuggest==null){
			freezerToSuggest = new Freezer();
		}

		return freezerToSuggest;
	}

	public void setFreezerToSuggest(Freezer freezerToSuggest) {
		this.freezerToSuggest = freezerToSuggest;
	}

	public Rack getRackToSuggest() {
		return rackToSuggest;
	}

	public void setRackToSuggest(Rack rackToSuggest) {
		this.rackToSuggest = rackToSuggest;
	}

	public Float getVolAlic() {
		return volAlic;
	}

	public void setVolAlic(Float volAlic) {
		this.volAlic = volAlic;
	}
			
	public Caja getBoxTosuggest() {
		if(boxTosuggest==null){
			boxTosuggest = new Caja();
		}
		return boxTosuggest;
	}

	public void setBoxTosuggest(Caja boxTosuggest) {
		this.boxTosuggest = boxTosuggest;
	}

	public Caja getNegativeBox() {
		return NegativeBox;
	}

	public void setNegativeBox(Caja negativeBox) {
		NegativeBox = negativeBox;
	}

	public Caja getTempBox() {
		return tempBox;
	}

	public void setTempBox(Caja tempBox) {
		this.tempBox = tempBox;
	}
	
	private Collection<String> typeAlicByStudy =  new ArrayList<String>();

	public String getIndPosNeg() {
		return indPosNeg;
	}

	public void setIndPosNeg(String indPosNeg) {
		this.indPosNeg = indPosNeg;
	}

	public Caja getBox() {
		return box;
	}

	public void setBox(Caja box) {
		this.box = box;
	}

	public Integer getPositionInBox() {
		return positionInBox;
	}

	public void setPositionInBox(Integer positionInBox) {
		this.positionInBox = positionInBox;
	}

	public CatAlicuotas getDataAlicToStore() {
		return dataAlicToStore;
	}

	public void setDataAlicToStore(CatAlicuotas dataAlicToStore) {
		this.dataAlicToStore = dataAlicToStore;
	}

	public Collection<String> getTypeAlicByStudy() {
		return typeAlicByStudy;
	}

	public void setTypeAlicByStudy(Collection<String> typeAlicByStudy) {
		this.typeAlicByStudy = typeAlicByStudy;
	}

	public String getTypeAlicSelected() {
		return typeAlicSelected;
	}

	public void setTypeAlicSelected(String typeAlicSelected) {
		this.typeAlicSelected = typeAlicSelected;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
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

	public String getCodeRack() {
		return codeRack;
	}

	public void setCodeRack(String codeRack) {
		this.codeRack = codeRack;
	}

	public Integer getCodeBox() {
		return codeBox;
	}

	public void setCodeBox(Integer codeBox) {
		this.codeBox = codeBox;
	}

	public Integer getCodeFreezer() {
		return codeFreezer;
	}

	public void setCodeFreezer(Integer codeFreezer) {
		this.codeFreezer = codeFreezer;
	}

	public MuestIngAlicMbean(){
		super();
		//Seteamos la Vista al Primer Formulario
		this.setMode(0);
	}
	
	//Accion Sugerencia de Ubicacion
	public void actionSuggestLocation(ActionEvent event){
		try {
			//LLenamos el campo pos neg
			this.actionValidateTypePosOrNot();
			//Validamos que el Campo Pos o Neg se encuentre lleno
			if(simlabStringUtils.isNullOrEmpty(this.getIndPosNeg()))throw new SimlabAppException(11000);
			this.setNotWrong(false);
			//Determinamos si la alicuota esta equivocada.
			this.detTypeAlic();
			//Localizamos la Ubicacion mas conveniente.
			this.suggestLocation();
			} catch (SimlabAppException e) {
				SimlabAppException.addFacesMessageError(11000);
			}
	}
	
	public Collection<RegAlic> getListAlicByCode(){
		
		Collection<RegAlic> listAlicByCode = new ArrayList<RegAlic>();
		try {
			if (this.codeBoxInUse!=null){
				for (int i=1;i<82;i++){
					RegAlic temp = SimlabEquipService.getAlicBoxPos(this.codeBoxInUse,i);
					if(temp!=null){
						listAlicByCode.add(temp);
					}
					else{
						temp = new RegAlic();
						RegAlicId idA = new RegAlicId();
						idA.setCodAlic("---");
						idA.setPosBox(i);
						temp.setId(idA);
						temp.setPesoAlic(0f);
						listAlicByCode.add(temp);
					}
				}
			}
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listAlicByCode;
	}
	
	//Validamos si la alicuota No se encuentra Equivocada.
		private void detTypeAlic() throws SimlabAppException{
			//Separamos en un arreglo de String los elementos de las alicuotas seleccionadas.
			String[] itemTypeAlicSelected = this.getTypeAlicSelected().split(","); 
			String getSufixAlic="";
			if (this.getStudy().matches("Estudio Hospitalario")|| this.getStudy().matches("Estudio Longitudinal")){
				//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
				getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
				//Obtenemos solamente parte del Sufijo
				//getCharAli = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+2, this.getCodeAlic().length());
			}
			else if(this.getStudy().matches("Estudio Cohorte")){
				//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
				getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
			}
			else if(this.getStudy().matches("Estudio Cohorte Muestras C")){
				//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
				getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")-2, this.getCodeAlic().length());
			}
			else if(this.getStudy().matches("Estudio Transmision Chikungunya")){
				//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
				getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().lastIndexOf(".")+1, this.getCodeAlic().length());
			}
			else if(this.getStudy().matches("Muestreo Anual")){
				//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
				getSufixAlic = this.getCodeAlic().substring(this.getCodeAlic().length()-1, this.getCodeAlic().length());
			}
			else if(this.getStudy().matches("Muestreo Covid 2020")){
				//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
				getSufixAlic = this.getCodeAlic().substring(this.getCodeAlic().length()-1, this.getCodeAlic().length());
			}
			//Validamos si el Arreglo contiene elementos
			if(itemTypeAlicSelected.length>0){
				for (String itemAlic : itemTypeAlicSelected) {
					//Verificamos si el Tipo Seleccionado contiene numeros para identificar el tipo de ALicuota
					//if(SimlabNumberUtil.isNumber(simlabStringUtils.cutToLenght(itemAlic.trim(), 0, 0+1))){
						//	Validamos que la cadena si el Elemento es igual a lo digitado por el usuario.
						if(simlabStringUtils.areEquals(itemAlic.trim(), getSufixAlic))
							{
							this.setNotWrong(true);
							this.setTypeAlicFromCodeAlic(getSufixAlic);
							break;}
					//	}
					//De lo Contrario
					else{
						//if(simlabStringUtils.areEquals(itemAlic, getCharAli)){
							this.setNotWrong(false);
							this.setTypeAlicFromCodeAlic(getSufixAlic);
							break;
						//}
					}
				}
			}
			
		//Obtenemos el Objeto Alicuota y seteamos el Objeto 
			this.setDataAlicToStore(simlabAlicuotaService.getObjectAlicuota(this.getTypeAlicFromCodeAlic()));
		}
	
	//Validamos el Tipo de la Muestra si Es Positivo o No Positivo
	public void actionValidateTypePosOrNot() throws SimlabAppException{
		
		int indiceCodigo =  this.getCodeAlic().indexOf('.');
		boolean patternAlicIsRight = false;
		try {
			if (this.getStudy().matches("Estudio Hospitalario")){
				patternAlicIsRight = SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 1));
			}
			if (this.getStudy().matches("Estudio Longitudinal")){
				patternAlicIsRight = SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 5));
			}
			if (this.getStudy().matches("Estudio Cohorte")){
				patternAlicIsRight = SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 6));
			}
			if (this.getStudy().matches("Estudio Cohorte Muestras C")){
				patternAlicIsRight = SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 7));
			}
			if (this.getStudy().matches("Estudio Transmision Chikungunya")){
				patternAlicIsRight = SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 8));
			}
			if (this.getStudy().matches("Muestreo Anual")){
				patternAlicIsRight = SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 9));
			}
			if (this.getStudy().matches("Muestreo Covid 2020")){
				patternAlicIsRight = SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 25));
			}
			} catch (SimlabAppException e) {
				e.printStackTrace();
			}
		if(indiceCodigo>0 && patternAlicIsRight){
			if (this.getStudy().matches("Estudio Hospitalario") || this.getStudy().matches("Estudio Longitudinal") ) {
				//Obtenemos el Codigo de la Muestra
				String alicuota = simlabStringUtils.cutToLength(this.getCodeAlic(), indiceCodigo);
				//Validamos si el dato ingresado es un numero	
				if(SimlabNumberUtil.isNumber(alicuota)){
					ResClinico objectMuestra =simlabAlicuotaService.getObjectResultMuestras(Integer.valueOf(alicuota)); 
						if(objectMuestra!=null)
						{	
							String paramPos = SimlabParameterService.getItemParam(CatalogParam.POS_NEG, 1).getDescItem();
							String paramNotPos = SimlabParameterService.getItemParam(CatalogParam.POS_NEG, 2).getDescItem();
							//Verificamos el Tipo de la Muestra.
							if(Integer.valueOf(objectMuestra.getResultadoFinal())==1)
								this.setIndPosNeg(paramPos); 
							else
								this.setIndPosNeg(paramNotPos);
						}
						else{
							String paramNR = SimlabParameterService.getItemParam(CatalogParam.POS_NEG, 3).getDescItem();
							this.setIndPosNeg(paramNR);
						}
					}
			} else if(this.getStudy().matches("Estudio Cohorte")) {
								
				int indiceCodigo2 = this.getCodeAlic().lastIndexOf(".");
									
				//Obtenemos el Codigo de la Muestra
				String alicuota = simlabStringUtils.cutToLength(this.getCodeAlic(), indiceCodigo2);				
				//Validamos si el dato ingresado es un numero	
					ResCohorte objectMuestra =simlabAlicuotaService.getObjectResultMuestrasCohorte(alicuota); 
						if(objectMuestra!=null)
						{	
							String paramPos = SimlabParameterService.getItemParam(CatalogParam.POS_NEG, 1).getDescItem();
							String paramNotPos = SimlabParameterService.getItemParam(CatalogParam.POS_NEG, 2).getDescItem();
							
							//Verificamos el Tipo de la Muestra.
							if(Integer.valueOf(objectMuestra.getResultadof())==1)
								this.setIndPosNeg(paramPos); 
							else
								this.setIndPosNeg(paramNotPos);
						}
						else{
							String paramNR = SimlabParameterService.getItemParam(CatalogParam.POS_NEG, 3).getDescItem();
							this.setIndPosNeg(paramNR);
						}
					}
			else {
				String paramNR = SimlabParameterService.getItemParam(CatalogParam.POS_NEG, 3).getDescItem();
				this.setIndPosNeg(paramNR);
			}
			
		}
		else {
			this.setIndPosNeg(null);
		}
	}
	
	//Accion que escucha las acciones sobre el menu de Volumen
	public void actionSetToNullIfVolIsZero(){
		//Validamos si el Vol. de Alicuota es Numero
		if(SimlabNumberUtil.isNumber(this.getVolAlic().toString())){
			//Validamos si el Vol. de la Alicuota es Cero.
			if(Integer.valueOf(this.getVolAlic().toString())==0){
				this.setCodeFreezer(null);
				this.setCodeRack(null);
				this.setCodeBox(null);
				this.setPositionInBox(null);
				this.setIndDescarte(true);
			}				
		}
		else{
			this.setIndDescarte(false);
		}
	}
	
	
	//Obtenemos la lista de Rack a Sugerir
	private Collection<Rack> listRackToSuggest(int codeFreezer) throws SimlabAppException{
		Collection<Rack> listRack = new ArrayList<Rack>();
			listRack =  SimlabEquipService.getListRackWithBoxByFreezer(codeFreezer);
		return listRack;
	}
	//Obtenemos la lista de Cajas
	private Collection<Caja> listBoxToSuggest(String rack, int freezer,String posNotPos, String uso, String tempOrOficial){
		Collection<Caja> list = new ArrayList<Caja>();
			list = SimlabEquipService.getListTypeCajaByRackAndFreezer(freezer, rack, posNotPos, uso, tempOrOficial);
		return list;
	}
	
 	//Logica para Sugerir la Ubicacion
	public void suggestLocation() throws SimlabAppException{
		
		String use = null;
		Integer temp  = null;
		//Numero de Freezer Recorrido.
				int nroFreezer = 0;
				//Indicador de Sugerencia Encontrada.
				int indicateFound = 0;
				//Nro. de Freezer Recorridos
				int nroFreezerRecorridos = 0;
				//Nro. de Rack Recorridos.
				int nroRackRecorridos = 0;

		//Obtenemos El tipo de Uso, para determinar la lista de Freezer
		for (AlicCaja itemAlicCaja: simlabAlicuotaService.getListAlicByBox()) {
			if(simlabStringUtils.areEquals(itemAlicCaja.getAlicPerm(), this.getTypeAlicSelected()))
			{
				//Seteamos el Uso de la Alicuota
				use = itemAlicCaja.getFreezerAlm();
				temp = itemAlicCaja.getTempAlm();
				break;
			}
		}
		//Seteamos la varible, para definir si es Temporal o Oficial
		String tempOroficial = !this.isNotWrong()
				?SimlabParameterService.getParameterCode(CatalogParam.LIST_USO_CAJA, 2)
				:SimlabParameterService.getParameterCode(CatalogParam.LIST_USO_CAJA, 1);
				
		//Obtenemos el Codigo positivo o no Positivo. segun lo que ingreso el Usuario.
		String posOrNeg = simlabDescriptionService.getCodeByDescriptionAndEstudy("POS_NEG", this.getIndPosNeg());
						
		//Obtenemos una lista de Freezer no vacios en Dependencia del Uso  y la temperatura.
		Collection<Freezer> listFreezer = SimlabEquipService.getListFreezerByUseAndTemp(use, temp);
		//Verificamos Existen Freezer Registrados en el Sistema..
		if(listFreezer.isEmpty())throw new SimlabAppException(10047);
		
		//Obtener la lista de Rack en dependencia del Freezer Seleccionado
		for (Freezer itemFreezer : listFreezer) {
			//Obtenemos la lista de Rack, no importando que estos no tengan cajas.
			if(SimlabEquipService.getListRackByFreezer(itemFreezer.getCodFreezer()).isEmpty())
				{
				nroFreezer= nroFreezer+1;				
				//Generamos Execpcion en caso de que no contengan Rack
				if(nroFreezer==listFreezer.size())
				throw new SimlabAppException(11034);
				}
				
			//Si todos los Rack se encuentran sin Cajas
			if(this.listRackToSuggest(itemFreezer.getCodFreezer()).isEmpty()){
				nroFreezer= nroFreezer+1;
				if(nroFreezer==listFreezer.size())throw new SimlabAppException(11035);
			}
			
			//Si el Freezer no Tiene Rack, entonces le damos la instruccion que continue
			if(this.listRackToSuggest(itemFreezer.getCodFreezer()).isEmpty())
				continue;
			//Nro. de Freezer Recorrido
			nroFreezerRecorridos=nroFreezerRecorridos+1;
			//Recorremos la lista de Rack Contenida en El Freezer
			for (Rack itemRack  : this.listRackToSuggest(itemFreezer.getCodFreezer())) {
				nroRackRecorridos = nroRackRecorridos+1;
				//Si el Rack no contiene Cajas
				if(this.listBoxToSuggest(itemRack.getId().getCodRack(), itemFreezer.getCodFreezer(), posOrNeg, use, tempOroficial).isEmpty()){
					continue;
					}
					
				//Obtenemos la lista de Cajas
				for (Caja itemCaja: this.listBoxToSuggest(itemRack.getId().getCodRack(), itemFreezer.getCodFreezer(), posOrNeg, use, tempOroficial)) {
					//Validamos que la caja tenga posiciones vacias.
					if(SimlabEquipService.boxHaveEmptyPosition(itemFreezer, itemRack,itemCaja)){
						//Si la alicuota es Oficial
						if(this.isNotWrong()){
							boolean encontrada = false;
							String test[] = itemCaja.getTipMues().split(",");
							for (int i=0 ; i<test.length;i++){
								if (test[i].matches("(?i:"+this.getTypeAlicFromCodeAlic()+")")){
									encontrada = true;
								}
							}
							if(encontrada &&simlabStringUtils.areEquals(itemCaja.getPosNeg(), posOrNeg)){
									//Seteamos los Campos para la Vista del Usuario
									this.setCodeRack(itemCaja.getId().getCcodRack());
									this.setCodeBox(Integer.valueOf(itemCaja.getId().getCodCaja()));
									this.setCodeBoxInUse(Integer.valueOf(itemCaja.getId().getCodCaja()));
									this.setAlicBoxInUse(itemCaja.getTipMues());
									this.setCodeFreezer(itemFreezer.getCodFreezer());
									this.setPositionInBox(this.searchPosition(itemFreezer.getCodFreezer(), itemCaja.getId().getCcodRack(), Integer.valueOf(itemCaja.getId().getCodCaja())));
									//Seteamos los Equipos a Sugerir para los Detalles de la Vista.
									this.setFreezerToSuggest(itemFreezer);
									this.setRackToSuggest(itemRack);
									this.setBoxTosuggest(itemCaja);
									
									indicateFound = 1;
									break;
							}							
						}
						//Si la alicuota es de Resguardo
						else{
							 this.setCodeRack(itemCaja.getId().getCcodRack());
							 this.setCodeBox(Integer.valueOf(itemCaja.getId().getCodCaja()));
							 this.setCodeBoxInUse(Integer.valueOf(itemCaja.getId().getCodCaja()));
						 	 this.setCodeFreezer(itemFreezer.getCodFreezer());
						 	 this.setPositionInBox(this.searchPosition(itemFreezer.getCodFreezer(), itemCaja.getId().getCcodRack(), Integer.valueOf(itemCaja.getId().getCodCaja())));
								//Seteamos los Equipos a Sugerir para los Detalles de la Vista.
								this.setFreezerToSuggest(itemFreezer);
								this.setRackToSuggest(itemRack);
								this.setBoxTosuggest(itemCaja);
						 	 indicateFound=1;
							break;
						}
					}
					
				}
				
				//Si el indicador de Sugerencia es = 1, romper el Ciclo de lista de Rack;
				if(indicateFound==1)
					break;
			}
			//Si el indicador de Sugerencia es = 1, romper el Ciclo de Lista de Freezer;
			if(indicateFound==1)
				break;
		}
		
		if(indicateFound==1) 
		
		//En caso de que se hayan recorrido todos los Rack y Freezer y no se encuentren Cajas con posiciones Vacias.
		if(indicateFound==0)
			throw new SimlabAppException(11036);
		
	}
	
	private Integer searchPosition(int freezerCode, String rackCode, int boxCode){
		//Obtenemos la lista de Alicuotas Registradas. En Dependencia de la Ubicacion.
		Collection<RegAlic> listAlicRegistered = simlabAlicuotaService.getListAlicReg(freezerCode, rackCode, boxCode);
		int previousPosition = 0;
		int retornaPosition = 0;
		//Recorremos la lista de Alicuotas Registradas.
	   	for (RegAlic regAlic : listAlicRegistered) {
			if(!(previousPosition+1==regAlic.getId().getPosBox())){
				retornaPosition = previousPosition+1;
				break;
			}
			
			previousPosition = regAlic.getId().getPosBox();
			if(previousPosition==listAlicRegistered.size())
				retornaPosition =previousPosition+1;
		}
	   	if(this.getPositionInBox()==0 || SimlabNumberUtil.isNull(this.getPositionInBox()))
	   		retornaPosition = previousPosition+1;
	   		
	   	return retornaPosition;
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
				this.setTypeAlicByStudy(listAlic);this.setTipAlicuota(listAlic);
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
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
	
	//Obtenemos la lista de Volumen Permitido
	public Collection<String> getListPermittedVol(){
		Collection<String> listPosOrNeg = new ArrayList<String>();
		try {
			listPosOrNeg = simlabDescriptionService.getListPermittedVol();
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
	return listPosOrNeg;

	}
	
	
	public void validateCodeAlicuota() throws SimlabAppException{
		//Validamos que el Codigo de la alicuota no se encuentre nulo o vacio.
		if(simlabStringUtils.isNullOrEmpty(this.getCodeAlic()))
			throw new SimlabAppException(10017);
		//Validamos que el codigo de alicuota corresponda al Patron e Estudio.
		if (this.getStudy().matches("Estudio Hospitalario")){
		if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 1)))
			throw new SimlabAppException(10038);
		//Validamos que el codigo de Alicuota Ingresado corresponda a algun tipo de alicuota registrado en la BD.
		}
		if (this.getStudy().matches("Estudio Longitudinal")){
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 5)))
				throw new SimlabAppException(10038);
			//Validamos que el codigo de Alicuota Ingresado corresponda a algun tipo de alicuota registrado en la BD.
		}
		if (this.getStudy().matches("Estudio Cohorte")){
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 6)))
				throw new SimlabAppException(10038);
			//Validamos que el codigo de Alicuota Ingresado corresponda a algun tipo de alicuota registrado en la BD.
		}
		if (this.getStudy().matches("Estudio Cohorte Muestras C")){
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 7)))
				throw new SimlabAppException(10038);
			//Validamos que el codigo de Alicuota Ingresado corresponda a algun tipo de alicuota registrado en la BD.
		}
		if (this.getStudy().matches("Estudio Transmision Chikungunya")){
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 8)))
				throw new SimlabAppException(10038);
			//Validamos que el codigo de Alicuota Ingresado corresponda a algun tipo de alicuota registrado en la BD.
		}
		if (this.getStudy().matches("Muestreo Covid 2020")){
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 25)))
				throw new SimlabAppException(10038);
			//Validamos que el codigo de Alicuota Ingresado corresponda a algun tipo de alicuota registrado en la BD.
		}
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
	
	//Obtenemos la lista de Tipos de Alicuotas en conforme al Estudio
	public Collection<String> getListTypeAlic(){
		Collection<String> listTypeAlic = null;
			
		return listTypeAlic;
	}
	
	//Agregamos la alicuota a la lista
	public void actionFormAddAlic(ActionEvent event){
		//Hace Falta validar los campos iniciales.. 
		
		//Pasamos a vista Ingreso de Alicuotas
		this.setMode(1);
		
	}
	
	//Validamos los Campos para ser ingresados en El Registros
	public boolean validateFieldsToAdd() throws SimlabAppException{	
		//Se retorna una Variable de Tipo Temporal
		boolean isTemporal = false;
		//if(simlabStringUtils.isNullOrEmpty(this.getVolAlic()))throw new SimlabAppException(10035);
		//Validamos el Codigo de la Alicuota
		if(this.getStudy().matches("Muestreo Anual")) this.tipo="muestreoanual";
		if(this.getStudy().matches("Cohorte Familia MA2017")) this.tipo="muestreoanualchf";
		this.validateCodeAlicuota();
		//Validamos que la alicuota no se encuentre registrada.
		RegAlic itemRegAlic =simlabAlicuotaService.getObjectRegAlic(this.getCodeAlic(),tipo);
		//Si la alicuota existe entonces, Verificamos si la caja donde se encuentra contenida es Temporal.
		if(itemRegAlic!=null){
			//Obtenemos el Codigo de la Caja.
			itemRegAlic.getCodBox();			
			//Obtenemos el Objeto Caja.
			Caja itemCaja = SimlabEquipService.getCajaByCode(String.valueOf(itemRegAlic.getCodBox()));
			//En caso de que sea de Tipo Temporal entonces Seteamos la Variable  a True, indicando que se encuentra en un registro Temporal
			if(simlabStringUtils.areEquals(itemCaja.getUso(), SimlabParameterService.getParameterCode(CatalogParam.LIST_USO_CAJA, 2)))
				isTemporal = true;
			else
				throw new SimlabAppException(10037);
		}
			 
		//Validamos si la Alicuota no sera Descartada.
		if(!this.isIndDescarte())
		{
			//Validamos que el Codigo de Freezer no se encuentre Vacio.
			if(SimlabNumberUtil.isNull(this.getCodeFreezer()))throw new SimlabAppException(10029);
			//Validamos que el Codigo de Rack no se encuentre vacio
			if(simlabStringUtils.isNullOrEmpty(this.getCodeRack()))throw new SimlabAppException(10031);
			if(SimlabNumberUtil.isNull(this.getCodeBox()))throw new SimlabAppException(10032);
			if(SimlabNumberUtil.isNull(this.getPositionInBox()))throw new SimlabAppException(10033); 
			if(simlabStringUtils.isNullOrEmpty(this.getIndPosNeg()))throw new SimlabAppException(10034);
			if(!(this.getPesoAlic()>0 && this.getPesoAlic()<=2))throw new SimlabAppException(12001);
		}
		return isTemporal;
	}
	
	//Accion para Agregar alicuotas a la lista..
	public void actionRegisterAlic(ActionEvent event){
		Session session  = null;
		try {
			//Validamos los Campos antes de Agregar.
			boolean isTemporal = this.validateFieldsToAdd();
			session = this.openSessionAndBeginTransaction();
			//Verificamos si la Opcion es de Descarte.
			if(this.isIndDescarte()){
				//Descartamos la alicuota
				simlabAlicuotaService.discardAlic(session, this.getCodeAlic(), this.getUserCode());
			}
			
			//En Caso De ser Registro
		else{
			this.regAlic(session, this.getCodeAlic(), this.getPositionInBox(), 
					this.getCodeBox(), this.getCodeFreezer(), this.getCodeRack(), this.getUserCode()
					, simlabDescriptionService.getCodeByDescriptionAndEstudy("POS_NEG", this.getIndPosNeg()), isTemporal, this.getVolAlic(), this.getPesoAlic(), tipo);
			}
//				imlabAlicuotaService.addRegAlic(session, this.getCodeAlic(), this.getPositionInBox(), this.getCodeFreezer(), this.getCodeRack(), this.getCodeBox(), "+", "N", this.getUserCode());
			//Nulamos los detalles de Sugerencia.
//			this.removeObjectRegAlicFromList();
			this.setBoxTosuggest(null);
			this.setFreezerToSuggest(null);
			this.closeSessionAndCommitTransaction(session);
			this.fieldToNull();
			this.removeObjectRegAlicFromList();
		}
		catch(SimlabAppException exception){
			SimlabAppException.addFacesMessageError(exception);
		}
		finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
	}
	
	/**Realizamos un nuevo Registro de Alicuotas
	 * @throws SimlabAppException */
	public void regAlic(Session session, String codeAlic, Integer posBox, Integer codBox, Integer codFreezer, 
						String codRack, String codUser, String negPos, boolean isTemporal, Float volAlic, Float pesoAlic, String tipo) throws SimlabAppException{
		try {
			RegAlicId regAlicId = new RegAlicId();
			regAlicId.setCodAlic(codeAlic);
			regAlicId.setPosBox(posBox);
			RegAlic regAlic = new RegAlic();
			regAlic.setId(regAlicId);
			regAlic.setCodBox(codBox);
			regAlic.setCodFreezer(codFreezer);
			regAlic.setCodRack(codRack);
			regAlic.setCodUser(codUser);
			regAlic.setFehorReg(SimlabDateUtil.getCurrentTimestamp());
			regAlic.setNegPos(negPos);	
			regAlic.setVolAlic(pesoAlic*1000);
			regAlic.setPesoAlic(pesoAlic);
			regAlic.setTipo(tipo);
			//Si la alicuota esta registrada como Temporal entonces se Registra la Alicuota
			if(isTemporal){
				session.update(regAlic);
				this.listAlicRegRec.add(regAlic);
			}
			//Caso Contrario
			else{
				session.save(regAlic);
				this.listAlicRegRec.add(regAlic);
			}
		} catch (Throwable exception) {
			throw SimlabAppException.generateExceptionByInsert(MuestIngAlicMbean.class, exception);
		}
}
	//Eliminamos un Objeto de Tipo itemRegAlic de la lista
	public void removeObjectRegAlicFromList(){
	   		if(this.getListAlicRegRec().size()==5){
			int count = 0;
			for (RegAlic itemRegAlic: this.getListAlicRegRec()) {
				count++;
				if(count==1){
					this.getListAlicRegRec().remove(itemRegAlic);
					break;
				}
			}
		}
	}
	
	//Nulamos todos los Campos
	public void fieldToNull(){
		this.setCodeAlic(null);
		this.setCodeFreezer(null);
		this.setCodeRack(null);
		this.setCodeBox(null);
		this.setPositionInBox(null);
		this.setIndPosNeg(null);
		this.setVolAlic(null);
		this.setPesoAlic(null);
	}
	
	/**Este me llena el combo de proposito**/
	public Collection<String> getListUsoAlic(){
		Collection<String>listUsoAlic = new ArrayList<String>();
		try {
			listUsoAlic = simlabDescriptionService.getListUsoAlic("LIST_PROPO");
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return listUsoAlic;
	}
	/**Este me llena el combo de positivo/negativo**/
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
	/**Este me llena el combo de tipo alicuota*/
	public Collection<String> getListCatAlic(){
		Collection<String> listCatAlic = new ArrayList<String>();
		try {
			if (!simlabStringUtils.isNullOrEmpty(this.getUsoAlicuota())) {
				listCatAlic = SimlabEquipService.getListCatAlic(this.getUsoAlicuota());
				this.setTipAlicuota(listCatAlic);
			}else {
				listCatAlic = null;
			}
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
		return this.getTipAlicuota();
	}
	
	public void actionRegisterCaja(ActionEvent event){
		Session session = null;
		try {
			validateFields();
			validateEquipRack(this.getPosRack(), this.getCodRack(), this.getCodigoFreezer());
			int temp = SimlabEquipService.getTempFreezer(this.getCodigoFreezer());
			String codUso = getCodeEquipFromType("LIST_PROPO", this.getUso(), 1);
			String codPosNeg = getCodeEquipFromType("POS_NEG", this.getPosNeg(), 1);
			//Abrimos Session e Iniciamos Transaccion.
			session =this.openSessionAndBeginTransaction();			
			//Registramos Caja	
			SimlabEquipService.addEquipCaja(session, this.getCodeCaja(), this.getCodRack(), this.getPosRack(), this.getUsoAlicuota(), this.getTypeAlicSelected(), 
					SimlabDateUtil.getCurrentDate(), SimlabDateUtil.MAX_DATE, temp, codUso, capAlmBox, codPosNeg,"");
				fieldDialogToNull();
			RequestContext.getCurrentInstance().addCallbackParam("ok", true);
			this.closeSessionAndCommitTransaction(session);
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
	}
	
	private void validateEquipRack(int position, String rackCode, int freezerCode) throws SimlabAppException{
		/*Validando los codigos de rack*/
		if(!SimlabPatternService.isRightPattern(rackCode, SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 4)))
			throw new SimlabAppException(11003);
		
		Rack r = new Rack();
		/*Validando que no se repitan los codigos de rack*/
		r = SimlabEquipService.verifCodRack(rackCode,freezerCode);
		if(r == null) throw new SimlabAppException(11033);
		/*Validando la capacidad del rack*/
		if(SimlabEquipService.isMaxCapcRack(freezerCode, rackCode))throw new SimlabAppException(11011);;
		//else if(r.getCapAlm() >= 15) throw new SimlabAppException(11011);
		/*validando que la posicion digitada del rack este vacia*/
		if(!SimlabEquipService.isPositionAvailableInCaja(position, rackCode))throw new SimlabAppException(11029);//anteriormente 11005
		
		/*Validando que no escribe un numero mayor a la capacidad del rack*/
		if(position > r.getCapAlm())throw new SimlabAppException(11032);
		
		/*Validando que el uso de alicuota digitado coincida con el q se encuentre en la bd*/
		if(!SimlabEquipService.isValidUsoAlic(this.getUsoAlicuota()))throw new SimlabAppException(11031);
		
		/*Validando que el uso de alicuota digitado corresponda con el freezer digitado*/
		if(!SimlabEquipService.isSameTypeAlicuota(this.getCodigoFreezer(), this.getUsoAlicuota()))throw new SimlabAppException(11010);
	}
	
	//Validamos que los Campos 
	private void validateFields() throws SimlabAppException{
		if (simlabStringUtils.isNullOrEmpty(this.getCodeCaja()))throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getCodigoFreezer()) || this.getCodigoFreezer() == 0)throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getCodRack())) throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getPosRack()) || this.getPosRack() == 0)throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getTypeAlicSelected()))throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getUsoAlicuota()))throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getUso()))throw new SimlabAppException(11000);
		if(simlabStringUtils.isNullOrEmpty(this.getPosNeg()))throw new SimlabAppException(11000);
		if(SimlabNumberUtil.isNull(this.getPesoAlic()))throw new SimlabAppException(11000);
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
	
	public void setNextCodeCaja(){
		System.out.println(this.code);
		try {
			if ((this.code != 0 || this.code != null)&&(!simlabStringUtils.isNullOrEmpty(this.getCodRack()))) {
				this.setCodeCaja(SimlabEquipService.getNextCodeCaja(this.getCodigoFreezer(), this.getCodRack()));
			}else {
				this.setCodeCaja("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setCodFreezer(){
		try {
			this.code = this.getCodigoFreezer();
			this.setUsoAlicuota(SimlabEquipService.getUsoAlicFreezer(this.code));
			getListCatAlic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fieldDialogToNull(){
		this.setCodigoFreezer(null);
		this.setCodRack(simlabStringUtils.EMPTY_STRING);
		this.setCodeCaja(simlabStringUtils.EMPTY_STRING);
		this.setPosRack(null);
		this.setUsoAlicuota(simlabStringUtils.EMPTY_STRING);
		this.setUso(simlabStringUtils.EMPTY_STRING);
		this.setPosNeg(simlabStringUtils.EMPTY_STRING);
	}

	public Float getPesoAlic() {
		return pesoAlic;
	}

	public void setPesoAlic(Float pesoAlic) {
		this.pesoAlic = pesoAlic;
	}

	public Collection<RegAlic> getListAlicRegBox() {
		return listAlicRegBox;
	}

	public void setListAlicRegBox(Collection<RegAlic> listAlicRegBox) {
		this.listAlicRegBox = listAlicRegBox;
	}

	public Integer getCodeBoxInUse() {
		return codeBoxInUse;
	}

	public void setCodeBoxInUse(Integer codeBoxInUse) {
		this.codeBoxInUse = codeBoxInUse;
	}

	public String getAlicBoxInUse() {
		return alicBoxInUse;
	}

	public void setAlicBoxInUse(String alicBoxInUse) {
		this.alicBoxInUse = alicBoxInUse;
	}
	
}
