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

package org.infinitypfm.action;

import org.eclipse.swt.widgets.TreeItem;

/**
 * @author wggray
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeNodeInfo {

	private int sNodeType = 0;
	private TreeItem tiRoot = null;
	private TreeItem tiBankAccount = null;
	private TreeItem tiExpenseAccount = null;
	private TreeItem tiIncomeAccount = null;
	private TreeItem tiBudgetNode = null;
	
	/**
	 * 
	 */
	public TreeNodeInfo() {
		super();
	}

		
	/**
	 * @return Returns the sNodeType.
	 */
	public int getNodeType() {
		return sNodeType;
	}
	/**
	 * @param nodeType The sNodeType to set.
	 */
	public void setNodeType(int nodeType) {
		sNodeType = nodeType;
	}
	
	/**
	 * @return Returns the root bank account node.
	 */
	public TreeItem getBankAccountNode() {
		return tiBankAccount;
	}
	/**
	 * @param sets the root bank account node
	 */
	public void setBankAccountNode(TreeItem tiAct) {
		this.tiBankAccount = tiAct;
	}
	/**
	 * @return Returns the tiRoot.
	 */
	public TreeItem getRootNode() {
		return tiRoot;
	}
	/**
	 * @param tiRoot The tiRoot to set.
	 */
	public void setRootNode(TreeItem tiRoot) {
		this.tiRoot = tiRoot;
	}
	/**
	 * @return Returns the tiServer.
	 */
	public TreeItem getExpenseAccountNode() {
		return tiExpenseAccount;
	}
	/**
	 * @param tiServer The tiServer to set.
	 */
	public void setExpenseNode(TreeItem tiAct) {
		this.tiExpenseAccount = tiAct;
	}
	
	/**
	 * @return Returns the tiIncomeAccount.
	 */
	public TreeItem getIncomeAccountNode() {
		return tiIncomeAccount;
	}
	/**
	 * @param tiIncomeAccount The tiIncomeAccount to set.
	 */
	public void setIncomeAccountNode(TreeItem tiIncomeAccount) {
		this.tiIncomeAccount = tiIncomeAccount;
	}
	
	/**
	 * @return Returns the tiBudgetNode.
	 */
	public TreeItem getBudgetNode() {
		return tiBudgetNode;
	}
	/**
	 * @param tiBudgetNode The tiBudgetNode to set.
	 */
	public void setBudgetNode(TreeItem tiBudgetNode) {
		this.tiBudgetNode = tiBudgetNode;
	}
}
