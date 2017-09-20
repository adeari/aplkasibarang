import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import apps.component.MenuItem;
import aps.controller.BarangFormPanel;
import aps.controller.BarangTablePanel;
import aps.controller.GedungFormPanel;
import aps.controller.RakFormPanel;
import aps.controller.RuangFormPanel;
import lancarjaya.component.Menu;
import lancarjaya.component.MenuBar;
 
public class Main {
	private static GedungFormPanel gedungFormPanel;
	private static RuangFormPanel ruangFormPanel;
	private static RakFormPanel rakFormPanel;
	private static BarangFormPanel barangFormPanel;
	private static BarangTablePanel barangTablePanel;
	private static JPanel panel;
	
    public static void main(String[] args) {
    	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    	JFrame frame = new JFrame("Aplikasi Barang");
    	frame.setPreferredSize(dimension);
    	frame.setSize(dimension);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setState(Frame.MAXIMIZED_BOTH);
        panel = new JPanel();
        panel.setPreferredSize(frame.getPreferredSize());
        
        ruangFormPanel = new RuangFormPanel(panel);
        panel.add(ruangFormPanel);
        
        gedungFormPanel = new GedungFormPanel(panel);
        panel.add(gedungFormPanel);
        
        rakFormPanel = new RakFormPanel(panel);
        panel.add(rakFormPanel);
        
        barangTablePanel = new BarangTablePanel(panel);
        panel.add(barangTablePanel);
        
        barangFormPanel = new BarangFormPanel(panel);
        panel.add(barangFormPanel);
        
        frame.add(panel);
        
        MenuBar menubar = new MenuBar();
    	Menu menu = new Menu("M e n u");
    	menubar.add(menu);
    	
    	MenuItem tambahGedung = new MenuItem("Tambah gedung");
    	tambahGedung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closesss();
				gedungFormPanel.setTambah();
			}
		});
    	menu.add(tambahGedung);
    	
    	MenuItem tambahRuang = new MenuItem("Tambah ruang");
    	tambahRuang.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			closesss();
    			ruangFormPanel.setTambah();
    		}
    	});
    	menu.add(tambahRuang);
    	
    	MenuItem tambahRak = new MenuItem("Tambah rak");
    	tambahRak.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			closesss();
    			rakFormPanel.setTambah();
    		}
    	});
    	menu.add(tambahRak);
    	
    	MenuItem tambahBarang = new MenuItem("Tambah Barang");
    	tambahBarang.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			closesss();
    			barangFormPanel.setTambah();
    		}
    	});
    	menu.add(tambahBarang);
    	
    	MenuItem barangTable = new MenuItem("Data Barang");
    	barangTable.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			closesss();
    			barangTablePanel.view();
    		}
    	});
    	menu.add(barangTable);
    	
    	frame.setJMenuBar(menubar);
        frame.pack();
        frame.setVisible(true);
        
        closesss();
    }
    
    private static void closesss() {
    	ruangFormPanel.setVisible(false);
    	gedungFormPanel.setVisible(false);
    	rakFormPanel.setVisible(false);
    	barangFormPanel.setVisible(false);
    	barangTablePanel.setVisible(false);
    }
}