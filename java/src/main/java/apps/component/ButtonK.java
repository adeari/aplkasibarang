package apps.component;

import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JButton;

public class ButtonK extends JButton {
	private static final long serialVersionUID = 1L;

	public ButtonK(String label) {
		super(label);
		setFont(new Font("Arial", Font.BOLD, 22));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
}
