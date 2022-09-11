/*
 * Copyright (c) 2005-2020 Wayne Gray All rights reserved
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.DataFormatUtil;

/**
 * Pop-up Dialog for adding new accounts.
 * 
 * @author Wayne Gray
 */
public class NewAccountDialog extends BaseDialog {

	private Label lblActName = null;
	private Label lblActId = null;
	private Label lblActType = null;
	private Label lblBeginBalance = null;
	private Label lblCurrency = null;
	private Text txtActName = null;
	private Text txtActId = null;
	private Combo cmbCurrency = null;
	private Combo cmbActType = null;
	private Text txtBeginBalance = null;
	private Button cmdSave = null;
	private Button cmdCancel = null;
	private Account account = null;
	
	private DataFormatUtil formatter = null;

	public NewAccountDialog() {
		super();
	}

	protected void LoadUI(Shell sh) {
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		lblActName = new Label(sh, SWT.NONE);
		lblActId = new Label(sh, SWT.NONE);
		lblActType = new Label(sh, SWT.NONE);
		lblBeginBalance = new Label(sh, SWT.NONE);
		lblCurrency = new Label(sh, SWT.NONE);

		txtActName = new Text(sh, SWT.BORDER);
		txtActId = new Text(sh, SWT.BORDER);
		cmbActType = new Combo(sh, SWT.BORDER);
		cmbCurrency = new Combo(sh, SWT.BORDER);
		txtBeginBalance = new Text(sh, SWT.BORDER);
		cmdSave = new Button(sh, SWT.PUSH);
		cmdCancel = new Button(sh, SWT.PUSH);

		lblActName.setText(MM.PHRASES.getPhrase("1"));
		lblActId.setText("ID");
		lblActType.setText(MM.PHRASES.getPhrase("56"));
		lblBeginBalance.setText(MM.PHRASES.getPhrase("2"));
		lblCurrency.setText(MM.PHRASES.getPhrase("207"));

		cmdSave.setText(MM.PHRASES.getPhrase("38"));
		cmdSave.addSelectionListener(cmdSave_OnClick);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);
		txtBeginBalance.setText("0");
		
		try {
			@SuppressWarnings("rawtypes")
			java.util.List list = MM.sqlMap.selectList("getAccountsTypes",
					null);

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					cmbActType.add((String) list.get(i));
				}

