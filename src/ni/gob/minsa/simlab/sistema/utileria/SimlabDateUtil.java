package ni.gob.minsa.simlab.sistema.utileria;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import ni.gob.minsa.simlab.sistema.services.SimlabPropertiesService;


public class SimlabDateUtil {

	/** Fecha/Bandera del sistema que indica registro vigente. */
    public static final Date MAX_DATE = getDate(3000, 12, 31);
    /** Numero de dias en un anno. */
    public static final Integer DAY_OF_YEAR = 366;
    /** Numero de dias en un mes. */
    public static final Integer DAY_OF_MONTH = 31;
    /** Numero de dias en una semana. */
    public static final Integer DAY_OF_WEEK = 7;

    public static final Integer HOURS_OF_DAY = 24;
    
    private SimlabDateUtil() { }
    
    public static boolean areEqualsDate(Date dateA, Date dateB) {
    	return simlabStringUtils.changeNullToEmpty(getDateFormated(dateA)).equals(simlabStringUtils.changeNullToEmpty(getDateFormated(dateB)));
    }

    /** Obtiene la fecha/hora actual del sistema. */
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
    
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(getCalendarLocale().getTime().getTime());
    }
    
    /** Obtiene la fecha/hora de ma�ana del sistema. */
    public static Date getTomorrowDate() {
        return addDays(getCurrentDate(),1);
    }
    
    /** Obtiene la fecha actual como cadena de texto. */
    public static String getCurrentDateFormated() {
        return getDateFormated(getCurrentDate());
    }
    
    /** Obtiene la fecha/hora actual como cadena de texto. */
    public static String getCurrentDateTimeFormated() {
		return getDateTimeFormated(getCurrentDate());
    }
    
    /** Obtiene la fecha/hora actual como cadena de texto. */
    public static String getCurrentDateTimeDbFormated() {
		return getDateTimeDbFormated(getCurrentDate());
    }
    
    /** Obtiene la fecha como cadena de texto. */
    public static String getDateFormated(Date date) {
    	try {
	    	DateFormat formatter = new SimpleDateFormat(SimlabPropertiesService.getPropertiesLabel("default_date_pattern"));
	    	return formatter.format(date);
    	} catch (Throwable e) {
    		return getCurrentDate().toString();
    	}
    }
    
    /** Obtiene la fecha/hora como cadena de texto. */
    public static String getDateTimeFormated(Date date) {
    	try {
	    	DateFormat formatter = new SimpleDateFormat(SimlabPropertiesService.getPropertiesLabel("default_datetime_pattern"));
	    	return formatter.format(date);
    	} catch (Throwable e) {
    		return getCurrentDate().toString();
    	}
    }
    
    /** Obtiene la fecha como cadena de texto para la base de datos. */
    public static String getDateDbFormated(Date date) {
    	try {
	    	DateFormat formatter = new SimpleDateFormat(SimlabPropertiesService.getPropertiesLabel("default_date_pattern_db"));
	    	return formatter.format(date);
    	} catch (Throwable e) {
    		return getCurrentDate().toString();
    	}
    }
    
    /** Obtiene la fecha/hora como cadena de texto para la base de datos. */
    public static String getDateTimeDbFormated(Date date) {
    	try {
	    	DateFormat formatter = new SimpleDateFormat(SimlabPropertiesService.getPropertiesLabel("default_datetime_pattern_db"));
	    	return formatter.format(date);
    	} catch (Throwable e) {
    		return getCurrentDate().toString();
    	}
    }
    
    /** Obtiene el Calendar actual del sistema. */
    public static Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    /** Obtiene el año actual del sistema. */
    public static int getCurrentYear() {
        return getCurrentCalendar().get(Calendar.YEAR);
    }
    
    /** Obtienela hora actual del sistema. */
    public static int getHour24(Date date) {
        return dateToCalendar(date).get(Calendar.HOUR_OF_DAY);
    }

    /** Obtiene el año actual del sistema como cadena de dos digitos. */
    public static String getCurrentYear2d() {
        // TODO  aun no probado
        return Integer.toString(getCurrentYear()).substring(2, 4);
    }

    /** Obtiene la fecha formada a partir de los parámetros recibidos. */
    public static int getYear(Date date) {
        return dateToCalendar(date).get(Calendar.YEAR);
    }

    /** Obtiene la fecha formada a partir de los parámetros recibidos. */
    public static String getYear2d(Date date) {
        return Integer.toString(getYear(date)).substring(2, 4);
    }

    /** Obtiene la fecha formada a partir de los parámetros recibidos. */
    public static Date getDate(int year, int month, int day) {
        return new Date((new GregorianCalendar(year, month-1, day)).getTime().getTime());
    }

    /** Obtiene la fecha formada a partir de los parámetros recibidos. */
    public static Date getDate(Date date, int hour24, int minute, int second) {
    	Calendar calendar = dateToCalendar(date);
    	calendar.set(Calendar.HOUR_OF_DAY, hour24);
    	calendar.set(Calendar.MINUTE, minute);
    	calendar.set(Calendar.SECOND, second);
    	calendar.set(Calendar.MILLISECOND, 0);
        return calendarToDate(calendar);
    }
    
    /** Obtiene la Fecha y Hora actual como texto. */
    public static String getCurrentHour24MinuteAsString() {
    	Calendar calendar = getCurrentCalendar();
    	String hour24 = simlabStringUtils.fillStringAtLeft(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),simlabStringUtils.ZERO__STRING.charAt(0),2);
    	String minute = simlabStringUtils.fillStringAtLeft(String.valueOf(calendar.get(Calendar.MINUTE)),simlabStringUtils.ZERO__STRING.charAt(0),2);
    	return hour24+":"+minute;
    }
    
    /** Determina si el formato pasado como parametro tiene el formato de Hora:Minuto:Segundo siendo opcional los Segundos. */
    public static boolean isValidFormat(String hour24_minute_second) {
    	if (simlabStringUtils.isNullOrEmpty(hour24_minute_second)) return false;
    	return hour24_minute_second.matches("([0-1][0-9]|[2][0-3])\\:[0-5][0-9]")
    		|| hour24_minute_second.matches("([0-1][0-9]|[2][0-3])\\:[0-5][0-9]\\:[0-5][0-9]");
    }
    
    /** Obtiene la fecha formada a partir de los parámetros recibidos. */
    public static Date getDate(Date date, String hour24_minute_second) {
    	if (!isValidFormat(hour24_minute_second)) return getDateZeroHour(date);
    	Calendar calendar = dateToCalendar(date);
    	calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour24_minute_second.substring(0,2)));
    	calendar.set(Calendar.MINUTE, Integer.parseInt(hour24_minute_second.substring(3,5)));
    	calendar.set(Calendar.SECOND, hour24_minute_second.length()<=5 ? 0 : Integer.parseInt(hour24_minute_second.substring(7,9)));
    	calendar.set(Calendar.MILLISECOND, 0);
        return calendarToDate(calendar);
    }
    
    /** Obtiene la fecha informada con sus valores de Hora, Minuto y Segundos en Cero */
    public static Date getDateZeroHour(Date date) {
    	return getDate(date, 0, 0, 0);
    }
    
