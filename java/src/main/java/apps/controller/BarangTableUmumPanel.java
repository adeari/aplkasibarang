package apps.controller;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
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

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

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

public class BarangTableUmumPanel extends JPanel {
	final static Logger logger = Logger.getLogger(BarangTableUmumPanel.class);
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private BarangTableUmumPanel barangTablePanel;
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

	public BarangTableUmumPanel(JPanel jPanel, MainForm mainForm1) {
		super();
		barangTablePanel = this;
		int width = Double.valueOf(jPanel.getPreferredSize().getWidth()).intValue() - 20;
		int height = Double.valueOf(jPanel.getPreferredSize().getHeight()).intValue() - 110;

		setPreferredSize(new Dimension(width, height));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
		flowLayout.setHgap(1);
		setLayout(flowLayout);
		titel = new LabelK("Data barang");
		titel.setFont(new Font("Arial", Font.BOLD, 50));
		add(titel);

		JSeparator separator = new JSeparator();
		separator.setSize(new Dimension(width, 5));
		separator.setPreferredSize(separator.getSize());
		add(separator);

		ButtonK pdfButtonK = new ButtonK("P D F");
		pdfButtonK.setIcon(new ImageIcon(getClass().getResource("/apps/icons/pdf.png")));
		pdfButtonK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createPdf();
			}
		});
		add(pdfButtonK);
		ButtonK printButtonK = new ButtonK("PRINT");
		printButtonK.setIcon(new ImageIcon(getClass().getResource("/apps/icons/printer.png")));
		printButtonK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printPdf();
			}
		});
		add(printButtonK);

		predicates = new ArrayList<Predicate>();
		kolom = new String[] { "", "Barang", "Gedung", "Ruang", "Rak", "id", " " };
		filters = new String[] { "", "", "", "", "", "", " " };
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
		tableColumn = table.getColumn("Barang");
		tableColumn.setMinWidth(widthColumns);
		tableColumn = table.getColumn("Gedung");
		tableColumn.setMinWidth(widthColumns);
		tableColumn = table.getColumn("Ruang");
		tableColumn.setMinWidth(widthColumns);
		tableColumn = table.getColumn("Rak");
		tableColumn.setMinWidth(widthColumns);
		tableColumn = table.getColumn(" ");
		tableColumn.setMinWidth(130);
		tableColumn.setCellRenderer(new ButtonRenderActionColumn("barangumum", barangTablePanel));
		tableColumn.setCellEditor(new ButtonActionEditor("barangumum", barangTablePanel));

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
		CriteriaQuery<Barang> criteriaQuery = criteriaBuilder.createQuery(Barang.class);
		Root<Barang> root = criteriaQuery.from(Barang.class);

		Join<Barang, Gedung> gedungJoin = root.join("gedung", JoinType.LEFT);
		Join<Barang, Ruang> ruangJoin = root.join("ruang", JoinType.LEFT);
		Join<Barang, Rak> rakJoin = root.join("rak", JoinType.LEFT);

		if (!filters[1].isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("barang"), "%".concat(filters[1]).concat("%")));
		}
		if (!filters[2].isEmpty()) {
			predicates.add(criteriaBuilder.like(gedungJoin.get("gedung"), "%".concat(filters[2]).concat("%")));
		}
		if (!filters[3].isEmpty()) {
			predicates.add(criteriaBuilder.like(ruangJoin.get("ruang"), "%".concat(filters[3]).concat("%")));
		}
		if (!filters[4].isEmpty()) {
			predicates.add(criteriaBuilder.like(rakJoin.get("rak"), "%".concat(filters[4]).concat("%")));
		}
		predicatesr = predicates.toArray(new Predicate[] {});
		criteriaQuery.select(root);
		criteriaQuery.where(predicatesr);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("barang")));
		List<Barang> barangs = null;
		if (rowOptions.getSelectedItem().equals("ALL")) {
			barangs = (List<Barang>) session.createQuery(criteriaQuery).getResultList();
		} else {
			barangs = (List<Barang>) session.createQuery(criteriaQuery).setFirstResult(firstResult).setMaxResults(limit)
					.getResultList();
		}

		CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<?> entityCountRoot = countCriteriaQuery.from(criteriaQuery.getResultType());
		entityCountRoot.join("gedung", JoinType.LEFT);
		entityCountRoot.join("ruang", JoinType.LEFT);
		entityCountRoot.join("rak", JoinType.LEFT);
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
		String displaying = "Displaying ".concat(String.valueOf(row + 1));

		for (Barang barang : barangs) {
			row++;
			rowData = new Vector<Object>();
			rowData.addElement(row);
			rowData.addElement(barang.getBarang());
			rowData.addElement(barang.getGedung().getGedung());
			rowData.addElement((barang.getRuang() == null) ? "" : barang.getRuang().getRuang());
			rowData.addElement((barang.getRak() == null) ? "" : barang.getRak().getRak());
			rowData.addElement(barang.getId());
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

	private void createPdf() {
		File pdfFile = getPdfFile();
		if (pdfFile != null) {
			try {
				Desktop.getDesktop().open(pdfFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private File getPdfFile() {
		try {
			String folderPdf = apps.component.Properties.pdLocation;
			File folder = new File(folderPdf);
			if (!folder.isDirectory()) {
				folder.mkdirs();
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			File pdfFile = new File(
					folderPdf.concat("barang_").concat(simpleDateFormat.format(new Date())).concat(".pdf"));
			Document document = new Document(PageSize.LETTER);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile.getAbsolutePath()));
			writer.setPageEvent(new PageNumeration());

			document.open();

			com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD);

			Paragraph preface = new Paragraph("DATA BARANG", fontTitle);
			preface.setAlignment(Element.ALIGN_CENTER);
			document.add(preface);

			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			preface = new Paragraph("Tanggal print : ".concat(simpleDateFormat.format(new Date())));
			preface.setAlignment(Element.ALIGN_RIGHT);
			document.add(preface);

			document.add(new Paragraph(" "));

			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 5, 25, 23, 22, 25 });
			PdfPCell cell;
			com.itextpdf.text.Font cellTitle = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 15.0f, Font.BOLD);

			cell = new PdfPCell(new Phrase("NO", cellTitle));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setPaddingBottom(5);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Barang", cellTitle));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setPaddingBottom(5);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Gedung", cellTitle));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setPaddingBottom(5);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Ruang", cellTitle));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setPaddingBottom(5);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Rak", cellTitle));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setPaddingBottom(5);
			table.addCell(cell);

			Session session = HibernateUtil.getSessionFactory().openSession();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Barang> criteriaQuery = criteriaBuilder.createQuery(Barang.class);
			Root<Barang> root = criteriaQuery.from(Barang.class);

			criteriaQuery.select(root);
			criteriaQuery.orderBy(criteriaBuilder.asc(root.get("barang")));
			List<Barang> barangs = (List<Barang>) session.createQuery(criteriaQuery).getResultList();

			int i = 1;
			for (Barang barang : barangs) {
				cell = new PdfPCell(new Phrase(String.valueOf(i)));
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				i++;
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(barang.getBarang()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(barang.getGedung().getGedung()));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase((barang.getRuang() != null) ? barang.getRuang().getRuang() : ""));
				table.addCell(cell);

				cell = new PdfPCell(new Phrase((barang.getRak() != null) ? barang.getRak().getRak() : ""));
				table.addCell(cell);
			}

			document.add(table);

			document.close();
			return pdfFile;
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	private void printPdf() {
		File pdfFile = getPdfFile();
		if (pdfFile != null) {
			InputStream is = null;
			try {
				PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
				if (defaultPrintService == null) {
					JOptionPane.showMessageDialog(null,
							"<html><span style='font-size:22px;'>Komputer tidak terhubung ke printer</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					return;
				}
				DocPrintJob printerJob = defaultPrintService.createPrintJob();

				is = new BufferedInputStream(new FileInputStream(pdfFile));
				Doc simpleDoc = new SimpleDoc(is, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
				printerJob.print(simpleDoc, null);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"<html><span style='font-size:22px;'>Komputer tidak terhubung ke printer</span>", "Perhatian",
						JOptionPane.ERROR_MESSAGE);
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		}
	}

	class PageNumeration extends PdfPageEventHelper {
		PdfTemplate total;

		/**
		 * Creates the PdfTemplate that will hold the total number of pages.
		 * 
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
		 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
		 */
		public void onOpenDocument(PdfWriter writer, Document document) {
			total = writer.getDirectContent().createTemplate(30, 16);
		}

		/**
		 * Adds a header to every page
		 * 
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
		 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
		 */
		public void onEndPage(PdfWriter writer, Document document) {
			PdfPTable table = new PdfPTable(3);
			try {
				table.setWidths(new int[] { 24, 24, 2 });
				table.getDefaultCell().setFixedHeight(10);
				table.getDefaultCell().setBorder(Rectangle.TOP);
				PdfPCell cell = new PdfPCell();
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPhrase(new Phrase(" "));
				table.addCell(cell);

				cell = new PdfPCell();
				cell.setBorder(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPhrase(new Phrase(String.format("Halaman %d dari", writer.getPageNumber())));
				table.addCell(cell);

				cell = new PdfPCell(Image.getInstance(total));
				cell.setBorder(0);
				table.addCell(cell);
				table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
				table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 10,
						writer.getDirectContent());
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			}
		}

		public void onCloseDocument(PdfWriter writer, Document document) {
			ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
					new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
		}
	}
}
