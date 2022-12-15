package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.Query;
import org.hibernate.Session;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabEquipService;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.utileria.SimlabDateUtil;
import ni.gob.minsa.sistema.hibernate.bussines.Caja;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;
import org.primefaces.event.FileUploadEvent;


@ManagedBean(name="busqMuestraMbean")
@ViewScoped
public class BusqMuestraMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 8523255793629321552L;
	
	private Integer mode;
	private String codeAlic;
	private String titlePanel;
	private String motCloseVigency;
	private RegAlic alicToEdit;
	private String codeAlicToFind = null;
	Collection<RegAlic> listAlicuotas = new ArrayList<RegAlic>();
    List<String> alicList = new ArrayList<String>();

    public List<String> getAlicList() {
        return alicList;
    }

    public void setAlicList(List<String> alicList) {
        this.alicList = alicList;
    }

    public String getMotCloseVigency() {
		return motCloseVigency;
	}

	public void setMotCloseVigency(String motCloseVigency) {
		this.motCloseVigency = motCloseVigency;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getCodeAlic() {
		return codeAlic;
	}

	public void setCodeAlic(String codeAlic) {
		this.codeAlic = codeAlic;
	}

	public String getTitlePanel() {
		return titlePanel;
	}

	public void setTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}

	public String getCodeAlicToFind() {
		return codeAlicToFind;
	}

	public void setCodeAlicToFind(String codeAlicToFind) {
		this.codeAlicToFind = codeAlicToFind;
	}

	public Collection<RegAlic> getListAlicuotas() {
		return listAlicuotas;
	}

	public void setListAlicuotas(Collection<RegAlic> listAlicuotas) {
		this.listAlicuotas = listAlicuotas;
	}

	public BusqMuestraMbean() {
		// TODO Auto-generated constructor stub
		super();
		this.setMode(0);
	}
	
	public Collection<RegAlic> getListAlicByCode(){
		
		Collection<RegAlic> listAlicByCode = new ArrayList<RegAlic>();
		try {
			if (this.getCodeAlicToFind() == null) {
				listAlicByCode = null;
			}else if (this.getCodeAlicToFind().equals("")){
				listAlicByCode = null;
			}
			else {
				String criterio = this.getCodeAlicToFind();
				listAlicByCode = SimlabEquipService.getListAlicCrit(criterio);
				for(RegAlic ra: listAlicByCode) {
					ra.setCodFreezer(0);
					ra.setCodRack(null);
					
					Caja caja = SimlabEquipService.getCajaByCode(String.valueOf(ra.getCodBox()));
					if(caja!=null) {
						Rack rack = SimlabEquipService.getRack(caja.getId().getCcodRack());
						ra.setCodFreezer(rack.getId().getRcodFreezer());
						ra.setCodRack(rack.getId().getCodRack());
					}
					
				}
			}
			
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}
		return listAlicByCode;
	}


	public Collection<RegAlic> getListAlicByCode2() {

		Collection<RegAlic> listAlicByCode2 = new ArrayList<RegAlic>();
		try {
			if (this.getAlicList() == null) {
				listAlicByCode2 = null;
			} else if (this.getAlicList().equals("")) {
				listAlicByCode2 = null;
			} else {
				for (int i = 0; i < this.getAlicList().size(); i++) {
					String cod = this.getAlicList().get(i);
					Collection<RegAlic> temp = SimlabEquipService.getListAlicCrit2(cod);

					if (temp != null) {

						for (RegAlic ra : temp) {
							ra.setCodFreezer(0);
							ra.setCodRack(null);

							Caja caja = SimlabEquipService.getCajaByCode(String.valueOf(ra.getCodBox()));
							if (caja != null) {
								Rack rack = SimlabEquipService.getRack(caja.getId().getCcodRack());
								ra.setCodFreezer(rack.getId().getRcodFreezer());
								ra.setCodRack(rack.getId().getCodRack());
							}

							listAlicByCode2.add(ra);


						}

					}

				}

			}



		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
		}

		return listAlicByCode2;

	}

	
	
	
	public void actionEditInfoAlic(ActionEvent event, RegAlic alic){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("edit_alic"));
		this.setAlicToEdit(alic);
		this.setMode(1);
	}
	
	public void actionCloseVigencyMuestra(ActionEvent event, RegAlic alic){
		this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("edit_alic"));
		//Habilitamos la Vista para Cerrar la Vigencia del Aparato
		this.setAlicToEdit(alic);
		this.setMode(2);	
	}

	public RegAlic getAlicToEdit() {
		return alicToEdit;
	}

	public void setAlicToEdit(RegAlic alicToEdit) {
		this.alicToEdit = alicToEdit;
	}
	
	public void actionCancel(ActionEvent event){
		
		this.setMode(0);
	}
	
	//Registramos el EncPrincipal
	public void actionRegisterAlicuota(ActionEvent event) throws SimlabAppException{
		Session session   = null;
		try {
			//Hacemos las Respectivas Validaciones para el Registro
			
			session = this.openSessionAndBeginTransaction();
			
		    alicToEdit.setCodUser(this.getLoginMbean().getCodeUser());
		    alicToEdit.setFehorReg(SimlabDateUtil.getCurrentDate());
			session.update(alicToEdit);	
			this.closeSessionAndCommitTransaction(session);
			this.setMode(0);
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,this.getTransactionTitle(), "Realizado"));
		} catch (SimlabAppException e) {
			SimlabAppException.addFacesMessageError(e);
			e.printStackTrace();
		}
	}
	
	
	//Registramos el EncPrincipal
	public void actionDeleteAlicuota(ActionEvent event) throws SimlabAppException{
		Session session   = null;
		session = this.openSessionAndBeginTransaction();
		String codAlic = alicToEdit.getId().getCodAlic();
		int posBox = alicToEdit.getId().getPosBox();
		int codFreezer = alicToEdit.getCodFreezer();
		String codRack = alicToEdit.getCodRack();
		int codBox = alicToEdit.getCodBox();
		String negPos = alicToEdit.getNegPos();
		Date fHoraReg = alicToEdit.getFehorReg();
		String codUser = alicToEdit.getCodUser();
		Float volAlic = alicToEdit.getVolAlic();
		Float pesoAlic = alicToEdit.getPesoAlic();
		String motivo = this.getMotCloseVigency();
		String usuario = this.getLoginMbean().getCodeUser();
		String tipo = alicToEdit.getTipo();
		String condicion = alicToEdit.getCondicion();
		String separada = alicToEdit.getSeparada();
		int numDes = alicToEdit.getNumDes();
		Query query = session.createSQLQuery("INSERT INTO `reg_alic_borradas` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
				"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `peso_alic`, `motivo`, `usuario`, `tipo`, `condicion`, `separada`, `num_desc`)" +
				" VALUES ('" + codAlic + "'," + posBox +", " + codFreezer + ", '" + codRack +"', " + codBox + ",  '" + negPos +"', " +
						"'" + fHoraReg +"', '" + codUser +"', " + volAlic + ", " + pesoAlic +",'" + motivo +"', '"+ usuario +"', '"+ tipo +"', '"+ condicion +"', '"+ separada +"'," + numDes +"); ");
		query.executeUpdate();
		query = session.createSQLQuery("Delete from reg_alic " +
				"where cod_alic = '" + alicToEdit.getId().getCodAlic() +"' and " +
				"pos_box = " + alicToEdit.getId().getPosBox());
		query.executeUpdate();
		this.closeSessionAndCommitTransaction(session);
		this.setMode(0);
		//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,this.getTransactionTitle(), "Realizado"));
	}


	public void uploadFile(FileUploadEvent event) {
		Session session   = null;
		Query query = null;

			//BufferedWriter writer = null;
			// Do what you want with the file
			try {
				session = this.openSessionAndBeginTransaction();
				Workbook wb = WorkbookFactory.create(event.getFile().getInputstream());
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				this.alicList.clear();

				while (rowIterator.hasNext())
				{
					Row row = rowIterator.next();
					//For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext()) {

						Cell nextCell = cellIterator.next();

						int columnIndex = nextCell.getColumnIndex();

						switch (columnIndex) {

							case 0:
								//First column
								if (nextCell.getCellType() == Cell.CELL_TYPE_STRING) {
									if (!nextCell.getStringCellValue().isEmpty()) {
										String col1 = nextCell.getStringCellValue();
										alicList.add(col1);
									}

									break;
								} else if (nextCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									if (nextCell.getNumericCellValue() != 0) {
										int col2 = (int) nextCell.getNumericCellValue();
										alicList.add(String.valueOf(col2));

									}
									break;
								}

							default:
								break;
						}
					}

				}
                wb.close();
				this.closeSessionAndCommitTransaction(session);
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Búsqueda finalizada." ,"Se encontraron  " + getListAlicByCode2().size() +  " " + "registros para" + " " + alicList.size() + " " + "códigos de alicuotas importados."));
			} catch (InvalidFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch(Exception e){
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Ha ocurrido un error" ,e.getLocalizedMessage()));
				e.printStackTrace();
			}
			finally {
				try {
					// Close the writer regardless of what happens...
					//writer.close();
				} catch (Exception e) {
				}
			}
	}

}
