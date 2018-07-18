package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.Query;
import org.hibernate.Session;
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
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlicId;

@ManagedBean(name="subAnuales")
@ViewScoped
public class SubAnuales extends GenericMbean implements Serializable {

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

	public SubAnuales() {
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

	
	public Collection<Object[]> getListBoxesSuggested() throws SimlabAppException{
		
		Collection<Object[]> listBoxesSuggested = new ArrayList<Object[]>();
		if (simlabStringUtils.isNullOrEmpty(this.getTypeAlicSelected())) {
			listBoxesSuggested = null;
		}
		else {
			listBoxesSuggested = simlabAlicuotaService.getCajasSugeridasSubAlicuotas(typeAlicSelected);
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
		if (this.getTypeAlicSelected().matches("14A-2,14B-2")){
			this.volAlic = 200F;
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
			Session session   = null;
			session = this.openSessionAndBeginTransaction();
			Query query;
			try {
				String codigoAlmacenado = this.codeAlic.substring(0, this.codeAlic.length()-2);
				query = session.createQuery("FROM RegAlic reg WHERE lower(reg.id.codAlic) = lower('"+codigoAlmacenado+"') and (tipo = 'muestreoanual' or tipo = 'muestreoanualchf')");
				RegAlic result = (RegAlic) query.uniqueResult();
				if (result == null){
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
															"No encontrado" ,"No hay alicuotas registradas con ese codigo"));
				}
				else {
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
					regAlic.setTipo("muestreoanual");
					regAlic.setPesoAlic(null);
					regAlic.setCondicion(simlabDescriptionService.getCodeByDescriptionAndEstudy("LIST_CONDICION", this.getCondicion()));
					regAlic.setSeparada(simlabDescriptionService.getCodeByDescriptionAndEstudy("LIST_LAB", this.getSeparada()));
					simlabAlicuotaService.saveAlicuota(regAlic);
					Timestamp fHoraReg = SimlabDateUtil.getCurrentTimestamp();
					Float volumenUsado = this.volAlic + 200;
					
					if(result.getVolAlic() <= volumenUsado){
						query = session.createSQLQuery("INSERT INTO `reg_alic_uso` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
								"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `vol_usado`, `peso_alic`, `uso`, `usuario`, `tipo`, `condicion`, `separada`, `num_desc`)" +
								" VALUES ('" + result.getId().getCodAlic() + "'," + result.getId().getPosBox() +", " + result.getCodFreezer() + ", '" + result.getCodRack() +"', " + result.getCodBox() + ",  '" + result.getNegPos() +"', " +
										"'" + result.getFehorReg() +"', '" + result.getCodUser() +"', " + result.getVolAlic() + ", " + volumenUsado + ", " + result.getPesoAlic() +",'muestreo anual y subalicuota', '"+ this.getUserCode() +"', '"+ result.getTipo() +"', '"+ result.getCondicion() +"', '"+ result.getSeparada() +"', "+result.getNumDes()+"); ");
						query.executeUpdate();
						query = session.createSQLQuery("Delete from reg_alic " +
								"where cod_alic = '" + result.getId().getCodAlic() +"' and " +
								"pos_box = " + result.getId().getPosBox());
						query.executeUpdate();
					}
					else{
						query = session.createSQLQuery("INSERT INTO `reg_alic_uso` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
								"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `vol_usado`, `peso_alic`, `uso`, `usuario`, `tipo`, `condicion`, `separada`, `num_desc`)" +
								" VALUES ('" + result.getId().getCodAlic() + "'," + result.getId().getPosBox() +", " + result.getCodFreezer() + ", '" + result.getCodRack() +"', " + result.getCodBox() + ",  '" + result.getNegPos() +"', " +
										"'" + result.getFehorReg() +"', '" + result.getCodUser() +"', " + result.getVolAlic() + ", " + volumenUsado + ", " + result.getPesoAlic() +",'muestreo anual y subalicuota', '"+ this.getUserCode() +"', '"+ result.getTipo() +"', '"+ result.getCondicion() +"', '"+ result.getSeparada() +"', "+result.getNumDes()+"); ");
						query.executeUpdate();
						result.setVolAlic(result.getVolAlic()-this.volAlic - 200);
						result.setNumDes(result.getNumDes()+1);
						result.setFehorReg(fHoraReg);
						result.setCodUser(this.getUserCode());
						session.update(result);
					}
					
					this.closeSessionAndCommitTransaction(session);
					this.fieldToNull();
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
		//if (this.getTypeAlicSelected().matches("14B-2")||this.getTypeAlicSelected().matches("14A-2")){
			//Validamos el patron
			if(!SimlabPatternService.isRightPattern(this.getCodeAlic(), SimlabParameterService.getParameterCode(CatalogParam.LIST_PATRON, 14))) return false;
			//Obtenemos todo el Sufijo de la alicuota ingresada por el Usuario
			getSufixAlic = simlabStringUtils.cutToLenght(this.getCodeAlic(), this.getCodeAlic().indexOf(".")+1, this.getCodeAlic().length());
		//}
		//Validamos si el Arreglo contiene elementos
		if(itemTypeAlicSelected.length>0){
			for (String itemAlic : itemTypeAlicSelected) {
				//Verificamos si el Tipo Seleccionado contiene numeros para identificar el tipo de ALicuota
				//if(SimlabNumberUtil.isNumber(simlabStringUtils.cutToLenght(itemAlic.trim(), 0, 0+1))){
					//	Validamos que la cadena si el Elemento es igual a lo digitado por el usuario.
					if(itemAlic.matches("(?i)" +  getSufixAlic)){
						return true;
					}
			}
		}
		return false;
	}
	
}