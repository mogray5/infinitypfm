/*
 * Copyright (c) 2005-2018 Wayne Gray All rights reserved
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
package org.infinitypfm.core.conf;

public class LangInstance {

	private static LangInstance instance = null;
	private final LangLoader lang;
	
	protected LangInstance() {
		lang = new LangLoader();
	}
	
	 public static LangInstance getInstance() {
	
		 if (instance == null) {
			 instance = new LangInstance();
			 
		 }
		 
		 return instance;
		 
	 }
	 
	 public String getPhrase(String index) {
		 
		 return this.lang.getPhrase(index);
	 }
	
}
