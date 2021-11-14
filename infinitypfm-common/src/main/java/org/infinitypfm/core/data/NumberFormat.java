/*
 * Copyright (c) 2005-2021 Wayne Gray All rights reserved
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

import org.apache.commons.lang.StringUtils;

public class NumberFormat {
	
	public static final String getDefault(int precision){
		
		String fmt = null;
		
		
			fmt = "#,##0." + StringUtils.rightPad("", 
					precision, '0');

		return fmt + ";(" + fmt + ")"; 
	}
	
	public static final String getDefault(){
		
		String fmt = "#,##0.0";
		
		return fmt + ";(" + fmt + ")"; 
	}
	
	public static final String getNoParens(int precision){
		String fmt = "#,##0." + StringUtils.rightPad("", 
				precision, '0');
		return fmt; 
	}
	
	public static final String getNoCommaNoParems(int precision){
		String fmt = "###0." + StringUtils.rightPad("", 
				precision, '0');
		return fmt; 
	}
}
