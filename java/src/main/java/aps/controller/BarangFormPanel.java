package aps.controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import apps.component.ButtonK;
import apps.component.LabelK;
import apps.component.TextFieldK;
import apps.tables.Barang;
import apps.tables.Gedung;
import apps.tables.Rak;
import apps.tables.Ruang;
import lancarjaya.component.Combobox;
import lancarjaya.controller.HibernateUtil;

public class BarangFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;

	private TextFieldK namaBarang;

	private ButtonK simpanButton, resetButton, tabelButton;

	private LabelK labelStatus;

	private Timer timer;

	private Gedung valueEditted;

	private Combobox gedungCombobox;
	private List<Gedung> gedungValues;

	private Combobox ruangCombobox;
	private List<Ruang> ruangValues;

	private Combobox rakCombobox;
	private List<Rak> rakValues;

	private ActionListener gedungComboboxActionListener;
	private ActionListener ruangComboboxActionListener;

	public BarangFormPanel(JPanel jPanel) {
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
		int textWidth = width * 78 / 100;
		LabelK labelK = new LabelK("Gedung");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);

		gedungCombobox = new Combobox();
		gedungCombobox.setPreferredSize(new Dimension(textWidth, 35));
		gedungComboboxActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshRuangCombobox();
			}
		};
		gedungCombobox.addActionListener(gedungComboboxActionListener);
		add(gedungCombobox);

		labelK = new LabelK("Ruangan");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);

		ruangCombobox = new Combobox();
		ruangCombobox.setPreferredSize(new Dimension(textWidth, 35));
		ruangComboboxActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshRakCombobox();
			}
		};
		ruangCombobox.addActionListener(ruangComboboxActionListener);
		add(ruangCombobox);

		labelK = new LabelK("Rak");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);

		rakCombobox = new Combobox();
		rakCombobox.setPreferredSize(new Dimension(textWidth, 35));
		add(rakCombobox);

		labelK = new LabelK("Nama Barang");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);

		namaBarang = new TextFieldK();
		namaBarang.setPreferredSize(new Dimension(textWidth, 35));
		add(namaBarang);

		simpanButton = new ButtonK("Simpan");
		simpanButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/save.png")));
		simpanButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (namaBarang.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Isi nama barang</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					namaBarang.requestFocus();
					return;
				}
				
				Barang barang = new Barang();
				barang.setBarang(namaBarang.getText());
				barang.setGedung(gedungValues.get(gedungCombobox.getSelectedIndex()));
				if (ruangCombobox.getSelectedItem() != null) {
					barang.setRuang(ruangValues.get(ruangCombobox.getSelectedIndex()));
				}
				if (rakCombobox.getSelectedItem() != null) {
					barang.setRak(rakValues.get(rakCombobox.getSelectedIndex()));
				}

				Session session = HibernateUtil.getSessionFactory().openSession();
				Transaction transaction = session.beginTransaction();
				session.save(barang);
				transaction.commit();
				session.close();
				afterSaved();
			}
		});
		add(simpanButton);
		resetButton = new ButtonK("Reset");
		resetButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/reset.png")));
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				namaBarang.setText("");
			}
		});
		add(resetButton);
		tabelButton = new ButtonK("Tabel");
		tabelButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tabel.png")));
		add(tabelButton);
		labelStatus = new LabelK("");
		add(labelStatus);
	}

	public void setTambah() {
		if (refreshGedung()) {
			titel.setText("Tambah barang");
			setVisible(true);
		}
	}

	private boolean refreshGedung() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

		CriteriaQuery<Gedung> criteriaQuery = criteriaBuilder.createQuery(Gedung.class);
		Root<Gedung> root = criteriaQuery.from(Gedung.class);
		criteriaQuery.select(root);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("gedung")));
		if (gedungValues != null) {
			gedungValues.clear();
		}
		gedungValues = (List<Gedung>) session.createQuery(criteriaQuery).getResultList();
		gedungCombobox.removeActionListener(gedungComboboxActionListener);
		gedungCombobox.removeAllItems();
		if (gedungValues != null) {
			for (Gedung gedung : gedungValues) {
				gedungCombobox.addItem(gedung.getGedung());
			}
		}
		gedungCombobox.addActionListener(gedungComboboxActionListener);
		session.close();
		if (gedungCombobox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Isi gedung dulu</span>",
					"Perhatian", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		refreshRuangCombobox();
		return true;
	}

	private void refreshRuangCombobox() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

		CriteriaQuery<Ruang> criteriaQuery = criteriaBuilder.createQuery(Ruang.class);
		Root<Ruang> root = criteriaQuery.from(Ruang.class);
		criteriaQuery.select(root);
		if (gedungCombobox.getSelectedItem() != null) {
			criteriaQuery.where(
					criteriaBuilder.equal(root.get("gedung"), gedungValues.get(gedungCombobox.getSelectedIndex())));
		}
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("ruang")));
		if (ruangValues != null) {
			ruangValues.clear();
		}
		ruangValues = (List<Ruang>) session.createQuery(criteriaQuery).getResultList();
		ruangCombobox.removeActionListener(ruangComboboxActionListener);
		ruangCombobox.removeAllItems();
		if (ruangValues != null && gedungCombobox.getSelectedItem() != null) {
			for (Ruang ruang : ruangValues) {
				ruangCombobox.addItem(ruang.getRuang());
			}
		}
		ruangCombobox.addActionListener(ruangComboboxActionListener);
		session.close();
		refreshRakCombobox();
	}

	private void refreshRakCombobox() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

		CriteriaQuery<Rak> criteriaQuery = criteriaBuilder.createQuery(Rak.class);
		Root<Rak> root = criteriaQuery.from(Rak.class);
		criteriaQuery.select(root);
		if (ruangCombobox.getSelectedItem() != null) {
		criteriaQuery
				.where(criteriaBuilder.equal(root.get("ruang"), ruangValues.get(ruangCombobox.getSelectedIndex())));
		}
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("rak")));
		if (rakValues != null) {
			rakValues.clear();
		}
		rakValues = (List<Rak>) session.createQuery(criteriaQuery).getResultList();
		rakCombobox.removeAllItems();
		if (rakValues != null && ruangCombobox.getSelectedItem() != null) {
			for (Rak rak : rakValues) {
				rakCombobox.addItem(rak.getRak());
			}
		}
		session.close();
	}

	private void afterSaved() {
		namaBarang.requestFocus();
		labelStatus.setText("Data tersimpan");
		timer = new Timer();
		timer.schedule(new RemindTask(), 3 * 1000, 3 * 1000);
		if (valueEditted == null) {
			resetButton.doClick();
		}
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
