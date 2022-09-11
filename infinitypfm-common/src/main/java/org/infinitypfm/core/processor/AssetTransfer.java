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
package org.infinitypfm.core.processor;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.infinitypfm.core.conf.LangLoader;
import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;
import org.infinitypfm.core.exception.TransactionException;
import org.infinitypfm.core.transaction.BasisStep;
import org.infinitypfm.core.transaction.DoubleEntryStep;
import org.infinitypfm.core.transaction.TradeStep;
import org.infinitypfm.core.transaction.TransactionBuilder;

/**
 * 
 * AssetTransfer records a basis value along with sale price and transaction amount when an asset is sold.
 * 
 * This applies to digital currencies for tax reporting purposes.
 * 
 */
public class AssetTransfer extends SimpleProcessor {
	
	public AssetTransfer(int precision) {
		super(precision);
	}

	/**
	 * Set up processor steps using TransactionBuilder need to transfer an asset.
	 */
	@Override
	public void AddTransaction(Transaction tran, boolean saveMemo) throws SQLException, TransactionException {
		
		DoubleEntryStep doubleEntryProcessor = new DoubleEntryStep(_session);
		BasisStep basisProcessor = new BasisStep(_session);
		TradeStep tradeProcessor = new TradeStep(_session);
		
		TransactionBuilder processor = new TransactionBuilder.Builder(tran)
				.startingProcessor(doubleEntryProcessor)
				.thenProcessor(basisProcessor)
				.thenProcessor(tradeProcessor)
				.build();
				
		processor.process();

	}

}
