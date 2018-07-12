package org.art.web.controllers;

import org.apache.commons.io.FileUtils;
import org.art.entities.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static org.art.web.controllers.ControllerConstants.IMAGES_ROOT_PATH;

@Controller
@RequestMapping("/displayImage")
@SessionAttributes("user")
public class ImageController {

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] displayImage(HttpServletRequest req, HttpServletResponse resp,
                               @ModelAttribute("user") User user) throws IOException {

        String fileName = req.getParameter("image");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpg");
        File file = new File(IMAGES_ROOT_PATH + user.getLogin() + ".jpg");
        return FileUtils.readFileToByteArray(file);
    }
}
