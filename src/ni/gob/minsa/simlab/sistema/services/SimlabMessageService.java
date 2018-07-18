package ni.gob.minsa.simlab.sistema.services;

import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.sistema.hibernate.bussines.Messages;

import org.hibernate.Session;

public class SimlabMessageService {
	
	public SimlabMessageService(){
		super();
	}
	
	/**Devolvemos el objeto Mensaje por medio del codigo de Mensaje*/ 
	public static Messages getObjMsg( int codeMsg){
		Session openSesion = null;
		Messages msg = null;
			try {
				openSesion = HibernateUtil.openSesion();
				msg = (Messages) openSesion.createQuery("FROM Messages Where MID = ?").setInteger(0, codeMsg).uniqueResult();
				openSesion.close();
		} catch (Exception e) {
			
		}	//Si el mensaje no se encuentra entonces...
			if(msg == null)
			//Creamos un nuevo Objeto con un Mensaje Predeterminado de Mensaje No Encontrado
				msg = new Messages(0001, "MENSAJE NO ENCONTRADO", "APP");
		return msg;
	} 
	
	/**Devolvemos la descripcion del Mensaje*/
	public static String getDescription(int codeMsg){
		Messages objMsg = SimlabMessageService.getObjMsg(codeMsg);
		if(objMsg!=null)
			return objMsg.getDescription();  
		else return null;
	}
	
	/**Devolvemos una cadena con  el Tipo de Mensaje*/
	public static String getTypeMsg(int codeMsg){
		Messages objMsg = SimlabMessageService.getObjMsg(codeMsg);
		if(objMsg!=null)
			return objMsg.getTypeMsn();  
		else return null;		
	}
	
	/**Obtenemos la Cabera del Mensaje, esto en dependiencia de la Excepcion*/
		public static String getHeaderMessage(int codeMsg, int nroOrde){
			String headerMsg = null;
			//Construyendo el Mensaje Cabecera de Error.
					headerMsg = nroOrde==1
							?SimlabParameterService.getItemParam(CatalogParam.TYPE_MSN, nroOrde).getDescItem()
									.concat("-").concat(SimlabMessageService.getTypeMsg(codeMsg)).concat("-").concat(String.valueOf(codeMsg)).concat(":")
							:SimlabParameterService.getItemParam(CatalogParam.TYPE_MSN, nroOrde).getDescItem()
							.concat("-").concat(String.valueOf(codeMsg)).concat(":");
				
			return headerMsg;
		}
}
