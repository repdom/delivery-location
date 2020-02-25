package com.mundo.delivery.documents;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("Mensajero")
public class delivery {
    @MongoId
    private String _id;
    private String nombre;
    private String imagenErp;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenErp() {
        return imagenErp;
    }

    public void setImagenErp(String imagenErp) {
        this.imagenErp = imagenErp;
    }
}
