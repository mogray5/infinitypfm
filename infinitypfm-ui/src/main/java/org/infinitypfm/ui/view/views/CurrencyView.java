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
package org.infinitypfm.ui.view.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.CurrencyMethod;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.currency.RateParser;
import org.infinitypfm.types.DefaultDateFormat;
import org.infinitypfm.ui.view.dialogs.MessageDialog;
import org.infinitypfm.ui.view.dialogs.UpdateMethodSelectorDialog;
import org.infinitypfm.ui.view.toolbars.CurrencyToolbar;

public class CurrencyView extends BaseView {

	private CurrencyToolbar tbMain = null;
	private Table tblCurrencies = null;
	private Label lblMethodHeader = null;
	private Label lblCurrencies = null;
	private Combo cmbCurrencies = null;
	private Label lblMethod = null;
	private Text txtMethod = null;
	private Label lblUrl = null;
	private Text txtUrl = null;
	private Label lblPath = null;
	private Text txtPath = null;
	private Composite cmpHeader = null;
	private Button cmdRefresh = null;
	private Button cmdAddMethod = null;
	private Button cmdRemoveMethod = null;
	private Button cmdLoadMethod = null;
	private DataFormatUtil formatter = null;
	private ArrayList<Button> buttons = null;
	private ArrayList<Combo> combos = null;

	public CurrencyView(Composite arg0, int arg1) {
		super(arg0, arg1);

		LoadUI();
		LoadLayout();
	}

	@Override
	protected void LoadUI() {

		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		tbMain = new CurrencyToolbar(this);
		tblCurrencies = new Table(this, SWT.BORDER);
		
		LoadColumns();

		cmpHeader = new Composite(this, SWT.BORDER);
		cmpHeader.setLayout(new FormLayout());
		cmdRefresh = new Button(cmpHeader, SWT.PUSH);
		cmdRefresh.setText(MM.PHRASES.getPhrase("43"));
		cmdRefresh.addSelectionListener(cmdRefreshAll_OnClick);
		cmdAddMethod = new Button(cmpHeader, SWT.PUSH);
		cmdAddMethod.setText(MM.PHRASES.getPhrase("45"));
		cmdAddMethod.addSelectionListener(cmdAddMethod_OnClick);
		cmdRemoveMethod = new Button(cmpHeader, SWT.PUSH);
		cmdRemoveMethod.setText(MM.PHRASES.getPhrase("230"));
		cmdRemoveMethod.addSelectionListener(cmdRemoveMethod_OnClick);
		cmdLoadMethod = new Button(cmpHeader, SWT.PUSH);
		cmdLoadMethod.setText(MM.PHRASES.getPhrase("231"));
		cmdLoadMethod.addSelectionListener(cmbLoadMethod_OnClick);
		lblMethodHeader = new Label(cmpHeader, SWT.BORDER);
		lblMethodHeader.setText(MM.PHRASES.getPhrase("229"));
		lblCurrencies = new Label(cmpHeader, SWT.NONE);
		lblCurrencies.setText(MM.PHRASES.getPhrase("209"));
		cmbCurrencies = new Combo(cmpHeader, SWT.BORDER | SWT.READ_ONLY);
		lblMethod = new Label(cmpHeader, SWT.NONE);
		lblMethod.setText(MM.PHRASES.getPhrase("226"));
		txtMethod = new Text(cmpHeader, SWT.BORDER);
		lblUrl = new Label(cmpHeader, SWT.NONE);
		lblUrl.setText(MM.PHRASES.getPhrase("227"));
		txtUrl = new Text(cmpHeader, SWT.BORDER);
		lblPath = new Label(cmpHeader, SWT.NONE);
		lblPath.setText(MM.PHRASES.getPhrase("228"));
		txtPath = new Text(cmpHeader, SWT.BORDER);

		LoadData();

		// Set tabs
		this.setTabList(new Control[]{cmpHeader, tblCurrencies});
		cmpHeader.setTabList(new Control[]{cmdLoadMethod, txtMethod, cmbCurrencies, 
				txtUrl, txtPath, cmdAddMethod, cmdRemoveMethod, cmdRefresh});
	}

