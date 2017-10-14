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
package org.infinitypfm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.infinitypfm.client.InfinityPfm;

public class PageHandler {

	public static String getPage(String url){
		
		URL page;
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		
		try {
			page = new URL(url);
		
			in = new BufferedReader(
					new InputStreamReader(
					page.openStream()));

			String inputLine;

			while ((inputLine = in.readLine()) != null)
				result.append(inputLine);

			

		} catch (MalformedURLException e) {
			InfinityPfm.LogMessage(e.getMessage());
		} catch (IOException e) {
			InfinityPfm.LogMessage(e.getMessage());
		} finally {
			try {in.close();} catch (Exception e){}
		}
		
		return result.toString();
		
	}
	
}
