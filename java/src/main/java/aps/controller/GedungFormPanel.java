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
import apps.tables.Gedung;

public class GedungFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private TextFieldK namaGedung;
	private ButtonK simpanButton, resetButton, tabelButton;
	private LabelK labelStatus;
	private Timer timer;
	private Gedung valueEditted;
	private MainForm mainForm;
	private Integer idEditted; 
	
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
		setVisible(true);
	}
	
	private void afterSaved() {
		namaGedung.requestFocus();
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
