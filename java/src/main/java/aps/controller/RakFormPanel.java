package aps.controller;

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
import apps.tables.Gedung;
import apps.tables.Rak;
import apps.tables.Ruang;

public class RakFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private TextFieldK namaRak;
	private ButtonK simpanButton, resetButton, tabelButton;
	private LabelK labelStatus;
	private Timer timer;
	private Combobox gedungCombobox;
	private List<Gedung> gedungValues;
	private Combobox ruangCombobox;
	private List<Ruang> ruangValues;
	private ActionListener gedungComboboxActionListener;
	private MainForm mainForm;
	private Integer idEditted;
	private List<Predicate> predicates;
	private Predicate[] predicatesr;

	public RakFormPanel(JPanel jPanel, MainForm mainForm1) {
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
		add(ruangCombobox);
		
		labelK = new LabelK("Nama Rak");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);
		
		namaRak = new TextFieldK();
		namaRak.setPreferredSize(new Dimension(textWidth, 35));
		add(namaRak);
		
		simpanButton = new ButtonK("Simpan");
		simpanButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/save.png")));
		simpanButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ruangCombobox.getItemCount() == 0) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Ruangan tidak boleh kosong</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (namaRak.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Isi nama rak</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					namaRak.requestFocus();
					return;
				}
				
				if (isRakWithSameNameExist(namaRak.getText(), idEditted)) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Rak <font style=\"color:blue;\">".concat(namaRak.getText()).concat("</font> telah terdaftar</span>"),
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					namaRak.requestFocus();
					return;
				}
				
				Rak rak = new Rak();
				rak.setRak(namaRak.getText());
				rak.setRuang(ruangValues.get(ruangCombobox.getSelectedIndex()));
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				Transaction transaction = session.beginTransaction();
				if (idEditted == null) {
					session.save(rak);
				} else {
					rak.setId(idEditted);
					session.update(rak);
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
				namaRak.setText("");
			}
		});
		add(resetButton);
		tabelButton = new ButtonK("Tabel");
		tabelButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tabel.png")));
		tabelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainForm.viewRakTable();
			}
		});
		add(tabelButton);
		labelStatus = new LabelK("");
		add(labelStatus);
	}
	
	public void setTambah() {
		if (refreshGedungCombobox()) {
			titel.setText("Tambah Rak");
			setVisible(true);
			resetButton.doClick();
			idEditted = null;
		}
	}
	
	public void setEdit(int id) {
		if (refreshGedungCombobox()) {
			titel.setText("Edit Rak");
			resetButton.doClick();
			idEditted = id;
			
			Session session = HibernateUtil.getSessionFactory().openSession();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Rak> criteriaQuery = criteriaBuilder.createQuery(Rak.class);
			Root<Rak> root = criteriaQuery.from(Rak.class);
			criteriaQuery.select(root);
			criteriaQuery.where(criteriaBuilder.equal(root.get("id"), idEditted));
			List<Rak> raks = (List<Rak>) session.createQuery(criteriaQuery).getResultList();
			for (Rak rak : raks) {
				int idruang = rak.getRuang().getId();
				int idGedung = rak.getRuang().getGedung().getId();
				int i = 0;
				for (Gedung gedung : gedungValues) {
					if (gedung.getId() == idGedung) {
						gedungCombobox.setSelectedIndex(i);
						refreshRuangCombobox();
						i = 0;
						for (Ruang ruang : ruangValues) {
							if (ruang.getId() == idruang) {
								ruangCombobox.setSelectedIndex(i);
								break;
							}
							i++;
						}
						break;
					}
					i++;
				}
				namaRak.setText(rak.getRak());
			}
			session.close();
			setVisible(true);
		}
	}
	
	private boolean refreshGedungCombobox() {
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
		session.close();
		gedungCombobox.addActionListener(gedungComboboxActionListener);
		if (gedungCombobox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Isi gedung dulu</span>",
					"Perhatian", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return refreshRuangCombobox();
	}
	
	private boolean refreshRuangCombobox() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		
		CriteriaQuery<Ruang> criteriaQuery = criteriaBuilder.createQuery(Ruang.class);
		Root<Ruang> root = criteriaQuery.from(Ruang.class);
		criteriaQuery.select(root);
		if (gedungCombobox.getSelectedItem() != null) {
			criteriaQuery.where(criteriaBuilder.equal(root.get("gedung"), gedungValues.get(gedungCombobox.getSelectedIndex())));
		}
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("ruang")));
		if (ruangValues != null) {
			ruangValues.clear();
		}
		ruangValues = (List<Ruang>) session.createQuery(criteriaQuery).getResultList();
		ruangCombobox.removeAllItems();
		if (ruangValues != null && gedungCombobox.getSelectedItem() != null) {
			for (Ruang Ruang : ruangValues) {
				ruangCombobox.addItem(Ruang.getRuang());
			}
		}
		session.close();
		return true;
	}
	
	private void afterSaved() {
		namaRak.requestFocus();
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
	
	private boolean isRakWithSameNameExist(String namarak, Integer id) {
		Session sessionCheck = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilderCheck = sessionCheck.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuerycheck = criteriaBuilderCheck.createQuery(Long.class);
		criteriaQuerycheck.select(criteriaBuilderCheck.count(criteriaQuerycheck.from(Rak.class)));
		
		Root<Rak> root = criteriaQuerycheck.from(Rak.class);
		predicates.add(criteriaBuilderCheck.equal(root.get("ruang"), ruangValues.get(ruangCombobox.getSelectedIndex())));
		predicates.add(criteriaBuilderCheck.equal(root.get("rak"), namarak));
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
