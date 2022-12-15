package ni.gob.minsa.simlab.sistema.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.model.AlicuotaModel;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabNumberUtil;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Caja;
import ni.gob.minsa.sistema.hibernate.bussines.CajaId;
import ni.gob.minsa.sistema.hibernate.bussines.CatAlicuotas;
import ni.gob.minsa.sistema.hibernate.bussines.CatAlicuotasId;
import ni.gob.minsa.sistema.hibernate.bussines.Freezer;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;
import ni.gob.minsa.sistema.hibernate.bussines.RackId;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;

import org.hibernate.Query;
import org.hibernate.Session;

public class SimlabEquipService {
	
	public SimlabEquipService(){
		super();
		}
	
	/**Obtenemos el Objeto Caja en Dependencia del Estudio*/
	public static Caja getCajaByCode(String codigoCaja){
		Caja caja =null;
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				caja  =  (Caja) session.createSQLQuery("select * from caja where COD_CAJA = ?")
						.addEntity(Caja.class).setString(0, codigoCaja).uniqueResult();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
			}
		return caja;
	}
	
	/**Obtenemos la lista de Caja en Dependencia del Rack*/
	@SuppressWarnings("unchecked")
	public static Collection<Caja> getCajaByRack(String rack) throws SimlabAppException{
		Session session =null;
		Collection<Caja> listCaja = new ArrayList<Caja>();
		try {
			session = HibernateUtil.openSesion();
			listCaja = session.createSQLQuery("select caja.* from caja, rack WHERE CCOD_RACK = COD_RACK and ccod_rack = ? order by POS_RACK")
					.addEntity(Caja.class).setString(0, rack).list();
		} catch (Throwable e) {
			throw SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return listCaja;
	}
	
	//Obtenemos la posicion Vacia en el Rack para la proxima Caja.
	public static Integer getPositionInRack(String codeRack) throws SimlabAppException{
		//Retornamos la Primera Posicion en Caso de que el Rack se encuentre Vacio
		if(SimlabEquipService.getCajaByRack(codeRack).isEmpty())
			return 1;
		//Recorremos la lista de Cajas Obtenidas
		int count = 1;
		for (Caja caja : SimlabEquipService.getCajaByRack(codeRack)) {
			int post = caja.getPosRack();
			if (count == post){
				count++;
				continue;
			}
		} 
		return count;
	}
	
	/**Retornamos la lista de Freezer Disponibles
	 * @throws SimlabAppException */
	@SuppressWarnings("unchecked")
	public static Collection<Freezer> getListFreezer() throws SimlabAppException{
		Collection<Freezer> listFreezer  =  new ArrayList<Freezer>();
		Session session = null;
			try {
				session =  HibernateUtil.openSesion();
						listFreezer = session.createQuery("from Freezer where ? between FEINIVIG AND FEFINVIG")
								.setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).list();
				session.close();
			} catch (Throwable exception) {
				throw SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, exception);
			}
		return listFreezer;
	}
	
	/**Obtenemos una lista de Freezer en dependencia del Uso y de la Temperatura*/
	@SuppressWarnings("unchecked")
	public static Collection<Freezer> getListFreezerByUseAndTemp(String use, int temp){
		Collection<Freezer> listFreezer = new ArrayList<Freezer>();
		Session session = null;
		try {
			use = '%' + use + '%';
			session = HibernateUtil.openSesion();
			listFreezer = session.createSQLQuery("select * from freezer where USO_ALM like ? and ? between temp_max and temp_min and ? between FEINIVIG AND FEFINVIG order by cod_freezer")
					.addEntity(Freezer.class).setString(0, use).setInteger(1, temp).setTimestamp(2, SimlabDateUtil.getCurrentTimestamp()).list();
			session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
			}
		return listFreezer;
	}
	
	/**Obtenemos un Objeto de Tipo Freezer*/
	public static Freezer getObjectFreezer(int codeFreezer){
		Session session = null;
		Freezer freezer = null;
			try {
				session = HibernateUtil.openSesion();						
				freezer = (Freezer) session.createQuery("FROM Freezer where codFreezer = ?")
						.setInteger(0, codeFreezer).uniqueResult();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
			}
			return freezer;
	}
	
	/**Registramos en la BD 
	 * @throws SimlabAppException */
	public static void addEquip(Session session, int codFreezer, Date feIniVig, Date feFinVig, int tempMin, int tempMax, String usoAlm, int capAlm, String typeEquip,
			String serieEquip, String modeloEquip, String nameResp, Integer nroFila,Integer nroCol) throws SimlabAppException{
			try {
				Integer formatNroFila = SimlabNumberUtil.isNull(nroFila)?0:nroFila;
				Integer formatNroCol = SimlabNumberUtil.isNull(nroCol)?0:nroCol;
					Freezer freezerToAdd = new Freezer();
					freezerToAdd.setCodFreezer(codFreezer);
					freezerToAdd.setFeinivig(feIniVig);
					freezerToAdd.setFefinvig(feFinVig);
					freezerToAdd.setTempMin(tempMin);
					freezerToAdd.setTempMax(tempMax);
					freezerToAdd.setUsoAlm(usoAlm);
					freezerToAdd.setCapAlm(capAlm);
					freezerToAdd.setTipAlm(typeEquip);
					freezerToAdd.setEquipSerie(serieEquip);
					freezerToAdd.setEquipModelo(modeloEquip);
					freezerToAdd.setNameResp(nameResp);
					freezerToAdd.setNroFila(formatNroFila);
					freezerToAdd.setNroColumn(formatNroCol);
					//Guardamos el Objeto Freezer
					session.save(freezerToAdd);
			} catch (Exception e) {
				throw SimlabAppException.generateExceptionByInsert(SimlabEquipService.class, e);
			}
	}
	
	public static void updateEquip(Session session, int codFreezer, int tempMin, int tempMax, String alicAlm, int capAlm, String typeEquip,
			String serieEquipo, String modeloEquip, String nameResponsable, Integer nroFila, Integer nroCol) throws SimlabAppException{
		try {
			Query query = session.createQuery("UPDATE Freezer SET COD_FREEZER =? , TEMP_MIN =  ?, " +
					"TEMP_MAX = ?, USO_ALM= ?, CAP_ALM = ?, TIP_ALM=?," +
					" EQUIP_SERIE= ?, EQUIP_MODELO =?, NAME_RESP = ?, NRO_FILA = ?, NRO_COLUMN = ? " +
					" WHERE  COD_FREEZER =?").setInteger(0, codFreezer).setInteger(1, tempMin)
					.setInteger(2, tempMax).setString(3, alicAlm).setInteger(4, capAlm).setString(5, typeEquip)
					.setString(6, serieEquipo).setString(7, modeloEquip).setString(8, nameResponsable).setInteger(9, nroFila).setInteger(10, nroCol)
					.setInteger(11, codFreezer);
			query.executeUpdate();
		} catch (Throwable e) {
			throw SimlabAppException.generateExceptionByUpdate(SimlabEquipService.class, e);
		}
	}
	
	/**Cerramos la Vigencia de Equipo*/
	public static void closeVigency(Session session, String motCloseVigency, int codFreezer){
		try {
			Query queryExcuteUpdate = session.createQuery("UPDATE Freezer SET FEFINVIG = ?, MOT_CLOSE = ? where COD_FREEZER  = ? ORDER BY COD_FREEZER")
					.setDate(0, SimlabDateUtil.getCurrentDate()).setString(1, motCloseVigency).setInteger(2, codFreezer);
			queryExcuteUpdate.executeUpdate();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionByUpdate(SimlabEquipService.class, exception);
		}
	}
	
	/**Obtenemos una lista de Freezer en dependencia del Tipo de Alicuotas que almacenan*/
	@SuppressWarnings("unchecked")
	public static Collection<Freezer> getListFreezer(String usoOrAlicAlm){
		Collection<Freezer> listFreezer = new ArrayList<Freezer>();
			Session session  = null;
			try {
				session = HibernateUtil.openSesion();
				listFreezer = session.createSQLQuery("select * from freezer where ? between FEINIVIG AND FEFINVIG 	AND USO_ALM=?")
						.addEntity(Freezer.class).setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).setString(1, usoOrAlicAlm).list();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
			}
		return listFreezer;				
	}
	
	/**Obtenemos una lista de Freezer de PBMC*/
	@SuppressWarnings("unchecked")
	public static Collection<Object> getListFreezerPBMC(){
		Collection<Object> listFreezer = new ArrayList<Object>();
			Session session  = null;
			try {
				session = HibernateUtil.openSesion();
				listFreezer = session.createSQLQuery("SELECT freezer.COD_FREEZER, freezer.USO_ALM, freezer.CAP_ALM, Count(rack.COD_RACK) AS total, freezer.CAP_ALM- Count(rack.COD_RACK) as libres, " + 
						"freezer.NAME_RESP FROM freezer LEFT JOIN rack ON freezer.COD_FREEZER = rack.RCOD_FREEZER " + 
						"GROUP BY freezer.COD_FREEZER, freezer.USO_ALM, freezer.CAP_ALM HAVING (((freezer.USO_ALM)='PBMC'));").list();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
			}
		return listFreezer;				
	}
	
	/**Obtenemos una lista de Freezer de PBMC*/
	@SuppressWarnings("unchecked")
	public static Collection<Integer> getListCodesFreezerPBMC(){
		Collection<Integer> listFreezer = new ArrayList<Integer>();
			Session session  = null;
			try {
				session = HibernateUtil.openSesion();
				listFreezer = session.createSQLQuery("SELECT freezer.COD_FREEZER FROM freezer LEFT JOIN rack ON freezer.COD_FREEZER = rack.RCOD_FREEZER " + 
						"GROUP BY freezer.COD_FREEZER, freezer.USO_ALM, freezer.CAP_ALM HAVING (((freezer.USO_ALM)='PBMC'));").list();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
			}
		return listFreezer;				
	}
	
	/**Obtenemos una lista de Rack del Freezer*/
	@SuppressWarnings("unchecked")
	public static Collection<Object> getListRackPBMC(Integer tanqueId){
		Collection<Object> listFreezer = new ArrayList<Object>();
			Session session  = null;
			try {
				session = HibernateUtil.openSesion();
				listFreezer = session.createSQLQuery("SELECT rack.RCOD_FREEZER, rack.COD_RACK, rack.POS_FREEZER, rack.CAP_ALM, Count(caja.COD_CAJA) AS usados, rack.CAP_ALM-Count(caja.COD_CAJA) AS libres " + 
						"FROM caja RIGHT JOIN (rack INNER JOIN freezer ON rack.RCOD_FREEZER = freezer.COD_FREEZER) ON caja.CCOD_RACK = rack.COD_RACK " + 
						"GROUP BY rack.COD_RACK, rack.RCOD_FREEZER, rack.CAP_ALM, rack.POS_FREEZER " + 
						"HAVING (((rack.RCOD_FREEZER)= " + tanqueId + "));").list();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
			}
		return listFreezer;				
	}
	

	public static boolean isMaxCapacFreezer(int codigoFreezer){
		boolean state = false;
		Freezer freezer = getObjectFreezer(codigoFreezer);
		/*si el count de la tabla rack x freezer es igual o mayor a la capacidad de almacenamiento del
		 * freezer indica que se encuentra lleno
		 * */
		if (countEquipFreezer(codigoFreezer) >= freezer.getCapAlm()) {
			state = true;
		}else {
			state = false;
		}
		return state;
	}
	private static int countEquipFreezer(int codigo){
		int cont = 0;
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "";
			q = "select rack from Rack as rack where rcod_freezer = :cod";
			//q = "select caja from Caja as caja where ccod_rack = :cod";
			Query query = session.createQuery(q);
			query.setParameter("cod", codigo);
			if (query.list() != null) {
				cont = query.list().size();
			}
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabAppException.class, e);
		}
		return cont;
	}
	public static int getTempFreezer(int codeFreezer) throws SimlabAppException{
		
		int temp = 0;
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "select f.tempMax from freezer f where f.codFreezer = :codigo";
			Query query = session.createQuery(q);
			query.setParameter("codigo", codeFreezer);
			temp = Integer.parseInt(query.list().get(0).toString());
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return temp;
	}
	@SuppressWarnings("unchecked")
	public static Collection<Freezer> getListFreezerInactive()throws SimlabAppException{
		Session session = null;
		Collection<Freezer> lst = new ArrayList<Freezer>();
		try {
			String q = "select * from freezer where FEFINVIG <= ?";
			session = HibernateUtil.openSesion();
			lst = session.createSQLQuery(q).addEntity(Freezer.class).setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).list();
		session.close();
		}catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lst;
	}	
	/**********************************Metodos Equipo Rack*******************************************/

	
	/**Obtenemos una lista de Freezer que Contienen Rack en dependencia del Uso*/
	@SuppressWarnings("unchecked")
	public static Collection<Freezer> getListFreezerWithRack(String usoOrAlicAlm){
		Session session  = null;
		Collection<Freezer> ListFreezerWithRack = null;
			try {
				session= HibernateUtil.openSesion();
				ListFreezerWithRack = session.createSQLQuery("select distinct freezer.* from freezer, rack " +
						"where ? between FEINIVIG AND FEFINVIG 	AND USO_ALM=? and COD_FREEZER = RCOD_FREEZER;")
						.addEntity(Freezer.class).setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).setString(1, usoOrAlicAlm).list();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, exception);
			}
			return ListFreezerWithRack;
	}
	
	@SuppressWarnings("unchecked")
	/**Obtenemos una lista completa de Rack*/
	public static Collection<Rack> getlistRack()throws SimlabAppException{
		Collection<Rack> lstRack = new ArrayList<Rack>();
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String query = "select r.rcod_Freezer,r.cod_rack,r.cap_Alm,r.pos_Freezer,r.fe_Ini,r.fe_fin from rack r inner join freezer f on r.rcod_freezer = f.cod_freezer" +
					 " where ? between r.fe_ini and r.fe_fin;";
			lstRack = session.createSQLQuery(query).addEntity(Rack.class).setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).list();
			session.close();
		} catch (Throwable e) {
			throw SimlabAppException.generateExceptionBySelect(Rack.class, e);
		}
		return lstRack;		
	}
	
	/**Obtenemos una lista de Rack en dependencia del Codigo de Freezer*/
	@SuppressWarnings("unchecked")
	public static Collection<Rack> getListRackByFreezer(int codeFreezerPert)throws SimlabAppException{
		Collection<Rack> listRackByFreezer = new ArrayList<Rack>();
		Session session  = null;
		try {
				session = HibernateUtil.openSesion();
				listRackByFreezer = session.createSQLQuery("" +
						"SELECT * FROM rack WHERE ? BETWEEN FE_INI AND FE_FIN AND RCOD_FREEZER = ? ORDER BY COD_RACK")
						.addEntity(Rack.class).setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).setInteger(1, codeFreezerPert).list();
				//Cerramos Session
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, exception);
			}
		return listRackByFreezer;
	}
	
	/**Obtenemos una lista de cajas en dependencia del Codigo de busqueda*/
	@SuppressWarnings("unchecked")
	public static Collection<Caja> getListCajasByCode(String code)throws SimlabAppException{
		Collection<Caja> listCaja = new ArrayList<Caja>();
		Session session  = null;
		try {
				session = HibernateUtil.openSesion();
				listCaja = session.createSQLQuery("" +
						"SELECT * FROM caja WHERE ? BETWEEN FE_INI AND FE_FIN AND COD_CAJA = ? ORDER BY COD_CAJA")
						.addEntity(Caja.class).setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).setString(1, code).list();
				//Cerramos Session
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, exception);
			}
		return listCaja;
	}
	
	/**Obtenemos una lista de Rack que Contienen Cajas en dependencia del Codigo de Freezer*/
	@SuppressWarnings("unchecked")
	public static Collection<Rack> getListRackWithBoxByFreezer(int codeFreezerPert)throws SimlabAppException{
		Collection<Rack> listRackByFreezer = new ArrayList<Rack>();
		Session session  = null;
		try {
				session = HibernateUtil.openSesion();
				listRackByFreezer = session.createSQLQuery("" +
						"select rack.* from rack, caja, freezer where ? between rack.FE_INI AND rack.FE_FIN " +
						"and ? between caja.FE_INI AND caja.FE_FIN " +
						"and caja.CCOD_RACK = rack.COD_RACK and rack.RCOD_FREEZER = freezer.COD_FREEZER " +
						"and RCOD_FREEZER = ? ORDER BY rack.COD_RACK")
						.addEntity(Rack.class).setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).setTimestamp(1, SimlabDateUtil.getCurrentTimestamp())
						.setInteger(2, codeFreezerPert).list();
				//Cerramos Session
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, exception);
			}
		return listRackByFreezer;
	}
	
	/**Agregamos a la Base de Datos un Equipo de Tipo Rack*/
	public static void addEquipRack(Session session, String codeRack, int codeFreezer, int capAlm, int posFreezer, Date fechaIni, Date fechaFin){
		try {
			RackId rId = new RackId();rId.setCodRack(codeRack);rId.setRcodFreezer(codeFreezer);
			Rack r = new Rack();r.setCapAlm(capAlm);r.setPosFreezer(posFreezer);r.setId(rId); r.setFeIni(fechaIni);r.setFeFin(fechaFin);
			session.save(r);
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByInsert(SimlabEquipService.class, e);
		}
	}
	
	public static void updateEquipRack(Session session, String codeRack, int codeFreezer, int capAlm, int posFreezer){
		try {
			String q = "UPDATE rack set cap_alm = ?, pos_freezer = ? where cod_rack = ? and rcod_freezer = ?";
			Query query = session.createSQLQuery(q).addEntity(Rack.class).setInteger(0, capAlm).setInteger(1, posFreezer)
					.setString(2, codeRack).setInteger(3, codeFreezer);
			query.executeUpdate();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByUpdate(SimlabEquipService.class, e);
		}
	}
	
	public static Rack getRack(int codFreezer, String codeRack){
		Session session = null;
		Rack rack = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "select r.rcod_freezer,r.cod_rack,r.cap_alm,r.fe_ini,r.fe_fin,r.pos_freezer from rack r where r.rcod_freezer = ? and r.cod_rack = ?";
			rack = (Rack) session.createSQLQuery(q).addEntity(Rack.class).setInteger(0, codFreezer).setString(1, codeRack).uniqueResult();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return rack;
	}
	
	public static Rack getRack(String codeRack){
		Session session = null;
		Rack rack = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "select * from rack r where r.cod_rack = ?";
			rack = (Rack) session.createSQLQuery(q).addEntity(Rack.class).setString(0, codeRack).uniqueResult();
		session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return rack;
	}
	
	public static Rack verifCodRack(String codRack, int codFreezer){
		Session session = null;
		Rack rack = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "select r.rcod_freezer,r.cod_rack,r.cap_alm,r.fe_ini,r.fe_fin,r.pos_freezer from rack r where r.cod_rack = ? and rcod_freezer = ?";
			rack = (Rack) session.createSQLQuery(q).addEntity(Rack.class).setString(0, codRack).setInteger(1, codFreezer).uniqueResult();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return rack;
	}
	
	public static boolean isPositionAvailableInRack(int _position, int _code){
		boolean isValid = false;
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "";
			q = "from Rack r where r.posFreezer = :position and r.id.rcodFreezer = :codigo";
			Query query = session.createQuery(q);
			query.setParameter("position", _position);
			query.setParameter("codigo", _code);
			if (query.list().size() == 0) {
				isValid = true;
			}else {
				isValid = false;
			}
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabAppException.class, e);
		}		
		return isValid;
	}
	public static boolean isMaxCapcRack(int codigoFreezer, String codigoRack){
		boolean state = false;
		Collection<Caja> c = getListCajaByRackAndFreezer(codigoFreezer, codigoRack);
		if (c.size() >= getCapAlmRack(codigoFreezer, codigoRack)) {
			state = true;
		}else {
			state = false;
		}
		return state;
	}
	public static boolean isMaxCapcRack(String codigoRack) throws SimlabAppException{
		boolean state = false;
		Collection<Caja> c = getListCajaByRack(codigoRack);
		if (c.size() >= getCapAlmRack(codigoRack)) {
			state = true;
		}else {
			state = false;
		}
		return state;
	}
	private static int getCapAlmRack(int codigoFreezer, String codigoRack){
		int cont = 0;
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String query = "select r.* from rack r inner join freezer f on f.cod_freezer = r.rcod_freezer where f.cod_freezer = ? and r.cod_rack = ?";			
			Rack r = (Rack) session.createSQLQuery(query).addEntity(Rack.class).setInteger(0, codigoFreezer).setString(1, codigoRack).uniqueResult();
			cont = r.getCapAlm();
			session.close();			
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabAppException.class, e);
		}
		return cont;

	}
	
	private static int getCapAlmRack(String codigoRack){
		int cont = 0;
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String query = "select * from rack r where r.cod_rack = ?";			
			Rack r = (Rack) session.createSQLQuery(query).addEntity(Rack.class).setString(0, codigoRack).uniqueResult();
			cont = r.getCapAlm();
			session.close();			
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabAppException.class, e);
		}
		return cont;

	}
	
	public static void closeVigencyRack(Session session, String rackCode){
		try {
			String q = "UPDATE rack set fe_fin = ? where cod_rack = ?";
			Query query = session.createSQLQuery(q).setDate(0, SimlabDateUtil.getCurrentDate()).setString(1, rackCode);
			query.executeUpdate();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByUpdate(SimlabEquipService.class, e);
		}
	}
	
	/**********************************Metodos Equipo Caja*******************************************/
	@SuppressWarnings("unchecked")
	public static Collection<Caja> getlistCaja()throws SimlabAppException{
		Collection<Caja> lstcaja = new ArrayList<Caja>();
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String query = "select * from caja c";
			lstcaja = session.createSQLQuery(query).addEntity(Caja.class).list();
			session.close();
		} catch (Throwable e) {
			throw SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lstcaja;		
	}
	
	public static Integer getUltimaCaja()throws SimlabAppException{
		Integer numCaja;
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String queryStr = "select max(cod_caja) as numCaja from caja";
			Query query  = session.createSQLQuery(queryStr);
			numCaja = (Integer) query.uniqueResult();
			session.close();
		} catch (Throwable e) {
			throw SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}

		return numCaja;		
	}
	
	/**Obtenemos una lista de Cajas en dependencia del Codigo de Rack*/
	@SuppressWarnings("unchecked")
	public static Collection<Caja> getListBoxByRack(String codeRack){
		Collection<Caja> listBox = new ArrayList<Caja>();
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			listBox = session.createSQLQuery("select * from caja where ? between FE_INI AND FE_FIN AND CCOD_RACK = ? ORDER BY COD_CAJA")
					.addEntity(Caja.class).setTimestamp(0, SimlabDateUtil.getCurrentTimestamp()).setString(1, codeRack).list();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, exception);
		}
		return listBox;
	}
	
	public static void addEquipCaja(Session session, String codeCaja, String codeRack, int positionRack,
			String usoAlic, String tipMuestra, Date iniVigencia, Date finVigencia, int temp, String uso, int capAlmac, String posNeg, String estudio){
		try {
			CajaId cId = new CajaId(); cId.setCodCaja(codeCaja); cId.setCcodRack(codeRack);
			Caja c = new Caja(); c.setId(cId); c.setPosRack(positionRack); c.setUsoAlic(usoAlic); c.setTipMues(tipMuestra); 
			c.setFeIni(iniVigencia); c.setFeFin(finVigencia); c.setTempAlm(temp); c.setUso(uso); c.setCapAlm(capAlmac);c.setEstudio(estudio);
			c.setPosNeg(posNeg);
			session.save(c);
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByInsert(SimlabEquipService.class, e);
		}		
	}
	
	public static void updateEquipCaja(Session session, String codeCaja, String codeRack, int positionRack,
			String usoAlic, String tipMuestra, Date iniVigencia, Date finVigencia,int temp, String uso, int capAlmac, String posNeg, String estudio){
		try {
			CajaId cId = new CajaId(); cId.setCodCaja(codeCaja); cId.setCcodRack(codeRack);
			Caja c = new Caja(); c.setId(cId); c.setPosRack(positionRack); c.setUsoAlic(usoAlic); c.setTipMues(tipMuestra); 
			c.setFeIni(iniVigencia); c.setFeFin(finVigencia); c.setTempAlm(temp);c.setUso(uso); c.setCapAlm(capAlmac);c.setEstudio(estudio);
			c.setPosNeg(posNeg);
			session.update(c);
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByInsert(SimlabEquipService.class, e);
		}		
	}
	
	public static void deleteCaja(Session session, Caja cajatoDelete, String codUser, String motivo){
		try {
			String codCaja = cajatoDelete.getId().getCodCaja();
			String codRack = cajatoDelete.getId().getCcodRack();
			int posRack = cajatoDelete.getPosRack();
			String codUsoAlic = cajatoDelete.getUsoAlic();
			String codTipoMuestra = cajatoDelete.getTipMues();
			Date fIni = cajatoDelete.getFeIni();
			Date fFin = cajatoDelete.getFeFin();
			int tempAlm = cajatoDelete.getTempAlm();
			String uso = cajatoDelete.getUso();
			int capAlm = cajatoDelete.getCapAlm();
			String posNeg = cajatoDelete.getPosNeg();
			
			Query query = session.createSQLQuery("INSERT INTO `caja_borrada` (`COD_CAJA`, `CCOD_RACK`, `POS_RACK`, `USO_ALIC`, `TIP_MUES`, " +
					"`FE_INI`, `FE_FIN`, `TEMP_ALM`, `USO`, `CAP_ALM`, `POS_NEG`, `MOTIVO`, `USUARIO`)" +
					" VALUES (" + codCaja + ",'" + codRack +"', " + posRack + ", '" + codUsoAlic +"', '" + codTipoMuestra +"', " +
							"'" + fIni +"', '" + fFin +"', " + tempAlm + ", '" + uso +"'," + capAlm + ", '" + posNeg +"', '"+ motivo +"','"+ codUser +"'); ");
			query.executeUpdate();
			query = session.createSQLQuery("DELETE FROM caja where cod_caja = "+ codCaja + ";");
			query.executeUpdate();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByInsert(SimlabEquipService.class, e);
		}		
	}
	
	public static Caja getCaja(String _codCaja, String _codRack){
		Session session = null;
		Caja caja = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "select c.* from caja c where c.cod_caja = ? and c.ccod_rack = ?";
			caja = (Caja) session.createSQLQuery(q).addEntity(Caja.class).setString(0, _codCaja).setString(1, _codRack).uniqueResult();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return caja;
	}
	
	public static int getAlicCaja(String _codCaja){
		Session session = null;
		int cantidad = 0;
		try {
			session = HibernateUtil.openSesion();
			String q = "select * from reg_alic where cod_box = ?";
			Query query =  session.createSQLQuery(q).setString(0, _codCaja);
			cantidad = query.list().size();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return cantidad;
	}
	
	public static boolean existCodeCaja(String _codCaja){
		boolean exist = false;
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "select c.id.codCaja, c.id.ccodRack, c.usoAlic, c.tipMues, c.feIni from Caja c where c.id.codCaja = :codCaja ";
			Query query = session.createQuery(q);
			query.setParameter("codCaja", _codCaja);
			if (query.list().size() == 0) {
				exist = false;
			}else {
				exist = true;
			}
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabAppException.class, e);
		}
		return exist;
	}
	
	public static boolean isPositionAvailableInCaja(int _position, String _code){
		boolean isValid = false;
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "";
			q = "from Caja c where c.posRack = :position and c.id.ccodRack = :codigo";
			Query query = session.createQuery(q);
			query.setParameter("position", _position);
			query.setParameter("codigo", _code);
			if (query.list().size() == 0) {
				isValid = true;
			}else {
				isValid = false;
			}
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabAppException.class, e);
		}		
		return isValid;
	}

	public static String getUsoAlicFreezer(int codeFreezer) throws SimlabAppException{
		String uso = "";
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "select f.usoAlm from Freezer f where f.codFreezer = :codigo";
			Query query = session.createQuery(q);
			query.setParameter("codigo", codeFreezer);
			uso = query.list().get(0).toString();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return uso;
	}
	/**Retorna el siguiente codigo de caja*/
	public static String getNextCodeCaja(int codeFreezer, String codeRack){
		Integer position =0;
		try {
			position =  getUltimaCaja() + 1;//getListCajaByRackAndFreezer(codeFreezer, codeRack);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return Integer.toString(position);
	}
	
	public static boolean isSameTypeAlicuota(int codeFreezer, String tipoAlic ){
		boolean isSame = false;
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String q = "select f.usoAlm from Freezer f where f.codFreezer = :codigo";
			Query query = session.createQuery(q);
			query.setParameter("codigo", codeFreezer);
			if (query.list().get(0).toString().equalsIgnoreCase(tipoAlic)) {
				isSame = true;
			}else {
				isSame = false;
			}
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabAppException.class, e);
		}
		return isSame;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Collection<CatAlicuotas> getListAlic()throws SimlabAppException{
		Collection<CatAlicuotas> lstAlic = new ArrayList<CatAlicuotas>();
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			String query = "SELECT ca.tipo_alicuota,ca.estudio_pert,ca.sep_uso,ca.temp_alm,ca.vol_perm,ca.muest_pert FROM cat_alicuotas ca";
			lstAlic = session.createSQLQuery(query).addEntity(CatAlicuotas.class).list();
			session.close();
		} catch (Throwable e) {
			throw SimlabAppException.generateExceptionBySelect(SimlabAppException.class, e);
		}
		return lstAlic;
	}
	
	public static void addAlicuota(Session session, String tipAlic, String stdAlic, int temp, String usoAlic, int volAlic, String tipMuest ){
		
		try {
			CatAlicuotasId cId = new CatAlicuotasId(); cId.setTipoAlicuota(tipAlic); cId.setEstudioPert(stdAlic);
			CatAlicuotas ca = new CatAlicuotas(); ca.setId(cId); ca.setMuestPert(tipMuest); ca.setSepUso(usoAlic); 
			ca.setTempAlm(temp);ca.setVolPerm(volAlic);
			session.save(ca);
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByInsert(SimlabAppException.class, e);
		}
	}
	
	
	
	/**Metodo para obtener la ultima posicion de la caja en la cual continuaremos el Registro*/
	public static Integer sequencePositionBox(int freezer, String rack, int caja){
		Integer position = null;
		for (RegAlic regalic: simlabAlicuotaService.getListAlicReg(freezer, rack, caja)) {
				position = regalic.getId().getPosBox();
			}
		return position;
	}
	
	
	/**Metodo que indica si una caja se encuentra llena*/
	public static boolean boxIsFull(Caja objectBox, int freezer, String rack, int caja){
		boolean isFull = false;
		Collection<RegAlic> listAlicReg = new ArrayList<RegAlic>();
		//Obtenemos la lista de Alicuotas registradas por Freezer, Rack y Caja
		listAlicReg = simlabAlicuotaService.getListAlicReg(freezer,rack, caja);
		//Validamos si la capacidad de almacenamiento de la caja es mayor al tama�o de la lista obtenida
		if(objectBox.getCapAlm()==listAlicReg.size())
			isFull =true;
		return isFull;
	}
	
	/**Indica si la caja se encuentra Vacia*/
	public static boolean boxIsEmpty(int freezer, String rack, int caja){
		boolean isEmpty = false;
		if(simlabAlicuotaService.getListAlicReg(freezer, rack,caja).isEmpty())
			isEmpty = true;
		return isEmpty;
	}
	
	/**Metodo que indica si un Rack Contiene Cajas con posiciones Vacias*/
	public static boolean rackHaveBoxWithEmptyPosition(int freezer, String rack){
		boolean haveBoxWithEmptyPosition= false;	
		//Obtenemos el Objeto Rack indicado y Obtenemos la Capacidad de Almacenamiento.
		SimlabEquipService.getRack(freezer, rack).getCapAlm();
		//Recorremos las Cajas que se encuentran almacenadas en el Rack
		for (Caja caja: SimlabEquipService.getListBoxByRack(rack)){
			//Valido si no existen posiciones vacias en la caja
			if(!SimlabEquipService.boxIsFull(caja, freezer, rack, Integer.valueOf(caja.getId().getCodCaja())))
				{
				haveBoxWithEmptyPosition = true;
				break;}
				}
		return haveBoxWithEmptyPosition;
	}
	
	/**Validamos si un Rack tiene posiciones Vacias*/
	public static boolean rackHaveEmptyPosition(int freezer, String rack){
		boolean haveEmptyPosition = false;
			int capAlm = SimlabEquipService.getRack(freezer, rack).getCapAlm();
			 	if(!(capAlm==SimlabEquipService.getListBoxByRack(rack).size()))
				 haveEmptyPosition = true;
		return haveEmptyPosition;
	}
	
	/**Obtenemos la lista de Cajas contenidas en un Rack*/
	@SuppressWarnings("unchecked")
	public static Collection<Caja> getListCajaByRackAndFreezer(int freezer, String rack){
		Collection<Caja> listCajaByRackAndFreezer = new ArrayList<Caja>();
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
					listCajaByRackAndFreezer = session.createSQLQuery(
							"select caja.* from caja, rack, freezer where freezer.COD_FREEZER = ? and rack.COD_RACK = ?" +
							" and freezer.COD_FREEZER = rack.RCOD_FREEZER AND rack.COD_RACK = caja.CCOD_RACK").addEntity(Caja.class)
							.setInteger(0, freezer).setString(1, rack).list();
					session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, exception);
			}
		return listCajaByRackAndFreezer;
	}


	/**Obtenemos la lista de Cajas de acuerdo a Tipo(Positiva, Negativa o Temporal) contenidas en un Rack*/
	@SuppressWarnings("unchecked")
	public static Collection<Caja> getListTypeCajaByRackAndFreezer(int freezer, String rack, String typePosNegOrTemp, String usoAlic, String usoOficOrResg){
		Collection<Caja> listCajaByRackAndFreezer = new ArrayList<Caja>();
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
					listCajaByRackAndFreezer = session.createSQLQuery(
							"select caja.* from caja, rack, freezer  where freezer.COD_FREEZER = ? and rack.COD_RACK = ?  " +
							"and freezer.COD_FREEZER = rack.RCOD_FREEZER  AND rack.COD_RACK = caja.CCOD_RACK " +
							"and USO_ALIC = ? AND USO = ? order by COD_CAJA").addEntity(Caja.class)
							.setInteger(0, freezer).setString(1, rack).setString(2, usoAlic)
							.setString(3, usoOficOrResg).list();
					session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, exception);
			}
		return listCajaByRackAndFreezer;
	}
	
	
	/*****Metodos para mover equipo rack a otro freezer*******/
	public static Collection<String> getListCodeFreezer()throws SimlabAppException{
		
		Collection<Freezer> lstFreezer = new ArrayList<Freezer>();
		Collection<String> listCodeFreezer = new ArrayList<String>();
		lstFreezer = getListFreezer();
		for (Freezer f : lstFreezer) {
			if(listCodeFreezer.isEmpty()){
				listCodeFreezer.add(simlabStringUtils.EMPTY_STRING);
			}
			listCodeFreezer.add(Integer.toString(f.getCodFreezer()));
		}
		return listCodeFreezer;
	}
	
	public static Collection<String> getListCodeRack(int codeFreezer)throws SimlabAppException{
		
		Collection<Rack> lstRack = new ArrayList<Rack>();
		Collection<String> listCodeRack = new ArrayList<String>();
		lstRack = getListRackByFreezer(codeFreezer);
		for (Rack r : lstRack) {
			listCodeRack.add(r.getId().getCodRack());
		}
		return listCodeRack;
	}

	
	public static void moveRackToFreezer(Session session, int codeFreezerAct, int codeFreezerNew, String codeRack, int position){
		try {
			String q = "update rack set rcod_freezer = ?, pos_freezer = ? where cod_rack = ? and rcod_freezer = ?";
			Query query = session.createSQLQuery(q).setInteger(0, codeFreezerNew).setInteger(1, position).
					setString(2, codeRack).setInteger(3, codeFreezerAct);
			query.executeUpdate();			
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByUpdate(SimlabEquipService.class, e);
		}
	}
	
	public static void moveCajaToRack(Session session, String codeRackAct, String codeRackNew, String codeCaja, int position)throws SimlabAppException	{
		try {
			String q = "update caja set ccod_rack = ?, pos_rack = ? where cod_caja = ? and ccod_rack = ?";
			Query query = session.createSQLQuery(q).setString(0, codeRackNew).setInteger(1, position).setString(2, codeCaja)
					.setString(3, codeRackAct);
			query.executeUpdate();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByUpdate(SimlabEquipService.class, e);
		}
	}
		
	public static boolean isFreezerEmpty(int codFreezer){
		boolean isEmpty = false;
		Collection<Rack> lstFreezer = null;
		try {
			lstFreezer = getListRackByFreezer(codFreezer);
			if (lstFreezer.isEmpty()) {
				isEmpty = true;
			}else {
				isEmpty = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isEmpty;
	}
	
	public static boolean isRackEmpty(int codFreezer, String codRack) {
		boolean isEmpty = false;
		Collection<Caja> lstBox = null;
		try {
			lstBox = getListCajaByRack(codFreezer, codRack);
			if (lstBox.isEmpty()) {
				isEmpty = true;
			}else {
				isEmpty = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isEmpty;
	}
		
	@SuppressWarnings("unchecked")
	public static Collection<Caja> getListCajaByRack(int codeFreezer, String codeRack)throws SimlabAppException{
		
		Session session = null;
		Collection<Caja> lst = new ArrayList<Caja>();
		try {
			session = HibernateUtil.openSesion();
			String q = "select c.* from rack r inner join freezer f on r.rcod_freezer = f.cod_freezer " +
					"inner join caja c on r.cod_rack = c.ccod_rack where r.cod_rack = ? and f.cod_freezer = ?";
			lst = session.createSQLQuery(q).addEntity(Caja.class).setString(0, codeRack).setInteger(1, codeFreezer).list();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lst;
	}
	@SuppressWarnings("unchecked")
	public static Collection<Caja> getListCajaByRack(String codeRack)throws SimlabAppException{
		Session session = null;
		Collection<Caja> lst = new ArrayList<Caja>();
		try {
			session = HibernateUtil.openSesion();
			String q = "select c.* from rack r inner join caja c on r.cod_rack = c.ccod_rack where r.cod_rack = ?";
			lst = session.createSQLQuery(q).addEntity(Caja.class).setString(0, codeRack).list();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lst;
	}

	
	/**
	 * Validamos si una caja tiene posiciones Vacias, contando el Numero de
	 * Alicuotas Registradas
	 */
	public static boolean boxHaveEmptyPosition(Freezer freezer, Rack rack,
			Caja caja) {
		// Obtenemos la lista de Alicuotas Registrada en la Caja
		Collection<RegAlic> listAlicRegistered = simlabAlicuotaService
				.getListAlicReg(freezer.getCodFreezer(), rack.getId()
						.getCodRack(), Integer.valueOf(caja.getId()
						.getCodCaja()));

		int numRegistros = 0;

		if (listAlicRegistered.isEmpty()) {
			return true;
		} else {
			numRegistros = listAlicRegistered.size();
		}

		if (numRegistros < caja.getCapAlm())
			return true;
		return false;
	}
	
	
	/**Obtiene un objeto alicutoa por codigo de alicuota*/
	public static RegAlic getAlic(String codeAlic){
	RegAlic alic = null;
		try {
			Session session = HibernateUtil.openSesion();
			String q = "select * from reg_alic where cod_alic = ?";
			alic =  (RegAlic) session.createSQLQuery(q).addEntity(RegAlic.class).setString(0, codeAlic).uniqueResult();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return alic;
	}
	
	/**Obtiene un objeto alicutoa por codigo de alicuota*/
	public static boolean existCodeMuest(int codeMuest){
		boolean exist = false; 
		try {
			Session session = HibernateUtil.openSesion();
			String q = "select ra from RegAlic as ra where ra.id.codAlic like :codigo";
			Query query = session.createQuery(q);
			query.setParameter("codigo", "%"+codeMuest+"%");
			if (query.list().size() <= 0) {
				exist = false;
			}else {
				exist = true;
			}
			//alic =  (RegAlic) session.createSQLQuery(q).addEntity(RegAlic.class).setString(0, "%"+Integer.toString(codeMuest)+"%").uniqueResult();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return exist;
	}
	
	/***
	 * Obtiene una lista de alicuota por codigo de muestra o codigo de alicuota
	 * @param code codigo de muestra o codigo de alicuota
	 * @return lista de alicuota
	 * @throws SimlabAppException
	 */
	public static Collection<AlicuotaModel> getListAlicByCode(String code)throws SimlabAppException{
		Collection<AlicuotaModel> lst = new ArrayList<AlicuotaModel>();
		try {
			Session session = HibernateUtil.openSesion();
			String q = "select ra.id.codAlic,ra.codFreezer,ra.codRack,ra.codBox,ra.id.posBox,ra.fehorReg from RegAlic as ra" +
					" where ra.id.codAlic like :codigo";
			Query query = session.createQuery(q);
			query.setParameter("codigo", "%"+code+"%");
			lst = createCollection(query.list());
			session.close();
		} catch (Throwable e){
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lst;
	}


	/***
	 * Obtiene una lista de alicuota
	 * @return lista de alicuota
	 * @throws SimlabAppException
	 */
	@SuppressWarnings("unchecked")
	public static Collection<RegAlic> getListAlic1() throws SimlabAppException{
		Collection<RegAlic> lst = new ArrayList<RegAlic>();
		try {
			Session session = HibernateUtil.openSesion();
			String q = "From RegAlic ra order by ra.fehorReg DESC";
			Query query = session.createQuery(q);
			query.setMaxResults(50);
			lst = query.list();
			session.close();
		} catch (Throwable e){
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lst;
	}
	
	/***
	 * Obtiene una lista de alicuota
	 * @return lista de alicuota
	 * @throws SimlabAppException
	 */
	public static RegAlic getAlicBoxPos(int numCaja, int posCaja) throws SimlabAppException{
		RegAlic alic = new RegAlic();
		try {
			Session session = HibernateUtil.openSesion();
			String q = "From RegAlic ra where ra.codBox = ? and ra.id.posBox = ?";
			Query query = session.createQuery(q);
			query.setParameter(0, numCaja);
			query.setParameter(1, posCaja);
			alic = (RegAlic) query.uniqueResult();
			session.close();
		} catch (Throwable e){
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return alic;
	}
	
	
	/***
	 * Obtiene una lista de alicuota
	 * @return lista de alicuota
	 * @throws SimlabAppException
	 */
	@SuppressWarnings("unchecked")
	public static Collection<RegAlic> getListAlic2() throws SimlabAppException{
		Collection<RegAlic> lst = new ArrayList<RegAlic>();
		try {
			Session session = HibernateUtil.openSesion();
			
			Query query = session.createQuery("From RegAlic ra Order by ra.codBox, ra.id.posBox");
			lst = query.list();
			session.close();
		} catch (Throwable e){
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lst;
	}
	
	
	/***
	 * Obtiene una lista de alicuota por criterio
	 * @return lista de alicuota
	 * @throws SimlabAppException
	 */
	@SuppressWarnings("unchecked")
	public static Collection<RegAlic> getListAlicCrit(String criterio) throws SimlabAppException{
		Collection<RegAlic> lst = new ArrayList<RegAlic>();
		try {
			Session session = HibernateUtil.openSesion();
			Query query = session.createQuery("From RegAlic ra where lower(ra.id.codAlic) like :codBuscar Order by ra.codBox, ra.id.posBox");
			query.setParameter("codBuscar", criterio.toLowerCase()+'%');
			lst = query.list();
			session.close();
		} catch (Throwable e){
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lst;
	}

	/***
	 * Obtiene una lista de alicuota por criterio
	 * @return lista de alicuota
	 * @throws SimlabAppException
	 */
	@SuppressWarnings("unchecked")
	public static Collection<RegAlic> getListAlicCrit2(String criterio) throws SimlabAppException{
		Collection<RegAlic> lst = new ArrayList<RegAlic>();
		try {
			Session session = HibernateUtil.openSesion();
			Query query = session.createQuery("From RegAlic ra where ra.id.codAlic = :codBuscar");
			query.setParameter("codBuscar", criterio);
			lst = query.list();
			session.close();
		} catch (Throwable e){
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lst;
	}

	/**Crea la coleccion cuando se escribe el codigo de la muestra */
	@SuppressWarnings("rawtypes")
	private static Collection<AlicuotaModel> createCollection(List lst){
		
		AlicuotaModel am;
		Collection<AlicuotaModel> cAlic = new ArrayList<AlicuotaModel>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < lst.size(); i++) {
			Object[] element = (Object []) lst.get(i);
			am = new AlicuotaModel();
			am.setCodeAlic(element[0].toString());
			am.setCodeMuestra(splitString(am.getCodeAlic(), 1));			
			am.setCodeFreezer(Integer.parseInt(element[1].toString()));
			am.setCodeRack(element[2].toString());
			am.setCodeCaja(element[3].toString());
			am.setPosition(Integer.parseInt(element[4].toString()));
			am.setUso(getUseAlic(splitString(am.getCodeAlic(), 2)));
			try {
				am.setFechaRegistro(df.parse(element[5].toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}					
			cAlic.add(am);
		}
		return cAlic;
	}
	/**Metodo q parte el codigo de la alicuota*/
	private static String splitString(String codigo, int part){
		String [] value = codigo.split("\\.");
		String result="";
			if (part == 1 ) {
				result = value[0];
			}else if (part == 2){
				result = value[1];
			}	
		return result;
	}
	/**Retorna el uso de una alicuota*/
	private static String getUseAlic(String codeAlic){
		String uso ="";
		try {
			String sCodigo = codeAlic.substring(codeAlic.length()-1);
			Session session = HibernateUtil.openSesion();
			String q = "select catalicuotas.sepUso from CatAlicuotas as catalicuotas " +
					"where catalicuotas.id.tipoAlicuota like :codigo";
			Query query = session.createQuery(q);
			query.setParameter("codigo", "%"+sCodigo+"%");
			uso = query.list().get(0).toString();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return uso;
	}
	
	public static boolean isValidUsoAlic(String uso){
		Session session = null;
		boolean isValid = false;
		try {
			session = HibernateUtil.openSesion();
			String q = "select dp from DetParam as dp where dp.descItem = :item";
			Query query = session.createQuery(q);
			query.setParameter("item", uso);
			if (query.list().size() <= 0) {
				isValid = false;
			}else {
				isValid = true;
			}
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return isValid;
	}
	
	@SuppressWarnings("unchecked")
	private static Collection<CatAlicuotas> getListCatalogoAlicuotas(){
		
		Session session = null;
		Collection<CatAlicuotas> catAlicuotas = new ArrayList<CatAlicuotas>();
		try {
			session = HibernateUtil.openSesion();
			String q = "select * from cat_alicuotas";
			catAlicuotas = session.createSQLQuery(q).addEntity(CatAlicuotas.class).list();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return catAlicuotas;
	}
	
	public static Collection<String> getListCatAlic(String uso)throws SimlabAppException{
		Collection<String> lst = new ArrayList<String>();
		try {
			for (CatAlicuotas ca : getListCatalogoAlicuotas()) {
				if (ca.getSepUso().equalsIgnoreCase(uso)) {
					lst.add(ca.getId().getTipoAlicuota());
				}
			}			
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return lst;
	}

	/** * * HERROLD* * */
	public static Freezer getFreezerbyID(String IdFreezer)
	{
		Session session = null;
		Freezer freezer = null;
		try
		{
			session = HibernateUtil.openSesion();
			String q = "Select * from freezer where Cod_freezer = ?";
			freezer = (Freezer)session.createSQLQuery(q).addEntity(Freezer.class).setString(0, IdFreezer).uniqueResult();
			session.close();
		}
		catch(Throwable e)
		{
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return
				freezer;
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<Rack> getListRackbyIdFreezer(String idFreezer)
	{
		Session session = null;
		Collection<Rack> listRack = null;
		try{
			session = HibernateUtil.openSesion();
			String q = "Select * from rack where rCod_freezer = ?";
			listRack = session.createSQLQuery(q).addEntity(Rack.class).setString(0, idFreezer).list();
			session.close();
		}catch(Throwable e){
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return
				listRack;
	}
	
	public static Rack getLastRack()
	{
		Session session = null;
		Rack r = null;
		try
		{
			session = HibernateUtil.openSesion();
			String q = "select * from rack where COD_RACK = (Select MAX(rack.COD_RACK) from rack)";
			r = (Rack) session.createSQLQuery(q).addEntity(Rack.class).uniqueResult();
			session.close();
		}
		catch(Throwable e)
		{
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return
				r;
	}
	
	public static Rack compareCapFreezerRack()
	{
		Rack result = null;
		try
		{
			Rack rack = getLastRack();
			Freezer freezer = getFreezerbyID(Integer.toString(rack.getId().getRcodFreezer()));
			if(rack.getPosFreezer() != freezer.getCapAlm())
				result = rack;
		}
		catch(Exception e)
		{
			e.getCause();
			e.printStackTrace();
		}
		return
				result;
	}
	
	public static String setCodigoRack()
	{
		String code = simlabStringUtils.EMPTY_STRING;
		try{
			if(compareCapFreezerRack() != null){
 				code = compareCapFreezerRack().getId().getCodRack();
			}
		}catch(Exception hEx){
			hEx.getCause();
			hEx.printStackTrace();
		}
		return
				code;
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<Caja> getListCajaByCodeRack(String idRack){
		Session session = null;
		Collection<Caja> listCaja = null;
		try{
			session = HibernateUtil.openSesion();
			String q = "select * from caja where CCOD_RACK = ?";
			listCaja = session.createSQLQuery(q).setString(0, idRack).list();
			session.close();
		}catch(Throwable hEx){
			hEx.getCause();
			hEx.printStackTrace();
		}
		return
				listCaja;
	}
		
	public static String setPosRack(){
		String code = simlabStringUtils.EMPTY_STRING;
		String posRack = simlabStringUtils.EMPTY_STRING;
		try {
			code = compareCapFreezerRack().getId().getCodRack();
			int temp = SimlabEquipService.getPositionInRack(code);
			posRack = Integer.toString(temp + 1);
			int cant = SimlabEquipService.getCapAlmRack(code);
			if(Integer.parseInt(posRack) <= cant)
				return posRack;
			else
			{
				SimlabAppException.addFacesMessageError(11050);
				return Integer.toString(0);
			}
		} catch (SimlabAppException e) {
			e.getCause();
			e.printStackTrace();
			SimlabAppException.addFacesMessageError(11050);
			return Integer.toString(0);
		}
	}
	
	public static void saveTanque (Session session, Freezer tanque) throws SimlabAppException{
		try {
			session.beginTransaction();
			session.saveOrUpdate(tanque);
			session.getTransaction().commit();
		}
		catch (Exception e) {
			throw SimlabAppException.generateExceptionByUpdate(SimlabEquipService.class, e);
		}
	}
}