package apps.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import aps.controller.BarangTablePanel;

public class ButtonActionEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;
	private ButtonActionPanel theButtonPanel;
	private String actionName;
	private Object actionObject;

	public ButtonActionEditor(String string, Object object) {
		theButtonPanel = new ButtonActionPanel(string, object);
		actionName = string;
		actionObject = object;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object valueIn, boolean isSelected, int row,
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
			theButtonPanel.setBackground(table.getSelectionBackground());
		} else {
			theButtonPanel.setBackground(table.getBackground());
		}
		theButtonPanel.setRow(row);
		return theButtonPanel;

	}

}
