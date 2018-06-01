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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.conf.MM;

public class ImportDefSelector extends BaseDialog {

	private Composite cmpAddEditDef = null;
	private Composite cmpDefList = null;
	private Composite cmpSelectedDef = null;
	private Button cmdClose = null;
	
	@Override
	protected void LoadUI(Shell sh) {

		cmpAddEditDef = new Composite(sh, SWT.BORDER);
		cmpAddEditDef.setLayout(new FormLayout());
		cmpDefList = new Composite(sh, SWT.BORDER);
		cmpDefList.setLayout(new FormLayout());
		cmpSelectedDef = new Composite(sh, SWT.BORDER);
		cmpSelectedDef.setLayout(new FormLayout());
		cmdClose = new Button(sh, SWT.PUSH);
		cmdClose.setText(MM.PHRASES.getPhrase("54"));
		cmdClose.addSelectionListener(cmdClose_OnClick);
	}

	@Override
	protected void LoadLayout() {
		FormData cmpaddeditdefdata = new FormData();
		cmpaddeditdefdata.top = new FormAttachment(0, 10);
		cmpaddeditdefdata.bottom = new FormAttachment(0, 80);
		cmpaddeditdefdata.left = new FormAttachment(0, 10);
		cmpaddeditdefdata.right = new FormAttachment(100, -10);
		cmpAddEditDef.setLayoutData(cmpaddeditdefdata);
		cmpAddEditDef.setToolTipText("a");
		
		FormData cmpdeflistdata = new FormData();
		cmpdeflistdata.top = new FormAttachment(cmpAddEditDef, 10);
		cmpdeflistdata.bottom = new FormAttachment(cmpAddEditDef, 170);
		cmpdeflistdata.left = new FormAttachment(0, 10);
		cmpdeflistdata.right = new FormAttachment(100, -10);
		cmpDefList.setLayoutData(cmpdeflistdata);
		cmpDefList.setToolTipText("b");
		
		FormData cmpselecteddefdata = new FormData();
		cmpselecteddefdata.top = new FormAttachment(cmpDefList, 10);
		cmpselecteddefdata.bottom = new FormAttachment(cmpAddEditDef, 250);
		cmpselecteddefdata.left = new FormAttachment(0, 10);
		cmpselecteddefdata.right = new FormAttachment(100, -10);
		cmpSelectedDef.setLayoutData(cmpselecteddefdata);
		cmpSelectedDef.setToolTipText("c");
		
		FormData cmdclosedata = new FormData();
		cmdclosedata.top = new FormAttachment(100, -40);
		cmdclosedata.bottom = new FormAttachment(100, -20);
		cmdclosedata.left = new FormAttachment(100, -80);
		cmdclosedata.right = new FormAttachment(100, -10);
		cmdClose.setLayoutData(cmdclosedata);
		
	}
	
	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("139"));
		shell.setSize(800, 600);
		this.CenterWindow();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return 1;
	}

	/*
	 * Listeners
	 */
	SelectionAdapter cmdClose_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			shell.dispose();
		}
	};

}
