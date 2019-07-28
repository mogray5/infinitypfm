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
package org.infinitypfm.ui.view.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;

public class BasisView extends BaseView {
	private Text txtQtyLifo;
	private Table tblBasis;
	private Text txtQtyFifo;
	private Text txtCost;


	public BasisView(Composite arg0, int arg1) {
		super(arg0, arg1);
		setLayout(new FormLayout());
		
		Composite composite = new Composite(this, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 642);
		fd_composite.right = new FormAttachment(0, 918);
		fd_composite.top = new FormAttachment(0, 35);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new FormLayout());
		
		Group group = new Group(composite, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.left = new FormAttachment(0, 23);
		fd_group.top = new FormAttachment(0, 30);
		fd_group.right = new FormAttachment(100, -26);
		group.setLayoutData(fd_group);
		group.setLayout(new FormLayout());
		
		Label lblAquireCurrency = new Label(group, SWT.NONE);
		FormData fd_lblAquireCurrency = new FormData();
		fd_lblAquireCurrency.left = new FormAttachment(0, 10);
		lblAquireCurrency.setLayoutData(fd_lblAquireCurrency);
		lblAquireCurrency.setText("Aquire Currency:");
		
		Combo cmbAquireCurrency = new Combo(group, SWT.NONE);
		FormData fd_cmbAquireCurrency = new FormData();
		fd_cmbAquireCurrency.left = new FormAttachment(lblAquireCurrency, 18);
		fd_cmbAquireCurrency.top = new FormAttachment(0, 10);
		cmbAquireCurrency.setLayoutData(fd_cmbAquireCurrency);
		
		Label lblAquireDate = new Label(group, SWT.NONE);
		FormData fd_lblAquireDate = new FormData();
		lblAquireDate.setLayoutData(fd_lblAquireDate);
		lblAquireDate.setText("Aquire:\nDate");
		
		DateTime dtAquireDate = new DateTime(group, SWT.BORDER);
		FormData fd_dtAquireDate = new FormData();
		fd_dtAquireDate.bottom = new FormAttachment(lblAquireDate, 0, SWT.BOTTOM);
		fd_dtAquireDate.left = new FormAttachment(lblAquireDate, 6);
		dtAquireDate.setLayoutData(fd_dtAquireDate);
		
		Label lblQtyLifo = new Label(group, SWT.NONE);
		FormData fd_lblQtyLifo = new FormData();
		fd_lblQtyLifo.top = new FormAttachment(0, 22);
		fd_lblQtyLifo.left = new FormAttachment(cmbAquireCurrency, 35);
		lblQtyLifo.setLayoutData(fd_lblQtyLifo);
		lblQtyLifo.setText("Qty Lifo:");
		
		txtQtyLifo = new Text(group, SWT.BORDER);
		FormData fd_txtQtyLifo = new FormData();
		fd_txtQtyLifo.bottom = new FormAttachment(lblAquireCurrency, 0, SWT.BOTTOM);
		fd_txtQtyLifo.left = new FormAttachment(lblQtyLifo, 6);
		txtQtyLifo.setLayoutData(fd_txtQtyLifo);
		
		tblBasis = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		fd_group.bottom = new FormAttachment(tblBasis, -6);
		FormData fd_tblBasis = new FormData();
		fd_tblBasis.top = new FormAttachment(0, 206);
		fd_tblBasis.bottom = new FormAttachment(100, 63);
		
		Label lblQtyFifo = new Label(group, SWT.NONE);
		FormData fd_lblQtyFifo = new FormData();
		fd_lblQtyFifo.top = new FormAttachment(lblQtyLifo, 32);
		fd_lblQtyFifo.left = new FormAttachment(lblQtyLifo, 0, SWT.LEFT);
		lblQtyFifo.setLayoutData(fd_lblQtyFifo);
		lblQtyFifo.setText("Qty Fifo:");
		
		txtQtyFifo = new Text(group, SWT.BORDER);
		FormData fd_txtQtyFifo = new FormData();
		fd_txtQtyFifo.top = new FormAttachment(txtQtyLifo, 20);
		fd_txtQtyFifo.left = new FormAttachment(txtQtyLifo, 0, SWT.LEFT);
		txtQtyFifo.setLayoutData(fd_txtQtyFifo);
		
		Label lblCost = new Label(group, SWT.NONE);
		fd_lblAquireDate.top = new FormAttachment(lblCost, 26);
		fd_lblAquireDate.left = new FormAttachment(lblCost, 0, SWT.LEFT);
		FormData fd_lblCost = new FormData();
		fd_lblCost.bottom = new FormAttachment(lblAquireCurrency, 0, SWT.BOTTOM);
		fd_lblCost.left = new FormAttachment(txtQtyLifo, 29);
		lblCost.setLayoutData(fd_lblCost);
		lblCost.setText("Cost:");
		
		txtCost = new Text(group, SWT.BORDER);
		FormData fd_txtCost = new FormData();
		fd_txtCost.bottom = new FormAttachment(lblAquireCurrency, 0, SWT.BOTTOM);
		fd_txtCost.left = new FormAttachment(lblCost, 16);
		txtCost.setLayoutData(fd_txtCost);
		
		Label lblCostCurrency = new Label(group, SWT.NONE);
		fd_lblAquireCurrency.bottom = new FormAttachment(100, -129);
		FormData fd_lblCostCurrency = new FormData();
		fd_lblCostCurrency.top = new FormAttachment(0, 59);
		fd_lblCostCurrency.right = new FormAttachment(lblAquireCurrency, 0, SWT.RIGHT);
		lblCostCurrency.setLayoutData(fd_lblCostCurrency);
		lblCostCurrency.setText("Cost Currency:");
		
		Combo combo = new Combo(group, SWT.NONE);
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(cmbAquireCurrency, 11);
		fd_combo.left = new FormAttachment(cmbAquireCurrency, 0, SWT.LEFT);
		combo.setLayoutData(fd_combo);
		
		Button cmdAdd = new Button(group, SWT.NONE);
		FormData fd_cmdAdd = new FormData();
		fd_cmdAdd.top = new FormAttachment(combo, 17);
		fd_cmdAdd.left = new FormAttachment(0, 231);
		cmdAdd.setLayoutData(fd_cmdAdd);
		cmdAdd.setText("Add");
		fd_tblBasis.left = new FormAttachment(0, 47);
		fd_tblBasis.right = new FormAttachment(100, -137);
		tblBasis.setLayoutData(fd_tblBasis);
		tblBasis.setHeaderVisible(true);
		tblBasis.setLinesVisible(true);
		LoadUI();
		LoadLayout();
	}

	@Override
	protected void LoadUI() {
		// TODO Auto-generated method stub
		
	}
}