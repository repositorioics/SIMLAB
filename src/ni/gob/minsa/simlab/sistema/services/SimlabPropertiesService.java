package ni.gob.minsa.simlab.sistema.services;

import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import ni.gob.minsa.simlab.sistema.utileria.simlabStringUtils;

public class SimlabPropertiesService {

    /** Extension de los archivos de propiedades. */
    private static final String PROPS_PATH_UBIC     = "ni.gob.minsa.simlab.sistema.recursos.";
    private static final String PROPERTIES_MESSAGES = "simlab_messages";
    private static final String PROPERTIES_LABELS   = "simlab_label";
   
    /** Cache de acceso en memoria de las listas de propiedades. */
    private static HashMap<String, ResourceBundle> propertiesMap = new HashMap<String, ResourceBundle>();
    public static void resetPropertiesMap() { propertiesMap.clear(); }
    
    private SimlabPropertiesService() {
    }
    
    /** Obtiene el objeto Propiedades a partir de un nombre de archivo de propiedades pasado como parametro. */
    private static ResourceBundle getProperties(String resource) {
        // si no recibimos el archivo de propiedades, salimos
        if (simlabStringUtils.isNullOrEmpty(resource)) return null; 
        // obtenemos el objeto Propiedades de la variable cache
        resource = PROPS_PATH_UBIC + resource;
        ResourceBundle rb = propertiesMap.get(resource);
        // si no encontramos el objeto en la variable cache ...
        if (rb == null) {
            try { // cargamos el objeto propiedades directamente del archivo                
                rb = ResourceBundle.getBundle(resource);
                // guardamos el objeto propiedades en la variable cache
                propertiesMap.put(resource, rb);
            } catch (Exception ex) {
                Logger.getLogger(SimlabPropertiesService.class.getName()).log(Level.SEVERE, "Archivo de propiedades no existe. Archivo="+resource, ex);
            }
        }
        // retornamos el objeto propiedades
        return rb;
    }

    /** Obtiene el objeto Propiedades configurado para los Mensajes de sistema. */
    private static ResourceBundle getPropertiesMessages() {
        return getProperties(PROPERTIES_MESSAGES);
    }

    /** Obtiene la descripcion del mensaje configurado para el codigo pasado como parametro. */
    public static String getPropertiesMessageDescription(int code) {
        ResourceBundle properties = getPropertiesMessages(); 
        try {
        	return properties==null ? null : properties.getString(Integer.toString(code));
        } catch (MissingResourceException e) {
        	return null;	// si no encuentra la propiedad retornaremos nulo
        }
    }

    /** Obtiene la descripcion del mensaje configurado para el codigo y los parametro pasados al metodo. */
    public static String getPropertiesMessageDescription(int code, String... params) {
        String message = getPropertiesMessageDescription(code);
        if (message==null) return null;
        for (int i = 0; i < params.length; i++) {
            message = message.replace("{" + i + "}", params[i]);
        }
        return message;
    }

    /** Obtiene la descripcion completa del mensaje configurado para el codigo pasado como parametro. */
    public static String getPropertiesMessageToString(int code) {
        return "INT-" + code + ": " + getPropertiesMessageDescription(code);
    }

    /** Obtiene la descripcion completa del mensaje configurado para el codigo y los parámetro pasados al metodo. */
    public static String getPropertiesMessageToString(int code, String... params) {
        return "INT-" + code + ": " + getPropertiesMessageDescription(code, params);
    }
    
    /** Obtiene el objeto Propiedades configurado para las etiquetas de sistema. */
    private static ResourceBundle getPropertiesLabels() {
        return getProperties(PROPERTIES_LABELS);
    }

    /** Obtiene la descripcion de la etiqueta configurado para el codigo pasado como parametro. */
    public static String getPropertiesLabel(String code) {
        ResourceBundle properties = getPropertiesLabels();
        try {
        	return properties==null ? null : properties.getString(code);
        } catch (MissingResourceException e) {
        	return code;	// si no encuentra la propiedad retornaremos el codigo
        }
    }

    /** Obtiene la descripcion la etiqueta configurado para el codigo y los parametro pasados al metodo. */
    public static String getPropertiesLabel(String code, String... params) {
        String message = getPropertiesLabel(code);
        if (message==null) return null;
        for (int i = 0; i < params.length; i++) {
            message = message.replace("{" + i + "}", params[i]);
        }
        return message;
    }
	
}
