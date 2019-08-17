package org.infinitypfm.core.transaction;

import java.sql.SQLException;
import java.util.Iterator;

import org.infinitypfm.core.data.Basis;
import org.infinitypfm.core.data.TransactionOffset;

import com.ibatis.sqlmap.client.SqlMapClient;

public class BasisProcessor extends BaseProcessor {

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

	public BasisProcessor(SqlMapClient map) {
		super(map);
	}
}
