package ni.gob.minsa.simlab.sistema.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.sistema.hibernate.bussines.Patron;

import org.hibernate.Session;

/**Metodo para Manejo de Patrones en BD*/
public class SimlabPatternService {

	public SimlabPatternService(){
		//Constructor
		super();
	}
	
	/**Valida patron en comparacion con un elemento Patron de la BD
	 * @throws SimlabAppException */
	public static boolean isRightPattern(String patronApp, String codePatronBd) throws SimlabAppException{
		String patronToString = null;
		if(SimlabPatternService.getPattern(codePatronBd)!=null)
			patronToString = SimlabPatternService.getPattern(codePatronBd).getPatron();
		//Verificamos si El Valor Ingresado tiene un Formato Valido
		return  SimlabPatternService.patternCompare(patronApp, patronToString);		
		
	}

	//Obtenemos el Patron
	public static Patron getPattern(String codePatron){
		Patron patron = null;
		Session session = null;
			try {
				//Abrimos nueva Session
				session = HibernateUtil.openSesion();
						patron = (Patron) session.createSQLQuery("SELECT * FROM patron where cod_patron = ?")
								.addEntity(Patron.class).setString(0, codePatron).uniqueResult();
					session.close();	
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(SimlabPatternService.class, e);
			}
			return patron;	
	}
	
	/**Compara Patron con Cadena Ingresada por el Usuario*/
	public static boolean patternCompare(String patronApp, String codePatronBd){
		boolean isValid = false;
		//Compilamos el Patron
		Pattern patron  = Pattern.compile(codePatronBd);
		Matcher matcher = patron.matcher(patronApp);
		matcher.groupCount();
		//Verificamos si existe coincidencia con el patron, En caso contrario Generamos una Excepcion
		isValid = matcher.matches();
		//			throw new SimlabAppException(11005);
		return isValid;
	}	
	
	public static void savePattern (Session session, Patron pattern) throws SimlabAppException{		
		try {
			session.beginTransaction();
			session.saveOrUpdate(pattern);
			session.getTransaction().commit();
		}
		catch (Exception e) {
			throw SimlabAppException.generateExceptionByUpdate(SimlabPatternService.class, e);
		}
	}
	
}
