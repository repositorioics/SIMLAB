package ni.gob.minsa.simlab.sistema.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.sistema.hibernate.bussines.AlicCaja;
import ni.gob.minsa.sistema.hibernate.bussines.CatAlicuotas;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;
import ni.gob.minsa.sistema.hibernate.bussines.RegSepCohorte;
import ni.gob.minsa.sistema.hibernate.bussines.ResClinico;
import ni.gob.minsa.sistema.hibernate.bussines.ResCohorte;

import org.hibernate.Query;
import org.hibernate.Session;

public class simlabAlicuotaService {
	
	public simlabAlicuotaService(){
		super();
	}
	
	/**Obtenemos la lista de Catalogo de Alicuotas*/
	@SuppressWarnings("unchecked")
	public static Collection<CatAlicuotas> getListCatAlic(){
		Session session = null;
		Collection<CatAlicuotas > listCatAlic = new ArrayList<CatAlicuotas>();
			try {
				session =  HibernateUtil.openSesion();
				listCatAlic =  session.createSQLQuery("Select * from cat_alicuotas").addEntity(CatAlicuotas.class).list();
				session.close();
				} catch (Throwable exception) {
					//	Generamos la Exception
					SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return listCatAlic;
	}
	
	/**Obtener lista de Alicuotas por Estudio*/
	@SuppressWarnings("unchecked")
	public static Collection<CatAlicuotas> getListAlicByStudy(String study){
		Collection<CatAlicuotas> listAlicByStudy = new ArrayList<CatAlicuotas>();
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
					listAlicByStudy = session.createSQLQuery(
							"SELECT * FROM cat_alicuotas where estudio_pert = ?")
							.addEntity(CatAlicuotas.class).setString(0, study).list();
				//Cerramos Sesion
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return listAlicByStudy;
	}
	
	/**Obtenemos la lista de Alicuotas permitidas por Caja y por Estudio*/
	@SuppressWarnings("unchecked")
	public static Collection<AlicCaja> getListAlicPermittedByBox(String estudio){
		Collection<AlicCaja> listAlicPermittedByBox = new ArrayList<AlicCaja>();
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				listAlicPermittedByBox =session.createSQLQuery("select * FROM alic_caja WHERE estudio= ?").addEntity(AlicCaja.class)
						.setString(0, estudio).list();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, e);
			}
			return listAlicPermittedByBox;
		}
	
	/**Obtenemos la lista de Alicuotas permitidas por Caja y por Estudio*/
	@SuppressWarnings("unchecked")
	public static Collection<AlicCaja> getListAlicByBox(){
		Collection<AlicCaja> listAlicByBox = new ArrayList<AlicCaja>();
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				listAlicByBox =session.createSQLQuery("select * FROM alic_caja").addEntity(AlicCaja.class).list();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, e);
			}
			return listAlicByBox;
		}
	
	/**Obtenemos la Alicuota seleccionada*/
	public static AlicCaja getAlicBySelec(String seleccion){
		AlicCaja alicSelec = new AlicCaja();
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				alicSelec = (AlicCaja) session.createQuery("FROM AlicCaja alic where alic.alicPerm =:seleccion").setParameter("seleccion", seleccion).uniqueResult();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, e);
			}
			return alicSelec;
		}
	
	/**Obtenemos la lista de Alicuotas permitidas por Caja y por Estudio*/
	@SuppressWarnings("unchecked")
	public static Collection<AlicCaja> getListAlicByBoxByUse(String use){
		Collection<AlicCaja> listAlicByBox = new ArrayList<AlicCaja>();
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				listAlicByBox = session.createSQLQuery("select * FROM alic_caja WHERE freezer_alm= ?").addEntity(AlicCaja.class).setString(0, use).list();
				session.close();
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, e);
			}
			return listAlicByBox;
		}
	
	/**Obtenemos un objeto de Tipo Alicuota de Catalogo*/
	public static CatAlicuotas getObjectAlicuota(String typeAlic){
		CatAlicuotas objectAlic = null;
		Session session  = null;
		try {
			session = HibernateUtil.openSesion();
				objectAlic = (CatAlicuotas) session.createSQLQuery("SELECT * FROM cat_alicuotas WHERE  tipo_alicuota = ? ").addEntity(CatAlicuotas.class)
						.setString(0, typeAlic).uniqueResult();
				session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, e);
		}
		return objectAlic;
	}
	
	/**Obtenemos la lista de Alicuotas en dependencia del Freezer, Rack y caja*/
	@SuppressWarnings("unchecked")
	public static Collection<RegAlic> getListAlicReg(int freezerCode, String rackCode, int boxCode){
		Collection<RegAlic> listAlicReg = new ArrayList<RegAlic>();
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createQuery("FROM RegAlic ra where ra.codBox =:codcaja order by ra.id.posBox");
				query.setParameter("codcaja", boxCode);
				listAlicReg = query.list();
				session.close();				
			} catch (Throwable e) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, e);
			}
		return listAlicReg;
	}
	
	public static void updateAlic(Session session, String tipAlic, String stdPer, String sepUso, int tempAlm, int volPerm, String muestPer){
		try {
			String q = "UPDATE CatAlicuotas SET sep_uso = ?, temp_alm = ?, vol_perm = ?, muest_pert = ? where tipo_alicuota = ? and estudio_pert = ?";
			Query query = session.createQuery(q).setString(0, sepUso).setInteger(1, tempAlm).setInteger(2, volPerm)
					.setString(3, muestPer).setString(4, tipAlic).setString(5, stdPer);
			query.executeUpdate();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByUpdate(SimlabEquipService.class, e);
		}
	}
	
	public static CatAlicuotas getObjAlic(String tipAlic, String stdPer){
		Session session = null;
		CatAlicuotas catAlic = null;
		try {
			session = HibernateUtil.openSesion();
			String query = "SELECT ca.tipo_alicuota,ca.estudio_pert,ca.sep_uso,ca.temp_alm,ca.vol_perm,ca.muest_pert " +
					"FROM cat_alicuotas ca where ca.tipo_alicuota = ? and ca.estudio_pert = ?";
			catAlic = (CatAlicuotas) session.createSQLQuery(query).addEntity(CatAlicuotas.class).setString(0, tipAlic).setString(1, stdPer).uniqueResult();
			session.close();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionBySelect(SimlabEquipService.class, e);
		}
		return catAlic;
	}
	
	/**Registramos Alicuota*/
	public static void addRegAlic(Session session, String codAlic, Integer posBox, Integer codFreezer, Integer codRack, Integer codBox, String indPosOrNeg,String indDsc, String codUser){
		try {
			Query query =  session.createSQLQuery("INSERT INTO reg_alic(cod_alic,pos_box,cod_freezer,cod_rack,cod_box,neg_pos,ind_desc,fehor_reg,cod_user) " +
					" VALUES(?,?,?,?,?,?,?,?,?)").setString(0, codAlic).setInteger(1, posBox).setInteger(2, codFreezer).setInteger(3, codRack).setInteger(4, codBox)
					.setString(5, indPosOrNeg).setString(6, indDsc).setTimestamp(7, SimlabDateUtil.getCurrentTimestamp()).setString(8, codUser);
			query.executeUpdate();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionByInsert(simlabAlicuotaService.class, exception);
		}
	}
	
	/**Registra Descarte en la BD.*/
	public static void discardAlic(Session session, String codAlic, String codeUsr){
		try {
			Query query  = session.createSQLQuery("INSERT INTO reg_descarte (cod_alic, fehor_desc,cod_usr) VALUES(?,?,?)")
					.setString(0, codAlic).setTimestamp(1, SimlabDateUtil.getCurrentTimestamp()).setString(2, codeUsr);
			query.executeUpdate();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByInsert(simlabAlicuotaService.class, e);
		}
	}
	
	/**Obtenemos el Objeto Resultado de la Tabla res_clinico*/
	public static ResClinico getObjectResultMuestras(int codMuestra){
		ResClinico objectResClinico = null;
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				objectResClinico = (ResClinico) session.createSQLQuery("SELECT * FROM res_clinico WHERE Codigo_nino = ?").addEntity(ResClinico.class)
						.setInteger(0, codMuestra).uniqueResult();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objectResClinico;
	}
	
	/**Obtenemos el Objeto Resultado de la Tabla res_cohorte*/
	public static ResCohorte getObjectResultMuestras1(int codMuestra){
		ResCohorte objectResCohorte = null;
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				objectResCohorte = (ResCohorte) session.createSQLQuery("SELECT * FROM res_cohorte WHERE Cod_Epi = ?").addEntity(ResCohorte.class)
						.setInteger(0, codMuestra).uniqueResult();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objectResCohorte;
	}
	
	public static RegAlic getObjectRegAlic(String codeAlic, String tipo){
		RegAlic objectRegAlic = null;
		Session session = null;
			try {
				session  =  HibernateUtil.openSesion();
						objectRegAlic = (RegAlic) session.createSQLQuery("SELECT * FROM reg_alic where cod_alic  = ? and tipo = ?")
								.addEntity(RegAlic.class).setString(0, codeAlic).setString(1, tipo).uniqueResult();
						session.close();
			} catch (Throwable exception) {
					SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
					
			}
		return objectRegAlic;
	}
	
