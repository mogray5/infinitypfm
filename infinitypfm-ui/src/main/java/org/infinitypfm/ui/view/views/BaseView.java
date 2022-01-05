/*
 * Copyright (c) 2005-2022 Wayne Gray All rights reserved
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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.infinitypfm.ui.Widget;
import org.infinitypfm.ui.view.toolbars.BaseToolbar;

/**
 * Common functions for views 
 *
 */
public abstract class BaseView extends Composite implements Widget {

	protected Label lblTitle = null;

	private Font fontBold = null;

	private BaseToolbar toolbar = null;

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BaseView(Composite arg0, int arg1) {
		super(arg0, arg1);
	}

	protected abstract void LoadUI();

	public void Refresh() {
		this.layout(true);
	}

	public void AddTitle(String sTitle) {

		if (lblTitle == null) {
			lblTitle = new Label(this, SWT.FLAT);
		}

		lblTitle.setText(sTitle);

		Font f = lblTitle.getFont();
		FontData[] fd = f.getFontData();

		for (int i = 0; i < fd.length; i++) {
			fd[i].setStyle(SWT.BOLD);
		}

		if (fontBold != null) {
			fontBold.dispose();
		}
		fontBold = new Font(this.getDisplay(), fd);
		lblTitle.setFont(fontBold);

		this.LoadLayout();
	}

	public void setToolBar(BaseToolbar tb) {
		toolbar = tb;
	}

	public BaseToolbar getToolBar() {
		return toolbar;
	}

	/*
	 * SWT object cleanup
	 */
	public void QZDispose() {
		if (!fontBold.isDisposed()) {
			fontBold.dispose();
		}

		toolbar.QZDispose();
		
	}

	protected void LoadLayout() {

		if (lblTitle != null) {
			FormData lbltitledata = new FormData();
			lbltitledata.top = new FormAttachment(0, 5);
			lbltitledata.left = new FormAttachment(0, 5);
			lbltitledata.right = new FormAttachment(50, 0);
			//lbltitledata.bottom = new FormAttachment(20,0);
			lblTitle.setLayoutData(lbltitledata);
		}

		if (toolbar != null) {

			FormData tbqueuedata = new FormData();
			tbqueuedata.top = new FormAttachment(0, 0);
			//tbqueuedata.left = new FormAttachment(100, -180);
			tbqueuedata.right = new FormAttachment(100, 0);
			toolbar.getToolbar().setLayoutData(tbqueuedata);

		}

	}

	public Table getTable() {
		return null;
	}
}
