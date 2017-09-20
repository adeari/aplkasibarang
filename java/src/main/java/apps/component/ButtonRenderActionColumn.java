package apps.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import aps.controller.BarangTablePanel;
import lancarjaya.component.ButtonActionPanel;
import lancarjaya.component.ButtonK;

public class ButtonRenderActionColumn extends ButtonActionPanel implements TableCellRenderer {
	private String actionName;
	private Object actionObject;
	
	public ButtonRenderActionColumn(String string, Object object) {
		super(string, object);
		actionName = string;
		actionObject = object;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object valueIn, boolean isSelected, boolean hasFocus, int row,
			int column) {
		if ("barang".equals(actionName) && row == 0) {
			ButtonK searchButton = new ButtonK("Search");
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					BarangTablePanel barangTablePanel = (BarangTablePanel) actionObject;
					barangTablePanel.populteFilter();
					barangTablePanel.showData();
				}
			});
			return searchButton;
		}
		if (isSelected) {
			setBackground(table.getSelectionBackground());
		} else {
			setBackground(table.getBackground());
		}
		return this;
	}
	

}
