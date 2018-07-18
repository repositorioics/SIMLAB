package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ni.gob.minsa.simlab.sistema.exception.SimlabAppException;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.services.simlabAlicuotaService;


@ManagedBean(name="busqSalMuestraMbean")
@ViewScoped
public class BusqSalMuestraMbean extends GenericMbean implements Serializable{

	private static final long serialVersionUID = 8523255793629321552L;
	
	private String codeAlicToFind = null;
	

	public String getCodeAlicToFind() {
		return codeAlicToFind;
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
}
