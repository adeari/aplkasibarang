package apps.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import aps.controller.BarangTablePanel;
import aps.controller.GedungTablePanel;
import aps.controller.RakTablePanel;
import aps.controller.RuangTablePanel;

public class SearchButton extends ButtonK {
	private static final long serialVersionUID = 1L;
	private SearchButton searchButton;

	public SearchButton(String label, String actionName, Object actionObject) {
		super(label);
		searchButton = this;
		if ("barang".equals(actionName)) {
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					BarangTablePanel barangTablePanel = (BarangTablePanel) actionObject;
					barangTablePanel.populteFilter();
					barangTablePanel.showData();
				}
			});
		} else if ("gedung".equals(actionName)) {
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GedungTablePanel tabePanel = (GedungTablePanel) actionObject;
					tabePanel.populteFilter();
					tabePanel.showData();
				}
			});
		} else if ("ruang".equals(actionName)) {
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RuangTablePanel tabePanel = (RuangTablePanel) actionObject;
					tabePanel.populteFilter();
					tabePanel.showData();
				}
			});
		} else if ("rak".equals(actionName)) {
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RakTablePanel tabePanel = (RakTablePanel) actionObject;
					tabePanel.populteFilter();
					tabePanel.showData();
				}
			});
		}
	}
}
