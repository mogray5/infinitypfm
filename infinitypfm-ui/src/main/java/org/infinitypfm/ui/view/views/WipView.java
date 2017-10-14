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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.infinitypfm.conf.MM;

/**
 * @author Wayne Gray
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class WipView extends BaseView {

	/*
	 * Widgets
	 */
	private Label lblMsg = null;


	/**
	 * @param arg0
	 * @param arg1
	 */
	public WipView(Composite arg0, int arg1) {
		super(arg0, arg1);
		this.setLayout(new FormLayout());
		LoadUI();
		LoadLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.view.QZView#LoadUI()
	 */
	protected void LoadUI() {
		lblMsg = new Label(this, SWT.BORDER | SWT.WRAP);
		lblMsg.setText(MM.PHRASES.getPhrase("53"));
		lblMsg.setAlignment(SWT.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.view.QZView#LoadLayout()
	 */
	protected void LoadLayout() {

		FormData lblmsgdata = new FormData();
		lblmsgdata.top = new FormAttachment(40, 0);
		lblmsgdata.left = new FormAttachment(20, 0);
		lblmsgdata.right = new FormAttachment(80, 0);
		lblmsgdata.bottom = new FormAttachment(60, 0);
		lblMsg.setLayoutData(lblmsgdata);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.view.QZView#QZDispose()
	 */
	public void QZDispose() {
		if (!this.isDisposed()) {
			this.dispose();
		}
	}

}
