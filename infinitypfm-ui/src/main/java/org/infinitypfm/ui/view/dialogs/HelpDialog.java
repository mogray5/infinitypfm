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
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;

/**
 * @author Wayne Gray
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HelpDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	private Browser browser = null;
	private Label lblStatus = null;
	private ProgressBar pbProgress = null;
	private Button cmdForward = null;
	private Button cmdBack = null;
	
	/**
	 * 
	 */
	public HelpDialog() {
		super();
	}

	/* (non-Javadoc)
	 * @see net.mogray.quezen.ui.view.QZDialog#LoadUI(org.eclipse.swt.widgets.Shell)
	 */
	protected void LoadUI(Shell sh) {
		browser = new Browser(sh, SWT.NONE);
		pbProgress = new ProgressBar(shell, SWT.NONE);
		browser.addProgressListener(Browser_OnProgress);
		browser.addStatusTextListener(Browser_OnStatusText);
		//browser.addLocationListener(Browser_OnLocation);
		browser.setUrl(MM.HELPPATH);
		lblStatus = new Label(sh, SWT.NONE);
		cmdForward = new Button (sh, SWT.PUSH);
		cmdForward.setImage(InfinityPfm.imMain.getImage(MM.IMG_HELPFORWARD));
		cmdForward.addSelectionListener(cmdForward_OnClick);
		cmdBack = new Button(sh, SWT.PUSH);
		cmdBack.setImage(InfinityPfm.imMain.getImage(MM.IMG_HELPBACK));
		cmdBack.addSelectionListener(cmdBack_OnClick);
	}

	/* (non-Javadoc)
	 * @see net.mogray.quezen.ui.view.QZDialog#LoadLayout()
	 */
	protected void LoadLayout() {
		FormData browserdata = new FormData();
		browserdata.top = new FormAttachment(0,40);
		browserdata.bottom = new FormAttachment(100,-25);
		browserdata.left = new FormAttachment(0,10);
		browserdata.right = new FormAttachment(100,-10);
		browser.setLayoutData(browserdata);
		
		FormData pbprogressdata = new FormData();
		pbprogressdata.top = new FormAttachment(browser, 5);
		pbprogressdata.right = new FormAttachment(100, -10);
		pbProgress.setLayoutData(pbprogressdata);
		
		FormData lblstatusdata = new FormData();
		lblstatusdata.top = new FormAttachment(browser, 5);
		lblstatusdata.left = new FormAttachment(0, 10);
		lblstatusdata.right = new FormAttachment(50, 0);
		lblStatus.setLayoutData(lblstatusdata);
		
		FormData cmdforwarddata = new FormData();
		cmdforwarddata.top = new FormAttachment(0, 5);
		cmdforwarddata.right = new FormAttachment(100, -10);
		cmdForward.setLayoutData(cmdforwarddata);
		
		FormData cmdbackdata = new FormData();
		cmdbackdata.top = new FormAttachment(0, 5);
		cmdbackdata.right = new FormAttachment(cmdForward, -10);
		cmdBack.setLayoutData(cmdbackdata);
		
	}

	/* (non-Javadoc)
	 * @see net.mogray.quezen.ui.view.QZDialog#Open()
	 */
	public int Open() {
		parent = getParent();
		shell = new Shell(parent, SWT.MIN | SWT.MAX | SWT.CLOSE | SWT.RESIZE);
		shell.setLayout(new FormLayout());
		shell.setLocation(150, 150);
		LoadUI(shell);
		LoadLayout();
		shell.setText(MM.APPTITLE + " - " +
				MM.PHRASES.getPhrase("62"));

		shell.setSize(700, 500);
		this.CenterWindow();
		
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		return 1;
	}
	
	/*
	 * Listeners
	 */
	ProgressListener Browser_OnProgress = new ProgressListener()
		{
			public void changed(ProgressEvent event) {
					if (event.total == 0) return;                            
					int ratio = event.current * 100 / event.total;
					pbProgress.setSelection(ratio);
			}
			public void completed(ProgressEvent event) {
				pbProgress.setSelection(0);
			}
		};
		
	StatusTextListener Browser_OnStatusText = new StatusTextListener() {
		public void changed(StatusTextEvent event) {
			lblStatus.setText(event.text);	
		}
	};
	
	SelectionAdapter cmdForward_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			browser.forward();
		}
	};
	
	SelectionAdapter cmdBack_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			browser.back();
		}
	};
}
