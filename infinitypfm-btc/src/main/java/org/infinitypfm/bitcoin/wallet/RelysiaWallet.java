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

import org.apache.commons.lang3.tuple.Pair;
import org.infinitypfm.bitcoin.relysia.api.v1.AddressResponse;
import org.infinitypfm.bitcoin.relysia.api.v1.Auth;
import org.infinitypfm.bitcoin.relysia.api.v1.AuthResponse;
import org.infinitypfm.bitcoin.relysia.api.v1.BalanceResponse;
import org.infinitypfm.bitcoin.relysia.api.v1.MnemonicResponse;
import org.infinitypfm.bitcoin.wallet.exception.SendException;
import org.infinitypfm.bitcoin.wallet.exception.WalletException;
import org.infinitypfm.core.data.AuthData;
import org.infinitypfm.core.data.ReceivingAddress;
import org.infinitypfm.core.data.RestResponse;
import org.infinitypfm.core.util.RestClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.glxn.qrgen.QRCode;

public class RelysiaWallet implements BsvWallet {

	private AuthData _auth = null;
	private boolean _signedIn = false;
	private RestClient _client = null;
	private ObjectMapper _mapper = null;
	private WalletEvents _events = null;
	
	/**********/
	/* cTor's */
	/**********/

	public RelysiaWallet(String baseUrl) {
		_client = new RestClient(baseUrl);
		_mapper = new ObjectMapper();
	}
	
	public RelysiaWallet(String baseUrl, AuthData auth) {
		super();
		_auth = auth;
		_client = new RestClient(baseUrl);
		_mapper = new ObjectMapper();
		
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
	public void stop() {}

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
			double amount = response.getData().getCoins()[0].getBalance();
			return String.valueOf(amount);
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
	public void sendCoins(String toAddress, String amount) throws SendException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isImplemented(WalletFunction function) {

		switch (function) {
		
		case GETSETBALANCEFIAT:
			return false;
		case GETSETBALANCEBSV:
		case REGISTERFOREVENTS:
		case UNREGISTERFOREVENTS:
		case CURRENTRECEIVINGADDRESS:
		case GETMNEUMONIC:
			return true;
		case RESTOREFROMSEED:
			return false;
		case GETQRCODE:
			return true;
		case SENDCOINS:
			return false;
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
