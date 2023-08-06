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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
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
		tblPlanDetail = new Table(this, SWT.BORDER);
		cmpHeader = new Composite(this, SWT.BORDER);
		cmpHeader.setLayout(new FormLayout());
		cmdRemovePlan = new Button(cmpHeader, SWT.PUSH);
		cmdRemovePlan.setText(MM.PHRASES.getPhrase("335"));
		cmdRemovePlan.setEnabled(false);
		
		cmdRunPlan = new Button(cmpHeader, SWT.PUSH);
		cmdRunPlan.setText(MM.PHRASES.getPhrase("336"));
		cmdRunPlan.setEnabled(false);
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
		cmpheaderdata.bottom = new FormAttachment(0, 80);
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


		
		FormData tbmaindata = new FormData();
		tbmaindata.right = new FormAttachment(100, 0);
		tbMain.setLayoutDat(tbmaindata);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void Refresh() {
		super.Refresh();
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


}
