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
import apps.tables.Rak;
import apps.tables.Ruang;
import lancarjaya.component.Combobox;
import lancarjaya.controller.HibernateUtil;

public class RakFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	
	private TextFieldK namaRak;
	
	private ButtonK simpanButton, resetButton, tabelButton;
	
	private LabelK labelStatus;
	
	private Timer timer;
	
	private Gedung valueEditted;
	
	private Combobox gedungCombobox;
	private List<Gedung> gedungValues;
	
	private Combobox ruangCombobox;
	private List<Ruang> ruangValues;

	public RakFormPanel(JPanel jPanel) {
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
		gedungCombobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshRuangCombobox();
			}
		});
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
				Rak rak = new Rak();
				rak.setRak(namaRak.getText());
				rak.setRuang(ruangValues.get(ruangCombobox.getSelectedIndex()));
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				Transaction transaction = session.beginTransaction();
				session.save(rak);
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
		add(tabelButton);
		labelStatus = new LabelK("");
		add(labelStatus);
	}
	
	public void setTambah() {
		if (refreshGedungCombobox()) {
			titel.setText("Tambah Rak");
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
		gedungCombobox.removeAllItems();
		if (gedungValues != null) {
			for (Gedung gedung : gedungValues) {
				gedungCombobox.addItem(gedung.getGedung());
			}
		}
		session.close();
		return refreshRuangCombobox();
	}
	
	private boolean refreshRuangCombobox() {
		if (gedungCombobox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Isi gedung dulu</span>",
					"Perhatian", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		
		CriteriaQuery<Ruang> criteriaQuery = criteriaBuilder.createQuery(Ruang.class);
		Root<Ruang> root = criteriaQuery.from(Ruang.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("gedung"), gedungValues.get(gedungCombobox.getSelectedIndex())));
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("ruang")));
		if (ruangValues != null) {
			ruangValues.clear();
		}
		ruangValues = (List<Ruang>) session.createQuery(criteriaQuery).getResultList();
		ruangCombobox.removeAllItems();
		if (ruangValues != null) {
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
