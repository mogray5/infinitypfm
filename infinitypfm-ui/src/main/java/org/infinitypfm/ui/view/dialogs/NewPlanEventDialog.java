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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.Plan;
import org.infinitypfm.core.data.PlanEvent;
import org.infinitypfm.core.data.PlanEventType;

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
	private Label lblPlanNameValue = null;
	private Label lblEventName = null;
	private Label lblEventType = null;
	private Label lblEventValue = null;
	private Label lblEventValueType = null;
	private Label lblStartAge = null;
	private Label lblEndAge = null;
	
	private Text txtEventName = null;
	private Text txtEventValue = null;
	private Text txtStartAge = null;
	private Text txtEndAge = null;

	private Combo cmbEventValueType = null;
	private Combo cmbEventType = null;
		
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
		lblPlanName.setText(MM.PHRASES.getPhrase("333") + ": ");
		lblPlanNameValue = new Label(sh, SWT.NONE);
		lblPlanNameValue.setText(_plan.getPlanName());
		lblEventName = new Label(sh, SWT.NONE);
		lblEventName.setText(MM.PHRASES.getPhrase("347"));
		lblEventType = new Label(sh, SWT.NONE);
		lblEventType.setText(MM.PHRASES.getPhrase("348") + ":");
		lblEventValue = new Label(sh, SWT.NONE);
		lblEventValue.setText(MM.PHRASES.getPhrase("349") + ":");
		lblEventValueType = new Label(sh, SWT.NONE);
		lblEventValueType.setText(MM.PHRASES.getPhrase("350") + ":");
		lblStartAge = new Label(sh, SWT.NONE);
		lblStartAge.setText(MM.PHRASES.getPhrase("339") + ":");
		lblEndAge = new Label(sh, SWT.NONE);
		lblEndAge.setText(MM.PHRASES.getPhrase("354") + ":");
		
		txtEventName = new Text(sh, SWT.BORDER);
		txtEventValue = new Text(sh, SWT.BORDER);

		cmbEventType = new Combo(sh, SWT.BORDER | SWT.READ_ONLY);
		this.populatePlanEventTypes();
		cmbEventValueType = new Combo(sh, SWT.BORDER | SWT.READ_ONLY);
		this.populatePlanEventValueType();
		
		txtStartAge = new Text(sh, SWT.BORDER);		
		txtEndAge = new Text(sh, SWT.BORDER);
		
		
		cmdSave = new Button(sh, SWT.PUSH);
		cmdCancel = new Button(sh, SWT.PUSH);

		cmdSave.setText(MM.PHRASES.getPhrase("38"));
		cmdSave.addSelectionListener(cmdSave_OnClick);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);
		
		// Set tab order
		sh.setTabList(new Control[] {txtEventName, cmbEventType, txtEventValue, txtStartAge, txtEndAge, cmdSave, cmdCancel});

	}

	protected void LoadLayout() {
		FormData lblplannamedata = new FormData();
		lblplannamedata.top = new FormAttachment(0, 20);
		lblplannamedata.left = new FormAttachment(0, 10);
		lblPlanName.setLayoutData(lblplannamedata);

		FormData lblplannamevaluedata = new FormData();
		lblplannamevaluedata.top = new FormAttachment(0, 20);
		lblplannamevaluedata.left = new FormAttachment(lblEventName, 10);
		lblPlanNameValue.setLayoutData(lblplannamevaluedata);
		
		FormData lbleventnamedata = new FormData();
		lbleventnamedata.top = new FormAttachment(lblPlanName, 12);
		lbleventnamedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(0, 150);
		lblEventName.setLayoutData(lbleventnamedata);

		FormData txteventnamedata = new FormData();
		txteventnamedata.top = new FormAttachment(lblPlanName, 7);
		txteventnamedata.left = new FormAttachment(lblEventName, 10);
		txteventnamedata.right = new FormAttachment(lblEventName, 300);
		txtEventName.setLayoutData(txteventnamedata);

		FormData lbleventtypedata = new FormData();
		lbleventtypedata.top = new FormAttachment(lblEventName, 23);
		lbleventtypedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblEventType.setLayoutData(lbleventtypedata);
		
		FormData cmbeventtypedata = new FormData();
		cmbeventtypedata.top = new FormAttachment(lblEventName, 14);
		cmbeventtypedata.left = new FormAttachment(lblEventName, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		cmbEventType.setLayoutData(cmbeventtypedata);
	
		FormData lbleventvaluedata = new FormData();
		lbleventvaluedata.top = new FormAttachment(lblEventType, 22);
		lbleventvaluedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblEventValue.setLayoutData(lbleventvaluedata);

		FormData txteventvaluedata = new FormData();
		txteventvaluedata.top = new FormAttachment(lblEventType, 14);
		txteventvaluedata.left = new FormAttachment(lblEventName, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		txtEventValue.setLayoutData(txteventvaluedata);

		FormData lbleventvaluetypedata = new FormData();
		lbleventvaluetypedata.top = new FormAttachment(lblEventType, 22);
		lbleventvaluetypedata.left = new FormAttachment(txtEventValue, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblEventValueType.setLayoutData(lbleventvaluetypedata);
	
		FormData cmbeventvaluetypedata = new FormData();
		cmbeventvaluetypedata.top = new FormAttachment(lblEventType, 14);
		cmbeventvaluetypedata.left = new FormAttachment(lblEventValueType, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		cmbEventValueType.setLayoutData(cmbeventvaluetypedata);
		
		FormData lblstartagedata = new FormData();
		lblstartagedata.top = new FormAttachment(lblEventValue, 22);
		lblstartagedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblStartAge.setLayoutData(lblstartagedata);

		FormData txtstartagedata = new FormData();
		txtstartagedata.top = new FormAttachment(lblEventValue, 12);
		txtstartagedata.left = new FormAttachment(lblEventName, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		txtStartAge.setLayoutData(txtstartagedata);
		
		FormData lblendagedata = new FormData();
		lblendagedata.top = new FormAttachment(lblStartAge, 22);
		lblendagedata.left = new FormAttachment(0, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		lblEndAge.setLayoutData(lblendagedata);

		FormData txtendagedata = new FormData();
		txtendagedata.top = new FormAttachment(lblStartAge, 12);
		txtendagedata.left = new FormAttachment(lblEventName, 10);
		//lbleventnamedata.right = new FormAttachment(100, -40);
		txtEndAge.setLayoutData(txtendagedata);
				
		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(lblEndAge, 30);
		cmdcanceldata.left = new FormAttachment(0, 95);
		cmdcanceldata.right = new FormAttachment(100, -255);
		cmdCancel.setLayoutData(cmdcanceldata);
		
		FormData cmdsavedata = new FormData();
		cmdsavedata.top = new FormAttachment(lblEndAge, 30);
		cmdsavedata.left = new FormAttachment(cmdCancel, 5);
		cmdsavedata.right = new FormAttachment(100, -115);
		cmdSave.setLayoutData(cmdsavedata);

	}

	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("346") + " " + MM.APPTITLE);

		shell.setSize(500, 310);
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
	
 	private void populatePlanEventTypes() {
 		
 		cmbEventType.removeAll();
 		List<PlanEventType> eventTypes = MM.sqlMap.selectList("getPlanEventsTypes");
 	
 		for (PlanEventType pType : eventTypes) {
 			cmbEventType.add(pType.getEventTypeName());
 			cmbEventType.setData(pType.getEventTypeName(), pType.getEventTypeID());
 		}
 		cmbEventType.select(0);
 	}
	
 	private void populatePlanEventValueType() {
 		cmbEventValueType.add(MM.PHRASES.getPhrase("352"));
 		cmbEventValueType.add(MM.PHRASES.getPhrase("351"));
 		cmbEventValueType.select(0);
 	}
 	
	/*
	 * Listeners
	 */

	SelectionAdapter cmdSave_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			MainAction action = new MainAction();
			boolean doDispose = true;
			boolean inputValid = false;

			if (!StringUtils.isEmpty(txtEventName.getText()) && 
					DataFormatUtil.isNumber(txtEventValue.getText())) {
				inputValid = true;
				_result = new PlanEvent();
				_result.setEventName(txtEventName.getText());
				_result.setEventTypeId((int)cmbEventType.getData(cmbEventType.getText()));
				_result.setEventValue(formatter.moneyToLong(txtEventValue.getText()));
				_result.setEventValueType(cmbEventValueType.getSelectionIndex());
				_result.setPlanID(_plan.getPlanID());
				_result.setStartAge(Integer.parseInt(txtStartAge.getText()));
				_result.setEndAge(Integer.parseInt(txtEndAge.getText()));
			}
			
			if (!inputValid) {
				MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
						MM.PHRASES.getPhrase("340"));
				show.Open();
				doDispose = false;
			}
			
			MM.sqlMap.insert("insertPlanEvent", _result);
						
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
	
}
