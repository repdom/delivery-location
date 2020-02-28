package com.mundo.delivery.DAO;

import org.springframework.http.HttpStatus;

import java.security.Timestamp;
import java.util.Date;

public class DataResponse {
        Date timestamp = new Date();
        private HttpStatus status;
        private String message;
        private ProductLocalityDao[] data;

    public DataResponse(HttpStatus HttpStatus, String message, ProductLocalityDao[] productLocalityDaos) {
        this.status = HttpStatus;
        this.message = message;
        this.data = productLocalityDaos;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductLocalityDao[] getData() {
        return data;
    }

    public void setData(ProductLocalityDao[] data) {
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date today) {
        this.timestamp = today;
    }
}
