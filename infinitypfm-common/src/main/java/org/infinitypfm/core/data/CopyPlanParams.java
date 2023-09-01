/*
 * Copyright (c) 2005-2023 Wayne Gray All rights reserved
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

public class CopyPlanParams {

	private int oldPlanID;
	private int newPlanID;
	
	public int getOldPlanID() {
		return oldPlanID;
	}
	public void setOldPlanID(int oldPlanID) {
		this.oldPlanID = oldPlanID;
	}
	public int getNewPlanID() {
		return newPlanID;
	}
	public void setNewPlanID(int newPlanID) {
		this.newPlanID = newPlanID;
	}
	
}
