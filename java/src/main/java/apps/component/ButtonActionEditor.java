package apps.component;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

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
		if (row == 0) {
			return new SearchButton("Search", actionName, actionObject);
		}
		if (isSelected) {
			theButtonPanel.setBackground(table.getSelectionBackground());
		} else {
			theButtonPanel.setBackground(table.getBackground());
		}
		theButtonPanel.setRow((int) row);
		return theButtonPanel;

	}

}
