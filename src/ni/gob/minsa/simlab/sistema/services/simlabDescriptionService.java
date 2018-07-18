package ni.gob.minsa.simlab.sistema.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.DetParam;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;
import ni.gob.minsa.sistema.hibernate.bussines.Transacs;

import org.hibernate.Session;

public class simlabDescriptionService {
	
	public simlabDescriptionService(){
		super();
	}
	
	@SuppressWarnings("unchecked")
	private static Collection<DetParam> getListParamDetFromDb(String codList) throws SimlabAppException{
		Session session = null;
		Collection<DetParam> listParamDet = new ArrayList<DetParam>();
		try {
			session = HibernateUtil.openSesion();
			listParamDet = session.createSQLQuery(
					"SELECT * FROM det_param  where COD_CAT = ? order by nro_orde").addEntity(DetParam.class)
					.setString(0, codList).list();	
				session.close();
			} catch (Throwable exception) {
				throw SimlabAppException.generateExceptionBySelect(DetParam.class,exception);
			}
		return listParamDet;
	}
	
	/**
	 * Metodo para castear una lista y todos los elementos que esten dentro de ella, este metodo hace mas limpio
	 * y "normaliza" los datos con un cast seguro evitando el error @SuppressWarnings("unchecked")
	 * @param clazz clase de donde obtendras los datos
	 * @param c Lista de datos del tipo de la Clazz
	 * @return Retorna la Lista(Collection) con todos sus valores casteados
	 */
	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c)
	{
		List<T> r = new ArrayList<T>(c.size());
		for(Object o : c)
			r.add(clazz.cast(o));
		return
				r;
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<String> getCodListRegAlic() throws SimlabAppException
	{
		Session session = null;
		Collection<String> listAlic = new ArrayList<String>();
		try
		{
			session = HibernateUtil.openSesion();
			listAlic = session.createSQLQuery("select cod_alic from reg_alic").list();
			session.close();
		}catch(Throwable ex)
		{
			throw SimlabAppException.generateExceptionBySelect(RegAlic.class, ex);
		}
		return
				listAlic;
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<String> getListAlic() throws SimlabAppException{
		Session session = null;
		Collection<String> listAlic = new ArrayList<String>();
		try{
			session = HibernateUtil.openSesion();
			listAlic = session.createSQLQuery("Select tipo_alicuota from cat_alicuotas").list();
			session.close();
		}catch(Throwable hEx){
			throw SimlabAppException.generateExceptionBySelect(RegAlic.class, hEx);
		}
		return
				listAlic;
	}
	
	public static DetParam getObjectParamDet(String codList, String description) throws SimlabAppException{
		Session session = null;
		DetParam listParamDet = null;
		try {
			session = HibernateUtil.openSesion();
			listParamDet = (DetParam) session.createSQLQuery(
					"SELECT * FROM det_param  where COD_CAT = ? and Desc_item = ?").addEntity(DetParam.class)
					.setString(0, codList).setString(1, description).uniqueResult();	
				session.close();
			} catch (Throwable exception) {
				throw SimlabAppException.generateExceptionBySelect(DetParam.class,exception);
			}
		return listParamDet;		
	}
		
	private static Collection<String> getListDescription(String codList, boolean emptyString) throws SimlabAppException{
		Collection<String> listDescription = new ArrayList<String>();	
		if(!getListParamDetFromDb(codList).isEmpty()){
					for (DetParam detParam: getListParamDetFromDb(codList)) {
						if(listDescription.isEmpty()){
						if(emptyString)listDescription.add(simlabStringUtils.EMPTY_STRING);
						}
						listDescription.add(detParam.getDescItem());
					}
			}
		return listDescription;
	}
	
	public static String getCodeParamDet(String codList, String description) throws SimlabAppException{
		String code = null;
		if(getObjectParamDet(codList, description)!=null){
			code = getObjectParamDet(codList, description).getId().getCodItem();
		}
		return code;
	}
	
	/**Obtenemos los parametros de Descripcion para SI_NO*/
	public static Collection<String> getListSiNo() throws SimlabAppException{
		return getListDescription("SI_NO", false);
	}
	
	public static Collection<String> getListSiNoEmpty() throws SimlabAppException{
		return getListDescription("SI_NO", true);
	}

	public static Collection<String> getListPositiveOrNegativeEmpty() throws SimlabAppException{
		return getListDescription("POS_NEG",true);
		}

	public static Collection<String> getListCondicion() throws SimlabAppException{
		return getListDescription("LIST_CONDICION",true);
		}
	
	public static Collection<String> getListLab() throws SimlabAppException{
		return getListDescription("LIST_LAB",true);
		}	
	
	public static Collection<String> getListPermittedVol()throws SimlabAppException{
		return getListDescription("VOL_ALIC",true);
		}
	
	/**Obtenemos los parametros de Descripcion para Modulos
	 * @throws SimlabAppException */
	public static Collection<String> getListModules() throws SimlabAppException{
		return getListDescription("LIST_MOD", false);
	}
	
	//Obtenemos la lista de Trasacciones para el MenuSelect 
		public static Collection<String> getListTransactionToAdd() throws SimlabAppException{
			Collection<String> ListTransactionToAdd = new ArrayList<String>();
				for (Transacs transacs: SimlabUserService.getListTx()) {
					if(ListTransactionToAdd.isEmpty())
						ListTransactionToAdd.add(simlabStringUtils.EMPTY_STRING);				
					ListTransactionToAdd.add(transacs.getDescTrans());
				}
			return ListTransactionToAdd;
		}
		
	public static Collection<String> getListMod(String codMod) throws SimlabAppException{
		Collection<String> listMod =  new ArrayList<String>();
			for (DetParam detParam: getListParamDetFromDb(codMod)) {
				if(!simlabStringUtils.areEquals(detParam.getId().getCodItem(), "m0_main")){
					if(listMod.isEmpty())listMod.add(simlabStringUtils.EMPTY_STRING);
					listMod.add(detParam.getDescItem());
					}
				}
		return listMod;	

		}	
	
	/**Obtenemos el Codigo de un Elemento mediante el Catalogo y la Descripcion*/
	public static String getCodeByDescriptionAndEstudy(String catalog, String itemDescription) throws SimlabAppException{
		String code = null;
			for (DetParam detParam: simlabDescriptionService.getListParamDetFromDb(catalog)) {
					if(simlabStringUtils.areEquals(detParam.getDescItem(), itemDescription))
						code = detParam.getId().getCodItem();}
			return code;
	}
	
	/**Obtenemos los parametros de Descripcion para Modulos con una Cadena Vacia Inicialmente*/
	public static Collection<String> getListModulesWithEmptyString() throws SimlabAppException{
		return getListMod("PRINC_MNU");
	}
	
	public static Collection<String> getListTypeFreezer() throws SimlabAppException{
		return simlabDescriptionService.getListDescription("LIST_EQUIP", true);
	}
	
	/**Obtenemos la lista de Estudios*/
	public static Collection<String> getListEstudy() throws SimlabAppException{
			return simlabDescriptionService.getListDescription("LIST_EST", true);
	}
	
	/**Obtenemos la lista de tipos de estudio*/
	public static Collection<String> getListStudy() throws SimlabAppException{
			return simlabDescriptionService.getListDescription("LIST_EST", true);
	}
	
	public static Collection<String> getListUseAlic() throws SimlabAppException{
			return simlabDescriptionService.getListDescription("LIST_USO_ALIC", false);
	}
	
	public static Collection<String> getListUseAlicWithEmpty() throws SimlabAppException{
		return simlabDescriptionService.getListDescription("LIST_USO_ALIC", true);
	}
	
	/**Obtenemos la lista de tipos de estudio*/
	public static Collection<String> getListStudyCaja() throws SimlabAppException{
			return simlabDescriptionService.getListDescription("LIST_EST_CAJA", true);
	}
		
/**********************************Agregado*******************************************/
	
	/**Obtenemos la lista de Tipo de Alicutoas*/
	public static Collection<String> getLisTypeAlic(String codeCategoria) throws SimlabAppException{
			return simlabDescriptionService.getListDescription(codeCategoria, true);
	}
		
	public static Collection<String> getListTypeMuestByAlic(String codeCategoria) throws SimlabAppException{
			return simlabDescriptionService.getListDescription(codeCategoria, true);
	}
		
	public static Collection<String> getListUsoAlic(String codeCategoria) throws SimlabAppException{
		return simlabDescriptionService.getListDescription(codeCategoria, true);
	}
	
	public static Collection<String> getListEstudioAlic(String codeCategoria) throws SimlabAppException{
		return simlabDescriptionService.getListDescription(codeCategoria, true);
	}
	
	public static Collection<String> getListMuestPertAlic(String codeCategoria)throws SimlabAppException{
		return simlabDescriptionService.getListDescription(codeCategoria, true);
	}

	public static Collection<String> getListPosNeg(String codeCategoria)throws SimlabAppException{
		return simlabDescriptionService.getListDescription(codeCategoria, true);
	}
}