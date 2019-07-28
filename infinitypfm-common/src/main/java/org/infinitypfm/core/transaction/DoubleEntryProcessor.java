package org.infinitypfm.core.transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;

import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;

import com.ibatis.sqlmap.client.SqlMapClient;

public class DoubleEntryProcessor extends BaseProcessor {

	public DoubleEntryProcessor(SqlMapClient map) {
		super(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process() throws SQLException {

		try {
		
		if (this.isValid()) {
			
			BigDecimal newOffsetAmount = null;
						
			Iterator<TransactionOffset> offsets = this._transaction.getOffsets().iterator();
			
			while (offsets.hasNext()) {
			
				TransactionOffset offset = (TransactionOffset) offsets.next();
				
				// Perform currency conversion if going to/from Default/BSV
				if (this.fromDefault() && this.isBSV(offset.getOffsetId()) && offset.needsConversion()) {
					
					newOffsetAmount = this._formatter.strictDivide(this._transaction.getExchangeRate(), Long.toString(offset.getOffsetAmount()), 8);
					offset.setOffsetAmount(newOffsetAmount.longValue());
					
				} else if (this.fromBsv() && this.isDefault(offset.getOffsetId()) && offset.needsConversion()) {
					
					newOffsetAmount = this._formatter.strictMultiply(this._transaction.getExchangeRate(), Long.toString(offset.getOffsetAmount()));
					offset.setOffsetAmount(newOffsetAmount.longValue());
				}
				
				Transaction tranOffset = new Transaction();
				
				tranOffset.setTranMemo(this._transaction.getTranMemo());
				tranOffset.setTranDate(this._transaction.getTranDate());
				tranOffset.setTransactionKey(this._transaction.getTransactionKey());
				tranOffset.setTranAmount(offset.getOffsetAmount());
				
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
