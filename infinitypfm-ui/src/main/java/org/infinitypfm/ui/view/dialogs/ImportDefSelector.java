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

import java.sql.SQLException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.ImportDef;

public class ImportDefSelector extends BaseDialog {

	private Composite cmpAddEditDef = null;
	private Composite cmpDefList = null;
	private Composite cmpSelectedDef = null;
	private Button cmdClose = null;
	private Button cmdAdd = null;
	private Button cmdDelete = null;
	
	private Label lblImportName = null;
	private Label lblImportType = null;
	private Label lblDateField = null;
	private Label lblDateFormat = null;
	private Label lblMemoField = null;
	private Label lblAmountField = null;
	
	private Text txtImportName = null;
	private Combo cmbImportType = null;
	private Text txtDateField = null;
	private Text txtDateFormat = null;
	private Text txtMemoField = null;
	private Text txtAmountField = null;
	
	private Table tblImportDefs = null;
	
	private Label lblSelectedName = null;
	
	private ImportDef selectedDef = null;
	
	@Override
	protected void LoadUI(Shell sh) {

		LoadCompositeUI(sh);
		LoadAddUI();
		
		tblImportDefs = new Table(cmpDefList, SWT.MULTI | SWT.HIDE_SELECTION);
		tblImportDefs.setLinesVisible(true);
		
		LoadColumns();
		
		tblImportDefs.addSelectionListener(tblImportDefs_OnSelect);
		
		lblSelectedName = new Label(cmpSelectedDef, SWT.NONE);
		lblSelectedName.setText(MM.PHRASES.getPhrase("259"));
		
		try {
			ReloadImportDefList();
		} catch (SQLException e) {
			InfinityPfm.LogMessage(e.getMessage(), true);
		}
		
	}

