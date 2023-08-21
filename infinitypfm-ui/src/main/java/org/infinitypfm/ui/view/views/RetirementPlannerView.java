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
package org.infinitypfm.ui.view.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.Plan;
import org.infinitypfm.core.data.PlanEvent;
import org.infinitypfm.core.plan.PlanRunner;
import org.infinitypfm.ui.view.dialogs.MessageDialog;
import org.infinitypfm.ui.view.dialogs.NewPlanEventDialog;
import org.infinitypfm.ui.view.toolbars.RetirementPlannerToolbar;

/**
 * Dialog to allow transaction import and setting of offsets before import.
 * 
 */
public class RetirementPlannerView extends BaseView {

	private DataFormatUtil formatter = null;
	private RetirementPlannerToolbar tbMain = null;
	
	private List lstPlans = null;
	private Composite cmpHeader = null;
	private Button cmdRemovePlan = null;
	private Button cmdRunPlan = null;
	private Button cmdAddEvent = null;
	private Button cmdRemoveEvent = null;
	private Table tblPlanDetail = null;
	private Label lblPlanName = null; 
	private Label lblStartAmount = null;
	private Label lblStartAge = null; 
	
	private Plan _plan = null;
	private PlanEvent _planEvent = null;
	
	public RetirementPlannerView(Composite arg0, int arg1) {
		super(arg0, arg1);
		
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		
		// init console
		LoadUI();
		LoadLayout();
		LoadPlans();
		LoadColumns();
	}

	protected void LoadUI() {
		tbMain = new RetirementPlannerToolbar(this);
		lstPlans = new List (this, SWT.BORDER | SWT.V_SCROLL);
		lstPlans.addSelectionListener(lstPlans_OnClick);
		tblPlanDetail = new Table(this, SWT.BORDER);
		tblPlanDetail.addSelectionListener(tblPlanDetail_OnClick);
		cmpHeader = new Composite(this, SWT.BORDER);
		cmpHeader.setLayout(new FormLayout());
		cmdRemovePlan = new Button(cmpHeader, SWT.PUSH);
		cmdRemovePlan.setText(MM.PHRASES.getPhrase("335"));
		cmdRemovePlan.setEnabled(false);
		cmdRemovePlan.addSelectionListener(cmdRemovePlan_OnClick);
		
		cmdRunPlan = new Button(cmpHeader, SWT.PUSH);
		cmdRunPlan.setText(MM.PHRASES.getPhrase("336"));
		cmdRunPlan.setEnabled(false);
		cmdRunPlan.addSelectionListener(cmdRunPlan_OnClick);
		
		cmdAddEvent = new Button(cmpHeader, SWT.PUSH);
		cmdAddEvent.setText(MM.PHRASES.getPhrase("346"));
		cmdAddEvent.setEnabled(false);
		cmdAddEvent.addSelectionListener(cmdAddEvent_OnClick);

		cmdRemoveEvent = new Button(cmpHeader, SWT.PUSH);
		cmdRemoveEvent.setText(MM.PHRASES.getPhrase("353"));
		cmdRemoveEvent.setEnabled(false);
		cmdRemoveEvent.addSelectionListener(cmdRemoveEvent_OnClick);
		
		lblPlanName = new Label(cmpHeader, SWT.NONE); 
		lblPlanName.setText(" ");
		lblStartAmount = new Label(cmpHeader, SWT.NONE);
		lblStartAmount.setText(" ");
		lblStartAge = new Label(cmpHeader, SWT.NONE);
		lblStartAge.setText(" ");
	}

