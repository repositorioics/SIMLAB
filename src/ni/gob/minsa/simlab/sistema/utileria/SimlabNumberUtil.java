package ni.gob.minsa.simlab.sistema.utileria;

public class SimlabNumberUtil {
    
    private SimlabNumberUtil(){
    	super();
    }
    
    public static boolean areEquals(Number numberA, Number numberB) {
    	if (numberA==null && numberB==null) return true;
    	if (numberA!=null && numberB==null) return false;
    	if (numberA==null && numberB!=null) return false;
    	return Math.round(numberA.doubleValue()) == Math.round(numberB.doubleValue());
    }
    
    /** Determina si un numero se encuentro nulo. */
    public static boolean isNull(Number number) {
        return number==null;
    }
    
    /** Determina si numero no se encuentra nulo. */
    public static boolean isNotNull(Number number) {
        return !isNull(number);
    }
    
    /**Determina si un numero es mayor de cero.*/
    public static boolean isHigherToZero (Integer number){
    	return (number > 0);
    }

    public static boolean isNumber(String string) {
    	boolean result = false;
    	try {
    		Double.parseDouble(string);
    		result = true;
    	} catch (NumberFormatException e) {
    		result = false;
    	}
    	return result;
    }
    
    public static Long parseToLong(Object object) {
    	if (object == null) return null;
    	String string = object.toString();
    	if (simlabStringUtils.isNullOrEmpty(string)) return null;
    	if (!isNumber(string)) return null;
    	Long number = Long.valueOf(string);
    	return number;
    }
}