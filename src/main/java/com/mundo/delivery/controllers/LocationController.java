package com.mundo.delivery.controllers;

import com.mundo.delivery.DeliveryApplication;
import com.mundo.delivery.documents.delivery;
import com.mundo.delivery.models.Location;
import com.mundo.delivery.documents.locationDocument;
import com.mundo.delivery.repository.deliveryRepository;
import com.mundo.delivery.repository.latLngRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LocationController {
    @Autowired
    public latLngRepository latLngRepository;

    @Autowired
    public deliveryRepository deliveryRepository;

    @GetMapping("/getLocation")
    public String getTest() {
        return "";
    }

    @PostMapping("/postLocation")
    public Location postLocation(@RequestBody Location location) {
        Optional<delivery> delivery = deliveryRepository.findById(location.getDeliveryCode());
        if (delivery.isPresent()) {
            System.out.println("Existe: " + delivery.get().get_id());
        } else {
            delivery deliveryCreate = new delivery();
            deliveryCreate.set_id(location.getDeliveryCode());
            deliveryCreate.setImagenErp(location.getDeliveryImage());
            deliveryCreate.setNombre(location.getDeliveryName());
            deliveryRepository.save(deliveryCreate);
            // System.out.println("No existe.");
        }
        locationDocument locationDocument = new locationDocument();
        locationDocument.setAccuracy(String.valueOf(location.getHorizontalAccuracy()));
        locationDocument.setLatitud(String.valueOf(location.getLatitude()));
        locationDocument.setLongitud(String.valueOf(location.getLongitude()));
        locationDocument.setVelocidad(String.valueOf(location.getSpeed()));
        locationDocument.setMensajeroCodigo(location.getDeliveryCode());
        locationDocument.setFechaLocalizacion(location.getTimestamp());

        latLngRepository.save(locationDocument);

        return location;
    }
    @MessageMapping("/")
    public void send(Location message) throws Exception {
        System.out.println("Probando socket: " + message.getLatitude());
    }
}
