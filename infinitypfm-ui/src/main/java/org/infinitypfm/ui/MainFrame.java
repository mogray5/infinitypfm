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

package org.infinitypfm.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.infinitypfm.action.MainAction;
import org.infinitypfm.client.InfinityPfm;
import org.infinitypfm.conf.MM;
import org.infinitypfm.ui.view.menus.MainMenu;
import org.infinitypfm.ui.view.toolbars.BaseToolbar;
import org.infinitypfm.ui.view.toolbars.MainToolbar;
import org.infinitypfm.ui.view.views.ConsoleView;
import org.infinitypfm.ui.view.views.StatusView;

public class MainFrame {

	private static Sash VertSash;
	private static Sash HorSash;
	private FormData Vsashdata;
	private FormData Hsashdata;
	public Label lblStatus = null;

	private MoneyTree trMain;
	private ConsoleView msgMain;
	private ViewHandler vwMain;
	private MainMenu mnuMain;
	private BaseToolbar cbMain;
	private StatusView stMain;

	public MainFrame() {
		super();

		// save inputs
		InfinityPfm.shMain.addShellListener(new onClose());

	}

	public void LoadUI(boolean bUseIcons) {
		InfinityPfm.shMain.setLayout(new FormLayout());
		InfinityPfm.shMain.setText(MM.APPTITLE + " - "
				+ MM.PHRASES.getPhrase("63"));
		InfinityPfm.shMain.setImage(InfinityPfm.imMain
				.getImage(MM.IMG_LOGO_ICON));

		/*
		 * Load Status Box
		 */
		stMain = new StatusView(InfinityPfm.shMain, SWT.FLAT);

		/*
		 * Load Main Menu
		 */
		mnuMain = new MainMenu(InfinityPfm.shMain);
		InfinityPfm.shMain.setMenuBar(mnuMain.getMenu());

		/*
		 * Load Main Toolbar
		 */
		cbMain = new MainToolbar(InfinityPfm.shMain);

		/*
		 * Load Tree
		 */
		trMain = new MoneyTree(InfinityPfm.shMain);

		/*
		 * Load View Handler
		 */
		vwMain = new ViewHandler();
	
	}

	public void Open() {

		setListeners();
		InfinityPfm.shMain.open();
		VertSash.setFocus();
		InfinityPfm.shMain.setSize(1024, 768);
		
		if (MM.options.isConsoleDefaultOpen()) {
			MainAction action = new MainAction();
			action.ToggleConsole(true);
		}

	}

	public void LoadLayout() {
		// create vertical sash
		VertSash = new Sash(InfinityPfm.shMain, SWT.FLAT | SWT.VERTICAL);
		Vsashdata = new FormData();
		Vsashdata.bottom = new FormAttachment(stMain, 0);
		Vsashdata.left = new FormAttachment(20, 0);
		Vsashdata.top = new FormAttachment(cbMain.getToolbar(), 0);
		VertSash.setLayoutData(Vsashdata);

		// Bind other controls
		FormData coolbardata = new FormData();
		coolbardata.top = new FormAttachment(0, 0);
		coolbardata.left = new FormAttachment(0, 0);
		coolbardata.right = new FormAttachment(100, 0);
		cbMain.getToolbar().setLayoutData(coolbardata);

		FormData stmaindata = new FormData();
		stmaindata.left = new FormAttachment(0, 0);
		stmaindata.bottom = new FormAttachment(100, 0);
		stmaindata.top = new FormAttachment(100, -20);
		stmaindata.right = new FormAttachment(100, 0);
		stMain.setLayoutData(stmaindata);

		FormData trmaindata = new FormData();
		trmaindata.left = new FormAttachment(0, 0);
		trmaindata.right = new FormAttachment(VertSash, 0);
		trmaindata.top = new FormAttachment(cbMain.getToolbar(), 0);
		trmaindata.bottom = new FormAttachment(stMain, 0);
		trMain.getTree().setLayoutData(trmaindata);

		FormData rightpanedata = new FormData();
		rightpanedata.left = new FormAttachment(VertSash, 0);
		rightpanedata.bottom = new FormAttachment(stMain, 0);
		rightpanedata.top = new FormAttachment(cbMain.getToolbar(), 0);
		rightpanedata.right = new FormAttachment(100, 0);

		vwMain.getParent().setLayoutData(rightpanedata);

	}

	public Point getWindowCenter() {
		Point p = InfinityPfm.shMain.getLocation();
		Rectangle rect = InfinityPfm.shMain.getBounds();
		int xc = p.x + (rect.height / 2);
		int yc = p.y + (rect.width / 2);
		p = null;
		rect = null;
		return new Point(xc, yc);

	}

	public void getRightPane() {
		vwMain = new ViewHandler();
	}

