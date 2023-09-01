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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.Plan;

/**
 * Pop-up Dialog for adding new plans.
 * 
 */
public class NewPlanDialog extends BaseDialog {

	private Label lblPlanName = null;
	private Label lblStartBalance = null;
	private Label lblStartAge = null;
	private Text txtPlanName = null;
	private Text txtStartBalance = null;
	private Text txtStartAge = null;
	private Button cmdSave = null;
	private Button cmdCancel = null;
	
	private DataFormatUtil formatter = null;

	public NewPlanDialog() {
		super();
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
	}

	protected void LoadUI(Shell sh) {
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		lblPlanName = new Label(sh, SWT.NONE);
		lblStartBalance = new Label(sh, SWT.NONE);
		lblStartAge = new Label(sh, SWT.NONE);

		txtPlanName = new Text(sh, SWT.BORDER);
		txtStartBalance = new Text(sh, SWT.BORDER);
		txtStartAge = new Text(sh, SWT.BORDER);
		cmdSave = new Button(sh, SWT.PUSH);
		cmdCancel = new Button(sh, SWT.PUSH);

		lblPlanName.setText(MM.PHRASES.getPhrase("337"));
		lblStartBalance.setText(MM.PHRASES.getPhrase("338"));
		lblStartAge.setText(MM.PHRASES.getPhrase("339"));

		cmdSave.setText(MM.PHRASES.getPhrase("38"));
		cmdSave.addSelectionListener(cmdSave_OnClick);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);
		txtStartBalance.setText("0");
		
		// Set tab order
		sh.setTabList(new Control[] {txtPlanName, txtStartBalance, txtStartAge, cmdSave, cmdCancel});

	}

	protected void LoadLayout() {
		FormData lblplannamedata = new FormData();
		lblplannamedata.top = new FormAttachment(0, 20);
		lblplannamedata.left = new FormAttachment(0, 40);
		lblPlanName.setLayoutData(lblplannamedata);

		FormData txtplannamedata = new FormData();
		txtplannamedata.top = new FormAttachment(0, 20);
		txtplannamedata.left = new FormAttachment(lblPlanName, 80);
		txtplannamedata.right = new FormAttachment(100, -40);
		txtPlanName.setLayoutData(txtplannamedata);

		FormData lblstartbalancedata = new FormData();
		lblstartbalancedata.top = new FormAttachment(lblPlanName, 20);
		lblstartbalancedata.left = new FormAttachment(0, 40);
		lblStartBalance.setLayoutData(lblstartbalancedata);

		FormData txtstartbalancedata = new FormData();
		txtstartbalancedata.top = new FormAttachment(lblPlanName, 25);
		txtstartbalancedata.left = new FormAttachment(lblPlanName, 80);
		txtstartbalancedata.right = new FormAttachment(100, -120);
		txtStartBalance.setLayoutData(txtstartbalancedata);

		FormData lblstartagedata = new FormData();
		lblstartagedata.top = new FormAttachment(lblStartBalance, 30);
		lblstartagedata.left = new FormAttachment(0, 40);
		lblStartAge.setLayoutData(lblstartagedata);

		FormData txtstartagedata = new FormData();
		txtstartagedata.top = new FormAttachment(lblStartBalance, 25);
		txtstartagedata.left = new FormAttachment(lblPlanName, 80);
		txtstartagedata.right = new FormAttachment(100, -200);
		txtStartAge.setLayoutData(txtstartagedata);

		
		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(lblStartAge, 20);
		cmdcanceldata.left = new FormAttachment(0, 95);
		cmdcanceldata.right = new FormAttachment(100, -255);
		cmdCancel.setLayoutData(cmdcanceldata);
		
		FormData cmdsavedata = new FormData();
		cmdsavedata.top = new FormAttachment(lblStartAge, 20);
		cmdsavedata.left = new FormAttachment(cmdCancel, 5);
		cmdsavedata.right = new FormAttachment(100, -115);
		cmdSave.setLayoutData(cmdsavedata);

	}

	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("334") + " " + MM.APPTITLE);

		shell.setSize(500, 200);
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

	SelectionAdapter cmdSave_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			MainAction action = new MainAction();
			boolean doDispose = true;
			boolean inputValid = false;
			
			if (StringUtils.isNoneEmpty(txtPlanName.getText())) {
				if (DataFormatUtil.isInteger(txtStartAge.getText())) {
					if (DataFormatUtil.isNumber(txtStartBalance.getText())) {
			
						inputValid = true;
						
						// Check if plan name is unique
						Plan plan = MM.sqlMap.selectOne("getPlanByName", txtPlanName.getText());
						
						if (plan == null) {
							plan = new Plan();
							plan.setPlanName(txtPlanName.getText());
							plan.setStartAge(Integer.parseInt(txtStartAge.getText()));
							plan.setStartBalance(DataFormatUtil.moneyToLong(txtStartBalance.getText()));
							//insertPlan
							MM.sqlMap.insert("insertPlan", plan);
	
						
						} else {
							MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
									MM.PHRASES.getPhrase("341"));
							show.Open();
							doDispose = false;
						}
					}
				}
			}
			
			if (!inputValid) {
				MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
						MM.PHRASES.getPhrase("340"));
				show.Open();
				doDispose = false;
			}
						
			if (doDispose){
				shell.dispose();
			}

		}
	};

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			shell.dispose();
		}
	};

}
