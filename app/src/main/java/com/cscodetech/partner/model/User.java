package com.cscodetech.partner.model;

import com.google.gson.annotations.SerializedName;

public class User {

	@SerializedName("ccode")
	private String ccode;

	@SerializedName("category_id")
	private String categoryid;

	@SerializedName("aprove")
	private String aprove;

	@SerializedName("password")
	private String password;

	@SerializedName("code")
	private String code;

	@SerializedName("wallet")
	private String wallet;

	@SerializedName("rdate")
	private String rdate;

	@SerializedName("refercode")
	private Object refercode;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	@SerializedName("city")
	private String city;

	@SerializedName("address")
	private String address;

	@SerializedName("category")
	private String category;

	@SerializedName("bio")
	private String boi;

	@SerializedName("pimg")
	private String pimg;

	@SerializedName("credit")
	private int credit;

	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public String getAprove() {
		return aprove;
	}

	public void setAprove(String aprove) {
		this.aprove = aprove;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCcode(String ccode){
		this.ccode = ccode;
	}

	public String getCcode(){
		return ccode;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setWallet(String wallet){
		this.wallet = wallet;
	}

	public String getWallet(){
		return wallet;
	}

	public void setRdate(String rdate){
		this.rdate = rdate;
	}

	public String getRdate(){
		return rdate;
	}

	public void setRefercode(Object refercode){
		this.refercode = refercode;
	}

	public Object getRefercode(){
		return refercode;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBoi() {
		return boi;
	}

	public void setBoi(String boi) {
		this.boi = boi;
	}

	public String getPimg() {
		return pimg;
	}

	public void setPimg(String pimg) {
		this.pimg = pimg;
	}
}