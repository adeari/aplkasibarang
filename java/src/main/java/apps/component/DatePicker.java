package apps.component;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import lancarjaya.component.DateLabelFormatter;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

public class DatePicker extends JDatePickerImpl {
	private static final long serialVersionUID = 1L;
	private AbstractFormatterFactory abstractFormatterFactory;
	private DefaultFormatterFactory defaultFormatterFactory;
	private JFormattedTextField jFormattedTextField;
	private JButton button;
	private DateLabelFormatter _dateLabelFormatter;

	public DatePicker(JDatePanelImpl datePanel, DateLabelFormatter dateLabelFormatter) {
		super(datePanel, dateLabelFormatter);
		setTextEditable(true);
		_dateLabelFormatter = dateLabelFormatter;
		jFormattedTextField = getJFormattedTextField();
		jFormattedTextField.setFont(new Font("Arial", Font.BOLD, 22));
		jFormattedTextField.setEditable(true);
		jFormattedTextField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (defaultFormatterFactory != null) {
					jFormattedTextField.setValue(null);
					jFormattedTextField.setFormatterFactory(defaultFormatterFactory);
				}
			}

			public void focusLost(FocusEvent e) {

			}
		});
		abstractFormatterFactory = jFormattedTextField.getFormatterFactory();
		try {
			MaskFormatter maskFormatter = new MaskFormatter("##/##/####");
			maskFormatter.setValidCharacters("1234567890/ ");
			maskFormatter.setPlaceholderCharacter(' ');
			defaultFormatterFactory = new DefaultFormatterFactory(maskFormatter);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		for (Component component : getComponents()) {
			if (component instanceof JButton) {
				button = (JButton) component;
				ActionListener[] actionListenersClone = button.getActionListeners().clone();
				while (button.getActionListeners().length > 0) {
					button.removeActionListener(button.getActionListeners()[0]);
				}
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							if (jFormattedTextField.getValue() != null) {
								Calendar cal = Calendar.getInstance();
								cal.setTime((Date) _dateLabelFormatter.getDateFormatter()
										.parseObject(jFormattedTextField.getText()));
							}
						} catch (ClassCastException classCastException) {
							jFormattedTextField.setValue(null);
						} catch (ParseException e1) {
							jFormattedTextField.setValue(null);
						}
						jFormattedTextField.setFormatterFactory(abstractFormatterFactory);
					}
				});
				for (ActionListener actionListener : actionListenersClone) {
					button.addActionListener(actionListener);
				}
				button.setText("");
				button.setIcon(new ImageIcon(getClass().getResource("/lancarjaya/icons/date.png")));
				button.setCursor(new Cursor(Cursor.HAND_CURSOR));
				break;
			}
		}
	}

	public void setSize(Dimension dimension) {
		setPreferredSize(new Dimension(220, Double.valueOf(dimension.getHeight()).intValue()));
		jFormattedTextField.setPreferredSize(new Dimension(150, Double.valueOf(dimension.getHeight()).intValue()));
		button.setPreferredSize(new Dimension(50, Double.valueOf(dimension.getHeight()).intValue()));
	}

	public void setValue(Object object) {
		jFormattedTextField.setValue(null);
		if (object != null) {
			if (object instanceof Calendar) {
				jFormattedTextField.setValue((Calendar) object);
			} else if (object instanceof String) {
				String string = (String) object;
				if (string != null && !string.isEmpty()) {
					jFormattedTextField.setFormatterFactory(abstractFormatterFactory);
					try {
						Calendar cal = Calendar.getInstance();
						cal.setTime((Date) _dateLabelFormatter.getDateFormatter().parseObject(string));
						jFormattedTextField.setValue(cal);
					} catch (ParseException e) {
						System.out.println("nothing");
					}
				}
			} else if (object instanceof java.sql.Date) {
				Calendar cal = Calendar.getInstance();
				cal.setTime((java.sql.Date) object);
				jFormattedTextField.setValue(cal);
				jFormattedTextField.setFormatterFactory(abstractFormatterFactory);
			} 
		}
	}

	public String getValue() {
		if (jFormattedTextField.getValue() != null) {
			if (jFormattedTextField.getValue() instanceof Calendar) {
				return _dateLabelFormatter.getDateFormatter()
						.format(((Calendar) jFormattedTextField.getValue()).getTime());
			} else if (jFormattedTextField.getValue() instanceof String
					&& (!jFormattedTextField.getValue().toString().isEmpty())) {
				return jFormattedTextField.getValue().toString();
			}
		}
		return "";
	}
	
	public java.sql.Date getSqlDate() {
		if (jFormattedTextField.getValue() != null) {
			if (jFormattedTextField.getValue() instanceof Calendar) {
				Calendar calendar = (Calendar) jFormattedTextField.getValue();
				return new java.sql.Date(calendar.getTimeInMillis());
			} else if (jFormattedTextField.getValue() instanceof String
					&& (!jFormattedTextField.getValue().toString().isEmpty())) {
				try {
					Date parsed = (Date) _dateLabelFormatter.getDateFormatter().parseObject((String) jFormattedTextField.getValue());
					return new java.sql.Date(parsed.getTime());
				} catch (ParseException e) {
					System.out.println("e");
				}
			}
		}
		return null;
	}
}
