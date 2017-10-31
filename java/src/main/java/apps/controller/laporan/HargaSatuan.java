package apps.controller.laporan;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import apps.component.ButtonActionEditor;
import apps.component.ButtonK;
import apps.component.ButtonRenderActionColumn;
import apps.component.LabelK;
import apps.component.TableK;
import apps.component.TextFieldK;
import apps.controller.MainForm;

public class HargaSatuan extends JPanel {
	final static Logger logger = Logger.getLogger(HargaSatuan.class);
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private HargaSatuan hargaSatuan;
	private HargaBarangFormPanel hargaBarangFormPanel;
	private HargaSatuanFormPanel hargaSatuanFormPanel;
	private HargaJasaFormPanel hargaJasaFormPanel;
	private HargaLainFormPanel hargaLainFormPanel;
	
	private AbstractTableModel hargaBarangTableModel, hargaSatuanTableModel, hargaJasaTableModel, hargaLainTableModel;
	private Vector<Object> hargaBarangDataini, hargaSatuanDataini, hargaJasaDataini, hargaLainDataini;
	private TableK hargaBarangTable, hargaSatuanTable, hargaJasaTable, hargaLainTable;
	private String hargaBarangKolom[], hargaSatuanKolom[], hargaJasaKolom[], hargaLainKolom[];
	
	private TextFieldK kementrianTextFieldK, satuanKerjaTextFieldK, unitKerjaTextFieldK, tahunAngaranTextFieldK, revisiBarangTextFieldK, 
	revisiSatuanTextFieldK, revisiJasaTextFieldK, revisiLainTextFieldK;

	public HargaSatuan(JPanel jPanel, MainForm mainForm1) {
		super();
		hargaSatuan = this;
		int width = Double.valueOf(jPanel.getPreferredSize().getWidth()).intValue() - 20;
		int height = Double.valueOf(jPanel.getPreferredSize().getHeight()).intValue() - 110;

		setPreferredSize(new Dimension(width, height));
		
		FlowLayout flowLayout0 = new FlowLayout(FlowLayout.LEADING);
		flowLayout0.setHgap(0);
		flowLayout0.setVgap(0);
		setLayout(flowLayout0);
		
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
		flowLayout.setHgap(4);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(width - 19, 1630));
		panel.setSize(panel.getPreferredSize());
		panel.setLayout(flowLayout);
		
		JScrollPane scroll = new JScrollPane(panel);
		scroll.setPreferredSize(getPreferredSize());
		scroll.setSize(scroll.getPreferredSize());
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		add(scroll);
		
		titel = new LabelK("DAFTAR HARGA SATUAN BARANG, PEKERJAAN KONSTRUKSI, JASA KONSULTANSI, JASA LAINNYA");
		titel.setFont(new Font("Arial", Font.BOLD, 20));
		panel.add(titel);

		JSeparator separator = new JSeparator();
		separator.setSize(new Dimension(width, 5));
		separator.setPreferredSize(separator.getSize());
		panel.add(separator);
		
		int widthLabel = width * 20 / 100;
		int widthText = width * 70 / 100;
		
		LabelK kementrianLabel = new LabelK("KEMENTERIAN");
		kementrianLabel.setPreferredSize(new Dimension(widthLabel, 32));
		panel.add(kementrianLabel);
		
		kementrianTextFieldK = new TextFieldK();
		kementrianTextFieldK.setPreferredSize(new Dimension(widthText, 32));
		panel.add(kementrianTextFieldK);
		
		LabelK satuanKerjaLabel = new LabelK("SATUAN KERJA");
		satuanKerjaLabel.setPreferredSize(kementrianLabel.getPreferredSize());
		panel.add(satuanKerjaLabel);
		
		satuanKerjaTextFieldK = new TextFieldK();
		satuanKerjaTextFieldK.setPreferredSize(kementrianTextFieldK.getPreferredSize());
		panel.add(satuanKerjaTextFieldK);
		
