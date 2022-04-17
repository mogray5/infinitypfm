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
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.ui.view.toolbars.ReportToolbar;

public class BookmarksView extends BaseView {

	private Browser browser = null;
	private String reportUrl = null;
	
	public BookmarksView(Composite arg0, int arg1) {
		super(arg0, arg1);
		// init console
		LoadUI();
		LoadLayout();
	}

	protected void LoadUI() {
		browser = new Browser(this, SWT.WEBKIT);
	}

	protected void LoadLayout() {
		
		FormData browserdata = new FormData();
		browserdata.top = new FormAttachment(0,0);
		browserdata.bottom = new FormAttachment(100,0);
		browserdata.left = new FormAttachment(0,0);
		browserdata.right = new FormAttachment(100,0);
		browser.setLayoutData(browserdata);
		
	}
	
	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;

		if (browser != null)
			browser.setUrl(reportUrl);
		else
			InfinityPfm.LogMessage(MM.PHRASES.getPhrase("316"));
		
	}

	public void QZDispose() {
		this.dispose();
	}
	
}
