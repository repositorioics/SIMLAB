package ni.gob.minsa.simlab.sistema.exception;
/**Clase para manejar las excepciones de Consulta de Hibernate*/
public class HibernateException extends SimlabAppException{
	/**
	 *Manejo de Excepciones Referentes a las acciones  
	 */
	private static final long serialVersionUID = 4462396530639123639L;
	
	HibernateException(int id){
		super(11000);
	}
		
}
