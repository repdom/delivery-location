package com.mundo.delivery.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mundo.delivery.DAO.*;
import com.mundo.delivery.models.PuntoDeVenta;
import com.mundo.delivery.repository.puntoDeVentaRepository;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.util.*;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class PosQantityLocation {
    @Autowired
    private puntoDeVentaRepository puntoDeVentaRepository;

    @GetMapping("/pos")
    public List<PuntoDeVenta> getPuntosDeVenta() {
        return puntoDeVentaRepository.findAll();
    }

    @PostMapping("/closestPosLatLng")
    public ResponseEntity<DataResponse> getclosestPost(@RequestBody NearLocationDao nearLocationDao) {
        List<PuntoDeVenta> puntos = puntoDeVentaRepository.findAll();
        ProductLocalityDao[] productoLocalidad = new ProductLocalityDao[puntos.size()];

        int i = 0;
        for (PuntoDeVenta puntoDeVenta : puntos) {
            // double lat1, double lon1, double lat2, double lon2, String unit
            puntos.get(i).setDistance(distance(nearLocationDao.getLat(), nearLocationDao.getLng(),
                    Double.parseDouble(puntoDeVenta.getLatitude()), Double.parseDouble(puntoDeVenta.getLongitude()), "K"));
            // RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            MultiValueMap<String, String> params = new LinkedMultiValueMap();
            // const body = `ecommerce={"document_id":+"${this.encryptedCode}",+"get_data_type":+"invoiceDelivered",+"token":+"${Config.MUNDO_MOVIL_TOKEN}",+"delivery_id":+"${this.employeeId}"}`;
            // \"ROM\"
            System.out.printf(String.valueOf(Double.parseDouble(puntoDeVenta.getLatitude())));
            String request = "ecommerce={ \"token\": \"%s\", \"get_data_type\": \"%s\", \"point_of_sales_id\": \"%s\", \"codes\": \"%s\"}";
            String strRequest = String.format(request, "EVcsp9kjDrdAPdLeRA3x5yABThhXbj2Tke94", "getExistenceByPOS", puntoDeVenta.getCodigo(), nearLocationDao.getProductCode());

            RestTemplate template = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity(strRequest, headers);

            String tokenUrl = "http://mundomovil.com.do/mDelivery/GetData.php";

            Gson googleJson = new Gson();
            // product product;
            String json = template.exchange(tokenUrl, HttpMethod.POST, entity, String.class).getBody();
            product product = googleJson.fromJson(json, product.class);
            // System.out.printf(String.valueOf(product.getData()[0].getCodearticulo()));
            ProductLocalityDao productLocalityDao = new ProductLocalityDao(puntoDeVenta.getCodigo(),
                                                                            puntoDeVenta.getDescripcion(),
                                                                            puntoDeVenta.getLongitude(),
                                                                            puntoDeVenta.getLatitude(),
                                                                            product.getData()[0].getCodearticulo(),
                                                                            product.getData()[0].getExistencia(),
                                                                            puntos.get(i).getDistance());
            productoLocalidad[i] = productLocalityDao;
            i += 1;
        }

        for (int i2 = 0; i2 < productoLocalidad.length; i2++) {
            for (int j = 0; j < productoLocalidad.length; j++) {
                if (((ProductLocalityDao) productoLocalidad[i2]).getDistance() < ((ProductLocalityDao) productoLocalidad[j]).getDistance()) {
                    ProductLocalityDao puntoAux = productoLocalidad[i2];
                    productoLocalidad[i2] = productoLocalidad[j];
                    productoLocalidad[j] = puntoAux;
                }
            }
        }

        // JsonParser jsonParser = new JsonParser()
        DataResponse dataResponse = new DataResponse(HttpStatus.OK, "Success", productoLocalidad);

        //  httpResponse = new HttpResponse<ProductLocalityDao>(productoLocalidad);
        // JsonParser jsonParser = new JsonParser()
        // new ResponseBody<ProductLocalityDao[]>(productoLocalidad, HttpStatus.OK)
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/closestPos") //closestPos
    public ResponseEntity<DataResponse> getclosestPostLatLng(@RequestBody NearPos nearPosDao) {
        List<PuntoDeVenta> puntos = puntoDeVentaRepository.findAll();
        Optional<PuntoDeVenta> punto = puntoDeVentaRepository.findById(nearPosDao.getPos());
        ProductLocalityDao[] productoLocalidad = new ProductLocalityDao[puntos.size() - 1];

        int i = 0, h = 0;
        for (PuntoDeVenta puntoDeVenta : puntos) {
            // double lat1, double lon1, double lat2, double lon2, String unit
            puntos.get(i).setDistance(distance(Double.parseDouble(punto.get().getLatitude()), Double.parseDouble(punto.get().getLongitude()),
                    Double.parseDouble(puntoDeVenta.getLatitude()), Double.parseDouble(puntoDeVenta.getLongitude()), "K"));
            // RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            String request = "ecommerce={ \"token\": \"%s\", \"get_data_type\": \"%s\", \"point_of_sales_id\": \"%s\", \"codes\": \"%s\"}";
            String strRequest = String.format(request, "EVcsp9kjDrdAPdLeRA3x5yABThhXbj2Tke94", "getExistenceByPOS", puntoDeVenta.getCodigo(), nearPosDao.getProductCode());

            RestTemplate template = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity(strRequest, headers);

            String tokenUrl = "http://mundomovil.com.do/mDelivery/GetData.php";

            Gson googleJson = new Gson();
            // product product;
            String json = template.exchange(tokenUrl, HttpMethod.POST, entity, String.class).getBody();
            product product = googleJson.fromJson(json, product.class);
            // System.out.printf(String.valueOf(product.getData()[0].getCodearticulo()));
            if (!nearPosDao.getPos().equals(puntoDeVenta.getCodigo())) {
                ProductLocalityDao productLocalityDao = new ProductLocalityDao(puntoDeVenta.getCodigo(),
                        puntoDeVenta.getDescripcion(),
                        puntoDeVenta.getLongitude(),
                        puntoDeVenta.getLatitude(),
                        product.getData()[0].getCodearticulo(),
                        product.getData()[0].getExistencia(),
                        puntos.get(i).getDistance());
                productoLocalidad[h] = productLocalityDao;
                h += 1;
            }
            i += 1;
        }
        for (int i2 = 0; i2 < productoLocalidad.length; i2++) {
            for (int j = 0; j < productoLocalidad.length; j++) {
                if ((productoLocalidad[i2].getDistance() < productoLocalidad[j].getDistance())) {
                    ProductLocalityDao puntoAux = productoLocalidad[i2];
                    productoLocalidad[i2] = productoLocalidad[j];
                    productoLocalidad[j] = puntoAux;
                }
            }
        }
        // int status, String error, String message, ProductLocalityDao[] productLocalityDaos
        DataResponse dataResponse = new DataResponse(HttpStatus.OK, "Success", productoLocalidad);

        //  httpResponse = new HttpResponse<ProductLocalityDao>(productoLocalidad);
        // JsonParser jsonParser = new JsonParser()
        // new ResponseBody<ProductLocalityDao[]>(productoLocalidad, HttpStatus.OK)
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}
