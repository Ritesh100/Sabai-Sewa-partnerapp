package com.cscodetech.partner.model;

import com.google.gson.annotations.SerializedName;

public class ResponseMessge {

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	@SerializedName("signupcredit")
	private String signupcredit;

	@SerializedName("Partner_credit")
	private int PartnerCredit;

	@SerializedName("code")
	private String code;


	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}

	public String getSignupcredit() {
		return signupcredit;
	}

	public void setSignupcredit(String signupcredit) {
		this.signupcredit = signupcredit;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getPartnerCredit() {
		return PartnerCredit;
	}

	public void setPartnerCredit(int partnerCredit) {
		PartnerCredit = partnerCredit;
	}
}