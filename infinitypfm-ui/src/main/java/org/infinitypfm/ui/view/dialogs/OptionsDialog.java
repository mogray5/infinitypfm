/*
 * Copyright (c) 2005-2021 Wayne Gray All rights reserved
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.CurrencyMethod;
import org.infinitypfm.core.data.Options;
import org.infinitypfm.core.data.Password;
import org.infinitypfm.core.util.EncryptUtil;
 
/**
 * Configure application options.  Changes are persisted to
 * the database.
 */
public class OptionsDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	private Label lblPrecision = null;
	private Label lblBaseCurrency = null;
	private Label lblSpendingPassword = null;
	private Label lblEmail = null;
	private Label lblBookmarks = null;
	private Text txtPrecision = null;
	private Text txtSpendingPassword = null;
	private Text txtEmail = null;
	private Text txtBookmarks = null;
	
	private Combo cmbBaseCurrency = null;
	private Button cmdOk = null;
	private Button cmdCancel = null;
	private Button chkReportsInBrowswer;
	private Button chkDefaultOpenConsole;
	private Button chkEnableWallet;
	private Scale scale = null;
	private TabFolder tabFolder = null;
	
	private EncryptUtil _encryptUtil = null; 
	private Password _walletPassword = null;
	private Label lblBsvRefresh = null;
	private Combo cmbBsvRefresh = null;
	
	private boolean walletSettingsChanged = false;
	
	public OptionsDialog() {
		super();
		_encryptUtil = new EncryptUtil();
	}

	protected void LoadUI(Shell sh) {

		tabFolder = new TabFolder(shell, SWT.BORDER);
		TabItem reportItem = new TabItem(tabFolder, SWT.NONE);
		TabItem currencyItem = new TabItem(tabFolder, SWT.NONE);
		
		Group currencyGroup = new Group(tabFolder, SWT.NONE);
		Group reportGroup = new Group(tabFolder, SWT.NONE);
		
		currencyGroup.setLayout(new FormLayout());
		reportGroup.setLayout(new FormLayout());

		currencyItem.setText(MM.PHRASES.getPhrase("224"));
		reportItem.setText(MM.PHRASES.getPhrase("250"));

		lblPrecision = new Label(currencyGroup, SWT.NONE);
		lblPrecision.setText(MM.PHRASES.getPhrase("225") + ":");

		lblBaseCurrency = new Label(currencyGroup, SWT.NONE);
		lblBaseCurrency.setText(MM.PHRASES.getPhrase("207") + ":");

		Options options = null;

		try {
			options = (Options) MM.sqlMap.selectOne("getOptions", null);
		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

		scale = new Scale(currencyGroup, SWT.BORDER);
		scale.setMaximum(8);
		scale.setPageIncrement(1);

		if (options != null) {
			scale.setSelection(options.getCurrencyPrecision());
		} else {
			scale.setSelection(2);
		}

		scale.addSelectionListener(scale_onGesture);

		txtPrecision = new Text(currencyGroup, SWT.BORDER);
		txtPrecision.setEnabled(false);
		txtPrecision.setText(Integer.toString(scale.getSelection()));

		cmbBaseCurrency = new Combo(currencyGroup, SWT.BORDER | SWT.READ_ONLY);
		try {

			String defaultCurrency = null;
			@SuppressWarnings("rawtypes")
			List lstCurrencies = MM.sqlMap.selectList("getCurrencies");
			if (lstCurrencies != null) {

				for (int i = 0; i < lstCurrencies.size(); i++) {

					Currency currency = (Currency) lstCurrencies.get(i);
					cmbBaseCurrency.add(currency.getCurrencyName());
					if (currency.getCurrencyID() == options
							.getDefaultCurrencyID()) {
						defaultCurrency = currency.getCurrencyName();
					}

				}

				if (defaultCurrency != null)
					cmbBaseCurrency.setText(defaultCurrency);

			}

		} catch (Exception e) {
			InfinityPfm.LogMessage(e.toString());
		}

		chkReportsInBrowswer = new Button(reportGroup, SWT.CHECK);
		chkReportsInBrowswer.setText(MM.PHRASES.getPhrase("249"));
		
		if (options != null)
			chkReportsInBrowswer.setSelection(options.isReportsInBrowswer());
		
		chkDefaultOpenConsole = new Button(reportGroup, SWT.CHECK);
		chkDefaultOpenConsole.setText(MM.PHRASES.getPhrase("251"));
		chkDefaultOpenConsole.setSelection(options.isConsoleDefaultOpen());
		
		chkEnableWallet = new Button(reportGroup, SWT.CHECK);
		chkEnableWallet.setText(MM.PHRASES.getPhrase("283"));
		chkEnableWallet.setSelection(options.isEnableWallet());
		chkEnableWallet.addSelectionListener(chkEnableWallet_OnClick);
		
		lblSpendingPassword = new Label(reportGroup, SWT.NONE);
		lblSpendingPassword.setText(MM.PHRASES.getPhrase("284") + ":");
		
		txtSpendingPassword = new Text(reportGroup, SWT.BORDER);
		txtSpendingPassword.setEnabled(options.isEnableWallet());		
		txtSpendingPassword.setEchoChar('*');
		txtSpendingPassword.addModifyListener(dcFieldsModified);
		
		lblEmail = new Label(reportGroup, SWT.NONE);
		lblEmail.setText(MM.PHRASES.getPhrase("304") + ":");
		txtEmail = new Text(reportGroup, SWT.BORDER);
		txtEmail.setEnabled(options.isEnableWallet());
		txtEmail.addModifyListener(dcFieldsModified);

		lblBsvRefresh = new Label(reportGroup, SWT.NONE);
		lblBsvRefresh.setText(MM.PHRASES.getPhrase("216") + ":");
		cmbBsvRefresh = new Combo(reportGroup, SWT.BORDER | SWT.READ_ONLY);
		
		if (options.isEnableWallet()) {
			_walletPassword = new Password(null, options.getSpendPassword(), _encryptUtil);
		
			// Need to set password to something initially so the box visually shows as populated
			if (_walletPassword.getHashedPassword() != null && _walletPassword.getHashedPassword().length()>0)
				txtSpendingPassword.setText(_walletPassword.getHashedPassword());

			txtEmail.setText(options.getEmailAddress());
			
			cmbBsvRefresh.setText(options.getDefaultBsvCurrencyMethod());
			
			InfinityPfm.LogMessage("Password starting hash is = " + _walletPassword.getHashedPassword());
			
			loadBsvMethods();
			
		}
		
		txtSpendingPassword.addFocusListener(txtSpendingPassword_OnLostFocus);

		lblBookmarks = new Label(reportGroup, SWT.NONE);
		lblBookmarks.setText(MM.PHRASES.getPhrase("317"));
		
		txtBookmarks = new Text(reportGroup, SWT.BORDER);
		if (options.getBookmarksUrl() != null)
			txtBookmarks.setText(options.getBookmarksUrl());
		
		currencyItem.setControl(currencyGroup);
		reportItem.setControl(reportGroup);
		
		cmdOk = new Button(sh, SWT.PUSH);
		cmdOk.setText(MM.PHRASES.getPhrase("5"));
		cmdOk.addSelectionListener(cmdOk_OnClick);
		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);

		// wallet changed flag may have been toggled during initial load
		// set it back to false
		walletSettingsChanged = false;
		
	}

	protected void LoadLayout() {

		FormData tabfolderdata = new FormData();
		tabfolderdata.top = new FormAttachment(0, 10);
		tabfolderdata.left = new FormAttachment(0, 10);
		tabfolderdata.right = new FormAttachment(100, -10);
		tabfolderdata.bottom = new FormAttachment(100, -40);
		tabFolder.setLayoutData(tabfolderdata);

		FormData lblprecisiondata = new FormData();
		lblprecisiondata.top = new FormAttachment(0, 20);
		lblprecisiondata.left = new FormAttachment(0, 20);
		lblPrecision.setLayoutData(lblprecisiondata);

		FormData scaledata = new FormData();
		scaledata.top = new FormAttachment(0, 20);
		scaledata.left = new FormAttachment(lblPrecision, 10);
		scale.setLayoutData(scaledata);

		FormData txtprecisiond = new FormData();
		txtprecisiond.top = new FormAttachment(0, 20);
		txtprecisiond.left = new FormAttachment(scale, 10);
		txtprecisiond.right = new FormAttachment(scale, 100);
		txtPrecision.setLayoutData(txtprecisiond);

		FormData lblbasecurrencydata = new FormData();
		lblbasecurrencydata.top = new FormAttachment(lblPrecision, 20);
		lblbasecurrencydata.left = new FormAttachment(0, 20);
		lblBaseCurrency.setLayoutData(lblbasecurrencydata);

		FormData cmbbasecurrencydata = new FormData();
		cmbbasecurrencydata.top = new FormAttachment(lblPrecision, 20);
		cmbbasecurrencydata.left = new FormAttachment(lblBaseCurrency, 20);
		cmbBaseCurrency.setLayoutData(cmbbasecurrencydata);

		FormData chkreportsinbrowswerdata = new FormData();
		chkreportsinbrowswerdata.top = new FormAttachment(0, 20);
		chkreportsinbrowswerdata.left = new FormAttachment(0, 20);
		chkReportsInBrowswer.setLayoutData(chkreportsinbrowswerdata);
		
		FormData chkdefaultopenconsoledata = new FormData();
		chkdefaultopenconsoledata.top = new FormAttachment(chkReportsInBrowswer, 10);
		chkdefaultopenconsoledata.left = new FormAttachment(0, 20);
		chkDefaultOpenConsole.setLayoutData(chkdefaultopenconsoledata);
		
		FormData chkenablewalletdata = new FormData();
		chkenablewalletdata.top = new FormAttachment(chkDefaultOpenConsole, 10);
		chkenablewalletdata.left = new FormAttachment(0, 20);
		chkEnableWallet.setLayoutData(chkenablewalletdata);
		
		FormData lblspendingpassworddata = new FormData();
		lblspendingpassworddata.top = new FormAttachment(chkEnableWallet, 20);
		lblspendingpassworddata.left = new FormAttachment(0, 60);
		lblSpendingPassword.setLayoutData(lblspendingpassworddata);
		
		FormData txtspendingpassworddata = new FormData();
		txtspendingpassworddata.top = new FormAttachment(chkEnableWallet, 8);
		txtspendingpassworddata.left = new FormAttachment(lblSpendingPassword, 10);
		txtspendingpassworddata.right = new FormAttachment(lblSpendingPassword, 300);
		txtSpendingPassword.setLayoutData(txtspendingpassworddata);
		
		FormData lblbsvrefreshdata = new FormData();
		lblbsvrefreshdata.top = new FormAttachment(txtSpendingPassword, 10);
		lblbsvrefreshdata.left = new FormAttachment(0, 60);
		lblBsvRefresh.setLayoutData(lblbsvrefreshdata);
		
		FormData cmbbsvrefreshdata = new FormData();
		cmbbsvrefreshdata.top = new FormAttachment(txtSpendingPassword, 8);
		cmbbsvrefreshdata.left = new FormAttachment(lblBsvRefresh, 38);
		cmbbsvrefreshdata.right = new FormAttachment(lblBsvRefresh, 300);
		cmbBsvRefresh.setLayoutData(cmbbsvrefreshdata);
		
		FormData lblemaildata = new FormData();
		lblemaildata.top = new FormAttachment(cmbBsvRefresh, 10);
		lblemaildata.left = new FormAttachment(0, 60);
		lblEmail.setLayoutData(lblemaildata);
		
		FormData txtemaildata = new FormData();
		txtemaildata.top = new FormAttachment(cmbBsvRefresh, 8);
		txtemaildata.left = new FormAttachment(lblEmail, 38);
		txtemaildata.right = new FormAttachment(lblEmail, 300);
		txtEmail.setLayoutData(txtemaildata);
		
		FormData lblbookmarksdata = new FormData();
		lblbookmarksdata.top = new FormAttachment(txtEmail, 20);
		lblbookmarksdata.left = new FormAttachment(0, 20);
		lblBookmarks.setLayoutData(lblbookmarksdata);
		
		FormData txtbookmarksdata = new FormData();
		txtbookmarksdata.top = new FormAttachment(txtEmail, 18);
		txtbookmarksdata.left = new FormAttachment(lblBookmarks, 20);
		txtbookmarksdata.right = new FormAttachment(80, 0);
		txtBookmarks.setLayoutData(txtbookmarksdata);
		
		FormData cmdokdata = new FormData();
		cmdokdata.top = new FormAttachment(tabFolder, 5);
		cmdokdata.left = new FormAttachment(30, 0);
		cmdokdata.right = new FormAttachment(50, 0);
		cmdOk.setLayoutData(cmdokdata);

		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(tabFolder, 5);
		cmdcanceldata.left = new FormAttachment(cmdOk, 10);
		cmdcanceldata.right = new FormAttachment(70, 10);
		cmdCancel.setLayoutData(cmdcanceldata);

	}

	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("68"));

		shell.setSize(600, 400);
		this.CenterWindow();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return 1;
	}
	
	@SuppressWarnings("unchecked")
	private void loadBsvMethods() {
		
		Currency c = new Currency();
		c.setIsoName("BSV");
		
		try {
		
			Currency bsv = (Currency) MM.sqlMap.selectOne("getCurrencyByIsoCode", c.getIsoName()); 
			
			if (bsv == null) {
				// Need to add BSV
				c.setCurrencyName("Bitcoin SV");
				c.setExchangeRate("69");
				c.setCurrencyPrecision(8);
				MM.sqlMap.insert("addCurrency", c);
				
				//Refresh BSV currency to get the ID
				bsv = (Currency) MM.sqlMap.selectOne("getCurrencyByIsoCode", c.getIsoName());
				
				//Set the option in memory as it's used later when the wallet
				//account is created.
				MM.options.setDefaultBsvCurrencyID(bsv.getCurrencyID());
			}
			
			List<CurrencyMethod> methods = MM.sqlMap.selectList("getCurrencyMethods", bsv);
			
			if (methods == null || methods.size()==0) {
				/**************************************************************************/
				/* Need to add a default refresh method.  This is going to be hit or miss */
				/* unfortunately as the ticker API's tend to come and go or quickly       */
				/* become incompatible.                                                   */
				
				// Need information for the default currence to construct the api url
				Currency defaultCurrency = (Currency) MM.sqlMap.selectOne("getCurrencyById", 
						Long.valueOf(MM.options.getDefaultCurrencyID()));
				
				//http://bitcoinsv-rates.com/api/rates/
				//https://api.coingecko.com/api/v3/simple/price?ids=bitcoin-cash-sv&vs_currencies=usd
				//https://api.cryptonator.com/api/ticker/bsv-usd
				//https://api.whatsonchain.com/v1/bsv/main/exchangerate
				
				String tickerUrl = "https://api.whatsonchain.com/v1/bsv/main/exchangerate";
				
				CurrencyMethod newMethod = new CurrencyMethod();
				newMethod.setCurrencyID(bsv.getCurrencyID());
				newMethod.setMethodName("whatsonchain_sv");
				newMethod.setMethodPath("$.rate");
				newMethod.setMethodUrl(tickerUrl);
				
				MM.sqlMap.insert("insertCurrencyMethod", newMethod);
				
				
				methods = MM.sqlMap.selectList("getCurrencyMethods", bsv);
				
				/**************************************************************************/

			}
			
			for (int i = 0; i < methods.size(); i++) {
	
				CurrencyMethod method = (CurrencyMethod) methods.get(i);
				cmbBsvRefresh.add(method.getMethodName());
				cmbBsvRefresh.setData(method.getMethodName(), method.getCurrencyID());
				
				if (method.getMethodName().equals(MM.options.getDefaultBsvCurrencyMethod()))
					cmbBsvRefresh.select(i);
			}
		
		} catch (Exception se) {
			InfinityPfm.LogMessage(se.getMessage(), true);
		}
		
	}
	
	private void addBsvWalletAccount() {
		
		Account account=null;
		try {
			account = (Account) MM.sqlMap.selectOne("getAccountForName", MM.BSV_WALLET_ACCOUNT);
		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		
		if (account == null) {
		
			MainAction action = new MainAction();
			account = new Account();
			account.setActBalance(0);
			account.setActTypeName(MM.ACT_TYPE_BANK);
			account.setActName(MM.BSV_WALLET_ACCOUNT);
			account.setCurrencyID(MM.options.getDefaultBsvCurrencyID());
			action.AddAccount(account);
		
		}
		
		// Also add a receiving account for new coins
		try {
			account = (Account) MM.sqlMap.selectOne("getAccountForName", MM.BSV_WALLET_RECEIVING_ACCOUNT);
		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		
		if (account == null) {
		
			MainAction action = new MainAction();
			account = new Account();
			account.setActBalance(0);
			account.setActTypeName(MM.ACT_TYPE_INCOME);
			account.setActName(MM.BSV_WALLET_RECEIVING_ACCOUNT);
			account.setCurrencyID(MM.options.getDefaultCurrencyID());
			action.AddAccount(account);
		
		}
		
	}
	
	private boolean ConfirmNoPassword() {
		
		MessageDialog dialog = new MessageDialog(MM.DIALOG_QUESTION, 
				MM.PHRASES.getPhrase("270"),
				MM.PHRASES.getPhrase("272"));
	
		return dialog.Open()==0;
	}

	private void restartRequiredNotice() {
		
		MessageDialog dialog = new MessageDialog(MM.DIALOG_INFO, 
				MM.PHRASES.getPhrase("67"),
				MM.PHRASES.getPhrase("70"));
		
		dialog.Open();
	}
	
	/*
	 * Listeners
	 */

	SelectionAdapter scale_onGesture = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			int perspectiveValue = scale.getSelection();
			txtPrecision.setText(Integer.toString(perspectiveValue));

		}
	};

	SelectionAdapter chkEnableWallet_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			boolean walletEnabled = chkEnableWallet.getSelection();
			
			txtSpendingPassword.setEnabled(walletEnabled);
			cmbBsvRefresh.setEnabled(walletEnabled);
			txtEmail.setEnabled(walletEnabled);
			
			if (walletEnabled) {
				
				if (_walletPassword == null) 
					_walletPassword = new Password(null, null, _encryptUtil);
				
				loadBsvMethods();
			}
		}
	};
	
	SelectionAdapter cmdOk_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			Options options = null;

			try {

				options = (Options) MM.sqlMap
						.selectOne("getOptions", null);
				options.setCurrencyPrecision(Integer.parseInt(txtPrecision
						.getText()));

				Currency currency = (Currency) MM.sqlMap.selectOne(
						"getCurrencyByName", cmbBaseCurrency.getText());

				if (currency == null) {
					InfinityPfm.LogMessage(MM.PHRASES.getPhrase("271"));
					return;
				} else {
					options.setDefaultCurrencyID((int) currency.getCurrencyID());
				}
				
				options.setEnableWallet(chkEnableWallet.getSelection());
				
				if (options.isEnableWallet()) {
					
					// If the wallet is enabled without a password, ask for confirmation
					if ((_walletPassword.getHashedPassword() == null || _walletPassword.getHashedPassword().length()==0) 
							&& !ConfirmNoPassword())
						return;
		
					InfinityPfm.LogMessage("Password changed = " + _walletPassword.passwordChanged());
					InfinityPfm.LogMessage("Password hash is = " + _walletPassword.getHashedPassword());
					
					options.setSpendPassword(_walletPassword.getHashedPassword());
					options.setDefaultBsvCurrencyMethod(cmbBsvRefresh.getText());
					Object id = cmbBsvRefresh.getData(cmbBsvRefresh.getText());
					if (id != null)
						options.setDefaultBsvCurrencyID(((Long)id).longValue());
					
					options.setEmailAddress(txtEmail.getText());
					
					if (walletSettingsChanged)
						restartRequiredNotice();
				
				}
					
				options.setReportsInBrowswer(chkReportsInBrowswer.getSelection());
				options.setConsoleDefaultOpen(chkDefaultOpenConsole.getSelection());

				options.setBookmarksUrl(txtBookmarks.getText());
				
				MM.sqlMap.update("updateOptions", options);

				// Make sure wallet account exists
				addBsvWalletAccount();

				MM.options = (Options) MM.sqlMap.selectOne("getOptions",
						null);

			} catch (Exception e1) {
				InfinityPfm.LogMessage(e1.getMessage());
			}

			shell.dispose();
		}
	};

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			shell.dispose();
		}
	};
	
	FocusAdapter txtSpendingPassword_OnLostFocus = new FocusAdapter() {

		@Override
		public void focusLost(FocusEvent arg0) {
			
			if (_walletPassword != null)
				_walletPassword.setPlainPassword(txtSpendingPassword.getText());
		}
	};
	
	ModifyListener dcFieldsModified = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent arg0) {
			walletSettingsChanged = true;
		}
		
	};
}