	protected void LoadLayout() {

		FormData tbmaindata = new FormData();
		tbmaindata.right = new FormAttachment(100, 0);
		tbMain.setLayoutDat(tbmaindata);

		FormData cmdaddmethoddata = new FormData();
		cmdaddmethoddata.top = new FormAttachment(100, -40);
		cmdaddmethoddata.left = new FormAttachment(5, 0);
		cmdAddMethod.setLayoutData(cmdaddmethoddata);

		FormData cmdremovemethoddata = new FormData();
		cmdremovemethoddata.top = new FormAttachment(100, -40);
		cmdremovemethoddata.left = new FormAttachment(cmdAddMethod, 10);
		cmdRemoveMethod.setLayoutData(cmdremovemethoddata);

		FormData cmdrefreshdata = new FormData();
		cmdrefreshdata.top = new FormAttachment(100, -40);
		cmdrefreshdata.left = new FormAttachment(cmdRemoveMethod, 10);
		cmdRefresh.setLayoutData(cmdrefreshdata);

		FormData cmpheaderdata = new FormData();
		cmpheaderdata.top = new FormAttachment(0, 10);
		cmpheaderdata.left = new FormAttachment(0, 10);
		cmpheaderdata.right = new FormAttachment(100, -10);
		cmpheaderdata.bottom = new FormAttachment(0, 250);
		cmpHeader.setLayoutData(cmpheaderdata);

		FormData lblmethodheaderdata = new FormData();
		lblmethodheaderdata.top = new FormAttachment(5, 5);
		lblmethodheaderdata.left = new FormAttachment(5, 0);
		lblMethodHeader.setLayoutData(lblmethodheaderdata);

		FormData lblmethoddata = new FormData();
		lblmethoddata.top = new FormAttachment(lblMethodHeader, 20);
		lblmethoddata.left = new FormAttachment(5, 0);
		lblMethod.setLayoutData(lblmethoddata);

		FormData txtmethoddata = new FormData();
		txtmethoddata.top = new FormAttachment(lblMethodHeader, 20);
		txtmethoddata.left = new FormAttachment(lblMethod, 20);
		txtmethoddata.right = new FormAttachment(lblMethod, 400);
		txtMethod.setLayoutData(txtmethoddata);

		FormData lblcurrenciesdata = new FormData();
		lblcurrenciesdata.top = new FormAttachment(lblMethodHeader, 25);
		lblcurrenciesdata.left = new FormAttachment(txtMethod, 5);
		lblCurrencies.setLayoutData(lblcurrenciesdata);

		FormData cmbcurrenciesdata = new FormData();
		cmbcurrenciesdata.top = new FormAttachment(lblMethodHeader, 25);
		cmbcurrenciesdata.left = new FormAttachment(lblCurrencies, 5);
		cmbCurrencies.setLayoutData(cmbcurrenciesdata);

		FormData lblurldata = new FormData();
		lblurldata.top = new FormAttachment(cmbCurrencies, 17);
		lblurldata.left = new FormAttachment(5, 0);
		lblUrl.setLayoutData(lblurldata);

		FormData txturldata = new FormData();
		txturldata.top = new FormAttachment(cmbCurrencies, 17);
		txturldata.left = new FormAttachment(lblMethod, 20);
		txturldata.right = new FormAttachment(lblMethod, 700);
		txtUrl.setLayoutData(txturldata);

		FormData lblpathdata = new FormData();
		lblpathdata.top = new FormAttachment(txtUrl, 10);
		lblpathdata.left = new FormAttachment(5, 0);
		lblPath.setLayoutData(lblpathdata);

		FormData txtpathdata = new FormData();
		txtpathdata.top = new FormAttachment(txtUrl, 7);
		txtpathdata.left = new FormAttachment(lblMethod, 20);
		txtpathdata.right = new FormAttachment(lblMethod, 700);
		txtPath.setLayoutData(txtpathdata);

		FormData tblcurrenciesdata = new FormData();
		tblcurrenciesdata.top = new FormAttachment(cmpHeader, 7);
		tblcurrenciesdata.right = new FormAttachment(100, -20);
		tblcurrenciesdata.left = new FormAttachment(0, 20);
		tblcurrenciesdata.bottom = new FormAttachment(100, -20);
		tblCurrencies.setLayoutData(tblcurrenciesdata);

		FormData cmdloadmethoddata = new FormData();
		cmdloadmethoddata.top = new FormAttachment(3, 7);
		cmdloadmethoddata.left = new FormAttachment(lblMethodHeader, 20);
		cmdLoadMethod.setLayoutData(cmdloadmethoddata);

	}

	private void LoadColumns() {

		TableColumn tc1 = new TableColumn(tblCurrencies, SWT.CENTER);
		TableColumn tc2 = new TableColumn(tblCurrencies, SWT.LEFT);
		TableColumn tc3 = new TableColumn(tblCurrencies, SWT.LEFT);
		TableColumn tc4 = new TableColumn(tblCurrencies, SWT.CENTER);
		TableColumn tc5 = new TableColumn(tblCurrencies, SWT.CENTER);
		TableColumn tc6 = new TableColumn(tblCurrencies, SWT.CENTER);

		tc1.setText(MM.PHRASES.getPhrase("210"));
		tc2.setText(MM.PHRASES.getPhrase("209"));
		tc3.setText(MM.PHRASES.getPhrase("211"));
		tc4.setText(MM.PHRASES.getPhrase("215"));
		tc5.setText(MM.PHRASES.getPhrase("216"));
		// tc6.setText(MM.PHRASES.getPhrase("47"));

		tc1.setWidth(110);
		tc2.setWidth(115);
		tc3.setWidth(110);
		tc4.setWidth(170);
		tc5.setWidth(140);
		tc6.setWidth(70);

		tblCurrencies.setHeaderVisible(true);

	}

