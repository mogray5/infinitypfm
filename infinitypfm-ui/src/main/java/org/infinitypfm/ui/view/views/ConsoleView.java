/*
 * Copyright (c) 2005-2011 Wayne Gray All rights reserved
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


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.ui.view.toolbars.ConsoleToolbar;

public class ConsoleView extends BaseView {

	private Shell sh;

	private ConsoleToolbar tbMain;

	private Text txtConsole;

	private Color bgColor;

	private Label lblConsole;

	private String sStatus = "";

	private int iLines = 0;

	public ConsoleView(Shell shMain) {
		super(shMain, SWT.BORDER);
		sh = shMain;
		// init console
		LoadUI();
		LoadLayout();
	}

	public ConsoleView() {
		super(InfinityPfm.shMain, SWT.BORDER);
		sh = InfinityPfm.shMain;
		// init console
		LoadUI();
		LoadLayout();
	}

	protected void LoadUI() {
		tbMain = new ConsoleToolbar(this);
		txtConsole = new Text(this, SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL
				| SWT.BORDER);
		bgColor = sh.getDisplay().getSystemColor(SWT.COLOR_WHITE);

		/*
		 * The below property settings is sometimes failing on Linux. Wrap in
		 * try block until can figure out what is happening (WGG)
		 *  
		 */
		try {
			txtConsole.setBackground(bgColor);
		} catch (IllegalArgumentException iae) {
			InfinityPfm.LogMessage(iae.getMessage());
		}

		lblConsole = new Label(this, SWT.NONE);
		lblConsole.setText(MM.PHRASES.getPhrase("65") + sStatus.toString());

	}

	protected void LoadLayout() {
		this.setLayout(new FormLayout());
		FormData tbdata = new FormData();
		tbdata.right = new FormAttachment(100, 0);
		tbMain.setLayoutDat(tbdata);
		FormData txtdata = new FormData();
		txtdata.top = new FormAttachment(tbMain.getToolbar(), 0);
		txtdata.right = new FormAttachment(100, -3);
		txtdata.left = new FormAttachment(0, 0);
		txtdata.bottom = new FormAttachment(100, -3);
		txtConsole.setLayoutData(txtdata);
		FormData lblConsoledata = new FormData();
		lblConsoledata.top = new FormAttachment(0, 3);
		lblConsoledata.left = new FormAttachment(0, 3);
		lblConsole.setLayoutData(lblConsoledata);

	}

	public void ClearConsole() {
		txtConsole.setText("");
		iLines = 0;
	}

	public void AppendMsg(String sMsg) {

		try {

			if (iLines > 0) {
				txtConsole.append("\n" + sMsg);
			} else {
				txtConsole.append(sMsg);
			}
			iLines++;

		} catch (Exception e) {
			System.err.println(sMsg);
		}
	}

	public void setStatus(String sMsg) {
		sStatus = sMsg;
		lblConsole.setText(MM.PHRASES.getPhrase("65") + sStatus.toString());
	}

	public void QZDispose() {

		tbMain.QZDispose();

		if (!txtConsole.isDisposed()) {
			txtConsole.dispose();
		}

		if (!bgColor.isDisposed()) {
			bgColor.dispose();
		}
		if (!this.isDisposed()) {
			this.dispose();
		}
	}
}
