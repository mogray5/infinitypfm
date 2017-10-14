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
package org.infinitypfm.core.data;

import java.util.*;

/**
 * @author wayne
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccountHash {
	
	private HashMap<String, Account> actHash = null;
	@SuppressWarnings("rawtypes")
	private List actList = null;

	/**
	 * 
	 */
	public AccountHash(@SuppressWarnings("rawtypes") List list) {
		super();
		actList = list;
		HashAccounts();

	}

	private void HashAccounts(){
		
		actHash = new HashMap<String, Account>();
		Account act = null;
		
		if ( actList != null){
			for (int i=0; i< actList.size(); i++){
				act = (Account)actList.get(i);
				actHash.put(act.getActName(), act);
			}
		}
	}
	
	public Account get(String actName){
		return (Account)actHash.get(actName);
	}
}