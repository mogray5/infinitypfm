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
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.AccountHash;
import org.infinitypfm.core.data.ImportRule;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.data.DataHandler;
import org.infinitypfm.exception.TransactionException;
import org.infinitypfm.types.AccountTypes;
import org.infinitypfm.types.ImportRuleNames;

/**
 * Main import dialog. Handles all file types.
 * 
 * @author Wayne Gray
 * 
 */
public class ImportDialog extends BaseDialog {

	private Combo cmbAct = null;

	private Button cmdSave = null;
	private Button cmdCancel = null;
	private Table tblTrans = null;
	private TableEditor editor = null;
	private Label lblAct = null;
	private Label lblAlert = null;

	private java.util.List<Transaction> tranList = null;

	private java.util.List<Account> allActList = null;
	private AccountHash actHash = null;
	private DataHandler data = new DataHandler();

	private String importCurrency = null;
	private final int importAccount;

	private static final int EDITABLECOLUMN = 2;
	private static final String RATE_EDITOR = "rateEditor";
	private static final String TRAN = "tran";
	private static final String SPLIT = "**" + MM.PHRASES.getPhrase("235")
			+ "**";

	@SuppressWarnings("unchecked")
	public ImportDialog() {
		super();

		importAccount = -1;

		try {
			allActList = MM.sqlMap.queryForList("getAllAccountsByType", null);
		} catch (SQLException se) {
			InfinityPfm.LogMessage(se.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public ImportDialog(java.util.List<Transaction> tList, int importAcct) {
		super();

		tranList = tList;
		importAccount = importAcct;

		try {
			allActList = MM.sqlMap.queryForList("getAllAccountsByType", null);
			actHash = new AccountHash(allActList);
		} catch (SQLException se) {
			InfinityPfm.LogMessage(se.getMessage());
		}

	}

	protected void LoadUI(Shell sh) {

		cmbAct = new Combo(sh, SWT.BORDER);
		cmbAct.addSelectionListener(cmbAccount_OnClick);
		cmdSave = new Button(sh, SWT.PUSH);
		cmdSave.setText(MM.PHRASES.getPhrase("14"));
		cmdSave.addSelectionListener(cmdSave_OnClick);
		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);
		tblTrans = new Table(sh, SWT.MULTI | SWT.HIDE_SELECTION);
		tblTrans.setLinesVisible(true);

		lblAct = new Label(sh, SWT.NONE);
		lblAct.setText(MM.PHRASES.getPhrase("15"));
		lblAlert = new Label(sh, SWT.BORDER);
		lblAlert.setText(MM.PHRASES.getPhrase("135"));

		LoadAccountCombo();
		LoadColumns();
		LoadAccounts();

		editor = new TableEditor(tblTrans);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;

		tblTrans.addSelectionListener(tblTrans_OnClick);
		tblTrans.addListener(SWT.MeasureItem, paintListener);

	}

	protected void LoadLayout() {

		FormData lblactdata = new FormData();
		lblactdata.top = new FormAttachment(0, 10);
		lblactdata.left = new FormAttachment(0, 10);
		lblAct.setLayoutData(lblactdata);

		FormData cmbactdata = new FormData();
		cmbactdata.top = new FormAttachment(lblAct, 10);
		cmbactdata.left = new FormAttachment(0, 10);
		cmbAct.setLayoutData(cmbactdata);

		FormData lblalertdata = new FormData();
		lblalertdata.top = new FormAttachment(lblAct, 10);
		lblalertdata.left = new FormAttachment(cmbAct, 10);
		lblalertdata.right = new FormAttachment(100, -10);
		lblAlert.setLayoutData(lblalertdata);

		FormData tbltransdata = new FormData();
		tbltransdata.top = new FormAttachment(cmbAct, 20);
		tbltransdata.left = new FormAttachment(0, 10);
		tbltransdata.right = new FormAttachment(100, -10);
		tbltransdata.bottom = new FormAttachment(100, -50);
		tblTrans.setLayoutData(tbltransdata);

		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(tblTrans, 10);
		cmdcanceldata.right = new FormAttachment(cmdSave, -10);
		cmdCancel.setLayoutData(cmdcanceldata);

		FormData cmdsavedata = new FormData();
		cmdsavedata.top = new FormAttachment(tblTrans, 10);
		cmdsavedata.right = new FormAttachment(100, -10);
		cmdSave.setLayoutData(cmdsavedata);
	}

	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("18") + " " + MM.APPTITLE);

		shell.setSize(1024, 700);
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
	private void LoadAccounts() {

		TableEditor rateEditor = null;
		TableItem ti = null;
		Transaction tran = null;
		java.util.List<Account> accountList = null;
		Account account = null;

		final Display display = InfinityPfm.shMain.getDisplay();

		@SuppressWarnings("rawtypes")
		List importRules = null;
		ImportRule rule = null;
		Account offset = null;

		// Load all defined import rules
		try {
			importRules = MM.sqlMap.queryForList("getImportRules");
		} catch (SQLException e1) {
			InfinityPfm.LogMessage("Error loading import rules:  "
					+ e1.getMessage());
		}

		if (tranList != null) {

			for (int i = 0; i < tranList.size(); i++) {
				tran = tranList.get(i);
				ti = new TableItem(tblTrans, SWT.NONE);
				ti.setText(0, tran.getTranDateFmt());

				if (tran.getTranMemo() != null) {
					ti.setText(1, tran.getTranMemo());
				}

				try {

					// Apply any import rules if defined
					if (importRules != null && importRules.size() > 0) {

						offset = null;

						for (int j = 0; j < importRules.size(); j++) {

							// See if any of the rules match the transaction
							// Break out and use the mapped account on first one
							// found
							rule = (ImportRule) importRules.get(j);
							if (rule != null) {

								if (rule.getRuleName().equals(
										ImportRuleNames.CONTAINS)) {

									if (tran.getTranMemo().contains(
											rule.getKeyword())) {
										offset = new Account();
										offset.setActId(rule.getActId());
										break;
									}

								} else if (rule.getRuleName().equals(
										ImportRuleNames.STARTSWITH)) {

									if (tran.getTranMemo().startsWith(
											rule.getKeyword())) {
										offset = new Account();
										offset.setActId(rule.getActId());
										break;
									}

								} else if (rule.getRuleName().equals(
										ImportRuleNames.ENDSWITH)) {

									if (tran.getTranMemo().endsWith(
											rule.getKeyword())) {
										offset = new Account();
										offset.setActId(rule.getActId());
										break;
									}
								}
							}
						}
					}

					if (offset != null) {

						// Found a rule match. Add mapped offset to the
						// transactions
						offset = (Account) MM.sqlMap.queryForObject(
								"getAccountByActId", offset.getActId());
						if (offset != null) {
							tran.setActOffset(offset.getActId());
							ti.setText(2, offset.getActName());
							ti.setText(4, offset.getIsoCode());
						} else {
							// This should never happen
							InfinityPfm.LogMessage(MM.PHRASES.getPhrase("246"));
						}

					} else {

						// Try to use last saved offset for this transactions
						accountList = MM.sqlMap.queryForList(
								"getAccountsForMemo", tran);

						if (accountList != null) {
							if (accountList.size() > 0) {
								account = accountList.get(0);
								tran.setActOffset(account.getActId());
								ti.setText(2, account.getActName());
								ti.setText(4, account.getIsoCode());

							}
						}
					}

				} catch (SQLException e) {
					InfinityPfm.LogMessage(e.getMessage());
				}

				ti.setData(TRAN, tran);
				ti.setText(3,
						formatter.getAmountFormatted(tran.getTranAmount()));

				rateEditor = new TableEditor(tblTrans);
				Text txtRate = new Text(tblTrans, SWT.CENTER);
				txtRate.addFocusListener(ratebox_lostFocus);
				txtRate.setData(Integer.toString(i));
				txtRate.pack();
				rateEditor.grabHorizontal = true;
				rateEditor.minimumWidth = 30;
				rateEditor.horizontalAlignment = SWT.CENTER;
				rateEditor.setEditor(txtRate, ti, 5);
				ti.setData(RATE_EDITOR, rateEditor);

				setRate(account, actHash.get(cmbAct.getText()), txtRate, tran);

				if (i % 2D == 0) {

					Color altRowColor = display
							.getSystemColor(MM.ROW_BACKGROUND);

					ti.setBackground(0, altRowColor);
					ti.setBackground(1, altRowColor);
					ti.setBackground(2, altRowColor);
					ti.setBackground(3, altRowColor);
					txtRate.setBackground(altRowColor);
				}
			}

			cmdSave.setEnabled(true);

		}
	}

	private void LoadColumns() {

		TableColumn tc1 = new TableColumn(tblTrans, SWT.CENTER);
		TableColumn tc2 = new TableColumn(tblTrans, SWT.LEFT);
		TableColumn tc3 = new TableColumn(tblTrans, SWT.LEFT);
		TableColumn tc4 = new TableColumn(tblTrans, SWT.CENTER);
		TableColumn tc5 = new TableColumn(tblTrans, SWT.CENTER);
		TableColumn tc6 = new TableColumn(tblTrans, SWT.CENTER);

		tc1.setText(MM.PHRASES.getPhrase("24"));
		tc2.setText(MM.PHRASES.getPhrase("41"));
		tc3.setText(MM.PHRASES.getPhrase("42"));
		tc4.setText(MM.PHRASES.getPhrase("55"));
		tc5.setText(MM.PHRASES.getPhrase("210"));
		tc6.setText(MM.PHRASES.getPhrase("211"));

		tc1.setWidth(80);
		tc2.setWidth(285);
		tc3.setWidth(190);
		tc4.setWidth(90);
		tc5.setWidth(120);
		tc6.setWidth(120);

		tblTrans.setHeaderVisible(true);
	}

	@SuppressWarnings("unchecked")
	private void LoadAccountCombo() {

		try {
			Account act = null;
			java.util.List<Account> list = MM.sqlMap.queryForList(
					"getAccountsForType", AccountTypes.BANK);

			if (list.size() == 0) {
				lblAlert.setText(MM.PHRASES.getPhrase("64"));
				return;
			}

			cmbAct.removeAll();

			int showIndex = 0;

			for (int i = 0; i < list.size(); i++) {
				act = list.get(i);
				cmbAct.add(act.getActName());

				if (act.getActId() == importAccount) {
					showIndex = i;
					importCurrency = act.getIsoCode();
					lblAct.setText(MM.PHRASES.getPhrase("15") + " ("
							+ importCurrency + ")");
				}

			}

			cmbAct.select(showIndex);

		} catch (SQLException se) {
			InfinityPfm.LogMessage(se.getMessage());
		}
	}

	private Composite getEditor() {

		Composite cellEditor = new Composite(tblTrans, SWT.NONE);
		cellEditor.setLayout(new FormLayout());
		CCombo cmbOffset = new CCombo(cellEditor, SWT.READ_ONLY);
		Button cmdSplit = new Button(cellEditor, SWT.PUSH);
		cmdSplit.setText("...");
		cmdSplit.setToolTipText(MM.PHRASES.getPhrase("233"));

		FormData cmboffsetdata = new FormData();
		cmboffsetdata.top = new FormAttachment(0, 0);
		cmboffsetdata.left = new FormAttachment(0, 0);
		cmboffsetdata.right = new FormAttachment(100, -23);
		cmbOffset.setLayoutData(cmboffsetdata);

		FormData cmdsplitdata = new FormData();
		cmdsplitdata.top = new FormAttachment(0, 0);
		cmdsplitdata.left = new FormAttachment(cmbOffset, 0);
		cmdSplit.setLayoutData(cmdsplitdata);

		Account act = null;

		if (allActList == null) {
			lblAlert.setText(MM.PHRASES.getPhrase("64"));
			return null;
		}

		for (int i = 0; i < allActList.size(); i++) {
			act = allActList.get(i);
			cmbOffset.add(act.getActName());
		}

		cmbOffset.add(SPLIT);

		cellEditor.pack();
		return cellEditor;

	}

	private void setRate(Account offsetAccount, Account importAccount,
			Text rateBox, Transaction tran) {

		if (offsetAccount == null || importAccount == null) {
			rateBox.setEnabled(false);
			tran.setExchangeRate("1");
			return;
		}

		if (!offsetAccount.getIsoCode().equalsIgnoreCase(importCurrency)) {
			rateBox.setEnabled(true);

			if (rateBox.getText().length() == 0) {
				try {

					String rate = data.getExchangeRate(importAccount,
							offsetAccount);
					tran.setExchangeRate(rate);
					rateBox.setText(rate);

				} catch (SQLException e) {
					InfinityPfm.LogMessage(e.getMessage());
				}

			}

		} else {
			rateBox.setText("");
			rateBox.setEnabled(false);
			tran.setExchangeRate("1");

		}

	}

	/*
	 * Listeners
	 */

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			shell.dispose();
		}
	};

	SelectionAdapter cmdSave_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			InfinityPfm.qzMain.getStMain().setBusy(true);
			InfinityPfm.qzMain.getStMain()
					.setStatus(MM.PHRASES.getPhrase("89"));

			Account act = actHash.get(cmbAct.getText());

			if (act != null) {

				Transaction tran = null;
				for (int i = 0; i < tranList.size(); i++) {
					tran = tranList.get(i);
					tran.setActId(act.getActId());
				}

			}
			Object[] trans = tranList.toArray();

			// confirm save
			MessageDialog msg = new MessageDialog(MM.DIALOG_QUESTION,
					MM.APPTITLE, MM.PHRASES.getPhrase("90"));

			int answer = msg.Open();

			if (answer == MM.YES) {
				try {
					data.AddTransactionBatch(trans);
				} catch (SQLException e) {
					InfinityPfm.LogMessage(e.getMessage(), true);
				} catch (TransactionException e) {
					InfinityPfm.LogMessage(e.getMessage(), true);
				}
			}

			InfinityPfm.qzMain.getStMain()
					.setStatus(MM.PHRASES.getPhrase("87"));
			InfinityPfm.qzMain.getStMain().setBusy(false);

			if (answer != MM.CANCEL) {
				shell.dispose();
			}

		}
	};

	SelectionAdapter tblTrans_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {

			// Clean up any previous editor control
			Control oldEditor = editor.getEditor();
			if (oldEditor != null)
				oldEditor.dispose();

			// Identify the selected row
			TableItem item = (TableItem) event.item;
			if (item == null)
				return;

			// The control that will be the editor must be a child of the Table
			Composite newEditor = getEditor();

			Control[] childList = newEditor.getChildren();
			for (Control child : childList) {
				if (child instanceof CCombo) {

					((CCombo) child).addSelectionListener(rowCombo_OnClick);

				} else if (child instanceof Button) {
					((Button) child).addSelectionListener(rowButton_OnClick);
				}
			}

			cmdSave.setEnabled(true);

			if (newEditor != null) {
				newEditor.setFocus();
				editor.setEditor(newEditor, item, EDITABLECOLUMN);
			}
		}
	};

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

	SelectionAdapter rowCombo_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			Composite c = (Composite) editor.getEditor();

			for (Control child : c.getChildren()) {
				if (child instanceof CCombo) {

					CCombo combo = (CCombo) child;
					String sAct = combo.getText();
					Account act = null;
					Account importAct = null;
					Transaction tran = null;
					Text rateBox = null;

					if (sAct != null && !sAct.equals(SPLIT)) {
						editor.getItem().setText(EDITABLECOLUMN, sAct);
						tran = (Transaction) editor.getItem().getData(TRAN);
						rateBox = (Text) ((TableEditor) (editor.getItem()
								.getData(RATE_EDITOR))).getEditor();
						act = (Account) actHash.get(sAct);

						importAct = (Account) actHash.get(cmbAct.getText());

						if (act != null) {
							tran.setActOffset(act.getActId());
						} else {
							rateBox.setEnabled(false);
						}

						setRate(act, importAct, rateBox, tran);

						editor.getItem().setText(4, act.getIsoCode());

					} else if (sAct != null && sAct.equals(SPLIT)) {
						editor.getItem().setText(EDITABLECOLUMN, SPLIT);
					}
				}
			}
		}
	};

	SelectionAdapter rowButton_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			Composite c = (Composite) editor.getEditor();

			Account importAct = (Account) actHash.get(cmbAct.getText());
			Transaction savedTran = (Transaction) editor.getItem()
					.getData(TRAN);

			TransactionDialog dialog = new TransactionDialog(importAct,
					savedTran);
			dialog.setTransactionMode(MM.TRANSACTION_MODE_NOCOMMIT);
			dialog.Open();
			Transaction tran = dialog.getTransaction();

			if (tran != null) {

				savedTran.setOffsets(tran.getOffsets());

				for (Control child : c.getChildren()) {
					if (child instanceof CCombo) {
						CCombo combo = (CCombo) child;

						if (tran.getOffsets() != null
								&& tran.getOffsets().size() > 1) {
							editor.getItem().setText(EDITABLECOLUMN, SPLIT);
							combo.setText(SPLIT);
						} else if (tran.getOffsets().size() == 1) {
							editor.getItem().setText(tran.getActOffsetName());
							combo.setText(tran.getActOffsetName());
						}
					}
				}
			}
		}
	};

	SelectionAdapter cmbAccount_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {

			TableItem[] rows = tblTrans.getItems();
			TableItem row = null;
			TableEditor editor = null;
			Account offset = null;
			String act = null;
			Text txtRate = null;
			Transaction tran = null;

			Account importAccount = actHash.get(cmbAct.getText());
			importCurrency = importAccount.getIsoCode();

			for (int i = 0; i < rows.length; i++) {
				row = rows[i];
				editor = (TableEditor) row.getData(RATE_EDITOR);
				tran = (Transaction) row.getData(TRAN);
				act = row.getText(2);

				if (act != null) {
					offset = actHash.get(act);
					txtRate = (Text) editor.getEditor();

					setRate(offset, importAccount, txtRate, tran);

				} else {
					txtRate.setEnabled(true);
				}

			}

			lblAct.setText(MM.PHRASES.getPhrase("15") + " (" + importCurrency
					+ ")");
		}
	};

	FocusAdapter ratebox_lostFocus = new FocusAdapter() {

		@Override
		public void focusLost(FocusEvent event) {

			Text ratebox = (Text) event.widget;
			int row = Integer.parseInt((String) ratebox.getData());
			Table tbl = (Table) ratebox.getParent();
			TableItem[] items = tbl.getItems();
			Transaction tran = (Transaction) items[row].getData(TRAN);
			tran.setExchangeRate(ratebox.getText());

			super.focusLost(event);

		}

	};

}
