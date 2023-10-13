package com.cscodetech.partner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatlistItem{

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("cat_subtitle")
	@Expose
	private String catSubtitle;
	@SerializedName("cat_name")
	@Expose
	private String catName;
	@SerializedName("cat_status")
	@Expose
	private String catStatus;
	@SerializedName("cat_img")
	@Expose
	private String catImg;
	@SerializedName("cat_video")
	@Expose
	private String catVideo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatSubtitle() {
		return catSubtitle;
	}

	public void setCatSubtitle(String catSubtitle) {
		this.catSubtitle = catSubtitle;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getCatStatus() {
		return catStatus;
	}

	public void setCatStatus(String catStatus) {
		this.catStatus = catStatus;
	}

	public String getCatImg() {
		return catImg;
	}

	public void setCatImg(String catImg) {
		this.catImg = catImg;
	}

	public String getCatVideo() {
		return catVideo;
	}

	public void setCatVideo(String catVideo) {
		this.catVideo = catVideo;
	}

	public boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}