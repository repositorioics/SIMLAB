/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ni.gob.minsa.simlab.sistema.utileria;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hsomoza
 */
public class SimlabFacesUtils {
    
    public static Object getValueExpression(String expression, Class<?> expectedType) {
    	// si vienen los parametros vacios, salimos retornando nulo
    	if (simlabStringUtils.isNullOrEmpty(expression) || expectedType==null) return null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.el.ELContext elContext = facesContext. getELContext();
        javax.el.ExpressionFactory ef = facesContext.getApplication().getExpressionFactory();
        javax.el.ValueExpression ve = ef.createValueExpression(elContext, expression, expectedType);
        return ve.getValue(elContext);
    }
    
    public static void setValueExpression(String expression, Class<?> expectedType, Object value) {
    	// si vienen los parametros vacios, salimos
    	if (simlabStringUtils.isNullOrEmpty(expression) || expectedType==null) return;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.el.ELContext elContext = facesContext. getELContext();
        javax.el.ExpressionFactory ef = facesContext.getApplication().getExpressionFactory();
        javax.el.ValueExpression ve = ef.createValueExpression(elContext, expression, expectedType);
        ve.setValue(elContext, value);
    }
    
    public static MethodExpression createAction(String actionExpression, Class<?> returnType) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory().createMethodExpression(context.getELContext(), actionExpression, returnType, new Class[0]);
    }

    public static MethodExpressionActionListener createActionListener(String actionListenerExpression) {
        FacesContext context = FacesContext.getCurrentInstance();
        return new MethodExpressionActionListener(context.getApplication().getExpressionFactory().createMethodExpression(context.getELContext(), actionListenerExpression, null, new Class[] {ActionEvent.class}));
    }
    
    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public static HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }
    
    public static void sendRedirect(String url) {
        try {
            getHttpServletResponse().sendRedirect(url);
        } catch (Exception ex) { // no haremos nada!
            Logger.getLogger(SimlabFacesUtils.class.getName()).log(Level.SEVERE, "No se pudo redirigir. URL="+url, ex);
        }
    }
    
    public static javax.servlet.http.HttpSession getHttpSession() {
        return getHttpServletRequest().getSession();
    }

    public static Object getRequestAttribute(String attribute, Object value) {
        return getHttpServletRequest().getAttribute(attribute);
    }   
        
    public static void setRequestAttribute(String attribute, Object value) {
        getHttpServletRequest().setAttribute(attribute, value);
    }   

    public static String getRequestParameter(String parameter) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parameter);
    }   
        
    public static void setRequestParameter(String parameter, String value) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().put(parameter,value);
    }   
        
    public static void setLocaleOnContext(Locale locale) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(locale);
    }
    
    public static Locale getLocaleFromContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getViewRoot().getLocale();
    }
    
    public static void removeObjectFromContext(String name) {
        FacesContext context = FacesContext.getCurrentInstance(); 
        context.getExternalContext().getSessionMap().remove(name); 
    }
    
    
   }
