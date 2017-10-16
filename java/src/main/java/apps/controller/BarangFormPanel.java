package apps.controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import apps.component.ButtonK;
import apps.component.Combobox;
import apps.component.LabelK;
import apps.component.TextFieldK;
import apps.tables.Barang;
import apps.tables.Gedung;
import apps.tables.Rak;
import apps.tables.Ruang;

public class BarangFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private TextFieldK namaBarang;
	private ButtonK simpanButton, resetButton, tabelButton;
	private LabelK labelStatus;
	private Timer timer;
	private Combobox gedungCombobox;
	private List<Gedung> gedungValues;
	private Combobox ruangCombobox;
	private List<Ruang> ruangValues;
	private Combobox rakCombobox;
	private List<Rak> rakValues;
	private ActionListener gedungComboboxActionListener;
	private ActionListener ruangComboboxActionListener;
	private MainForm mainForm;
	private Integer idEditted;
	private List<Predicate> predicates;
	private Predicate[] predicatesr;

	public BarangFormPanel(JPanel jPanel, MainForm mainForm1) {
		mainForm = mainForm1;
		int width = Double.valueOf(jPanel.getPreferredSize().getWidth()).intValue() - 20;
		setPreferredSize(new Dimension(width, 600));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
		flowLayout.setHgap(1);
		setLayout(flowLayout);
		titel = new LabelK("Form gedung");
		titel.setFont(new Font("Arial", Font.BOLD, 50));
		add(titel);
		predicates = new ArrayList<Predicate>();

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

				if (isBarangWithSameNameExist(namaBarang.getText(), idEditted)) {
					JOptionPane.showMessageDialog(null,
							"<html><span style='font-size:22px;'>Rak <font style=\"color:blue;\">"
									.concat(namaBarang.getText()).concat("</font> telah terdaftar</span>"),
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					namaBarang.requestFocus();
					return;
				}

				Barang barang = new Barang();
				barang.setBarang(namaBarang.getText());
				barang.setGedung(gedungValues.get(gedungCombobox.getSelectedIndex()));
				if (ruangCombobox.getItemCount() > 0 && ruangCombobox.getSelectedItem() != null) {
					barang.setRuang(ruangValues.get(ruangCombobox.getSelectedIndex()));
				}
				if (rakCombobox.getItemCount() > 0 && rakCombobox.getSelectedItem() != null) {
					barang.setRak(rakValues.get(rakCombobox.getSelectedIndex()));
				}

				Session session = HibernateUtil.getSessionFactory().openSession();
				Transaction transaction = session.beginTransaction();
				if (idEditted == null) {
					session.save(barang);
				} else {
					barang.setId(idEditted);
					session.update(barang);
				}
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
		tabelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainForm.viewBarangTable();
			}
		});
		add(tabelButton);
		labelStatus = new LabelK("");
		add(labelStatus);
	}

	public void setTambah() {
		if (refreshGedung()) {
			titel.setText("Tambah barang");
			setVisible(true);
			resetButton.doClick();
			idEditted = null;
			
		}
	}

	public void setEdit(int id) {
		if (refreshGedung()) {
			titel.setText("Edit barang");
			idEditted = id;
			resetButton.doClick();

			Session session = HibernateUtil.getSessionFactory().openSession();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Barang> criteriaQuery = criteriaBuilder.createQuery(Barang.class);
			Root<Barang> root = criteriaQuery.from(Barang.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("id"), idEditted));
			List<Barang> barangs = (List<Barang>) session.createQuery(criteriaQuery).getResultList();
			for (Barang barang : barangs) {
				int idGedung = barang.getGedung().getId();
				int i = 0;
				for (Gedung gedung : gedungValues) {
					if (gedung.getId() == idGedung) {
						gedungCombobox.setSelectedIndex(i);
						refreshRuangCombobox();
						if (ruangCombobox.getItemCount() > 0 && barang.getRuang() != null) {
							i = 0;
							int idruang = barang.getRuang().getId();
							for (Ruang ruang : ruangValues) {
								if (ruang.getId() == idruang) {
									ruangCombobox.setSelectedIndex(i);
									refreshRakCombobox();
									if (rakCombobox.getItemCount() > 0 && barang.getRak() != null) {
										i = 0;
										int idRak = barang.getRak().getId();
										for (Rak rak : rakValues) {
											if (rak.getId() == idRak) {
												rakCombobox.setSelectedIndex(i);
												break;
											}
											i++;
										}
									}
									break;
								}
								i++;
							}
						}
						break;
					}
					i++;
				}
				namaBarang.setText(barang.getBarang());
			}
			session.close();

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
		if (idEditted == null) {
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

	private boolean isBarangWithSameNameExist(String namaBarang, Integer id) {
		Session sessionCheck = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilderCheck = sessionCheck.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuerycheck = criteriaBuilderCheck.createQuery(Long.class);
		criteriaQuerycheck.select(criteriaBuilderCheck.count(criteriaQuerycheck.from(Barang.class)));

		Root<Barang> root = criteriaQuerycheck.from(Barang.class);

		Ruang ruang = null;
		if (ruangCombobox.getItemCount() > 0) {
			ruang = ruangValues.get(ruangCombobox.getSelectedIndex());
		}
		predicates.add(criteriaBuilderCheck.equal(root.get("ruang"), ruang));

		Rak rak = null;
		if (rakCombobox.getItemCount() > 0) {
			rak = rakValues.get(rakCombobox.getSelectedIndex());
		}
		predicates.add(criteriaBuilderCheck.equal(root.get("rak"), rak));

		predicates.add(
				criteriaBuilderCheck.equal(root.get("gedung"), gedungValues.get(gedungCombobox.getSelectedIndex())));
		predicates.add(criteriaBuilderCheck.equal(root.get("barang"), namaBarang));
		if (id != null) {
			predicates.add(criteriaBuilderCheck.notEqual(root.get("id"), id));
		}
		predicatesr = predicates.toArray(new Predicate[] {});
		criteriaQuerycheck.where(predicatesr);
		Long dataSize = (Long) sessionCheck.createQuery(criteriaQuerycheck).getSingleResult();
		sessionCheck.close();
		predicates.clear();
		if (dataSize > 0) {
			return true;
		}
		return false;
	}
}
