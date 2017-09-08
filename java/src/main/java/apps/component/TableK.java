package apps.component;

import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class TableK extends JTable {
	private static final long serialVersionUID = 1L;

	public TableK(TableModel tableModel) {
		super(tableModel);
		setFont(new Font("Arial", Font.PLAIN, 22));
		getTableHeader().setFont(new Font("Arial", Font.PLAIN, 22));
		setRowHeight(30);
	}
}
