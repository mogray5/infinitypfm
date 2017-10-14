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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.AccountHash;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;
import org.infinitypfm.data.DataHandler;
import org.infinitypfm.exception.TransactionException;
import org.infinitypfm.types.NumberFormat;

/**
 * @author Wayne Gray
 */
public class TransactionDialog extends BaseDialog {

	private Label lblAccount = null;
	private Label lblAmount = null;
	private Label lblMemo = null;
	private Label lblDate = null;
	private Label lblExRate = null;
	private Text txtAmount = null;
	private Text txtMemo = null;
	private Text txtExRate = null;
	private Button cmdCommit = null;
	private Button cmdClose = null;
	private Button cmdDatePicker = null;
	private Text txtDate = null;
	private DateDialog dateDialog = null;
	
	private Composite cmpOffset = null;
	private Label lblOffset = null;
	private Label lblOffsetAmount = null;
	private Text txtOffset = null;
	private Button cmdAddOffset = null;
	private Button cmdClearOffset = null;
	private Button cmdDeleteOffsetLine = null;
	private Combo cmbOffset = null;
	private Table tblOffsets = null;
	
	private Account account = null;
	private AccountHash actHash = null;
	private DataHandler dataHandler = new DataHandler();
	
	private Transaction transaction = null;
	private int transactionMode = MM.TRANSACTION_MODE_AUTOCOMMIT;
	
	
	public TransactionDialog() {
		super();
		transaction = new Transaction();
	}
	
	public TransactionDialog(Account act){
		super();
		account = act;
		transaction = new Transaction();
	}

	public TransactionDialog(Account act, Transaction t){
		
		account = act;
		transaction = t;
		
	}
	
