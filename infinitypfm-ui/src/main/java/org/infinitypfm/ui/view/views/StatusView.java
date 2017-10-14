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
import org.eclipse.swt.widgets.ProgressBar;
import org.infinitypfm.conf.MM;
import org.eclipse.swt.widgets.Display;


/**
 * @author wggray
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StatusView extends BaseView {

	/*
	 * Widgets
	 */
	private Label lblStatus = null;
	private Label lblNote = null;
	private ProgressBar bar = null; 
	
	private boolean bBusy = false;
	private int i;

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StatusView(Composite arg0, int arg1) {
		super(arg0, arg1);
		this.setLayout(new FormLayout());
		LoadUI();
		LoadLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.view.views.QZView#LoadUI()
	 */
	protected void LoadUI() {
		
		lblStatus = new Label(this, SWT.FLAT);
		lblStatus.setText(MM.PHRASES.getPhrase("87"));
		lblNote = new Label(this, SWT.FLAT);
		bar = new ProgressBar (this, SWT.SMOOTH);
		bar.setVisible(false);
		
		bar.setMaximum(5);
		
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.view.views.QZView#LoadLayout()
	 */
	protected void LoadLayout() {
		FormData lblstatusdata = new FormData();
		lblstatusdata.top = new FormAttachment(0, 5);
		lblstatusdata.left = new FormAttachment(0, 10);
		lblstatusdata.bottom = new FormAttachment(100, 0);
		lblstatusdata.right = new FormAttachment(50, 0);
		lblStatus.setLayoutData(lblstatusdata);

		FormData lblnotedata = new FormData();
		lblnotedata.top = new FormAttachment(0, 2);
		lblnotedata.left = new FormAttachment(lblStatus, 2);
		lblnotedata.bottom = new FormAttachment(100, -2);
		lblnotedata.right = new FormAttachment(100, -60);
		lblNote.setLayoutData(lblnotedata);

		FormData bardata = new FormData();
		bardata.top = new FormAttachment(0, 2);
		bardata.left = new FormAttachment(lblNote, 10);
		bardata.right = new FormAttachment(100, -2);
		bar.setLayoutData(bardata);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mogray.quezen.ui.view.views.QZView#QZDispose()
	 */
	public void QZDispose() {
		if (!this.isDisposed()) {
			this.dispose();
		}
	}

	public void setStatus(String s) {
		lblStatus.setText(s);
	}

	public void Reset() {
		lblStatus.setText(MM.PHRASES.getPhrase("87"));
	}
	
	/**
	 * @return Returns the bBusy.
	 */
	public synchronized boolean isBusy() {
		return bBusy;
	}
	/**
	 * @param busy The bBusy to set.
	 */
	public synchronized void setBusy(boolean busy) {
		if (busy != bBusy){

			bBusy = busy;
		
			if (bBusy){
				new Thread(new BusyStatus()).start();
				//Display.getDefault().asyncExec(new BusyStatus());
			}
		}
	}
	private class BusyStatus implements Runnable {
		
			/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			
			while (bBusy){
				
				Display.getDefault().syncExec(new Runnable(){
					public void run(){
						bar.setVisible(true);
					}
				});
				
				for (i=0; i<=4; i++) {
					try {Thread.sleep (500);} catch (Throwable th) {}

					Display.getDefault().syncExec(new Runnable(){
						public void run() {
							bar.setSelection(i);
						}
					});
				}
			}
			
			Display.getDefault().syncExec(new Runnable(){
				public void run(){
					bar.setVisible(false);
				}
			});
		}
	}
}
