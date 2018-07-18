package ni.gob.minsa.sistema.hibernate.bussines;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class Entity {
	
	private String entityProperty ;

    public String getEntityProperty() {
        return entityProperty;
    }

    public void setEntityProperty(String entityProperty) {
        this.entityProperty = entityProperty;
    }


    public Entity(String e) {
        this.entityProperty = e ;
    }

}
