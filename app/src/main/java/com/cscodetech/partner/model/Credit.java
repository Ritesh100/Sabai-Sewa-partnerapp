package com.cscodetech.partner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credit{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("CreditList")
	private List<CreditList> catlist;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public List<CreditList> getCatlist(){
		return catlist;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}