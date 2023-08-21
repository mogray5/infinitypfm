package org.infinitypfm.core.data;

public class PlanEventType {

	private int eventTypeID;
	private String eventTypeName;
	private int allowAmmounts;
	private int allowPercents;
	
	public static final int INVEST = 0;
	public static final int DRAW = 1;
	public static final int EARN = 2;
	public static final int RETURN = 3;
	
	public PlanEventType(int eventTypeID, String eventTypeName, int allowAmmounts, int allowPercents) {
		super();
		this.eventTypeID = eventTypeID;
		this.eventTypeName = eventTypeName;
		this.allowAmmounts = allowAmmounts;
		this.allowPercents = allowPercents;
	}
	
	public PlanEventType(String eventTypeName, int allowAmmounts, int allowPercents) {
		super();
		this.eventTypeName = eventTypeName;
		this.allowAmmounts = allowAmmounts;
		this.allowPercents = allowPercents;
	}
	public int getEventTypeID() {
		return eventTypeID;
	}
	public void setEventTypeID(int eventTypeID) {
		this.eventTypeID = eventTypeID;
	}
	public String getEventTypeName() {
		return eventTypeName;
	}
	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}
	public int getAllowAmmounts() {
		return allowAmmounts;
	}
	public void setAllowAmmounts(int allowAmmounts) {
		this.allowAmmounts = allowAmmounts;
	}
	public int getAllowPercents() {
		return allowPercents;
	}
	public void setAllowPercents(int allowPercents) {
		this.allowPercents = allowPercents;
	}
	
}
