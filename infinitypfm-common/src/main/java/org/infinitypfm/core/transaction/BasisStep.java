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
import org.infinitypfm.core.data.Basis;
import org.infinitypfm.core.data.TransactionOffset;

public class BasisStep extends BaseStep {

	@SuppressWarnings("unchecked")
	@Override
	public void process() throws SQLException {
		try {
			
			if (this.isValid()) {
				
				Iterator<TransactionOffset> offsets = this._transaction.getOffsets().iterator();
				
				//This will not work for split transactions.
				while (offsets.hasNext()) {
				
					TransactionOffset offset = (TransactionOffset) offsets.next();
									
					// Record basis for SV coins received
					if (this.isBSV(this._transaction.getActId()) && this._transaction.getTranAmount() > 0) {
						if (this.isDefault(offset.getOffsetId())) {
							
							Basis basis = new Basis();
							basis.setAquireCurrencyID(this._bsvCurrency.getCurrencyID());
							basis.setAquireDate(this._transaction.getTranDate());
							
							// Do any need currency conversions
							this.checkAndConvert(offset);
							
							if (offset.getOffsetAmount() >= 0)
								basis.setCost(offset.getOffsetAmount());
							else 
								basis.setCost(-offset.getOffsetAmount());
							
							basis.setCostCurrencyID(this._defaultCurrency.getCurrencyID());
							basis.setQtyFifo(this._transaction.getTranAmount());
							basis.setQtyLifo(this._transaction.getTranAmount());
							this._map.insert("insertBasis", basis);
							
						}
					}
				}
			}
			
		} finally {
			super.process();
		}
	}

	public BasisStep(SqlSession map) {
		super(map);
	}
}
