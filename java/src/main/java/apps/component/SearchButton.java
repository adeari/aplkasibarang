package apps.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import apps.controller.BarangTablePanel;
import apps.controller.BarangTableUmumPanel;
import apps.controller.GedungTablePanel;
import apps.controller.RakTablePanel;
import apps.controller.RuangTablePanel;

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
					barangTablePanel.populateFilter();
					barangTablePanel.showData();
				}
			});
		} else if ("gedung".equals(actionName)) {
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GedungTablePanel tabePanel = (GedungTablePanel) actionObject;
					tabePanel.populateFilter();
					tabePanel.showData();
				}
			});
		} else if ("ruang".equals(actionName)) {
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RuangTablePanel tabePanel = (RuangTablePanel) actionObject;
					tabePanel.populateFilter();
					tabePanel.showData();
				}
			});
		} else if ("rak".equals(actionName)) {
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RakTablePanel tabePanel = (RakTablePanel) actionObject;
					tabePanel.populateFilter();
					tabePanel.showData();
				}
			});
		} else if ("barangumum".equals(actionName)) {
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					BarangTableUmumPanel tabePanel = (BarangTableUmumPanel) actionObject;
					tabePanel.populateFilter();
					tabePanel.showData();
				}
			});
		}
	}
}
