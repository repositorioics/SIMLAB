package ni.gob.minsa.simlab.sistema.hibernate;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Startup;
import javax.inject.Singleton;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

@Singleton
@Startup
public class HibernateUtil_uniqueSession {
	private static final String HIBERNATE_CONFIG_FILE = "ni/gob/minsa/simlab/sistema/hibernate/hibernate.cfg.xml";
//	private static final String JNDI_NAME = "java:global/SessionFactory";
	private static final SessionFactory sesionFactory = buildSessionFactory();
	
	private static final ThreadLocal<Session> session  = new ThreadLocal<Session>();
		
	private static SessionFactory buildSessionFactory(){
//			SessionFactory sessionFactory = null;
				try {
					Configuration configuration = new Configuration();
					configuration.configure(HIBERNATE_CONFIG_FILE);
					ServiceRegistry serviceRegistry =  new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
//					sessionFactory = new Configuration().configure(HIBERNATE_CONFIG_FILE).buildSessionFactory();
					return configuration.buildSessionFactory(serviceRegistry);
				} catch (Throwable exception) {
					exception.getCause();
					throw new ExceptionInInitializerError(exception);
				}
			}

	@SuppressWarnings("unused")
	private static SessionFactory getSessionFactory(){
			return sesionFactory;			
		}
		
		public static Session openSesion(){
			Session sessionLocal = HibernateUtil_uniqueSession.session.get();
			try {
				if(sessionLocal!=null){
						if(sessionLocal.isOpen()){
							return sessionLocal;
						}
					
				}
				
				sessionLocal = HibernateUtil_uniqueSession.sesionFactory.openSession();
				HibernateUtil_uniqueSession.session.set(sessionLocal);
				return sessionLocal;
				//				session = getSessionFactory().openSession();
			} catch (Exception e) {
				Logger.getLogger(HibernateUtil_uniqueSession.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			return sessionLocal;
		}
		
		public static void closeSession(Session session){
			if(session != null)
				{
					if(session.isOpen()){
						session.close();
						HibernateUtil_uniqueSession.session.set(null);
					}
				
				}
		}
		
		/**Anliza si hubo Cambios en la Base de Datos, Hace RollBack y Cierra Session*/
		public static void analizeForRollbackAndCloseSession(Session session){
			if(session ==null || !session.isOpen())return;
			org.hibernate.Transaction tx = session.getTransaction();
			if(!tx.wasCommitted()||tx==null)tx.rollback();
			session.close();				
		}

		/**Guadamos Objeto en BD*/
		public static void saveObject(Object object){
			Session session = null;
			try {
				session = HibernateUtil_uniqueSession.openSesion();
				org.hibernate.Transaction tx = session.beginTransaction();
				session.save(object);
				tx.commit();
				session.close();
			} catch (Throwable exception) {
				Logger.getLogger(HibernateUtil_uniqueSession.class.getName()).log(Level.SEVERE, exception.getMessage(), exception);
			}finally{analizeForRollbackAndCloseSession(session);}
		}
		
		/**actualizamos Objeto en BD*/
		public static void updateObject(Object object){
			Session session = null;
			try {
				session = HibernateUtil_uniqueSession.openSesion();
				org.hibernate.Transaction tx = session.beginTransaction();
				session.update(object);
				tx.commit();
				session.close();
			} catch (Throwable e) {
				Logger.getLogger(HibernateUtil_uniqueSession.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}finally{
				analizeForRollbackAndCloseSession(session);
			}
		}
				
		/**Eliminamos Objeto de la Bd*/
		public static void deleteObject(Object object){
			Session session = null;
			try {
				session = HibernateUtil_uniqueSession.openSesion();
				org.hibernate.Transaction tx  =  session.beginTransaction();
				session.delete(object);
				tx.commit();
				session.close();
			} catch (Exception e) {
				Logger.getLogger(HibernateUtil_uniqueSession.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}finally{
				analizeForRollbackAndCloseSession(session);
			}
					
		}
		
		/**Eliminamos Objeto de la Bd*/
		public static void saveOrUpdateObject(Object object){
			Session session = null;
			try {
				session = HibernateUtil_uniqueSession.openSesion();
				org.hibernate.Transaction tx  =  session.beginTransaction();
				session.saveOrUpdate(object);
				tx.commit();
				session.close();
			} catch (Exception e) {
				
			}finally{
				analizeForRollbackAndCloseSession(session);
			}	
	}		

}