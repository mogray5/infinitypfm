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

package org.infinitypfm.ui.view.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.conf.MM;

public class DateDialog extends BaseDialog {

	
	private DateTime calendar = null;
	private Button ok = null;
	private String selectedDate = null;
	
	/* (non-Javadoc)
	 * @see net.mogray.quezen.ui.view.QZDialog#Open()
	 */
	public int Open() {
		super.Open();
		shell.setText(MM.PHRASES.getPhrase("163"));

		shell.setSize(300, 300);
		shell.open();
		Display display = parent.getDisplay();
		this.CenterWindow();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		return MM.CANCEL;
	}
	
	@Override
	protected void LoadUI(Shell sh) {
		 calendar = new DateTime (sh, SWT.CALENDAR | SWT.BORDER);
		 ok = new Button (sh, SWT.PUSH);
		 ok.setText(MM.PHRASES.getPhrase("5"));
		 ok.addSelectionListener(cmdOk_OnClick);
	}

	@Override
	protected void LoadLayout() {
		FormData calendardata = new FormData();
		calendardata.top = new FormAttachment(0,10);
		calendardata.bottom = new FormAttachment(100,-50);
		calendardata.left = new FormAttachment(0,10);
		calendardata.right = new FormAttachment(100, -10);
		calendar.setLayoutData(calendardata);
		
		FormData okdata = new FormData();
		okdata.top = new FormAttachment(100, -40);
		okdata.left = new FormAttachment(100,-50);
		okdata.right = new FormAttachment(100, -20);
		ok.setLayoutData(okdata);
		
	}
	
	public String getSelectedDate() {
		return selectedDate;
	}
	
	/*
	 * Listeners
	 */
	
	SelectionAdapter cmdOk_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			selectedDate = (calendar.getMonth () + 1) + "-" + calendar.getDay () + "-" + calendar.getYear ();
			shell.dispose();
		}
	};

}
