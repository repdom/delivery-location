package com.mundo.delivery.DAO;

public class product {
    private String code;
    private String description;
    private data[] data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public com.mundo.delivery.DAO.data[] getData() {
        return data;
    }

    public void setData(com.mundo.delivery.DAO.data[] data) {
        this.data = data;
    }
}
