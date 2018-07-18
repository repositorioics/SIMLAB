package ni.gob.minsa.simlab.sistema.hibernate;

import javax.ejb.Startup;
import javax.inject.Singleton;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

@Singleton
@Startup
public class HibernateUtil {
	private static final String HIBERNATE_CONFIG_FILE = "ni/gob/minsa/simlab/sistema/hibernate/hibernate.cfg.xml";
//	private static final String JNDI_NAME = "java:global/SessionFactory";
	private static final SessionFactory sesionFactory = buildSessionFactory();
		
	private static SessionFactory buildSessionFactory(){
			try {
					Configuration configuration = new Configuration();
					configuration.configure(HIBERNATE_CONFIG_FILE);
					ServiceRegistry serviceRegistry =  new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
					return configuration.buildSessionFactory(serviceRegistry);
//					sessionFactory = new Configuration().configure(HIBERNATE_CONFIG_FILE).buildSessionFactory();
				} catch (Throwable exception) {
					exception.getCause();
					throw new ExceptionInInitializerError(exception);
				}
			}

	private static SessionFactory getSessionFactory(){
			return sesionFactory;			
		}
		
		public static Session openSesion(){
			Session session = null;
			try {
				session = getSessionFactory().openSession();
			} catch (Exception e) {
			
			}
			return session;
		}
		
		public static void closeSession(Session session){
			if(session == null||!session.isOpen())session.close();
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
				session = HibernateUtil.openSesion();
				org.hibernate.Transaction tx = session.beginTransaction();
				session.save(object);
				tx.commit();
				session.close();
			} catch (Exception e) {
				
			}finally{analizeForRollbackAndCloseSession(session);}
		}
		
		/**actualizamos Objeto en BD*/
		public static void updateObject(Object object){
			Session session = null;
			try {
				session = HibernateUtil.openSesion();
				org.hibernate.Transaction tx = session.beginTransaction();
				session.update(object);
				tx.commit();
				session.close();
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				analizeForRollbackAndCloseSession(session);
			}
		}
				
		/**Eliminamos Objeto de la Bd*/
		public static void deleteObject(Object object){
			Session session = null;
			try {
				session = HibernateUtil.openSesion();
				org.hibernate.Transaction tx  =  session.beginTransaction();
				session.delete(object);
				tx.commit();
				session.close();
			} catch (Exception e) {
				
			}finally{
				analizeForRollbackAndCloseSession(session);
			}
					
		}
		
		/**Eliminamos Objeto de la Bd*/
		public static void saveOrUpdateObject(Object object){
			Session session = null;
			try {
				session = HibernateUtil.openSesion();
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