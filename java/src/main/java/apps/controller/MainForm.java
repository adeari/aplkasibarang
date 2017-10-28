package apps.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import apps.component.Menu;
import apps.component.MenuBar;
import apps.component.MenuItem;
import apps.component.Properties;
import apps.controller.laporan.HargaSatuan;

public class MainForm {
	private GedungFormPanel gedungFormPanel;
	private RuangFormPanel ruangFormPanel;
	private RakFormPanel rakFormPanel;
	private BarangFormPanel barangFormPanel;
	private BarangTablePanel barangTablePanel;
	private BarangTableUmumPanel barangTableUmumPanel;
	private RakTablePanel rakTablePanel;
	private RuangTablePanel ruangTablePanel;
	private GedungTablePanel gedungTablePanel;
	private LoginFormPanel loginFormPanel;
	private PasswordFormPanel passwordFormPanel;
	private HargaSatuan hargaSatuan;
	private JPanel panel;
	private MainForm mainForm;
	private Menu menu, laporanMenu;
	private MenuItem loginAdmin, barangUmum;

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

		ruangFormPanel = new RuangFormPanel(panel, mainForm);
		panel.add(ruangFormPanel);
		gedungFormPanel = new GedungFormPanel(panel, mainForm);
		panel.add(gedungFormPanel);
		rakFormPanel = new RakFormPanel(panel, mainForm);
		panel.add(rakFormPanel);
		barangTablePanel = new BarangTablePanel(panel, mainForm);
		panel.add(barangTablePanel);
		barangFormPanel = new BarangFormPanel(panel, mainForm);
		panel.add(barangFormPanel);
		barangTableUmumPanel = new BarangTableUmumPanel(panel, mainForm);
		panel.add(barangTableUmumPanel);
		rakTablePanel = new RakTablePanel(panel, mainForm);
		panel.add(rakTablePanel);
		ruangTablePanel = new RuangTablePanel(panel, mainForm);
		panel.add(ruangTablePanel);
		gedungTablePanel = new GedungTablePanel(panel, mainForm);
		panel.add(gedungTablePanel);
		loginFormPanel = new LoginFormPanel(panel, mainForm);
		panel.add(loginFormPanel);
		passwordFormPanel = new PasswordFormPanel(panel, mainForm);
		panel.add(passwordFormPanel);
		hargaSatuan = new HargaSatuan(panel, mainForm);
		panel.add(hargaSatuan);

		frame.add(panel);

		MenuBar menubar = new MenuBar();

		Menu menuUser = new Menu("Menu");
		menubar.add(menuUser);

