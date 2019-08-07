package ni.gob.minsa.simlab.sistema.services;

import java.util.ArrayList;
import java.util.Collection;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.hibernate.HibernateUtil;
import ni.gob.minsa.simlab.sistema.services.SimlabParameterService.CatalogParam;
import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;
import ni.gob.minsa.sistema.hibernate.bussines.Menline;
import ni.gob.minsa.sistema.hibernate.bussines.Menprinc;
import ni.gob.minsa.sistema.hibernate.bussines.PerfTransac;
import ni.gob.minsa.sistema.hibernate.bussines.Transacs;

import org.hibernate.Session;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

public class SimlabMenuService {
	private static Collection<Transacs> listTx = new ArrayList<Transacs>();
	private static Collection<Menprinc> listMenPrinc =new ArrayList<Menprinc>();
	private static final String sufixUrl = ".xhtml";
			
	public SimlabMenuService(){}
	
		/**Obtenemos la lista de Objetos Transacciones*/
		@SuppressWarnings("unchecked")
		public static Collection<Transacs> getListTx(){
			if(listTx == null || listTx.isEmpty()){
				Session session = null;
				try {
					session = HibernateUtil.openSesion();
					listTx = session.createQuery("FROM Transacs order by COD_TRANS").list();
					session.close();
				} catch (Throwable exception) {
					SimlabAppException.generateExceptionBySelect(SimlabMenuService.class, exception);
				}
			}			
		return listTx;
		}
		
		/**Obtenemos un objeto de Tipo Transaccion mediante su codigo*/
		public static String getTxModule(String codeTx){
				Collection<Transacs> listTransaction = SimlabMenuService.getListTx();
				String moduleTx = null;
			for (Transacs transacs : listTransaction) {
				if(simlabStringUtils.areEquals(transacs.getCodTrans(), codeTx))
					{moduleTx = transacs.getModpTrans();break;} 
			}
			return moduleTx;
		}
		/**Obtenemos la lista de Menus Principal*/
		@SuppressWarnings("unchecked")
		public static Collection<Menprinc> getListMenPrinc(){
			Session session  = null;
			if(listMenPrinc == null || listMenPrinc.isEmpty()) {
				try {
					 session  = HibernateUtil.openSesion();
					 listMenPrinc = session.createQuery("MENPRINC ORDER BY COD_MEN").list();
					 session.close();
				} catch (Throwable exception) {
					SimlabAppException.generateExceptionBySelect(SimlabMenuService.class, exception);
				}
			}
			return listMenPrinc;
		}
		
		@SuppressWarnings("unchecked")
		public static Collection<Menline> geListMenuHeader(){
			Collection<Menline> listMenuHeader = new ArrayList<Menline>();
			Session session  = null;
				try {
					session =  HibernateUtil.openSesion();
					listMenuHeader = session.createSQLQuery("SELECT menline.* " +
							"FROM menline, menprinc WHERE tran_menl = menprinc.COD_MEN " +
							"and menline.COD_MEN = ? and TIP_LINE = ? ORDER BY NORDEN_MEN").addEntity(Menline.class)
							.setString(0, SimlabParameterService.getParameterCode(CatalogParam.MENU_HEADER, 2)).
							setString(1, SimlabParameterService.getParameterCode(CatalogParam.TYPE_MENU,2 )).list();
					HibernateUtil.closeSession(session);
				} catch (Throwable Exception) {
					SimlabAppException.generateExceptionBySelect(SimlabMenuService.class, Exception);
				}
			return listMenuHeader;
		}
		
		/**Obtenemos un Objeto de Tipo Transsacion, mediante el Codigo de Modulo*/
		@SuppressWarnings("unchecked")
		public static Collection<Transacs> getListTxByCodeMod(String codeMod){
			Session session = null;
			Collection<Transacs> tx = new ArrayList<Transacs>();
				try {
					session = HibernateUtil.openSesion();
					tx =  session.createQuery("FROM Transacs where modp_trans= ?").setString(0, codeMod).list();
					session.close();
				} catch (Throwable exception) {
					SimlabAppException.generateExceptionBySelect(SimlabMenuService.class, exception);
				}
			return tx;
			}
		
