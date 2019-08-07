package ni.gob.minsa.simlab.sistema.services;

import java.util.ArrayList;
import java.util.Collection;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.sistema.hibernate.bussines.DetParam;

import org.hibernate.Session;

public class SimlabParameterService {
	public enum CatalogParam{
		TYPE_MSN("LIST_MSN"),
		TYPE_MENU("LIST_MNU"),
		MENU_HEADER("PRINC_MNU"),
		SI_NO("SI_NO"),
		MOD_SYSTEM("LIST_MOD"),
		LIST_EQUIP("LIST_EQUIP"),
		LIST_PATRON("LIST_CODPAT"),
		POS_NEG("POS_NEG"),
		VOL_ALIC("VOL_ALIC"),
		LIST_USO("LIST_USO_ALIC"),
		LIST_USO_CAJA("LIST_PROPO"),	
		LIST_EST_PBMC("LIST_PBMC");
				
		String codeParam = null;
		
		CatalogParam(String codeParam) {
			this.codeParam = codeParam;
		}
		
		public String getCodeParam(){
			return codeParam;
		}
	}
	
	/**Obtenemos la lista de Parametros mediante el Codigo*/
	@SuppressWarnings("unchecked")
	public static Collection<DetParam> getListDetParam(String codeParam){
		Collection<DetParam> listParam = new ArrayList<DetParam>();
		Session session = null;
		try {
				session = HibernateUtil.openSesion();
				listParam = session.createQuery(
						"FROM DetParam WHERE COD_CAT = ?").setString(0, codeParam).list();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(SimlabParameterService.class, exception);
			}
		return listParam;
	}
	/**Obtenemos el Codigo de Parametro, tomando como base el Codigo del Catalogo y el Numero de orden*/
	public static String getParameterCode(CatalogParam catalogParam, int orde){
		return SimlabParameterService.getItemParam(catalogParam, orde).getId().getCodItem();
		}
	
	/**Obtenemos la lista de Detalle de Parametros*/
	public static Collection<DetParam> getListDetParam(CatalogParam catalogParam){
		return getListDetParam(catalogParam.getCodeParam());
	}
	
	public static DetParam getItemParam(CatalogParam catalogParam, int nroOrde){
		return getItemParam(catalogParam.getCodeParam(), nroOrde);
	}
	
	public static DetParam getItemParam(String codeParam, int nroOrde){
		DetParam itemParam = null;
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				itemParam =  (DetParam) session.createQuery("FROM DetParam where COD_CAT = ? AND NRO_ORDE = ? ")
						.setString(0, codeParam).setInteger(1, nroOrde).uniqueResult();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(SimlabParameterService.class, exception);
			}
		return itemParam;
		
	}
	
	public static void saveDetParam (Session session, DetParam detParam) throws SimlabAppException{
		try {
			session.beginTransaction();
			session.saveOrUpdate(detParam);
			session.getTransaction().commit();
		}
		catch (Exception e) {
			throw SimlabAppException.generateExceptionByUpdate(SimlabParameterService.class, e);
		}
	}
}
