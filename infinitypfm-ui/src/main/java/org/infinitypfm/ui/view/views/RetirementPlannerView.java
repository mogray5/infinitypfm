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
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.Plan;
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
	private Table tblPlanDetail = null;
	private Label lblPlanName = null; 
	private Label lblStartAmount = null;
	private Label lblStartAge = null; 
	
	public RetirementPlannerView(Composite arg0, int arg1) {
		super(arg0, arg1);
		
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		
		// init console
		LoadUI();
		LoadLayout();
		LoadPlans();
	}

	protected void LoadUI() {
		tbMain = new RetirementPlannerToolbar(this);
		lstPlans = new List (this, SWT.BORDER | SWT.V_SCROLL);
		lstPlans.addSelectionListener(lstPlans_OnClick);
		tblPlanDetail = new Table(this, SWT.BORDER);
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
		cmpheaderdata.bottom = new FormAttachment(0, 120);
		cmpHeader.setLayoutData(cmpheaderdata);

		FormData cmdrunplandata = new FormData();
		cmdrunplandata.top = new FormAttachment(0, 6);
		cmdrunplandata.left = new FormAttachment(0, 10);
		cmdRunPlan.setLayoutData(cmdrunplandata);
		
		FormData cmdremoveplandata = new FormData();
		cmdremoveplandata.top = new FormAttachment(0, 6);
		cmdremoveplandata.left = new FormAttachment(cmdRunPlan, 10);
		cmdRemovePlan.setLayoutData(cmdremoveplandata);
		
		FormData tblplandetaildata = new FormData();
		tblplandetaildata.top = new FormAttachment(cmpHeader, 10);
		tblplandetaildata.left = new FormAttachment(lstPlans, 10);
		tblplandetaildata.right = new FormAttachment(100, -10);
		tblplandetaildata.bottom = new FormAttachment(100, -10);
		tblPlanDetail.setLayoutData(tblplandetaildata);

		FormData lblplannamedata = new FormData();
		lblplannamedata.top = new FormAttachment(0, 10);
		lblplannamedata.left = new FormAttachment(cmdRemovePlan, 10);
		lblplannamedata.right = new FormAttachment(cmdRemovePlan, 280);
		lblPlanName.setLayoutData(lblplannamedata);
		
		FormData lblstartamountdata = new FormData();
		lblstartamountdata.top = new FormAttachment(0, 10);
		lblstartamountdata.left = new FormAttachment(lblPlanName, 10);
		lblstartamountdata.right = new FormAttachment(lblPlanName, 400);
		lblStartAmount.setLayoutData(lblstartamountdata);
		
		FormData lblstartagetdata = new FormData();
		lblstartagetdata.top = new FormAttachment(lblPlanName, 10);
		lblstartagetdata.left = new FormAttachment(cmdRemovePlan, 10);
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
	
	private void InitTable() {
		
	}
	
	@SuppressWarnings("rawtypes")
	private void LoadPlans() {
		
		lstPlans.removeAll();
		
		java.util.List plans = (java.util.List)MM.sqlMap.selectList("getPlans", null);
		if (plans != null && plans.size() >0) {
			for (Object p : plans) {
				
				Plan plan = (Plan) p;
				lstPlans.add(plan.getPlanName());
			}
		}
		 
	}

	/*
	 * Listeners
	 */

	SelectionAdapter lstPlans_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			Plan plan = (Plan) MM.sqlMap.selectOne("getPlanByName", lstPlans.getSelection()[0]);
			InfinityPfm.LogMessage("Plan " + plan.getPlanName());
			lblPlanName.setText(MM.PHRASES.getPhrase("337") + ": " + plan.getPlanName());
			lblStartAmount.setText(MM.PHRASES.getPhrase("338")  + ": " + formatter.getAmountFormatted(plan.getStartBalance()));
			lblStartAge.setText(MM.PHRASES.getPhrase("339") + ": " +  Integer.toString(plan.getStartAge()));
			cmdRunPlan.setEnabled(true);
			cmdRemovePlan.setEnabled(true);
		}
		
	};
	
	SelectionAdapter cmdRemovePlan_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			MM.sqlMap.delete("deletePlan", lstPlans.getSelection()[0]);
			lblPlanName.setText("");
			lblStartAmount.setText("");
			lblStartAge.setText("");
			tblPlanDetail.removeAll();
			cmdRemovePlan.setEnabled(false);
			cmdRunPlan.setEnabled(false);
			LoadPlans();
		}
	};
	
	SelectionAdapter cmdRunPlan_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		}
	};
		
		
}
