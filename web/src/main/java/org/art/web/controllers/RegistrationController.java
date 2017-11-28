package org.art.web.controllers;

import org.apache.commons.io.FileUtils;
import org.art.entities.DifficultyGroup;
import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceSystemException;
import org.art.web.auth.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.sql.Date;

import static org.art.dao.utils.DateTimeUtil.toSQLDate;

@Controller
@SessionAttributes("user")
@RequestMapping(value = "/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    static final String REGISTRATION_MAIN = "registration/main";
    static final String IMAGES_PATH = "C:\\Users\\admin1\\IdeaProjects\\AdvancedJCW\\web\\src\\main\\resources\\img\\";

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String regPage(ModelMap modelMap) {
        modelMap.put("user", new User());
        return REGISTRATION_MAIN;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String register(ModelMap modelMap,

                           @Valid @ModelAttribute("user") User user, BindingResult br,
                           @RequestParam(value = "avatar", required = false) MultipartFile image) {

        if(!br.hasErrors()) {
            try {
                if (!validateImage(image, modelMap)) {
                    return REGISTRATION_MAIN;
                }
                saveImage(user.getLogin() + ".jpg", image);
                //Increasing of rating by 1 after registration
                user.setRating(1);
                fillUser(user);
                user = userService.save(user);
            } catch (ServiceSystemException e) {
                modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
                return REGISTRATION_MAIN;
            } catch (IOException e) {
                modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
                return REGISTRATION_MAIN;
            }
            modelMap.put("user", user);
            return "redirect:statistics";
        }
        return REGISTRATION_MAIN;
    }

    private void saveImage(String filename, MultipartFile image) throws IOException {
        File file = new File(IMAGES_PATH + filename);
        FileUtils.writeByteArrayToFile(file, image.getBytes());
    }

    private boolean validateImage(MultipartFile image, ModelMap modelMap) {
        if (!image.getContentType().equals("image/jpeg")) {
            modelMap.put("errorMsg", "Only JPG images accepted");
            return false;
        } else if (image == null && image.isEmpty()) {
            modelMap.put("errorMsg", "Image was not loaded!");
            return false;
        }
        return true;
    }

    private void fillUser(User user) {
        user.setBirthDate(toSQLDate(user.getBirth()));
        user.setPassword(Encoder.encode(user.getPassword()));
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setRole("user");
        user.setStatus("ACTIVE");
        user.setLevel(DifficultyGroup.BEGINNER.toString());
    }
}
