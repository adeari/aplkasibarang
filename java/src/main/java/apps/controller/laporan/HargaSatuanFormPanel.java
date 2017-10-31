package apps.controller.laporan;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import apps.component.ButtonK;
import apps.component.DecimalTextFieldK;
import apps.component.LabelK;
import apps.component.TextFieldK;
import apps.controller.MainForm;

public class HargaSatuanFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private HargaSatuan hargaSatuan;
	
	private TextFieldK jenisPekerjaan, merk, contoh, satuan, sumberInformasi, keterangan;
	private DecimalTextFieldK hargaSatuanDecimalTextFieldK;
	
	private ButtonK simpanButton, resetButton, tabelButton;
	private LabelK labelStatus;
	private Timer timer;
	private MainForm mainForm;
	private Integer rowEditted;
	
	public HargaSatuanFormPanel(JPanel jPanel, MainForm mainForm1, HargaSatuan hargaSatuan1) {
		mainForm = mainForm1;
		hargaSatuan = hargaSatuan1;
		int width = Double.valueOf(jPanel.getPreferredSize().getWidth()).intValue() - 20;
		setPreferredSize(new Dimension(width, 600));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
		flowLayout.setHgap(1);
		setLayout(flowLayout);
		titel = new LabelK("Form gedung");
		titel.setFont(new Font("Arial", Font.BOLD, 50));
		add(titel);
		
		JSeparator separator = new JSeparator();
		separator.setSize(new Dimension(width, 20));
		separator.setPreferredSize(separator.getSize());
		add(separator);
		int labelWidth = width * 20 / 100;
		int textWidth = width * 75 / 100;
		
		LabelK labelK = new LabelK("Jenis Pekerjaan");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);
		
		jenisPekerjaan = new TextFieldK();
		jenisPekerjaan.setPreferredSize(new Dimension(textWidth, 35));
		add(jenisPekerjaan);
		
		LabelK labelMerk = new LabelK("Merk/ Tipe/ Spesifikasi");
		labelMerk.setPreferredSize(new Dimension(width * 30 / 100, 35));
		add(labelMerk);
		
		merk = new TextFieldK();
		merk.setPreferredSize(new Dimension(width * 65 / 100, 35));
		add(merk);
		
		LabelK labelSatuan = new LabelK("Satuan");
		labelSatuan.setPreferredSize(labelK.getPreferredSize());
		add(labelSatuan);
		
		satuan = new TextFieldK();
		satuan.setPreferredSize(jenisPekerjaan.getPreferredSize());
		add(satuan);
		
		LabelK labelHargaSatuan = new LabelK("Harga Satuan (Rp)");
		labelHargaSatuan.setPreferredSize(labelK.getPreferredSize());
		add(labelHargaSatuan);
		
		hargaSatuanDecimalTextFieldK = new DecimalTextFieldK();
		hargaSatuanDecimalTextFieldK.setPreferredSize(jenisPekerjaan.getPreferredSize());
		add(hargaSatuanDecimalTextFieldK);
		
		LabelK labelContoh = new LabelK("Contoh");
		labelContoh.setPreferredSize(labelK.getPreferredSize());
		add(labelContoh);
		
		contoh = new TextFieldK();
		contoh.setPreferredSize(jenisPekerjaan.getPreferredSize());
		add(contoh);
		
		LabelK labelSumberInformasi = new LabelK("Sumber informasi");
		labelSumberInformasi.setPreferredSize(labelK.getPreferredSize());
		add(labelSumberInformasi);
		
		sumberInformasi = new TextFieldK();
		sumberInformasi.setPreferredSize(jenisPekerjaan.getPreferredSize());
		add(sumberInformasi);
		
		LabelK labelKeterangan = new LabelK("Keterangan");
		labelKeterangan.setPreferredSize(labelK.getPreferredSize());
		add(labelKeterangan);
		
		keterangan = new TextFieldK();
		keterangan.setPreferredSize(jenisPekerjaan.getPreferredSize());
		add(keterangan);
		
		simpanButton = new ButtonK("Simpan");
		simpanButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/save.png")));
		simpanButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Vector<Object> rowData = new Vector<Object>();
				rowData.addElement(1);
				rowData.addElement(jenisPekerjaan.getText());
				rowData.addElement(merk.getText());
				rowData.addElement(satuan.getText());
				rowData.addElement(hargaSatuanDecimalTextFieldK.getText());
				rowData.addElement(contoh.getText());
				rowData.addElement(sumberInformasi.getText());
				rowData.addElement(keterangan.getText());
				rowData.addElement(1);
				hargaSatuan1.addRowHargaSatuan(rowEditted, rowData);
				afterSaved();
			}
		});
		add(simpanButton);
		resetButton = new ButtonK("Reset");
		resetButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/reset.png")));
		resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jenisPekerjaan.setText("");
				merk.setText("");
				satuan.setText("");
				hargaSatuanDecimalTextFieldK.setText("");
				contoh.setText("");
				sumberInformasi.setText("");
				keterangan.setText("");
			}
		});
		add(resetButton);
		tabelButton = new ButtonK("Tabel");
		tabelButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tabel.png")));
		tabelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				hargaSatuan.closeAll();
				hargaSatuan.setVisible(true);
			}
		});
		add(tabelButton);
		labelStatus = new LabelK("");
		add(labelStatus);
	}
	
	public void setTambah() {
		titel.setText("Tambah Harga Satuan Pekerjaan");
		rowEditted = null;
		resetButton.doClick();
		setVisible(true);
	}
	
	public void setEdit(int row, Vector<Object> vector) {
		rowEditted = row;
		titel.setText("Edit Harga Satuan Pekerjaan");
		resetButton.doClick();
		setVisible(true);
		jenisPekerjaan.setText(vector.get(1).toString());
		merk.setText(vector.get(2).toString());
		satuan.setText(vector.get(3).toString());
		hargaSatuanDecimalTextFieldK.setText(vector.get(4).toString());
		contoh.setText(vector.get(5).toString());
		sumberInformasi.setText(vector.get(6).toString());
		keterangan.setText(vector.get(7).toString());
	}
	
	private void afterSaved() {
		jenisPekerjaan.requestFocus();
		labelStatus.setText("Data tersimpan");
		if (rowEditted == null) {
			resetButton.doClick();
		}
		timer = new Timer();
		timer.schedule(new RemindTask(), 3 * 1000, 3 * 1000);
	}
	private class RemindTask extends TimerTask {
		public void run() {
			labelStatus.setText("");
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
		}
	}
}
