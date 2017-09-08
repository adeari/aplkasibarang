package Method;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MyFunction {
	public static String getStringForm(String string) {
		string = string.replace("\\r\\n", "\n");
		return string;
	}
	public static Date getDateFroString(String string) {
		if (string != null && (!string.isEmpty())) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				java.util.Date date = formatter.parse(string);
				return new Date(date.getTime());
			} catch (ParseException e) {
				System.out.println("eeee");
			}
		}
		return null;
	}
	public static String getNumberFormat(String text) {
		String textUsed = "";
		String character = "";
		int textlength = text.length();
		int decimal = 0;
		for (int i = textlength - 1; i > -1 ; i--) {
			decimal++;
			character = text.substring(i, i + 1);
			textUsed = character.concat(textUsed);
			if (i > 0 && decimal % 3 == 0) {
				textUsed = ".".concat(textUsed);
				decimal = 0;
			}
			
		}
		return textUsed;
	}
}