	public void LoadConsole(boolean bLoad) {
		if (bLoad && msgMain == null) {
			msgMain = new ConsoleView(InfinityPfm.shMain);

			HorSash = new Sash(InfinityPfm.shMain, SWT.FLAT | SWT.HORIZONTAL);
			Hsashdata = new FormData();
			Hsashdata.left = new FormAttachment(0, 0);
			Hsashdata.right = new FormAttachment(100, 0);
			Hsashdata.bottom = new FormAttachment(80, 0);
			HorSash.setLayoutData(Hsashdata);

			FormData rightbottpanedata = new FormData();
			rightbottpanedata.left = new FormAttachment(0, 0);
			rightbottpanedata.bottom = new FormAttachment(stMain, 0);
			rightbottpanedata.top = new FormAttachment(HorSash, 0);
			rightbottpanedata.right = new FormAttachment(100, 0);
			msgMain.setLayoutData(rightbottpanedata);

			FormData data = (FormData) vwMain.getParent().getLayoutData();
			data.bottom = new FormAttachment(HorSash, 0);
			Vsashdata.bottom = new FormAttachment(HorSash, 0);
			data = (FormData) trMain.getTree().getLayoutData();
			data.bottom = new FormAttachment(HorSash, 0);

			HorSash.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					Rectangle sashRect = HorSash.getBounds();
					Rectangle shellRect = InfinityPfm.shMain.getClientArea();
					int right = shellRect.height - sashRect.height - 10;
					e.y = Math.max(Math.min(e.y, right), 29);

					if (e.y != sashRect.y) {
						Hsashdata.bottom = new FormAttachment(0, e.y);
						InfinityPfm.shMain.layout();
					}
				}
			});

		} else if (!bLoad && msgMain != null) {
			msgMain.QZDispose();
			msgMain = null;
			if (!HorSash.isDisposed()) {
				HorSash.dispose();
			}

			FormData data = (FormData) vwMain.getParent().getLayoutData();
			data.bottom = new FormAttachment(stMain, 0);
			Vsashdata.bottom = new FormAttachment(stMain, 0);
			data = (FormData) trMain.getTree().getLayoutData();
			data.bottom = new FormAttachment(stMain, 0);
			data = (FormData) stMain.getLayoutData();
			data.top = new FormAttachment(100, -20);
		}
	}

	public void setListeners() {

		VertSash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Rectangle sashRect = VertSash.getBounds();
				Rectangle shellRect = InfinityPfm.shMain.getClientArea();
				int right = shellRect.width - sashRect.width - 100;
				e.x = Math.max(Math.min(e.x, right), 100);
				if (e.x != sashRect.x) {
					Vsashdata.left = new FormAttachment(0, e.x);
					InfinityPfm.shMain.layout();
				}
			}
		});

	}

	public void QZDispose() {
		
		try {
			cbMain.QZDispose();
		} catch (Exception e1) {
			System.err.println(e1.getMessage());
		}
		try {
			mnuMain.QZDispose();
		} catch (Exception e2) {
			System.err.println(e2.getMessage());
		}
		try {
			trMain.QZDispose();
		} catch (Exception e3) {
			System.err.println(e3.getMessage());
		}
		try {
			InfinityPfm.imMain.QZDispose();
		} catch (Exception e5) {
			System.err.println(e5.getMessage());
		}
		try {
			vwMain.QZDispose();
		} catch (Exception e6) {
			System.err.println(e6.getMessage());
		}

		if (!VertSash.isDisposed()) {
			VertSash.dispose();
		}

		try {
			msgMain.QZDispose();
		} catch (Exception e4) {
		}

	}

	/**
	 * @return Returns the cbMain.
	 */
	public BaseToolbar getCbMain() {
		return cbMain;
	}

	/**
	 * @param cbMain
	 *            The cbMain to set.
	 */
	public void setCbMain(BaseToolbar cbMain) {
		this.cbMain = cbMain;
	}

	/**
	 * @return Returns the mnuMain.
	 */
	public MainMenu getMnuMain() {
		return mnuMain;
	}

	/**
	 * @param mnuMain
	 *            The mnuMain to set.
	 */
	public void setMnuMain(MainMenu mnuMain) {
		this.mnuMain = mnuMain;
	}

	/**
	 * @return Returns the msgMain.
	 */
	public ConsoleView getMsgMain() {
		return msgMain;
	}

	/**
	 * @param msgMain
	 *            The msgMain to set.
	 */
	public void setMsgMain(ConsoleView msgMain) {
		this.msgMain = msgMain;
	}

	/**
	 * @return Returns the stMain.
	 */
	public StatusView getStMain() {
		return stMain;
	}

	/**
	 * @param stMain
	 *            The stMain to set.
	 */
	public void setStMain(StatusView stMain) {
		this.stMain = stMain;
	}

	/**
	 * @return Returns the trMain.
	 */
	public MoneyTree getTrMain() {
		return trMain;
	}

	/**
	 * @param trMain
	 *            The trMain to set.
	 */
	public void setTrMain(MoneyTree trMain) {
		this.trMain = trMain;
	}

	/**
	 * @return Returns the vwMain.
	 */
	public ViewHandler getVwMain() {
		return vwMain;
	}

	/**
	 * @param vwMain
	 *            The vwMain to set.
	 */
	public void setVwMain(ViewHandler vwMain) {
		this.vwMain = vwMain;
	}

	class onClose extends ShellAdapter {
		public void shellClosed(ShellEvent e) {
			QZDispose();
			super.shellClosed(e);

		}
	}

}
