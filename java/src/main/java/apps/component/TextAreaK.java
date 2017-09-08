package apps.component;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextArea;

public class TextAreaK extends JTextArea {
	private TextAreaK textAreaK;
	private static final long serialVersionUID = 1L;
	public TextAreaK() {
		textAreaK = this;
		setFont(new Font("Arial", Font.PLAIN, 22));
		addFocusListener(new FocusListener() {            
	        @Override
	        public void focusLost(FocusEvent arg0) {
	        	if (textAreaK.getText().endsWith("\t")) {
	        		textAreaK.setText(textAreaK.getText().replaceAll("\t", ""));
	        	}
	        }
	        @Override
	        public void focusGained(FocusEvent arg0) {
	            // TODO Auto-generated method stub

	        };
	    }); 
	}
}
