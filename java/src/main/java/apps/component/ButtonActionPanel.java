package apps.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class ButtonActionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String handle;
	private Object tableAccess;
	private Integer row;

	public ButtonActionPanel(String handleWhat, Object object) {
		super(new GridLayout(0, 1));
		handle = handleWhat;
		tableAccess = object;
		row = null;
		ButtonK editButton = new ButtonK("");
		editButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/edit.png")));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*if ("jmlContainerinJobPanel".equals(handle)) {
					((JobPanel) tableAccess).setEditFormJmlContainer(row);
				} else if ("dataContainer".equals(handle)) {
					((JobPanel) tableAccess).setEditFormContainer(row);
				} else if ("jobTable".equals(handle)) {
					((JobPanel) tableAccess).setEditFormJob(row);
				} */
			}
		});
		ButtonK deleteButton = new ButtonK("");
		deleteButton.setIcon(new ImageIcon(getClass().getResource("/apps/icons/delete.png")));
		deleteButton.setBackground(Color.RED);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*if ("jmlContainerinJobPanel".equals(handle)) {
					((JobPanel) tableAccess).removeRowTotalContainers(row);
				} else if ("dataContainer".equals(handle)) {
					((JobPanel) tableAccess).removeRowContainers(row);
				} else if ("jobTable".equals(handle)) {
					JobPanel jobPanel = (JobPanel) tableAccess;
					jobPanel.deleteJobByRow(row);
				}*/
			}
		});
		setOpaque(true);
		setBackground(editButton.getBackground());
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		add(editButton, c);
		add(deleteButton, c);
	}

	public void setRow(int rowSelected) {
		row = rowSelected;
	}
}