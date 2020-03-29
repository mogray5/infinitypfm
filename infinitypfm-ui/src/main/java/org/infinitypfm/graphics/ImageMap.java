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

package org.infinitypfm.graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;

public class ImageMap {

	Shell shMain;
	HashMap<String, ImageData> hm;
	ArrayList<String> aFiles = new ArrayList<String>();

	public ImageMap(Shell sh) {
		super();
		shMain = sh;
		// create hashmap to organize images
		hm = new HashMap<String, ImageData>(10);

	}

	public Image getImage(String sName) throws SWTException {

		ImageData item = getImageData(sName);

		return new Image(shMain.getDisplay(), item);
	}
	
	/**
	 * Get image from file system
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public Image getTransparentImage(File file) throws FileNotFoundException {
		InputStream stream = new FileInputStream(file);
		ImageData item = new ImageData(stream);
		int whitePixel = item.palette.getPixel(new RGB(255, 255, 255));
		item.transparentPixel = whitePixel;
		return new Image(shMain.getDisplay(), item);
	}

	/**
	 * Get image from classpath
	 * 
	 * @param sName
	 * @return
	 */
	public Image getTransparentImage(String sName) {
		ImageData item = getImageData(sName);
		int whitePixel = item.palette.getPixel(new RGB(255, 255, 255));
		item.transparentPixel = whitePixel;
		return new Image(shMain.getDisplay(), item);
	}

	public ImageData getImageData(String sName) throws SWTException {
		ImageData item;

		// check hashmap
		item = (ImageData) hm.get(sName);
		if (item == null) {
			item = new ImageData(this.getClass().getResourceAsStream(sName));
			hm.put(sName, item);
			aFiles.add(sName);
		}

		return item;
	}

	public void QZDispose() {

		ImageData item;

		// dispose images
		for (int i = 0; i < aFiles.size(); i++) {
			item = (ImageData) hm.get(aFiles.get(i));
			if (item != null) {
			}
		}
	}
}
