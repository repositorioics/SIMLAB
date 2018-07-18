package ni.gob.minsa.simlab.sistema.services;

import java.util.ArrayList;
import java.util.Collection;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabSqlUtils;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.PerfTransac;
import ni.gob.minsa.sistema.hibernate.bussines.Transacs;
import ni.gob.minsa.sistema.hibernate.bussines.UsuaPerf;
import ni.gob.minsa.sistema.hibernate.bussines.UsuaPerfId;
import ni.gob.minsa.sistema.hibernate.bussines.Usuario;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SimlabUserService {
	//Metodo Constructor
  SimlabUserService(){}
   
//obtenemos la lista de Usuarios registrados en el Sistema.
 @SuppressWarnings("unchecked")
public static Collection<Usuario> getListUser() throws SimlabAppException{
	 Collection<Usuario> listUser = new ArrayList<Usuario>();
	 Session sesion = null; 	
	 try{
		 //abrimos Sesion
		 sesion = HibernateUtil.openSesion();
		 listUser = sesion.createSQLQuery("select * from usuario where ?>= USUA_DATEINI AND ? <USUA_DATEFIN").addEntity(Usuario.class)
				 .setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).setTimestamp(1, SimlabDateUtil.getCurrentTimestamp()).list();
		 sesion.close();
		 }catch (Throwable exception) {
			 SimlabAppException.generateExceptionBySelect(SimlabUserService.class, exception);
		}
	return listUser;
	}
 
  //Obtenemos Usuario mediante Password y NickName
 public static Usuario getUserByPassAndNickName(String password, String nickName) throws SimlabAppException{
	 Usuario user = null;
	  Session sesion = null;
	 	try {
			sesion = HibernateUtil.openSesion();
			user = (Usuario) sesion.createSQLQuery(
					"SELECT usuario.* FROM usuario, password where usua_code = PASS_CODUSER AND PASS_CODUSER = ? AND PASS_MD5=? " +
					"AND ?>= USUA_DATEINI AND ? <=USUA_DATEFIN").addEntity(Usuario.class).setString(0, nickName)
					.setString(1, password).setTimestamp(2, SimlabDateUtil.getCurrentTimestamp()).setTimestamp(3, SimlabDateUtil.getCurrentTimestamp()).uniqueResult();
			sesion.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(SimlabUserService.class, exception);
		}
	 return user;
 }
 
 /**Verificamos que el Usuario Exista o se encuentre Habilitado, Enviamos un msn de Error en Caso que no se cumpla la condicion
 * @throws SimlabAppException 
  * */
 public static void userIsAbleAndExist(String nickName) throws SimlabAppException{
	 if(SimlabUserService.getUserByNickName(nickName)==null)throw new SimlabAppException(11049);
	 if(!SimlabUserService.isHabilited(nickName))throw new SimlabAppException(11049);
 } 
 
 /**Obtenemos una lista de Usuarios Filtrados por Nombre y codigo de Usuario*/
 @SuppressWarnings("unchecked")
public static Collection<Usuario> getUserFilterList(String name, String codeUser){
	 Collection<Usuario> userlistFiltered = new ArrayList<Usuario>();
	 Session session = null;
	 try {
		 session = HibernateUtil.openSesion();
		userlistFiltered = session.createSQLQuery("SELECT * " +
				"FROM usuario " +
				"where usua_name like upper(?) and usua_code like upper(?) and ?>= USUA_DATEINI AND ? <=USUA_DATEFIN")
				.addEntity(Usuario.class).setString(0, SimlabSqlUtils.likeRightLef(name)).setString(1, SimlabSqlUtils.likeRightLef(codeUser))
				.setDate(2, SimlabDateUtil.getCurrentDate()).setDate(3, SimlabDateUtil.getCurrentDate()).list();
		session.close();		
	} catch (Throwable exception) {
		SimlabAppException.generateExceptionBySelect(SimlabUserService.class, exception);
	}
	 return userlistFiltered;
 }
 
 /**Verificar que exista el Usuario Informado
 * @throws SimlabAppException */
 public static boolean existUser(String password, String nickName) throws SimlabAppException{
	 boolean exist = false;
	 	if(SimlabUserService.getUserByPassAndNickName(password, nickName)!=null)exist = true;
	 return exist;
 }
 
 /**Obtenemos la lista de Objeto Perfil asociada al Usuario mediante el Codigo de Usuario*/
 @SuppressWarnings("unchecked")
