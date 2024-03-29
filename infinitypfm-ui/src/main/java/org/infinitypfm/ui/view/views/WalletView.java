/*
 * Copyright (c) 2005-2022 Wayne Gray All rights reserved
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.bitcoin.wallet.BsvWallet.WalletFunction;
import org.infinitypfm.bitcoin.wallet.WalletEvents;
import org.infinitypfm.bitcoin.wallet.exception.SendException;
import org.infinitypfm.bitcoin.wallet.exception.WalletException;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.conf.WalletAuth;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.CurrencyMethod;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.DigitalAssetTransaction;
import org.infinitypfm.core.data.ParamDateRangeAccount;
import org.infinitypfm.core.data.ReceivingAddress;
import org.infinitypfm.core.data.TransactionOffset;
import org.infinitypfm.core.exception.PasswordInvalidException;
import org.infinitypfm.currency.RateParser;
import org.infinitypfm.data.DataHandler;
import org.infinitypfm.graphics.ImageMap;
import org.infinitypfm.ui.view.dialogs.ImportDialog;
import org.infinitypfm.ui.view.dialogs.MessageDialog;
import org.infinitypfm.ui.view.toolbars.WalletToolbar;

public class WalletView extends BaseView implements WalletEvents {

	private WalletToolbar tbMain = null;
	private Composite cmpHeader = null;
	private Canvas bcCanvas = null;
	private Image bcLogo = null;
	private Color colorWhite = null;
	private Color colorFont = null;
	private Color addressFont = null;
	private Label lblAmount = null;
	private Label lblAmountBsv = null;
	private TabFolder tabFolder = null;
	private Table tblHistory = null;
	
	private Label lblSendTo = null;
	private Text txtSendTo = null;
	private Button cmdSend = null;
	
	private Image imgRcvQrCode = null;
	private Label lblRcvAddress =  null;
	private Label lblRcvPaymail = null;
	private Canvas qrCanvas = null;
	private Button cmdClipBoardAdr = null;
	private Button cmdClipBoardPaymail = null;
	
	private Label lblOffset = null;
	private Combo cmbOffset = null;
	
	private Label lblMemo = null;
	private Text txtMemo = null;
	
	private Label lblSendAmount = null;
	private Text txtSendAmount = null;
	private Combo cmbSendAmountIso = null;
	
	private ReceivingAddress _receiveAddress;
	private Account bsvAccount = null;
	private Account bsvCoinsReceived = null;
	private Currency _bsvCurrency;
	private DataFormatUtil _format;
	
	private boolean _inSend = false;
	private List<Link> _links = null;
	
	private DataFormatUtil _formatter = null;
	
	protected ImageMap imIcons = null;
	
	Cursor cursorHand = null;
	
	// Use to control when a history lookup is needed.
	// No need to fetch history again if the balance has not
	// changed.
	private String lastBsvBalance = null;
	
	public WalletView(Composite arg0, int arg1) throws PasswordInvalidException {
		super(arg0, arg1);
	
		_links = new ArrayList<Link>();
	
		imIcons = InfinityPfm.imMain;
		
		if (MM.wallet != null) {
			if (MM.wallet.isImplemented(WalletFunction.REGISTERFOREVENTS))
				MM.wallet.registerForEvents(this);
			if (MM.wallet.isImplemented(WalletFunction.CURRENTRECEIVINGADDRESS)) {
				
				try {
					// This will prompt user to log in the first time
					WalletAuth.getInstance().walletPassword();
					_receiveAddress = MM.wallet.getCurrentReceivingAddress();
				} catch (PasswordInvalidException e) {
					InfinityPfm.LogMessage(e.getMessage());
					throw e;
				}
				
			}
		}
		_format = new DataFormatUtil();
		
		Display.getDefault().asyncExec( new Runnable() {
			@Override
			public void run() {
				
				refreshExchangeRate();
				
				try {
					bsvAccount = (Account) MM.sqlMap.selectOne("getAccountForName", MM.BSV_WALLET_ACCOUNT);
				} catch (Exception e) {
					InfinityPfm.LogMessage(e.getMessage());
				}
				
				try {
					bsvCoinsReceived = (Account) MM.sqlMap.selectOne("getAccountForName", MM.BSV_WALLET_RECEIVING_ACCOUNT);
				} catch (Exception e) {
					InfinityPfm.LogMessage(e.getMessage());
				}	
				
			}
		} );
		
		_formatter = new DataFormatUtil();
		
		LoadUI();
		LoadLayout();
		LoadColumns();
	}

	@Override
	protected void LoadUI() {
		
		colorWhite = new Color(InfinityPfm.shMain.getDisplay(), 255,255,255);
		colorFont = new Color(InfinityPfm.shMain.getDisplay(), 14,46,6);
		addressFont = new Color(InfinityPfm.shMain.getDisplay(), 30,30,36);
		
		cursorHand = new Cursor(InfinityPfm.shMain.getDisplay(), SWT.CURSOR_HAND);
		
		tbMain = new WalletToolbar(this);
		cmpHeader = new Composite(this, SWT.BORDER);
		cmpHeader.setLayout(new FormLayout());
		cmpHeader.setBackground(colorWhite);
		bcLogo = imIcons.getTransparentImage(MM.IMG_BSV_LOGO);
		bcCanvas = new Canvas(cmpHeader, SWT.NONE);
		bcCanvas.setBackground(colorWhite);
		bcCanvas.addPaintListener(logo_OnPaint);
		
		lblAmount = new Label(cmpHeader, SWT.NONE);
		lblAmount.setBackground(colorWhite);
		lblAmount.setForeground(colorFont);
		lblAmount.setText("$0");
		lblAmountBsv = new Label(this, SWT.NONE);
		lblAmountBsv.setText("0.00000000 BSV");
		
		FontData[] fD = lblAmount.getFont().getFontData();
		fD[0].setHeight(45);
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

		sendItem.setText(MM.PHRASES.getPhrase("277"));
		receiveItem.setText(MM.PHRASES.getPhrase("278"));
		
		sendItem.setControl(sendGroup);
		receiveItem.setControl(receiveGroup);

		tblHistory = new Table(this, SWT.BORDER);
		//tblHistory.setLinesVisible(true);
		
		lblSendTo = new Label(sendGroup, SWT.NONE);
		FontData[] fD2 = lblSendTo.getFont().getFontData();
		fD2[0].setHeight(40);
		lblSendTo.setFont(new Font(InfinityPfm.shMain.getDisplay(),fD2[0]));
		lblSendTo.setText(MM.PHRASES.getPhrase("279") + ":");
		
		txtSendTo = new Text(sendGroup, SWT.BORDER);
		FontData[] fD3 = txtSendTo.getFont().getFontData();
		fD3[0].setHeight(15);
		txtSendTo.setFont(new Font(InfinityPfm.shMain.getDisplay(), fD3[0]));
		txtSendTo.setToolTipText(MM.PHRASES.getPhrase("318"));
		
		cmdSend = new Button(sendGroup, SWT.PUSH);
		cmdSend.setImage(imIcons.getImage(MM.IMG_BSV));
		
		if (MM.wallet.isImplemented(WalletFunction.SENDCOINS)) {
			cmdSend.addSelectionListener(cmdSend_OnClick);
		} else {
			cmdSend.setEnabled(false);
		}
		
		cmdSend.setToolTipText(MM.PHRASES.getPhrase("277"));
		
		lblOffset = new Label(sendGroup, SWT.NONE);
		lblOffset.setText(MM.PHRASES.getPhrase("286") + ":");
		cmbOffset = new Combo(sendGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		LoadAccountCombos();
		
		lblMemo = new Label(sendGroup, SWT.NONE);
		lblMemo.setText(MM.PHRASES.getPhrase("41") + ":");
		
		txtMemo = new Text(sendGroup, SWT.BORDER);
		
		lblRcvAddress = new Label(receiveGroup, SWT.NONE);
		FontData[] fD4 = lblRcvAddress.getFont().getFontData();
		fD4[0].setHeight(15);
		lblRcvAddress.setFont(new Font(InfinityPfm.shMain.getDisplay(),fD4[0]));
		lblRcvAddress.setBackground(colorWhite);
		lblRcvAddress.setForeground(addressFont);
		
		lblRcvPaymail = new Label(receiveGroup, SWT.NONE);
		lblRcvPaymail.setFont(new Font(InfinityPfm.shMain.getDisplay(),fD4[0]));
		lblRcvPaymail.setBackground(colorWhite);
		lblRcvPaymail.setForeground(addressFont);
		
		if (_receiveAddress != null) {
			lblRcvAddress.setText(_receiveAddress.getAddress());
			lblRcvPaymail.setText(_receiveAddress.getPaymail());
		}
		
		
		lblSendAmount = new Label(sendGroup, SWT.NONE);
		lblSendAmount.setText(MM.PHRASES.getPhrase("55") + ":");
		txtSendAmount = new Text(sendGroup, SWT.BORDER);
		cmbSendAmountIso = new Combo(sendGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		this.loadSendIsoCombo();
		
		cmdClipBoardAdr = new Button(receiveGroup, SWT.PUSH);
		cmdClipBoardAdr.setImage(imIcons.getImage(MM.IMG_CLIPBOARD));
		cmdClipBoardAdr.setBackground(colorWhite);
		cmdClipBoardAdr.addSelectionListener(cmdClipBoardAdr_OnClick);
		cmdClipBoardAdr.setToolTipText(MM.PHRASES.getPhrase("309"));
		
		cmdClipBoardPaymail = new Button(receiveGroup, SWT.PUSH);
		cmdClipBoardPaymail.setImage(imIcons.getImage(MM.IMG_CLIPBOARD));
		cmdClipBoardPaymail.setBackground(colorWhite);
		cmdClipBoardPaymail.addSelectionListener(cmdClipBoardPaymail_OnClick);
		cmdClipBoardPaymail.setToolTipText(MM.PHRASES.getPhrase("309"));
		
		try {
			if (_receiveAddress != null && MM.wallet.isImplemented(WalletFunction.GETQRCODE)) {
				File qrImage = MM.wallet.getQrCode(_receiveAddress.getAddress());
				if (qrImage.exists()) {
					imgRcvQrCode = imIcons.getTransparentImage(qrImage);
					qrCanvas = new Canvas(receiveGroup, SWT.BORDER);
					qrCanvas.setBackground(colorWhite);
					qrCanvas.addPaintListener(logo_OnPaintQr);
					
				}
			}
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		
		// Set tab order
		sendGroup.setTabList(new Control[] {txtSendTo, cmdSend, txtSendAmount, cmbSendAmountIso, txtMemo, cmbOffset});
		receiveGroup.setTabList(new Control[] {cmdClipBoardAdr, cmdClipBoardPaymail});
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
		cmpheaderdata.top = new FormAttachment(0, 35);
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
		lblamountdata.top = new FormAttachment(0, 30);
		lblamountdata.left = new FormAttachment(5, 0);
		lblamountdata.right = new FormAttachment(60, 0);
		lblamountdata.bottom = new FormAttachment(100, 0);
		lblAmount.setLayoutData(lblamountdata);
		
		FormData lblamountbsvdata = new FormData();
		lblamountbsvdata.top = new FormAttachment(cmpHeader, 0);
		lblamountbsvdata.left = new FormAttachment(100, -127);
		//lblamountbsvdata.right = new FormAttachment(60, 0);
		//lblamountbsvdata.bottom = new FormAttachment(100, 0);
		lblAmountBsv.setLayoutData(lblamountbsvdata);
		
		FormData tabfolderdata = new FormData();
		tabfolderdata.top = new FormAttachment(cmpHeader, 13);
		tabfolderdata.left = new FormAttachment(0, 20);
		tabfolderdata.right = new FormAttachment(100, -10);
		tabfolderdata.bottom = new FormAttachment(cmpHeader, 385);
		tabFolder.setLayoutData(tabfolderdata);
		
		FormData tblhistorydata = new FormData();
		tblhistorydata.top = new FormAttachment(tabFolder, 10);
		tblhistorydata.left = new FormAttachment(0, 20);
		tblhistorydata.right = new FormAttachment(100, -10);
		tblhistorydata.bottom = new FormAttachment(100, -20);
		tblHistory.setLayoutData(tblhistorydata);
		
		FormData lblsentodata = new FormData();
		lblsentodata.top = new FormAttachment(0, 15);
		lblsentodata.left = new FormAttachment(0, 40);
		//lblsentodata.right = new FormAttachment(100, -10);
		//lblsentodata.bottom = new FormAttachment(100, -20);
		lblSendTo.setLayoutData(lblsentodata);

		FormData txtsendtodata = new FormData();
		txtsendtodata.top = new FormAttachment(0, 35);
		txtsendtodata.left = new FormAttachment(lblSendTo, 20);
		txtsendtodata.right = new FormAttachment(lblSendTo, 550);
		//txtsendtodata.bottom = new FormAttachment(100, -20);
		txtSendTo.setLayoutData(txtsendtodata);
		
		FormData cmdsenddata = new FormData();
		cmdsenddata.top = new FormAttachment(0, 32);
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
		cmbsendamountisodata.top = new FormAttachment(lblSendTo, -5);
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
		qrcanvasdata.top = new FormAttachment(0, -20);
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
		cmdClipBoardAdr.setLayoutData(cmdclipboarddata);
		
		FormData lblrcvpaymaildata = new FormData();
		lblrcvpaymaildata.top = new FormAttachment(lblRcvAddress, 10);
		lblrcvpaymaildata.left = new FormAttachment(0, 250);
		//lblrcvaddressdata.right = new FormAttachment(100, 0);
		//lblrcvaddressdata.bottom = new FormAttachment(100, 0);
		lblRcvPaymail.setLayoutData(lblrcvpaymaildata);
		
		FormData cmdclipboardpaymaildata = new FormData();
		cmdclipboardpaymaildata.top = new FormAttachment(lblRcvAddress, 8);
		cmdclipboardpaymaildata.left = new FormAttachment(lblRcvPaymail, 10);
		//cmdclipboardpaymaildata.right = new FormAttachment(100, 0);
		//cmdclipboardpaymaildata.bottom = new FormAttachment(100, 0);
		cmdClipBoardPaymail.setLayoutData(cmdclipboardpaymaildata);
		

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
				//try {
					//Thread.sleep(1000);
					LoadTransactionHistory();
				//} catch (InterruptedException e) {
				//	InfinityPfm.LogMessage(e.getMessage());
				//}
			}
		} );
		
	}
	
	protected void LoadTransactionHistory() {
		
		// First get the latest balance
		this.setFiatAndBsvBalance();
		
		// Compare latest balance with last saved
		// balance to see if a history update is actually needed.
		
		if (lblAmountBsv.getText().equals(lastBsvBalance)) 
			return;
		
		tblHistory.removeAll();
		
		org.infinitypfm.core.data.Transaction lastTran = null;
		
		try {
			// Find last transaction having a transaction key
			// getLastBsvTransaction
			lastTran = MM.sqlMap.selectOne("getLastBsvTransaction");
			
		} catch (Exception e) {
			
		}
		
		if (MM.wallet.isImplemented(WalletFunction.GETHISTORY)) {

			// Wallet implementation allows fetching history.
			// Get history and compare with what is in the DB 
			// and make necessary additions / corrections
			
			// Going to call once for now on first startup
			// and require refresh button click to do it again
			if (MM.walletNeedsSync) {
				LoadWalletHistory(lastTran);
				MM.walletNeedsSync = false;
			}
		}
		
		ParamDateRangeAccount range = new ParamDateRangeAccount();
		
		LocalDateTime lastTranDate;
		
		if (lastTran != null) {
			Date tranDate = lastTran.getTranDate();
			lastTranDate = tranDate.toInstant()
		      .atZone(ZoneId.systemDefault())
		      .toLocalDateTime();
			
		} else {
			// Did not find a previous transaction so start from today
			lastTranDate = LocalDateTime.now();
		}

		range.setEndDate(Timestamp.valueOf(lastTranDate));
		range.setStartDate(Timestamp.valueOf(lastTranDate.minusDays(30)));
		if (bsvAccount != null)
			range.setActId(bsvAccount.getActId());
		TableItem ti = null;
		final Display display = InfinityPfm.shMain.getDisplay();
		int rowCount = 0;
		try {
			List<org.infinitypfm.core.data.Transaction> tranList = MM.sqlMap.selectList("getTransactionsForRangeAndAccountReverseSort", range);
			if (tranList != null && tranList.size() > 0) {
				
				if (bsvAccount != null)
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
				ListIterator<org.infinitypfm.core.data.Transaction> li = tranList.listIterator(0);
				
				long newBalance = 0;
				
				if (bsvAccount != null)
					newBalance = bsvAccount.getActBalance();
				org.infinitypfm.core.data.Transaction tran = null;
				
				_format.setPrecision(8);
				
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
						Label link = new Label(tblHistory, SWT.CENTER);
						link.setImage(imIcons.getImage(MM.IMG_EMBLEM_WEB));
						link.setText(MM.PHRASES.getPhrase("86"));
						link.setData(t);
						link.addListener(SWT.MouseUp, linkVew_mouse);
						link.setEnabled(true);
						
						link.setForeground(colorWhite);
						//_links.add(link);
						editor.minimumWidth = 50;
						editor.horizontalAlignment = SWT.CENTER;
						editor.setEditor(link, ti, 5);
					}
					
					rowCount++;
					tblHistory.redraw();
				}
				
				lastBsvBalance = lblAmountBsv.getText(); 
				
			}
				
		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
	}
	
	private void LoadWalletHistory(org.infinitypfm.core.data.Transaction lastTran) {
		
		if (bsvAccount ==  null) return;
					
		try {

			String param = null;
			if (lastTran != null) param = lastTran.getTransactionKey();
			List<DigitalAssetTransaction> result =  MM.wallet.getHistory(param);
			List<org.infinitypfm.core.data.Transaction> updates = new ArrayList<org.infinitypfm.core.data.Transaction>();
			
			if (result != null) {
				for (DigitalAssetTransaction tran : result) {
					// Make sure transaction not already in DB, for coins sent
					// from InfinityPfm directly, this will be the case
					List<org.infinitypfm.core.data.Transaction> tranSearch = MM.sqlMap.selectList("getTransactionsByKeyBSVOnly", tran.getTxId());
					if (tranSearch == null || tranSearch.size()==0) {
						
						String memo = "BSV transaction";
						// Not found, add it
						if (tran.getNotes() != null) memo = tran.getNotes();
						else if (tran.getDocId() != null) memo = memo + " " + tran.getDocId();
						
						double dAmount = tran.getBalance_change() * 0.00000001D;
						
						//_formatter.getAmountFormatted(dAmount,  "###.#########")
						
						org.infinitypfm.core.data.Transaction newTran = new org.infinitypfm.core.data.Transaction();
						newTran.setActId(bsvAccount.getActId());
						newTran.setTranAmount(DataFormatUtil.moneyToLong(Double.toString(dAmount)));
						newTran.setTranDate(this.getTransactionDateFromString(tran.getTimestamp(), false));
						newTran.setTranMemo(memo);
						newTran.setTransactionKey(tran.getTxId());
						updates.add(newTran);
					}
				}
				
				if (updates.size() > 0) {
					//Load import dialog to get the offsets for new transactions
					ImportDialog importDialog = new ImportDialog(updates, bsvAccount.getActId());
					importDialog.Open();
				}
				
			}
			
		} catch (WalletException e) {
			InfinityPfm.LogMessage(e.getMessage(), false);
		}
	}
	
	private void LoadAccountCombos() {

		try {
			Account act = null;
			@SuppressWarnings("rawtypes")
			java.util.List list = MM.sqlMap.selectList(
					"getAllAccountsByType", null);
 
			cmbOffset.removeAll();

			for (int i = 0; i < list.size(); i++) {
				act = (Account) list.get(i);
				cmbOffset.add(act.getActName());
				cmbOffset.setData(act.getActName(), act);
			}

		} catch (Exception se) {
			InfinityPfm.LogMessage(se.getMessage());
		}
	}
	
	private void refreshExchangeRate() {
		
		CurrencyMethod method = new CurrencyMethod();
		method.setCurrencyID(MM.options.getDefaultBsvCurrencyID());
		method.setMethodName(MM.options.getDefaultBsvCurrencyMethod());

		try {
			method = (CurrencyMethod) MM.sqlMap.selectOne(
					"getCurrencyMethod", method);
		} catch (Exception e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
		
		String newRate = RateParser.getRate(method);
		
		if (newRate != null) {
			
			try {
			
				Currency currency = (Currency) MM.sqlMap.selectOne("getCurrencyById", MM.options.getDefaultBsvCurrencyID());
				currency.setExchangeRate(newRate);
				currency.setLastUpdate(new Date());
				
				MM.sqlMap.update("updateExchangeRate", currency);
				_bsvCurrency = currency;
				
			} catch (Exception e) {
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
		
		if (bsvBalance != null) {
			
			BigDecimal amount = _format.strictMultiply(bsvBalance, _bsvCurrency.getExchangeRate());
			BigDecimal bsvAmount = new BigDecimal(bsvBalance);
			long lbsvAmount = DataFormatUtil.moneyToLong(bsvAmount);
			if (bsvAccount != null)
				bsvAccount.setActBalance(lbsvAmount);
			long lAmount = DataFormatUtil.moneyToLong(amount);
			_format.setPrecision(MM.options.getCurrencyPrecision());
			String fiatBalance = _format.getAmountFormatted(lAmount);
			Display.getDefault().asyncExec(() -> lblAmount.setText("$" + fiatBalance));
			Display.getDefault().asyncExec(() -> lblAmountBsv.setText(bsvBalance + " BSV"));
			try {
				MM.sqlMap.update("updateAccountBalanceFromAccount", bsvAccount);
			} catch (Exception e) {
				InfinityPfm.LogMessage(e.getMessage());
			}
		} else {
			lblAmountBsv.setText("0 BSV");
			lblAmount.setText("$0");
		}
	}
	
	/**
	 * Ask the wallet kit for the BSV balance and 
	 * pass it to overload for formatting.
	 */
	private void setFiatAndBsvBalance() {
		
		Display.getDefault().asyncExec( new Runnable() {
			@Override
			public void run() {
				try {
					if (MM.wallet != null && MM.wallet.isImplemented(WalletFunction.GETSETBALANCEBSV)) { 
						try {
							WalletAuth.getInstance().walletPassword();
							setFiatAndBsvBalance( MM.wallet.getBsvBalance());
						} catch (PasswordInvalidException e) {
							InfinityPfm.LogMessage(e.getMessage());
						}
					}
				} catch (Exception e) {
					InfinityPfm.LogMessage(e.getMessage());
				}
			}
		} );
	}
	
	private void loadSendIsoCombo() {
		
		try {
			Currency c = (Currency) MM.sqlMap.selectOne("getCurrencyById", Long.valueOf(MM.options.getDefaultCurrencyID()).longValue() );
			
			if (c != null) {
				cmbSendAmountIso.add(c.getIsoName());
				cmbSendAmountIso.setData(c.getIsoName(), c.getCurrencyID());
			}
			
			cmbSendAmountIso.add("BSV");
			cmbSendAmountIso.setData("BSV", MM.options.getDefaultBsvCurrencyID());
			
			cmbSendAmountIso.select(0);
		
		} catch (Exception e) {
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
	
	private void clearSendForm() {
		
		txtSendTo.setText("");
		txtSendAmount.setText("");
		
	}
	
	private Date getTransactionDateFromString (String sDate, boolean isUTC) {
		
		if (sDate == null) return new Date();
		
		//2022-01-07 01:22:41
		_formatter.setDate(sDate, "yyyy-MM-dd HH:mm:ss");
		
		if (isUTC)
			return _formatter.convertUTCToLocal();
		else
			return _formatter.getDate();
		
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
    
   
	SelectionAdapter cmdClipBoardAdr_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			
			String str = lblRcvAddress.getText();
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			StringSelection strSel = new StringSelection(str);
			clipboard.setContents(strSel, null);
		}
	};
	
	SelectionAdapter cmdClipBoardPaymail_OnClick = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent event) {
			
			String str = lblRcvPaymail.getText();
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
				InfinityPfm.LogMessage(MM.PHRASES.getPhrase("288"), true);
				return;
			}
			
			// Always get a new password from user to send
			WalletAuth.getInstance().clearPassword();
			try {
				WalletAuth.getInstance().walletPassword();
			} catch (PasswordInvalidException e2) {
				InfinityPfm.LogMessage(MM.PHRASES.getPhrase("292"), _inSend);
				return;
			}
			
			long selectedCurrency = (long) cmbSendAmountIso.getData(cmbSendAmountIso.getText());
			
			String sendAmount = null;
			
			if (selectedCurrency == MM.options.getDefaultBsvCurrencyID()) 
				sendAmount = txtSendAmount.getText();
			else {
				// Convert from fiat to BSV before sending
				refreshExchangeRate();
				BigDecimal amountToSend = _format.strictDivide(sAmount, _bsvCurrency.getExchangeRate(), MM.MAX_PRECISION);
				sendAmount = amountToSend.toString();
			}
			
			if (confirmSend(sAmount, cmbSendAmountIso.getText(), sendTo)) {
				
				// Send it!!
				try {
					_inSend = true;
					MM.wallet.sendCoins(sendTo, sendAmount, memo);
				} catch (SendException e1) {
					InfinityPfm.LogMessage(e1.getMessage(), true);
				}
			}
			
		}
	};

	Listener linkVew_mouse = new Listener() {

		@Override
		public void handleEvent(Event event) {
			
			if (event.type == SWT.MouseUp) {
			
				org.infinitypfm.core.data.Transaction t = 
						(org.infinitypfm.core.data.Transaction) event.widget.getData();
				
				if (t.getTransactionKey() != null) {
					String sLink = "https://whatsonchain.com/tx/" + t.getTransactionKey();
					Program.launch(sLink);
				}
			}
		}		
	};
	
	/*****************/
	/* Wallet Events */
	/*****************/

	@Override
	public void coinsReceived(String transactionHash, String memo, String value, String prevBalance, String newBalance, String transactionTime) {
		
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
		
			if (_inSend) {
				_inSend = false;
				return;
			}
				
			if (bsvAccount != null && bsvCoinsReceived != null) {
				
				long amount = DataFormatUtil.moneyToLong(new BigDecimal(value));
				
				org.infinitypfm.core.data.Transaction t = new org.infinitypfm.core.data.Transaction();
				t.setTranAmount(amount);
				t.setActId(bsvAccount.getActId());
				t.setTransactionKey(transactionHash);
				
				TransactionOffset offset = new TransactionOffset();
				offset.setOffsetId(bsvCoinsReceived.getActId());
				
				offset.setOffsetAmount(-amount);
				
				offset.setOffsetName("Coins received");
				offset.setNeedsConversion(true);
				
				ArrayList<TransactionOffset> offsets = new ArrayList<TransactionOffset>();
				offsets.add(offset);
				
				t.setOffsets(offsets);
				
				t.setExchangeRate(_bsvCurrency.getExchangeRate());
				
				
				t.setTranDate(getTransactionDateFromString(transactionTime, true));
				
				if (memo != null && memo.length() > 0)
					t.setTranMemo(memo);
				else
					t.setTranMemo(MM.PHRASES.getPhrase("282"));
				
				DataHandler handler = new DataHandler();
				try {
					handler.AddTransaction(t, false);
				} catch (Exception e) {
					InfinityPfm.LogMessage(e.getMessage());
				} 
			}
			
				// Reload the wallet balance and history if wallet implementation can dynamically 
				// receive coins vs polling for them
				if (MM.wallet.isImplemented(WalletFunction.RECIEVEREALTIME)) {
				
					if (MM.wallet.isImplemented(WalletFunction.GETSETBALANCEBSV))
						setFiatAndBsvBalance(newBalance);
					LoadTransactionHistoryAsync();
					
				}
			}
			
		});
	}

	@Override
	public void coinsSent(String transactionHash, String memo, String value, String prevBalance, String newBalance, String transactionTime) {
		
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
		
			Account offset = (Account) cmbOffset.getData(cmbOffset.getText());
			
			if (bsvAccount != null && offset != null) {
				org.infinitypfm.core.data.Transaction t = new org.infinitypfm.core.data.Transaction();
				t.setTranAmount(-DataFormatUtil.moneyToLong(value));
				t.setActId(bsvAccount.getActId());
				t.setActOffset(offset.getActId());
				t.setExchangeRate(_bsvCurrency.getExchangeRate());
				t.setTranMemo(txtMemo.getText());
				t.setTransactionKey(transactionHash);
				t.setTranDate(getTransactionDateFromString(transactionTime, false));
				
				DataHandler handler = new DataHandler();
				try {
					handler.AddTransaction(t, false);
					
				} catch (Exception e) {
					InfinityPfm.LogMessage(e.getMessage());
				} 
			}
				InfinityPfm.LogMessage(MM.PHRASES.getPhrase("310"), true);
				clearSendForm();
				setFiatAndBsvBalance(newBalance);
				LoadTransactionHistoryAsync();
			}
			
		});
	}

	@Override
	public void walletMessage(String message, WalletException e) {
		InfinityPfm.LogMessage(message, false);
		if (e != null)
			InfinityPfm.LogMessage(e.getMessage());
	}

	@Override
	public void signIn(boolean success, String message, WalletException e) {
		
		if (success) {
			InfinityPfm.LogMessage(MM.PHRASES.getPhrase("306"), true);
			setFiatAndBsvBalance();
		}
		
		if (message != null) {
			InfinityPfm.LogMessage(message, true);
			e.printStackTrace();
		}
		
	}

	@Override
	public void QZDispose() {
		
		// SWT object cleanup
		
		try { colorFont.dispose();} catch (Exception e) {System.out.println(e.getMessage());}
		try { colorWhite.dispose();} catch (Exception e) {System.out.println(e.getMessage());}
		try { addressFont.dispose();} catch (Exception e) {System.out.println(e.getMessage());}
		
		try { bcLogo.dispose(); } catch (Exception e) {System.out.println(e.getMessage());}
		try { imgRcvQrCode.dispose(); } catch (Exception e) {System.out.println(e.getMessage());}
		try { cursorHand.dispose(); } catch (Exception e) {System.out.println(e.getMessage());}
		
		try { lblAmount.getFont().dispose(); } catch (Exception e ) {System.out.println(e.getMessage());}
		try { lblAmountBsv.getFont().dispose(); } catch (Exception e ) {System.out.println(e.getMessage());}
		try { lblSendTo.getFont().dispose(); } catch (Exception e ) {System.out.println(e.getMessage());}
		try { txtSendTo.getFont().dispose(); } catch (Exception e ) {System.out.println(e.getMessage());}
		try { lblRcvAddress.getFont().dispose(); } catch (Exception e ) {System.out.println(e.getMessage());}
		try { imIcons.QZDispose(); } catch (Exception e) {System.out.println(e.getMessage());}
		
		try {
			for (Link link : _links) {
				link.dispose();
			}
		} catch (Exception e) {System.out.println(e.getMessage());}
		
		try { qrCanvas.dispose();} catch (Exception e){System.out.println(e.getMessage());}
		try { bcCanvas.dispose();} catch (Exception e) {System.out.println(e.getMessage());}
	}

}
