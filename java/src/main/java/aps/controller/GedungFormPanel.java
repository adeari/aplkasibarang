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
import apps.component.LabelK;
import apps.component.TextFieldK;
import apps.tables.Gedung;

public class GedungFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private TextFieldK namaGedung;
	private ButtonK simpanButton, resetButton, tabelButton;
	private LabelK labelStatus;
	private Timer timer;
	private MainForm mainForm;
	private Integer idEditted;
	private List<Predicate> predicates;
	private Predicate[] predicatesr;
	
	public GedungFormPanel(JPanel jPanel, MainForm mainForm1) {
		mainForm = mainForm1;
		int width = Double.valueOf(jPanel.getPreferredSize().getWidth()).intValue() - 20;
		setPreferredSize(new Dimension(width, 400));
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
		int textWidth = width * 75 / 100;
		LabelK labelK = new LabelK("Nama gedung");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);
		
		namaGedung = new TextFieldK();
		namaGedung.setPreferredSize(new Dimension(textWidth, 35));
		add(namaGedung);
		
		simpanButton = new ButtonK("Simpan");
		simpanButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/save.png")));
		simpanButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (namaGedung.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Isi nama gedung</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					namaGedung.requestFocus();
					return;
				}
				if (isGedungWithSameNameExist(namaGedung.getText(), idEditted)) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Gedung dengan nama <font style=\"color:blue;\">".concat(namaGedung.getText()).concat("</font> telah terdaftar</span>"),
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					namaGedung.requestFocus();
					return;
				}
				Gedung gedung = new Gedung();
				gedung.setGedung(namaGedung.getText());
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				Transaction transaction = session.beginTransaction();
				if (idEditted == null) {
					session.save(gedung);
				} else {
					gedung.setId(idEditted);
					session.update(gedung);
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
				namaGedung.setText("");
			}
		});
		add(resetButton);
		tabelButton = new ButtonK("Tabel");
		tabelButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/tabel.png")));
		tabelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainForm.viewGedungTable();
			}
		});
		add(tabelButton);
		labelStatus = new LabelK("");
		add(labelStatus);
	}
	
	public void setTambah() {
		titel.setText("Tambah gedung");
		idEditted = null;
		resetButton.doClick();
		setVisible(true);
	}
	
	public void setEdit(int id) {
		titel.setText("Edit gedung");
		resetButton.doClick();
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Gedung> criteriaQuery = criteriaBuilder.createQuery(Gedung.class);
		Root<Gedung> root = criteriaQuery.from(Gedung.class);
		
		criteriaQuery.select(root);
		idEditted = id;
		criteriaQuery.where(criteriaBuilder.equal(root.get("id"), idEditted));
		List<Gedung> gedungs = (List<Gedung>) session.createQuery(criteriaQuery).getResultList();
		for (Gedung gedung : gedungs) {
			namaGedung.setText(gedung.getGedung());
		}
		session.close();
		setVisible(true);
	}
	
	private void afterSaved() {
		namaGedung.requestFocus();
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
	
	private boolean isGedungWithSameNameExist(String namagedung, Integer id) {
		Session sessionCheck = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilderCheck = sessionCheck.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuerycheck = criteriaBuilderCheck.createQuery(Long.class);
		criteriaQuerycheck.select(criteriaBuilderCheck.count(criteriaQuerycheck.from(Gedung.class)));
		
		Root<Gedung> root = criteriaQuerycheck.from(Gedung.class);
		predicates.add(criteriaBuilderCheck.equal(root.get("gedung"), namagedung));
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
