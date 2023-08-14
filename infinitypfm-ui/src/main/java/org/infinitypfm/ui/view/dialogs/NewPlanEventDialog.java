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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.Plan;
import org.infinitypfm.core.data.PlanEvent;
import org.infinitypfm.types.DefaultDateFormat;

/**
 * Pop-up Dialog for adding new plan events.  
 * 
 * Plan events are defined as a financial event that can occur monthly
 * such as collecting social security, drawing on an investment,
 * earning interest on an investment, etc.
 * 
 */
public class NewPlanEventDialog extends BaseDialog {

	private Label lblPlanName = null;
	private Label lblEventName = null;
	private Label lblEventType = null;
	private Label lblEventValue = null;
	private Label lblEventValueType = null;
	private Label lblStartDate = null;
	private Label lblEndDate = null;
	
	private Text txtEventName = null;
	private Text txtEventValue = null;
	private Text txtStartDate = null;
	private Text txtEndDate = null;

	private Combo cmbEventValueType = null;
	private Combo cmbEventType = null;
	
	private Button cmdStartDate = null;
	private Button cmdEndDate = null;
	
	private Button cmdSave = null;
	private Button cmdCancel = null;
	
	private DataFormatUtil formatter = null;
	
	private DateDialog dateDialog = null;
	
	private final Plan _plan;
	private PlanEvent _result;

	public NewPlanEventDialog(String title, Plan plan) {
		super();
		_plan = plan;
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
	}
	
	public PlanEvent getResult() {
		return _result;
	}