		LabelK unitKerjaLabel = new LabelK("UNIT KERJA");
		unitKerjaLabel.setPreferredSize(kementrianLabel.getPreferredSize());
		panel.add(unitKerjaLabel);
		
		unitKerjaTextFieldK = new TextFieldK();
		unitKerjaTextFieldK.setPreferredSize(kementrianTextFieldK.getPreferredSize());
		panel.add(unitKerjaTextFieldK);
		
		LabelK tahunAnggaranLabel = new LabelK("TAHUN ANGGARAN");
		tahunAnggaranLabel.setPreferredSize(kementrianLabel.getPreferredSize());
		panel.add(tahunAnggaranLabel);
		
		tahunAngaranTextFieldK = new TextFieldK();
		tahunAngaranTextFieldK.setPreferredSize(kementrianTextFieldK.getPreferredSize());
		panel.add(tahunAngaranTextFieldK);
		
		int tableLabel = width * 70 / 100;
		LabelK standardSatuanBarangLabel = new LabelK("A  STANDAR HARGA SATUAN BARANG");
		standardSatuanBarangLabel.setPreferredSize(new Dimension(tableLabel, 32));
		panel.add(standardSatuanBarangLabel);
		
		int tableLabel2 = width * 10 / 100;
		LabelK revisiKeLbel = new LabelK("Revisi ke ");
		revisiKeLbel.setPreferredSize(new Dimension(tableLabel2, 32));
		panel.add(revisiKeLbel);
		
		int tableRevisi = width * 10 / 100;
		revisiBarangTextFieldK = new TextFieldK();
		revisiBarangTextFieldK.setPreferredSize(new Dimension(tableRevisi, 32));
		panel.add(revisiBarangTextFieldK);
		
