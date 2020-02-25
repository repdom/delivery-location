package com.mundo.delivery.controllers;

import com.mundo.delivery.models.LocalityPicture;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

@RestController
public class LocalityController {

    @PostMapping("/localityPhoto")
    public ArrayList<String> postLocality(@RequestBody LocalityPicture... localityPhoto) {
        ArrayList<String> nameImages = new ArrayList<>();
        for (LocalityPicture lP: localityPhoto) {
            byte[] data = Base64.decodeBase64(lP.getImage());
            // /Users/markelhage/Desktop/Hermes/hermes-storage/client
            try (OutputStream stream = new FileOutputStream(lP.getPath() + lP.getContainer() + "/" + lP.getName())) {
                stream.write(data);
                nameImages.add(lP.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nameImages;
    }

}
