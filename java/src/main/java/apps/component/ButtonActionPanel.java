package apps.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import aps.controller.BarangTablePanel;
import aps.controller.GedungTablePanel;
import aps.controller.RakTablePanel;
import aps.controller.RuangTablePanel;


public class ButtonActionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String handle;
	private Object tableAccess;
	private Integer row;

	public ButtonActionPanel(String handleWhat, Object object) {
		super(new GridLayout(0, 1));
		handle = handleWhat;
		tableAccess = object;
		row = null;
		ButtonK editButton = new ButtonK("");
		editButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/edit.png")));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("gedung".equals(handle)) {
					((GedungTablePanel) tableAccess).setEdit(row);
				} else if ("ruang".equals(handle)) {
					((RuangTablePanel) tableAccess).setEdit(row);
				} else if ("rak".equals(handle)) {
					((RakTablePanel) tableAccess).setEdit(row);
				} else if ("barang".equals(handle)) {
					((BarangTablePanel) tableAccess).setEdit(row);
				}
			}
		});
		ButtonK deleteButton = new ButtonK("");
		deleteButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/delete.png")));
		deleteButton.setBackground(Color.RED);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("gedung".equals(handle)) {
					((GedungTablePanel) tableAccess).removeDataById(row);
				}  else if ("ruang".equals(handle)) {
					((RuangTablePanel) tableAccess).removeDataById(row);
				}  else if ("rak".equals(handle)) {
					((RakTablePanel) tableAccess).removeDataById(row);
				}  else if ("barang".equals(handle)) {
					((BarangTablePanel) tableAccess).removeDataById(row);
				}
			}
		});
		setOpaque(true);
		setBackground(editButton.getBackground());
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		add(editButton, c);
		add(deleteButton, c);
	}

	public void setRow(int rowSelected) {
		row = rowSelected;
	}
}