/*
 * Copyright (c) 2005-2023 Wayne Gray All rights reserved
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
package org.infinitypfm.bitcoin.wallet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.infinitypfm.bitcoin.liteclient.payd.api.v1.Balance;
import org.infinitypfm.bitcoin.liteclient.payd.api.v1.CreateInvoiceRequest;
import org.infinitypfm.bitcoin.liteclient.payd.api.v1.CreateInvoiceResponse;
import org.infinitypfm.bitcoin.wallet.exception.SendException;
import org.infinitypfm.bitcoin.wallet.exception.WalletException;
import org.infinitypfm.core.data.AuthData;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.DigitalAssetTransaction;
import org.infinitypfm.core.data.DigitalAssetUtxo;
import org.infinitypfm.core.data.ReceivingAddress;
import org.infinitypfm.core.data.RestResponse;
import org.infinitypfm.core.util.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LiteClientWallet implements BsvWallet {

	private RestClient _client = null;
	private ObjectMapper _mapper = null;
	private WalletEvents _events = null;
	private DataFormatUtil _formatter = null;
	
	/**********/
	/* cTor's */
	/**********/
	public LiteClientWallet(String baseUrl) {
		_client = new RestClient(baseUrl);
		_mapper = new ObjectMapper();
		_formatter = new DataFormatUtil(8);
	}
	
	@Override
	public AuthData getAuthData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAuthData(AuthData authData) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRunning(boolean TriggerEventOnSignInSuccess) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFiatBalance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBsvBalance() {

		RestResponse restResult = _client.get("/api/v1/balance", null);
		String response = null;

		try {
			Balance balance = _mapper.readValue(restResult.getBody(), Balance.class);
			response = Long.toString(balance.getSatoshis());
		} catch (Exception e) {
			_events.walletMessage("Error in getBsvBalance", new WalletException(e));
		}
		
		return response;
	}

	@Override
	public void registerForEvents(WalletEvents events) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterForEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	public ReceivingAddress getCurrentReceivingAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMnemonicCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreFromSeed(String seedCode, String passphrase) throws WalletException {
		// TODO Auto-generated method stub

	}

	@Override
	public File getQrCode(String address) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DigitalAssetTransaction> getHistory(String sinceTransaction) throws WalletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendCoins(String toAddress, String amount, String memo) throws SendException {

		CreateInvoiceRequest request = new CreateInvoiceRequest();
		request.setSatoshis(Long.parseLong(amount));
		request.setDescription(memo);
		request.setExpiresAt("2024-01-01T20:00:00.000Z");
		request.setReference("Invoice");
		String json = null;
		try {
			json = _mapper.writeValueAsString(request);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RestResponse restResult = _client.post("/api/v1/invoices", null, json);
		
		try {
			CreateInvoiceResponse balance = _mapper.readValue(restResult.getBody(), CreateInvoiceResponse.class);
		} catch (Exception e) {
			_events.walletMessage("Error in getBsvBalance", new WalletException(e));
		}
		
	}

	@Override
	public List<DigitalAssetUtxo> getUtxo() throws WalletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isImplemented(WalletFunction function) {
	switch (function) {
		
		case BACKUP:
			return false;
		case GETHISTORY:
			return true;
		case GETSETBALANCEFIAT:
			return false;
		case GETSETBALANCEBSV:
			return true;
		case RECIEVEREALTIME:
			return false;
		case REGISTERFOREVENTS:
		case UNREGISTERFOREVENTS:
		case CURRENTRECEIVINGADDRESS:
		case GETMNEUMONIC:
			return true;
		case GETUTXO:
			return true;
		case RESTOREFROMSEED:
			return false;
		case GETQRCODE:
		case SENDCOINS:
		case SIGNIN:
			return false;
		}
		
		return false;
	}

}