/** Herrold */
	
	public static void addSalAlic(Session session, String codeAlic, String vol, String propUso){
		try{
			Query query = session.createSQLQuery("INSERT INTO `alic_salida`(`codAlic`,`propUso`,`vol_salida`)VALUES (?,?,?)").setString(0, codeAlic).setString(1, propUso).setString(2, vol);
			query.executeUpdate();
		}catch(Throwable hEx){
			SimlabAppException.generateExceptionByInsert(simlabAlicuotaService.class, hEx);
		}
	}
	
	public static void addDescargo(Session session, String codeAlic, String vol){
		try{
			if(codeAlic.isEmpty() == false && vol.isEmpty() == false){
				Query query = session.createSQLQuery("UPDATE reg_alic SET peso_alic = peso_alic - ? WHERE cod_alic = ?").setString(0, vol).setString(1, codeAlic);
				query.executeUpdate();
			}
		}catch(Throwable hEx){
			SimlabAppException.generateExceptionByUpdate(simlabAlicuotaService.class, hEx);
		}
	}

	public static ResCohorte getObjectResultMuestrasCohorte(String codEpi) {
		ResCohorte objectResCohorte = null;
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				objectResCohorte = (ResCohorte) session.createSQLQuery("SELECT * FROM res_cohorte WHERE cod_epi = ?").addEntity(ResCohorte.class)
						.setString(0, codEpi).uniqueResult();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objectResCohorte;
	}
	
	public static RegSepCohorte getObjectRegSepCohorte(String codLab) {
		RegSepCohorte objectRegSepCohorte = null;
		Session session = null;
			try {
				session = HibernateUtil.openSesion();
				objectRegSepCohorte = (RegSepCohorte) session.createSQLQuery("SELECT * FROM reg_sep_cohorte WHERE codigo_lab = ?").addEntity(RegSepCohorte.class)
						.setString(0, codLab).uniqueResult();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objectRegSepCohorte;
	}
	
	/** Fin Herrold*/
	
	
	@SuppressWarnings("unchecked")
	public static List<Object[]> getObjectosParaSugerir(String use,Integer temp, String tempOroficial, String posOrNeg) {
		List<Object[]> objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("SELECT freezer.COD_FREEZER, rack.COD_RACK, caja.COD_CAJA, caja.TIP_MUES, caja.CAP_ALM " +
						"FROM ((freezer INNER JOIN rack ON freezer.COD_FREEZER = rack.RCOD_FREEZER) INNER JOIN caja ON rack.COD_RACK = caja.CCOD_RACK) " +
						"LEFT JOIN reg_alic ON caja.COD_CAJA = reg_alic.cod_box WHERE caja.USO=:tempOroficial AND caja.POS_NEG=:posOrNeg AND freezer.USO_ALM like :use " +
						"AND freezer.TEMP_MIN >= :temp AND freezer.TEMP_MAX <= :temp AND freezer.FEINIVIG <= :hoy " +
								"AND freezer.FEFINVIG >= :hoy AND rack.FE_INI <= :hoy AND rack.FE_FIN >= :hoy " +
								"AND caja.FE_INI <= :hoy AND caja.FE_FIN >= :hoy GROUP BY freezer.COD_FREEZER, rack.COD_RACK, caja.COD_CAJA, caja.TIP_MUES, caja.CAP_ALM " +
						"HAVING Count(reg_alic.cod_alic)<caja.CAP_ALM ORDER BY caja.COD_CAJA;");
				query.setParameter("use", '%'+use+'%');
				query.setParameter("temp", temp);
				query.setParameter("tempOroficial", tempOroficial);
				query.setParameter("posOrNeg", posOrNeg);
				query.setTimestamp("hoy", SimlabDateUtil.getCurrentTimestamp());
				objetosResultado = query.list();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Object[]> getObjectosParaSugerirMA2018(String use,Integer temp, String tempOroficial, String posOrNeg, String tipoAlic, String cajaEstudio) {
		List<Object[]> objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("SELECT freezer.COD_FREEZER, rack.COD_RACK, caja.COD_CAJA, caja.TIP_MUES, caja.CAP_ALM " +
						"FROM ((freezer INNER JOIN rack ON freezer.COD_FREEZER = rack.RCOD_FREEZER) INNER JOIN caja ON rack.COD_RACK = caja.CCOD_RACK) " +
						"LEFT JOIN reg_alic ON caja.COD_CAJA = reg_alic.cod_box WHERE caja.USO=:tempOroficial AND caja.ESTUDIO=:cajaEstudio AND caja.POS_NEG=:posOrNeg AND caja.TIP_MUES like :tipoMuestra AND freezer.USO_ALM like :use " +
						"AND freezer.TEMP_MIN >= :temp AND freezer.TEMP_MAX <= :temp AND freezer.FEINIVIG <= :hoy " +
								"AND freezer.FEFINVIG >= :hoy AND rack.FE_INI <= :hoy AND rack.FE_FIN >= :hoy " +
								"AND caja.FE_INI <= :hoy AND caja.FE_FIN >= :hoy GROUP BY freezer.COD_FREEZER, rack.COD_RACK, caja.COD_CAJA, caja.TIP_MUES, caja.CAP_ALM " +
						"HAVING Count(reg_alic.cod_alic)<caja.CAP_ALM ORDER BY caja.COD_CAJA;");
				query.setParameter("use", '%'+use+'%'); 
				query.setParameter("temp", temp);
				query.setParameter("tempOroficial", tempOroficial);
				query.setParameter("posOrNeg", posOrNeg);
				query.setParameter("cajaEstudio", cajaEstudio);
				query.setParameter("tipoMuestra", '%'+tipoAlic+'%');
				query.setTimestamp("hoy", SimlabDateUtil.getCurrentTimestamp());
				objetosResultado = query.list();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Object[]> getObjectosParaSugerir2(String use,Integer temp, String tempOroficial, String posOrNeg, String tipoAlic) {
		List<Object[]> objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("SELECT freezer.COD_FREEZER, rack.COD_RACK, caja.COD_CAJA, caja.TIP_MUES, caja.CAP_ALM " +
						"FROM ((freezer INNER JOIN rack ON freezer.COD_FREEZER = rack.RCOD_FREEZER) INNER JOIN caja ON rack.COD_RACK = caja.CCOD_RACK) " +
						"LEFT JOIN reg_alic ON caja.COD_CAJA = reg_alic.cod_box WHERE caja.USO=:tempOroficial AND caja.POS_NEG=:posOrNeg AND caja.TIP_MUES like :tipoMuestra AND freezer.USO_ALM like :use " +
						"AND freezer.TEMP_MIN >= :temp AND freezer.TEMP_MAX <= :temp AND freezer.FEINIVIG <= :hoy " +
								"AND freezer.FEFINVIG >= :hoy AND rack.FE_INI <= :hoy AND rack.FE_FIN >= :hoy " +
								"AND caja.FE_INI <= :hoy AND caja.FE_FIN >= :hoy GROUP BY freezer.COD_FREEZER, rack.COD_RACK, caja.COD_CAJA, caja.TIP_MUES, caja.CAP_ALM " +
						"HAVING Count(reg_alic.cod_alic)<caja.CAP_ALM ORDER BY caja.COD_CAJA;");
				query.setParameter("use", '%'+use+'%'); 
				query.setParameter("temp", temp);
				query.setParameter("tempOroficial", tempOroficial);
				query.setParameter("posOrNeg", posOrNeg);
				query.setParameter("tipoMuestra", '%'+tipoAlic+'%');
				query.setTimestamp("hoy", SimlabDateUtil.getCurrentTimestamp());
				objetosResultado = query.list();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}
	
	
	public static Object[] getUbicacionMA2017(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("SELECT * from posiciones_ma where cod_alic =:codigo");
				query.setParameter("codigo", codigo);
				objetosResultado = (Object[]) query.uniqueResult();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}
	
	public static Object[] getUbicacionMA2018(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("SELECT * from posiciones_ma_2018 where cod_alic =:codigo");
				query.setParameter("codigo", codigo);
				objetosResultado = (Object[]) query.uniqueResult();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}
	
	public static Object[] getUbicacionMA2019(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("SELECT * from posiciones_ma_2019 where cod_alic =:codigo");
				query.setParameter("codigo", codigo);
				objetosResultado = (Object[]) query.uniqueResult();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}

	public static Object[] getUbicacionMA2020(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
		try {
			session = HibernateUtil.openSesion();
			query = session.createSQLQuery("SELECT * from posiciones_ma_2020 where cod_alic =:codigo");
			query.setParameter("codigo", codigo);
			objetosResultado = (Object[]) query.uniqueResult();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
		}
		return objetosResultado;
	}

	public static Object[] getUbicacionMCV20(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
		try {
			session = HibernateUtil.openSesion();
			query = session.createSQLQuery("SELECT * from posiciones_mcv_2020 where cod_alic =:codigo");
			query.setParameter("codigo", codigo);
			objetosResultado = (Object[]) query.uniqueResult();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
		}
		return objetosResultado;
	}

	public static Object[] getUbicacionMCV21(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
		try {
			session = HibernateUtil.openSesion();
			query = session.createSQLQuery("SELECT * from posiciones_mcv_2021 where cod_alic =:codigo");
			query.setParameter("codigo", codigo);
			objetosResultado = (Object[]) query.uniqueResult();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
		}
		return objetosResultado;
	}

	public static Object[] getUbicacionMA2021(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
		try {
			session = HibernateUtil.openSesion();
			query = session.createSQLQuery("SELECT * from posiciones_ma_2021 where cod_alic =:codigo");
			query.setParameter("codigo", codigo);
			objetosResultado = (Object[]) query.uniqueResult();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
		}
		return objetosResultado;
	}

	public static Object[] getUbicacionEstMinsa(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
		try {
			session = HibernateUtil.openSesion();
			query = session.createSQLQuery("SELECT * from posiciones_minsa where cod_alic =:codigo");
			query.setParameter("codigo", codigo);
			objetosResultado = (Object[]) query.uniqueResult();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
		}
		return objetosResultado;
	}

	public static Object[] getUbicacionMA2022(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
		try {
			session = HibernateUtil.openSesion();
			query = session.createSQLQuery("SELECT * from posiciones_ma_2022 where cod_alic =:codigo");
			query.setParameter("codigo", codigo);
			objetosResultado = (Object[]) query.uniqueResult();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
		}
		return objetosResultado;
	}

	public static Object[] getUbicacionMA2023(String codigo) {
		Object[] objetosResultado = null;
		Session session = null;
		Query query = null;
		try {
			session = HibernateUtil.openSesion();
			query = session.createSQLQuery("SELECT * from posiciones_ma_2023 where cod_alic =:codigo");
			query.setParameter("codigo", codigo);
			objetosResultado = (Object[]) query.uniqueResult();
			session.close();
		} catch (Throwable exception) {
			SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
		}
		return objetosResultado;
	}
	
	public static Object[] getEstadoParticipante(Integer codigo) {
		Object[] objetoResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("SELECT * from estado_cohorte where codigo =:codigo");
				query.setParameter("codigo", codigo);
				objetoResultado = (Object[]) query.uniqueResult();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetoResultado;
	}
	
	@SuppressWarnings("unchecked")
	public static int getAlicCaja(Integer caja) {
		int objetoResultado = 99;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("SELECT * FROM reg_alic where cod_box =:codcaja");
				query.setParameter("codcaja", caja);
				List<Object> res = query.list();
				objetoResultado = res.size();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetoResultado;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static Collection<Object[]> getCajasSugeridas(String posOrNeg, String tipoAlic, String tipo) {
		Collection<Object[]> objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				String sqlQueryTipoAlic = "((caja.TIP_MUES)='"+tipoAlic+"'))";
				query = session.createSQLQuery("SELECT caja.COD_CAJA, rack.RCOD_FREEZER, rack.POS_FREEZER, caja.CCOD_RACK, caja.POS_RACK, caja.TEMP_ALM, "
						+ "caja.USO_ALIC, caja.TIP_MUES, caja.POS_NEG, 81 - count(reg_alic.cod_alic) AS libres "
						+ "FROM (caja LEFT JOIN reg_alic ON caja.COD_CAJA = reg_alic.cod_box) INNER JOIN rack ON caja.CCOD_RACK = rack.COD_RACK "
						+ "WHERE (caja.POS_NEG =:posOrNeg and " + sqlQueryTipoAlic 
						+ " GROUP BY caja.COD_CAJA, rack.RCOD_FREEZER, rack.POS_FREEZER, caja.CCOD_RACK, caja.POS_RACK, caja.TEMP_ALM, caja.USO_ALIC, "
						+ "caja.TIP_MUES, caja.POS_NEG HAVING (((Count(reg_alic.cod_alic))<81)) ORDER BY 81 - Count(reg_alic.cod_alic) DESC;");
				query.setParameter("posOrNeg", posOrNeg);
				
				
				objetosResultado = query.list();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Collection<Object[]> getMuestras(String codigoABuscar) {
		Collection<Object[]> objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("select cod_alic, cod_freezer, cod_rack, cod_box, pos_box, neg_pos,vol_alic, peso_alic, fecha_salida, usuario from reg_alic_salidas where cod_alic Like '%"+codigoABuscar+"%'");
				objetosResultado = query.list();
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<Object[]> getCajasSugeridasSubAlicuotas(String tipoAlic) {
		Collection<Object[]> objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				String[] itemTypeAlicSelected = tipoAlic.split(","); 
				for (String alic:itemTypeAlicSelected) {
					String sqlQueryTipoAlic = " caja.TIP_MUES Like '"+alic+",%' Or caja.TIP_MUES Like '%,"+alic+",%' Or caja.TIP_MUES Like '%,"+alic+"' Or caja.TIP_MUES='"+alic+"'";
					query = session.createSQLQuery("SELECT caja.COD_CAJA, rack.RCOD_FREEZER, rack.POS_FREEZER, caja.CCOD_RACK, caja.POS_RACK, caja.TEMP_ALM, "
							+ "caja.USO_ALIC, caja.TIP_MUES, caja.POS_NEG, 81 - count(reg_alic.cod_alic) AS libres "
							+ "FROM (caja LEFT JOIN reg_alic ON caja.COD_CAJA = reg_alic.cod_box) INNER JOIN rack ON caja.CCOD_RACK = rack.COD_RACK "
							+ "WHERE " + sqlQueryTipoAlic 
							+ " GROUP BY caja.COD_CAJA, rack.RCOD_FREEZER, rack.POS_FREEZER, caja.CCOD_RACK, caja.POS_RACK, caja.TEMP_ALM, caja.USO_ALIC, "
							+ "caja.TIP_MUES, caja.POS_NEG HAVING (((Count(reg_alic.cod_alic))<81)) ORDER BY 81 - Count(reg_alic.cod_alic) DESC;");
					
					objetosResultado = query.list();
					if(!objetosResultado.isEmpty()) {
						return objetosResultado;
					}
				}
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<Object[]> getDatosReporteRecursos(Date fecha1, Date fecha2) {
		String fechaInicio;
		String fechaFin;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		
		fechaInicio = format1.format(fecha1);
		fechaFin = format1.format(fecha2);
		
		
		Collection<Object[]> objetosResultado = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("select cod_user from reg_alic WHERE fehor_reg BETWEEN '"+ fechaInicio +"' AND '"+fechaFin+"'group by cod_user");
				objetosResultado = query.list();
				
				
				String sqlQuery = "SELECT DATE_FORMAT(fehor_reg,'%Y-%m-%d') as fecha, count(cod_user) as total";
				
				for(Object obj:objetosResultado) {
					sqlQuery = sqlQuery  + " , SUM((CASE cod_user WHEN '"+ obj.toString()+"' THEN 1 ELSE 0 END)) AS '" + obj.toString() +"'";
				}
				
				sqlQuery = sqlQuery  + " FROM reg_alic WHERE fehor_reg BETWEEN '"+ fechaInicio +"' AND '"+fechaFin+"'";
				sqlQuery = sqlQuery  + " GROUP BY DATE_FORMAT(fehor_reg,'%Y-%m-%d') ORDER BY DATE_FORMAT(fehor_reg,'%Y-%m-%d');";
				
				query = session.createSQLQuery(sqlQuery);
				
				objetosResultado = query.list();
				
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return objetosResultado;
	}
	
	@SuppressWarnings("unchecked")
	public static String[] getEncabezadosDatosReporteRecursos(Date fecha1, Date fecha2) {
		String fechaInicio;
		String fechaFin;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		
		fechaInicio = format1.format(fecha1);
		fechaFin = format1.format(fecha2);
		
		
		Collection<Object[]> objetosResultado = null;
		String[] encabezados = null;
		Session session = null;
		Query query = null;
			try {
				session = HibernateUtil.openSesion();
				query = session.createSQLQuery("select cod_user from reg_alic WHERE fehor_reg BETWEEN '"+ fechaInicio +"' AND '"+fechaFin+"'group by cod_user");
				objetosResultado = query.list();
				encabezados = new String[objetosResultado.size()];
				int i =0;
				for(Object obj:objetosResultado) {
					encabezados[i] =  obj.toString();
					i++;
				}
				session.close();
			} catch (Throwable exception) {
				SimlabAppException.generateExceptionBySelect(simlabAlicuotaService.class, exception);
			}
		return encabezados;
	}
	
	public static void saveAlicuota(RegAlic regAlic) {
		Session session = HibernateUtil.openSesion();
		try {
			session.beginTransaction();
			session.save(regAlic);
			session.getTransaction().commit();
			session.close();
		}
		
		finally{
			HibernateUtil.analizeForRollbackAndCloseSession(session);
		}
	}
}

