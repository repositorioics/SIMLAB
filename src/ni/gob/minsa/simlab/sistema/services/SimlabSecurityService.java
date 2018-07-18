package ni.gob.minsa.simlab.sistema.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Password;

import org.hibernate.Session;

public class SimlabSecurityService {

	private static final int SIZE_PASS_ENCRIPTED = 32;
    private static final String ALGORITM_ENCRIPT = "MD5";
    
	public SimlabSecurityService(){super();}
	
	public static void saveOrUpdatePass(String codeUser, String thePass, boolean isBlanqueo, Session session) throws SimlabAppException{
		//Verificamos que el Campo Usuario no se encuentre Vacio
		if(simlabStringUtils.isNullOrEmpty(codeUser))throw new SimlabAppException(10001);
		//Verificamos que el Campo Contrase�a no se encuentre Vacio
		if(simlabStringUtils.isNullOrEmpty(thePass)) throw new SimlabAppException(10002);
		//Obtenemos el Objeto Pass del Usuario
		Password getPass = SimlabSecurityService.getPassObject(codeUser);
		try {
			if(getPass==null){
				Password objectPassword = new Password(
				codeUser, SimlabParameterService.getParameterCode(CatalogParam.SI_NO, 1), SimlabDateUtil.getCurrentDate(), 
				SimlabSecurityService.generateMd5Key(thePass));
				session.save(objectPassword);
			}else{
				getPass.setPassIndblan(SimlabParameterService.getParameterCode(CatalogParam.SI_NO, isBlanqueo?1:2));
				getPass.setPassMd5(SimlabSecurityService.generateMd5Key(thePass));
				getPass.setPassDtblan(SimlabDateUtil.getCurrentDate());
				session.update(getPass);
			}
		} catch (Throwable e){
			throw SimlabAppException.generateExceptionByUpdate(SimlabSecurityService.class, e);
		}		
	}
	
/** Obtiene la contraseña almacenada para un usuario como una cadena de texto.
NOTA: La contraseña se encuentra encriptada. */
public static String getPassAsString(String userCode) throws SimlabAppException {
    Password pass = getPassObject(userCode);
    return pass==null ? null : pass.getPassMd5();
}

/** Verifica que la contraseña informada para un usuario corresponda con la almacenada.
    NOTA: Internamente la contraseña es encritada para compararse con el registro que tambien se encuentra encriptado. */
public static boolean isCorrectPass(String userCode, String thePass) throws SimlabAppException {
    // verificamos que el usuario no se encuentre vacio
    if (simlabStringUtils.isNullOrEmpty(userCode)) throw new SimlabAppException(10001);
    // verificamos que el password no se encuentre vacio
    if (simlabStringUtils.isNullOrEmpty(thePass)) throw new SimlabAppException(10002);
    // obtenemos el password guardado y encriptado del usuario
    String passSaved = getPassAsString(userCode);
    // si el password guardado es nulo o tiene un formato incorrecto, error
    if (simlabStringUtils.isNullOrEmpty(passSaved)) throw new SimlabAppException(11005);
    if (passSaved.length()!=SIZE_PASS_ENCRIPTED) {
        SimlabAppException appEx = new SimlabAppException(11006);
        Logger.getLogger(SimlabSecurityService.class.getName()).log(Level.SEVERE, appEx.getMessage()+" >>> Contraseña Encriptada="+passSaved);
        throw appEx;
    }
    // comparamos y retornamos el resultado
    return generateMd5Key(thePass).equals(passSaved);
}

    /** Obtiene el objeto contraseña almacenada para un usuario. */
    public static Password getPassObject(String userCode) throws SimlabAppException{
    	// verificamos que el usuario no se encuentre vacio
        if (simlabStringUtils.isNullOrEmpty(userCode)) throw new SimlabAppException(10001);
        Session session = null;
        Password objectPassword = null;
        try {
			session = HibernateUtil.openSesion();
			objectPassword = (Password) session.createSQLQuery("SELECT * from password where pass_Coduser = ?")
					.addEntity(Password.class).setString(0, userCode).uniqueResult();
			session.close();
			} catch (Throwable e) {
				throw SimlabAppException.generateExceptionBySelect(SimlabSecurityService.class, e);
		}
    	return objectPassword;
    }
	
	 /** 
     * Encripta un String con el algoritmo MD5.  
     */
    public static String generateMd5Key(String string) throws SimlabAppException{
        // si la cadena de entrada es nula, salimos
        if (string==null) return null;
        // obtenemos el objero que implementa el algoritmo
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance(ALGORITM_ENCRIPT);
        } catch (NoSuchAlgorithmException ex) {
        	//TODO: cambiar el Numero de Excepcion, anexar el numero en BD.
            SimlabAppException appEx = new SimlabAppException(90100);
            Logger.getLogger(SimlabSecurityService.class.getName()).log(Level.SEVERE, appEx.getMessage(), ex);
            throw appEx;
        }
        // procesamos
        algorithm.reset();
        algorithm.update(string.getBytes());
        byte bytes[] = algorithm.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        // retornamos el resultado
        return sb.toString();
    }
}
