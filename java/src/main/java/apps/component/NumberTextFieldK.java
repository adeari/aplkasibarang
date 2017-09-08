package apps.component;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class NumberTextFieldK extends JTextField {
	private static final long serialVersionUID = 1L;
	private NumberTextFieldK textFieldK;

	public NumberTextFieldK() {
		setFont(new Font("Arial", Font.PLAIN, 22));
		textFieldK = this;
		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				textFieldK.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				textFieldK.select(0, 0);
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent evt) {
				char c = evt.getKeyChar();
				if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					evt.consume();
				}
			}
		});
	}
}
