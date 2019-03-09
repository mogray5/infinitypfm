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

package org.infinitypfm.ui;

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
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.ui.view.dialogs.MessageDialog;
import org.infinitypfm.ui.view.views.BaseView;
import org.infinitypfm.ui.view.views.BudgetView;
import org.infinitypfm.ui.view.views.CurrencyView;
import org.infinitypfm.ui.view.views.RecurrenceView;
import org.infinitypfm.ui.view.views.RegisterView;
import org.infinitypfm.ui.view.views.ReportView;
import org.infinitypfm.ui.view.views.WalletView;

/**
 * @author Wayne Gray
 * 
 */
public class ViewHandler {

	private Composite cmpMain;
	private Composite cmpWin;
	private Shell sh = InfinityPfm.shMain;
	private Canvas canvas = null;
	private final Image banner = InfinityPfm.imMain
			.getTransparentImage(MM.IMG_QUEZEN_BANNER);

	public ViewHandler() {
		cmpWin = new Composite(sh, SWT.BORDER);
		cmpWin.setLayout(new FormLayout());

	}

	private Composite getDefaultView() {
		Composite cmpResult = new Composite(cmpWin, SWT.FLAT);
		cmpResult.setLayout(new FormLayout());

		FormData cmpresultdata = new FormData();
		cmpresultdata.top = new FormAttachment(0, 0);
		cmpresultdata.left = new FormAttachment(0, 0);
		cmpresultdata.right = new FormAttachment(100, 0);
		cmpresultdata.bottom = new FormAttachment(100, 0);
		cmpResult.setLayoutData(cmpresultdata);

		Color color = new Color(InfinityPfm.shMain.getDisplay(), 255, 255, 255);
		cmpResult.setBackground(color);

		canvas = new Canvas(cmpResult, SWT.NONE);
		canvas.setBackground(color);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(banner, 0, 0);
			}
		});

		FormData canvasdata = new FormData();
		canvasdata.top = new FormAttachment(35, 0);
		canvasdata.left = new FormAttachment(30, 0);
		canvasdata.right = new FormAttachment(70, 0);
		canvasdata.bottom = new FormAttachment(70, 0);
		canvas.setLayoutData(canvasdata);

		return cmpResult;
	}

	public void LoadView(BaseView oView) {

		try {
			if (!cmpMain.isDisposed()) {
				cmpMain.dispose();
			}
		} catch (Exception e) {
		}

		cmpMain = oView;

		cmpMain.setVisible(true);
		cmpWin.layout(true);

	}

	public void LoadDefaultView() {

		try {
			if (!cmpMain.isDisposed()) {
				cmpMain.dispose();
			}
		} catch (Exception e) {
		}
		;

		cmpMain = this.getDefaultView();
		cmpMain.setVisible(true);
		cmpWin.layout(true);
	}

	public void UnloadCurrentView() {
		if (!cmpMain.isDisposed()) {
			cmpMain.dispose();
		}
		cmpMain = getDefaultView();

	}

	public void RefreshCurrentView() {
		try {
			((BaseView) cmpMain).Refresh();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public Composite getCurrentView() {
		return cmpMain;
	}

	public Composite getParent() {
		return cmpWin;
	}

	public void QZDispose() {

		if (!banner.isDisposed()) {
			banner.dispose();
		}

		if (!canvas.isDisposed()) {
			canvas.dispose();
		}

		if (!cmpMain.isDisposed()) {
			cmpMain.dispose();
		}

		if (!cmpWin.isDisposed()) {
			cmpWin.dispose();
		}

	}

	public BaseView getView(int iViewID) {

		BaseView vw = null;

		if (iViewID == MM.VIEW_REGISTER) {
			vw = new RegisterView(cmpWin, SWT.NONE);
		} else if (iViewID == MM.VIEW_BUDGET) {
			vw = new BudgetView(cmpWin, SWT.NONE);
		} else if (iViewID == MM.VIEW_RECURRENCE) {
			vw = new RecurrenceView(cmpWin, SWT.NONE);
		} else if (iViewID == MM.VIEW_CURRENCY) {
			vw = new CurrencyView(cmpWin, SWT.NONE);
		} else if (iViewID == MM.VIEW_WALLET) {
			vw = new WalletView(cmpWin, SWT.NONE);
		} else {
			return null;
		}

		vw.setLayout(new FormLayout());

		FormData cmpmaindata = new FormData();
		cmpmaindata.top = new FormAttachment(0, 0);
		cmpmaindata.left = new FormAttachment(0, 0);
		cmpmaindata.bottom = new FormAttachment(100, 0);
		cmpmaindata.right = new FormAttachment(100, 0);
		vw.setLayoutData(cmpmaindata);

		return vw;

	}

	public void LoadReportView(String sReport) {

		try {
			ReportView reportView = new ReportView(cmpWin, SWT.NONE);

			reportView.setBackground(InfinityPfm.shMain.getDisplay()
					.getSystemColor(SWT.COLOR_WHITE));

			reportView.setLayout(new FormLayout());

			reportView.setReportUrl(sReport);

			FormData cmpmaindata = new FormData();
			cmpmaindata.top = new FormAttachment(0, 0);
			cmpmaindata.left = new FormAttachment(0, 0);
			cmpmaindata.bottom = new FormAttachment(100, 0);
			cmpmaindata.right = new FormAttachment(100, 0);
			reportView.setLayoutData(cmpmaindata);

			try {
				if (!cmpMain.isDisposed()) {
					cmpMain.dispose();
				}
			} catch (Exception e) {
			}
			;

			cmpMain = reportView;
			cmpMain.setVisible(true);
			cmpWin.layout(true);
		} catch (Exception e) {

			MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE,
					e.getMessage());
			show.Open();

		}

	}

	/*
	 * Listeners
	 */

	SelectionAdapter on_Click = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {

		}

	};

}
