package apps.component;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class RowNumberRenderer implements TableCellRenderer {
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        JLabel label = new JLabel(value.toString());
        label.setFont(new Font("Arial", Font.PLAIN, 22));
        if (isSelected) {
        	label.setBackground(table.getSelectionBackground());
		} else {
			label.setBackground(table.getTableHeader().getBackground());
		}
        
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setOpaque(true);
        return label;
    }
}
