package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;
import ni.gob.minsa.simlab.sistema.services.simlabAlicuotaService;
import ni.gob.minsa.sistema.hibernate.bussines.AlicSalida;
import ni.gob.minsa.sistema.hibernate.bussines.RegAlic;


@ManagedBean(name="busqSalMuestraMbean")
@ViewScoped
public class BusqSalMuestraMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 8523255793629321552L;
	
	private String codeAlicToFind = null;
    private String titlePanel;
    private Integer mode;
    private AlicSalida alicToEdit;


    public AlicSalida getAlicToEdit() {
        return alicToEdit;
    }

    public void setAlicToEdit(AlicSalida alicToEdit) {
        this.alicToEdit = alicToEdit;
    }

    public String getCodeAlicToFind() {
		return codeAlicToFind;
	}

    public String getTitlePanel() {
        return titlePanel;
    }

    public void setTitlePanel(String titlePanel) {
        this.titlePanel = titlePanel;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public void setCodeAlicToFind(String codeAlicToFind) {
		this.codeAlicToFind = codeAlicToFind;
	}

	public BusqSalMuestraMbean() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public Collection<Object[]> getSamplesFound() throws SimlabAppException{
		
		Collection<Object[]> samplesFound = new ArrayList<Object[]>();
		samplesFound = simlabAlicuotaService.getMuestras(codeAlicToFind);
		return samplesFound;
	}


    public void actionCloseVigencyMuestra(ActionEvent event, AlicSalida alic){
        this.setTitlePanel(SimlabPropertiesService.getPropertiesLabel("edit_alic"));
        //Habilitamos la Vista para Cerrar la Vigencia del Aparato
        this.setAlicToEdit(alic);
        this.setMode(2);
    }
}
