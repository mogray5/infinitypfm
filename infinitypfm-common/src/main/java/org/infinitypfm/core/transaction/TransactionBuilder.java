package org.infinitypfm.core.transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.infinitypfm.core.data.Transaction;

/**
 *  Builder for posting transactions to the database.
 *  One or more processors can be added and will be called
 *  in order in the process method.
 *
 */
public class TransactionBuilder {

	private Transaction _transaction = null;
	private List<TransactionProcessor> _processors = null;
	
	public static class Builder {
		
		private Transaction _buildtransaction = null;
		private List<TransactionProcessor> _buildprocessors = null;
		
		public Builder (Transaction tran) {
			_buildtransaction = tran;
		}
			
		public Builder startingProcessor (TransactionProcessor p) {
			
			if (_buildprocessors == null)
				_buildprocessors = new ArrayList<TransactionProcessor>();
			
			_buildprocessors.add(p);
			
			return this;
		}
		
		public Builder thenProcessor (TransactionProcessor p) {
			
			return this.startingProcessor(p);
			
		}
		
		public TransactionBuilder build() {
			
			TransactionBuilder builder = new TransactionBuilder();
			builder.initialize(_buildtransaction, _buildprocessors);
			return builder;
		}
		
	}
	
	private TransactionBuilder(){}
	
	public void initialize(Transaction tran, List<TransactionProcessor> aProcessors) {
		
		_transaction = tran;
		_processors = aProcessors;
		
	}
	
	public void process() throws SQLException {
		
		if (_processors != null && _processors.size() > 0) {

			for (TransactionProcessor proc : _processors) {
				proc.setTransaction(_transaction);
				proc.process();
			}
		}
	}
}
