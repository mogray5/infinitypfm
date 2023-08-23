/*
 * Copyright (c) 2005-2023 Wayne Gray All rights reserved
 * 
 * This file is part of Infinity PFM.
 * 
 * Infinity PFM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Infinity PFM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Infinity PFM.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.infinitypfm.ui.view.dialogs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.CopyPlanParams;
import org.infinitypfm.core.data.Plan;

public class ClonePlanDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	private Label lblPlan = null;
	private Combo cmbPlan = null;
	private Label lblNewPlan = null;
	private Text txtNewPlan = null;
	private Button cmdClone = null;
	private Button cmdCancel = null;
	
	public ClonePlanDialog() {
		super();
	}

	protected void LoadUI(Shell sh) {

		lblPlan = new Label(sh, SWT.NONE);
		lblPlan.setText(MM.PHRASES.getPhrase("333"));
		cmdClone = new Button(sh, SWT.PUSH);
		cmdClone.setText(MM.PHRASES.getPhrase("362"));
		cmdClone.addSelectionListener(cmdSelect_OnClick);
		lblNewPlan = new Label(sh, SWT.NONE);
		lblNewPlan.setText(MM.PHRASES.getPhrase("363"));
		txtNewPlan = new Text(sh, SWT.BORDER);
		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);

		cmbPlan = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);

		try {
			Account act = null;
			@SuppressWarnings("rawtypes")
			List<Plan> plans = MM.sqlMap.selectList("getPlans");

			cmbPlan.removeAll();

			for (Plan plan : plans) {
				cmbPlan.add(plan.getPlanName());
				cmbPlan.setData(plan.getPlanName(), plan.getPlanID());
			}
			
			cmbPlan.select(0);

		} catch (Exception se) {
			InfinityPfm.LogMessage(se.getMessage());
		}
	}

	protected void LoadLayout() {
		FormData lblplandata = new FormData();
		lblplandata.top = new FormAttachment(0, 40);
		lblplandata.left = new FormAttachment(20, 0);
		lblPlan.setLayoutData(lblplandata);

		FormData cmbplandata = new FormData();
		cmbplandata.top = new FormAttachment(0, 30);
		cmbplandata.left = new FormAttachment(lblPlan, 10);
		cmbplandata.right = new FormAttachment(100, -30);
		cmbPlan.setLayoutData(cmbplandata);

		FormData lblnewplandata = new FormData();
		lblnewplandata.top = new FormAttachment(lblPlan, 20);
		lblnewplandata.left = new FormAttachment(20, 0);
		lblNewPlan.setLayoutData(lblnewplandata);
		
		FormData txtnewplandata = new FormData();
		txtnewplandata.top = new FormAttachment(lblPlan, 15);
		txtnewplandata.left = new FormAttachment(lblNewPlan, 10);
		txtnewplandata.right = new FormAttachment(lblNewPlan, 280);
		txtNewPlan.setLayoutData(txtnewplandata);
		
		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(lblNewPlan, 30);
		cmdcanceldata.right = new FormAttachment(40, 10);
		cmdCancel.setLayoutData(cmdcanceldata);

		FormData cmdclonedata = new FormData();
		cmdclonedata.top = new FormAttachment(lblNewPlan, 30);
		cmdclonedata.left = new FormAttachment(cmdCancel, 10);
		cmdclonedata.right = new FormAttachment(cmdCancel, 180);
		cmdClone.setLayoutData(cmdclonedata);
	}

	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("362"));
		shell.setSize(400, 200);
		this.CenterWindow();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return 1;
	}

	/*
	 * Listeners
	 */
	SelectionAdapter cmdSelect_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			int planId = (int) cmbPlan.getData(cmbPlan.getText());
			
			if (!StringUtils.isEmpty(txtNewPlan.getText())) {
			
				Plan plan = MM.sqlMap.selectOne("getPlanByName", txtNewPlan.getText());
				
				if (plan == null) {
				
					plan = MM.sqlMap.selectOne("getPlanById", planId);
					if (plan != null) {
						Plan newPlan = new Plan();
						newPlan.setPlanName(txtNewPlan.getText());
						newPlan.setStartAge(plan.getStartAge());
						newPlan.setStartBalance(plan.getStartBalance());
						MM.sqlMap.insert("insertPlan", newPlan);
						newPlan = MM.sqlMap.selectOne("getPlanByName", newPlan.getPlanName());
						
						if (newPlan != null) {
							
							CopyPlanParams params = new CopyPlanParams();
							params.setOldPlanID(plan.getPlanID());
							params.setNewPlanID(newPlan.getPlanID());
							
							MM.sqlMap.insert("copyPlanEvents", params);
						}
					}
					shell.dispose();
				} else {
					MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
							MM.PHRASES.getPhrase("365"));
					show.Open();
				}
			} else {
				MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
						MM.PHRASES.getPhrase("364"));
				show.Open();
			}
		}
	};

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			userCancelled = true;
			shell.dispose();
		}
	};

}
