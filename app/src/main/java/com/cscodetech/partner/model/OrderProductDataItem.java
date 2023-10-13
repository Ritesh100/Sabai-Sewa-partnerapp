package com.cscodetech.partner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrderProductDataItem implements Parcelable{

	@SerializedName("Product_quantity")
	private String productQuantity;

	@SerializedName("Product_price")
	private double productPrice;

	@SerializedName("Product_name")
	private String productName;

	@SerializedName("Product_discount")
	private double productDiscount;

	@SerializedName("Product_total")
	private double productTotal;

	public String getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(String productQuantity) {
		this.productQuantity = productQuantity;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductDiscount() {
		return productDiscount;
	}

	public void setProductDiscount(double productDiscount) {
		this.productDiscount = productDiscount;
	}

	public double getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(int productTotal) {
		this.productTotal = productTotal;
	}

	protected OrderProductDataItem(Parcel in) {
		productQuantity = in.readString();
		productPrice = in.readDouble();
		productName = in.readString();
		productDiscount = in.readDouble();
		productTotal = in.readDouble();
	}

	public static final Creator<OrderProductDataItem> CREATOR = new Creator<OrderProductDataItem>() {
		@Override
		public OrderProductDataItem createFromParcel(Parcel in) {
			return new OrderProductDataItem(in);
		}

		@Override
		public OrderProductDataItem[] newArray(int size) {
			return new OrderProductDataItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(productQuantity);
		dest.writeDouble(productPrice);
		dest.writeString(productName);
		dest.writeDouble(productDiscount);
		dest.writeDouble(productTotal);
	}
}