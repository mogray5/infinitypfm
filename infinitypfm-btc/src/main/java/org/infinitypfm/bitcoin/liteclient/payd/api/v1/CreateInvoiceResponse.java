package org.infinitypfm.bitcoin.liteclient.payd.api.v1;

public class CreateInvoiceResponse {
	  
	  private String id;
	  private String reference;
	  private String description;
	  private long satoshis;
	  private String expiresAt;
	  private String paymentReceivedAt;
	  private String refundTo;
	  private String refundedAt;
	  private String state;
	  private String createdAt;
	  private String updatedAt;
	  private String deletedAt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getSatoshis() {
		return satoshis;
	}
	public void setSatoshis(long satoshis) {
		this.satoshis = satoshis;
	}
	public String getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}
	public String getPaymentReceivedAt() {
		return paymentReceivedAt;
	}
	public void setPaymentReceivedAt(String paymentReceivedAt) {
		this.paymentReceivedAt = paymentReceivedAt;
	}
	public String getRefundTo() {
		return refundTo;
	}
	public void setRefundTo(String refundTo) {
		this.refundTo = refundTo;
	}
	public String getRefundedAt() {
		return refundedAt;
	}
	public void setRefundedAt(String refundedAt) {
		this.refundedAt = refundedAt;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
	}
	  
}
