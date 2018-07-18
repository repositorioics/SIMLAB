package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.sistema.hibernate.bussines.Entity;


@ManagedBean
@ViewScoped
public class HacerListaEnvioMbean extends GenericMbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String property ;
    private Collection<Entity> list ;

    public Collection<Entity> getList() {
        return list;
    }

    public void setList(Collection<Entity> list) {
        this.list = list;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public HacerListaEnvioMbean() {
        list = new ArrayList<Entity>();
        list.add(new Entity("William"));
        list.add(new Entity("Raul"));
        list.add(new Entity("Aviles"));
        list.add(new Entity("Monterrey"));
        
    }

    public void showInDataTable(){
        list.add(new Entity(property));
        list.add(new Entity("Aviles2"));
    }
}
