package com.mundo.delivery.DAO;

public class requestTemplate {
    private String token;
    private String get_data_type;
    private String point_of_sales_id;
    private String codes;

    public requestTemplate() {

    }

    public requestTemplate(String token, String get_data_type, String point_of_sales_id, String codes) {
        this.token = token;
        this.get_data_type = get_data_type;
        this.point_of_sales_id = point_of_sales_id;
        this.codes = codes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGet_data_type() {
        return get_data_type;
    }

    public void setGet_data_type(String get_data_type) {
        this.get_data_type = get_data_type;
    }

    public String getPoint_of_sales_id() {
        return point_of_sales_id;
    }

    public void setPoint_of_sales_id(String point_of_sales_id) {
        this.point_of_sales_id = point_of_sales_id;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }
}
