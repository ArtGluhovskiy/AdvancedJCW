package org.art.web.controllers;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.entities.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

import static org.art.web.controllers.ControllerConstants.IMAGES_ROOT_PATH;

@Controller
@RequestMapping("/displayImage")
@SessionAttributes("user")
public class ImageController {

    private static final Logger LOG = LogManager.getLogger(ImageController.class);

    @ResponseBody
    @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] displayImage(@ModelAttribute("user") User user) {
        LOG.debug("ImageController: displayImage()");
        byte[] image = new byte[0];
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpg");
        String filePath = IMAGES_ROOT_PATH + user.getLogin() + ".jpg";
        File file = new File(filePath);
        try {
            image = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            LOG.info("ImageController: IOException - cannot get image from file! File path: {}", filePath, e);
        }
        return image;
    }
}