				cmbActType.setText((String) list.get(0));
				
			}

		} catch (Exception se) {
			InfinityPfm.LogMessage(se.getMessage());
		}

		Currency curr = null;

		try {
			@SuppressWarnings("rawtypes")
			java.util.List list = MM.sqlMap.selectList("getCurrencies", null);

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					curr = (Currency) list.get(i);
					cmbCurrency.add(curr.getIsoName());
					if (curr.getCurrencyID() == MM.options.getDefaultCurrencyID()){
						cmbCurrency.select(i);
					}
				}

			}

		} catch (Exception se) {
			InfinityPfm.LogMessage(se.getMessage());
		}
		
		// Load account data and mark everything but account name read only if we have an account.
		if (account != null) {
			txtBeginBalance.setText(formatter.getAmountFormatted(account.getActBalance()));
			txtActName.setText(account.getActName());
			if (account.getActNumber() != null )
				txtActId.setText(account.getActNumber());
			cmbActType.setText(account.getActTypeName());
			cmbCurrency.setText(account.getCurrencyName());

			txtBeginBalance.setEnabled(false);
			txtActId.setEnabled(false);
			cmbActType.setEnabled(false);
			cmbCurrency.setEnabled(false);
		}
		
		// Set tab order
		sh.setTabList(new Control[] {txtActName, txtActId, cmbActType, txtBeginBalance, cmbCurrency, cmdSave, cmdCancel});

	}

	protected void LoadLayout() {
		FormData lblactnamedata = new FormData();
		lblactnamedata.top = new FormAttachment(0, 20);
		lblactnamedata.left = new FormAttachment(0, 40);
		lblActName.setLayoutData(lblactnamedata);

		FormData txtactnamedata = new FormData();
		txtactnamedata.top = new FormAttachment(0, 20);
		txtactnamedata.left = new FormAttachment(lblActName, 80);
		txtactnamedata.right = new FormAttachment(100, -40);
		txtActName.setLayoutData(txtactnamedata);

		FormData lblactiddata = new FormData();
		lblactiddata.top = new FormAttachment(lblActName, 25);
		lblactiddata.left = new FormAttachment(0, 40);
		lblActId.setLayoutData(lblactiddata);

		FormData txtactiddata = new FormData();
		txtactiddata.top = new FormAttachment(lblActName, 25);
		txtactiddata.left = new FormAttachment(lblActName, 80);
		txtactiddata.right = new FormAttachment(100, -120);
		txtActId.setLayoutData(txtactiddata);

		FormData lblacttypedata = new FormData();
		lblacttypedata.top = new FormAttachment(lblActId, 35);
		lblacttypedata.left = new FormAttachment(0, 40);
		lblActType.setLayoutData(lblacttypedata);

		FormData txtacttypedata = new FormData();
		txtacttypedata.top = new FormAttachment(lblActId, 30);
		txtacttypedata.left = new FormAttachment(lblActName, 80);
		cmbActType.setLayoutData(txtacttypedata);

		FormData lblbeginbalancedata = new FormData();
		lblbeginbalancedata.top = new FormAttachment(lblActType, 25);
		lblbeginbalancedata.left = new FormAttachment(0, 40);
		lblBeginBalance.setLayoutData(lblbeginbalancedata);

		FormData txtbeginbalancedata = new FormData();
		txtbeginbalancedata.top = new FormAttachment(lblActType, 25);
		txtbeginbalancedata.left = new FormAttachment(lblActName, 80);
		txtbeginbalancedata.right = new FormAttachment(100, -200);
		txtBeginBalance.setLayoutData(txtbeginbalancedata);

		FormData lblcurrencydata = new FormData();
		lblcurrencydata.top = new FormAttachment(lblBeginBalance, 25);
		lblcurrencydata.left = new FormAttachment(0, 40);
		// lblcurrencydata.right = new FormAttachment(100, -200);
		lblCurrency.setLayoutData(lblcurrencydata);

		FormData cmbcurrencydata = new FormData();
		cmbcurrencydata.top = new FormAttachment(lblBeginBalance, 25);
		cmbcurrencydata.left = new FormAttachment(lblActName, 80);
		cmbcurrencydata.right = new FormAttachment(100, -260);
		cmbCurrency.setLayoutData(cmbcurrencydata);

		FormData cmdsavedata = new FormData();
		cmdsavedata.top = new FormAttachment(cmbCurrency, 20);
		cmdsavedata.left = new FormAttachment(0, 95);
		cmdsavedata.right = new FormAttachment(100, -255);
		cmdSave.setLayoutData(cmdsavedata);
				
		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(cmbCurrency, 20);
		cmdcanceldata.left = new FormAttachment(cmdSave, 5);
		cmdcanceldata.right = new FormAttachment(100, -115);
		cmdCancel.setLayoutData(cmdcanceldata);


	}

	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("82") + " " + MM.APPTITLE);

		shell.setSize(500, 350);
		this.CenterWindow();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return 1;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	/*
	 * Listeners
	 */

	SelectionAdapter cmdSave_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			MainAction action = new MainAction();
			boolean doDispose = true;
			
			if (account != null && account.getActId() > 0){
				
				// Edit
				
				account.setActName(txtActName.getText());
				doDispose = action.EditAccount(account);
				
			} else {
				
				// Add
				account = new Account();
				account.setActBalance(DataFormatUtil.moneyToLong(txtBeginBalance
						.getText()));
				account.setActName(txtActName.getText());
				account.setActNumber(txtActId.getText());
				account.setActTypeName(cmbActType.getText());

				try {
					Currency curr = (Currency) MM.sqlMap.selectOne(
							"getCurrencyByIsoCode", cmbCurrency.getText());
					account.setCurrencyID(curr.getCurrencyID());
				} catch (Exception e1) {
					InfinityPfm.LogMessage(e1.getMessage());
				}
				
				action.AddAccount(account);
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
