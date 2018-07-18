package ni.gob.minsa.simlab.sistema.services;

import org.hibernate.Query;
import org.hibernate.Session;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.sistema.hibernate.bussines.Transacs;

public class SimlabTxService {
	
	public SimlabTxService(){
		super();
	}
	
	public static void addTx(Transacs tx, String codeTx, String modPert, String description, String indEntrada, String indHab,  String indDeveloped) throws SimlabAppException{
					Transacs transact = new Transacs(codeTx,description,indEntrada,modPert,indDeveloped, indHab);
					HibernateUtil.saveObject(transact);	
	}
	
	public static void updateTx(Transacs tx, String codeTx, String modPert, String description, String indEntrada, String indHab, String indDeveloped){
		Session session = null;
		try {
			session = HibernateUtil.openSesion();
			Query query  = session.createSQLQuery(
				"UPDATE Transacs set COD_TRANS = ?, DESC_TRANS =  ?, INDPE_TRANS = ?, MODP_TRANS = ?, IND_DEVELOP = ?, IND_HAB = ? where COD_TRANS = ?")
					.addEntity(Transacs.class).setString(0, codeTx).setString(1, description).setString(2, indEntrada).setString(3, modPert).setString(4, indDeveloped)
					.setString(5, indHab).setString(6, tx.getCodTrans());
			query.executeUpdate();
		} catch (Throwable e) {
			SimlabAppException.generateExceptionByUpdate(SimlabTxService.class, e);
		}
	}
	
	
}
