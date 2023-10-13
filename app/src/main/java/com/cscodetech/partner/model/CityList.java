package com.cscodetech.partner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cname")
    @Expose
    private String cname;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
