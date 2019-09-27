package com.example.lpukipathshala.Equipments;

public class Add_Equipment_Model {
    private String userId;
    private String type;
    private String equipmentId;
    private String EquipmentName;
    private String branch;
    private String description;
    private String price;
    private String picUrl;

    public Add_Equipment_Model() {
    }

    public Add_Equipment_Model(String userId, String type, String equipmentId, String equipmentName, String branch, String description, String price, String picUrl) {
        this.userId = userId;
        this.type = type;
        this.equipmentId = equipmentId;
        EquipmentName = equipmentName;
        this.branch = branch;
        this.description = description;
        this.price = price;
        this.picUrl = picUrl;
    }

    public Add_Equipment_Model(String equipmentName, String price, String picUrl, String equipmentId) {
        this.equipmentId = equipmentId;
        EquipmentName = equipmentName;
        this.price = price;
        this.picUrl = picUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return EquipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        EquipmentName = equipmentName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
