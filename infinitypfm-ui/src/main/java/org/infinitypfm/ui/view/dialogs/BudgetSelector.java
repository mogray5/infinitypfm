/*
 * Copyright (c) 2005-2013 Wayne Gray All rights reserved
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

import java.sql.SQLException;

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
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Budget;

public class BudgetSelector extends BaseDialog {

	/*
	 * Widgets
	 */
	private Label lblBudget = null;
	private Combo cmbBudget = null;
	private Button cmdSelect = null;
	private Button cmdCancel = null;
	private String budgetName = null;
	
	
	@Override
	protected void LoadUI(Shell sh) {
		lblBudget = new Label(sh, SWT.NONE);
		lblBudget.setText(MM.PHRASES.getPhrase("97"));
		cmdSelect = new Button(sh, SWT.PUSH);
		cmdSelect.setText(MM.PHRASES.getPhrase("5"));
		cmdSelect.addSelectionListener(cmdSelect_OnClick);
		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);
		
		cmbBudget = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);
	
		//TODO: Create a UI utility class to handle common tasks such as below
		//        load of accounts into a combo
		
		try {
			Budget budget = null;
			@SuppressWarnings("rawtypes")
			java.util.List list = MM.sqlMap.queryForList("getAllBudgets", null);
			
			cmbBudget.removeAll();
			
			for (int i=0; i<list.size(); i++) {
				budget = (Budget)list.get(i);
				cmbBudget.add(budget.getBudgetName());
				
			}			
			
			cmbBudget.select(0);
			
		} catch (SQLException se){
			InfinityPfm.LogMessage(se.getMessage());
		}

	}

	@Override
	protected void LoadLayout() {
		FormData lblaccountdata = new FormData();
		lblaccountdata.top = new FormAttachment(0, 40);
		lblaccountdata.left = new FormAttachment(20, 0);
		lblBudget.setLayoutData(lblaccountdata);
		
		FormData cmbaccountdata = new FormData();
		cmbaccountdata.top = new FormAttachment(0, 40);
		cmbaccountdata.left = new FormAttachment(lblBudget, 10);
		cmbaccountdata.right = new FormAttachment(100, -30);
		cmbBudget.setLayoutData(cmbaccountdata);
		
		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(lblBudget, 40);
		//cmdcanceldata.left = new FormAttachment(cmdOne, 10);
		cmdcanceldata.right = new FormAttachment(40,10);
		cmdCancel.setLayoutData(cmdcanceldata);

		FormData cmdadddata = new FormData();
		cmdadddata.top = new FormAttachment(lblBudget, 40);
		cmdadddata.left = new FormAttachment(cmdCancel, 10);
		cmdadddata.right = new FormAttachment(60,10);
		cmdSelect.setLayoutData(cmdadddata);

	}
	
	
	
	@Override
	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("165"));
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



	public String getBudgetName() {
		return budgetName;
	}

	public void setBudgetName(String budgetName) {
		this.budgetName = budgetName;
	}



	/*
	 * Listeners
	 */
	SelectionAdapter cmdSelect_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			budgetName = cmbBudget.getText();
			shell.dispose();
		}
	};

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			userCancelled = true;
			shell.dispose();
		}
	};

}
