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

package org.infinitypfm.ui.view.dialogs;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;

public class MessageDialog extends BaseDialog {

	private int iDType = MM.DIALOG_INFO;
	private String sDTitle = MM.APPTITLE;
	private String sDMsg = "";
	private int iReturn = MM.CANCEL;
	private int width = 400;
	private int height = 175;
	private Image imgContext = null;
	private Canvas cvContext = null;
	private String sImage = null;
	//private Color color = null;
	
	/*
	 * Widgets
	 */
	private Label lblMsg = null;
	private Button cmdOne = null;
	private Button cmdTwo = null;
	private Button cmdThree = null;
	
	public int Open() {
		super.Open();
		
		shell.setSize(width, height);
		shell.setText(sDTitle);
		this.CenterWindow();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		return iReturn;
	}
	
	public int Open (int w, int h, String image) {
		width = w;
		height = h;
		sImage = image;
		return iReturn;
	}

	public MessageDialog(int iType, String sTitle, String sMsg) {
		super();
		iDType = iType;
		sDTitle = sTitle;
		sDMsg = sMsg;
		
	}
	
	public void setDimensions(int w, int h){
		width = w;
		height = h;
	}

	public void setImage(String image) {
		sImage = image;
	}
	
	protected void LoadUI(Shell sh) {
		lblMsg = new Label(sh, SWT.WRAP);
		lblMsg.setText(sDMsg);
		cmdOne = new Button(sh, SWT.PUSH);
		cmdOne.addSelectionListener(cmdOne_OnClick);
		cmdOne.setText(MM.PHRASES.getPhrase("12"));
		cmdTwo = new Button(sh, SWT.PUSH);
		cmdTwo.addSelectionListener(cmdTwo_OnClick);
		cmdThree = new Button(sh, SWT.PUSH);
		cmdThree.addSelectionListener(cmdThree_OnClick);
		cmdThree.setText(MM.PHRASES.getPhrase("4"));
		
		if (iDType == MM.DIALOG_INFO){
			cmdOne.setVisible(false);
			cmdTwo.setText(MM.PHRASES.getPhrase("5"));
			cmdThree.setVisible(false);
			
			if (sImage == null)
				imgContext = InfinityPfm.imMain.getTransparentImage(MM.IMG_MESSAGE_DEFAULT);
			
		} else {
			cmdOne.setVisible(true);
			cmdTwo.setText(MM.PHRASES.getPhrase("13"));
			cmdThree.setVisible(true);
			
			if (sImage == null)
				imgContext = InfinityPfm.imMain.getTransparentImage(MM.IMG_HELP);
		}
		
		if (sImage != null)
			imgContext = InfinityPfm.imMain.getTransparentImage(sImage);
		
		cvContext = new Canvas(sh, SWT.NONE);
		//color = new Color(InfinityPfm.shMain.getDisplay(), 255,255,255);
		//cvContext.setBackground(color);
		cvContext.addPaintListener(logo_OnPaint);

	}

	protected void LoadLayout() {

		FormData cvcontextdata = new FormData();
		cvcontextdata.top = new FormAttachment(25, 0);
		cvcontextdata.left = new FormAttachment(10, 0);
		cvcontextdata.right = new FormAttachment(10,20);
		cvcontextdata.bottom = new FormAttachment(25,30);
		cvContext.setLayoutData(cvcontextdata);

		FormData lblmsgdata = new FormData();
		lblmsgdata.top = new FormAttachment(25,0);
		lblmsgdata.left = new FormAttachment(cvContext,5);
		lblmsgdata.right = new FormAttachment(90,0);
		lblmsgdata.bottom = new FormAttachment(100,-50);
		lblMsg.setLayoutData(lblmsgdata);
		
		FormData cmdonedata = new FormData();
		cmdonedata.top = new FormAttachment(100, -40);
		cmdonedata.left = new FormAttachment(20,0);
		cmdonedata.right = new FormAttachment(40,0);
		cmdOne.setLayoutData(cmdonedata);
		
		if (iDType == MM.DIALOG_QUESTION){
		
			FormData cmdtwodata = new FormData();
			cmdtwodata.top = new FormAttachment(100, -40);
			cmdtwodata.left = new FormAttachment(cmdOne, 10);
			cmdtwodata.right = new FormAttachment(60,10);
			cmdTwo.setLayoutData(cmdtwodata);

			FormData cmdthreedata = new FormData();
			cmdthreedata.top = new FormAttachment(100, -40);
			cmdthreedata.left = new FormAttachment(cmdTwo, 10);
			cmdthreedata.right = new FormAttachment(80,10);
			cmdThree.setLayoutData(cmdthreedata);
			
		} else {

			FormData cmdtwodata = new FormData();
			cmdtwodata.top = new FormAttachment(100, -40);
			cmdtwodata.left = new FormAttachment(35, 0);
			cmdtwodata.right = new FormAttachment(60,0);
			cmdTwo.setLayoutData(cmdtwodata);

			FormData cmdthreedata = new FormData();
			cmdthreedata.top = new FormAttachment(100, -40);
			cmdthreedata.left = new FormAttachment(cmdTwo, 10);
			cmdthreedata.right = new FormAttachment(70,10);
			cmdThree.setLayoutData(cmdthreedata);
		}
		
	}

	/*
	 * Listeners
	 */
	SelectionAdapter cmdOne_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			iReturn = MM.YES;
			shell.dispose();
		}
	};

	SelectionAdapter cmdTwo_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			iReturn = MM.NO;
			shell.dispose();
		}
	};
	
	SelectionAdapter cmdThree_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			iReturn = MM.CANCEL;
			shell.dispose();
		}
	};
	
	PaintListener logo_OnPaint = new PaintListener() {
        public void paintControl(PaintEvent e) {
         e.gc.drawImage(imgContext,0,0);
        }
    };
}