		barangUmum = new MenuItem("Barang");
		barangUmum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closesss();
				barangTableUmumPanel.view();
			}
		});
		menuUser.add(barangUmum);

		loginAdmin = new MenuItem("Login Admin");
		loginAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Properties.isProduction) {
					closesss();
					loginFormPanel.setLogin();
				} else {
					loginAdmin();
				}
			}
		});
		menuUser.add(loginAdmin);

		menuUser.add(new JSeparator());
		MenuItem keluar = new MenuItem("Keluar");
		keluar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuUser.add(keluar);

		menu = new Menu("Admin");
		menu.setForeground(Color.BLUE);
		menu.setVisible(false);
		menubar.add(menu);

		MenuItem gantiPassword = new MenuItem("Ganti Password");
		gantiPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closesss();
				passwordFormPanel.view();
			}
		});
		menu.add(gantiPassword);

		MenuItem logoutAdmin = new MenuItem("Logout Admin");
		logoutAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closesss();
				menu.setVisible(false);
				laporanMenu.setVisible(false);
				loginAdmin.setVisible(true);
				barangTableUmumPanel.view();
			}
		});
		menu.add(logoutAdmin);
		menu.add(new JSeparator());

		MenuItem tambahGedung = new MenuItem("Tambah gedung");
		tambahGedung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tambahGedung();
			}
		});
		menu.add(tambahGedung);
		MenuItem gedungTable = new MenuItem("Data gedung");
		gedungTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewGedungTable();
			}
		});
		menu.add(gedungTable);

		menu.add(new JSeparator());

		MenuItem tambahRuang = new MenuItem("Tambah ruang");
		tambahRuang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tambahRuang();
			}
		});
		menu.add(tambahRuang);

		MenuItem ruangTable = new MenuItem("Data ruang");
		ruangTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewRuangTable();
			}
		});
		menu.add(ruangTable);

		menu.add(new JSeparator());

		MenuItem tambahRak = new MenuItem("Tambah rak");
		tambahRak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tambahRak();
			}
		});
		menu.add(tambahRak);

		MenuItem rakTable = new MenuItem("Data Rak");
		rakTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewRakTable();
			}
		});
		menu.add(rakTable);

		menu.add(new JSeparator());

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
				viewBarangTable();
			}
		});
		menu.add(barangTable);

		laporanMenu = new Menu("Laporan");
		laporanMenu.setVisible(false);
		menubar.add(laporanMenu);

		MenuItem hargaSatuanMenuItem = new MenuItem("Harga satuan");
		hargaSatuanMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closesss();
				hargaSatuan.view();
			}
		});
		laporanMenu.add(hargaSatuanMenuItem);

		frame.setJMenuBar(menubar);
		frame.pack();
		frame.setVisible(true);

		closesss();
		barangTableUmumPanel.view();
	}

	public void closesss() {
		ruangFormPanel.setVisible(false);
		gedungFormPanel.setVisible(false);
		rakFormPanel.setVisible(false);
		barangFormPanel.setVisible(false);
		barangTablePanel.setVisible(false);
		rakTablePanel.setVisible(false);
		ruangTablePanel.setVisible(false);
		gedungTablePanel.setVisible(false);
		loginFormPanel.setVisible(false);
		passwordFormPanel.setVisible(false);
		barangTableUmumPanel.setVisible(false);
		hargaSatuan.setVisible(false);

	}

	public void tambahBarang() {
		closesss();
		barangFormPanel.setTambah();
	}

	public void tambahRak() {
		closesss();
		rakFormPanel.setTambah();
	}

	public void tambahRuang() {
		closesss();
		ruangFormPanel.setTambah();
	}

	public void tambahGedung() {
		closesss();
		gedungFormPanel.setTambah();
	}

	public void viewRuangTable() {
		closesss();
		ruangTablePanel.view();
	}

	public void viewRakTable() {
		closesss();
		rakTablePanel.view();
	}

	public void viewBarangTable() {
		closesss();
		barangTablePanel.view();
	}

	public void viewGedungTable() {
		closesss();
		gedungTablePanel.view();
	}

	public GedungFormPanel getGedungFormPanel() {
		return gedungFormPanel;
	}

	public void setGedungFormPanel(GedungFormPanel gedungFormPanel) {
		this.gedungFormPanel = gedungFormPanel;
	}

	public RuangFormPanel getRuangFormPanel() {
		return ruangFormPanel;
	}

	public void setRuangFormPanel(RuangFormPanel ruangFormPanel) {
		this.ruangFormPanel = ruangFormPanel;
	}

	public RakFormPanel getRakFormPanel() {
		return rakFormPanel;
	}

	public void setRakFormPanel(RakFormPanel rakFormPanel) {
		this.rakFormPanel = rakFormPanel;
	}

	public BarangFormPanel getBarangFormPanel() {
		return barangFormPanel;
	}

	public void setBarangFormPanel(BarangFormPanel barangFormPanel) {
		this.barangFormPanel = barangFormPanel;
	}

	public void loginAdmin() {
		menu.setVisible(true);
		laporanMenu.setVisible(true);
		loginAdmin.setVisible(false);
		viewBarangTable();
	}
}