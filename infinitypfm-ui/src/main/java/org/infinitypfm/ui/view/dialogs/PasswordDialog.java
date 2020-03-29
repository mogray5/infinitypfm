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

public class PasswordDialog extends BaseDialog {
	
	/*
	 * Widgets
	 */
	private Label lblUser = null;
	private Label lblPwd = null;
	private Text txtUser = null;
	private Text txtPwd = null;
	private Button cmdCancel = null;
	private Button cmdOk = null;
	
	private String user = null;
	private String password = null;
	private boolean bDisplayUser = true;
	
	public PasswordDialog() {}
	
	public PasswordDialog(boolean passwordOnly) {
		bDisplayUser  = passwordOnly;
	}
	
	@Override
	protected void LoadUI(Shell sh) {
		
		if (bDisplayUser) {
			lblUser = new Label(sh, SWT.WRAP);
			lblUser.setText(MM.PHRASES.getPhrase("80"));
			txtUser = new Text(sh, SWT.BORDER);
			txtUser.setFocus();
		}
		
		lblPwd = new Label(sh, SWT.WRAP);
		lblPwd.setText(MM.PHRASES.getPhrase("81"));
		
		txtPwd = new Text(sh, SWT.BORDER);
		txtPwd.setEchoChar('*');

		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.addSelectionListener(cmdCancel_OnClick);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));

		cmdOk = new Button(sh, SWT.PUSH);
		cmdOk.addSelectionListener(cmdOk_OnClick);
		cmdOk.setText(MM.PHRASES.getPhrase("5"));
		
		this.CenterWindow();

	}

	@Override
	protected void LoadLayout() {
		
		if (bDisplayUser) {
			FormData lbluserdata = new FormData();
			lbluserdata.top = new FormAttachment(20,0);
			lbluserdata.left = new FormAttachment(10,0);
			lblUser.setLayoutData(lbluserdata);
			
			FormData txtuserdata = new FormData();
			txtuserdata.top = new FormAttachment(30,0);
			txtuserdata.left = new FormAttachment(lblUser,20);
			txtuserdata.right = new FormAttachment(90,0);
			txtUser.setLayoutData(txtuserdata);
		}
		
		FormData lblpwddata = new FormData();
		
		if (bDisplayUser) {
			lblpwddata.top = new FormAttachment(txtUser,10);
			lblpwddata.left = new FormAttachment(10,0);
		} else {
			lblpwddata.top = new FormAttachment(20,10);
			lblpwddata.left = new FormAttachment(10,0);
		}
		
		lblPwd.setLayoutData(lblpwddata);
		
		FormData txtpwddata = new FormData();
		
		if (bDisplayUser) {
			txtpwddata.top = new FormAttachment(txtUser,0);
			txtpwddata.left = new FormAttachment(lblUser,20);
		} else {
			txtpwddata.top = new FormAttachment(20,0);
			txtpwddata.left = new FormAttachment(lblPwd,5);
		}
		txtpwddata.right = new FormAttachment(90,0);
		txtPwd.setLayoutData(txtpwddata);

		FormData cmdokdata = new FormData();
		cmdokdata.top = new FormAttachment(80, 0);
		cmdokdata.left = new FormAttachment(30, 0);
		cmdokdata.right = new FormAttachment(50,0);
		cmdOk.setLayoutData(cmdokdata);
		
		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(80, 0);
		cmdcanceldata.left = new FormAttachment(cmdOk,10);
		cmdcanceldata.right = new FormAttachment(70,10);
		cmdCancel.setLayoutData(cmdcanceldata);
		
	}
	
	public String[] getCredentials() {
		super.Open();
		shell.setText(MM.APPTITLE);

		shell.setSize(400, 200);
		this.CenterWindow();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		return new String[]{user, password};
	}
	
	/*
	 * Listeners
	 */

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			
			user = null;
			password = null;
			
			shell.dispose();
		}
	};
	
	SelectionAdapter cmdOk_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			
			if (bDisplayUser)
				user = txtUser.getText();
			
			password = txtPwd.getText();
			
			shell.dispose();
		}
	};

}