	private void LoadData() {

		Currency item = null;
		TableItem ti = null;

		tblCurrencies.removeAll();

		if (buttons != null) {

			for (int b = 0; b < buttons.size(); b++) {
				Button button = buttons.get(b);
				button.dispose();
			}
		}

		if (combos != null) {

			for (int b = 0; b < combos.size(); b++) {
				Combo c = combos.get(b);
				c.dispose();
			}
		}

		buttons = new ArrayList<Button>();
		combos = new ArrayList<Combo>();

		try {
			@SuppressWarnings("rawtypes")
			List currList = MM.sqlMap.selectList("getCurrencies", null);

			if (currList != null && currList.size() > 0) {

				for (int i = 0; i < currList.size(); i++) {

					item = (Currency) currList.get(i);
					formatter.setDate(item.getLastUpdate());
					ti = new TableItem(tblCurrencies, SWT.NONE);
					
					ti.setText(new String[] { item.getIsoName(),
							item.getCurrencyName(), item.getExchangeRate(),
							formatter.getFormat(DefaultDateFormat.TIME) });

					AddRowRefreshCommands(item, ti);

				}

			}

			tblCurrencies.setLinesVisible(true);
			

			// Load currency combo

			cmbCurrencies.removeAll();

			@SuppressWarnings("rawtypes")
			List lstCurrencies = MM.sqlMap.selectList("getCurrencies");

			if (!lstCurrencies.isEmpty()) {
				for (Object o : lstCurrencies) {

					if (((Currency) o).getIsDefault().equalsIgnoreCase("false"))
						cmbCurrencies.add(((Currency) o).getCurrencyName());

				}
			}

		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}

	@Override
	public void Refresh() {
		super.Refresh();
	}

	private void AddRowRefreshCommands(Currency curr, TableItem ti) {

		try {

			// Don't show any currency methods for the default currency
			if (curr.getIsDefault().equalsIgnoreCase("true"))
				return;

			@SuppressWarnings("rawtypes")
			List methods = MM.sqlMap.selectList("getCurrencyMethods", curr);
			CurrencyMethod method = null;

			if (methods == null || methods.size() == 0)
				return;

			RowSelection rowSelection = null;

			TableEditor editor = new TableEditor(tblCurrencies);
			Combo cmbMethod = new Combo(tblCurrencies, SWT.DROP_DOWN
					| SWT.READ_ONLY);

			for (int i = 0; i < methods.size(); i++) {

				method = (CurrencyMethod) methods.get(i);
				cmbMethod.add(method.getMethodName());

			}

			cmbMethod.select(0);

			cmbMethod.pack();
			editor.minimumWidth = cmbMethod.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(cmbMethod, ti, 4);

			rowSelection = new RowSelection();
			rowSelection.setCurrencyID(curr.getCurrencyID());
			rowSelection.setCmbMethod(cmbMethod);

			rowSelection.setRow(ti);

			editor = new TableEditor(tblCurrencies);
			Button button = new Button(tblCurrencies, SWT.PUSH);
			button.addSelectionListener(cmdRefreshRow_OnClick);
			button.setText(MM.PHRASES.getPhrase("47"));
			button.setData(rowSelection);
			button.pack();
			editor.minimumWidth = button.getSize().x;
			editor.horizontalAlignment = SWT.LEFT;
			editor.setEditor(button, ti, 5);

			combos.add(cmbMethod);
			buttons.add(button);
			
		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}

	}

	private void UpdateExchangeRate(RowSelection selection) {

		TableItem item = (TableItem) selection.getRow();

		CurrencyMethod method = new CurrencyMethod();
		method.setCurrencyID(selection.getCurrencyID());
		method.setMethodName(selection.getCmbMethod().getText());

		try {
			method = (CurrencyMethod) MM.sqlMap.selectOne(
					"getCurrencyMethod", method);

			String newRate = RateParser.getRate(method);
			formatter.setDate(new Date());
			item.setText(2, newRate);
			item.setText(3, formatter.getFormat(DefaultDateFormat.TIME));

			Currency currUpdate = new Currency();
			currUpdate.setCurrencyID(selection.getCurrencyID());
			currUpdate.setExchangeRate(newRate);
			currUpdate.setLastUpdate(formatter.getDate());
			MM.sqlMap.update("updateExchangeRate", currUpdate);

		} catch (Exception e) {
			InfinityPfm.LogMessage(e.toString());
		}

	}

	/*
	 * Listeners
	 */
	SelectionAdapter cmdRefreshRow_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			InfinityPfm.qzMain.getStMain().setBusy(true);
			InfinityPfm.qzMain.getStMain().setStatus("working...");

			// get row selection data
			RowSelection selection = (RowSelection) ((Button) event.getSource())
					.getData();

			UpdateExchangeRate(selection);

			InfinityPfm.qzMain.getStMain().setBusy(false);
			InfinityPfm.qzMain.getStMain().setStatus("Ready");
		}
	};

	SelectionAdapter cmdRefreshAll_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			InfinityPfm.qzMain.getStMain().setBusy(true);
			InfinityPfm.qzMain.getStMain().setStatus("working...");

			Button button = null;
			RowSelection selection = null;

			for (int i = 0; i < buttons.size(); i++) {

				button = buttons.get(i);
				selection = (RowSelection) button.getData();
				UpdateExchangeRate(selection);

			}

			InfinityPfm.qzMain.getStMain().setBusy(false);
			InfinityPfm.qzMain.getStMain().setStatus("Ready");

		}
	};

	SelectionAdapter cmdRemoveMethod_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			if (txtMethod.getText().length() == 0
					|| cmbCurrencies.getText().length() == 0)
				return;

			MessageDialog dialog = new MessageDialog(MM.DIALOG_QUESTION, "",
					MM.PHRASES.getPhrase("232"));
			int answer = dialog.Open();

			if (answer == MM.YES) {

				CurrencyMethod method = new CurrencyMethod();
				method.setMethodName(txtMethod.getText());

				try {
					Currency currency = (Currency) MM.sqlMap.selectOne(
							"getCurrencyByName", cmbCurrencies.getText());
					
					if (currency != null) {
						method.setCurrencyID(currency.getCurrencyID());
						MM.sqlMap.delete("deleteUpdateMethod", method);
						LoadData();
					}

				} catch (Exception e) {
					InfinityPfm.LogMessage(e.toString());
				}

			}

		}
	};

	SelectionAdapter cmdAddMethod_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			if (txtMethod.getText() == null
					|| txtMethod.getText().length() == 0)
				return;

			if (txtUrl.getText() == null || txtUrl.getText().length() == 0)
				return;

			if (txtPath.getText() == null || txtPath.getText().length() == 0)
				return;

			CurrencyMethod method = new CurrencyMethod();
			method.setMethodName(txtMethod.getText());
			method.setMethodUrl(txtUrl.getText());
			Currency currency = null;

			try {
				currency = (Currency) MM.sqlMap.selectOne(
						"getCurrencyByName", cmbCurrencies.getText());
			} catch (Exception e1) {
				InfinityPfm.LogMessage(e1.toString());
			}

			method.setCurrencyID(currency.getCurrencyID());
			method.setMethodPath(txtPath.getText());

			try {
				MM.sqlMap.insert("insertCurrencyMethod", method);
			} catch (Exception e) {
				InfinityPfm.LogMessage(e.toString());
			}

			LoadData();

		}
	};

	SelectionAdapter cmbLoadMethod_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			UpdateMethodSelectorDialog um = new UpdateMethodSelectorDialog();
			um.Open();
			CurrencyMethod method = um.getMethod();

			if (method != null) {

				try {

					txtMethod.setText(method.getMethodName());
					txtUrl.setText(method.getMethodUrl());
					txtPath.setText(method.getMethodPath());

					Currency currency = (Currency) MM.sqlMap.selectOne(
							"getCurrencyById", method.getCurrencyID());

					if (currency != null) {
						cmbCurrencies.setText(currency.getCurrencyName());
					}

				} catch (Exception e) {
					InfinityPfm.LogMessage(e.toString());
				}
			}
		}
	};

	/*
	 * Inner Classes
	 */

	private class RowSelection {

		private Long currencyID;
		private Combo cmbMethod = null;
		private TableItem row = null;

		public Long getCurrencyID() {
			return currencyID;
		}

		public void setCurrencyID(Long currencyID) {
			this.currencyID = currencyID;
		}

		public Combo getCmbMethod() {
			return cmbMethod;
		}

		public void setCmbMethod(Combo cmbMethod) {
			this.cmbMethod = cmbMethod;
		}

		public TableItem getRow() {
			return row;
		}

		public void setRow(TableItem row) {
			this.row = row;
		}

	}

}
