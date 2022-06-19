/*
 * Copyright (c) 2005-2022 Wayne Gray All rights reserved
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.infinitypfm.bitcoin.relysia.api.v1.AddressResponse;
import org.infinitypfm.bitcoin.relysia.api.v1.Auth;
import org.infinitypfm.bitcoin.relysia.api.v1.AuthResponse;
import org.infinitypfm.bitcoin.relysia.api.v1.BalanceResponse;
import org.infinitypfm.bitcoin.relysia.api.v1.HistoryResponse;
import org.infinitypfm.bitcoin.relysia.api.v1.HistoryRow;
import org.infinitypfm.bitcoin.relysia.api.v1.MnemonicResponse;
import org.infinitypfm.bitcoin.relysia.api.v1.SendData;
import org.infinitypfm.bitcoin.relysia.api.v1.SendRequest;
import org.infinitypfm.bitcoin.relysia.api.v1.SendResponse;
import org.infinitypfm.bitcoin.wallet.exception.SendException;
import org.infinitypfm.bitcoin.wallet.exception.WalletException;
import org.infinitypfm.core.data.AuthData;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.DigitalAssetTransaction;
import org.infinitypfm.core.data.ReceivingAddress;
import org.infinitypfm.core.data.RestResponse;
import org.infinitypfm.core.util.RestClient;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import net.glxn.qrgen.QRCode;

public class RelysiaWallet implements BsvWallet {

	private AuthData _auth = null;
	private boolean _signedIn = false;
	private boolean _inShutdown = false;
	private RestClient _client = null;
	private ObjectMapper _mapper = null;
	private WalletEvents _events = null;
	private DataFormatUtil _formatter = null;
	
	
	/**********/
	/* cTor's */
	/**********/

	public RelysiaWallet(String baseUrl) {
		_client = new RestClient(baseUrl);
		_mapper = new ObjectMapper();
		_formatter = new DataFormatUtil(8);
	}
	
	public RelysiaWallet(String baseUrl, AuthData auth) {
		super();
		_auth = auth;
		_client = new RestClient(baseUrl);
		_mapper = new ObjectMapper();
		//_mapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		//_mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
		//_mapper.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
		//_mapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
		_formatter = new DataFormatUtil(8);
	}
	
	/******************/
	/* Public Methods */
	/******************/
	
	@Override
	public boolean isRunning() {
		return isRunning(false);
	}
	
	@Override
	public boolean isRunning(boolean TriggerEventOnSignInSuccess) {
		
		if (_inShutdown) return false;
		
		if (!_signedIn) {
			try {
				signIn();
				
				if (_events != null && TriggerEventOnSignInSuccess)
					_events.signIn(true, null, null);
				
			} catch (WalletException e) {
				if (_events != null)
					_events.signIn(false, "Error on sign in", e);
			}
		}
		
		return _signedIn;
	}

	@Override
	public void stop() {
		_inShutdown = true;
		}

	@Override
	public String getFiatBalance() {
		
		if (!isRunning()) return null;
		
		return null;
	}

	@Override
	public String getBsvBalance() {
		
		if (!isRunning()) return null;
		
		List<Pair<String,String>> headers = new ArrayList<Pair<String,String>>();
		headers.add(Pair.of("authToken", _auth.getAuthToken()));
		headers.add(Pair.of("walletID", _auth.getAccountId()));
		headers.add(Pair.of("type", ""));
		headers.add(Pair.of("currency", "BSV"));
		
		RestResponse restResult = _client.get("/v1/balance", headers);
		
		BalanceResponse response = null;
		
		try {
			response = _mapper.readValue(restResult.getBody(), BalanceResponse.class);
		} catch (JsonParseException e) {
			_events.walletMessage("Error in getBsvBalance", new WalletException(e));
		} catch (JsonMappingException e) {
			_events.walletMessage("Error in getBsvBalance", new WalletException(e));
		} catch (IOException e) {
			_events.walletMessage("Error in getBsvBalance", new WalletException(e));
		}
		
		if (response != null && response.getStatusCode() == 200) {
			double amount = response.getData().getTotalBalance().getBalance();
			return _formatter.getAmountFormatted(amount);
		}
		
		return "0";
		
	}

	@Override
	public void registerForEvents(WalletEvents events) {
		_events = events;
	}

	@Override
	public void unregisterForEvents() {
		_events = null;

	}

	@Override
	public ReceivingAddress getCurrentReceivingAddress() {

		if (!isRunning()) return null;
		
		List<Pair<String,String>> headers = new ArrayList<Pair<String,String>>();
		headers.add(Pair.of("authToken", _auth.getAuthToken()));
		headers.add(Pair.of("walletID", _auth.getAccountId()));
		
		RestResponse restResult = _client.get("/v1/address", headers);
		
		AddressResponse response = null;
		
		try {
			response = _mapper.readValue(restResult.getBody(), AddressResponse.class);
		} catch (JsonParseException e) {
			_events.walletMessage("Error in getCurrentReceivingAddress", new WalletException(e));
		} catch (JsonMappingException e) {
			_events.walletMessage("Error in getCurrentReceivingAddress", new WalletException(e));
		} catch (IOException e) {
			_events.walletMessage("Error in getCurrentReceivingAddress", new WalletException(e));
		}
		
		if (response != null && response.getStatusCode() == 200) {
			ReceivingAddress result = new ReceivingAddress(response.getData().getAddress(),
					response.getData().getPaymail());
			return result;
		}
		
		return null;
	}

	@Override
	public String getMnemonicCode() {

		if (!isRunning()) return null;
		
		List<Pair<String,String>> headers = new ArrayList<Pair<String,String>>();
		headers.add(Pair.of("authToken", _auth.getAuthToken()));
		headers.add(Pair.of("walletID", _auth.getAccountId()));
		
		RestResponse restResult = _client.get("/v1/mnemonic", headers);
		
		MnemonicResponse response = null;
		
		try {
			response = _mapper.readValue(restResult.getBody(), MnemonicResponse.class);
		} catch (JsonParseException e) {
			_events.walletMessage("Error in getMnemonicCode", new WalletException(e));
		} catch (JsonMappingException e) {
			_events.walletMessage("Error in getMnemonicCode", new WalletException(e));
		} catch (IOException e) {
			_events.walletMessage("Error in getMnemonicCode", new WalletException(e));
		}
		
		if (response != null && response.getStatusCode() == 200) {
			return response.getData().getMnemonic();
		}
		
		return restResult.getBody();
		
	}

	@Override
	public void restoreFromSeed(String seedCode, String passphrase) throws WalletException {
		// TODO Auto-generated method stub

	}

	@Override
	public File getQrCode(String address) throws IOException {
		
		File newFile = QRCode.from(address)
		.withSize(250, 250).file();
		
		return newFile;
	}

	@Override
	public void sendCoins(String toAddress, String amount, String memo) throws SendException {

		if (!isRunning()) return;
		
		
		
		SendData data = new SendData(toAddress,
				Float.valueOf(amount));
		SendData[] sendList = new SendData [] { data };
		SendRequest sendRequest = new SendRequest(sendList); 
		
		List<Pair<String,String>> headers = new ArrayList<Pair<String,String>>();
		headers.add(Pair.of("authToken", _auth.getAuthToken()));
		headers.add(Pair.of("walletID", _auth.getAccountId()));
		String json = null;
		
		try {
			json = _mapper.writeValueAsString(sendRequest);
		} catch (JsonProcessingException e) {
			_events.walletMessage("Error in getMnemonicCode", new WalletException(e));
			return;
		}
		RestResponse restResult = _client.post("/v1/send", headers, json);
		
		SendResponse response = null;
		
		try {
			response = _mapper.readValue(restResult.getBody(), SendResponse.class);
		} catch (JsonParseException e) {
			_events.walletMessage("Error in getMnemonicCode", new WalletException(e));
		} catch (JsonMappingException e) {
			_events.walletMessage("Error in getMnemonicCode", new WalletException(e));
		} catch (IOException e) {
			_events.walletMessage("Error in getMnemonicCode", new WalletException(e));
		}
		
		
		if (response != null && response.getStatusCode() == 200 
				&& response.getData().getTxIds().length>0) {
			
			//String transactionHash, String memo, String value, 
			//String prevBalance, String newBalance, String transactionTime
			
			_events.coinsSent(response.getData().getTxIds()[0], 
					memo, amount, null, null, null);
			
		} else 
			_events.walletMessage("Relysia returned code: " + response.getStatusCode() +
					" and message: " + response.getData().getMsg(), null);
		
		
	}

	@Override
	public List<DigitalAssetTransaction> getHistory(String sinceTransaction) throws WalletException {
		if (!isRunning()) return null;
	
		// Convert results into more generic object to return
		// to keep it generic and allow more wallet integrations 
		List<DigitalAssetTransaction> result = new ArrayList<DigitalAssetTransaction>();
		String oldNextPageToken = null;
		String nextPageToken = null;
		
		do {
		
		
			List<Pair<String,String>> headers = new ArrayList<Pair<String,String>>();
			headers.add(Pair.of("authToken", _auth.getAuthToken()));
			headers.add(Pair.of("walletID", _auth.getAccountId()));
			String url = "/v1/history";
			
			if (nextPageToken != null)
				url += "?nextPageToken=" +  nextPageToken;
			
			oldNextPageToken = nextPageToken;
			nextPageToken = null;
			
			RestResponse restResult = _client.get(url, headers);
			
			HistoryResponse response = null;
			
			try {
				response = _mapper.readValue(restResult.getBody(), HistoryResponse.class);
			} catch (JsonParseException e) {
				_events.walletMessage("Error in getHistory", new WalletException(e));
			} catch (JsonMappingException e) {
				_events.walletMessage("Error in getHistory", new WalletException(e));
			} catch (IOException e) {
				_events.walletMessage("Error in getHistory", new WalletException(e));
			}
			
			if (response != null && response.getStatusCode() == 200) {
				if (response.getData().getHistories() != null && 
						response.getData().getHistories().length >0) {
									
					// Capture next page token
					nextPageToken = response.getData().getNextPageTokenId();
					
					// If the same nextPageToken was passed then don't continue
					// to pull the same page
					
					if (nextPageToken != null && nextPageToken.equals(oldNextPageToken)) {
						nextPageToken = null;
					} else {
					
						// Relyia results will be in reverse order with latest transaction
						// at the top.
						// Stop adding to the result once sinceTransaction is found
						
						for (HistoryRow row : response.getData().getHistories()) {
							
							// Only take up to sinceTransaction
							if (row.getTxId().equals(sinceTransaction)) {
								// Set nextPageToken to null to break out of do loop
								nextPageToken = null;
								// Break out of for loop
								break;
							}
							
							DigitalAssetTransaction returnRow = new DigitalAssetTransaction();
							returnRow.setProtocol(row.getProtocol());
							returnRow.setFrom(row.getFrom());
							returnRow.setTimestamp(row.getTimestamp());
							returnRow.setNotes(row.getNotes());
							returnRow.setTxId(row.getTxId());
							returnRow.setType(row.getType());
							returnRow.setTo(row.getTo());
							returnRow.setBalance_change(row.getBalance_change());
							returnRow.setDocId(row.getDocId());
							result.add(returnRow);							
							
						}
					
					}
				}
			}
		
		} while (nextPageToken != null && nextPageToken.length() > 0);
		
		return result;
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
		case RESTOREFROMSEED:
			return false;
		case GETQRCODE:
		case SENDCOINS:
		case SIGNIN:
			return true;
		}
		
		return false;
	}

	@Override
	public AuthData getAuthData() {
		return _auth;
	}

	@Override
	public void setAuthData(AuthData authData) {
		_auth = authData;
		isRunning();
	}
	
	/*******************/
	/* Private Methods */
	/*******************/
	
	/**
	 * Sign in user.  _auth should contain credentials to send to endpoint.
	 * @throws WalletException
	 */
	private void signIn() throws WalletException {
		
		if (!_signedIn) {
		
			if (_auth == null || StringUtils.isEmpty(_auth.getPlainPassword())) return;
			
			Auth auth = new Auth(_auth.getEmailAddress(), _auth.getPlainPassword());
			try {
				String json = _mapper.writeValueAsString(auth);
				RestResponse restResult = _client.post("v1/auth", null, json);
				
				if (restResult.getException() != null)
					throw new WalletException(restResult.getException());
				
				System.out.println(restResult.getBody());
				System.out.println(restResult.getCode());
				
				AuthResponse response = _mapper.readValue(restResult.getBody(), AuthResponse.class);
				
				_auth.setAuthToken(response.getData().getToken());
				_auth.setUser(response.getData().getUserId());
				
				if (response.getStatusCode() == 200) 
					_signedIn = true;
				
			} catch (JsonProcessingException e) {
				throw new WalletException(e);
			} catch (IOException e) {
				throw new WalletException(e);
			}

		}
		
	}
	
	
}
