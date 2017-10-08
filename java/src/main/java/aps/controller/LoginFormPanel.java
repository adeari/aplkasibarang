package aps.controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.hibernate.Session;

import Method.MyFunction;
import apps.component.ButtonK;
import apps.component.LabelK;
import apps.component.PasswordK;
import apps.tables.Password;

public class LoginFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private LabelK titel;
	private PasswordK password;
	private LabelK labelStatus;
	private MainForm mainForm;
	private ButtonK loginButton;
	
	public LoginFormPanel(JPanel jPanel, MainForm mainForm1) {
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
		LabelK labelK = new LabelK("Password");
		labelK.setPreferredSize(new Dimension(labelWidth, 35));
		add(labelK);
		
		password = new PasswordK();
		password.setPreferredSize(new Dimension(textWidth, 35));
		password.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginButton.doClick();
				}
			}
		});
		add(password);
		
		loginButton = new ButtonK("Login");
		loginButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/login.png")));
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (password.getPassword().length == 0) {
					JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Isi Password</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
					password.requestFocus();
					return;
				}
				Session session = HibernateUtil.getSessionFactory().openSession();
				CriteriaBuilder criteriaBuilderCheck = session.getCriteriaBuilder();
        		CriteriaQuery<Long> criteriaQuerycheck = criteriaBuilderCheck.createQuery(Long.class);
        		criteriaQuerycheck.select(criteriaBuilderCheck.count(criteriaQuerycheck.from(Password.class)));
            	
        		Root<Password> root = criteriaQuerycheck.from(Password.class);
        		criteriaQuerycheck.where(criteriaBuilderCheck.equal(root.get("password"), MyFunction.MD5(String.valueOf(password.getPassword()))));
        		if ((Long) session.createQuery(criteriaQuerycheck).getSingleResult() > 0) {
        			mainForm.loginAdmin();
        		} else {
        			JOptionPane.showMessageDialog(null, "<html><span style='font-size:22px;'>Password Salah</span>",
							"Perhatian", JOptionPane.ERROR_MESSAGE);
        		}
			}
		});
		add(loginButton);
	}
	
	public void setLogin() {
		titel.setText("Login");
		setVisible(true);
		password.requestFocus();
	}
}
