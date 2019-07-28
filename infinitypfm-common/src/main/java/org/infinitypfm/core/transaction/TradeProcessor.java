package org.infinitypfm.core.transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;

import org.infinitypfm.core.data.Basis;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.Trade2;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;

import com.ibatis.sqlmap.client.SqlMapClient;

public class TradeProcessor extends BaseProcessor {

	public TradeProcessor(SqlMapClient map) {
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
									
					// All trades are recorded in the default currency so
					// convert if necessary
					if (this.isBSV(this._transaction.getActId())) {
						if (this.isDefault(offset.getOffsetId())) {
							
							// Transactions should already be posted
							// Lookup transaction using the key from the blockchain
							
							Transaction tran = (Transaction) this._map.queryForObject("getTransactionsByKeyBSVOnly", this._transaction.getTransactionKey());
							
							if (tran != null) {
								
								// Build a trade transactions using one or more basis entries
								Trade2 trade = new Trade2();
								
								trade.setTranDate(tran.getTranDate());
								trade.setTranId(tran.getTranId());
								trade.setFromCurrencyID(this._bsvCurrency.getCurrencyID());
								trade.setToCurrencyID(this._defaultCurrency.getCurrencyID());
								
								// LIFO
								trade.setBasisLifo(this.getLifoBasis(tran.getTranAmount()));
								// FIFO
								trade.setBasisFifo(this.getFifoBasis(tran.getTranAmount()));
								
								trade.setFromAmount(tran.getTranAmount());
								
								// Convert to default currency using exchange rate
								trade.setToAmount( DataFormatUtil.moneyToLong(
										this._formatter.strictMultiply(
												_bsvCurrency.getExchangeRate(), 
												this._formatter.getAmountFormatted(tran.getTranAmount()))));
								
								this._map.insert("insertTrade", trade);
								this._map.commitTransaction();
							}
							
						}
					}
					
				}
			}
			
			
		} finally {
			super.process();
		}
		
	}
	
	/**
	 * Iterate on the basis table and reduce LIFO qty until bsvQty is reached.
	 * @param bsvQty
	 * @return Average LIFO basis found during iteration
	 * @throws SQLException 
	 */
	private long getLifoBasis(long bsvQty) throws SQLException {
	
		long qty = bsvQty;
		long qtyLifo = 0;
		BigDecimal totalQty = null;
		BigDecimal currAvg = null;
		
		while (qty > 0) {
			
			Basis basis = (Basis) this._map.queryForObject("getLifoBasis", this._bsvCurrency);
			qtyLifo = basis.getQtyFifo();
			
			
			// Reduce the basis object qty by LIFO qty. 
			if (qty > qtyLifo) 
				basis.setQtyLifo(0);
			 else 
				basis.setQtyLifo(qtyLifo - qty);
			
			this._map.update("updateLifo", basis);
			
			currAvg = newAvg(currAvg, totalQty, qtyLifo, basis.getCost());
			
			// Reduce remaining quantity
			qty -= qtyLifo;
			
		}

		// Return the calculated average as a long	
		return DataFormatUtil.moneyToLong(currAvg);
	
	}
	
	/**
	 * Iterate on the basis table and reduce LIFO qty until bsvQty is reached.
	 * @param bsvQty
	 * @return Average LIFO basis found during iteration
	 * @throws SQLException 
	 */
	private long getFifoBasis(long bsvQty) throws SQLException {
	
		long qty = bsvQty;
		long qtyFifo = 0;
		BigDecimal totalQty = null;
		BigDecimal currAvg = null;
		
		while (qty > 0) {
			
			Basis basis = (Basis) this._map.queryForObject("getFifoBasis", this._bsvCurrency);
			qtyFifo = basis.getQtyFifo();
			
			
			// Reduce the basis object qty by FIFO qty. 
			if (qty > qtyFifo) 
				basis.setQtyFifo(0);
			 else 
				basis.setQtyFifo(qtyFifo - qty);
			
			this._map.update("updateFifo", basis);
			
			currAvg = newAvg(currAvg, totalQty, qtyFifo, basis.getCost());
			
			// Reduce remaining quantity
			qty -= qtyFifo;
			
		}
		
		// Return the calculated average as a long	
		return DataFormatUtil.moneyToLong(currAvg);
	
	}
	
	public BigDecimal newAvg(BigDecimal currAvg, BigDecimal totalQty, long qtyNew, long cost) {
		
		BigDecimal result = null;
		
		// Get the new average, this gets a little messy as
		// multiple conversions between long to BigDecimal are needed

		//newAve = ((oldAve*oldNumPoints) + x)/(oldNumPoints+1)
		//currAvg = ((currAvg*totalQty) + basis.cost) / (totalQty + qtyLifo)
	
		// Convert totalQty to a long val
		long totalQtyLong = DataFormatUtil.moneyToLong(totalQty);
		
		// Get denominator using longs
		long avgDenomLong = totalQtyLong + qtyNew;
		
		// Get numerator using BigDecimal since we're multiplying
		BigDecimal avgNum = this._formatter.strictMultiply(currAvg.toString(), totalQty.toString());
		avgNum.add(new BigDecimal(this._formatter.getAmountFormatted(cost)));
		
		// Calculate the new average as a BigDecimal
		currAvg = this._formatter.strictDivide(avgNum.toString(), 
				this._formatter.getAmountFormatted(avgDenomLong), 
							8);
		
		return result;
	}
}