		hargaBarangKolom = new String[] { "No.", "Jenis Barang", "Merk/ Tipe/ Spesifikasi", "Satuan", "Harga Satuan (Rp)", "Contoh", "Sumber informasi", "Keterangan", " " };
		hargaBarangDataini = new Vector<Object>();
		hargaBarangTableModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return hargaBarangKolom.length;
			}

			public int getRowCount() {
				return hargaBarangDataini.size();
			}

			public Object getValueAt(int baris1, int kolom1) {
				Vector<?> barisan = (Vector<?>) hargaBarangDataini.elementAt(baris1);
				return barisan.elementAt(kolom1);
			}

			public String getColumnName(int kolom1) {
				return hargaBarangKolom[kolom1];
			}

			public boolean isCellEditable(int baris1, int kolom1) {
				if (kolom1 == hargaBarangKolom.length - 1) {
					return true;
				} else if (baris1 == 0 && kolom1 != 0 && kolom1 != hargaBarangKolom.length - 1) {
					return true;
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			public void setValueAt(Object obj, int baris1, int kolom1) {
				if (baris1 < getRowCount()) {
					Vector<Object> barisdata = (Vector<Object>) hargaBarangDataini.elementAt(baris1);
					barisdata.setElementAt(obj, kolom1);
				}
			}

			public Class<? extends Object> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}
		};
		hargaBarangTable = new TableK(hargaBarangTableModel);
		ButtonK addHargaBarang = new ButtonK("Tambah");
		addHargaBarang.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tambah.png")));
		addHargaBarang.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			closeAll();
    			hargaBarangFormPanel.setTambah();
    		}
    	});
		panel.add(addHargaBarang);
		
		JScrollPane tableScroll = new JScrollPane(hargaBarangTable);
		tableScroll.setPreferredSize(new Dimension(width * 96 / 100, 250));
		hargaBarangTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		panel.add(tableScroll);
		
		TableColumn tableColumn = new TableColumn();
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tableColumn = hargaBarangTable.getColumn("No.");
		tableColumn.setMinWidth(50);
		tableColumn.setCellRenderer(rightRenderer);
		tableColumn = hargaBarangTable.getColumn("Jenis Barang");
		tableColumn.setMinWidth(200);
		tableColumn = hargaBarangTable.getColumn("Merk/ Tipe/ Spesifikasi");
		tableColumn.setMinWidth(300);
		tableColumn = hargaBarangTable.getColumn("Satuan");
		tableColumn.setMinWidth(200);
		tableColumn = hargaBarangTable.getColumn("Harga Satuan (Rp)");
		tableColumn.setMinWidth(300);
		tableColumn = hargaBarangTable.getColumn("Contoh");
		tableColumn.setMinWidth(170);
		tableColumn = hargaBarangTable.getColumn("Sumber informasi");
		tableColumn.setMinWidth(300);
		tableColumn = hargaBarangTable.getColumn("Keterangan");
		tableColumn.setMinWidth(300);
		tableColumn = hargaBarangTable.getColumn(" ");
		tableColumn.setCellRenderer(new ButtonRenderActionColumn("hargabarang", hargaSatuan));
		tableColumn.setCellEditor(new ButtonActionEditor("hargabarang", hargaSatuan));
		tableColumn.setMinWidth(130);
		
		LabelK standardHargaBLabel = new LabelK("B  STANDAR HARGA SATUAN PEKERJAAN KONSTRUKSI");
		standardHargaBLabel.setPreferredSize(standardSatuanBarangLabel.getPreferredSize());
		panel.add(standardHargaBLabel);
		
		LabelK revisiKeLbel1 = new LabelK("Revisi ke ");
		revisiKeLbel1.setPreferredSize(revisiKeLbel.getPreferredSize());
		panel.add(revisiKeLbel1);
		
		revisiSatuanTextFieldK = new TextFieldK();
		revisiSatuanTextFieldK.setPreferredSize(revisiBarangTextFieldK.getPreferredSize());
		panel.add(revisiSatuanTextFieldK);

		hargaSatuanKolom = new String[] { "No.", "Jenis Pekerjaan", "Merk/ Tipe/ Spesifikasi", "Satuan", "Harga Satuan (Rp)", "Contoh", "Sumber informasi", "Keterangan", " " };
		hargaSatuanDataini = new Vector<Object>();
		hargaSatuanTableModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return hargaSatuanKolom.length;
			}

			public int getRowCount() {
				return hargaSatuanDataini.size();
			}

			public Object getValueAt(int baris1, int kolom1) {
				Vector<?> barisan = (Vector<?>) hargaSatuanDataini.elementAt(baris1);
				return barisan.elementAt(kolom1);
			}

			public String getColumnName(int kolom1) {
				return hargaSatuanKolom[kolom1];
			}

			public boolean isCellEditable(int baris1, int kolom1) {
				if (kolom1 == hargaSatuanKolom.length - 1) {
					return true;
				} else if (baris1 == 0 && kolom1 != 0 && kolom1 != hargaSatuanKolom.length - 1) {
					return true;
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			public void setValueAt(Object obj, int baris1, int kolom1) {
				if (baris1 < getRowCount()) {
					Vector<Object> barisdata = (Vector<Object>) hargaSatuanDataini.elementAt(baris1);
					barisdata.setElementAt(obj, kolom1);
				}
			}

			public Class<? extends Object> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}
		};
		hargaSatuanTable = new TableK(hargaSatuanTableModel);
		ButtonK addHargaSatuan = new ButtonK("Tambah");
		addHargaSatuan.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tambah.png")));
		addHargaSatuan.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			closeAll();
    			hargaSatuanFormPanel.setTambah();
    		}
    	});
		panel.add(addHargaSatuan);
		
		JScrollPane tableScroll1 = new JScrollPane(hargaSatuanTable);
		tableScroll1.setPreferredSize(tableScroll.getPreferredSize());
		hargaSatuanTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		panel.add(tableScroll1);
		
		TableColumn tableColumnSatuan = new TableColumn();
		DefaultTableCellRenderer rightSatuanRenderer = new DefaultTableCellRenderer();
		rightSatuanRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tableColumnSatuan = hargaSatuanTable.getColumn("No.");
		tableColumnSatuan.setMinWidth(50);
		tableColumnSatuan.setCellRenderer(rightSatuanRenderer);
		tableColumnSatuan = hargaSatuanTable.getColumn("Jenis Pekerjaan");
		tableColumnSatuan.setMinWidth(200);
		tableColumnSatuan = hargaSatuanTable.getColumn("Merk/ Tipe/ Spesifikasi");
		tableColumnSatuan.setMinWidth(300);
		tableColumnSatuan = hargaSatuanTable.getColumn("Satuan");
		tableColumnSatuan.setMinWidth(200);
		tableColumnSatuan = hargaSatuanTable.getColumn("Harga Satuan (Rp)");
		tableColumnSatuan.setMinWidth(300);
		tableColumnSatuan = hargaSatuanTable.getColumn("Contoh");
		tableColumnSatuan.setMinWidth(170);
		tableColumnSatuan = hargaSatuanTable.getColumn("Sumber informasi");
		tableColumnSatuan.setMinWidth(300);
		tableColumnSatuan = hargaSatuanTable.getColumn("Keterangan");
		tableColumnSatuan.setMinWidth(300);
		tableColumnSatuan = hargaSatuanTable.getColumn(" ");
		tableColumnSatuan.setCellRenderer(new ButtonRenderActionColumn("hargasatuan", hargaSatuan));
		tableColumnSatuan.setCellEditor(new ButtonActionEditor("hargasatuan", hargaSatuan));
		tableColumnSatuan.setMinWidth(130);
		
		LabelK standardJasaLabel = new LabelK("C  STANDAR HARGA SATUAN JASA KONSULTANSI");
		standardJasaLabel.setPreferredSize(standardSatuanBarangLabel.getPreferredSize());
		panel.add(standardJasaLabel);
		
		LabelK revisiJasabel1 = new LabelK("Revisi ke ");
		revisiJasabel1.setPreferredSize(revisiKeLbel.getPreferredSize());
		panel.add(revisiJasabel1);
		
		revisiJasaTextFieldK = new TextFieldK();
		revisiJasaTextFieldK.setPreferredSize(revisiBarangTextFieldK.getPreferredSize());
		panel.add(revisiJasaTextFieldK);
		
		hargaJasaKolom = new String[] { "No.", "Jenis Jasa Konsultasi", "Merk/ Tipe/ Spesifikasi", "Satuan", "Harga Satuan (Rp)", "Contoh", "Sumber informasi", "Keterangan", " " };
		hargaJasaDataini = new Vector<Object>();
		hargaJasaTableModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;
			
			public int getColumnCount() {
				return hargaJasaKolom.length;
			}
			
			public int getRowCount() {
				return hargaJasaDataini.size();
			}
			
			public Object getValueAt(int baris1, int kolom1) {
				Vector<?> barisan = (Vector<?>) hargaJasaDataini.elementAt(baris1);
				return barisan.elementAt(kolom1);
			}
			
			public String getColumnName(int kolom1) {
				return hargaJasaKolom[kolom1];
			}
			
			public boolean isCellEditable(int baris1, int kolom1) {
				if (kolom1 == hargaJasaKolom.length - 1) {
					return true;
				} else if (baris1 == 0 && kolom1 != 0 && kolom1 != hargaJasaKolom.length - 1) {
					return true;
				}
				return false;
			}
			
			@SuppressWarnings("unchecked")
			public void setValueAt(Object obj, int baris1, int kolom1) {
				if (baris1 < getRowCount()) {
					Vector<Object> barisdata = (Vector<Object>) hargaJasaDataini.elementAt(baris1);
					barisdata.setElementAt(obj, kolom1);
				}
			}
			
			public Class<? extends Object> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}
		};
		hargaJasaTable = new TableK(hargaJasaTableModel);
		
		ButtonK addHargaJasa = new ButtonK("Tambah");
		addHargaJasa.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tambah.png")));
		addHargaJasa.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			closeAll();
    			hargaJasaFormPanel.setTambah();
    		}
    	});
		panel.add(addHargaJasa);
		
		JScrollPane tableJasaScroll1 = new JScrollPane(hargaJasaTable);
		tableJasaScroll1.setPreferredSize(tableScroll.getPreferredSize());
		hargaJasaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		panel.add(tableJasaScroll1);
		
		TableColumn tableColumnJasa = new TableColumn();
		DefaultTableCellRenderer rightJasaRenderer = new DefaultTableCellRenderer();
		rightJasaRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tableColumnJasa = hargaJasaTable.getColumn("No.");
		tableColumnJasa.setMinWidth(50);
		tableColumnJasa.setCellRenderer(rightJasaRenderer);
		tableColumnJasa = hargaJasaTable.getColumn("Jenis Jasa Konsultasi");
		tableColumnJasa.setMinWidth(230);
		tableColumnJasa = hargaJasaTable.getColumn("Merk/ Tipe/ Spesifikasi");
		tableColumnJasa.setMinWidth(300);
		tableColumnJasa = hargaJasaTable.getColumn("Satuan");
		tableColumnJasa.setMinWidth(200);
		tableColumnJasa = hargaJasaTable.getColumn("Harga Satuan (Rp)");
		tableColumnJasa.setMinWidth(300);
		tableColumnJasa = hargaJasaTable.getColumn("Contoh");
		tableColumnJasa.setMinWidth(170);
		tableColumnJasa = hargaJasaTable.getColumn("Sumber informasi");
		tableColumnJasa.setMinWidth(300);
		tableColumnJasa = hargaJasaTable.getColumn("Keterangan");
		tableColumnJasa.setMinWidth(300);
		tableColumnJasa = hargaJasaTable.getColumn(" ");
		tableColumnJasa.setCellRenderer(new ButtonRenderActionColumn("hargajasa", hargaSatuan));
		tableColumnJasa.setCellEditor(new ButtonActionEditor("hargajasa", hargaSatuan));
		tableColumnJasa.setMinWidth(130);
		
		LabelK standardLainLabel = new LabelK("D  STANDAR HARGA SATUAN JASA LAINNYA");
		standardLainLabel.setPreferredSize(standardSatuanBarangLabel.getPreferredSize());
		panel.add(standardLainLabel);
		
		LabelK revisiLainbel1 = new LabelK("Revisi ke ");
		revisiLainbel1.setPreferredSize(revisiKeLbel.getPreferredSize());
		panel.add(revisiLainbel1);
		
		revisiLainTextFieldK = new TextFieldK();
		revisiLainTextFieldK.setPreferredSize(revisiBarangTextFieldK.getPreferredSize());
		panel.add(revisiLainTextFieldK);
		
		hargaLainKolom = new String[] { "No.", "Jenis Jasa Lainnya", "Merk/ Tipe/ Spesifikasi", "Satuan", "Harga Satuan (Rp)", "Contoh", "Sumber informasi", "Keterangan", " " };
		hargaLainDataini = new Vector<Object>();
		hargaLainTableModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;
			
			public int getColumnCount() {
				return hargaLainKolom.length;
			}
			
			public int getRowCount() {
				return hargaLainDataini.size();
			}
			
			public Object getValueAt(int baris1, int kolom1) {
				Vector<?> barisan = (Vector<?>) hargaLainDataini.elementAt(baris1);
				return barisan.elementAt(kolom1);
			}
			
			public String getColumnName(int kolom1) {
				return hargaLainKolom[kolom1];
			}
			
			public boolean isCellEditable(int baris1, int kolom1) {
				if (kolom1 == hargaLainKolom.length - 1) {
					return true;
				} else if (baris1 == 0 && kolom1 != 0 && kolom1 != hargaLainKolom.length - 1) {
					return true;
				}
				return false;
			}
			
			@SuppressWarnings("unchecked")
			public void setValueAt(Object obj, int baris1, int kolom1) {
				if (baris1 < getRowCount()) {
					Vector<Object> barisdata = (Vector<Object>) hargaLainDataini.elementAt(baris1);
					barisdata.setElementAt(obj, kolom1);
				}
			}
			
			public Class<? extends Object> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}
		};
		hargaLainTable = new TableK(hargaLainTableModel);
		ButtonK addHargaLain = new ButtonK("Tambah");
		addHargaLain.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tambah.png")));
		addHargaLain.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			closeAll();
    			hargaLainFormPanel.setTambah();
    		}
    	});
		panel.add(addHargaLain);
		JScrollPane tableLainScroll1 = new JScrollPane(hargaLainTable);
		tableLainScroll1.setPreferredSize(tableScroll.getPreferredSize());
		hargaLainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		panel.add(tableLainScroll1);
		
		TableColumn tableColumnLain = new TableColumn();
		DefaultTableCellRenderer rightLainRenderer = new DefaultTableCellRenderer();
		rightLainRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tableColumnLain = hargaLainTable.getColumn("No.");
		tableColumnLain.setMinWidth(50);
		tableColumnLain.setCellRenderer(rightLainRenderer);
		tableColumnLain = hargaLainTable.getColumn("Jenis Jasa Lainnya");
		tableColumnLain.setMinWidth(210);
		tableColumnLain = hargaLainTable.getColumn("Merk/ Tipe/ Spesifikasi");
		tableColumnLain.setMinWidth(300);
		tableColumnLain = hargaLainTable.getColumn("Satuan");
		tableColumnLain.setMinWidth(200);
		tableColumnLain = hargaLainTable.getColumn("Harga Satuan (Rp)");
		tableColumnLain.setMinWidth(300);
		tableColumnLain = hargaLainTable.getColumn("Contoh");
		tableColumnLain.setMinWidth(170);
		tableColumnLain = hargaLainTable.getColumn("Sumber informasi");
		tableColumnLain.setMinWidth(300);
		tableColumnLain = hargaLainTable.getColumn("Keterangan");
		tableColumnLain.setMinWidth(300);
		tableColumnLain = hargaLainTable.getColumn(" ");
		tableColumnLain.setCellRenderer(new ButtonRenderActionColumn("hargalain", hargaSatuan));
		tableColumnLain.setCellEditor(new ButtonActionEditor("hargalain", hargaSatuan));
		tableColumnLain.setMinWidth(130);
		
		ButtonK pdfButtonK = new ButtonK("P D F");
		pdfButtonK.setIcon(new ImageIcon(getClass().getResource("/apps/icons/pdf.png")));
		pdfButtonK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				createPdf();
			}
		});
		panel.add(pdfButtonK);
		
		ButtonK printButtonK = new ButtonK("PRINT");
		printButtonK.setIcon(new ImageIcon(getClass().getResource("/apps/icons/printer.png")));
		printButtonK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				printPdf();
			}
		});
		panel.add(printButtonK);
		
		hargaBarangFormPanel = new HargaBarangFormPanel(jPanel, mainForm1, hargaSatuan);
		jPanel.add(hargaBarangFormPanel);
		hargaSatuanFormPanel = new HargaSatuanFormPanel(jPanel, mainForm1, hargaSatuan);
		jPanel.add(hargaSatuanFormPanel);
		hargaJasaFormPanel = new HargaJasaFormPanel(jPanel, mainForm1, hargaSatuan);
		jPanel.add(hargaJasaFormPanel);
		hargaLainFormPanel = new HargaLainFormPanel(jPanel, mainForm1, hargaSatuan);
		jPanel.add(hargaLainFormPanel);
	}

	public void view() {
		hargaSatuan.setVisible(true);
	}
	
	public void closeAll() {
		hargaSatuan.setVisible(false);
		hargaBarangFormPanel.setVisible(false);
		hargaSatuanFormPanel.setVisible(false);
		hargaJasaFormPanel.setVisible(false);
		hargaLainFormPanel.setVisible(false);
	}
	
	@SuppressWarnings("unchecked")
	public void addRowHargaBarang(Integer index, Vector<Object> row) {
		if (index == null) {
			row.set(0, hargaBarangDataini.size() + 1);
			row.set(8, hargaBarangDataini.size());
			hargaBarangDataini.addElement(row);
		} else {
			Vector<Object> rowSelected = (Vector<Object>) hargaBarangDataini.get(index);
			row.set(0, rowSelected.get(0));
			row.set(8, rowSelected.get(8));
			hargaBarangDataini.set(index, row);
		}
		hargaBarangTableModel.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void editHargaBarang(int row) {
		closeAll();
		hargaBarangFormPanel.setEdit(row, (Vector<Object>) hargaBarangDataini.get(row));
	}
	
	public void deleteHargaBarang(int row) {
		hargaBarangDataini.remove(row);
		hargaBarangTableModel.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void addRowHargaSatuan(Integer index, Vector<Object> row) {
		if (index == null) {
			row.set(0, hargaSatuanDataini.size() + 1);
			row.set(8, hargaSatuanDataini.size());
			hargaSatuanDataini.addElement(row);
		} else {
			Vector<Object> rowSelected = (Vector<Object>) hargaSatuanDataini.get(index);
			row.set(0, rowSelected.get(0));
			row.set(8, rowSelected.get(8));
			hargaSatuanDataini.set(index, row);
		}
		hargaSatuanTableModel.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void editHargaSatuan(int row) {
		closeAll();
		hargaSatuanFormPanel.setEdit(row, (Vector<Object>) hargaSatuanDataini.get(row));
	}
	
	public void deleteHargaSatuan(int row) {
		hargaSatuanDataini.remove(row);
		hargaSatuanTableModel.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void addRowHargaJasa(Integer index, Vector<Object> row) {
		if (index == null) {
			row.set(0, hargaJasaDataini.size() + 1);
			row.set(8, hargaJasaDataini.size());
			hargaJasaDataini.addElement(row);
		} else {
			Vector<Object> rowSelected = (Vector<Object>) hargaJasaDataini.get(index);
			row.set(0, rowSelected.get(0));
			row.set(8, rowSelected.get(8));
			hargaJasaDataini.set(index, row);
		}
		hargaJasaTableModel.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void editHargaJasa(int row) {
		closeAll();
		hargaJasaFormPanel.setEdit(row, (Vector<Object>) hargaJasaDataini.get(row));
	}
	
	public void deleteHargaJasa(int row) {
		hargaJasaDataini.remove(row);
		hargaJasaTableModel.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void addRowHargaLain(Integer index, Vector<Object> row) {
		if (index == null) {
			row.set(0, hargaLainDataini.size() + 1);
			row.set(8, hargaLainDataini.size());
			hargaLainDataini.addElement(row);
		} else {
			Vector<Object> rowSelected = (Vector<Object>) hargaLainDataini.get(index);
			row.set(0, rowSelected.get(0));
			row.set(8, rowSelected.get(8));
			hargaLainDataini.set(index, row);
		}
		hargaLainTableModel.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void editHargaLain(int row) {
		closeAll();
		hargaLainFormPanel.setEdit(row, (Vector<Object>) hargaLainDataini.get(row));
	}
	
	public void deleteHargaLain(int row) {
		hargaLainDataini.remove(row);
		hargaLainTableModel.fireTableDataChanged();
	}
}
