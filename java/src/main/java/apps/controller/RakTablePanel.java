package apps.controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.hibernate.Session;
import org.hibernate.Transaction;

import apps.component.ButtonActionEditor;
import apps.component.ButtonK;
import apps.component.ButtonRenderActionColumn;
import apps.component.Combobox;
import apps.component.LabelK;
import apps.component.RowNumberRenderer;
import apps.component.TableK;
import apps.component.TextFieldK;
import apps.tables.Barang;
import apps.tables.Gedung;
import apps.tables.Rak;
import apps.tables.Ruang;

public class RakTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private RakTablePanel barangTablePanel;
	private MainForm mainForm;
	private AbstractTableModel tableModel;
	private Vector<Object> dataini;
	private TableK table;
	private List<Predicate> predicates;
	private Predicate[] predicatesr;
	private TextFieldK pageText;
	private ButtonK back;
	private ButtonK backback;
	private ButtonK next;
	private ButtonK nextnext;
	private Combobox rowOptions;
	private LabelK displayPage;
	private LabelK ofPage;
	private String filters[];
	private String kolom[];
	private int pageSize;

	public RakTablePanel(JPanel jPanel, MainForm mainForm1) {
		super();
		mainForm = mainForm1;
		barangTablePanel = this;
		int width = Double.valueOf(jPanel.getPreferredSize().getWidth()).intValue() - 20;
		int height = Double.valueOf(jPanel.getPreferredSize().getHeight()).intValue() - 110;
		
		setPreferredSize(new Dimension(width, height));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
		flowLayout.setHgap(1);
		setLayout(flowLayout);
		titel = new LabelK("Data rak");
		titel.setFont(new Font("Arial", Font.BOLD, 50));
		add(titel);

		JSeparator separator = new JSeparator();
		separator.setSize(new Dimension(width, 5));
		separator.setPreferredSize(separator.getSize());
		add(separator);
		
		ButtonK addButtonK = new ButtonK("Tambah Rak");
		addButtonK.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tambah.png")));
		addButtonK.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			mainForm.tambahRak();
    		}
    	});
		add(addButtonK);
		
		predicates = new ArrayList<Predicate>();
		kolom = new String[]{"", "Rak", "Gedung", "Ruang", "id"," "};
		filters = new String[] {"", "", "", "", "", ""};
		dataini = new Vector<Object>();
		tableModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return kolom.length;
			}

			public int getRowCount() {
				return dataini.size();
			}

			public Object getValueAt(int baris1, int kolom1) {
				Vector<?> barisan = (Vector<?>) dataini.elementAt(baris1);
				return barisan.elementAt(kolom1);
			}

			public String getColumnName(int kolom1) {
				return kolom[kolom1];
			}

			public boolean isCellEditable(int baris1, int kolom1) {
				if (kolom1 == kolom.length - 1) {
					return true;
				} else if (baris1 == 0 && kolom1 != 0 && kolom1 != kolom.length - 1) {
					return true;
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			public void setValueAt(Object obj, int baris1, int kolom1) {
				if (baris1 < getRowCount()) {
					Vector<Object> barisdata = (Vector<Object>) dataini.elementAt(baris1);
					barisdata.setElementAt(obj, kolom1);
				}
			}

			public Class<? extends Object> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}
		};
		table = new TableK(tableModel);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setPreferredSize(new Dimension(width, height - 180));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setDefaultEditor(Object.class, new TableEditor());
		add(tableScroll);
		
		TableColumn tableColumn = new TableColumn();
		tableColumn = table.getColumn("");
		tableColumn.setMinWidth(50);
		tableColumn.setMaxWidth(50);
		tableColumn.setCellRenderer(new RowNumberRenderer());
		int widthColumns = width / kolom.length + 57;
		tableColumn = table.getColumn("Rak");
		tableColumn.setMinWidth(widthColumns);
		tableColumn = table.getColumn("Gedung");
		tableColumn.setMinWidth(widthColumns);
		tableColumn = table.getColumn("Ruang");
		tableColumn.setMinWidth(widthColumns);
		tableColumn = table.getColumn(" ");
		tableColumn.setMinWidth(130);
		tableColumn.setCellRenderer(new ButtonRenderActionColumn("rak", barangTablePanel));
		tableColumn.setCellEditor(new ButtonActionEditor("rak", barangTablePanel));
		
		tableColumn = table.getColumn("id");
		tableColumn.setMinWidth(0);
		tableColumn.setPreferredWidth(0);
		tableColumn.setWidth(0);
		tableColumn.setMaxWidth(0);
		
		JPanel rowPanel = new JPanel(flowLayout);
		add(rowPanel);
		rowOptions = new Combobox();
		rowOptions.addItem("50");
		rowOptions.addItem("100");
		rowOptions.addItem("200");
		rowOptions.addItem("500");
		rowOptions.addItem("ALL");
		rowOptions.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				showData();
			}
		});
		rowPanel.add(rowOptions);
		
		backback = new ButtonK("");
		backback.setIcon(new ImageIcon(getClass().getResource("/apps/icons/backback.png")));
		backback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pageText.setText("1");
				showData();
			}
		});
		rowPanel.add(backback);
		
		back = new ButtonK("");
		back.setIcon(new ImageIcon(getClass().getResource("/apps/icons/back.png")));
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pageText.setText(String.valueOf(Integer.valueOf(pageText.getText()).intValue() - 1));
				showData();
			}
		});
		rowPanel.add(back);
		rowPanel.add(new LabelK("Page"));
		pageText = new TextFieldK();
		pageText.setText("1");
		pageText.setPreferredSize(new Dimension(70, 40));
		pageText.setHorizontalAlignment(JTextField.RIGHT);
		pageText.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					e.consume();
				}
				if (c == KeyEvent.VK_ENTER) {
					showData();
				}
			}
		});
		rowPanel.add(pageText);
		ofPage = new LabelK("");
		rowPanel.add(ofPage);
		next = new ButtonK("");
		next.setIcon(new ImageIcon(getClass().getResource("/apps/icons/next.png")));
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pageText.setText(String.valueOf(Integer.valueOf(pageText.getText()).intValue() + 1));
				showData();
			}
		});
		rowPanel.add(next);
		nextnext = new ButtonK("");
		nextnext.setIcon(new ImageIcon(getClass().getResource("/apps/icons/nextnext.png")));
		nextnext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pageText.setText(String.valueOf(pageSize));
				showData();
			}
		});
		rowPanel.add(nextnext);
		displayPage = new LabelK("Display");
		displayPage.setPreferredSize(new Dimension(width - 550, 40));
		displayPage.setHorizontalAlignment(LabelK.RIGHT);
		rowPanel.add(displayPage);
	}
	public void view() {
		barangTablePanel.setVisible(true);
		showData();
	}
	private class TableEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;
		private TextFieldK textFieldKUsed;

		public TableEditor() {
			super(new TextFieldK());
			textFieldKUsed = (TextFieldK) getComponent();
			textFieldKUsed.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						filters[table.getSelectedColumn()] = textFieldKUsed.getText();
						showData();
					}
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			if (row == 0) {
				TextFieldK textField = (TextFieldK) super.getTableCellEditorComponent(table, value, isSelected, row,
						WIDTH);
				textField.setText((String) value);
				return textField;
			} else {
				if (value == null) {
					return new JLabel("");
				}
				return new JLabel(value.toString());
			}
		}
	}
	
	public void showData() {
		dataini.clear();
		if (1 > Integer.valueOf(pageText.getText())) {
			pageText.setText("1");
			showData();
			return;
		}
		int limit = 0;
		int firstResult = 0;
		if (!rowOptions.getSelectedItem().equals("ALL")) {
			limit = Integer.valueOf(rowOptions.getSelectedItem().toString()).intValue();
			firstResult = (Integer.valueOf(pageText.getText()).intValue() - 1) * limit;
		}

		Vector<Object> rowData = new Vector<Object>();
		for (String string : filters) {
			rowData.addElement(string);
		}
		dataini.addElement(rowData);
		predicates.clear();
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Rak> criteriaQuery = criteriaBuilder.createQuery(Rak.class);
		Root<Rak> root = criteriaQuery.from(Rak.class);
		
		Join<Rak, Ruang> ruangJoin = root.join("ruang", JoinType.LEFT);
		Join<Ruang, Gedung> gedungJoin = ruangJoin.join("gedung", JoinType.LEFT);
		
		if (!filters[1].isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("rak"), "%".concat(filters[1]).concat("%")));
		}
		if (!filters[2].isEmpty()) {
			predicates.add(criteriaBuilder.like(gedungJoin.get("gedung"), "%".concat(filters[2]).concat("%")));
		}
		if (!filters[3].isEmpty()) {
			predicates.add(criteriaBuilder.like(ruangJoin.get("ruang"), "%".concat(filters[3]).concat("%")));
		}
		
		predicatesr = predicates.toArray(new Predicate[] {});
		criteriaQuery.select(root);
		criteriaQuery.where(predicatesr);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("rak")));
		List<Rak> raks = null;
		if (rowOptions.getSelectedItem().equals("ALL")) {
			raks = (List<Rak>) session.createQuery(criteriaQuery).getResultList();
		} else {
			raks = (List<Rak>) session.createQuery(criteriaQuery).setFirstResult(firstResult)
					.setMaxResults(limit).getResultList();
		}

		CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<?> entityCountRoot = countCriteriaQuery.from(criteriaQuery.getResultType());
		entityCountRoot.join("ruang", JoinType.LEFT).join("gedung", JoinType.LEFT);
		countCriteriaQuery.select(criteriaBuilder.count(entityCountRoot));
		countCriteriaQuery.where(predicatesr);
		Long dataSize = (Long) session.createQuery(countCriteriaQuery).getSingleResult();
		pageSize = 1;
		if (rowOptions.getSelectedItem().equals("ALL")) {
			back.setEnabled(false);
			backback.setEnabled(false);
			next.setEnabled(false);
			nextnext.setEnabled(false);
			ofPage.setText("of 1");
		} else {
			if (dataSize != null && dataSize > 0) {
				pageSize = (int) (dataSize / limit);
				if (dataSize % limit > 0) {
					pageSize++;
				}
			}
			ofPage.setText("of ".concat(String.valueOf(pageSize)));
			if (1 == Integer.valueOf(pageText.getText())) {
				back.setEnabled(false);
				backback.setEnabled(false);
			} else {
				back.setEnabled(true);
				backback.setEnabled(true);
			}

			if (pageSize < Integer.valueOf(pageText.getText())) {
				pageText.setText(String.valueOf(pageSize));
				showData();
				return;
			} else if (pageSize == Integer.valueOf(pageText.getText())) {
				next.setEnabled(false);
				nextnext.setEnabled(false);
				pageText.setText(String.valueOf(pageSize));
			} else {
				next.setEnabled(true);
				nextnext.setEnabled(true);
			}
		}

		int row = firstResult;
		String displaying  = "Displaying ".concat(String.valueOf(row + 1));
		
		for (Rak rak : raks) {
			row++;
			rowData = new Vector<Object>();
			rowData.addElement(row);
			rowData.addElement(rak.getRak());
			String gedung = "";
			String ruang = "";
			if (rak.getRuang() != null) {
				ruang = rak.getRuang().getRuang();
				if (rak.getRuang().getGedung() != null) {
					gedung = rak.getRuang().getGedung().getGedung();
				}
			}
			rowData.addElement(gedung);
			rowData.addElement(ruang);
			rowData.addElement(rak.getId());
			rowData.addElement(dataini.size());
			dataini.addElement(rowData);
		}
		displaying = displaying.concat(
				" to ".concat(String.valueOf(row)).concat(" of ").concat(String.valueOf(dataSize)).concat(" items"));
		displayPage.setText(displaying);
		session.close();
		predicatesr = null;
		tableModel.fireTableDataChanged();
	}
	public void populateFilter() {
		for (int i = 0; i < filters.length; i++) {
			if (table.getValueAt(0, i) != null) {
				filters[i] = table.getValueAt(0, i).toString();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setEdit(int row) {
		mainForm.closesss();
		Vector<Object> rowData = (Vector<Object>) dataini.get(row);
		mainForm.getRakFormPanel().setEdit((int) rowData.get(4));
	}
	@SuppressWarnings("unchecked")
	public void removeDataById(int row) {
		Vector<Object> rowData = (Vector<Object>) dataini.get(row);
		Rak rak = getRakById((int) rowData.get(4));
		if (isBarangExistByGedung(rak)) {
			JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Rak tidak dihapus karena masih ada barangnya</span>", "Perhatian", JOptionPane.ERROR_MESSAGE);
		} else if (JOptionPane.showConfirmDialog(null,
				"<html><span style='font-size:22px;'>Apakah Rak <span style='color:red;'>".concat(rowData.get(1).toString()).concat("</span> akan di hapus?</span>"), "Perhatian",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			session.delete(rak);
			transaction.commit();
			session.close();
			showData();
		}
	}
	
	private Rak getRakById(int id) {
		Rak rak = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Rak> criteriaQuery = criteriaBuilder.createQuery(Rak.class);
		Root<Rak> root = criteriaQuery.from(Rak.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
		List<Rak> raks = (List<Rak>) session.createQuery(criteriaQuery).getResultList();
		for (Rak rakLoop : raks) {
			rak = rakLoop;
		}
		session.close();
		return rak;
	}
	
	private boolean isBarangExistByGedung(Rak rak) {
		Session sessionCheck = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilderCheck = sessionCheck.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuerycheck = criteriaBuilderCheck.createQuery(Long.class);
		criteriaQuerycheck.select(criteriaBuilderCheck.count(criteriaQuerycheck.from(Barang.class)));
		
		Root<Barang> root = criteriaQuerycheck.from(Barang.class);
		criteriaQuerycheck.where(criteriaBuilderCheck.equal(root.get("rak"), rak));
		Long dataSize = (Long) sessionCheck.createQuery(criteriaQuerycheck).getSingleResult();
		sessionCheck.close();
		if (dataSize > 0) {
			return true;
		}
		return false;
	}
}
