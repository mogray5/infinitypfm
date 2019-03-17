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
import java.sql.SQLException;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.bitcoin.wallet.WalletEvents;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Account;
import org.infinitypfm.ui.view.toolbars.WalletToolbar;

public class WalletView extends BaseView implements WalletEvents {

	private WalletToolbar tbMain = null;
	private Composite cmpHeader = null;
	private Canvas bcCanvas = null;
	private Image bcLogo = null;
	private Color color = null;
	private Label lblAmount = null;
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
	
	private String _receiveAddress;
	
	public WalletView(Composite arg0, int arg1) {
		super(arg0, arg1);
		MM.wallet.registerForEvents(this);
		_receiveAddress = MM.wallet.getCurrentReceivingAddress();
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
		lblAmount.setText("$" + MM.wallet.getFiatBalance());
		FontData[] fD = lblAmount.getFont().getFontData();
		fD[0].setHeight(70);
		lblAmount.setFont( new Font(InfinityPfm.shMain.getDisplay(),fD[0]));
		
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

		
		tblHistory = new Table(this, SWT.MULTI | SWT.HIDE_SELECTION);
		tblHistory.setLinesVisible(true);
		
		
		lblSendTo = new Label(sendGroup, SWT.NONE);
		FontData[] fD2 = lblSendTo.getFont().getFontData();
		fD2[0].setHeight(40);
		lblSendTo.setFont(new Font(InfinityPfm.shMain.getDisplay(),fD2[0]));
		lblSendTo.setText(MM.PHRASES.getPhrase("265") + ":");
		
		txtSendTo = new Text(sendGroup, SWT.BORDER);
		
		cmdSend = new Button(sendGroup, SWT.PUSH);
		cmdSend.setImage(InfinityPfm.imMain.getImage(MM.IMG_ARROW_RIGHT));
		//cmdSend.addSelectionListener(cmdStartDatePicker_OnClick);
		
		lblOffset = new Label(sendGroup, SWT.NONE);
		lblOffset.setText(MM.PHRASES.getPhrase("273") + ":");
		cmbOffset = new Combo(sendGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		LoadAccountCombos();
		
		lblMemo = new Label(sendGroup, SWT.NONE);
		lblMemo.setText(MM.PHRASES.getPhrase("41") + ":");
		
		txtMemo = new Text(sendGroup, SWT.BORDER);
		
		lblRcvAddress = new Label(receiveGroup, SWT.NONE);
		FontData[] fD3 = lblRcvAddress.getFont().getFontData();
		fD3[0].setHeight(15);
		lblRcvAddress.setFont(new Font(InfinityPfm.shMain.getDisplay(),fD3[0]));
		lblRcvAddress.setText(_receiveAddress);
		
		cmdClipBoard = new Button(receiveGroup, SWT.PUSH);
		cmdClipBoard.setImage(InfinityPfm.imMain.getImage(MM.IMG_CLIPBOARD));
		cmdClipBoard.addSelectionListener(cmdClipBoard_OnClick);
		
		//Get qr code
		//https://chart.googleapis.com/chart?chs=250x250&cht=qr&chl=%2012kQMUkB9QJu9X5JP9H9M2qMUmrGtDakkV
		
		try {
			File qrImage = MM.wallet.getQrCode(_receiveAddress);
			if (qrImage.exists()) {
				imgRcvQrCode = InfinityPfm.imMain.getTransparentImage(qrImage);
				qrCanvas = new Canvas(receiveGroup, SWT.BORDER);
				qrCanvas.setBackground(color);
				qrCanvas.addPaintListener(logo_OnPaintQr);
			}
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		}
	}
	
	protected void LoadLayout() {
		
		FormData tbmaindata = new FormData();
		tbmaindata.right = new FormAttachment(100, 0);
		tbMain.setLayoutDat(tbmaindata);
		
		FormData cmpheaderdata = new FormData();
		cmpheaderdata.top = new FormAttachment(0, 10);
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
		lblsentodata.top = new FormAttachment(0, 50);
		lblsentodata.left = new FormAttachment(0, 40);
		//lblsentodata.right = new FormAttachment(100, -10);
		//lblsentodata.bottom = new FormAttachment(100, -20);
		lblSendTo.setLayoutData(lblsentodata);

		FormData txtsendtodata = new FormData();
		txtsendtodata.top = new FormAttachment(0, 65);
		txtsendtodata.left = new FormAttachment(lblSendTo, 20);
		txtsendtodata.right = new FormAttachment(lblSendTo, 550);
		//txtsendtodata.bottom = new FormAttachment(100, -20);
		txtSendTo.setLayoutData(txtsendtodata);
		
		FormData cmdsenddata = new FormData();
		cmdsenddata.top = new FormAttachment(0, 62);
		cmdsenddata.left = new FormAttachment(txtSendTo, 10);
		//cmdsenddata.right = new FormAttachment(lblSendTo, 150);
		//cmdsenddata.bottom = new FormAttachment(100, -20);
		cmdSend.setLayoutData(cmdsenddata);
	
		FormData lblmemotdata = new FormData();
		lblmemotdata.top = new FormAttachment(lblSendTo, 5);
		lblmemotdata.left = new FormAttachment(0, 80);
		lblMemo.setLayoutData(lblmemotdata);
		
		FormData txtmemotdata = new FormData();
		txtmemotdata.top = new FormAttachment(lblSendTo, 0);
		txtmemotdata.left = new FormAttachment(lblMemo, 5);
		txtmemotdata.right = new FormAttachment(65, 0);
		txtMemo.setLayoutData(txtmemotdata);
		
		FormData lbloffsetdata = new FormData();
		lbloffsetdata.top = new FormAttachment(lblMemo, 25);
		lbloffsetdata.left = new FormAttachment(0, 80);
		lblOffset.setLayoutData(lbloffsetdata);

		FormData cmboffsetdata = new FormData();
		cmboffsetdata.top = new FormAttachment(lblMemo, 20);
		cmboffsetdata.left = new FormAttachment(lblOffset, 5);
		//cmboffsetdata.right = new FormAttachment(lblAccount, 300);
		cmbOffset.setLayoutData(cmboffsetdata);
		
		FormData qrcanvasdata = new FormData();
		qrcanvasdata.top = new FormAttachment(0, 0);
		qrcanvasdata.left = new FormAttachment(0, 0);
		qrcanvasdata.right = new FormAttachment(100, 0);
		qrcanvasdata.bottom = new FormAttachment(100, 0);
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
	
	/*****************/
	/* Wallet Events */
	/*****************/

	@Override
	public void coinsReceived(Transaction tx, Coin value, Coin prevBalance, Coin newBalance) {
		
		Display.getDefault().syncExec(new Runnable(){
			public void run(){
				
			}
		});
		
	}

	@Override
	public void coinsSent(Transaction tx, Coin value, Coin prevBalance, Coin newBalance) {
		// TODO Auto-generated method stub
		
	}
}
