/*
 * Copyright (c) 2005-2017 Wayne Gray All rights reserved
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
import java.util.List;

import org.eclipse.swt.SWT;
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
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.Options;

/**
 * @author wggray
 */
public class OptionsDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	private Label lblPrecision = null;
	private Label lblBaseCurrency = null;
	private Text txtPrecision = null;
	private Combo cmbBaseCurrency = null;
	private Button cmdOk = null;
	private Button cmdCancel = null;
	private Button chkReportsInBrowswer;
	private Button chkDefaultOpenConsole;
	private Scale scale = null;
	private TabFolder tabFolder = null;

	public OptionsDialog() {
		super();
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
		chkReportsInBrowswer.setSelection(options.isReportsInBrowswer());
		
		chkDefaultOpenConsole = new Button(reportGroup, SWT.CHECK);
		chkDefaultOpenConsole.setText(MM.PHRASES.getPhrase("251"));
		chkDefaultOpenConsole.setSelection(options.isConsoleDefaultOpen());
		
		currencyItem.setControl(currencyGroup);
		reportItem.setControl(reportGroup);

		cmdOk = new Button(sh, SWT.PUSH);
		cmdOk.setText(MM.PHRASES.getPhrase("5"));
		cmdOk.addSelectionListener(cmdOk_OnClick);
		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);

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
		// scaledata.right = new FormAttachment(80, 0);
		scale.setLayoutData(scaledata);

		FormData txtprecisiond = new FormData();
		txtprecisiond.top = new FormAttachment(0, 20);
		txtprecisiond.left = new FormAttachment(scale, 10);
		txtprecisiond.right = new FormAttachment(scale, 80);
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

	/*
	 * Listeners
	 */

	SelectionAdapter scale_onGesture = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			int perspectiveValue = scale.getSelection();
			txtPrecision.setText(Integer.toString(perspectiveValue));

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
					InfinityPfm.LogMessage("Problem saving options");
					return;
				} else {
					options.setDefaultCurrencyID((int) currency.getCurrencyID());
				}

				options.setReportsInBrowswer(chkReportsInBrowswer.getSelection());
				options.setConsoleDefaultOpen(chkDefaultOpenConsole.getSelection());
				
				MM.sqlMap.update("updateOptions", options);

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
}
