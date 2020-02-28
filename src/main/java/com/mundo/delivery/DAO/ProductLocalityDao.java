package com.mundo.delivery.DAO;

public class ProductLocalityDao {
    private Long codigo;
    private String descripcion;
    private String longitude;
    private String latitude;
    private String codearticulo;
    private String existencia;
    private double distance;

    public ProductLocalityDao() {
    }

    public ProductLocalityDao(Long codigo, String descripcion, String longitude, String latitude, String codearticulo, String existencia, double distance) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.longitude = longitude;
        this.latitude = latitude;
        this.codearticulo = codearticulo;
        this.existencia = existencia;
        this.distance = distance;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCodearticulo() {
        return codearticulo;
    }

    public void setCodearticulo(String codearticulo) {
        this.codearticulo = codearticulo;
    }

    public String getExistencia() {
        return existencia;
    }

    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