	@Override
	protected void LoadLayout() {
		
		LoadCompositeLayout();
		LoadAddUILayout();
		
		FormData tblimportdefsdata = new FormData();
		tblimportdefsdata.top = new FormAttachment(0, 10);
		tblimportdefsdata.left = new FormAttachment(0, 10);
		tblimportdefsdata.right = new FormAttachment(100, -10);
		tblimportdefsdata.bottom = new FormAttachment(100, -10);
		tblImportDefs.setLayoutData(tblimportdefsdata);
		
		FormData lblselectednamedata = new FormData();
		lblselectednamedata.top = new FormAttachment(0, 5);
		lblselectednamedata.left = new FormAttachment(50, 0);
		lblselectednamedata.right = new FormAttachment(50, 100);
		//lblselectednamedata.bottom = new FormAttachment(100, -10);
		lblSelectedName.setLayoutData(lblselectednamedata);
		
	}
	
	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("139"));
		shell.setSize(850, 550);
		this.CenterWindow();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		if (selectedDef != null) {
			return (int) selectedDef.getImportID();
		} else {
			return -1;
		}
	}
	
	private void LoadCompositeUI(Shell sh) {
		
		cmpAddEditDef = new Composite(sh, SWT.BORDER);
		cmpAddEditDef.setLayout(new FormLayout());
		cmpDefList = new Composite(sh, SWT.BORDER);
		cmpDefList.setLayout(new FormLayout());
		cmpSelectedDef = new Composite(sh, SWT.BORDER);
		cmpSelectedDef.setLayout(new FormLayout());
		cmdClose = new Button(sh, SWT.PUSH);
		cmdClose.setText(MM.PHRASES.getPhrase("261"));
		cmdClose.addSelectionListener(cmdClose_OnClick);
	}
	
	private void LoadCompositeLayout() {
		
		FormData cmpaddeditdefdata = new FormData();
		cmpaddeditdefdata.top = new FormAttachment(0, 10);
		cmpaddeditdefdata.bottom = new FormAttachment(0, 125);
		cmpaddeditdefdata.left = new FormAttachment(0, 10);
		cmpaddeditdefdata.right = new FormAttachment(100, -10);
		cmpAddEditDef.setLayoutData(cmpaddeditdefdata);
		
		FormData cmpdeflistdata = new FormData();
		cmpdeflistdata.top = new FormAttachment(cmpAddEditDef, 10);
		cmpdeflistdata.bottom = new FormAttachment(cmpAddEditDef, 400);
		cmpdeflistdata.left = new FormAttachment(0, 10);
		cmpdeflistdata.right = new FormAttachment(100, -10);
		cmpDefList.setLayoutData(cmpdeflistdata);
		
		FormData cmpselecteddefdata = new FormData();
		cmpselecteddefdata.top = new FormAttachment(cmpDefList, 10);
		cmpselecteddefdata.bottom = new FormAttachment(cmpAddEditDef, 450);
		cmpselecteddefdata.left = new FormAttachment(0, 10);
		cmpselecteddefdata.right = new FormAttachment(100, -10);
		cmpSelectedDef.setLayoutData(cmpselecteddefdata);
		
		FormData cmdclosedata = new FormData();
		cmdclosedata.top = new FormAttachment(100, -50);
		cmdclosedata.bottom = new FormAttachment(100, -15);
		cmdclosedata.left = new FormAttachment(100, -90);
		cmdclosedata.right = new FormAttachment(100, -10);
		cmdClose.setLayoutData(cmdclosedata);
		
	}
	
	private void LoadAddUI() {
		
		lblImportName = new Label(cmpAddEditDef, SWT.NONE);
		lblImportName.setText(MM.PHRASES.getPhrase("254") + ":");
		lblImportType = new Label(cmpAddEditDef, SWT.NONE);
		lblImportType.setText(MM.PHRASES.getPhrase("255") + ":");
		lblDateField = new Label(cmpAddEditDef, SWT.NONE);
		lblDateField.setText(MM.PHRASES.getPhrase("256") + ":");
		lblDateFormat = new Label(cmpAddEditDef, SWT.NONE);
		lblDateFormat.setText(MM.PHRASES.getPhrase("260") + ":");
		lblMemoField = new Label(cmpAddEditDef, SWT.NONE);
		
		lblMemoField.setText(MM.PHRASES.getPhrase("257") + ":");
		lblAmountField = new Label(cmpAddEditDef, SWT.NONE);
		lblAmountField.setText(MM.PHRASES.getPhrase("258") + ":");
		
		txtImportName = new Text(cmpAddEditDef, SWT.BORDER);
		cmbImportType = new Combo(cmpAddEditDef, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbImportType.setItems(new String[] {
				MM.PHRASES.getPhrase("253") });
		cmbImportType.select(0);
		txtDateField = new Text(cmpAddEditDef, SWT.BORDER);
		txtDateFormat = new Text(cmpAddEditDef, SWT.BORDER);
		txtDateFormat.setText("yyyy-M-dd");
		txtMemoField = new Text(cmpAddEditDef, SWT.BORDER);
		txtAmountField = new Text(cmpAddEditDef, SWT.BORDER);
		
		cmdAdd = new Button(cmpAddEditDef, SWT.PUSH);
		cmdAdd.setText(MM.PHRASES.getPhrase("45"));
		cmdAdd.addSelectionListener(cmdAdd_OnClick);
		
		cmdDelete = new Button(cmpAddEditDef, SWT.PUSH);
		cmdDelete.setText(MM.PHRASES.getPhrase("151"));
		cmdDelete.addSelectionListener(cmdDelete_OnClick);
		
	}
	
	private void LoadAddUILayout() {
		
		FormData lblimportnamedata = new FormData();
		lblimportnamedata.top = new FormAttachment(0, 10);
		lblimportnamedata.left = new FormAttachment(0, 10);
		//lblimportnamedata.right = new FormAttachment(0, 100);
		lblImportName.setLayoutData(lblimportnamedata);
		
		FormData txtimportnamedata = new FormData();
		txtimportnamedata.top = new FormAttachment(0, 5);
		txtimportnamedata.left = new FormAttachment(lblImportName, 10);
		txtimportnamedata.right = new FormAttachment(lblImportName, 250);
		txtImportName.setLayoutData(txtimportnamedata);
		
		FormData lblimporttypedata = new FormData();
		lblimporttypedata.top = new FormAttachment(0, 10);
		lblimporttypedata.left = new FormAttachment(lblImportName, 170);
		//lblimporttypedata.right = new FormAttachment(0, 100);
		lblImportType.setLayoutData(lblimporttypedata);

		FormData cmbimporttypedata = new FormData();
		cmbimporttypedata.top = new FormAttachment(0, 5);
		cmbimporttypedata.left = new FormAttachment(lblImportType, 20);
		cmbimporttypedata.right = new FormAttachment(lblImportType, 250);
		cmbImportType.setLayoutData(cmbimporttypedata);

		FormData lbldateformatdata = new FormData();
		lbldateformatdata.top = new FormAttachment(0, 10);
		lbldateformatdata.left = new FormAttachment(lblImportName, 435);
		//lbldateformatdata.right = new FormAttachment(lblImportType, 320);
		lblDateFormat.setLayoutData(lbldateformatdata);
		
		FormData txtdateformatdata = new FormData();
		txtdateformatdata.top = new FormAttachment(0, 5);
		txtdateformatdata.left = new FormAttachment(lblDateFormat, 15);
		txtdateformatdata.right = new FormAttachment(lblDateFormat, 270);
		txtDateFormat.setLayoutData(txtdateformatdata);
		
		FormData lbldatefielddata = new FormData();
		lbldatefielddata.top = new FormAttachment(0, 42);
		lbldatefielddata.left = new FormAttachment(0, 10);
		//lbldatefielddata.right = new FormAttachment(0, 75);
		lblDateField.setLayoutData(lbldatefielddata);
	
		FormData txtdatefielddata = new FormData();
		txtdatefielddata.top = new FormAttachment(0, 37);
		txtdatefielddata.left = new FormAttachment(lblImportName, 10);
		txtdatefielddata.right = new FormAttachment(lblDateField, 250);
		txtDateField.setLayoutData(txtdatefielddata);
		
		FormData lblmemofielddata = new FormData();
		lblmemofielddata.top = new FormAttachment(0, 42);
		lblmemofielddata.left = new FormAttachment(lblImportName, 170);
		//lblmemofielddata.right = new FormAttachment(lblImportType, 195);
		lblMemoField.setLayoutData(lblmemofielddata);
		
		FormData txtmemofielddata = new FormData();
		txtmemofielddata.top = new FormAttachment(0, 37);
		txtmemofielddata.left = new FormAttachment(lblImportType, 20);
		txtmemofielddata.right = new FormAttachment(lblImportType, 250);
		txtMemoField.setLayoutData(txtmemofielddata);
		
		FormData lblamountfielddata = new FormData();
		lblamountfielddata.top = new FormAttachment(0, 42);
		lblamountfielddata.left = new FormAttachment(lblMemoField, 180);
		//lblamountfielddata.right = new FormAttachment(0, 100);
		lblAmountField.setLayoutData(lblamountfielddata);
		
		FormData txtamountfielddata = new FormData();
		txtamountfielddata.top = new FormAttachment(0, 37);
		txtamountfielddata.left = new FormAttachment(lblAmountField, 10);
		txtamountfielddata.right = new FormAttachment(lblAmountField, 250);
		txtAmountField.setLayoutData(txtamountfielddata);
		
		FormData cmdadddata = new FormData();
		cmdadddata.top = new FormAttachment(lblAmountField, 15);
		cmdadddata.left = new FormAttachment(35, 0);
		cmdadddata.right = new FormAttachment(35, 70);
		cmdAdd.setLayoutData(cmdadddata);
		
		FormData cmddeletedata = new FormData();
		cmddeletedata.top = new FormAttachment(lblAmountField, 15);
		cmddeletedata.left = new FormAttachment(45, 0);
		cmddeletedata.right = new FormAttachment(45, 70);
		cmdDelete.setLayoutData(cmddeletedata);
		
	}
	
	private void LoadColumns() {
		
		TableColumn tc1 = new TableColumn(tblImportDefs, SWT.CENTER);
		TableColumn tc2 = new TableColumn(tblImportDefs, SWT.LEFT);
		TableColumn tc3 = new TableColumn(tblImportDefs, SWT.LEFT);
		TableColumn tc4 = new TableColumn(tblImportDefs, SWT.CENTER);
		TableColumn tc5 = new TableColumn(tblImportDefs, SWT.CENTER);
		TableColumn tc6 = new TableColumn(tblImportDefs, SWT.CENTER);

		tc1.setText(MM.PHRASES.getPhrase("254"));
		tc2.setText(MM.PHRASES.getPhrase("255"));
		tc3.setText(MM.PHRASES.getPhrase("256"));
		tc4.setText(MM.PHRASES.getPhrase("260"));
		tc5.setText(MM.PHRASES.getPhrase("257"));
		tc6.setText(MM.PHRASES.getPhrase("258"));

		tc1.setWidth(150);
		tc2.setWidth(150);
		tc3.setWidth(150);
		tc4.setWidth(150);
		tc5.setWidth(150);
		tc6.setWidth(90);

		tblImportDefs.setHeaderVisible(true);

	}
	
	@SuppressWarnings("unchecked")
	private void ReloadImportDefList() throws SQLException {
		
		
		tblImportDefs.removeAll();
		
		List<ImportDef> defList = MM.sqlMap.selectList("getImportDefs");
		
		if (defList != null && defList.size() > 0) {
			
			for (ImportDef def : defList) {
				
				TableItem ti = new TableItem(tblImportDefs, SWT.NONE);
				
				ti.setText(0, def.getImportName());
				ti.setText(1, def.getImportType());
				ti.setText(2, def.getDateField());
				ti.setText(3, def.getDateFormat());
				ti.setText(4, def.getMemoField());
				ti.setText(5, def.getAmountField());
				ti.setData(def);
			}
			
		}
		
	}

	/*
	 * Listeners
	 */
	
	SelectionAdapter cmdClose_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			shell.dispose();
		}
	};
	
	SelectionAdapter cmdAdd_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			boolean noErrors = true;
			
			if (txtImportName.getText().length() > 0
				&& txtDateField.getText().length() > 0
				&& txtMemoField.getText().length() > 0
				&& txtAmountField.getText().length() >0
				&& txtDateFormat.getText().length() > 0) {
				
				ImportDef def = new ImportDef();
				def.setImportName(txtImportName.getText());
				def.setImportType(cmbImportType.getText());
				def.setDateField(txtDateField.getText());
				def.setDateFormat(txtDateFormat.getText());
				def.setMemoField(txtMemoField.getText());
				def.setAmountField(txtAmountField.getText());
				
				// Check if name not already in use
				try {
					ImportDef txt = (ImportDef) MM.sqlMap.selectList("getImportDef", def);
				} catch (Exception e1) {
					noErrors = false;
					InfinityPfm.LogMessage(e1.getMessage(), true);
				}
				
				if (noErrors) {
					
					try {
						MM.sqlMap.insert("insertImportDef", def);
						//MM.sqlMap.commitTransaction();
						ReloadImportDefList();
					} catch (SQLException e1) {
						noErrors = false;
						InfinityPfm.LogMessage(e1.getMessage(), true);
					}
				}
			}
		}
	};

	SelectionAdapter cmdDelete_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			TableItem[] rows = tblImportDefs.getSelection();
			
			if (rows != null && rows.length > 0) {
				
				ImportDef def = (ImportDef) rows[0].getData();
				
				try {
					MM.sqlMap.delete("deleteImportDef", def);
					ReloadImportDefList();
				} catch (SQLException e1) {
					InfinityPfm.LogMessage(e1.getMessage(), true);
				}
		}
		}
	};
	
	SelectionAdapter tblImportDefs_OnSelect = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			
			TableItem[] rows = tblImportDefs.getSelection();
			
			if (rows != null && rows.length > 0) {
				
				ImportDef def = (ImportDef) rows[0].getData();
				
				lblSelectedName.setText(def.getImportName());
				selectedDef = def;
			}
			
		}
	};
	
}