//    /** Obtiene una representacion de texto de la fecha pasada como parametro. */
//    public static String toString(Date date, boolean incluirHora) {
//    	SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy"+(incluirHora?" hh24:mi":""));
//        return sdf.format(date);
//    }
    
    private static Calendar getCalendarLocale() {
    	Locale locale = new Locale("es_NI");
    	try {
//    	TODO: revisar	locale = SissFacesUtils.getLocaleFromContext();
    	} catch (Throwable ex) { /* no haremos nada, pasaremos el valor por defecto */ }
        return Calendar.getInstance(locale);
    }

    /** Convierte un objeto Date a un objeto Calendar. */
    public static Calendar dateToCalendar(Date date) {
        //return new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes(), date.getSeconds());
        Calendar calendar = getCalendarLocale();
        calendar.setTime(date);
        return calendar;
    }

    /** Convierte un objeto Calendar a un objeto Date. */
    public static Date calendarToDate(Calendar calendar) {
        return new Date(calendar.getTimeInMillis());
    }

    /** Obtiene una fecha despues de agregar la cantidad de Años pasados como parametro. */
    public static Date addYears(Date date, int years) {
        Calendar calendar = dateToCalendar(date);
        calendar.add(Calendar.YEAR, years);
        return calendarToDate(calendar);
    }
    
    /** Obtiene una fecha despues de agregar la cantidad de Meses pasados como parametro. */
    public static Date addMonths(Date date, int years) {
        Calendar calendar = dateToCalendar(date);
        calendar.add(Calendar.MONTH, years);
        return calendarToDate(calendar);
    }

    /** Obtiene una fecha despues de agregar la cantidad de dias pasados como parametro. */
    public static Date addDays(Date date, int days) {
        Calendar calendar = dateToCalendar(date);
        calendar.add(Calendar.DATE, days);
        return calendarToDate(calendar);
    }
    
    /** Obtiene una fecha despues de agregar la cantidad de Minutos pasados como parametro. */
    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = dateToCalendar(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendarToDate(calendar);
    }
    
    /** Obtiene una fecha despues de agregar la cantidad de Horas pasados como parametro. */
    public static Date addHours(Date date, int years) {
        Calendar calendar = dateToCalendar(date);
        calendar.add(Calendar.HOUR_OF_DAY, years);
        return calendarToDate(calendar);
    }

    public static Date addHoursToCurrent(int hours) {
        return addHours(getCurrentDate(), hours);
 	}

    public static Date addDaysToCurrent(int days) {
        return addDays(getCurrentDate(), days);

    }
    /* Valida el rango de fecha*/
    public static boolean isWithinRange(Date date, Date iniDate, Date finDate) {
        if (date == null || iniDate == null || finDate == null) {
            return false;
        }
        return (iniDate.before(date) || iniDate.equals(date)) && (date.before(finDate) || date.equals(finDate));
    }
    
    public static double diffYears(Date datePre, Date datePos) {
    	Long diffDays = diffDays(datePre, datePos);
		return diffDays.doubleValue() / 365.25;
    }

    /** Obtiene la diferencia de fechas en Dias. */
    public static long diffDays(Date datePre, Date datePos) {
        final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
        return (datePos.getTime() - datePre.getTime()) / MILLSECS_PER_DAY;
    }

    /** Obtiene la diferencia de fechas en Minutos. */
    public static long diffMinutes(Date datePre, Date datePos) {
        final long MILLSECS_PER_MIN = 60 * 1000;
        return (datePos.getTime() - datePre.getTime()) / MILLSECS_PER_MIN;
    }
    
    /**Obtiene la diferencia de fecha en Horas*/
    public static long getDiffHours(Date datePre, Date datePos){
    	final long MILLSECS_PER_HOUR =  60 * 60 * 1000;
    	return (datePos.getTime() - datePre.getTime()) / MILLSECS_PER_HOUR;
    }

    /** Determina si la fecha se encuentra nulo. */
    public static boolean isNull(Date date) {
        return date == null;
    }

    /** Determina si la fecha no se encuentra nula. */
    public static boolean isNotNull(Date date) {
        return !isNull(date);
    }
    
    /**Determina si una fecha es mayor que la otra
     * @throws SissAppException */
    public static boolean beforeEqual(Date date1, Date date2) {
    	 return date1.before(date2) || date1.toString().equals(date2.toString());
    }
    
    public static boolean before(Date date1, Date date2) {
    	return date1.before(date2);
    }
    
    public static boolean afterEqual(Date date1, Date date2) {
    	return date1.after(date2) || date1.toString().equals(date2.toString());
    }
    
    public static boolean after(Date date1, Date date2) {
    	return date1.after(date2);
    }
    
    public static Date getBestReferenceDate(Date dateIni, Date dateEnd) {
    	Date currentDate = SimlabDateUtil.getCurrentDate();
    	if (dateIni==null || dateEnd==null) return null;
    	if (dateIni.after(dateEnd)) return null;
    	if (currentDate.before(dateIni)) return dateIni;
    	if (currentDate.after(dateEnd)) return dateEnd;
    	return currentDate;
    }
    
    @SuppressWarnings("deprecation")
	public static Date getLastDateOfMonth(Date date) {
    	Date result = null;
    	if (date.getMonth()==Calendar.DECEMBER) {
    		result = getDate(date.getYear()+1, 1, 1);
    	} else {
    		result = addDays(getDate(date.getYear(), date.getMonth()+1, 1),-1);
    	}
    	return result;
    }
    
    /**Obtiene el ultimo dia del mes*/
    public static Date getLastDayOfMonth(int year, int month, int day){
    	Calendar calendarioStart=Calendar.getInstance() ;
    	Calendar calendarioEnd = Calendar.getInstance();
    	calendarioStart.set(year, month-1, day);
    	calendarioEnd.set(year, month-1, calendarioStart.getActualMaximum(Calendar.DAY_OF_MONTH));
    	return calendarToDate(calendarioEnd);
    	
    }

	
}
