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
package org.infinitypfm.ui.view.views;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.RecurHeader;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.data.DataHandler;
import org.infinitypfm.exception.TransactionException;
import org.infinitypfm.ui.view.dialogs.DateDialog;
import org.infinitypfm.ui.view.dialogs.MessageDialog;
import org.infinitypfm.ui.view.toolbars.RecurrenceToolbar;

public class RecurrenceView extends BaseView {

	private Table tblSaved = null;
	private Table tblPending = null;
	private Composite cmpHeader = null;
	private Button cmdAdd = null;
	private Button cmdPostAllPending = null;
	private Label lblName = null;
	private Label lblFrequency = null;
	private Label lblStartDate = null;
	private Text txtName = null;
	private Combo cmbFrequency = null;
	private Text txtStartDate = null;
	private Button cmdStartDatePicker = null;
	private Label lblAccount = null;
	private Label lblOffset = null;
	private Label lblAmount = null;
	private Combo cmbAccount = null;
	private Combo cmbOffset = null;
	private Text txtAmount = null;
	private TableEditor tableSavedEditor = null;
	private TableEditor tablePendingEditor = null;
	private RecurrenceToolbar tbMain = null;
	private Label lblPending = null;
	private Label lblSaved = null;
	private Label lblAdd = null;
	private ArrayList<Button> buttons = null;
	private ArrayList<Button> pendingButtons = null;
	private HashMap<String, String> mFrequency = null;
	private DataHandler handler = null;
	private DateDialog dateDialog = null;
	private DataFormatUtil dataFormat = null;

	public RecurrenceView(Composite arg0, int arg1) {
		super(arg0, arg1);

		// init console
		mFrequency = new HashMap<String, String>();
		mFrequency.put(MM.PHRASES.getPhrase(MM.RECUR_BIWEEKLY),
				MM.RECUR_BIWEEKLY);
		mFrequency
				.put(MM.PHRASES.getPhrase(MM.RECUR_MONTHLY), MM.RECUR_MONTHLY);
		mFrequency.put(MM.PHRASES.getPhrase(MM.RECUR_WEEKLY), MM.RECUR_WEEKLY);
		mFrequency.put(MM.PHRASES.getPhrase(MM.RECUR_YEARLY), MM.RECUR_YEARLY);

		handler = new DataHandler();
		dataFormat = new DataFormatUtil(MM.options.getCurrencyPrecision());
		LoadUI();
		LoadLayout();
	}

