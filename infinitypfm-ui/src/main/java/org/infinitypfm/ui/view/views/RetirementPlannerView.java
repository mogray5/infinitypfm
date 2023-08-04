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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.ui.view.toolbars.RetirementPlannerToolbar;

/**
 * Dialog to allow transaction import and setting of offsets before import.
 * 
 */
public class RetirementPlannerView extends BaseView {

	private DataFormatUtil formatter = null;
	private RetirementPlannerToolbar tbMain = null;
	
	private List lstPlans = null;
	private Label lblPlans = null;
	private Composite cmpHeader = null;
	private Button cmdDelPlan = null;
	private Button cmdAddPlan = null;
	
	public RetirementPlannerView(Composite arg0, int arg1) {
		super(arg0, arg1);
		
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		
		// init console
		LoadUI();
		LoadLayout();
	}

	protected void LoadUI() {
		tbMain = new RetirementPlannerToolbar(this);
		lstPlans = new List (this, SWT.BORDER | SWT.V_SCROLL);
		lblPlans = new Label(this, SWT.NONE);
		lblPlans.setText(MM.PHRASES.getPhrase("333"));
		cmdAddPlan = new Button(this, SWT.PUSH);
		cmdAddPlan.setText(MM.PHRASES.getPhrase("45"));
		cmdDelPlan = new Button(this, SWT.PUSH);
		cmdDelPlan.setText(MM.PHRASES.getPhrase("6"));
		
		
	}

	protected void LoadLayout() {
		this.setLayout(new FormLayout());

		FormData lblplansdata = new FormData();
		lblplansdata.top = new FormAttachment(0, 20);
		lblplansdata.left = new FormAttachment(0, 50);
		lblPlans.setLayoutData(lblplansdata);
		
		FormData cmdaddplandata = new FormData();
		cmdaddplandata.top = new FormAttachment(0, 10);
		cmdaddplandata.left = new FormAttachment(lblPlans, 5);
		cmdAddPlan.setLayoutData(cmdaddplandata);
		
		FormData cmddelplandata = new FormData();
		cmddelplandata.top = new FormAttachment(0, 10);
		cmddelplandata.left = new FormAttachment(cmdAddPlan, 5);
		cmdDelPlan.setLayoutData(cmddelplandata);
		
		FormData lstplansdata = new FormData();
		lstplansdata.top = new FormAttachment(lblPlans, 10);
		lstplansdata.left = new FormAttachment(0, 5);
		lstplansdata.right = new FormAttachment(0, 260);
		lstplansdata.bottom = new FormAttachment(100, -10);
		lstPlans.setLayoutData(lstplansdata);
		
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
	 * Listeners
	 */


}
