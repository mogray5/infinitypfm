/*
 * Copyright (c) 2005-2013 Wayne Gray All rights reserved
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

import java.sql.SQLException;
import java.util.HashMap;

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
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Currency;
import org.infinitypfm.core.data.CurrencyMethod;

public class UpdateMethodSelectorDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	private Label lblMethod = null;
	private Combo cmbMethod = null;
	private Button cmdSelect = null;
	private Button cmdCancel = null;
	private HashMap<String,CurrencyMethod> hsMethods = null;
	private CurrencyMethod selectedMethod = null;
	
	
	public CurrencyMethod getMethod() {
		return  selectedMethod;
	}

	@Override
	protected void LoadUI(Shell sh) {
		
		hsMethods = new HashMap<String,CurrencyMethod>();
		
		lblMethod = new Label(sh, SWT.NONE);
		lblMethod.setText(MM.PHRASES.getPhrase("226"));
		cmdSelect = new Button(sh, SWT.PUSH);
		cmdSelect.setText(MM.PHRASES.getPhrase("5"));
		cmdSelect.addSelectionListener(cmdSelect_OnClick);
		cmdCancel = new Button(sh, SWT.PUSH);
		cmdCancel.setText(MM.PHRASES.getPhrase("4"));
		cmdCancel.addSelectionListener(cmdCancel_OnClick);
		
		cmbMethod = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);
		
		try {
			CurrencyMethod method = null;
			@SuppressWarnings("rawtypes")
			java.util.List list = MM.sqlMap.selectList("getAllCurrencyMethods");
			
			cmbMethod.removeAll();
			
			for (int i=0; i<list.size(); i++) {
				
				method = (CurrencyMethod)list.get(i);
				Currency currency = (Currency)MM.sqlMap.selectOne("getCurrencyById", method.getCurrencyID());
				
				if (currency != null){
				
					String sName = method.getMethodName() + " (" + currency.getIsoName() + ")";
					
					cmbMethod.add(sName);
					hsMethods.put(sName, method);
					
				}
				
			}			
	
			cmbMethod.select(0);
			
		} catch (Exception se){
			InfinityPfm.LogMessage(se.getMessage());
		}
		
	}

	@Override
	protected void LoadLayout() {

		FormData lblmethoddata = new FormData();
		lblmethoddata.top = new FormAttachment(0, 40);
		lblmethoddata.left = new FormAttachment(20, 0);
		lblMethod.setLayoutData(lblmethoddata);
		
		FormData cmbmethoddata = new FormData();
		cmbmethoddata.top = new FormAttachment(0, 40);
		cmbmethoddata.left = new FormAttachment(lblMethod, 10);
		cmbmethoddata.right = new FormAttachment(100, -30);
		cmbMethod.setLayoutData(cmbmethoddata);
		
		FormData cmdcanceldata = new FormData();
		cmdcanceldata.top = new FormAttachment(lblMethod, 40);
		//cmdcanceldata.left = new FormAttachment(cmdOne, 10);
		cmdcanceldata.right = new FormAttachment(40,10);
		cmdCancel.setLayoutData(cmdcanceldata);

		FormData cmdadddata = new FormData();
		cmdadddata.top = new FormAttachment(lblMethod, 40);
		cmdadddata.left = new FormAttachment(cmdCancel, 10);
		cmdadddata.right = new FormAttachment(60,10);
		cmdSelect.setLayoutData(cmdadddata);
	}
		

	@Override
	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("231"));
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

	/*
	 * Listeners
	 */
	SelectionAdapter cmdSelect_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			selectedMethod = hsMethods.get(cmbMethod.getText());
			shell.dispose();
		}
	};

	SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			userCancelled = true;
			selectedMethod = null;
			shell.dispose();
		}
	};
	

	
}
