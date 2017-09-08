package apps.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
	private static final long serialVersionUID = 1L;
	private String datePattern;
    private SimpleDateFormat dateFormatter;;
    
    public DateLabelFormatter() {
    	datePattern = "dd/MM/yyyy";
    	dateFormatter = new SimpleDateFormat(datePattern);
    }
    
    @Override
    public Object stringToValue(String text) {
        Calendar cal = Calendar.getInstance();
        try {
			cal.setTime((Date) dateFormatter.parseObject(text));
		} catch (ParseException e) {
			return null;
		}
        return cal;
    }

    @Override
    public String valueToString(Object value)  {
        if (value != null) {
        	try {
        		return dateFormatter.format(((Calendar) value).getTime());
        	} catch (ClassCastException classCastException) {
        		//error
        	}
        }
        return "";
    }

	public SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}
    
}