	protected void LoadLayout() {
		this.setLayout(new FormLayout());

		FormData lstplansdata = new FormData();
		lstplansdata.top = new FormAttachment(0, 32);
		lstplansdata.left = new FormAttachment(0, 5);
		lstplansdata.right = new FormAttachment(0, 260);
		lstplansdata.bottom = new FormAttachment(100, -10);
		lstPlans.setLayoutData(lstplansdata);

		FormData cmpheaderdata = new FormData();
		cmpheaderdata.top = new FormAttachment(0, 32);
		cmpheaderdata.left = new FormAttachment(lstPlans, 10);
		cmpheaderdata.right = new FormAttachment(100, -10);
		cmpheaderdata.bottom = new FormAttachment(0, 125);
		cmpHeader.setLayoutData(cmpheaderdata);

		FormData cmdrunplandata = new FormData();
		cmdrunplandata.top = new FormAttachment(0, 10);
		cmdrunplandata.left = new FormAttachment(0, 10);
		cmdRunPlan.setLayoutData(cmdrunplandata);
		
		FormData cmdremoveplandata = new FormData();
		cmdremoveplandata.top = new FormAttachment(0, 10);
		cmdremoveplandata.left = new FormAttachment(cmdRunPlan, 10);
		cmdRemovePlan.setLayoutData(cmdremoveplandata);

		FormData cmdaddeventdata = new FormData();
		cmdaddeventdata.top = new FormAttachment(cmdRunPlan, 5);
		cmdaddeventdata.left = new FormAttachment(0, 10);
		cmdAddEvent.setLayoutData(cmdaddeventdata);
		
		FormData cmdremoveeventdata = new FormData();
		cmdremoveeventdata.top = new FormAttachment(cmdRunPlan, 5);
		cmdremoveeventdata.left = new FormAttachment(cmdAddEvent, 5);
		cmdRemoveEvent.setLayoutData(cmdremoveeventdata);
		
		FormData tblplandetaildata = new FormData();
		tblplandetaildata.top = new FormAttachment(cmpHeader, 10);
		tblplandetaildata.left = new FormAttachment(lstPlans, 10);
		tblplandetaildata.right = new FormAttachment(100, -10);
		tblplandetaildata.bottom = new FormAttachment(100, -10);
		tblPlanDetail.setLayoutData(tblplandetaildata);

		FormData lblplannamedata = new FormData();
		lblplannamedata.top = new FormAttachment(0, 15);
		lblplannamedata.left = new FormAttachment(cmdRemovePlan, 20);
		lblplannamedata.right = new FormAttachment(cmdRemovePlan, 280);
		lblPlanName.setLayoutData(lblplannamedata);
		
		FormData lblstartamountdata = new FormData();
		lblstartamountdata.top = new FormAttachment(0, 15);
		lblstartamountdata.left = new FormAttachment(lblPlanName, 10);
		lblstartamountdata.right = new FormAttachment(lblPlanName, 400);
		lblStartAmount.setLayoutData(lblstartamountdata);
		
		FormData lblstartagetdata = new FormData();
		lblstartagetdata.top = new FormAttachment(lblPlanName, 10);
		lblstartagetdata.left = new FormAttachment(cmdRemovePlan, 20);
		lblstartagetdata.right = new FormAttachment(cmdRemovePlan, 300);
		lblStartAge.setLayoutData(lblstartagetdata);
		
		FormData tbmaindata = new FormData();
		tbmaindata.right = new FormAttachment(100, 0);
		tbMain.setLayoutDat(tbmaindata);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void Refresh() {
		super.Refresh();
		LoadPlans();
	}

	/*
	 * Helpers
	 */
	
	private void LoadEvents() {
		
		TableItem ti = null;
		String eventValueType = null;
		java.util.List<PlanEvent> events = MM.sqlMap.selectList("getPlanEventsById", _plan.getPlanID());
		
		tblPlanDetail.removeAll();
		
		if (events != null && events.size() >0) {
			
			for (PlanEvent event : events ) {
				
				ti = new TableItem(tblPlanDetail, SWT.NONE);
				ti.setText(0,  event.getEventName());
				ti.setText(1, event.getEventTypeName());
				ti.setText(2, formatter.getAmountFormatted(event.getEventValue()));
				
				if (event.getEventValueType() == 0) 
					eventValueType = MM.PHRASES.getPhrase("352");
				else 
					eventValueType = MM.PHRASES.getPhrase("351");
				
				ti.setText(3, eventValueType);
				
				ti.setText(4,  Integer.toString(event.getStartAge()));
				ti.setText(5,  Integer.toString(event.getEndAge()));
			}
			
		}
		
		cmdRemoveEvent.setEnabled(false);
		
	}
	
	private void LoadColumns() {

		TableColumn tc1 = new TableColumn(tblPlanDetail, SWT.LEFT);
		TableColumn tc2 = new TableColumn(tblPlanDetail, SWT.LEFT);
		TableColumn tc3 = new TableColumn(tblPlanDetail, SWT.LEFT);
		TableColumn tc4 = new TableColumn(tblPlanDetail, SWT.LEFT);
		TableColumn tc5 = new TableColumn(tblPlanDetail, SWT.LEFT);
		TableColumn tc6 = new TableColumn(tblPlanDetail, SWT.LEFT);	

		tc1.setText(MM.PHRASES.getPhrase("347"));
		tc2.setText(MM.PHRASES.getPhrase("348"));
		tc3.setText(MM.PHRASES.getPhrase("349"));
		tc4.setText(MM.PHRASES.getPhrase("350"));
		tc5.setText(MM.PHRASES.getPhrase("339"));
		tc6.setText(MM.PHRASES.getPhrase("354"));

		tc1.setWidth(150);
		tc2.setWidth(100);
		tc3.setWidth(100);
		tc4.setWidth(150);
		tc5.setWidth(100);
		tc6.setWidth(100);
		
		tblPlanDetail.setHeaderVisible(true);
		
	}@SuppressWarnings("rawtypes")
	private void LoadPlans() {
		
		lstPlans.removeAll();
		
		java.util.List plans = (java.util.List)MM.sqlMap.selectList("getPlans", null);
		if (plans != null && plans.size() >0) {
			for (Object p : plans) {
				
				Plan plan = (Plan) p;
				lstPlans.add(plan.getPlanName());
				lstPlans.setData(plan.getPlanName(), p);
			}
		}
	}

	/*
	 * Listeners
	 */

	SelectionAdapter lstPlans_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			_plan = (Plan) MM.sqlMap.selectOne("getPlanByName", lstPlans.getSelection()[0]);
			InfinityPfm.LogMessage("Plan " + _plan.getPlanName());
			lblPlanName.setText(MM.PHRASES.getPhrase("337") + ": " + _plan.getPlanName());
			lblStartAmount.setText(MM.PHRASES.getPhrase("338")  + ": " + formatter.getAmountFormatted(_plan.getStartBalance()));
			lblStartAge.setText(MM.PHRASES.getPhrase("339") + ": " +  Integer.toString(_plan.getStartAge()));
			cmdRunPlan.setEnabled(true);
			cmdRemovePlan.setEnabled(true);
			cmdAddEvent.setEnabled(true);
			LoadEvents();
		}
		
	};
	
	SelectionAdapter cmdRemovePlan_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			
			MessageDialog dlg = new MessageDialog(MM.DIALOG_QUESTION, MM.APPTITLE,
					String.format(MM.PHRASES.getPhrase("355"), _plan.getPlanName()));
			
			dlg.setDimensions(400, 150);
			int iResult = dlg.Open();

			if (iResult == MM.YES) {
				MM.sqlMap.delete("deletePlanEvents", _plan.getPlanID());
				MM.sqlMap.delete("deletePlan", _plan.getPlanName());
				lblPlanName.setText("");
				lblStartAmount.setText("");
				lblStartAge.setText("");
				tblPlanDetail.removeAll();
				cmdRemovePlan.setEnabled(false);
				cmdRunPlan.setEnabled(false);
				cmdAddEvent.setEnabled(false);
				LoadPlans();
			}
		}
	};
	
	SelectionAdapter cmdRunPlan_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			PlanRunner runner = new PlanRunner();
			runner.run(_plan.getPlanID(), MM.sqlMap);
		}
	};
		
	SelectionAdapter cmdAddEvent_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			NewPlanEventDialog dialog = new NewPlanEventDialog(MM.PHRASES.getPhrase("346"), _plan);
			dialog.Open();
			
			if (dialog.getResult() != null) {
				
				PlanEvent event = dialog.getResult();
				
				InfinityPfm.LogMessage(event.getEventName());
				
			}
			LoadEvents();
		}
	};
	
	SelectionAdapter cmdRemoveEvent_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			MM.sqlMap.delete("deletePlanEventByIdAndName", _planEvent);
			LoadEvents();
		}
		};
	
	SelectionAdapter tblPlanDetail_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
	
			TableItem item = (TableItem) event.item;
			
			PlanEvent arg = new PlanEvent();
			arg.setEventName(item.getText(0));
			arg.setPlanID(_plan.getPlanID());
			
			_planEvent = MM.sqlMap.selectOne("getPlanEventsByIdAndName", arg);
			
			cmdRemoveEvent.setEnabled(true);
			
		}
	};
}