public static Collection<UsuaPerf> getListProfileByUser(String codeUser){
	Collection<UsuaPerf> listProfileByUser = new ArrayList<UsuaPerf>();
	Session session  = null;
	try {
		session = HibernateUtil.openSesion();
		listProfileByUser = session.createQuery("FROM UsuaPerf " +
				"where COD_USER = ? and ?> FEINI_VIG AND ?< FEFIN_VIG").setString(0, codeUser).setDate(1, SimlabDateUtil.getCurrentDate())
				.setDate(2, SimlabDateUtil.getCurrentDate()).list();
		session.close();
	} catch (Throwable exception) {
		SimlabAppException.generateExceptionBySelect(SimlabUserService.class,exception);
	}
	
	return listProfileByUser;
 }

 /**Obtenemos el Objeto Perfil pasado como parametro asociado al Usuario*/
 public static UsuaPerf getProfileByUser(String codeUser, String codeProfile){
	 UsuaPerf profile = null;
	 Session session = null;
	 	try {
			session = HibernateUtil.openSesion();
			profile = (UsuaPerf) session.createQuery("FROM usua_perf where cod_user = ? and COD_PERF = ? " +
					"and ?>= FEINI_VIG AND ?<= FEFIN_VIG").setString(0, codeUser).setString(1, codeProfile)
					.setDate(2, SimlabDateUtil.getCurrentDate()).setDate(3, SimlabDateUtil.getCurrentDate()).uniqueResult();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(SimlabUserService.class,exception);
		}
	 return profile;
 }
 
 /**Obtenemos la lista de Transacciones asociadas al Usuario, Pasado como parametros*/
 @SuppressWarnings("unchecked")
public static Collection<PerfTransac> getListTransactionByProfileAndUser(String codeProfile, String codeUser){
	 Collection<PerfTransac> listTransaction  = new ArrayList<PerfTransac>();
	 Session session = null;	
	 try {
			session = HibernateUtil.openSesion();
			listTransaction = session.createSQLQuery("select perf_transac.* " +
					"from perf_transac, usua_perf " +
					"where COD_USER = ? AND perf_transac.COD_PERF = ?").setString(0, codeUser).setString(1, codeProfile).list();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(SimlabUserService.class, exception);
		}
	 return listTransaction;
 }
 
/** Obtenemos lista de Codigo de Transacciones asociadas al Usuario */
 public static Collection<String> getListTxCode(String codeProfile, String codeUser){
	 Collection<String> listTxCode = new ArrayList<String>();
	 Collection<PerfTransac> listTx = SimlabUserService.getListTransactionByProfileAndUser(codeProfile, codeUser);
	 for (PerfTransac perfTransac : listTx) {
		 listTxCode.add(perfTransac.getId().getCodTrans());
	}
	 return listTxCode;
}
 
 /**Obtenemos la lista de Transacciones de usuario 
 * @throws SimlabAppException */
 @SuppressWarnings("unchecked")
public static Collection<PerfTransac> getListTxUser(String codeUser) throws SimlabAppException{
	 Collection<PerfTransac> listTxCode = new ArrayList<PerfTransac>();
	 String parameterS = SimlabParameterService.getParameterCode(CatalogParam.SI_NO, 1);
	 Session session = null;
	  try {
		  session = HibernateUtil.openSesion();
		  listTxCode = session.createSQLQuery("SELECT * FROM perf_transac " +
		  		"WHERE COD_TRANS IN (SELECT COD_TRANS FROM transacs where IND_HAB = ? and INDPE_TRANS = ?) " +
		  		"and COD_PERF IN (SELECT COD_PERF FROM usua_perf where COD_USER = ? and ? >= FEINI_VIG AND FEFIN_VIG >= ?) " +
		  		"AND ? >= FEINI_VIG AND FEFIN_VIG>=?")
		  		.addEntity(PerfTransac.class).setString(0, parameterS).setString(1, parameterS).setString(2, codeUser)
		  		.setTimestamp(3, SimlabDateUtil.getCurrentTimestamp()).setTimestamp(4, SimlabDateUtil.getCurrentTimestamp()).setTimestamp(5, SimlabDateUtil.getCurrentTimestamp())
		  		.setTimestamp(6, SimlabDateUtil.getCurrentTimestamp()).list();
		  session.close();
		} catch (Throwable exception) {
		throw SimlabAppException.generateExceptionBySelect(SimlabUserService.class, exception);
		}
	 return listTxCode;
 }
 
 /**Obtenemos la lista de Transacciones de la Aplicacion en General
 * @throws SimlabAppException */
 @SuppressWarnings("unchecked")
