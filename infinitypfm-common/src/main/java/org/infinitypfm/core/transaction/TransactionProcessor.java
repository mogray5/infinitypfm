package org.infinitypfm.core.transaction;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.infinitypfm.core.data.Transaction;
import org.infinitypfm.core.data.TransactionOffset;

public interface TransactionProcessor {

	public void process() throws SQLException ;
	
	public boolean fromBsv();
	
	public boolean isBSV(long actId) throws SQLException;
	
	public boolean isDefault(long actId) throws SQLException;
	
	public boolean fromDefault();
		
	public boolean isValid();
	
	public void setTransaction (Transaction t)  throws SQLException ;
	
	public Transaction getTransaction();
	
	public BigDecimal convert(long amount, long actId) throws SQLException;
	
	public void checkAndConvert(TransactionOffset offset) throws SQLException;

}
