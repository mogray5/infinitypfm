/*
 * Copyright (c) 2005-2017 Wayne Gray All rights reserved
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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;

public class AboutDialog extends BaseDialog {

	/*
	 * Widgets
	 */
	
	private Composite grpTopBorder = null;
	private Group grpBotBorder = null;
	private Canvas logo = null;
	private Label lblVersion = null;
	private Label lblVersionDat = null;
	private Label lblLink = null;
	private Label lblLinkDat = null;
	private Label lblUse = null;
	private Label lblUsedBy = null;
	private Label lblUsedBy2 = null;
	private Label lblUsedBy3 = null;
	private Label lblUsedBy4 = null;
	private Label lblCopyRight = null;
	private Label lblLic = null;
	private Image banner = null;
	private Button cmdOk = null;
	private Color color = null;
			
	/**
	 * 
	 */
	public AboutDialog() {
		super();
	}

	protected void LoadUI(Shell sh) {
		
		shell.setText(MM.PHRASES.getPhrase("61") + " " +
				MM.APPTITLE);
		
		color = new Color(InfinityPfm.shMain.getDisplay(), 255,255,255);
		grpTopBorder = new Composite(sh, SWT.NONE);
		grpTopBorder.setLayout(new FormLayout());
		grpTopBorder.setBackground(color);
		grpBotBorder = new Group(sh, SWT.NONE);
		grpBotBorder.setLayout(new FormLayout());
		banner = InfinityPfm.imMain.getTransparentImage(MM.IMG_QUEZEN_BANNER_SMALL);
		logo = new Canvas(grpTopBorder, SWT.NONE);
		logo.setBackground(color);
		logo.addPaintListener(logo_OnPaint);
		
		lblVersion = new Label(grpTopBorder, SWT.NONE);
		lblVersionDat  = new Label(grpTopBorder, SWT.NONE);
		lblLink = new Label(grpTopBorder, SWT.NONE);
		lblLinkDat = new Label(grpTopBorder, SWT.NONE);
		lblUse = new Label(grpBotBorder, SWT.NONE);
		lblUsedBy = new Label(grpBotBorder, SWT.NONE);
		lblUsedBy2 = new Label(grpBotBorder, SWT.NONE);
		lblUsedBy3 = new Label(grpBotBorder, SWT.NONE);
		lblUsedBy4 = new Label(grpBotBorder, SWT.NONE);
		lblCopyRight = new Label(grpBotBorder, SWT.NONE);
		lblLic = new Label(grpBotBorder, SWT.NONE);
		
		lblVersion.setText(MM.PHRASES.getPhrase("71") + ":");
		lblVersion.setBackground(color);
		lblVersionDat.setText(MM.options.getAppVersion());
		lblVersionDat.setBackground(color);
		lblLink.setText(MM.PHRASES.getPhrase("72") + ":");
		lblLink.setBackground(color);
		lblLinkDat.setText(MM.APPLINK);
		lblLinkDat.setBackground(color);
		lblUse.setText(MM.PHRASES.getPhrase("73"));
		lblUsedBy.setText(MM.PHRASES.getPhrase("75"));
		lblUsedBy2.setText(MM.PHRASES.getPhrase("74"));
		lblUsedBy3.setText(MM.PHRASES.getPhrase("124"));
		lblUsedBy4.setText(MM.PHRASES.getPhrase("248"));
		lblCopyRight.setText(MM.APPCOPYRIGHT);
		lblCopyRight.setAlignment(SWT.CENTER);
		lblLic.setText(MM.APPLICENCE);
		lblLic.setAlignment(SWT.CENTER);
		
		cmdOk = new Button(sh, SWT.PUSH);
		cmdOk.setText(MM.PHRASES.getPhrase("5"));
		cmdOk.addSelectionListener(cmdOk_OnClick);
	}

	protected void LoadLayout() {
		
		FormData grptopdata = new FormData();
		grptopdata.top = new FormAttachment(0, 10);
		grptopdata.left = new FormAttachment(0, 10);
		grptopdata.right = new FormAttachment(100, -10);
		grptopdata.bottom = new FormAttachment(26, 0);
		grpTopBorder.setLayoutData(grptopdata);
		
		FormData grpbotdata = new FormData();
		grpbotdata.top = new FormAttachment(grpTopBorder, 0);
		grpbotdata.left = new FormAttachment(0, 10);
		grpbotdata.right = new FormAttachment(100, 0);
		grpbotdata.bottom = new FormAttachment(85, 0);
		grpBotBorder.setLayoutData(grpbotdata);
		
		FormData logodata = new FormData();
		logodata.top = new FormAttachment(0, 0);
		logodata.left = new FormAttachment(0, 0);
		logodata.right = new FormAttachment(50, 0);
		logo.setLayoutData(logodata);
		
		FormData lblversiondata = new FormData();
		lblversiondata.top = new FormAttachment(0, 5);
		lblversiondata.left = new FormAttachment(logo, 0);
		lblVersion.setLayoutData(lblversiondata);

		FormData lblversiondatdata = new FormData();
		lblversiondatdata.top = new FormAttachment(0, 5);
		lblversiondatdata.left = new FormAttachment(lblVersion, 3);
		lblVersionDat.setLayoutData(lblversiondatdata);
		
		FormData lbllinkdata = new FormData();
		lbllinkdata.top = new FormAttachment(lblVersion, 5);
		lbllinkdata.left = new FormAttachment(logo, 0);
		lblLink.setLayoutData(lbllinkdata);

		FormData lbllinkdatdata = new FormData();
		lbllinkdatdata.top = new FormAttachment(lblVersion, 5);
		lbllinkdatdata.left = new FormAttachment(lblVersion, 3);
		lblLinkDat.setLayoutData(lbllinkdatdata);

		FormData lblusedata = new FormData();
		lblusedata.top = new FormAttachment(0, 10);
		lblusedata.left = new FormAttachment(0, 10);
		lblUse.setLayoutData(lblusedata);
		
		FormData lblusedbydata = new FormData();
		lblusedbydata.top = new FormAttachment(lblUse, 10);
		lblusedbydata.left = new FormAttachment(0, 20);
		lblUsedBy.setLayoutData(lblusedbydata);

		FormData lblusedby2data = new FormData();
		lblusedby2data.top = new FormAttachment(lblUsedBy, 3);
		lblusedby2data.left = new FormAttachment(0, 20);
		lblUsedBy2.setLayoutData(lblusedby2data);
		
		FormData lblusedby3data = new FormData();
		lblusedby3data.top = new FormAttachment(lblUsedBy2, 3);
		lblusedby3data.left = new FormAttachment(0, 20);
		lblUsedBy3.setLayoutData(lblusedby3data);
		
		FormData lblusedby4data = new FormData();
		lblusedby4data.top = new FormAttachment(lblUsedBy3, 3);
		lblusedby4data.left = new FormAttachment(0, 20);
		lblUsedBy4.setLayoutData(lblusedby4data);
		
		FormData lbllicdata = new FormData();
		lbllicdata.top = new FormAttachment(70, 10);
		lbllicdata.left = new FormAttachment(0, 0);
		lbllicdata.right = new FormAttachment(100, 0);
		lblLic.setLayoutData(lbllicdata);
		
		FormData lblcopyrightdata = new FormData();
		lblcopyrightdata.top = new FormAttachment(lblLic, 5);
		lblcopyrightdata.left = new FormAttachment(0, 0);
		lblcopyrightdata.right = new FormAttachment(100, 0);
		lblCopyRight.setLayoutData(lblcopyrightdata);
		
		FormData cmdokdata = new FormData();
		cmdokdata.top = new FormAttachment(grpBotBorder, 10);
		cmdokdata.left = new FormAttachment(45, 0);
		cmdokdata.right = new FormAttachment(55, 0);
		cmdOk.setLayoutData(cmdokdata);
	}

	public int Open() {
		super.Open();

		shell.setSize(650, 375);
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
	
	SelectionAdapter cmdOk_OnClick = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e){
			shell.dispose();
		}
	};
	
	PaintListener logo_OnPaint = new PaintListener() {
        public void paintControl(PaintEvent e) {
         e.gc.drawImage(banner,0,0);
        }
    };

}
