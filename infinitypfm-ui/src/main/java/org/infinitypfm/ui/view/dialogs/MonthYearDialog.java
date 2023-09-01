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

public class MonthYearDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	private Label lblMonth = null;
	private Combo cmbMonth = null;
	private Label lblYear = null;
	private Combo cmbYear = null;
	private Button cmdOk = null;
	private Button cmdCancel = null;
	
	private int year = -1;
	private int month = -1;
	private String monthName = null;

	@Override
	protected void LoadUI(Shell sh) {
		
		lblMonth = new Label(sh, SWT.NONE);
		lblMonth.setText(MM.PHRASES.getPhrase("99") + ":");
		lblYear = new Label(sh, SWT.NONE);
		lblYear.setText(MM.PHRASES.getPhrase("166") + ":");
		cmbMonth = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbYear = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);
		
		DataFormatUtil dateUtil = new DataFormatUtil(MM.options.getCurrencyPrecision());
		dateUtil.setDate(new Date());
		
		int year = dateUtil.getYear();
		
		for (int i=106; i<118; i++)
			cmbMonth.add(MM.PHRASES.getPhrase(Integer.toString(i)));
		
		cmbMonth.select(dateUtil.getMonth());
		cmbMonth.setText(dateUtil.getMonthName(0));
		
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
		
		FormData lblmonthdata = new FormData();
		lblmonthdata.top = new FormAttachment(0, 35);
		lblmonthdata.left = new FormAttachment(20, 0);
		lblMonth.setLayoutData(lblmonthdata);
		
		FormData cmbmonthdata = new FormData();
		cmbmonthdata.top = new FormAttachment(0, 35);
		cmbmonthdata.left = new FormAttachment(lblMonth, 30);
		cmbMonth.setLayoutData(cmbmonthdata);
		
		FormData lblyeardata = new FormData();
		lblyeardata.top = new FormAttachment(lblMonth, 20);
		lblyeardata.left = new FormAttachment(20, 0);
		lblYear.setLayoutData(lblyeardata);

		FormData cmbyeardata = new FormData();
		cmbyeardata.top = new FormAttachment(lblMonth, 20);
		cmbyeardata.left = new FormAttachment(lblMonth, 30);
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.view.QZDialog#Open()
	 */
	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("123"));
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
	
	public int getMonth(){
		
		return month;
		
	}
	
	public String getMonthName(){
		
		return monthName;
		
	}
	
	/*
	 * Listeners
	 */
	SelectionAdapter cmdOk_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			year = Integer.parseInt(cmbYear.getText());
			month = cmbMonth.getSelectionIndex()+1;
			monthName = cmbMonth.getText();
			shell.dispose();
		}
	};

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			userCancelled = true;
			year = -1;
			month = -1;
			monthName = null;
			shell.dispose();
		}
	};

}
