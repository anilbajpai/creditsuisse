package com.cs.contracts;

public class InstrumentPojo {
	private int instrumentId;
	public int getInstrumentId() {
		return instrumentId;
	}
	public void setInstrumentId(int instrumentId) {
		this.instrumentId = instrumentId;
	}
	public String getInstrumentDesc() {
		return instrumentDesc;
	}
	public void setInstrumentDesc(String instrumentDesc) {
		this.instrumentDesc = instrumentDesc;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	private String instrumentDesc;
	private String isActive;
}
