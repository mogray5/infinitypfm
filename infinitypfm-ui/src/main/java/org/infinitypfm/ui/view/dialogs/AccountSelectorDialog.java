/*
 * Copyright (c) 2005-2018 Wayne Gray All rights reserved
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
import org.infinitypfm.core.data.Account;

public class AccountSelectorDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	private Label lblAccount = null;
	private Combo cmbAccount = null;
	private Button cmdSelect = null;
	private Button cmdCancel = null;
	private String accountName = null;

	public boolean includeIncomeAccounts = false;
	
	public AccountSelectorDialog() {
		super();
	}

	protected void LoadUI(Shell sh) {

		lblAccount = new Label(sh, SWT.NONE);
		lblAccount.setText(MM.PHRASES.getPhrase("93"));
		cmdSelect = new Button(sh, SWT.PUSH);
		cmdSelect.setText(MM.PHRASES.getPhrase("5"));
		cmdSelect.addSelectionListener(cmdSelect_OnClick);
		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);

		cmbAccount = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);

		// TODO: Create a UI utility class to handle common tasks such as below
		// load of accounts into a combo

		try {
			Account act = null;
			@SuppressWarnings("rawtypes")
			java.util.List list = MM.sqlMap.selectList("getAccountsForType",
					MM.ACT_TYPE_EXPENSE);

			cmbAccount.removeAll();

			for (int i = 0; i < list.size(); i++) {
				act = (Account) list.get(i);
				cmbAccount.add(act.getActName());

			}
			list = MM.sqlMap.selectList("getAccountsForType",
					MM.ACT_TYPE_LIABILITY);

			for (int i = 0; i < list.size(); i++) {
				act = (Account) list.get(i);
				cmbAccount.add(act.getActName());

			}

			if (includeIncomeAccounts){
				
				list = MM.sqlMap.selectList("getAccountsForType",
						MM.ACT_TYPE_INCOME);

				for (int i = 0; i < list.size(); i++) {
					act = (Account) list.get(i);
					cmbAccount.add(act.getActName());

				}
				
			}
			
			cmbAccount.select(0);

		} catch (Exception se) {
			InfinityPfm.LogMessage(se.getMessage());
		}

	}

	protected void LoadLayout() {
		FormData lblaccountdata = new FormData();
		lblaccountdata.top = new FormAttachment(0, 40);
		lblaccountdata.left = new FormAttachment(20, 0);
		lblAccount.setLayoutData(lblaccountdata);

		FormData cmbaccountdata = new FormData();
		cmbaccountdata.top = new FormAttachment(0, 40);
		cmbaccountdata.left = new FormAttachment(lblAccount, 10);
		cmbaccountdata.right = new FormAttachment(100, -30);
		cmbAccount.setLayoutData(cmbaccountdata);

		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(lblAccount, 40);
		cmdcanceldata.right = new FormAttachment(40, 10);
		cmdCancel.setLayoutData(cmdcanceldata);

		FormData cmdadddata = new FormData();
		cmdadddata.top = new FormAttachment(lblAccount, 40);
		cmdadddata.left = new FormAttachment(cmdCancel, 10);
		cmdadddata.right = new FormAttachment(60, 10);
		cmdSelect.setLayoutData(cmdadddata);
	}

	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("139"));
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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public void setIncludeIncomeAccounts(boolean include){
		includeIncomeAccounts = include;
	}

	/*
	 * Listeners
	 */
	SelectionAdapter cmdSelect_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			accountName = cmbAccount.getText();
			shell.dispose();
		}
	};

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			userCancelled = true;
			shell.dispose();
		}
	};

}
