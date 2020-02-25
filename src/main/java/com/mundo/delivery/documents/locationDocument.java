package com.mundo.delivery.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("LatLng")
public class locationDocument {
    @Id
    private String _id;
    private String latitud;
    private String longitud;
    private Date fechaLocalizacion;
    private String mensajeroCodigo;
    private String velocidad;
    private String accuracy;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Date getFechaLocalizacion() {
        return fechaLocalizacion;
    }

    public void setFechaLocalizacion(Date fechaLocalizacion) {
        this.fechaLocalizacion = fechaLocalizacion;
    }

    public String getMensajeroCodigo() {
        return mensajeroCodigo;
    }

    public void setMensajeroCodigo(String mensajeroCodigo) {
        this.mensajeroCodigo = mensajeroCodigo;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
