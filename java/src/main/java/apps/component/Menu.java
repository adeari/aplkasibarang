package apps.component;

import java.awt.Font;

import javax.swing.JMenu;

public class Menu extends JMenu {
	private static final long serialVersionUID = 1L;

	public Menu(String label) {
		super(label);
		setFont(new Font("Arial", Font.BOLD, 22));
	}
}
