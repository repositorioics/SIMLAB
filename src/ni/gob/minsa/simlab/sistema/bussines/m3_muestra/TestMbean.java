package ni.gob.minsa.simlab.sistema.bussines.m3_muestra;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import ni.gob.minsa.simlab.sistema.mbean.GenericMbean;
import ni.gob.minsa.simlab.sistema.model.Car;
import ni.gob.minsa.simlab.sistema.services.CarService;


@ManagedBean(name="testMbean")
@ViewScoped
public class TestMbean extends GenericMbean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Car> cars;
    
    private Car selectedCar;
     
    @ManagedProperty("#{carService}")
    private CarService service;
     
    @PostConstruct
    public void init() {
        cars = service.createCars(81);
    }
 
    public List<Car> getCars() {
        return cars;
    }
 
    public void setService(CarService service) {
        this.service = service;
    }
 
    public Car getSelectedCar() {
        return selectedCar;
    }
 
    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }
	
}
