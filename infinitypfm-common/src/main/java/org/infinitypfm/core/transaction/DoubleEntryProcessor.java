package org.infinitypfm.core.transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;

import org.infinitypfm.core.data.DataFormatUtil;
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
