package com.cscodetech.partner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditTrasection {
    @SerializedName("CreditItem")
    @Expose
    private List<CreditItem> creditItem = null;
    @SerializedName("partnercredittotal")
    @Expose
    private Integer partnercredittotal;
    @SerializedName("partnerdebittotal")
    @Expose
    private Integer partnerdebittotal;
    @SerializedName("creditval")
    @Expose
    private String creditval;
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ResponseMsg")
    @Expose
    private String responseMsg;

    public List<CreditItem> getCreditItem() {
        return creditItem;
    }

    public void setCreditItem(List<CreditItem> creditItem) {
        this.creditItem = creditItem;
    }

    public Integer getPartnercredittotal() {
        return partnercredittotal;
    }

    public void setPartnercredittotal(Integer partnercredittotal) {
        this.partnercredittotal = partnercredittotal;
    }

    public Integer getPartnerdebittotal() {
        return partnerdebittotal;
    }

    public void setPartnerdebittotal(Integer partnerdebittotal) {
        this.partnerdebittotal = partnerdebittotal;
    }

    public String getCreditval() {
        return creditval;
    }

    public void setCreditval(String creditval) {
        this.creditval = creditval;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

}
