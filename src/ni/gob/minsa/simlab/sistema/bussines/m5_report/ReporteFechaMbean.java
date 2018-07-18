package ni.gob.minsa.simlab.sistema.bussines.m5_report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.simlabAlicuotaService;


@ManagedBean(name="reporteFechaMbean")
@ViewScoped
public class ReporteFechaMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 8523255793629321552L;
	
	private String titlePanel;
	private Date fechaInicio=null;
	private Date fechaFin=null;

    private List<ColumnModel> datosResultadosColumns;

    private Collection<Object[]> datosResultados = null;
 
    public Collection<Object[]> getDatosResultados() {
		return datosResultados;
	}

	public void setDatosResultados(Collection<Object[]> datosResultados) {
		this.datosResultados = datosResultados;
	}

	public List<ColumnModel> getDatosResultadosColumns() {
		return datosResultadosColumns;
	}

	public void setDatosResultadosColumns(List<ColumnModel> datosResultadosColumns) {
		this.datosResultadosColumns = datosResultadosColumns;
	}
	
	
    
    private void createDynamicColumns() {
    	String[] columnKeys = simlabAlicuotaService.getEncabezadosDatosReporteRecursos(fechaInicio, fechaFin);
    	datosResultadosColumns = new ArrayList<ColumnModel>();   
        
    	datosResultadosColumns.add(new ColumnModel("fecha", "fecha"));
    	datosResultadosColumns.add(new ColumnModel("total", "total"));
        for(String columnKey : columnKeys) {
            datosResultadosColumns.add(new ColumnModel(columnKey.toUpperCase(), columnKey));
        }
        
       datosResultados = new ArrayList<Object[]>();
		if (fechaInicio==null || fechaFin==null) {
			
		}
		else {
			datosResultados = simlabAlicuotaService.getDatosReporteRecursos(fechaInicio,fechaFin);
		}
    }
     
    public void updateColumns() {
        //reset table state
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:datosResultados");
        table.setValueExpression("sortBy", null);
        //update columns
        createDynamicColumns();
    }
	
	public String getTitlePanel() {
		return titlePanel;
	}

	public void setTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}
	
	
	
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public ReporteFechaMbean() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	static public class ColumnModel implements Serializable {
		 
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String header;
        private String property;
 
        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }
 
        public String getHeader() {
            return header;
        }
 
        public String getProperty() {
            return property;
        }
    }

}
