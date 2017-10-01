package apps.component;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

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
		if (row == 0) {
			return new SearchButton("Search", actionName, actionObject);
		}
		if (isSelected) {
			setBackground(table.getSelectionBackground());
		} else {
			setBackground(table.getBackground());
		}
		return this;
	}
	

}
