package apps.component;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class PasswordK extends JPasswordField  {
	private static final long serialVersionUID = 1L;
	private PasswordK passwordK;

	public PasswordK() {
		setFont(new Font("Arial", Font.PLAIN, 22));
		setEchoChar('*');
		passwordK = this;
		addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            	passwordK.selectAll();
            }
            @Override
            public void focusLost(FocusEvent e) {
            	passwordK.select(0, 0);
            }
        });
	}
}
