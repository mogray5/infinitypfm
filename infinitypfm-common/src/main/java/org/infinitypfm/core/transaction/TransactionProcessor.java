package org.infinitypfm.core.transaction;

import java.sql.SQLException;

import org.infinitypfm.core.data.Transaction;

public interface TransactionProcessor {

	public void process() throws SQLException ;
	
	public boolean fromBsv();
	
	public boolean isBSV(long actId) throws SQLException;
	
	public boolean isDefault(long actId) throws SQLException;
	
	public boolean fromDefault();
		
	public boolean isValid();
	
	public void setTransaction (Transaction t)  throws SQLException ;
	
	public Transaction getTransaction();

}