	@SuppressWarnings("rawtypes")
	protected void LoadUI(Shell sh) {
		lblAccount = new Label(sh, SWT.NONE);
		lblAmount = new Label(sh, SWT.NONE);
		lblAmount.setText(MM.PHRASES.getPhrase("55"));
		lblMemo = new Label(sh, SWT.NONE);
		lblMemo.setText(MM.PHRASES.getPhrase("41"));
		lblDate = new Label(sh, SWT.NONE);
		lblDate.setText(MM.PHRASES.getPhrase("24") + " (mm-dd-yyyy)");
		txtAmount = new Text(sh, SWT.BORDER);
		txtMemo = new Text(sh, SWT.BORDER);
		cmdCommit = new Button(sh, SWT.PUSH);
		cmdCommit.setText(MM.PHRASES.getPhrase("14"));
		cmdCommit.addSelectionListener(cmdCommit_OnClick);
		cmdClose = new Button(sh, SWT.PUSH);
		cmdClose.setText(MM.PHRASES.getPhrase("54"));
		cmdClose.addSelectionListener(cmdClose_OnClick);
		cmdDatePicker = new Button(sh, SWT.PUSH);
		cmdDatePicker.setImage(InfinityPfm.imMain.getImage((MM.IMG_CALENDAR)));
		cmdDatePicker.addSelectionListener(cmdDatePicker_OnClick);
		
		txtDate = new Text(sh, SWT.BORDER);
		
		if (transactionMode == MM.TRANSACTION_MODE_NOCOMMIT) {
			txtDate.setText(transaction.getTranDateFmt());
			txtMemo.setText(transaction.getTranMemo());
			txtAmount.setText(formatter.getAmountFormatted(transaction.getTranAmount()));
		} else {
			formatter.setDate(new Date());
			txtDate.setText(formatter.getFormat(DataFormatUtil.DefaultDateFormat));
			txtAmount.setText("0.0");
		}
		
		cmpOffset = new Composite(sh, SWT.BORDER);
		cmpOffset.setLayout(new FormLayout());
		lblOffset = new Label(cmpOffset, SWT.NONE);
		lblOffset.setText(MM.PHRASES.getPhrase("42"));
		lblOffsetAmount = new Label(cmpOffset, SWT.NONE);
		lblOffsetAmount.setText(MM.PHRASES.getPhrase("55"));
		txtOffset = new Text(cmpOffset, SWT.BORDER);
		txtOffset.setText("0.0");
		lblExRate = new Label(cmpOffset, SWT.NONE);
		lblExRate.setText(MM.PHRASES.getPhrase("211"));
		txtExRate = new Text(cmpOffset, SWT.BORDER);
		txtExRate.setEnabled(false);
		cmbOffset = new Combo(cmpOffset, SWT.BORDER | SWT.READ_ONLY);
		cmbOffset.addSelectionListener(cmbOffset_OnChange);
		cmdAddOffset = new Button(cmpOffset, SWT.PUSH);
		cmdAddOffset.setText(MM.PHRASES.getPhrase("45"));
		cmdAddOffset.addSelectionListener(cmdAddOffset_OnClick);
		cmdClearOffset = new Button(cmpOffset, SWT.PUSH);
		cmdClearOffset.setText(MM.PHRASES.getPhrase("66"));
		cmdClearOffset.addSelectionListener(cmdClearOffset_OnClick);
		cmdDeleteOffsetLine = new Button(cmpOffset, SWT.PUSH);
		cmdDeleteOffsetLine.setText(MM.PHRASES.getPhrase("236"));
		cmdDeleteOffsetLine.addSelectionListener(cmdDeleteOffsetLine_OnClick);
		tblOffsets = new Table(cmpOffset, SWT.MULTI | SWT.HIDE_SELECTION);
		tblOffsets.setLinesVisible(true);
		
		LoadColumns();
		
		if (account!=null){
			lblAccount.setText(MM.PHRASES.getPhrase("93") +
					": " + account.getActName());
		};
		
		//load offset accounts
		try {
			Account act = null;
			java.util.List actList = MM.sqlMap.queryForList("getAllAccountsByType", null);
			actHash = new AccountHash(actList);
			
			if (actList !=null){
				
				for (int i=0; i<actList.size(); i++){
					
					act = (Account)actList.get(i);
					if (act != null){
						if (!act.getActName().equals(account.getActName())) {
							cmbOffset.add(act.getActName());
						}
					}
				}
			}
			
		//load offsets stored with the transactions
		if (transaction.getOffsets() != null){
			
			for (int i=0; i<transaction.getOffsets().size(); i++){
				
				TransactionOffset offset = (TransactionOffset)transaction.getOffsets().get(i);
				TableItem ti = new TableItem(tblOffsets, SWT.NONE);
				
				if (offset.getOffsetName() == null && offset.getOffsetId()>0){
					//Offset has the ID but is missing the name.  Go get it.
					Transaction param = new Transaction();
					param.setActId(offset.getOffsetId());
					Account offsetAccount = (Account) MM.sqlMap.queryForObject("getAccountById", param);
					
					if (offsetAccount != null){
						
						offset.setOffsetName(offsetAccount.getActName());
						
					}
				}
				
				ti.setText(0, offset.getOffsetName());
				
				if (offset.getOffsetAmount()==0){
					ti.setText(1, formatter.getAmountFormatted(transaction.getTranAmount()*-1));
				} else {
					ti.setText(1, formatter.getAmountFormatted(offset.getOffsetAmount()));
				}
				ti.setText(2, transaction.getExchangeRate());
				
			}
		}
			
		} catch (SQLException se){
			InfinityPfm.LogMessage(se.getMessage());
		}
	}

