package ni.gob.minsa.simlab.sistema.utileria;

/**Retornamos una Cadena con el prefijo y sufijo '%' para realizar busquedas 'como'*/
public class SimlabSqlUtils {
	
	public SimlabSqlUtils(){}
	
	public static String likeRightLef(String paramStrings){
		if(simlabStringUtils.isNullOrEmpty(paramStrings))return paramStrings;
		else return "%"+paramStrings+"%";
	}
}
