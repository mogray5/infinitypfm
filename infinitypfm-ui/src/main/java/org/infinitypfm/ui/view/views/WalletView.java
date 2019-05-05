/*
 * Copyright (c) 2005-2019 Wayne Gray All rights reserved
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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Transaction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.bitcoin.wallet.WalletEvents;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.CurrencyMethod;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.ParamDateRangeAccount;
import org.infinitypfm.currency.RateParser;
import org.infinitypfm.data.DataHandler;
import org.infinitypfm.ui.view.dialogs.MessageDialog;
import org.infinitypfm.ui.view.toolbars.WalletToolbar;

public class WalletView extends BaseView implements WalletEvents {

	private WalletToolbar tbMain = null;
	private Composite cmpHeader = null;
	private Canvas bcCanvas = null;
	private Image bcLogo = null;
	private Color color = null;
	private Label lblAmount = null;
	private Label lblAmountBsv = null;
	private TabFolder tabFolder = null;
	private Table tblHistory = null;
	
	private Label lblSendTo = null;
	private Text txtSendTo = null;
	private Button cmdSend = null;
	
	private Image imgRcvQrCode = null;
	private Label lblRcvAddress =  null;
	private Canvas qrCanvas = null;
	private Button cmdClipBoard = null;
	
	private Label lblOffset = null;
	private Combo cmbOffset = null;
	
	private Label lblMemo = null;
	private Text txtMemo = null;
	
	private Label lblSendAmount = null;
	private Text txtSendAmount = null;
	private Combo cmbSendAmountIso = null;
	
	private String _receiveAddress;
	private Account bsvAccount = null;
	private Currency _bsvCurrency;
	private DataFormatUtil _format;
	
	private boolean _inSend = false;
	private List<Link> _links = null;
	
	public WalletView(Composite arg0, int arg1) {
		super(arg0, arg1);
	
		_links = new ArrayList<Link>();
		
		if (MM.wallet != null) {
			MM.wallet.registerForEvents(this);
			_receiveAddress = MM.wallet.getCurrentReceivingAddress();
		}
		_format = new DataFormatUtil();
		refreshExchangeRate();
		
		try {
			bsvAccount = (Account) MM.sqlMap.queryForObject("getAccountForName", "Bitcion SV Wallet");
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		
		LoadUI();
		LoadLayout();
		LoadColumns();
	}

	@Override
	protected void LoadUI() {
		
		color = new Color(InfinityPfm.shMain.getDisplay(), 255,255,255);
		tbMain = new WalletToolbar(this);
		cmpHeader = new Composite(this, SWT.BORDER);
		cmpHeader.setLayout(new FormLayout());
		cmpHeader.setBackground(color);
		bcLogo = InfinityPfm.imMain.getTransparentImage(MM.IMG_BSV_LOGO);
		bcCanvas = new Canvas(cmpHeader, SWT.NONE);
		bcCanvas.setBackground(color);
		bcCanvas.addPaintListener(logo_OnPaint);
		
		lblAmount = new Label(cmpHeader, SWT.NONE);
		lblAmountBsv = new Label(this, SWT.NONE);
		this.setFiatAndBsvBalance();
		
		FontData[] fD = lblAmount.getFont().getFontData();
		fD[0].setHeight(70);
		lblAmount.setFont( new Font(InfinityPfm.shMain.getDisplay(),fD[0]));
		
		FontData[] fe = lblAmountBsv.getFont().getFontData();
		fe[0].setHeight(8);
		lblAmountBsv.setFont( new Font(InfinityPfm.shMain.getDisplay(),fe[0]));
		
		tabFolder = new TabFolder(this, SWT.BORDER);
		TabItem sendItem = new TabItem(tabFolder, SWT.NONE);
		TabItem receiveItem = new TabItem(tabFolder, SWT.NONE);
		
		Group sendGroup = new Group(tabFolder, SWT.NONE);
		Group receiveGroup = new Group(tabFolder, SWT.NONE);
		
		sendGroup.setLayout(new FormLayout());
		receiveGroup.setLayout(new FormLayout());

		sendItem.setText(MM.PHRASES.getPhrase("263"));
		receiveItem.setText(MM.PHRASES.getPhrase("264"));
		
		sendItem.setControl(sendGroup);
		receiveItem.setControl(receiveGroup);

		tblHistory = new Table(this, SWT.BORDER);
		//tblHistory.setLinesVisible(true);
		
		lblSendTo = new Label(sendGroup, SWT.NONE);
		FontData[] fD2 = lblSendTo.getFont().getFontData();
		fD2[0].setHeight(40);
		lblSendTo.setFont(new Font(InfinityPfm.shMain.getDisplay(),fD2[0]));
		lblSendTo.setText(MM.PHRASES.getPhrase("265") + ":");
		
		txtSendTo = new Text(sendGroup, SWT.BORDER);
		FontData[] fD3 = txtSendTo.getFont().getFontData();
		fD3[0].setHeight(15);
		txtSendTo.setFont(new Font(InfinityPfm.shMain.getDisplay(), fD3[0]));

		cmdSend = new Button(sendGroup, SWT.PUSH);
		cmdSend.setImage(InfinityPfm.imMain.getImage(MM.IMG_BSV));
		cmdSend.addSelectionListener(cmdSend_OnClick);
		cmdSend.setToolTipText(MM.PHRASES.getPhrase("263"));
		
		lblOffset = new Label(sendGroup, SWT.NONE);
		lblOffset.setText(MM.PHRASES.getPhrase("273") + ":");
		cmbOffset = new Combo(sendGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		LoadAccountCombos();
		
		lblMemo = new Label(sendGroup, SWT.NONE);
		lblMemo.setText(MM.PHRASES.getPhrase("41") + ":");
		
		txtMemo = new Text(sendGroup, SWT.BORDER);
		
		lblRcvAddress = new Label(receiveGroup, SWT.NONE);
		FontData[] fD4 = lblRcvAddress.getFont().getFontData();
		fD4[0].setHeight(15);
		lblRcvAddress.setFont(new Font(InfinityPfm.shMain.getDisplay(),fD4[0]));
		
		if (_receiveAddress != null)
			lblRcvAddress.setText(_receiveAddress);
		
		lblSendAmount = new Label(sendGroup, SWT.NONE);
		lblSendAmount.setText(MM.PHRASES.getPhrase("55") + ":");
		txtSendAmount = new Text(sendGroup, SWT.BORDER);
		cmbSendAmountIso = new Combo(sendGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		this.loadSendIsoCombo();
		
		cmdClipBoard = new Button(receiveGroup, SWT.PUSH);
		cmdClipBoard.setImage(InfinityPfm.imMain.getImage(MM.IMG_CLIPBOARD));
		cmdClipBoard.addSelectionListener(cmdClipBoard_OnClick);
		
		try {
			if (_receiveAddress != null) {
				File qrImage = MM.wallet.getQrCode(_receiveAddress);
				if (qrImage.exists()) {
					imgRcvQrCode = InfinityPfm.imMain.getTransparentImage(qrImage);
					qrCanvas = new Canvas(receiveGroup, SWT.BORDER);
					qrCanvas.setBackground(color);
					qrCanvas.addPaintListener(logo_OnPaintQr);
				}
			}
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		
		// Set tab order
		sendGroup.setTabList(new Control[] {txtSendTo, cmdSend, txtSendAmount, cmbSendAmountIso, txtMemo, cmbOffset});
		receiveGroup.setTabList(new Control[] {cmdClipBoard});
		tabFolder.setTabList(new Control[] {sendGroup, receiveGroup});
		this.setTabList(new Control[] {tabFolder});
		

		LoadTransactionHistoryAsync();
		
		// Load transactions table after a pause to work around a timing issue with rendering. 
//		Display.getDefault().asyncExec( new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(1000);
//					LoadTransactionHistory();
//				} catch (InterruptedException e) {
//					InfinityPfm.LogMessage(e.getMessage());
//				}
//			}
//		} );
	}
	
	protected void LoadLayout() {
		
		FormData tbmaindata = new FormData();
		tbmaindata.right = new FormAttachment(100, 0);
		tbMain.setLayoutDat(tbmaindata);
		
		FormData cmpheaderdata = new FormData();
		cmpheaderdata.top = new FormAttachment(0, 27);
		cmpheaderdata.left = new FormAttachment(0, 10);
		cmpheaderdata.right = new FormAttachment(100, -15);
		cmpheaderdata.bottom = new FormAttachment(0, 160);
		cmpHeader.setLayoutData(cmpheaderdata);

		FormData logodata = new FormData();
		logodata.top = new FormAttachment(0, 0);
		logodata.left = new FormAttachment(100, -150);
		logodata.right = new FormAttachment(100, 0);
		logodata.bottom = new FormAttachment(100, 0);
		bcCanvas.setLayoutData(logodata);
	
		FormData lblamountdata = new FormData();
		lblamountdata.top = new FormAttachment(10, 0);
		lblamountdata.left = new FormAttachment(5, 0);
		lblamountdata.right = new FormAttachment(60, 0);
		lblamountdata.bottom = new FormAttachment(100, 0);
		lblAmount.setLayoutData(lblamountdata);
		
		FormData lblamountbsvdata = new FormData();
		lblamountbsvdata.top = new FormAttachment(cmpHeader, 5);
		lblamountbsvdata.left = new FormAttachment(100, -127);
		//lblamountbsvdata.right = new FormAttachment(60, 0);
		//lblamountbsvdata.bottom = new FormAttachment(100, 0);
		lblAmountBsv.setLayoutData(lblamountbsvdata);
		
		FormData tabfolderdata = new FormData();
		tabfolderdata.top = new FormAttachment(cmpHeader, 10);
		tabfolderdata.left = new FormAttachment(0, 20);
		tabfolderdata.right = new FormAttachment(100, -10);
		tabfolderdata.bottom = new FormAttachment(cmpHeader, 450);
		tabFolder.setLayoutData(tabfolderdata);
		
		FormData tblhistorydata = new FormData();
		tblhistorydata.top = new FormAttachment(tabFolder, 10);
		tblhistorydata.left = new FormAttachment(0, 20);
		tblhistorydata.right = new FormAttachment(100, -10);
		tblhistorydata.bottom = new FormAttachment(100, -20);
		tblHistory.setLayoutData(tblhistorydata);
		
		FormData lblsentodata = new FormData();
		lblsentodata.top = new FormAttachment(0, 35);
		lblsentodata.left = new FormAttachment(0, 40);
		//lblsentodata.right = new FormAttachment(100, -10);
		//lblsentodata.bottom = new FormAttachment(100, -20);
		lblSendTo.setLayoutData(lblsentodata);

		FormData txtsendtodata = new FormData();
		txtsendtodata.top = new FormAttachment(0, 50);
		txtsendtodata.left = new FormAttachment(lblSendTo, 20);
		txtsendtodata.right = new FormAttachment(lblSendTo, 550);
		//txtsendtodata.bottom = new FormAttachment(100, -20);
		txtSendTo.setLayoutData(txtsendtodata);
		
		FormData cmdsenddata = new FormData();
		cmdsenddata.top = new FormAttachment(0, 48);
		cmdsenddata.left = new FormAttachment(txtSendTo, 5);
		//cmdsenddata.right = new FormAttachment(lblSendTo, 150);
		//cmdsenddata.bottom = new FormAttachment(100, -20);
		cmdSend.setLayoutData(cmdsenddata);
	
		FormData lblsendamountdata = new FormData();
		lblsendamountdata.top = new FormAttachment(lblSendTo, 0);
		lblsendamountdata.left = new FormAttachment(0, 70);
		lblSendAmount.setLayoutData(lblsendamountdata);
		
		FormData txtsendamountdata = new FormData();
		txtsendamountdata.top = new FormAttachment(lblSendTo, -6);
		txtsendamountdata.left = new FormAttachment(lblSendAmount, 5);
		txtsendamountdata.right = new FormAttachment(lblSendAmount, 300);
		txtSendAmount.setLayoutData(txtsendamountdata);
		
		FormData cmbsendamountisodata = new FormData();
		cmbsendamountisodata.top = new FormAttachment(lblSendTo, 0);
		cmbsendamountisodata.left = new FormAttachment(txtSendAmount, 5);
		//cmbsendamountisodata.right = new FormAttachment(65, 0);
		cmbSendAmountIso.setLayoutData(cmbsendamountisodata);
		
		FormData lblmemotdata = new FormData();
		lblmemotdata.top = new FormAttachment(lblSendAmount, 20);
		lblmemotdata.left = new FormAttachment(0, 70);
		lblMemo.setLayoutData(lblmemotdata);
		
		FormData txtmemotdata = new FormData();
		txtmemotdata.top = new FormAttachment(lblSendAmount, 18);
		txtmemotdata.left = new FormAttachment(lblSendAmount, 5);
		txtmemotdata.right = new FormAttachment(65, 0);
		txtMemo.setLayoutData(txtmemotdata);
		
		FormData lbloffsetdata = new FormData();
		lbloffsetdata.top = new FormAttachment(lblMemo, 27);
		lbloffsetdata.left = new FormAttachment(0, 70);
		lblOffset.setLayoutData(lbloffsetdata);

		FormData cmboffsetdata = new FormData();
		cmboffsetdata.top = new FormAttachment(lblMemo, 22);
		cmboffsetdata.left = new FormAttachment(lblSendAmount, 5);
		//cmboffsetdata.right = new FormAttachment(lblAccount, 300);
		cmbOffset.setLayoutData(cmboffsetdata);
		
		FormData qrcanvasdata = new FormData();
		qrcanvasdata.top = new FormAttachment(0, 0);
		qrcanvasdata.left = new FormAttachment(0, 0);
		qrcanvasdata.right = new FormAttachment(100, 0);
		qrcanvasdata.bottom = new FormAttachment(100, 0);
		
		if (qrCanvas != null)
			qrCanvas.setLayoutData(qrcanvasdata);
		
		FormData lblrcvaddressdata = new FormData();
		lblrcvaddressdata.top = new FormAttachment(0, 85);
		lblrcvaddressdata.left = new FormAttachment(0, 250);
		//lblrcvaddressdata.right = new FormAttachment(100, 0);
		//lblrcvaddressdata.bottom = new FormAttachment(100, 0);
		lblRcvAddress.setLayoutData(lblrcvaddressdata);
	
		FormData cmdclipboarddata = new FormData();
		cmdclipboarddata.top = new FormAttachment(0, 83);
		cmdclipboarddata.left = new FormAttachment(lblRcvAddress, 10);
		//cmdclipboarddata.right = new FormAttachment(100, 0);
		//cmdclipboarddata.bottom = new FormAttachment(100, 0);
		cmdClipBoard.setLayoutData(cmdclipboarddata);

	}
	
	private void LoadColumns() {

		TableColumn tc1 = new TableColumn(tblHistory, SWT.CENTER);
		TableColumn tc2 = new TableColumn(tblHistory, SWT.CENTER);
		TableColumn tc3 = new TableColumn(tblHistory, SWT.CENTER);
		TableColumn tc4 = new TableColumn(tblHistory, SWT.CENTER);
		TableColumn tc5 = new TableColumn(tblHistory, SWT.CENTER);
		TableColumn tc6 = new TableColumn(tblHistory, SWT.CENTER);

		tc1.setText(MM.PHRASES.getPhrase("24"));
		tc2.setText(MM.PHRASES.getPhrase("41"));
		tc3.setText(MM.PHRASES.getPhrase("48"));
		tc4.setText(MM.PHRASES.getPhrase("46"));
		tc5.setText(MM.PHRASES.getPhrase("2"));
		tc6.setText(MM.PHRASES.getPhrase("86"));

		tc1.setWidth(150);
		tc2.setWidth(250);
		tc3.setWidth(150);
		tc4.setWidth(150);
		tc5.setWidth(150);
		tc6.setWidth(80);

		tblHistory.setHeaderVisible(true);
	}
	
	protected void LoadTransactionHistoryAsync() {
		
		// Load transactions table after a pause to work around a timing issue with rendering. 
		Display.getDefault().asyncExec( new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					LoadTransactionHistory();
				} catch (InterruptedException e) {
					InfinityPfm.LogMessage(e.getMessage());
				}
			}
		} );
		
	}
	
	@SuppressWarnings("unchecked")
	protected void LoadTransactionHistory() {
		
		tblHistory.removeAll();
		ParamDateRangeAccount range = new ParamDateRangeAccount();
		LocalDateTime today = LocalDateTime.now();
		range.setEndDate(Timestamp.valueOf(today));
		range.setStartDate(Timestamp.valueOf(today.minusDays(30)));
		range.setActId(bsvAccount.getActId());
		TableItem ti = null;
		final Display display = InfinityPfm.shMain.getDisplay();
		int rowCount = 0;
		try {
			List<org.infinitypfm.core.data.Transaction> tranList = MM.sqlMap.queryForList("getTransactionsForRangeAndAccountReverseSort", range);
			if (tranList != null && tranList.size() > 0) {
				
				_format.setPrecision(bsvAccount.getCurrencyPrecision());
				
				// Dispose previous links
				for (int i=0; i<_links.size(); i++) {
					Link oldLink = (Link)_links.get(i);
					try {
						oldLink.dispose();
					} catch (Exception e) {
						InfinityPfm.LogMessage(e.getMessage());
					}
				}
				
				_links.clear();
				
				//Loop through list and calculate account balance
				ListIterator li = tranList.listIterator(0);
				long newBalance = bsvAccount.getActBalance();
				org.infinitypfm.core.data.Transaction tran = null;
				
				while(li.hasNext()) {
				  tran = (org.infinitypfm.core.data.Transaction)li.next();
				  tran.setActBalance(newBalance);
				  newBalance-=tran.getTranAmount();
				}
				
				for (org.infinitypfm.core.data.Transaction t : tranList) {
					
					ti = new TableItem(tblHistory, SWT.NONE);
					if (rowCount % 2D == 0) {
						for (int i=0; i< 6; i++ ) 
							ti.setBackground(i,display.getSystemColor(MM.ROW_BACKGROUND));
						
					}
					
					//Date, Memo, Debit, Credit, Balance, View
					ti.setText(0, t.getTranDateFmt());
					ti.setText(1, t.getTranMemo());
					if (t.getTranAmount() > 0) 
						ti.setText(2, _format.getAmountFormatted(Math.abs(t.getTranAmount())));
					else
						ti.setText(3, _format.getAmountFormatted(Math.abs(t.getTranAmount())));
					
					ti.setText(4, _format.getAmountFormatted(t.getActBalance()));
					
					if (t.getTransactionKey()!=null) {
						TableEditor editor = new TableEditor(tblHistory);
						Link link = new Link(tblHistory, SWT.NONE);
						link.setText("<a href=\"https://eclipse.org\">" + MM.PHRASES.getPhrase("283") + "</a>");
						//link.setText(MM.PHRASES.getPhrase("86"));
						link.addSelectionListener(linkView_OnClick);
						link.setEnabled(true);
						link.setData(t);
						_links.add(link);
						editor.minimumWidth = 50;
						editor.horizontalAlignment = SWT.CENTER;
						editor.setEditor(link, ti, 5);
					}
					
					rowCount++;
					tblHistory.redraw();
				}
			}
				
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
	}
	
	private void LoadAccountCombos() {

		try {
			Account act = null;
			@SuppressWarnings("rawtypes")
			java.util.List list = MM.sqlMap.queryForList(
					"getAllAccountsByType", null);
 
			cmbOffset.removeAll();

			for (int i = 0; i < list.size(); i++) {
				act = (Account) list.get(i);
				cmbOffset.add(act.getActName());
				cmbOffset.setData(act.getActName(), act);
			}

		} catch (SQLException se) {
			InfinityPfm.LogMessage(se.getMessage());
		}
	}
	
	private void refreshExchangeRate() {
		
		CurrencyMethod method = new CurrencyMethod();
		method.setCurrencyID(MM.options.getDefaultBsvCurrencyID());
		method.setMethodName(MM.options.getDefaultBsvCurrencyMethod());

		try {
			method = (CurrencyMethod) MM.sqlMap.queryForObject(
					"getCurrencyMethod", method);
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		
		String newRate = RateParser.getRate(method);
		
		if (newRate != null) {
			
			try {
			
				Currency currency = (Currency) MM.sqlMap.queryForObject("getCurrencyById", MM.options.getDefaultBsvCurrencyID());
				currency.setExchangeRate(newRate);
				currency.setLastUpdate(new Date());
				
				MM.sqlMap.update("updateExchangeRate", currency);
				_bsvCurrency = currency;
				
			} catch (SQLException e) {
				InfinityPfm.LogMessage(e.getMessage());
			}
		}
	}
	
	/**
	 * Set both BSV balance and fiat balance using exchange rate stored
	 * in _bsvCurrency
	 * @param bsvBalance Wallet balance in BSV
	 */
	private void setFiatAndBsvBalance(String bsvBalance) {
		
		lblAmountBsv.setText(bsvBalance + " BSV");
		if (bsvBalance != null) {
			BigDecimal amount = _format.strictMultiply(bsvBalance, _bsvCurrency.getExchangeRate());
			BigDecimal bsvAmount = new BigDecimal(bsvBalance);
			long lbsvAmount = DataFormatUtil.moneyToLong(bsvAmount);
			bsvAccount.setActBalance(lbsvAmount);
			long lAmount = DataFormatUtil.moneyToLong(amount);
			_format.setPrecision(MM.options.getCurrencyPrecision());
			String fiatBalance = _format.getAmountFormatted(lAmount);
			lblAmount.setText("$" + fiatBalance);
			try {
				MM.sqlMap.update("updateAccountBalanceFromAccount", bsvAccount);
			} catch (SQLException e) {
				InfinityPfm.LogMessage(e.getMessage());
			}
		}
	}
	
	/**
	 * Ask the wallet kit for the BSV balance and 
	 * pass it to overload for formatting.
	 */
	private void setFiatAndBsvBalance() {
		if (MM.wallet != null) 
			setFiatAndBsvBalance( MM.wallet.getBsvBalance());
	}
	
	private void loadSendIsoCombo() {
		
		try {
			Currency c = (Currency) MM.sqlMap.queryForObject("getCurrencyById", Long.valueOf(MM.options.getDefaultCurrencyID()).longValue() );
			
			if (c != null) {
				cmbSendAmountIso.add(c.getIsoName());
				cmbSendAmountIso.setData(c.getIsoName(), c.getCurrencyID());
			}
			
			cmbSendAmountIso.add("BSV");
			cmbSendAmountIso.setData("BSV", MM.options.getDefaultBsvCurrencyID());
			
			cmbSendAmountIso.select(0);
		
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getLocalizedMessage());
		}
		
	}
	
	private boolean confirmSend(String amount, String iso, String address) {
		
		String confirmQuestion = StringUtils.join(new String[] {
				MM.PHRASES.getPhrase("276"),
				" ",
				MM.PHRASES.getPhrase("263"),
				" ",
				amount, 
				" ",
				iso,
				" ",
				MM.PHRASES.getPhrase("265"),
				" ",
				address,
				"?"});
		
		MessageDialog dlg = new MessageDialog(MM.DIALOG_QUESTION, MM.APPTITLE,
				confirmQuestion);
		dlg.setDimensions(400, 150);
		int iResult = dlg.Open();

		return iResult == MM.YES;
		
	}
	
	
	/*
	 * Listeners
	 */
	PaintListener logo_OnPaint = new PaintListener() {
        public void paintControl(PaintEvent e) {
         e.gc.drawImage(bcLogo,0,0);
        }
    };

	PaintListener logo_OnPaintQr = new PaintListener() {
        public void paintControl(PaintEvent e) {
         e.gc.drawImage(imgRcvQrCode,0,0);
        }
    };
    
	SelectionAdapter cmdClipBoard_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			
			String str = lblRcvAddress.getText();
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			StringSelection strSel = new StringSelection(str);
			clipboard.setContents(strSel, null);
		}
	};
	
	SelectionAdapter cmdSend_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			String sendTo = txtSendTo.getText();
			String memo = txtMemo.getText();
			String sAmount = txtSendAmount.getText();
			
			boolean canSend = true;
			
			if (sAmount == null || sAmount.length() ==0) canSend = false;
			if (sendTo == null || sAmount.length() ==0) canSend = false;
			
			if (!canSend) {
				InfinityPfm.LogMessage(MM.PHRASES.getPhrase("274"), true);
				return;
			}
			
			long selectedCurrency = (long) cmbSendAmountIso.getData(cmbSendAmountIso.getText());
			
			Coin amount = null;
			
			if (selectedCurrency == MM.options.getDefaultBsvCurrencyID()) 
				amount = Coin.parseCoin(txtSendAmount.getText());
			else {
				// Convert from Fiat to BSV before sending
				refreshExchangeRate();
				BigDecimal amountToSend = _format.strictDivide(sAmount, _bsvCurrency.getExchangeRate(), MM.MAX_PRECISION);
				amount = Coin.parseCoin(amountToSend.toString());
			}
			
			if (confirmSend(sAmount, cmbSendAmountIso.getText(), sendTo)) {
				
				// Send it!!
				try {
					_inSend = true;
					MM.wallet.sendCoins(sendTo, amount);
				} catch (AddressFormatException e1) {
					InfinityPfm.LogMessage(e1.getMessage(), true);
				} catch (InsufficientMoneyException e1) {
					InfinityPfm.LogMessage(e1.getMessage(), true);
				}
			}
			
		}
	};

	SelectionAdapter linkView_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
	
			String s = "stop";
			
			org.infinitypfm.core.data.Transaction t = 
					(org.infinitypfm.core.data.Transaction) e.widget.getData();
			
			if (t.getTransactionKey() != null) {
				String sLink = "https://whatsonchain.com/tx/" + t.getTransactionKey();
				Program.launch(sLink);
			}
		}
	};
	
	/*****************/
	/* Wallet Events */
	/*****************/

	@Override
	public void coinsReceived(Transaction tx, Coin value, Coin prevBalance, Coin newBalance) {
		
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
		
			if (_inSend) {
				_inSend = false;
				return;
			}
				
			Account offset = (Account) cmbOffset.getData(cmbOffset.getText());
			
			if (bsvAccount != null && offset != null) {
				org.infinitypfm.core.data.Transaction t = new org.infinitypfm.core.data.Transaction();
				t.setTranAmount(DataFormatUtil.moneyToLong(value.toPlainString()));
				t.setActId(bsvAccount.getActId());
				
				t.setActOffset(offset.getActId());
				t.setExchangeRate(_bsvCurrency.getExchangeRate());
				t.setTranDate(new Date());
				if (tx.getMemo() != null && tx.getMemo().length() > 0)
					t.setTranMemo(tx.getMemo());
				else
					t.setTranMemo(MM.PHRASES.getPhrase("282"));
				
				DataHandler handler = new DataHandler();
				try {
					handler.AddTransaction(t, false);
				} catch (Exception e) {
					InfinityPfm.LogMessage(e.getMessage());
				} 
			}
				setFiatAndBsvBalance(newBalance.toPlainString());
				LoadTransactionHistoryAsync();
			}
			
		});
	}

	@Override
	public void coinsSent(Transaction tx, Coin value, Coin prevBalance, Coin newBalance) {
		
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
		
			Account offset = (Account) cmbOffset.getData(cmbOffset.getText());
			
			if (bsvAccount != null && offset != null) {
				org.infinitypfm.core.data.Transaction t = new org.infinitypfm.core.data.Transaction();
				t.setTranAmount(DataFormatUtil.moneyToLong(value.toPlainString()));
				t.setActId(bsvAccount.getActId());
				t.setActOffset(offset.getActId());
				t.setExchangeRate(_bsvCurrency.getExchangeRate());
				t.setTranMemo(txtMemo.getText());
				t.setTransactionKey(tx.getHashAsString());
				t.setTranDate(new Date());
				
				DataHandler handler = new DataHandler();
				try {
					handler.AddTransaction(t, false);
					
				} catch (Exception e) {
					InfinityPfm.LogMessage(e.getMessage());
				} 
			}
				setFiatAndBsvBalance(newBalance.toPlainString());
				LoadTransactionHistoryAsync();
			}
		});
	}
}
