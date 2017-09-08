package apps.component;

import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JComboBox;

public class Combobox extends JComboBox<Object> {
	private static final long serialVersionUID = 1L;
	public Combobox() {
		super();
		setFont(new Font("Arial", Font.PLAIN, 22));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
}
