/*
 * Copyright (c) 2005-2023 Wayne Gray All rights reserved
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

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.DataFormatUtil;

public class YearDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	private Label lblYear = null;
	private Combo cmbYear = null;
	private Button cmdOk = null;
	private Button cmdCancel = null;
	
	private int year = -1;

	@Override
	protected void LoadUI(Shell sh) {
		
		lblYear = new Label(sh, SWT.NONE);
		lblYear.setText(MM.PHRASES.getPhrase("166") + ":");
		cmbYear = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);
		
		DataFormatUtil dateUtil = new DataFormatUtil(MM.options.getCurrencyPrecision());
		dateUtil.setDate(new Date());
		
		int year = dateUtil.getYear();
		
		for (int j=year; j>year-20; j--)	
			cmbYear.add(Integer.toString(j));

		
		cmbYear.select(year);
		cmbYear.setText(Integer.toString(year));
		
		cmdOk = new Button(sh, SWT.PUSH);
		cmdOk.setText(MM.PHRASES.getPhrase("5"));
		cmdOk.addSelectionListener(cmdOk_OnClick);
		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);
		
	}

	@Override
	protected void LoadLayout() {
		
		FormData lblyeardata = new FormData();
		lblyeardata.top = new FormAttachment(0, 40);
		lblyeardata.left = new FormAttachment(35, 0);
		lblYear.setLayoutData(lblyeardata);

		FormData cmbyeardata = new FormData();
		cmbyeardata.top = new FormAttachment(0, 36);
		cmbyeardata.left = new FormAttachment(lblYear, 5);
		cmbYear.setLayoutData(cmbyeardata);
		
		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(cmbYear, 20);
		cmdcanceldata.right = new FormAttachment(40,10);
		cmdCancel.setLayoutData(cmdcanceldata);

		FormData cmdokdata = new FormData();
		cmdokdata.top = new FormAttachment(cmbYear, 20);
		cmdokdata.left = new FormAttachment(cmdCancel, 10);
		cmdokdata.right = new FormAttachment(60,10);
		cmdOk.setLayoutData(cmdokdata);
		
	}
	
	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("311"));
		shell.setSize(400, 200);
		this.CenterWindow();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return 1;
	}
	
	public int getYear() {
		
		return year;
		
	}
	
	/*
	 * Listeners
	 */
	SelectionAdapter cmdOk_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			year = Integer.parseInt(cmbYear.getText());
			shell.dispose();
		}
	};

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			userCancelled = true;
			year = -1;
			shell.dispose();
		}
	};

}
