package apps.component;

import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JCheckBox;

public class CheckboxK extends JCheckBox {
	private static final long serialVersionUID = 1L;

	public CheckboxK(String label) {
		super(label);
		setFont(new Font("Arial", Font.BOLD, 22));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
}
