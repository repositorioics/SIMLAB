package ni.gob.minsa.simlab.sistema.utileria;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Utileria para Manejo de Cadenas*/
public class simlabStringUtils {

	public static final String EMPTY_STRING = "";
    public static final String GUION_STRING = "-";
    public static final String SPACE_STRING = " ";
    public static final String COMMA_STRING = ",";
    public static final String PAROP_STRING = "(";
    public static final String PARCL_STRING = ")";
    public static final String SNUMB_STRING = "#";
    public static final String ZERO__STRING = "0";
    public static final String ASTERIS = "*";
    
    
    private simlabStringUtils() {}
    
    public static boolean areEquals(String stringA, String stringB) {
    	return changeNullToEmpty(stringA).equals(changeNullToEmpty(stringB));
    }
    
    public static String replace(String string, String stringOri, String stringNew) {
    	if (simlabStringUtils.isNullOrEmpty(string)) return string;
    	return string.replace(changeNullToEmpty(stringOri),changeNullToEmpty(stringNew));
    }
    
    /** Determina si una cadena de texto es nula o vacía. */
    public static boolean isNullOrEmpty(String string) {
        return string==null || string.trim().length()==0;
    }
    
    /** Determina si una cadena de texto no esta vacia. */
    public static boolean isNotEmpty(String string) {
        return !isNullOrEmpty(string);
    }
    
    /** Forma una cadena de texto a partir de la repeticion de un caracter */
    public static String repeatCharacter(char character, int newSize) {
        String result = EMPTY_STRING;
        for (int i = 0; i < newSize; i++) result+=character;
        return result;
    }
    
    /** Rellena una cadena con caracteres a la izquierda hasta alcanzar el tamano pasado como parametro. */
    public static String fillStringAtLeft(String string, char character, int newSize) {
        // si la cadena viene nula, la asumimos vacia
        if (isNullOrEmpty(string)) string = EMPTY_STRING;
        // si el tamano viene menor o igual a cero, retornamos sin procesar
        if (newSize <= 0) return string;
        // formamos la cadena repitiendo los caracteres que hagan falta y anexando la cadena original
        return repeatCharacter(character, newSize-string.length()) + string;
    }
    
    public static String cutToLength(String string, int length){
        //si es nula o vacia retornamos
        if (isNullOrEmpty(string)) return string ;
        // si la long del String es inferior a la longitud returnamos la cadena original
        if (string.length()<length) return string;
        //retornamos la cadena recortada
        return string.substring(0, length);
    }
    
    public static String cutToLenght(String string, int beginIndex, int endIndex){
    	//si es nula o vacia retornamos
        if (isNullOrEmpty(string)) return string ;
        // si la long del String es inferior a la longitud returnamos la cadena original
//        if (string.length()<length) return string;
        //retornamos la cadena recortada
        return string.substring(beginIndex, endIndex);	
    }
    
    public static String trimUpper(String string){
        // si es nula o vacia retornamos
        if (isNullOrEmpty(string)) return EMPTY_STRING;
        //procesamos y retornamos la cadena
        return string.trim().toUpperCase();
    }
    
    public static String trimUpperCut(String string, int length){
        // si es nula o vacia retornamos
        if (isNullOrEmpty(string)) return EMPTY_STRING;
        //procesamos y retornamos la cadena
        return cutToLength(string.trim().toUpperCase(), length);
    }
    
    public static String changeNullToEmpty(String string) {
        return isNullOrEmpty(string) ? EMPTY_STRING : string;
    }
    
    public static String getStringWithParams(String string, String... params) {
        for (int i = 0; i < params.length; i++) {
            string = string.replace("{" + i + "}", simlabStringUtils.changeNullToEmpty(params[i]));
        }
        return string;
    }
    
    public static boolean existAnySpecialCharacter(String string, boolean isSpaceSpecialCharacter) {
    	if (!isSpaceSpecialCharacter) string = simlabStringUtils.changeNullToEmpty(string).replace(" ",""); 
    	Pattern patron = Pattern.compile("[^A-ZÃ�Ã‰Ã�Ã“ÃšÃ‘ñÑ]");
		Matcher mat = patron.matcher(string);
		return mat.find();
    }
    
    public static String toUpper(String string){
    	if(simlabStringUtils.isNullOrEmpty(string))return string;
    	return string.toUpperCase();
   }

	public static String cutToLength(String codeAlic, String indiceCodigo2) {
		
        //si es nula o vacia retornamos
        if (isNullOrEmpty(codeAlic)) return codeAlic ;
        // si la long del String es distinta retornar� la cadena original
        if (codeAlic != indiceCodigo2) return codeAlic;
        //retornamos la cadena recortada
        return codeAlic.substring(0,8);
    
		
		
	}
	
}
