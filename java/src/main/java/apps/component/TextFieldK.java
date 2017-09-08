package apps.component;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class TextFieldK extends JTextField {
	private static final long serialVersionUID = 1L;
	private TextFieldK textFieldK;
	public TextFieldK() {
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
	}
}
