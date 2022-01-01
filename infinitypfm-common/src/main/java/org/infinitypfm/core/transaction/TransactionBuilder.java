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
	private List<TransactionStep> _processors = null;
	
	public static class Builder {
		
		private Transaction _buildtransaction = null;
		private List<TransactionStep> _buildprocessors = null;
		
		public Builder (Transaction tran) {
			_buildtransaction = tran;
		}
			
		public Builder startingProcessor (TransactionStep p) {
			
			if (_buildprocessors == null)
				_buildprocessors = new ArrayList<TransactionStep>();
			
			_buildprocessors.add(p);
			
			return this;
		}
		
		public Builder thenProcessor (TransactionStep p) {
			
			return this.startingProcessor(p);
			
		}
		
		public TransactionBuilder build() {
			
			TransactionBuilder builder = new TransactionBuilder();
			builder.initialize(_buildtransaction, _buildprocessors);
			return builder;
		}
		
	}
	
	private TransactionBuilder(){}
	
	public void initialize(Transaction tran, List<TransactionStep> aProcessors) {
		
		_transaction = tran;
		_processors = aProcessors;
		
	}
	
	public void process() throws SQLException {
		
		if (_processors != null && _processors.size() > 0) {

			for (TransactionStep proc : _processors) {
				proc.setTransaction(_transaction);
				proc.process();
			}
		}
	}
}
