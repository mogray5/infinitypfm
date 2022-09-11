/*
 * Copyright (c) 2005-2021 Wayne Gray All rights reserved
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
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Currency;


public class NewCurrencyDialog extends BaseDialog {

	private Label lblCurrName = null;
	private Label lblCurrAbbr = null;
	private Label lblRate = null;
	private Text txtCurrName = null;
	private Text txtCurrAbbr= null;
	private Text txtRate = null;
	private Button cmdSave = null;
	private Button cmdCancel = null;
	private Label lblPrecision = null;
	private Text txtPrecision = null;
	private Scale scale = null;
	
	@Override
	protected void LoadUI(Shell sh) {
		lblCurrName = new Label(sh, SWT.NONE);
		lblCurrAbbr = new Label(sh, SWT.NONE);
		lblRate = new Label(sh, SWT.NONE);
		lblPrecision = new Label(sh, SWT.NONE);
		
		scale = new Scale(sh, SWT.BORDER);
		scale.setMaximum(8);
		scale.setPageIncrement(1);
		scale.addSelectionListener(scale_onGesture);
		scale.setSelection(2);

		txtPrecision = new Text(sh, SWT.BORDER);
		txtPrecision.setEnabled(false);
		txtPrecision.setText(Integer.toString(scale.getSelection()));
		txtCurrName = new Text(sh, SWT.BORDER);
		txtCurrAbbr = new Text(sh, SWT.BORDER);
		txtRate = new Text(sh, SWT.BORDER);
		cmdSave = new Button(sh, SWT.PUSH);
		cmdCancel = new Button(sh, SWT.PUSH);
	
		lblCurrName.setText(MM.PHRASES.getPhrase("209"));
		lblCurrAbbr.setText(MM.PHRASES.getPhrase("210"));
		lblRate.setText(MM.PHRASES.getPhrase("211"));
		lblPrecision.setText(MM.PHRASES.getPhrase("225") + ":");
		cmdSave.setText(MM.PHRASES.getPhrase("38"));
		cmdSave.addSelectionListener(cmdSave_OnClick);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);
		
	}

	@Override
	protected void LoadLayout() {
		FormData lblcurrnamedata = new FormData();
		lblcurrnamedata.top = new FormAttachment(0, 20);
		lblcurrnamedata.left = new FormAttachment(0, 40);
		lblCurrName.setLayoutData(lblcurrnamedata);
		
		FormData txtcurrnamedata = new FormData();
		txtcurrnamedata.top = new FormAttachment(0, 20);
		txtcurrnamedata.left = new FormAttachment(lblCurrName, 40);
		txtcurrnamedata.right = new FormAttachment(100, -40);
		txtCurrName.setLayoutData(txtcurrnamedata);

		FormData lblcurrabbrdata = new FormData();
		lblcurrabbrdata.top = new FormAttachment(lblCurrName, 25);
		lblcurrabbrdata.left = new FormAttachment(0, 40);
		lblCurrAbbr.setLayoutData(lblcurrabbrdata);

		FormData txtactiddata = new FormData();
		txtactiddata.top = new FormAttachment(lblCurrName, 25);
		txtactiddata.left = new FormAttachment(lblCurrName, 40);
		txtactiddata.right = new FormAttachment(100, -180);
		txtCurrAbbr.setLayoutData(txtactiddata);
		
		FormData lblratedata = new FormData();
		lblratedata.top = new FormAttachment(lblCurrAbbr, 25);
		lblratedata.left = new FormAttachment(0, 40);
		lblRate.setLayoutData(lblratedata);

		FormData txtratedata = new FormData();
		txtratedata.top = new FormAttachment(lblCurrAbbr, 25);
		txtratedata.left = new FormAttachment(lblCurrName, 40);
		txtratedata.right = new FormAttachment(100, -180);
		txtRate.setLayoutData(txtratedata);

		FormData lblprecisiondata = new FormData();
		lblprecisiondata.top = new FormAttachment(lblRate, 40);
		lblprecisiondata.left = new FormAttachment(0, 40);
		lblPrecision.setLayoutData(lblprecisiondata);

		FormData scaledata = new FormData();
		scaledata.top = new FormAttachment(lblRate, 40);
		scaledata.left = new FormAttachment(lblPrecision, 85);
		scale.setLayoutData(scaledata);
		
		FormData txtprecisiondata = new FormData();
		txtprecisiondata.top = new FormAttachment(lblRate, 30);
		txtprecisiondata.left = new FormAttachment(scale, 5);
		txtPrecision.setLayoutData(txtprecisiondata);

		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(lblPrecision, 35);
		cmdcanceldata.left = new FormAttachment(35, 0);
		cmdCancel.setLayoutData(cmdcanceldata);
		
		FormData cmdsavedata = new FormData();
		cmdsavedata.top = new FormAttachment(lblPrecision, 35);
		cmdsavedata.left = new FormAttachment(cmdCancel, 10);
		cmdSave.setLayoutData(cmdsavedata);
		
	}
	
	public int Open() {
		super.Open();
		 shell.setText(MM.PHRASES.getPhrase("208") + " " +
				MM.APPTITLE);

		shell.setSize(450, 300);
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
	
	SelectionAdapter scale_onGesture = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

			int perspectiveValue = scale.getSelection();
			txtPrecision.setText(Integer.toString(perspectiveValue));

		}
	};
	
	SelectionAdapter cmdSave_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			
			if (txtRate.getText().length()>0 &&
					txtCurrName.getText().length()>0 &&
					txtCurrAbbr.getText().length()>0){
				Currency currency = new Currency();
				currency.setCurrencyName(txtCurrName.getText());
				currency.setIsoName(txtCurrAbbr.getText());
				currency.setExchangeRate(txtRate.getText());
				currency.setCurrencyPrecision(Integer.parseInt(txtPrecision
						.getText()));
				
				try {
					MM.sqlMap.insert("addCurrency", currency);
				} catch (Exception e1) {
					InfinityPfm.LogMessage(e1.getMessage());
				}
			}
			
			shell.dispose();
			
		}
	};

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			shell.dispose();
		}
	};

}
