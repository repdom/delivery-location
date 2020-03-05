package com.mundo.delivery.controllers;

import com.google.gson.Gson;
import com.mundo.delivery.DAO.*;
import com.mundo.delivery.models.PuntoDeVenta;
import com.mundo.delivery.repository.puntoDeVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

import java.util.*;

class SortByDistance implements Comparator<ProductLocalityDao>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(ProductLocalityDao a, ProductLocalityDao b)
    {
        return (int) (a.getDistance() - b.getDistance());
    }
}

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
        ArrayList<ProductLocalityDao> productLocalityDaos = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> params = new LinkedMultiValueMap();

        String request = "ecommerce={ \"token\": \"%s\", \"get_data_type\": \"%s\", \"codes\": \"%s\"}";
        String strRequest = String.format(request, "EVcsp9kjDrdAPdLeRA3x5yABThhXbj2Tke94", "getExistenceByPOS", nearLocationDao.getProductCode());
        RestTemplate template = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity(strRequest, headers);

        String tokenUrl = "http://mundomovil.com.do/mDelivery/GetData.php";

        String json = template.exchange(tokenUrl, HttpMethod.POST, entity, String.class).getBody();

        Gson googleJson = new Gson();
        ErpResponsePreproProduct product = googleJson.fromJson(json, ErpResponsePreproProduct.class);

        for (PuntoDeVenta puntoDeVenta : puntos) {
            for(PreproProduct data: product.getData()) {
                if (data.getPos_id().equals(String.valueOf(puntoDeVenta.getCodigo()))) {
                    double d = distance(nearLocationDao.getLat(), nearLocationDao.getLng(), Double.parseDouble(puntoDeVenta.getLatitude()), Double.parseDouble(puntoDeVenta.getLongitude()), "K");
                    ProductLocalityDao productLocalityDao = new ProductLocalityDao(
                            puntoDeVenta.getCodigo(),
                            puntoDeVenta.getDescripcion(),
                            puntoDeVenta.getLongitude(),
                            puntoDeVenta.getLatitude(),
                            data.getCodarticulo(),
                            data.getExistencia(),
                            d
                    );
                    productLocalityDaos.add(productLocalityDao);
                    break;
                }
            }
        }

        productLocalityDaos.sort((p1, p2) -> (int) (p1.getDistance() - p2.getDistance()));
        ProductLocalityDao[] products = new ProductLocalityDao[productLocalityDaos.size()];
        int y = 0;
        for(ProductLocalityDao p : productLocalityDaos) {
            products[y] = p;
            y++;
        }

        DataResponse dataResponse = new DataResponse(HttpStatus.OK, "Success", products);

        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PostMapping("/closestPos") //closestPos
    public ResponseEntity<DataResponse> getclosestPostLatLng(@RequestBody NearPos nearPosDao) {
        List<PuntoDeVenta> puntos = puntoDeVentaRepository.findAll();
        Optional<PuntoDeVenta> punto = puntoDeVentaRepository.findById(nearPosDao.getPos());

        ArrayList<ProductLocalityDao> productLocalityDaos = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> params = new LinkedMultiValueMap();

        String request = "ecommerce={ \"token\": \"%s\", \"get_data_type\": \"%s\", \"codes\": \"%s\"}";
        String strRequest = String.format(request, "EVcsp9kjDrdAPdLeRA3x5yABThhXbj2Tke94", "getExistenceByPOS", nearPosDao.getProductCode());
        RestTemplate template = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity(strRequest, headers);

        String tokenUrl = "http://mundomovil.com.do/mDelivery/GetData.php";

        String json = template.exchange(tokenUrl, HttpMethod.POST, entity, String.class).getBody();
        // System.out.printf(json);
        Gson googleJson = new Gson();
        ErpResponsePreproProduct product = googleJson.fromJson(json, ErpResponsePreproProduct.class);
        DataResponse dataResponse;
        if (product.getData() == null) {
            dataResponse = new DataResponse(HttpStatus.ACCEPTED, "No hay datos disponibles", new ProductLocalityDao[0]);
        } else {
            for (PuntoDeVenta puntoDeVenta : puntos) {
                for(PreproProduct data: product.getData()) {
                    if ((data.getPos_id().equals(String.valueOf(puntoDeVenta.getCodigo()))) && !puntoDeVenta.getCodigo().equals(nearPosDao.getPos())) {
                        double d = distance(Double.parseDouble(punto.get().getLatitude()), Double.parseDouble(punto.get().getLongitude()),
                                Double.parseDouble(puntoDeVenta.getLatitude()), Double.parseDouble(puntoDeVenta.getLongitude()), "K");
                        ProductLocalityDao productLocalityDao = new ProductLocalityDao(
                                puntoDeVenta.getCodigo(),
                                puntoDeVenta.getDescripcion(),
                                puntoDeVenta.getLongitude(),
                                puntoDeVenta.getLatitude(),
                                data.getCodarticulo(),
                                data.getExistencia(),
                                d
                        );
                        productLocalityDaos.add(productLocalityDao);
                        break;
                    }
                }
            }

            productLocalityDaos.sort((p1, p2) -> (int) (p1.getDistance() - p2.getDistance()));
            ProductLocalityDao[] products = new ProductLocalityDao[productLocalityDaos.size()];
            int y = 0;
            for(ProductLocalityDao p : productLocalityDaos) {
                products[y] = p;
                y++;
            }
            dataResponse = new DataResponse(HttpStatus.OK, "Success", products);
        }


        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
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