package apps.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import apps.controller.BarangTablePanel;
import apps.controller.GedungTablePanel;
import apps.controller.RakTablePanel;
import apps.controller.RuangTablePanel;
import apps.controller.laporan.HargaSatuan;


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
				} else if ("hargabarang".equals(handle)) {
					((HargaSatuan) tableAccess).editHargaBarang(row);
				} else if ("hargasatuan".equals(handle)) {
					((HargaSatuan) tableAccess).editHargaSatuan(row);
				} else if ("hargasatuan".equals(handle)) {
					((HargaSatuan) tableAccess).editHargaSatuan(row);
				} else if ("hargajasa".equals(handle)) {
					((HargaSatuan) tableAccess).editHargaJasa(row);
				} else if ("hargalain".equals(handle)) {
					((HargaSatuan) tableAccess).editHargaLain(row);
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
				} else if ("hargabarang".equals(handle)) {
					((HargaSatuan) tableAccess).deleteHargaBarang(row);
				} else if ("hargasatuan".equals(handle)) {
					((HargaSatuan) tableAccess).deleteHargaSatuan(row);
				} else if ("hargajasa".equals(handle)) {
					((HargaSatuan) tableAccess).deleteHargaJasa(row);
				} else if ("hargalain".equals(handle)) {
					((HargaSatuan) tableAccess).deleteHargaLain(row);
				}
			}
		});
		setOpaque(true);
		setBackground(editButton.getBackground());
		setLayout(new GridBagLayout());
		
		if (!"barangumum".equals(handle)) {
			GridBagConstraints c = new GridBagConstraints();
			add(editButton, c);
			add(deleteButton, c);
		}
	}

	public void setRow(int rowSelected) {
		row = rowSelected;
	}
}