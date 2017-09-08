package apps.component;

import java.awt.Font;

import javax.swing.JLabel;

public class LabelK extends JLabel {
	private static final long serialVersionUID = 1L;

	public LabelK(String label) {
		super(label);
		setFont(new Font("Arial", Font.BOLD, 22));
	}
}