	protected void LoadLayout() {
		
		FormData lblaccountdata = new FormData();
		lblaccountdata.top = new FormAttachment(0, 20);
		lblaccountdata.left = new FormAttachment(0, 20);
		lblAccount.setLayoutData(lblaccountdata);
		
		FormData lblmemotdata = new FormData();
		lblmemotdata.top = new FormAttachment(lblAccount, 15);
		lblmemotdata.left = new FormAttachment(0, 30);
		lblMemo.setLayoutData(lblmemotdata);
		
		FormData txtmemotdata = new FormData();
		txtmemotdata.top = new FormAttachment(lblAccount, 15);
		txtmemotdata.left = new FormAttachment(lblMemo, 90);
		txtmemotdata.right = new FormAttachment(100, -20);
		txtMemo.setLayoutData(txtmemotdata);
		
		FormData lblamountdata = new FormData();
		lblamountdata.top = new FormAttachment(lblMemo, 15);
		lblamountdata.left = new FormAttachment(0, 30);
		lblAmount.setLayoutData(lblamountdata);
		
		FormData txtamountdata = new FormData();
		txtamountdata.top = new FormAttachment(lblMemo, 15);
		txtamountdata.left = new FormAttachment(lblMemo, 90);
		txtamountdata.right = new FormAttachment(100, -150);
		txtAmount.setLayoutData(txtamountdata);

		FormData lbldatedata = new FormData();
		lbldatedata.top = new FormAttachment(lblAmount, 15);
		lbldatedata.left = new FormAttachment(0, 30);
		lblDate.setLayoutData(lbldatedata);
		
		FormData cmbdatedata = new FormData();
		cmbdatedata.top = new FormAttachment(lblAmount, 15);
		cmbdatedata.left = new FormAttachment(lblMemo, 90);
		cmbdatedata.right = new FormAttachment(100, -200);
		txtDate.setLayoutData(cmbdatedata);
		
		FormData cmddatepickerdata = new FormData();
		cmddatepickerdata.top = new FormAttachment(lblAmount, 13);
		cmddatepickerdata.left = new FormAttachment(txtDate, 0);
		//cmddatepickerdata.right = new FormAttachment(100, -100);
		cmdDatePicker.setLayoutData(cmddatepickerdata);
		
		FormData cmpoffsetdata = new FormData();
		cmpoffsetdata.top = new FormAttachment(lblDate, 15);
		cmpoffsetdata.left = new FormAttachment(0, 30);
		cmpoffsetdata.right = new FormAttachment(100, -20);
		cmpoffsetdata.bottom = new FormAttachment(100, -60);
		cmpOffset.setLayoutData(cmpoffsetdata);
		
		FormData lbloffsetdata = new FormData();
		lbloffsetdata.top = new FormAttachment(0, 10);
		lbloffsetdata.left = new FormAttachment(0, 10);
		lblOffset.setLayoutData(lbloffsetdata);
	
		FormData cmboffsetdata = new FormData();
		cmboffsetdata.top = new FormAttachment(0, 10);
		cmboffsetdata.left = new FormAttachment(lblOffset, 10);
		cmboffsetdata.right = new FormAttachment(100, -100);
		cmbOffset.setLayoutData(cmboffsetdata);
		
		FormData lbloffsetamountdata = new FormData();
		lbloffsetamountdata.top = new FormAttachment(lblOffset, 10);
		lbloffsetamountdata.left = new FormAttachment(0, 10);
		lblOffsetAmount.setLayoutData(lbloffsetamountdata);
		
		FormData txtoffsetdata = new FormData();
		txtoffsetdata.top = new FormAttachment(lblOffset, 10);
		txtoffsetdata.left = new FormAttachment(lblOffset, 10);
		txtoffsetdata.right = new FormAttachment(100, -200);
		txtOffset.setLayoutData(txtoffsetdata);
		
		FormData lblexratedata = new FormData();
		lblexratedata.top = new FormAttachment(lblOffsetAmount, 10);
		lblexratedata.left = new FormAttachment(0, 10);
		lblExRate.setLayoutData(lblexratedata);
		
		FormData txtexratedata = new FormData();
		txtexratedata.top = new FormAttachment(lblOffsetAmount, 10);
		txtexratedata.left = new FormAttachment(lblOffset, 10);
		txtexratedata.right = new FormAttachment(100, -300);
		txtExRate.setLayoutData(txtexratedata);

		FormData cmdaddoffsetdata = new FormData();
		cmdaddoffsetdata.top = new FormAttachment(lblOffsetAmount, 10);
		cmdaddoffsetdata.left = new FormAttachment(txtExRate, 5);
		cmdAddOffset.setLayoutData(cmdaddoffsetdata);
		
		FormData cmdclearoffsetdata = new FormData();
		cmdclearoffsetdata.top = new FormAttachment(lblOffsetAmount, 10);
		cmdclearoffsetdata.left = new FormAttachment(cmdAddOffset, 5);
		cmdClearOffset.setLayoutData(cmdclearoffsetdata);
		
		FormData cmddeleteoffsetlinedata = new FormData();
		cmddeleteoffsetlinedata.top = new FormAttachment(lblOffsetAmount, 10);
		cmddeleteoffsetlinedata.left = new FormAttachment(cmdClearOffset, 5);
		cmdDeleteOffsetLine.setLayoutData(cmddeleteoffsetlinedata);
		
		FormData tbloffsetsdata = new FormData();
		tbloffsetsdata.top = new FormAttachment(lblExRate, 20);
		tbloffsetsdata.left = new FormAttachment(0, 10);
		tbloffsetsdata.right = new FormAttachment(100, -10);
		tbloffsetsdata.bottom = new FormAttachment(100, -10);
		tblOffsets.setLayoutData(tbloffsetsdata);
				
		FormData cmdcommitdata = new FormData();
		cmdcommitdata.bottom = new FormAttachment(100, -10);
		cmdcommitdata.left = new FormAttachment(40, 0);
		cmdCommit.setLayoutData(cmdcommitdata);
		
		FormData cmdclosedata = new FormData();
		cmdclosedata.bottom = new FormAttachment(100, -10);
		cmdclosedata.left = new FormAttachment(cmdCommit, 10);
		cmdClose.setLayoutData(cmdclosedata);
		
	}
	
private void LoadColumns() {
		
		TableColumn tc1 = new TableColumn(tblOffsets, SWT.CENTER);
		TableColumn tc2 = new TableColumn(tblOffsets, SWT.LEFT);
		TableColumn tc3 = new TableColumn(tblOffsets, SWT.LEFT);
		
		tc1.setText(MM.PHRASES.getPhrase("42"));
		tc2.setText(MM.PHRASES.getPhrase("55"));
		tc3.setText(MM.PHRASES.getPhrase("211"));
	
		tc1.setWidth(200);
		tc2.setWidth(150);
		tc3.setWidth(100);
	
		tblOffsets.setHeaderVisible(true);
	}
	
