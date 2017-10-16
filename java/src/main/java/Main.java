import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.Timer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import apps.controller.HibernateUtil;
import apps.controller.MainForm;
import apps.tables.Password;

public class Main extends JWindow {
	private static final long serialVersionUID = 1L;
	static boolean isRegistered;
    private static JProgressBar progressBar = new JProgressBar();
    private static Main execute;
    private static int count;
    private static Timer timer1;
    private static Session session;

    public Main() {
        Container container = getContentPane();
        container.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new javax.swing.border.EtchedBorder());
        panel.setBackground(new Color(255, 255, 255));
        panel.setBounds(10, 10, 348, 200);
        panel.setLayout(null);
        container.add(panel);
        
        JLabel label = new JLabel("Aplikasi Barang");
        label.setFont(new Font("Verdana", Font.BOLD, 18));
        label.setBounds(0, 0, 350, 30);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);

        label = new JLabel("");
        label.setIcon(new ImageIcon(getClass().getResource("/apps/icons/barang.jpg")));
        label.setBounds(0, 30, 350, 170);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);

        progressBar.setMaximum(100);
        progressBar.setBounds(55, 180, 250, 15);
        container.add(progressBar);
        loadProgressBar();
        setSize(370, 215);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadProgressBar() {
        timer1 = new Timer(50, new ActionListener() {
            @SuppressWarnings("unused")
			public void actionPerformed(java.awt.event.ActionEvent evt) {
            	count++;
                progressBar.setValue(count);
                if (count >= progressBar.getMaximum()) {
                	count = 1;
                }
                if (session != null && session.isOpen()) {
                	CriteriaBuilder criteriaBuilderCheck = session.getCriteriaBuilder();
            		CriteriaQuery<Long> criteriaQuerycheck = criteriaBuilderCheck.createQuery(Long.class);
            		criteriaQuerycheck.select(criteriaBuilderCheck.count(criteriaQuerycheck.from(Password.class)));
                	
            		Root<Password> root = criteriaQuerycheck.from(Password.class);
            		if ((Long) session.createQuery(criteriaQuerycheck).getSingleResult() == 0) {
            			session.close();
            			
            			session = HibernateUtil.getSessionFactory().openSession();
            			Transaction transaction = session.beginTransaction();
            			session.save(new Password("admin"));
            			transaction.commit();
            		}
            		session.close();
            		execute.setVisible(false);
            		new MainForm();
                    timer1.stop();
                    timer1 = null;
                    execute = null;
            	}
            }
        });
        timer1.start();
    }

    public static void main(String[] args) {
        execute = new Main();
        session = HibernateUtil.getSessionFactory().openSession();
    }
};
