package com.cscodetech.partner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderDataItem implements Parcelable {

	@SerializedName("p_method_name")
	private final String pMethodName;

	@SerializedName("customer_address")
	private final String customerAddress;

	@SerializedName("Order_Status")
	private final String orderStatus;

	@SerializedName("customer_mobile")
	private final String customerMobile;

	@SerializedName("add_total")
	private final String addTotal;

	@SerializedName("Order_Total")
	private final String orderTotal;

	@SerializedName("sign")
	private final String sign;

	@SerializedName("order_time")
	private final String orderTime;

	@SerializedName("Order_Product_Data")
	private ArrayList<OrderProductDataItem> orderProductData;

	@SerializedName("order_date")
	private final String orderDate;

	@SerializedName("customer_email")
	private final String customerEmail;

	@SerializedName("order_dateslot")
	private final String orderDateslot;

	@SerializedName("Order_Transaction_id")
	private final String orderTransactionId;

	@SerializedName("add_on_data")
	private ArrayList<AddOnDataItem> addOnData;

	@SerializedName("Order_SubTotal")
	private final String orderSubTotal;

	@SerializedName("customer_name")
	private final String customerName;

	@SerializedName("order_id")
	private final String orderId;

	@SerializedName("category_id")
	private String categoryId;

	@SerializedName("r_credit")
	private final String rCredit;

	@SerializedName("hire_type")
	private  String hireType;

	@SerializedName("add_desc")
	private String addDesc;

	@SerializedName("add_price")
	private String addPrice;

	@SerializedName("lats")
	private double  lats;

	@SerializedName("longs")
	private double  longs;

	protected OrderDataItem(Parcel in) {
		pMethodName = in.readString();
		customerAddress = in.readString();
		orderStatus = in.readString();
		customerMobile = in.readString();
		addTotal = in.readString();
		orderTotal = in.readString();
		sign = in.readString();
		orderTime = in.readString();
		orderDate = in.readString();
		customerEmail = in.readString();
		orderDateslot = in.readString();
		orderTransactionId = in.readString();
		orderSubTotal = in.readString();
		customerName = in.readString();
		orderId = in.readString();
		categoryId = in.readString();
		rCredit = in.readString();
		hireType = in.readString();
		addDesc = in.readString();
		addPrice = in.readString();
		lats = in.readDouble();
		longs = in.readDouble();
	}

	public static final Creator<OrderDataItem> CREATOR = new Creator<OrderDataItem>() {
		@Override
		public OrderDataItem createFromParcel(Parcel in) {
			return new OrderDataItem(in);
		}

		@Override
		public OrderDataItem[] newArray(int size) {
			return new OrderDataItem[size];
		}
	};

	public String getPMethodName(){
		return pMethodName;
	}

	public String getCustomerAddress(){
		return customerAddress;
	}

	public String getOrderStatus(){
		return orderStatus;
	}

	public String getCustomerMobile(){
		return customerMobile;
	}

	public String getAddTotal(){
		return addTotal;
	}

	public String getOrderTotal(){
		return orderTotal;
	}

	public String getSign(){
		return sign;
	}

	public String getOrderTime(){
		return orderTime;
	}

	public ArrayList<OrderProductDataItem> getOrderProductData(){
		return orderProductData;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public String getCustomerEmail(){
		return customerEmail;
	}

	public String getOrderDateslot(){
		return orderDateslot;
	}

	public String getOrderTransactionId(){
		return orderTransactionId;
	}

	public ArrayList<AddOnDataItem> getAddOnData(){
		return addOnData;
	}

	public String getOrderSubTotal(){
		return orderSubTotal;
	}

	public String getCustomerName(){
		return customerName;
	}

	public String getOrderId(){
		return orderId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getRCredit(){
		return rCredit;
	}

	public String getHireType() {
		return hireType;
	}

	public String getAddDesc() {
		return addDesc;
	}

	public String getAddPrice() {
		return addPrice;
	}

	public double  getLats() {
		return lats;
	}

	public void setLats(long lats) {
		this.lats = lats;
	}

	public double  getLongs() {
		return longs;
	}

	public void setLongs(long longs) {
		this.longs = longs;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(pMethodName);
		dest.writeString(customerAddress);
		dest.writeString(orderStatus);
		dest.writeString(customerMobile);
		dest.writeString(addTotal);
		dest.writeString(orderTotal);
		dest.writeString(sign);
		dest.writeString(orderTime);
		dest.writeString(orderDate);
		dest.writeString(customerEmail);
		dest.writeString(orderDateslot);
		dest.writeString(orderTransactionId);
		dest.writeString(orderSubTotal);
		dest.writeString(customerName);
		dest.writeString(orderId);
		dest.writeString(categoryId);
		dest.writeString(rCredit);
		dest.writeString(hireType);
		dest.writeString(addPrice);
		dest.writeString(addDesc);
		dest.writeDouble(lats);
		dest.writeDouble(longs);
	}
}