	public int Open() {
		super.Open();
		 shell.setText(MM.PHRASES.getPhrase("79") + " " +
				MM.APPTITLE);
	 
		shell.setSize(650, 435);
		this.CenterWindow();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		return 1;
	}

	private void ClearForm(){
		txtMemo.setText("");
		txtAmount.setText("0");
		tblOffsets.removeAll();
		txtOffset.setText("0");
	}
	
	private boolean OffsetValidate() {
		
		boolean result = false;
		
		try {
		
			if (txtOffset.getText().length()>0) {
				float amount = new Float(txtOffset.getText());
				if (amount != 0){
					if (cmbOffset.getText().length() > 0) {
						amount = new Float(txtExRate.getText());
						if (txtExRate.getText().length() > 0){
							if (amount != 0){
								result = true;
							}
						}
					}
				}
			}
		
		}catch (Exception e) {
			result = false;
		}
		
		return result;
	}
	
	public int getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(int transactionMode) {
		this.transactionMode = transactionMode;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/*
	 * Listeners
	 */
	SelectionAdapter cmdClose_OnClick = new SelectionAdapter(){
		
		public void widgetSelected(SelectionEvent event) {
			transaction = null;
			shell.dispose();
		}
	};
	
	SelectionAdapter cmdCommit_OnClick = new SelectionAdapter(){
		public void widgetSelected(SelectionEvent event) {
			
			java.util.Date dt = null;
			
			transaction = new Transaction();
			transaction.setActId(account.getActId());
			transaction.setTranAmount(DataFormatUtil.moneyToLong(txtAmount.getText()));
			transaction.setExchangeRate(txtExRate.getText());
			transaction.setTranMemo(txtMemo.getText());
			
			ArrayList<TransactionOffset> offsets = new ArrayList<TransactionOffset>();
			
			TableItem[] rows = tblOffsets.getItems();
			TableItem row = null;
			long totalOffset = 0;
			
			for (int i=0; i<rows.length; i++){
				row = rows[i];
				Account act = actHash.get(row.getText(0));
				TransactionOffset offset = new TransactionOffset();
				offset.setOffsetId(act.getActId());
				offset.setOffsetName(act.getActName());
				offset.setOffsetAmount(DataFormatUtil.moneyToLong(row.getText(1)));
				totalOffset += offset.getOffsetAmount();
				offsets.add(offset);
			}
			
			// Abort if the offsets + transaction amount does not equal zero
			if ((totalOffset + transaction.getTranAmount()) != 0) {
				InfinityPfm.LogMessage(MM.PHRASES.getPhrase("234"), true);
				return;
			}
			
			transaction.setOffsets(offsets);
			
			DateFormat fmt = new SimpleDateFormat("M-dd-yyyy");
			
			try {
				dt=fmt.parse(txtDate.getText());
			} catch (ParseException pe){
				InfinityPfm.LogMessage(pe.getMessage());
				return;
			}
			
			transaction.setTranDate(dt);

			if (transactionMode == MM.TRANSACTION_MODE_AUTOCOMMIT){
				try {
					dataHandler.AddTransaction(transaction, false);
				} catch (SQLException e) {
					InfinityPfm.LogMessage(e.getMessage(), true);
				} catch (TransactionException e) {
					InfinityPfm.LogMessage(e.getMessage(), true);
				}
				ClearForm();
			} else {
				//Let the caller have the transaction.  Don't commit it.
				shell.dispose();
			}
			
		}
	};
	
	SelectionAdapter cmdDatePicker_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			
			if (dateDialog==null) 
				dateDialog = new DateDialog();
			
			dateDialog.Open();
			
			try {
				txtDate.setText(dateDialog.getSelectedDate());
			} catch (Exception err) {
				InfinityPfm.LogMessage(err.getMessage());
			}
		}
	};
	
	SelectionAdapter cmdAddOffset_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			
			if (OffsetValidate()){
				TableItem ti = new TableItem(tblOffsets, SWT.NONE);
				ti.setText(0, cmbOffset.getText());
				ti.setText(1, txtOffset.getText());
				ti.setText(2, txtExRate.getText());
				
				cmbOffset.clearSelection();
				txtOffset.setText("0");
				txtExRate.setText("1");
			}
		}
	};
	
	SelectionAdapter cmdClearOffset_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			
			tblOffsets.removeAll();
		}
	};
	
	SelectionAdapter cmdDeleteOffsetLine_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			int row = tblOffsets.getSelectionIndex();
			if (row > -1) tblOffsets.remove(row);
		}
	};
	
	SelectionAdapter cmbOffset_OnChange = new SelectionAdapter(){
		public void widgetSelected(SelectionEvent e){
			
			try {
				Account act = (Account)MM.sqlMap.queryForObject("getAccountForName", cmbOffset.getText());
				
				if (!act.getIsoCode().equals(account.getIsoCode())){
					
					String rate = dataHandler.getExchangeRate(account, act);
					txtExRate.setText(rate);
					txtExRate.setEnabled(rate != null);
					
				} else {
					txtExRate.setEnabled(false);
					txtExRate.setText("1");
				}
				
				long ammount = DataFormatUtil.moneyToLong(txtAmount.getText());
				long toAmmount = DataFormatUtil.moneyToLong(txtOffset.getText());
				
				if (ammount != 0 && toAmmount==0) {
					txtOffset.setText(formatter.getAmountFormatted(ammount *-1, NumberFormat.getNoParens()));
				}
				
			} catch (SQLException e1) {
				InfinityPfm.LogMessage(e1.getMessage());
			}
			
		}
	};
	
}
