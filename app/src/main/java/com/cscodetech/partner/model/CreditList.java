package com.cscodetech.partner.model;

import com.google.gson.annotations.SerializedName;

public class CreditList {

	@SerializedName("credit_amt")
	private String creditAmt;

	@SerializedName("amt")
	private String amt;

	@SerializedName("id")
	private String id;

	@SerializedName("status")
	private String status;

	public String getCreditAmt(){
		return creditAmt;
	}

	public String getAmt(){
		return amt;
	}

	public String getId(){
		return id;
	}

	public String getStatus(){
		return status;
	}

	public boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}