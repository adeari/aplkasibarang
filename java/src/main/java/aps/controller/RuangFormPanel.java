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
import apps.tables.Ruang;

public class RuangFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private TextFieldK namaRuang;
	private ButtonK simpanButton, resetButton, tabelButton;
	private LabelK labelStatus;
	private Timer timer;
	private Combobox gedungCombobox;
	private List<Gedung> gedungValues;
	private MainForm mainForm;
	private Integer idEditted;
	private List<Predicate> predicates;
	private Predicate[] predicatesr;

	public RuangFormPanel(JPanel jPanel, MainForm mainForm1) {
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
		add(gedungCombobox);
		
		
		
		labelK = new LabelK("Nama Ruang");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);
		
		namaRuang = new TextFieldK();
		namaRuang.setPreferredSize(new Dimension(textWidth, 35));
		add(namaRuang);
		
		simpanButton = new ButtonK("Simpan");
		simpanButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/save.png")));
		simpanButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (namaRuang.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Isi nama ruang</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					namaRuang.requestFocus();
					return;
				}
				
				if (isRuangWithSameNameExist(namaRuang.getText(), idEditted)) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Ruang <font style=\"color:blue;\">".concat(namaRuang.getText()).concat("</font> telah terdaftar</span>"),
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					namaRuang.requestFocus();
					return;
				}
				
				Ruang ruang = new Ruang();
				ruang.setRuang(namaRuang.getText());
				ruang.setGedung(gedungValues.get(gedungCombobox.getSelectedIndex()));
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				Transaction transaction = session.beginTransaction();
				
				if (idEditted == null) {
					session.save(ruang);
				} else {
					ruang.setId(idEditted);
					session.update(ruang);
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
				namaRuang.setText("");
			}
		});
		add(resetButton);
		tabelButton = new ButtonK("Tabel");
		tabelButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tabel.png")));
		tabelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainForm.viewRuangTable();
			}
		});
		add(tabelButton);
		labelStatus = new LabelK("");
		add(labelStatus);
	}
	
	public void setTambah() {
		if (refreshGedung()) {
			titel.setText("Tambah ruang");
			setVisible(true);
			resetButton.doClick();
			idEditted = null;
		}
	}
	
	public void setEdit(int id) {
		if (refreshGedung()) {
			titel.setText("Edit ruang");
			resetButton.doClick();
			
			Session session = HibernateUtil.getSessionFactory().openSession();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Ruang> criteriaQuery = criteriaBuilder.createQuery(Ruang.class);
			Root<Ruang> root = criteriaQuery.from(Ruang.class);
			
			criteriaQuery.select(root);
			idEditted = id;
			criteriaQuery.where(criteriaBuilder.equal(root.get("id"), idEditted));
			List<Ruang> ruangs = (List<Ruang>) session.createQuery(criteriaQuery).getResultList();
			for (Ruang ruang : ruangs) {
				int idGedung = ruang.getGedung().getId();
				int i = 0;
				for (Gedung gedung : gedungValues) {
					if (gedung.getId() == idGedung) {
						gedungCombobox.setSelectedIndex(i);
						break;
					}
					i++;
				}
				namaRuang.setText(ruang.getRuang());
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
		gedungCombobox.removeAllItems();
		if (gedungValues != null) {
			for (Gedung gedung : gedungValues) {
				gedungCombobox.addItem(gedung.getGedung());
			}
		}
		session.close();
		if (gedungCombobox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Isi gedung dulu</span>",
					"Perhatian", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	private void afterSaved() {
		namaRuang.requestFocus();
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
	
	private boolean isRuangWithSameNameExist(String namaruang, Integer id) {
		Session sessionCheck = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilderCheck = sessionCheck.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuerycheck = criteriaBuilderCheck.createQuery(Long.class);
		criteriaQuerycheck.select(criteriaBuilderCheck.count(criteriaQuerycheck.from(Ruang.class)));
		
		Gedung gedung = gedungValues.get(gedungCombobox.getSelectedIndex());
		Root<Ruang> root = criteriaQuerycheck.from(Ruang.class);
		predicates.add(criteriaBuilderCheck.equal(root.get("ruang"), namaruang));
		predicates.add(criteriaBuilderCheck.equal(root.get("gedung"), gedung));
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
