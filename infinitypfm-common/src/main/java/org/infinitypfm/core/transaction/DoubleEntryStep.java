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
package org.infinitypfm.core.transaction;

import java.sql.SQLException;
import java.util.Iterator;

import org.apache.ibatis.session.SqlSession;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;

public class DoubleEntryStep extends BaseStep {

	public DoubleEntryStep(SqlSession map) {
		super(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process() throws SQLException {

		try {
					
		if (this.isValid()) {
			
			Iterator<TransactionOffset> offsets = this._transaction.getOffsets().iterator();
			
			while (offsets.hasNext()) {
			
				TransactionOffset offset = (TransactionOffset) offsets.next();
				
				// Do any need currency conversions
				this.checkAndConvert(offset);
			
				Transaction tranOffset = new Transaction();
				
				tranOffset.setTranMemo(this._transaction.getTranMemo());
				tranOffset.setTranDate(this._transaction.getTranDate());
				tranOffset.setTransactionKey(this._transaction.getTransactionKey());
				tranOffset.setTranAmount(offset.getOffsetAmount());
				tranOffset.setActId(offset.getOffsetId());
				
				this._map.insert("insertTransaction", tranOffset);
				this._map.insert("updateAccountBalance", tranOffset);
				this.UpdateMonthlyBalance(tranOffset);
			
			}
			
			this._map.insert("insertTransaction", this._transaction);
			this._map.insert("updateAccountBalance", this._transaction);
			UpdateMonthlyBalance(this._transaction);
			
			
		}
		} finally {
			super.process();
		}
		
	}

}
