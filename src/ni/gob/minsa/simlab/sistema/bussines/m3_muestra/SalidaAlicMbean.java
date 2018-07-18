package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.event.FileUploadEvent;

import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabEquipService;
import ni.gob.minsa.sistema.hibernate.bussines.Caja;
import ni.gob.minsa.sistema.hibernate.bussines.Rack;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;


@ManagedBean(name="salidaAlicMbean")
@ViewScoped
public class SalidaAlicMbean extends GenericMbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigoAlic;
	private String destAlic;
	
	private String eNic;
	private String medioTrans;
	private String numCont;
	private String transporta;
	private String respBusqueda;
	private String respEmbalaje;
	private String numCaja;
	private String solicita;
	private String aprueba;
	private String proposito;
	
	Collection<RegAlic> listAlicuotas = new ArrayList<RegAlic>();
	Collection<String> listNoEncontradas = new ArrayList<String>();
	//private String destination="D:\\test\\";  
	String resultSQL = "";
	private String tipo = null;
	private Date fechaSalida = new Date();
	private Integer filas = 9;
	private Integer columnas = 9;
	
	
	public SalidaAlicMbean() {
		super();	
	}
	
	public Collection<RegAlic> getListAlicuotas() {
		return listAlicuotas;
	}

	public void setListAlicuotas(Collection<RegAlic> listAlicuotas) {
		this.listAlicuotas = listAlicuotas;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	

	public String geteNic() {
		return eNic;
	}

	public void seteNic(String eNic) {
		this.eNic = eNic;
	}
	
	

	public String getMedioTrans() {
		return medioTrans;
	}

	public void setMedioTrans(String medioTrans) {
		this.medioTrans = medioTrans;
	}

	
	public String getNumCont() {
		return numCont;
	}

	public void setNumCont(String numCont) {
		this.numCont = numCont;
	}

	
	public String getTransporta() {
		return transporta;
	}

	public void setTransporta(String transporta) {
		this.transporta = transporta;
	}

	
	public String getRespBusqueda() {
		return respBusqueda;
	}

	public void setRespBusqueda(String respBusqueda) {
		this.respBusqueda = respBusqueda;
	}

	public String getRespEmbalaje() {
		return respEmbalaje;
	}

	public void setRespEmbalaje(String respEmbalaje) {
		this.respEmbalaje = respEmbalaje;
	}

	
	public String getNumCaja() {
		return numCaja;
	}

	public void setNumCaja(String numCaja) {
		this.numCaja = numCaja;
	}

	public String getSolicita() {
		return solicita;
	}

	public void setSolicita(String solicita) {
		this.solicita = solicita;
	}

	public String getAprueba() {
		return aprueba;
	}

	public void setAprueba(String aprueba) {
		this.aprueba = aprueba;
	}

	public String getProposito() {
		return proposito;
	}

	public void setProposito(String proposito) {
		this.proposito = proposito;
	}

	public Integer getFilas() {
		return filas;
	}

	public void setFilas(Integer filas) {
		this.filas = filas;
	}

	public Integer getColumnas() {
		return columnas;
	}

	public void setColumnas(Integer columnas) {
		this.columnas = columnas;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public Collection<String> getListNoEncontradas() {
		return listNoEncontradas;
	}

	public void setListNoEncontradas(Collection<String> listNoEncontradas) {
		this.listNoEncontradas = listNoEncontradas;
	}

	public String getCodigoAlic() {
		return codigoAlic;
	}

	public void setCodigoAlic(String codigoAlic) {
		this.codigoAlic = codigoAlic;
	}
	

	
	public void actionCancel(ActionEvent event) {
		this.setCodigoAlic(null);
		this.setDestAlic(null);
		this.listAlicuotas.clear();
		this.listNoEncontradas.clear();
	}
	
	public void actionRemoveAlic(ActionEvent event, RegAlic alic){
		//Habilitamos la Vista para quitar la alicuota
		this.listAlicuotas.remove(alic);
	}
	
	public void actionRemoveAlicNe(ActionEvent event, String alicNe){
		//Habilitamos la Vista para quitar la alicuota
		this.listNoEncontradas.remove(alicNe);
	}

	
	public void actionProcess(ActionEvent event) {
		Session session   = null;
		DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try{
			if (!this.listAlicuotas.isEmpty()){
				if (this.destAlic != null && !this.destAlic.equals("")){
					session = this.openSessionAndBeginTransaction();
					Query query;
					for (RegAlic alicuota:listAlicuotas){
						String codAlic = alicuota.getId().getCodAlic();
						int posBox = alicuota.getId().getPosBox();
						int codFreezer = alicuota.getCodFreezer();
						String codRack = alicuota.getCodRack();
						int codBox = alicuota.getCodBox();
						String negPos = alicuota.getNegPos();
						Date fHoraReg = alicuota.getFehorReg();
						String codUser = alicuota.getCodUser();
						Float volAlic = alicuota.getVolAlic();
						String tipo = alicuota.getTipo();
						String condicion = alicuota.getCondicion();
						String separada = alicuota.getSeparada();
						Float pesoAlic = alicuota.getPesoAlic();
						String destino = this.getDestAlic();
						String usuario = this.getLoginMbean().getCodeUser();
						int numDes = alicuota.getNumDes();
						
						query = session.createSQLQuery("INSERT INTO `reg_alic_salidas` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
								"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `peso_alic`, `destino`, `usuario`, `tipo`, `condicion`, `separada`, `fecha_salida`, `num_desc`, " +
								"`eNic`, `medioTrans`, `numCont`, `transporta`, `respBusqueda`, `respEmbalaje`, `numCaja`, `solicita`, `aprueba`, `proposito`)" +
								" VALUES ('" + codAlic + "'," + posBox +", " + codFreezer + ", '" + codRack +"', " + codBox + ",  '" + negPos +"', " +
										"'" + fHoraReg +"', '" + codUser +"', " + volAlic + ", " + pesoAlic +",'" + destino +"', '"+ usuario +"', '"+ tipo +"', '"+ condicion +"', '"+ separada +"', '" + mDateFormat.format(this.fechaSalida) + "'," + numDes +", " +
								"'" + eNic + "','" + medioTrans + "','" + numCont + "','" + transporta + "','" + respBusqueda + "','" + respEmbalaje + "','" + numCaja + "','" + solicita + "','" + aprueba + "','" + proposito + "'); ");
						query.executeUpdate();
						query = session.createSQLQuery("Delete from reg_alic " +
								"where cod_alic = '" + alicuota.getId().getCodAlic() +"' and " +
								"pos_box = " + alicuota.getId().getPosBox() + " and tipo ='" + tipo +"'");
						query.executeUpdate();
					}
					this.closeSessionAndCommitTransaction(session);
					
					this.listAlicuotas.clear();
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Finalizado" ,"El proceso se ha finalizado con exito"));
				}
				else{
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Requerido" ,"Debe entrar el destino de las alicuotas"));
				}
			}
			else{
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Vacía" ,"La lista de alicuotas esta vacía"));
			}
			if (!this.listNoEncontradas.isEmpty()){
				if (this.destAlic != null && !this.destAlic.equals("")){
					session = this.openSessionAndBeginTransaction();
					Query query;
					for (String alicuota:listNoEncontradas){
						String codAlic = alicuota;
						int posBox = 0;
						int codFreezer = 0;
						String codRack = "SR";
						int codBox = 0;
						String negPos = "SR";
						Date fHoraReg = new Timestamp(new Date().getTime());
						String codUser = "SU";
						Float volAlic = 0F;
						String tipo = this.tipo;
						String condicion = "SC";
						String separada = "SA";
						Float pesoAlic = 0F;
						String destino = this.getDestAlic();
						String usuario = this.getLoginMbean().getCodeUser();
						int numDes = 0;
						query = session.createSQLQuery("INSERT INTO `reg_alic_salidas` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
								"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `peso_alic`, `destino`, `usuario`, `tipo`, `condicion`, `separada`, `fecha_salida`, `num_desc`, " +
								"`eNic`, `medioTrans`, `numCont`, `transporta`, `respBusqueda`, `respEmbalaje`, `numCaja`, `solicita`, `aprueba`, `proposito`)" +
								" VALUES ('" + codAlic + "'," + posBox +", " + codFreezer + ", '" + codRack +"', " + codBox + ",  '" + negPos +"', " +
										"'" + fHoraReg +"', '" + codUser +"', " + volAlic + ", " + pesoAlic +",'" + destino +"', '"+ usuario +"', '"+ tipo +"', '"+ condicion +"', '"+ separada +"', '" + mDateFormat.format(this.fechaSalida) + "'," + numDes +", " +
								"'" + eNic + "','" + medioTrans + "','" + numCont + "','" + transporta + "','" + respBusqueda + "','" + respEmbalaje + "','" + numCaja + "','" + solicita + "','" + aprueba + "','" + proposito + "'); ");
						query.executeUpdate();
					}
					this.closeSessionAndCommitTransaction(session);
					
					this.listAlicuotas.clear();
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Finalizado" ,"El proceso se ha finalizado con exito"));
				}
				else{
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Requerido" ,"Debe entrar el destino de las alicuotas"));
				}
			}
			else{
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"Vacía" ,"La lista de alicuotas esta vacía"));
			}
		}
		catch(Exception e){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
													"Ha ocurrido un error" ,e.getLocalizedMessage()));
			e.printStackTrace();
		}
		this.setCodigoAlic(null);
		this.setNumCaja(null);
		
	}
	
	

	public String getDestAlic() {
		return destAlic;
	}

	public void setDestAlic(String destAlic) {
		this.destAlic = destAlic;
	}
	
	/*public void actionCrearExcel(ActionEvent event){

		Workbook wb = new HSSFWorkbook();  // or new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet1 = wb.createSheet("new sheet");
		Sheet sheet2 = wb.createSheet("second sheet");
		// Create a row and put some cells in it. Rows are 0 based.
		Row row = sheet1.createRow((short)0);
		// Create a cell and put a value in it.
		Cell cell = row.createCell(0);
		cell.setCellValue(1);

		// Or do it on one line.
		row.createCell(1).setCellValue(1.2);
		row.createCell(2).setCellValue(
				createHelper.createRichTextString("This is a string"));
		row.createCell(3).setCellValue(true);



		for(int i=0;i<9;i++){
			Row row2 = sheet2.createRow(i);
			for(int j=0;j<13;j++){
				Cell cell2 = row2.createCell(j);
				cell2.setCellValue(i+"-"+j);
			}
		}
		HttpServletResponse response =
				(HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=test.xls");
		try {
			wb.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().responseComplete();



	}*/
        
        /*try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
 
    /*public void copyFile(String fileName, InputStream in) {
           try {
              
              
                // write the inputStream to a FileOutputStream
                OutputStream out = new FileOutputStream(new File(destination + fileName));
              
                int read = 0;
                byte[] bytes = new byte[1024];
              
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
              
                in.close();
                out.flush();
                out.close();
                } catch (IOException e) {
                System.out.println(e.getMessage());
                }
    }*/
    
    public void upload(FileUploadEvent event) {  
		Session session   = null;
		Query query = null;
		this.listAlicuotas.clear();
		this.listNoEncontradas.clear();
    	DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	if(tipo==null) {
    		FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
													"Ha ocurrido un error" ,"No ha seleccionado tipo de alicuota"));
    	}
    	else {
    	//BufferedWriter writer = null;
        // Do what you want with the file    
        try {
        	//String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".sql";
        	//File logFile = new File(this.getDestAlic()+"-"+timeLog);
			//writer = new BufferedWriter(new FileWriter(logFile));
        	session = this.openSessionAndBeginTransaction();
			Workbook wb = WorkbookFactory.create(event.getFile().getInputstream());
			Sheet sheet = wb.getSheetAt(0);
			for (int j=0;j<=filas-1;j++){
		    Row row = sheet.getRow(j);
		    	if(row!=null) {
				    for (int i=0;i<=columnas-1;i++){
				    	Cell cell = row.getCell(i);
				    	if (cell!=null) {
				    		String codigoBuscar = cell.toString().trim();
				            System.out.println(codigoBuscar + " - fila:" + (j+1) + " columna:"+ (i+1));
				            if(tipo.equals("estudios")) {
				            	query = session.createQuery("FROM RegAlic reg WHERE lower(reg.id.codAlic) = lower('"+codigoBuscar+"') and tipo = 'estudios'");
				            }
				            else if(tipo.equals("muestreoanual")){
				            	query = session.createQuery("FROM RegAlic reg WHERE lower(reg.id.codAlic) = lower('"+codigoBuscar+"')  and (tipo = 'muestreoanual')");
				            }
				            else if(tipo.equals("muestreoanualchf")){
				            	query = session.createQuery("FROM RegAlic reg WHERE lower(reg.id.codAlic) = lower('"+codigoBuscar+"')  and (tipo = 'muestreoanualchf')");
				            }
							RegAlic result = (RegAlic) query.uniqueResult();
							if (result == null){
								if(codigoBuscar != null && !codigoBuscar.equals("")) {
									this.listNoEncontradas.add(codigoBuscar);
									String codAlic = codigoBuscar;
									int posBox = 0;
									int codFreezer = 0;
									String codRack = "SR";
									int codBox = 0;
									String negPos = "SR";
									Date fHoraReg = new Timestamp(new Date().getTime());
									String codUser = "SU";
									Float volAlic = 0F;
									String tipo = this.tipo;
									String condicion = "SC";
									String separada = "SA";
									Float pesoAlic = 0F;
									String destino = this.getDestAlic();
									String usuario = this.getLoginMbean().getCodeUser();
									resultSQL = "INSERT INTO `reg_alic_salidas` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
											"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `peso_alic`, `destino`, `usuario`, `tipo`, `condicion`, `separada`, `fecha_salida`)" +
											" VALUES ('" + codAlic + "'," + posBox +", " + codFreezer + ", '" + codRack +"', " + codBox + ",  '" + negPos +"', " +
													"'" + fHoraReg +"', '" + codUser +"', " + volAlic + ", " + pesoAlic +",'" + destino +"', '"+ usuario +"', '"+ tipo +"', '"+ condicion +"', '"+ separada +"', '" + mDateFormat.format(this.fechaSalida) + "'); " + System.lineSeparator();
									//writer.write(resultSQL);
								}
							}else {
								Caja caja = SimlabEquipService.getCajaByCode(String.valueOf(result.getCodBox()));
								if(caja!=null) {
									Rack rack = SimlabEquipService.getRack(caja.getId().getCcodRack());
									result.setCodFreezer(rack.getId().getRcodFreezer());
									result.setCodRack(rack.getId().getCodRack());
								}
								if (!this.listAlicuotas.contains(result)){
									this.listAlicuotas.add(result);
								}
								String codAlic = result.getId().getCodAlic();
								int posBox = result.getId().getPosBox();
								int codFreezer = result.getCodFreezer();
								String codRack = result.getCodRack();
								int codBox = result.getCodBox();
								String negPos = result.getNegPos();
								Date fHoraReg = result.getFehorReg();
								String codUser = result.getCodUser();
								Float volAlic = result.getVolAlic();
								String tipo = result.getTipo();
								String condicion = result.getCondicion();
								String separada = result.getSeparada();
								Float pesoAlic = result.getPesoAlic();
								String destino = this.getDestAlic();
								String usuario = this.getLoginMbean().getCodeUser();
								resultSQL = "INSERT INTO `reg_alic_salidas` (`cod_alic`, `pos_box`, `cod_freezer`, `cod_rack`, `cod_box`, " +
										"`neg_pos`, `fehor_reg`, `cod_user`, `vol_alic`, `peso_alic`, `destino`, `usuario`, `tipo`, `condicion`, `separada`, `fecha_salida`)" +
										" VALUES ('" + codAlic + "'," + posBox +", " + codFreezer + ", '" + codRack +"', " + codBox + ",  '" + negPos +"', " +
												"'" + fHoraReg +"', '" + codUser +"', " + volAlic + ", " + pesoAlic +",'" + destino +"', '"+ usuario +"', '"+ tipo +"', '"+ condicion +"', '"+ separada +"', '" + mDateFormat.format(this.fechaSalida) + "');" + System.lineSeparator();
								//writer.write(resultSQL);
								resultSQL = "Delete from reg_alic " +
										"where (cod_alic = '" + codAlic +"' and " +
										"pos_box = " + posBox + " and tipo = '"+ tipo + "');"+ System.lineSeparator();
								//writer.write(resultSQL);
							}
				    	}
				    }
		    	}
			}
		    wb.close();
		    this.closeSessionAndCommitTransaction(session);
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"Finalizado" ,"El proceso se ha finalizado con exito"));
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
}
