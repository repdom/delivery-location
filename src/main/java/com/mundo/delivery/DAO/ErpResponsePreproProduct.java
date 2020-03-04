package com.mundo.delivery.DAO;

public class ErpResponsePreproProduct {
    private String code;
    private String description;
    private PreproProduct[] data;

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

    public PreproProduct[] getData() {
        return data;
    }

    public void setData(PreproProduct[] data) {
        this.data = data;
    }
}
