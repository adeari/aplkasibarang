package apps.controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

import Method.MyFunction;
import apps.component.ButtonK;
import apps.component.LabelK;
import apps.component.PasswordK;
import apps.tables.Password;

public class PasswordFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private PasswordK password, passwordLama, password2;
	private LabelK labelStatus;
	private ButtonK gantiPasswordButton;
	private Timer timer;
	
	public PasswordFormPanel(JPanel jPanel, MainForm mainForm1) {
		int width = Double.valueOf(jPanel.getPreferredSize().getWidth()).intValue() - 20;
		setPreferredSize(new Dimension(width, 400));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
		flowLayout.setHgap(1);
		setLayout(flowLayout);
		titel = new LabelK("GANTI PASSWORD");
		titel.setFont(new Font("Arial", Font.BOLD, 50));
		add(titel);
		
		JSeparator separator = new JSeparator();
		separator.setSize(new Dimension(width, 20));
		separator.setPreferredSize(separator.getSize());
		add(separator);
		int labelWidth = width * 20 / 100;
		int textWidth = width * 75 / 100;
		LabelK labelK = new LabelK("Password Lama");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);
		
		passwordLama = new PasswordK();
		passwordLama.setPreferredSize(new Dimension(textWidth, 35));
		passwordLama.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					password.requestFocus();
				}
			}
		});
		add(passwordLama);
		
		labelK = new LabelK("Password Baru");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);
		
		password = new PasswordK();
		password.setPreferredSize(new Dimension(textWidth, 35));
		password.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					password2.requestFocus();
				}
			}
		});
		add(password);
		
		labelK = new LabelK("Re - Password Baru");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);
		
		password2 = new PasswordK();
		password2.setPreferredSize(new Dimension(textWidth, 35));
		password2.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					password2.requestFocus();
				}
			}
		});
		add(password2);
		
		gantiPasswordButton = new ButtonK("Ganti Password");
		gantiPasswordButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/login.png")));
		gantiPasswordButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (passwordLama.getPassword().length == 0) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Tulis password lama</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					passwordLama.requestFocus();
					return;
				}
				if (password.getPassword().length == 0) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Tulis password</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					password.requestFocus();
					return;
				}
				if (password.getPassword().length < 8) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Tulis password minimal 8 huruf</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					password.requestFocus();
					return;
				}
				if (!String.valueOf(password.getPassword()).equals(String.valueOf(password2.getPassword()))) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Password baru dan RE-Password harus sama</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					password.requestFocus();
					return;
				}
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
				CriteriaQuery<Password> criteriaQuery = criteriaBuilder.createQuery(Password.class);
				Root<Password> root = criteriaQuery.from(Password.class);
				criteriaQuery.select(root);
				criteriaQuery.where(criteriaBuilder.equal(root.get("password"), MyFunction.MD5(String.valueOf(passwordLama.getPassword()))));
				List<Password> passwords = (List<Password>) session.createQuery(criteriaQuery).getResultList();
				session.close();
				if (passwords.size() == 0) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Password lama salah</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					passwordLama.requestFocus();
					return;
				}
				for (Password passwordLooping : passwords) {
					passwordLooping.setPassword(MyFunction.MD5(String.valueOf(password.getPassword())));
					
					session = HibernateUtil.getSessionFactory().openSession();
					Transaction transaction = session.beginTransaction();
					session.update(passwordLooping);
					transaction.commit();
					session.close();
					break;
				}
				afterSaved();
			}
		});
		add(gantiPasswordButton);
		labelStatus = new LabelK("");
		add(labelStatus);
	}
	
	public void view() {
		setVisible(true);
		reset();
	}
	private void reset() {
		passwordLama.requestFocus();
		labelStatus.setText("");
		passwordLama.setText("");
		password.setText("");
		password2.setText("");
	}
	private void afterSaved() {
		passwordLama.requestFocus();
		labelStatus.setText("Passwod diganti");
		timer = new Timer();
		timer.schedule(new RemindTask(), 3 * 1000, 3 * 1000);
	}
	private class RemindTask extends TimerTask {
		public void run() {
			reset();
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
		}
	}
}