	protected void LoadUI(Shell sh) {
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		lblPlanName = new Label(sh, SWT.NONE);
		lblEventName = new Label(sh, SWT.NONE);
		lblEventType = new Label(sh, SWT.NONE);
		lblEventValue = new Label(sh, SWT.NONE);
		lblEventValueType = new Label(sh, SWT.NONE);
		lblStartDate = new Label(sh, SWT.NONE);
		lblEndDate = new Label(sh, SWT.NONE);
		
		txtEventName = new Text(sh, SWT.BORDER);
		txtEventValue = new Text(sh, SWT.BORDER);

		cmbEventType = new Combo(sh, SWT.BORDER | SWT.READ_ONLY);
		cmbEventValueType = new Combo(sh, SWT.BORDER | SWT.READ_ONLY);
		
		txtStartDate = new Text(sh, SWT.BORDER);
		formatter.setDate(formatter.getToday());
		formatter.setDate(formatter.getYear(), formatter.getMonth() == 1 ? 1
				: formatter.getMonth() - 1);
		txtStartDate.setText(formatter.getFormat(DefaultDateFormat.DAY));
		txtStartDate.setEditable(false);
		txtEndDate = new Text(sh, SWT.BORDER);
		formatter.setDate(formatter.getToday());
		txtEndDate.setText(formatter.getFormat(DefaultDateFormat.DAY));
		txtEndDate.setEditable(false);
		cmdStartDate = new Button(sh, SWT.PUSH);
		cmdStartDate.setImage(InfinityPfm.imMain.getImage(MM.IMG_CALENDAR));
		cmdStartDate.addSelectionListener(cmdStartDatePicker_OnClick);
		cmdEndDate = new Button(sh, SWT.PUSH);
		cmdEndDate.setImage(InfinityPfm.imMain.getImage(MM.IMG_CALENDAR));
		cmdEndDate.addSelectionListener(cmdEndDatePicker_OnClick);
		
		
		cmdSave = new Button(sh, SWT.PUSH);
		cmdCancel = new Button(sh, SWT.PUSH);

		lblPlanName.setText(MM.PHRASES.getPhrase("337"));


		cmdSave.setText(MM.PHRASES.getPhrase("38"));
		cmdSave.addSelectionListener(cmdSave_OnClick);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);

		
		// Set tab order
		sh.setTabList(new Control[] {txtEventName, cmbEventType, txtEventValue, cmdSave, cmdCancel});

	}

	protected void LoadLayout() {
		FormData lblplannamedata = new FormData();
		lblplannamedata.top = new FormAttachment(0, 20);
		lblplannamedata.left = new FormAttachment(0, 10);
		lblPlanName.setLayoutData(lblplannamedata);

		FormData lbleventnamedata = new FormData();
		lbleventnamedata.top = new FormAttachment(lblPlanName, 10);
		lbleventnamedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblEventName.setLayoutData(lbleventnamedata);

		FormData txteventnamedata = new FormData();
		txteventnamedata.top = new FormAttachment(lblPlanName, 10);
		txteventnamedata.left = new FormAttachment(lblEventName, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		txtEventName.setLayoutData(txteventnamedata);

		FormData lbleventtypedata = new FormData();
		lbleventtypedata.top = new FormAttachment(lblEventName, 10);
		lbleventtypedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblEventType.setLayoutData(lbleventtypedata);
		
		FormData cmbeventtypedata = new FormData();
		cmbeventtypedata.top = new FormAttachment(lblEventName, 10);
		cmbeventtypedata.left = new FormAttachment(lblEventType, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		cmbEventType.setLayoutData(cmbeventtypedata);
	
		FormData lbleventvaluedata = new FormData();
		lbleventvaluedata.top = new FormAttachment(lblEventType, 10);
		lbleventvaluedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblEventValue.setLayoutData(lbleventvaluedata);

		FormData txteventvaluedata = new FormData();
		txteventvaluedata.top = new FormAttachment(lblEventType, 10);
		txteventvaluedata.left = new FormAttachment(lblEventValue, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		txtEventValue.setLayoutData(txteventvaluedata);

		FormData lbleventvaluetypedata = new FormData();
		lbleventvaluetypedata.top = new FormAttachment(lblEventType, 10);
		lbleventvaluetypedata.left = new FormAttachment(txtEventValue, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblEventValueType.setLayoutData(lbleventvaluetypedata);
	
		FormData cmbeventvaluetypedata = new FormData();
		cmbeventvaluetypedata.top = new FormAttachment(lblEventType, 10);
		cmbeventvaluetypedata.left = new FormAttachment(lblEventValueType, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		cmbEventValueType.setLayoutData(cmbeventvaluetypedata);
		
		FormData lblstartdatedata = new FormData();
		lblstartdatedata.top = new FormAttachment(lblEventValue, 10);
		lblstartdatedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblStartDate.setLayoutData(lblstartdatedata);

		FormData txtstartdatedata = new FormData();
		txtstartdatedata.top = new FormAttachment(lblEventValue, 10);
		txtstartdatedata.left = new FormAttachment(lblStartDate, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		txtStartDate.setLayoutData(txtstartdatedata);
		
		FormData cmdstartdatedata = new FormData();
		cmdstartdatedata.top = new FormAttachment(lblEventValue, 10);
		cmdstartdatedata.left = new FormAttachment(txtStartDate, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		cmdStartDate.setLayoutData(cmdstartdatedata);

		FormData lblenddatedata = new FormData();
		lblenddatedata.top = new FormAttachment(lblStartDate, 10);
		lblenddatedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblEndDate.setLayoutData(lblenddatedata);

		FormData txtenddatedata = new FormData();
		txtenddatedata.top = new FormAttachment(lblStartDate, 10);
		txtenddatedata.left = new FormAttachment(lblEndDate, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		txtEndDate.setLayoutData(txtenddatedata);
		
		FormData cmdenddatedata = new FormData();
		cmdenddatedata.top = new FormAttachment(lblStartDate, 10);
		cmdenddatedata.left = new FormAttachment(txtEndDate, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		cmdEndDate.setLayoutData(cmdenddatedata);
		
		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(lblEndDate, 20);
		cmdcanceldata.left = new FormAttachment(0, 95);
		cmdcanceldata.right = new FormAttachment(100, -255);
		cmdCancel.setLayoutData(cmdcanceldata);
		
		FormData cmdsavedata = new FormData();
		cmdsavedata.top = new FormAttachment(lblEndDate, 20);
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
	
	private void Refresh() {
		
	}
	
	/*
	 * Listeners
	 */

	SelectionAdapter cmdSave_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			MainAction action = new MainAction();
			boolean doDispose = true;
			boolean inputValid = false;

			_result = new PlanEvent();
			_result.setEventName(txtEventName.getText());
			_result.setEventTypeId((int)cmbEventType.getData());
			_result.setEventValue(formatter.moneyToLong(txtEventValue.getText()));
			_result.setPlanID(_plan.getPlanID());
			formatter.setDate(txtStartDate.getText());
			_result.setStartDate(formatter.getDate());
			formatter.setDate(txtEndDate.getText());
			_result.setStopDate(formatter.getDate());
			
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
			_result = null;
			shell.dispose();
		}
	};
	
	SelectionAdapter cmdStartDatePicker_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (dateDialog == null) {
				dateDialog = new DateDialog();
			}

			dateDialog.Open();
			
			try {
				txtStartDate.setText(dateDialog.getSelectedDate());
				//updateReportParams();
			} catch (Exception err) {
				InfinityPfm.LogMessage(err.getMessage());
			}
			
			Refresh();
		}
	};

	SelectionAdapter cmdEndDatePicker_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (dateDialog == null) {
				dateDialog = new DateDialog();
			}

			dateDialog.Open();
			
			try {
				txtEndDate.setText(dateDialog.getSelectedDate());
				//updateReportParams();
			} catch (Exception err) {
				InfinityPfm.LogMessage(err.getMessage());
			}
			
			Refresh();
		}
	};

}