		//TODO: HACE FALTA AGREGAR LOS ITEMS A LOS MENUES
		/**Generamos el Modelo de Menu para el Usuario Logueado */
		public static MenuModel generateMenuModel(Collection<PerfTransac> collection){
			MenuModel menuModel = new DefaultMenuModel();
				SimlabMenuService.addSubmenu(menuModel, collection);
			return menuModel;
		}
		
		/**Agregamos los Submenus al Menu Default*/
		public static void addSubmenu(MenuModel menuModel, Collection<PerfTransac> listTxHab){
			//Obtenemos la lista de Menues Principal.
			Collection<Menline> getMenuPrinc = SimlabMenuService.geListMenuHeader();
				//Recorremos la lista de Menu Cabeceras 	
				for (Menline menline : getMenuPrinc) {
					Submenu submenu = new Submenu();
					submenu.setLabel(menline.getDescMen());
					if(simlabStringUtils.trimUpper(menline.getDescMen()).contains("ADMIN")) submenu.setIcon("ui-icon-gear");
					else if(simlabStringUtils.trimUpper(menline.getDescMen()).contains("CATALO")) submenu.setIcon("ui-icon-folder-open");
					else if(simlabStringUtils.trimUpper(menline.getDescMen()).contains("SISTEM")) submenu.setIcon("ui-icon-home");
					else if(simlabStringUtils.trimUpper(menline.getDescMen()).contains("BANCO")) submenu.setIcon("ui-icon-battery-3");
					else if(simlabStringUtils.trimUpper(menline.getDescMen()).contains("MUEST")) submenu.setIcon("ui-icon-star");
					else if(simlabStringUtils.trimUpper(menline.getDescMen()).contains("EQUI")) submenu.setIcon("ui-icon-calculator");
					else if(simlabStringUtils.trimUpper(menline.getDescMen()).contains("REPOR")) submenu.setIcon("ui-icon-script");
					else if(simlabStringUtils.trimUpper(menline.getDescMen()).contains("PBMC")) submenu.setIcon("ui-icon-lightbulb");
					//Agregamos el Submenu creado
					menuModel.addSubmenu(submenu);
					//Agregamos los elementos de Menu
					SimlabMenuService.addItem(submenu, listTxHab, menline.getId().getTranMenl());
				}
		}
		
		/**Agregamos los Items al SubMenu*/
		public static void addItem(Submenu submenu, Collection<PerfTransac> listTxHab, String codeMod){
			Collection<Transacs> listTxByMod = SimlabMenuService.getListTxByCodeMod(codeMod);
			//Obtenemos la lista de Transacciones Habilitadas al Usuario
			for (PerfTransac txCode: listTxHab) {
				//Obtenemos la lista de Transacciones por nombre de Modulo
				for (Transacs transacs : listTxByMod) {
					if(simlabStringUtils.areEquals(txCode.getId().getCodTrans(), transacs.getCodTrans()))
					{
						MenuItem menuItem = new MenuItem();
						menuItem.setValue(transacs.getDescTrans());
						//Agregamos Icono al Item de Menu
						if(simlabStringUtils.areEquals(transacs.getIndDevelop(), SimlabParameterService.getParameterCode(CatalogParam.SI_NO, 2)))
							menuItem.setIcon("ui-icon-circle-close");
						else
						menuItem.setIcon("ui-icon-document");
						//Formamos la Cadena URL para cada Items de Menu: SIMLAB/modulo/transaccion.xhtml
						menuItem.setUrl(
								"/"+transacs.getModpTrans()+"/"+
								transacs.getCodTrans().concat(sufixUrl));
						submenu.getChildren().add(menuItem);
					}
				}
			}
		}
		
		
	}