public static Collection<Transacs> getListTx() throws SimlabAppException{
	 Collection<Transacs> listTx = new ArrayList<Transacs>();
	 	Session session = null;
	 	try {
			session = HibernateUtil.openSesion();
				listTx = session.createQuery("FROM Transacs").list();
			session.close();
		} catch (Throwable exception) {
			throw SimlabAppException.generateExceptionBySelect(SimlabUserService.class, exception);
		}
	 	return listTx;
 }
 
 /**Retornamos la lista de Codigos de Transaccion asociada al Usuario
 * @throws SimlabAppException */
 public static Collection<String> getListTxCode( String codeUser) throws SimlabAppException{
	 Collection<String> listTxCode  = new ArrayList<String>();
	 Collection<PerfTransac> listTxUser = SimlabUserService.getListTxUser(codeUser);
	 	for (PerfTransac perfTransac : listTxUser) {
			listTxCode.add(perfTransac.getId().getCodTrans());
		}
	return listTxCode;
 }
 
//Obtenemos Usuario mediante Password y NickName
public static Usuario getUserByNickName(String nickName) throws SimlabAppException{
	 Usuario user = null;
	  Session sesion = null;
	 	try {
			sesion = HibernateUtil.openSesion();
			user = (Usuario) sesion.createQuery(
					"FROM Usuario where USUA_CODE = ? and ?>= USUA_DATEINI AND ?<= USUA_DATEFIN").setString(0, nickName)
					.setTimestamp(1,SimlabDateUtil.getCurrentTimestamp()).setTimestamp(2, SimlabDateUtil.getCurrentTimestamp()).uniqueResult();
			sesion.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(SimlabUserService.class, exception);
		}
	 return user;
}
 
/**Validamos que el Usuario se encuentre Habilitado*/
 public static boolean isHabilited(String nickName) throws SimlabAppException{
	 boolean isAble = false;
	 	Usuario user = 	getUserByNickName(nickName);
		if(user==null)throw new SimlabAppException(10000);
		else if(SimlabDateUtil.before(SimlabDateUtil.getCurrentDate(), user.getId().getUsuaDatefin())) isAble = true;
			 return isAble;
 } 

 /**Cerramos la vigencia del Usuario Mediante su Codigo de Usuario*/
public static void closeVigToUser(String nickName){
	Session session = null;
		try {
		//Abrimos Session 
			session = HibernateUtil.openSesion();
			Transaction tx = session.beginTransaction();
			Query query =  session.createSQLQuery("UPDATE Usuario set USUA_DATEFIN = ? where usua_code = ?")
					.setTimestamp(0, SimlabDateUtil.getCurrentDate()).setString(1, nickName);
			query.executeUpdate();
			tx.commit();
			session.close();
		} catch (Throwable exception) {
		SimlabAppException.generateExceptionByUpdate(SimlabUserService.class, exception);
	}finally{
		HibernateUtil.analizeForRollbackAndCloseSession(session);
	}
} 


/**Obtenemos el nombre Completo con un formato correcto*/
public static String getCompleteNameUser(String firstName, String secondName, String firstLastName, String secondLastName){
	String completeName = null;
		completeName = formatName(firstName).concat(" ").concat(formatName(secondName))
						.concat(" ").concat(formatName(firstLastName)).concat(" ").concat(formatName(secondLastName));
		return completeName;
}
 
/**Le aplicamos Formato a Nombre o Apellido*/
public static String formatName(String nameOrLastName){
	String formatName = null;
	if(simlabStringUtils.isNullOrEmpty(nameOrLastName)) formatName = simlabStringUtils.EMPTY_STRING;
	formatName = simlabStringUtils.toUpper(simlabStringUtils.cutToLenght(nameOrLastName, 0, 1)).concat(simlabStringUtils.cutToLenght(nameOrLastName, 1, nameOrLastName.length()));
	return formatName;
	}

	/**
	 * Metodo para asociar al usuario digitado el perfil
	 * @param session 
	 * @param codeUser codigo del usuario
	 * @param codePerfil codigo del perfil
	 */
	public static void savePerfil(Session session , String codeUser, String codePerfil){
		
		try {
			UsuaPerfId pId = new UsuaPerfId();pId.setCodUser(codeUser);pId.setCodPerf(codePerfil);
			UsuaPerf perf = new UsuaPerf();
			perf.setId(pId);perf.setFeiniVig(SimlabDateUtil.getCurrentDate());perf.setFefinVig(SimlabDateUtil.MAX_DATE);
			session.save(perf);
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByInsert(SimlabUserService.class, e);
		}
	}
 
}
