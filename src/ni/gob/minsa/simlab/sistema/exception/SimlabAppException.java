package ni.gob.minsa.simlab.sistema.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import ni.gob.minsa.simlab.sistema.services.SimlabMessageService;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;

public class SimlabAppException extends Exception{
	private static final long serialVersionUID = 1130816568336947111L;
	
	public SimlabAppException(){}
	//Manejo de Excepciones Verificadas.
	//Gestion de Errores, los mensajes de aplicacion denominados del 10000 
	//Mensajes de Informacion 11000
	//Mensajes de Conexion 
	private String descriptionMsg = null;
	
	private String headerMsg = null;
	
	private Integer exceptionNumber = null;
	
	public Integer getExceptionNumber() {
		return exceptionNumber;
	}

	public void setExceptionNumber(Integer exceptionNumber) {
		this.exceptionNumber = exceptionNumber;
	}

	public String getHeaderMsg() {
		return headerMsg;
	}

	public void setHeaderMsg(String headerMsg) {
		this.headerMsg = headerMsg;
	}

	public String getDescriptionMsg() {
		return descriptionMsg;
	}

	public void setDescriptionMsg(String descriptionMsg) {
		this.descriptionMsg = descriptionMsg;
	}
	
	public  SimlabAppException(int exceptionNumber){
		this.descriptionMsg = SimlabMessageService.getDescription(exceptionNumber);
		this.exceptionNumber = exceptionNumber;
	}
	
	public SimlabAppException(int exceptionNumber, String ... params){
		this.descriptionMsg = SimlabPropertiesService.getPropertiesMessageToString(exceptionNumber, params);
		this.exceptionNumber = exceptionNumber;
	}
	
	/**Agregamos un Mensaje de Error al contexto de la Aplicacion*/
	public static void addFacesMessageError(SimlabAppException exception){
		if(exception == null)return;
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
												SimlabMessageService.getHeaderMessage(exception.exceptionNumber, 1) ,exception.getDescriptionMsg()));
	}
	
	/**Agregamos un Mensaje de Informacion al contexto de la Aplicacion*/
	public static void addFacesMessageInfo(SimlabAppException exception){
		if(exception == null)return;
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,SimlabMessageService.getHeaderMessage(exception.exceptionNumber, 3) 
							,exception.getDescriptionMsg()));
	}
	
	/**Agregamos un Mensaje de Advertencia al contexto de la Aplicacion*/
	public static void addFacesWarning(SimlabAppException exception){
		if(exception ==null)return;
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, SimlabMessageService.getHeaderMessage(exception.exceptionNumber, 2)
				,exception.getDescriptionMsg()));
	}
	
	/**Creamos un nuevo objeto SimlabAppException*/
	public static void addFacesMessageError(int codeMessage){
		SimlabAppException.addFacesMessageError(new SimlabAppException(codeMessage));
	}
	
	/**Creamos un nuevo objeto SimlabAppException con Paso de Parametros a la Base de Datos*/
	public static void addFacesMessageError(int codeMessage, String ... params){
		SimlabAppException.addFacesMessageError(new SimlabAppException(codeMessage, params));
	}
	
	/**Genera Excepcion de Hibernate*/
	private static SimlabAppException generateException(Class<?> clase, Throwable hEx, int errorCode){
		 SimlabAppException appEx = new SimlabAppException(errorCode);
	        Logger.getLogger(clase.getName()).log(Level.SEVERE, hEx.getMessage(), hEx);
	        return appEx;
	  }
	
	/**Genera Error, Captura y muestra en el Log error al seleccionar producido por Hibernate*/
	public static SimlabAppException generateExceptionBySelect(Class<?> clase, Throwable hEx){
		return generateException(clase, hEx, 12003);
	}
	
	/**Genera Error, Captura y muestra en el Log error al Insertar producido por Hibernate*/
	public static SimlabAppException generateExceptionByInsert(Class<?> clase, Throwable hEx){
		return generateException(clase, hEx, 12001);
	}
	
	/**Genera Error, Captura y muestra en el Log error al Actualizar producido por Hibernate*/
	public static SimlabAppException generateExceptionByUpdate(Class<?> clase, Throwable hEx){
		return generateException(clase, hEx, 12002);
	}
	
}