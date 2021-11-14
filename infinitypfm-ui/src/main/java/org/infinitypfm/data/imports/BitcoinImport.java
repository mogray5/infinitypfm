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
package org.infinitypfm.data.imports;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.infinitypfm.conf.MM;
import org.infinitypfm.core.data.Transaction;

import ru.paradoxs.bitcoin.client.AccountInfo;
import ru.paradoxs.bitcoin.client.BitcoinClient;
import ru.paradoxs.bitcoin.client.TransactionInfo;

public class BitcoinImport extends BaseImport {

	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> ImportFile(ImportConfig config) {

		List<Transaction> result = null;

		try {

			if (config != null) config.config();
			
			BitcoinClient client = new BitcoinClient("localhost",
					MM.bitcoinUser, MM.bitcoinPwd);
			AccountInfo accountInfo = null;
			TransactionInfo tran = null;
			Transaction t = null;
			result = new ArrayList<Transaction>();
			List<AccountInfo> activeAccounts = client.listReceivedByAccount(0,
					false);

			List<Transaction> transInSystem = null;

			// Add the default account to the list so we get coins sent
			AccountInfo defaultAccount = new AccountInfo();
			defaultAccount.setAccount("");
			activeAccounts.add(defaultAccount);

			if (activeAccounts != null) {

				for (int i = 0; i < activeAccounts.size(); i++) {

					accountInfo = activeAccounts.get(i);

					List<TransactionInfo> trans = client.listTransactions(
							accountInfo.getAccount(), 500);

					if (trans != null) {

						for (int j = 0; j < trans.size(); j++) {

							tran = trans.get(j);

							try {
								transInSystem = MM.sqlMap.selectList(
										"getTransactionsByKey", tran.getTxId());

								if (transInSystem == null
										|| transInSystem.size() == 0) {

									t = new Transaction();
									t.setTransactionKey(tran.getTxId());
									t.setTranAmountBd(tran.getAmount());
									t.setTranMemo(tran.getCategory());
									t.setTranDate(new Date(
											tran.getTime() * 1000));
									result.add(t);

								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
