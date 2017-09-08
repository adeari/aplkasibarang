package apps.component;

import java.awt.Font;

import javax.swing.JMenuItem;

public class MenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;

	public MenuItem(String label) {
		super(label);
		setFont(new Font("Arial", Font.BOLD, 22));
	}
}
