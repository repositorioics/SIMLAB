package ni.gob.minsa.simlab.sistema.services;

import java.util.ArrayList;
import java.util.Collection;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.sistema.hibernate.bussines.PerfTransac;
import ni.gob.minsa.sistema.hibernate.bussines.PerfTransacId;
import ni.gob.minsa.sistema.hibernate.bussines.Perfil;
import ni.gob.minsa.sistema.hibernate.bussines.Transacs;
import ni.gob.minsa.sistema.hibernate.bussines.UsuaPerf;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SimlabProfileService {
	
	public SimlabProfileService(){super();}
	
	/**Obtiene la lista de Perfiles
	 * @throws SimlabAppException */
	@SuppressWarnings("unchecked")
	public static Collection<Perfil> getListProfile() throws SimlabAppException{
		Collection<Perfil> listProfile = new ArrayList<Perfil>();
		Session session = null;	
		try {
				session = HibernateUtil.openSesion();
				listProfile = session.createQuery("FROM Perfil where FINI_VIG<=? and current_date < ?")
						.setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).setTimestamp(1, SimlabDateUtil.getCurrentTimestamp()).list();
				session.close();
			} catch (Throwable e) {
				throw SimlabAppException.generateExceptionBySelect(SimlabProfileService.class, e);
			}
		return listProfile;
	}
	
	
	/**Obtiene la lista de Transacciones habilitadas para el Perfil pasado como parametro
	 * @throws SimlabAppException */
	@SuppressWarnings("unchecked")
	public static Collection<Transacs> getTransList(String codePerfil) throws SimlabAppException{
		Collection<Transacs> transList = new ArrayList<Transacs>();
		Session  session = null;
			try {
				session = HibernateUtil.openSesion(); 
				transList = session.createSQLQuery("" +
						"SELECT transacs.* FROM transacs , perf_transac " +
						"where COD_PERF = ? " +
						"and transacs.COD_TRANS = perf_transac.COD_TRANS " +
						"and ? >= FEINI_VIG AND ?<=FEFIN_VIG")
						.addEntity(Transacs.class).setString(0, codePerfil).setDate(1, SimlabDateUtil.getCurrentDate())
						.setDate(2, SimlabDateUtil.getCurrentDate()).list();
				session.close();
			} catch (Throwable e) {
				throw SimlabAppException.generateExceptionBySelect(SimlabProfileService.class,e);	
			}
		return transList;
	}
	
	/**Obtiene la lista de Transacciones asociadas al Perfils
	 * @throws SimlabAppException */
	@SuppressWarnings("unchecked")
	public static Collection<Transacs> getTransProfileList(String codePerfil) throws SimlabAppException{
		Collection<Transacs> listTransProfile = new ArrayList<Transacs>();
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				listTransProfile = session.createSQLQuery("" +
						"SELECT transacs.* FROM transacs , perf_transac " +
						"where COD_PERF = ? " +
						"and transacs.COD_TRANS = perf_transac.COD_TRANS " +
						"and ? >= FEINI_VIG AND ?<=FEFIN_VIG")
						.addEntity(Transacs.class).setString(0, codePerfil).setDate(1, SimlabDateUtil.getCurrentDate())
						.setDate(2, SimlabDateUtil.getCurrentDate()).list(); 
				session.close();
			} catch (Throwable e) {
				throw SimlabAppException.generateExceptionBySelect(SimlabProfileService.class,e);
			}
		return listTransProfile;
	}
	
	//Obtenemos la lista de Transacciones que no estan asociados al Perfil
	@SuppressWarnings("unchecked")
	public static Collection<Transacs> getListTxNotAsociated(String codeProfile) throws SimlabAppException{
		Session session = null;
		Collection<Transacs> listTxNotAsociated = new ArrayList<Transacs>();
		try {
			session = HibernateUtil.openSesion();
			listTxNotAsociated = session.createSQLQuery("" +
					"select * from transacs where transacs.COD_TRANS " +
					"NOT IN (SELECT perfil_tx.COD_TRANS From perf_transac perfil_tx where perfil_tx.COD_PERF = ?)")
					.addEntity(Transacs.class).setString(0, codeProfile).list();
			session.close();
			} catch (Throwable e) {
				throw SimlabAppException.generateExceptionBySelect(SimlabProfileService.class, e);
			}
		return listTxNotAsociated;
	}

	/**Obtenemos el Objeto Perfil Transaccion 
	 * @throws SimlabAppException */
	public static  PerfTransac getTransObject(String codePerfil, String codeTransac) throws SimlabAppException{
		PerfTransac objectTransac = null;
		Session session = null;
			try {
				session= HibernateUtil.openSesion();
				objectTransac = (PerfTransac) session.createQuery("FROM perf_transac where COD_PERF = ? " +
						"and COD_TRANS = ?").setString(0, codePerfil).setString(1, codeTransac).uniqueResult();
				session.close();
			} catch (Throwable e) {
				throw SimlabAppException.generateExceptionBySelect(SimlabProfileService.class,e);
			}
		return objectTransac;
	}
	
	/**Obtenemos la lista de Usuarios Asociados al Perfil
	 * @throws SimlabAppException */
	@SuppressWarnings("unchecked")
	public static Collection<UsuaPerf> getListUserProfile(String codeProfile) throws SimlabAppException{
		Collection<UsuaPerf> listUserProfile = new ArrayList<UsuaPerf>();	
		Session session =  null	;
		try {
				session = HibernateUtil.openSesion();
				listUserProfile = session.createQuery(
						"from UsuaPerf where COD_PERF = ? order by COD_PERF").setString(0, codeProfile).list();
				session.close();
			} catch (Throwable e) {
				throw SimlabAppException.generateExceptionBySelect(SimlabProfileService.class,e);
			}		
		return listUserProfile;
	}
	
	/**Agregamos o Actualizamos un elemento de la lista de Perfiles de Usuario en la Base de Datos*/
	public static void addOrUpdateProfileObject(Session session, boolean add, Perfil profile, String codeProfile, String descriptionProfile){
		if(add){
			Perfil perfil= new Perfil(codeProfile, descriptionProfile, SimlabDateUtil.getCurrentTimestamp(), SimlabDateUtil.MAX_DATE);
			session.save(perfil);
			}
		else{
			profile.setCodPerf(codeProfile);
			profile.setDescPerf(descriptionProfile);
			session.update(profile);
		}
	}
	
	//
	public static void asociateProfileWithTransac(boolean add, PerfTransac objectProfile,  String codeTx, String codeProfile ) throws SimlabAppException{
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				Transaction transactSession =  session.beginTransaction();
				if(add){
					PerfTransacId idPerfTx = new PerfTransacId(codeTx, codeProfile);
					PerfTransac objectPerfTx= new PerfTransac(idPerfTx, SimlabDateUtil.getCurrentTimestamp(), SimlabDateUtil.MAX_DATE);
					session.save(objectPerfTx);
					}else{
					objectProfile.setFefinVig(SimlabDateUtil.getCurrentTimestamp());
					session.update(objectProfile);
					}
				transactSession.commit();
				session.close();
			} catch (Throwable e) {
				throw SimlabAppException.generateExceptionByInsert(SimlabProfileService.class, e);
			}
		
	} 
	
	/**Otenemos un Objecto de Tipo Asociacion Pefil-Transaccion*/
	public static PerfTransac getObjectAsocitionPerfTransac(String codeTx, String codeProfile){
		Session session = null;
		PerfTransac perfTransac = null;
		try {
			session = HibernateUtil.openSesion();
				perfTransac = (PerfTransac) session.createSQLQuery(
						"select * from perf_transac where COD_TRANS = ? and COD_PERF=? and ?>= FEINI_VIG AND ?<=FEFIN_VIG").addEntity(PerfTransac.class)
						.setString(0, codeTx).setString(1, codeProfile).setTimestamp(2, SimlabDateUtil.getCurrentTimestamp()).setTimestamp(3, SimlabDateUtil.getCurrentTimestamp())
						.uniqueResult();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabProfileService.class, e);
		}
		return perfTransac;
	}

	public static void closeVigencyToProfile(String codePerf) throws SimlabAppException {
		Session session =  null;
			try {
				session = HibernateUtil.openSesion();
				Transaction  tx = session.beginTransaction();
				Query query = session.createSQLQuery("UPDATE Perfil set FFIN_VIG = ? where COD_PERF = ?").addEntity(Perfil.class)
						.setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).setString(1, codePerf);
				query.executeUpdate();
				tx.commit();
				session.close();
			} catch (Throwable exception) {
				throw SimlabAppException.generateExceptionByUpdate(SimlabProfileService.class, exception);
			}	
	}
	
}