import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.Timer;

import org.hibernate.Session;

import aps.controller.HibernateUtil;
import aps.controller.MainForm;

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
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	count++;
                progressBar.setValue(count);
                if (count >= progressBar.getMaximum()) {
                	count = 1;
                }
                if (session != null && session.isOpen()) {
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
