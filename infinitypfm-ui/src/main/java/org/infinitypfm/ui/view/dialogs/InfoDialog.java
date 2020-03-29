/*
 * Copyright (c) 2005-2020 Wayne Gray All rights reserved
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.conf.MM;

/**
 * @author wgray
 */
public class InfoDialog extends BaseDialog {

	private String sDTitle = MM.APPTITLE;
	private String sDMsg = "";
	private int iReturn = MM.CANCEL;
	private String sReturn = "";

	/*
	 * Widgets
	 */
	private Label lblInfo = null;
	private Button cmdOne = null;
	private Button cmdTwo = null;
	private Text txtInfo = null;

	public int Open() {
		super.Open();
		shell.setText(sDTitle);

		shell.setSize(400, 175);
		this.CenterWindow();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return iReturn;
	}

	public String getInput() {
		this.Open();
		return sReturn;
	}

	public InfoDialog(String sTitle, String sMsg) {
		super();
		sDTitle = sTitle;
		sDMsg = sMsg;

	}

	protected void LoadUI(Shell sh) {

		sh.setText(sDTitle);

		lblInfo = new Label(sh, SWT.WRAP);
		lblInfo.setText(sDMsg);

		cmdOne = new Button(sh, SWT.PUSH);
		cmdOne.addSelectionListener(cmdOne_OnClick);
		cmdOne.setText(MM.PHRASES.getPhrase("5"));

		cmdTwo = new Button(sh, SWT.PUSH);
		cmdTwo.addSelectionListener(cmdTwo_OnClick);
		cmdTwo.setText(MM.PHRASES.getPhrase("4"));

		txtInfo = new Text(sh, SWT.BORDER);
		txtInfo.setFocus();

		this.CenterWindow();
	}

	protected void LoadLayout() {
		FormData lblinfodata = new FormData();
		lblinfodata.top = new FormAttachment(15, 0);
		lblinfodata.left = new FormAttachment(10, 0);
		lblinfodata.right = new FormAttachment(90, 0);
		lblInfo.setLayoutData(lblinfodata);

		FormData txtinfodata = new FormData();
		txtinfodata.top = new FormAttachment(lblInfo, 10);
		txtinfodata.left = new FormAttachment(10, 0);
		txtinfodata.right = new FormAttachment(90, 0);
		txtInfo.setLayoutData(txtinfodata);
		
		FormData cmdonedata = new FormData();
		cmdonedata.top = new FormAttachment(65, 0);
		cmdonedata.left = new FormAttachment(25, 0);
		cmdonedata.right = new FormAttachment(50, 0);
		cmdOne.setLayoutData(cmdonedata);

		FormData cmdtwodata = new FormData();
		cmdtwodata.top = new FormAttachment(65, 0);
		cmdtwodata.left = new FormAttachment(cmdOne, 10);
		cmdtwodata.right = new FormAttachment(70, 10);
		cmdTwo.setLayoutData(cmdtwodata);

	}

	/*
	 * Listeners
	 */
	SelectionAdapter cmdOne_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			sReturn = txtInfo.getText();
			shell.dispose();
		}
	};

	SelectionAdapter cmdTwo_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			sReturn = null;
			shell.dispose();
		}
	};

}