	@Override
	protected void LoadUI() {
		tblSaved = new Table(this, SWT.MULTI | SWT.HIDE_SELECTION);
		tblSaved.setLinesVisible(true);

		tblPending = new Table(this, SWT.MULTI | SWT.HIDE_SELECTION);
		tblPending.setLinesVisible(true);

		LoadColumns();
		tbMain = new RecurrenceToolbar(this);

		cmpHeader = new Composite(this, SWT.BORDER);
		cmpHeader.setLayout(new FormLayout());

		cmdAdd = new Button(cmpHeader, SWT.PUSH);
		cmdAdd.setText(MM.PHRASES.getPhrase("45"));
		cmdAdd.setToolTipText(MM.PHRASES.getPhrase("142"));
		cmdAdd.addSelectionListener(cmdAdd_OnClick);

		cmdPostAllPending = new Button(this, SWT.PUSH);
		cmdPostAllPending.setText(MM.PHRASES.getPhrase("238"));
		cmdPostAllPending.addSelectionListener(cmdPostAllPending_OnClick);

		lblName = new Label(cmpHeader, SWT.NONE);
		lblName.setText(MM.PHRASES.getPhrase("41"));

		lblFrequency = new Label(cmpHeader, SWT.NONE);
		lblFrequency.setText(MM.PHRASES.getPhrase("144"));

		lblStartDate = new Label(cmpHeader, SWT.NONE);
		lblStartDate.setText(MM.PHRASES.getPhrase("145"));
		txtName = new Text(cmpHeader, SWT.BORDER);

		cmbFrequency = new Combo(cmpHeader, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbFrequency.setItems(new String[] {
				MM.PHRASES.getPhrase(MM.RECUR_MONTHLY),
				MM.PHRASES.getPhrase(MM.RECUR_WEEKLY),
				MM.PHRASES.getPhrase(MM.RECUR_BIWEEKLY),
				MM.PHRASES.getPhrase(MM.RECUR_YEARLY) });
		cmbFrequency.setText(MM.PHRASES.getPhrase("146"));

		txtStartDate = new Text(cmpHeader, SWT.BORDER);

		cmdStartDatePicker = new Button(cmpHeader, SWT.PUSH);
		cmdStartDatePicker.setImage(InfinityPfm.imMain.getImage(MM.IMG_CALENDAR));
		cmdStartDatePicker.addSelectionListener(cmdStartDatePicker_OnClick);

		lblAccount = new Label(cmpHeader, SWT.NONE);
		lblAccount.setText(MM.PHRASES.getPhrase("93"));

		lblOffset = new Label(cmpHeader, SWT.NONE);
		lblOffset.setText(MM.PHRASES.getPhrase("42"));

		lblAmount = new Label(cmpHeader, SWT.NONE);
		lblAmount.setText(MM.PHRASES.getPhrase("55"));

		cmbAccount = new Combo(cmpHeader, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbOffset = new Combo(cmpHeader, SWT.DROP_DOWN | SWT.READ_ONLY);
		LoadAccountCombos();
		txtAmount = new Text(cmpHeader, SWT.BORDER);

		lblPending = new Label(this, SWT.NONE);
		lblPending.setText(MM.PHRASES.getPhrase("150"));

		lblSaved = new Label(this, SWT.NONE);
		lblSaved.setText(MM.PHRASES.getPhrase("152"));

		lblAdd = new Label(cmpHeader, SWT.NONE);
		lblAdd.setText(MM.PHRASES.getPhrase("142"));

		try {
			LoadRecurringTransactions();
			LoadPendingTransactions();
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
	}

	@Override
	protected void LoadLayout() {

		this.setLayout(new FormLayout());

		FormData tbmaindata = new FormData();
		tbmaindata.right = new FormAttachment(100, 0);
		tbMain.setLayoutDat(tbmaindata);

		FormData cmpheaderdata = new FormData();
		cmpheaderdata.top = new FormAttachment(0, 10);
		cmpheaderdata.left = new FormAttachment(0, 10);
		cmpheaderdata.right = new FormAttachment(100, -10);
		cmpheaderdata.bottom = new FormAttachment(0, 200);
		cmpHeader.setLayoutData(cmpheaderdata);

		FormData cmdadddata = new FormData();
		cmdadddata.top = new FormAttachment(50, 0);
		cmdadddata.left = new FormAttachment(10, 0);
		cmdadddata.right = new FormAttachment(10, 50);
		cmdAdd.setLayoutData(cmdadddata);

		FormData lblnamedata = new FormData();
		lblnamedata.top = new FormAttachment(30, 0);
		lblnamedata.left = new FormAttachment(20, 0);
		lblName.setLayoutData(lblnamedata);

		FormData txtnamedata = new FormData();
		txtnamedata.top = new FormAttachment(30, 0);
		txtnamedata.left = new FormAttachment(lblName, 60);
		txtnamedata.right = new FormAttachment(lblName, 200);
		txtName.setLayoutData(txtnamedata);

		FormData lblfrequencydata = new FormData();
		lblfrequencydata.top = new FormAttachment(lblName, 20);
		lblfrequencydata.left = new FormAttachment(20, 0);
		lblFrequency.setLayoutData(lblfrequencydata);

		FormData cmbfrequencydata = new FormData();
		cmbfrequencydata.top = new FormAttachment(lblName, 20);
		cmbfrequencydata.left = new FormAttachment(lblName, 60);
		cmbfrequencydata.right = new FormAttachment(lblName, 200);
		cmbFrequency.setLayoutData(cmbfrequencydata);

		FormData lblstartdatedata = new FormData();
		lblstartdatedata.top = new FormAttachment(lblFrequency, 20);
		lblstartdatedata.left = new FormAttachment(20, 0);
		lblStartDate.setLayoutData(lblstartdatedata);

		FormData txtstartdatedata = new FormData();
		txtstartdatedata.top = new FormAttachment(lblFrequency, 20);
		txtstartdatedata.left = new FormAttachment(lblName, 60);
		txtstartdatedata.right = new FormAttachment(lblName, 200);
		txtStartDate.setLayoutData(txtstartdatedata);

		FormData cmdstartdatepickerdata = new FormData();
		cmdstartdatepickerdata.top = new FormAttachment(lblFrequency, 17);
		cmdstartdatepickerdata.left = new FormAttachment(txtStartDate, 0);
		cmdStartDatePicker.setLayoutData(cmdstartdatepickerdata);

		FormData lblaccountdata = new FormData();
		lblaccountdata.top = new FormAttachment(30, 0);
		lblaccountdata.left = new FormAttachment(55, 0);
		lblAccount.setLayoutData(lblaccountdata);

		FormData cmbaccountdata = new FormData();
		cmbaccountdata.top = new FormAttachment(30, 0);
		cmbaccountdata.left = new FormAttachment(lblAccount, 50);
		cmbaccountdata.right = new FormAttachment(lblAccount, 300);
		cmbAccount.setLayoutData(cmbaccountdata);

		FormData lbloffsetdata = new FormData();
		lbloffsetdata.top = new FormAttachment(lblAccount, 20);
		lbloffsetdata.left = new FormAttachment(55, 0);
		lblOffset.setLayoutData(lbloffsetdata);

		FormData cmboffsetdata = new FormData();
		cmboffsetdata.top = new FormAttachment(lblAccount, 20);
		cmboffsetdata.left = new FormAttachment(lblAccount, 50);
		cmboffsetdata.right = new FormAttachment(lblAccount, 300);
		cmbOffset.setLayoutData(cmboffsetdata);

		FormData lblamountdata = new FormData();
		lblamountdata.top = new FormAttachment(lblOffset, 20);
		lblamountdata.left = new FormAttachment(55, 0);
		lblAmount.setLayoutData(lblamountdata);

		FormData txtamountdata = new FormData();
		txtamountdata.top = new FormAttachment(lblOffset, 20);
		txtamountdata.left = new FormAttachment(lblAccount, 50);
		txtAmount.setLayoutData(txtamountdata);

		FormData lblsaveddata = new FormData();
		lblsaveddata.top = new FormAttachment(cmpHeader, 10);
		lblsaveddata.left = new FormAttachment(0, 20);
		lblSaved.setLayoutData(lblsaveddata);

		FormData tblsaveddata = new FormData();
		tblsaveddata.top = new FormAttachment(lblSaved, 10);
		tblsaveddata.left = new FormAttachment(0, 20);
		tblsaveddata.right = new FormAttachment(100, -20);
		tblsaveddata.bottom = new FormAttachment(60, 0);
		tblSaved.setLayoutData(tblsaveddata);

		FormData lblpendingdata = new FormData();
		lblpendingdata.top = new FormAttachment(tblSaved, 10);
		lblpendingdata.left = new FormAttachment(0, 20);
		lblPending.setLayoutData(lblpendingdata);

		FormData cmdpostallpendingdata = new FormData();
		cmdpostallpendingdata.top = new FormAttachment(tblSaved, 3);
		cmdpostallpendingdata.left = new FormAttachment(lblPending, 10);
		cmdPostAllPending.setLayoutData(cmdpostallpendingdata);

		FormData tblpendingdata = new FormData();
		tblpendingdata.top = new FormAttachment(tblSaved, 35);
		tblpendingdata.left = new FormAttachment(0, 20);
		tblpendingdata.right = new FormAttachment(100, -20);
		tblpendingdata.bottom = new FormAttachment(100, -20);
		tblPending.setLayoutData(tblpendingdata);

		FormData lbladdddata = new FormData();
		lbladdddata.top = new FormAttachment(0, 10);
		lbladdddata.left = new FormAttachment(40, 0);
		lblAdd.setLayoutData(lbladdddata);
	}

	private void LoadAccountCombos() {

		try {
			Account act = null;
			@SuppressWarnings("rawtypes")
			java.util.List list = MM.sqlMap.queryForList(
					"getAllAccountsByType", null);

			cmbAccount.removeAll();
			cmbOffset.removeAll();

			for (int i = 0; i < list.size(); i++) {
				act = (Account) list.get(i);
				cmbAccount.add(act.getActName());
				cmbAccount.setData(act.getActName(), act);
				cmbOffset.add(act.getActName());
				cmbOffset.setData(act.getActName(), act);

			}

		} catch (SQLException se) {
			InfinityPfm.LogMessage(se.getMessage());
		}
	}

	private void LoadColumns() {

		TableColumn tc1 = new TableColumn(tblSaved, SWT.CENTER);
		TableColumn tc2 = new TableColumn(tblSaved, SWT.LEFT);
		TableColumn tc3 = new TableColumn(tblSaved, SWT.LEFT);
		TableColumn tc4 = new TableColumn(tblSaved, SWT.CENTER);
		TableColumn tc5 = new TableColumn(tblSaved, SWT.CENTER);
		TableColumn tc6 = new TableColumn(tblSaved, SWT.CENTER);
		TableColumn tc7 = new TableColumn(tblSaved, SWT.NONE);

		tc1.setText(MM.PHRASES.getPhrase("41"));
		tc2.setText(MM.PHRASES.getPhrase("144"));
		tc3.setText(MM.PHRASES.getPhrase("145"));
		tc4.setText(MM.PHRASES.getPhrase("93"));
		tc5.setText(MM.PHRASES.getPhrase("42"));
		tc6.setText(MM.PHRASES.getPhrase("55"));

		tc1.setWidth(150);
		tc2.setWidth(150);
		tc3.setWidth(150);
		tc4.setWidth(150);
		tc5.setWidth(150);
		tc6.setWidth(90);
		tc7.setWidth(90);

		tblSaved.setHeaderVisible(true);

		TableColumn tc11 = new TableColumn(tblPending, SWT.CENTER);
		TableColumn tc13 = new TableColumn(tblPending, SWT.LEFT);
		TableColumn tc14 = new TableColumn(tblPending, SWT.CENTER);
		TableColumn tc15 = new TableColumn(tblPending, SWT.CENTER);
		TableColumn tc16 = new TableColumn(tblPending, SWT.CENTER);
		TableColumn tc17 = new TableColumn(tblPending, SWT.NONE);
		TableColumn tc18 = new TableColumn(tblPending, SWT.NONE);

		tc11.setText(MM.PHRASES.getPhrase("41"));
		tc13.setText(MM.PHRASES.getPhrase("24"));
		tc14.setText(MM.PHRASES.getPhrase("93"));
		tc15.setText(MM.PHRASES.getPhrase("42"));
		tc16.setText(MM.PHRASES.getPhrase("55"));

		tc11.setWidth(70);
		tc13.setWidth(150);
		tc14.setWidth(90);
		tc15.setWidth(150);
		tc16.setWidth(90);

		tc17.setWidth(90);
		tc18.setWidth(90);

		tblPending.setHeaderVisible(true);
	}

	private void LoadRecurringTransactions() throws SQLException {

		@SuppressWarnings("rawtypes")
		List recurList = MM.sqlMap.queryForList("getRecurringTransactions",
				null);

		tblSaved.removeAll();

		if (buttons != null) {

			for (int b = 0; b < buttons.size(); b++) {
				Button button = buttons.get(b);
				button.dispose();
			}
		}

		buttons = new ArrayList<Button>();

		if (recurList != null) {
			for (int i = 0; i < recurList.size(); i++) {
				tableSavedEditor = new TableEditor(tblSaved);
				RecurHeader recurHeader = (RecurHeader) recurList.get(i);
				TableItem ti = new TableItem(tblSaved, SWT.NONE);
				ti.setText(0, recurHeader.getMemo());
				ti.setText(1, recurHeader.getFrequency());
				ti.setText(2, recurHeader.getNextRunDateFmt());
				ti.setText(3, recurHeader.getActName());
				ti.setText(4, recurHeader.getOffsetName());
				ti.setText(5, recurHeader.getAmountFmt(MM.options.getCurrencyPrecision()));
				Button button = new Button(tblSaved, SWT.PUSH);
				button.addSelectionListener(cmdDelete_OnClick);
				button.setText(MM.PHRASES.getPhrase("151"));
				button.setData(recurHeader.getRecurId());
				button.pack();
				tableSavedEditor.minimumWidth = button.getSize().x;
				tableSavedEditor.horizontalAlignment = SWT.LEFT;
				tableSavedEditor.setEditor(button, ti, 6);
				buttons.add(button);
			}
		}
	}

	private void LoadPendingTransactions() throws SQLException {

		@SuppressWarnings("rawtypes")
		List trans = MM.sqlMap.queryForList("getPendingTransactions", null);
		DataFormatUtil format = new DataFormatUtil(MM.options.getCurrencyPrecision());

		tblPending.removeAll();

		if (pendingButtons != null) {

			for (int b = 0; b < pendingButtons.size(); b++) {
				Button button = pendingButtons.get(b);
				button.dispose();
			}
		}

		pendingButtons = new ArrayList<Button>();

		if (trans != null) {

			for (int i = 0; i < trans.size(); i++) {
				tablePendingEditor = new TableEditor(tblPending);
				Transaction tran = (Transaction) trans.get(i);
				TableItem ti = new TableItem(tblPending, SWT.NONE);
				ti.setText(0, tran.getTranMemo());
				ti.setText(1, tran.getTranDateFmt());
				ti.setText(2, tran.getActName());
				ti.setText(3, tran.getActOffsetName());
				ti.setText(4, format.getAmountFormatted(tran.getTranAmount()));
				Button button = new Button(tblPending, SWT.PUSH);
				button.setText(MM.PHRASES.getPhrase("14"));
				button.setData(tran);
				button.addSelectionListener(cmdPendingCommit_OnClick);
				button.pack();
				Button deleteButton = new Button(tblPending, SWT.PUSH);
				deleteButton.setText(MM.PHRASES.getPhrase("151"));
				deleteButton.setData(tran);
				deleteButton.addSelectionListener(cmdPendingDelete_OnClick);
				deleteButton.pack();
				tablePendingEditor.minimumWidth = button.getSize().x;
				tablePendingEditor.horizontalAlignment = SWT.LEFT;
				tablePendingEditor.setEditor(button, ti, 5);

				tablePendingEditor = new TableEditor(tblPending);
				tablePendingEditor.minimumWidth = deleteButton.getSize().x;
				tablePendingEditor.horizontalAlignment = SWT.LEFT;
				tablePendingEditor.setEditor(deleteButton, ti, 6);
				pendingButtons.add(button);
				pendingButtons.add(deleteButton);
			}

		}

	}

	private boolean FormValidate() {

		boolean result = false;

		try {

			if (txtAmount.getText().length() > 0) {
				float amount = new Float(txtAmount.getText());
				if (cmbAccount.getText().length() > 0) {
					if (cmbOffset.getText().length() > 0) {
						if (txtName.getText().length() > 0) {
							if (amount != 0) {
								result = true;
							}
						}

					}
				}
			}

		} catch (Exception e) {
			result = false;
		}

		return result;
	}

	/*
	 * Listeners
	 */
	SelectionAdapter cmdAdd_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			if (FormValidate()) {

				RecurHeader recurHeader = new RecurHeader();
				Account account = (Account) cmbAccount.getData(cmbAccount
						.getText());
				Account offset = (Account) cmbOffset.getData(cmbOffset
						.getText());

				recurHeader.setActId(account.getActId());
				recurHeader.setOffsetId(offset.getActId());

				recurHeader.setAmount(DataFormatUtil.moneyToLong(txtAmount
						.getText()));
				recurHeader.setFrequencyId(new Integer(mFrequency
						.get(cmbFrequency.getText())).intValue());
				recurHeader.setMemo(txtName.getText());
				dataFormat.setDate(txtStartDate.getText());
				recurHeader.setNextRunDate(dataFormat.getDate());

				try {
					MM.sqlMap.insert("insertRecurringTransaction", recurHeader);
					handler.ProcessRecurringTransaction();
					LoadRecurringTransactions();
					LoadPendingTransactions();
				} catch (SQLException e) {
					InfinityPfm.LogMessage(e.getMessage());
				}
			}
		}
	};

	SelectionAdapter cmdDelete_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			Integer recurId = (Integer) event.widget.getData();
			try {
				MessageDialog dialog = new MessageDialog(MM.DIALOG_QUESTION,
						"", MM.PHRASES.getPhrase("153"));
				int answer = dialog.Open();

				if (answer == MM.YES) {
					MM.sqlMap.delete("deletePendingTransactionsByRecurId",
							recurId);
					MM.sqlMap.delete("deleteRecurringTransaction", recurId);
					LoadRecurringTransactions();
				}

			} catch (NumberFormatException e) {
				InfinityPfm.LogMessage(e.getMessage());
			} catch (SQLException e) {
				InfinityPfm.LogMessage(e.getMessage());
			}
		}
	};

	/**
	 * Post all pending transactions.  Use the transactions already stored
	 * in the action widgets for the database commit.
	 */
	SelectionAdapter cmdPostAllPending_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			if (pendingButtons.size() == 0)
				return;

			try {
				DataHandler handler = new DataHandler();
				long postAmount = 0;
				int postCount = 0;
				DataFormatUtil formatter = new DataFormatUtil(MM.options.getCurrencyPrecision());

				for (int i = 0; i < pendingButtons.size(); i++) {
					Button b = (Button) pendingButtons.get(i);

					if (b.getText()
							.equalsIgnoreCase(MM.PHRASES.getPhrase("14"))) {

						Transaction tran = (Transaction) b.getData();

						if (tran != null) {
							postAmount += tran.getTranAmount();
							postCount++;
							handler.AddTransaction(tran, false);
							MM.sqlMap.delete("deletePendingTransaction",
									tran.getRecurTranId());
						}
					}
				}

				LoadPendingTransactions();
				InfinityPfm.LogMessage(
						postCount + " " + MM.PHRASES.getPhrase("239") + ". \n"
								+ MM.PHRASES.getPhrase("55") + ": "
								+ formatter.getAmountFormatted(postAmount),
						true);

			} catch (SQLException e) {
				InfinityPfm.LogMessage(e.getMessage());
			} catch (TransactionException e) {
				InfinityPfm.LogMessage(e.getMessage());
			}
		}
	};

	SelectionAdapter cmdPendingDelete_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			Transaction tran = (Transaction) event.widget.getData();
			try {
				MM.sqlMap.delete("deletePendingTransaction",
						tran.getRecurTranId());
				LoadPendingTransactions();
			} catch (SQLException e) {
				InfinityPfm.LogMessage(e.getMessage());
			}
		}
	};

	SelectionAdapter cmdPendingCommit_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			Transaction tran = (Transaction) event.widget.getData();
			try {
				DataHandler handler = new DataHandler();
				handler.AddTransaction(tran, false);
				MM.sqlMap.delete("deletePendingTransaction",
						tran.getRecurTranId());
				LoadPendingTransactions();
			} catch (SQLException e) {
				InfinityPfm.LogMessage(e.getMessage());
			} catch (TransactionException e) {
				InfinityPfm.LogMessage(e.getMessage());
			}
		}
	};

	SelectionAdapter cmdStartDatePicker_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			if (dateDialog == null)
				dateDialog = new DateDialog();
			
			dateDialog.Open();
			
			try {
				txtStartDate.setText(dateDialog.getSelectedDate());
			} catch (Exception err){
				InfinityPfm.LogMessage(err.getMessage());
			}
		}
	};
}
