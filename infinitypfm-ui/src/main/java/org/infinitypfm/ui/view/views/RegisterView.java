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

import java.sql.Timestamp;
import java.util.Date;
import java.util.ListIterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.ParamDateRangeAccount;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.types.DefaultDateFormat;
import org.infinitypfm.ui.view.dialogs.DateDialog;
import org.infinitypfm.ui.view.toolbars.RegisterToolbar;

/**
 * Dialog to allow transaction import and setting of offsets before import.
 * 
 */
public class RegisterView extends BaseView {

	private Table tblRegister = null;
	private Label lblBalance = null;
	private RegisterToolbar tbMain = null;
	private Label lblActName = null;
	private Label lblCurrency = null;
	private Label lblStartDate = null;
	private Label lblEndDate = null;
	private Text txtStartDate = null;
	private Text txtEndDate = null;
	private Button cmdStartDate = null;
	private Button cmdEndDate = null;
	private Account act = null;
	private boolean columnsLoaded = false;
	private DateDialog dateDialog = null;
	DataFormatUtil formatter = null;
	TableColumn tc3 = null;
	TableColumn tc4 = null;
	TableColumn tc5 = null;

	public RegisterView(Composite arg0, int arg1) {
		super(arg0, arg1);
		
		formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());
		
		// init console
		LoadUI();
		LoadLayout();
	}

	protected void LoadUI() {

		lblBalance = new Label(this, SWT.NONE);
		lblBalance.setText(MM.PHRASES.getPhrase("2"));
		tbMain = new RegisterToolbar(this);
		tblRegister = new Table(this, SWT.BORDER);
		tblRegister.addListener(SWT.MeasureItem, paintListener);
		tblRegister.addControlListener(onRegisterResize);

		// get currently selected account
		act = InfinityPfm.qzMain.getTrMain().getSelectedAccount();

		lblActName = new Label(this, SWT.NONE);
		lblActName.setText(act.getActName());
		lblCurrency = new Label(this, SWT.NONE);
		lblCurrency.setText(act.getIsoCode());
		lblStartDate = new Label(this, SWT.NONE);
		lblStartDate.setText(MM.PHRASES.getPhrase("222"));
		lblEndDate = new Label(this, SWT.NONE);
		lblEndDate.setText(MM.PHRASES.getPhrase("223"));
		txtStartDate = new Text(this, SWT.BORDER);
		formatter.setDate(formatter.getToday());
		formatter.setDate(formatter.getYear(), formatter.getMonth() == 1 ? 1
				: formatter.getMonth() - 1);
		txtStartDate.setText(formatter.getFormat(DefaultDateFormat.DAY));
		txtStartDate.setEditable(false);
		txtEndDate = new Text(this, SWT.BORDER);
		formatter.setDate(formatter.getToday());
		txtEndDate.setText(formatter.getFormat(DefaultDateFormat.DAY));
		txtEndDate.setEditable(false);
		cmdStartDate = new Button(this, SWT.PUSH);
		cmdStartDate.setImage(InfinityPfm.imMain.getImage(MM.IMG_CALENDAR));
		cmdStartDate.addSelectionListener(cmdStartDatePicker_OnClick);
		cmdEndDate = new Button(this, SWT.PUSH);
		cmdEndDate.setImage(InfinityPfm.imMain.getImage(MM.IMG_CALENDAR));
		cmdEndDate.addSelectionListener(cmdEndDatePicker_OnClick);

		this.Refresh();
	}

	protected void LoadLayout() {
		this.setLayout(new FormLayout());

		FormData lblactnamedata = new FormData();
		lblactnamedata.top = new FormAttachment(0, 40);
		lblactnamedata.left = new FormAttachment(0, 20);
		lblActName.setLayoutData(lblactnamedata);

		FormData lblstartdatedata = new FormData();
		lblstartdatedata.top = new FormAttachment(0, 40);
		lblstartdatedata.left = new FormAttachment(22, 0);
		lblStartDate.setLayoutData(lblstartdatedata);

		FormData txtstartdatedata = new FormData();
		txtstartdatedata.top = new FormAttachment(lblStartDate, -33);
		txtstartdatedata.left = new FormAttachment(lblStartDate, 5);
		txtstartdatedata.right = new FormAttachment(lblStartDate, 160);
		txtStartDate.setLayoutData(txtstartdatedata);

		FormData cmdstartdatedata = new FormData();
		cmdstartdatedata.top = new FormAttachment(lblStartDate, -37);
		cmdstartdatedata.left = new FormAttachment(txtStartDate, 2);
		cmdStartDate.setLayoutData(cmdstartdatedata);

		FormData lblenddatedata = new FormData();
		lblenddatedata.top = new FormAttachment(0, 40);
		lblenddatedata.left = new FormAttachment(cmdStartDate, 10);
		lblEndDate.setLayoutData(lblenddatedata);

		FormData txtenddatedata = new FormData();
		txtenddatedata.top = new FormAttachment(lblEndDate, -33);
		txtenddatedata.left = new FormAttachment(lblEndDate, 5);
		txtenddatedata.right = new FormAttachment(lblEndDate, 150);
		txtEndDate.setLayoutData(txtenddatedata);

		FormData cmdenddatedata = new FormData();
		cmdenddatedata.top = new FormAttachment(lblEndDate, -37);
		cmdenddatedata.left = new FormAttachment(txtEndDate, 2);
		cmdEndDate.setLayoutData(cmdenddatedata);

		FormData lblbalancedata = new FormData();
		lblbalancedata.top = new FormAttachment(0, 40);
		lblbalancedata.right = new FormAttachment(100, -60);
		lblBalance.setLayoutData(lblbalancedata);

		FormData lblcurrencydata = new FormData();
		lblcurrencydata.top = new FormAttachment(0, 40);
		lblcurrencydata.left = new FormAttachment(lblBalance, 5);
		lblCurrency.setLayoutData(lblcurrencydata);

		FormData tblregisterdata = new FormData();
		tblregisterdata.top = new FormAttachment(lblBalance, 10);
		tblregisterdata.right = new FormAttachment(100, -20);
		tblregisterdata.left = new FormAttachment(0, 20);
		tblregisterdata.bottom = new FormAttachment(100, -20);
		tblRegister.setLayoutData(tblregisterdata);

		FormData tbmaindata = new FormData();
		tbmaindata.right = new FormAttachment(100, 0);
		tbMain.setLayoutDat(tbmaindata);

	}

	@SuppressWarnings("rawtypes")
	private void LoadTransactions(java.util.List tranList) {

		TableItem ti = null;
		Transaction tran = null;

		if (act.getIsoCode().equalsIgnoreCase(MM.BSV))
			formatter.setPrecision(8);
		
		tblRegister.removeAll();

		final Display display = InfinityPfm.shMain.getDisplay();

		if (tranList != null) {

			for (int i = 0; i < tranList.size(); i++) {

				tran = (Transaction) tranList.get(i);
				ti = new TableItem(tblRegister, SWT.NONE);

				if (i % 2D == 0) {
					ti.setBackground(0,
							display.getSystemColor(MM.ROW_BACKGROUND));
					ti.setBackground(1,
							display.getSystemColor(MM.ROW_BACKGROUND));
					ti.setBackground(2,
							display.getSystemColor(MM.ROW_BACKGROUND));
					ti.setBackground(3,
							display.getSystemColor(MM.ROW_BACKGROUND));
				}

				ti.setText(0, tran.getTranDateFmt());
				ti.setText(1, tran.getTranMemo());

				if (tran.getTranAmount() > 0) {
					ti.setText(2, formatter.getAmountFormatted(Math.abs(tran
							.getTranAmount())));
				} else {
					ti.setText(3, formatter.getAmountFormatted(Math.abs(tran
							.getTranAmount())));
				}
				
				
				ti.setText(4, formatter.getAmountFormatted(tran.getActBalance()));
			}

		}
	}

	private void LoadColumns() {

		TableColumn tc1 = new TableColumn(tblRegister, SWT.CENTER);
		TableColumn tc2 = new TableColumn(tblRegister, SWT.LEFT);
		tc3 = new TableColumn(tblRegister, SWT.CENTER);
		tc4 = new TableColumn(tblRegister, SWT.CENTER);
		tc5 = new TableColumn(tblRegister, SWT.CENTER);
		

		tc1.setText(MM.PHRASES.getPhrase("24"));
		tc2.setText(MM.PHRASES.getPhrase("41"));
		tc3.setText(MM.PHRASES.getPhrase("48"));
		tc4.setText(MM.PHRASES.getPhrase("46"));
		tc5.setText(MM.PHRASES.getPhrase("2"));

		tc1.setWidth(100);
		tc2.setWidth(320);
		tc3.setWidth(70);
		tc4.setWidth(70);
		tc5.setWidth(70);
		
		tblRegister.setHeaderVisible(true);
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void Refresh() {
		super.Refresh();

		if (act != null) {

			// TODO: Show range of transactions, allow user to change range

			try {
				
				// get a fresh account in case balance changed
				Account account = (Account) MM.sqlMap.selectOne(
						"getAccountForName", act.getActName());
				// set act balance and name
				if (act.getIsoCode().equalsIgnoreCase(MM.BSV))
					formatter.setPrecision(8);
				else
					formatter.setPrecision(account.getCurrencyPrecision());
				
				lblBalance
						.setText(MM.PHRASES.getPhrase("2")
								+ ": "
								+ formatter.getAmountFormatted(account
										.getActBalance()));
				
				
				// load transactions
				ParamDateRangeAccount params = new ParamDateRangeAccount();
				params.setActId(act.getActId());
				formatter.setDate(txtStartDate.getText());
				params.setStartDate(new Timestamp(formatter.getDate().getTime()));
				formatter.setDate(txtEndDate.getText());
				params.setEndDate(new Timestamp(formatter.getDate().getTime()));
				java.util.List tranList = MM.sqlMap.selectList(
						"getTransactionsForRangeAndAccount", params);
				if (tranList != null) {

					if (!columnsLoaded) {
						LoadColumns();
						columnsLoaded = true;
					}

					//Loop through list backwards and calculate account balance
					ListIterator li = tranList.listIterator(tranList.size());
					long newBalance = account.getActBalance();
					Transaction tran = null;
					
					while(li.hasPrevious()) {
					  tran = (Transaction)li.previous();
					  tran.setActBalance(newBalance);
					  newBalance-=tran.getTranAmount();
					}
					
					LoadTransactions(tranList);
				}

				tblRegister.setLinesVisible(true);

				updateReportParams();
				
			} catch (Exception se) {
				InfinityPfm.LogMessage(se.getMessage());
			}

		}
	}
	
	/**
	 * Keep a report param account object in sync.
	 * Use when running register report
	 */
	private void updateReportParams() {
		if (MM.reportParams == null)
			MM.reportParams = new ParamDateRangeAccount();
		
		ParamDateRangeAccount param =(ParamDateRangeAccount) MM.reportParams;
		formatter.setDate(txtStartDate.getText(), "MM-dd-yyyy");
		Timestamp start = new Timestamp(formatter.getDate().getTime());
		formatter.setDate(txtEndDate.getText(), "MM-dd-yyyy");
		Timestamp end = new Timestamp(formatter.getDate().getTime()+24 * 60 * 60 * 1000); // Add a day for inclusive range
		param.setActId(act.getActId());
		param.setStartDate(start);
		param.setEndDate(end);
		param.setAccountName(act.getActName());
	}
	

	/*
	 * Listeners
	 */

	Listener paintListener = new Listener() {
		public void handleEvent(Event event) {

			switch (event.type) {
			case SWT.MeasureItem: {
				event.height = 20;
				break;
			}
			}

		}
	};

	ControlAdapter onRegisterResize = new ControlAdapter() {

		public void controlResized(ControlEvent e) {
			// Keep debit, credit, and balance columns the same 
			// width as table is resized
			try {
				tc5.setWidth((tc3.getWidth() + tc4.getWidth() + tc5.getWidth()) / 3);
				tc4.setWidth(tc5.getWidth());
				tc3.setWidth(tc5.getWidth());
			} catch (Exception err) {
				InfinityPfm.LogMessage(err.getMessage());
			}
		}
	};

	SelectionAdapter cmdStartDatePicker_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (dateDialog == null) {
				dateDialog = new DateDialog();
			}

			dateDialog.Open();
			
			try {
				txtStartDate.setText(dateDialog.getSelectedDate());
				updateReportParams();
			} catch (Exception err) {
				InfinityPfm.LogMessage(err.getMessage());
			}
			
			Refresh();
		}
	};

	SelectionAdapter cmdEndDatePicker_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (dateDialog == null) {
				dateDialog = new DateDialog();
			}

			dateDialog.Open();
			
			try {
				txtEndDate.setText(dateDialog.getSelectedDate());
				updateReportParams();
			} catch (Exception err) {
				InfinityPfm.LogMessage(err.getMessage());
			}
			
			Refresh();
		}
	};
}
