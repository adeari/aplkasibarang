package aps.controller;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import apps.component.Menu;
import apps.component.MenuBar;
import apps.component.MenuItem;
 
public class MainForm {
	private GedungFormPanel gedungFormPanel;
	private RuangFormPanel ruangFormPanel;
	private RakFormPanel rakFormPanel;
	private BarangFormPanel barangFormPanel;
	private BarangTablePanel barangTablePanel;
	private RakTablePanel rakTablePanel;
	private JPanel panel;
	private MainForm mainForm;
	
    public MainForm() {
    	mainForm = this;
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
        
        barangTablePanel = new BarangTablePanel(panel, mainForm);
        panel.add(barangTablePanel);
        
        barangFormPanel = new BarangFormPanel(panel);
        panel.add(barangFormPanel);
        
        rakTablePanel = new RakTablePanel(panel);
        panel.add(rakTablePanel);
        
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
    	
    	MenuItem rakTable = new MenuItem("Data Rak");
    	rakTable.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			closesss();
    			rakTablePanel.view();
    		}
    	});
    	menu.add(rakTable);
    	
    	MenuItem tambahBarang = new MenuItem("Tambah Barang");
    	tambahBarang.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			tambahBarang();
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
    
    private void closesss() {
    	ruangFormPanel.setVisible(false);
    	gedungFormPanel.setVisible(false);
    	rakFormPanel.setVisible(false);
    	barangFormPanel.setVisible(false);
    	barangTablePanel.setVisible(false);
    	rakTablePanel.setVisible(false);
    }
    public void tambahBarang() {
    	closesss();
		barangFormPanel.setTambah();
    }
}