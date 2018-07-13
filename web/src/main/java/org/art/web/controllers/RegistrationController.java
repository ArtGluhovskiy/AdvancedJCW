package org.art.web.controllers;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.entities.DifficultyGroup;
import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceSystemException;
import org.art.web.auth.StringEncoder;
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
import static org.art.web.controllers.ControllerConstants.*;

@Controller
@SessionAttributes("user")
@RequestMapping(value = "/registration")
public class RegistrationController {

    private static final Logger LOG = LogManager.getLogger(RegistrationController.class);

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/main")
    public String regPage(ModelMap modelMap) {
        LOG.debug("RegistrationController: regPage(...)");
        modelMap.put("user", new User());
        return REGISTRATION_MAIN_VIEW;
    }

    @PostMapping
    public String register(ModelMap modelMap,
                           @Valid @ModelAttribute("user") User user, BindingResult br,
                           @RequestParam(value = "avatar", required = false) MultipartFile image) {

        LOG.debug("RegistrationController: register(...)");
        if (!br.hasErrors()) {
            try {
                if (!validateImage(image, modelMap)) {
                    return REGISTRATION_MAIN_VIEW;
                }
                saveImage(user.getLogin() + ".jpg", image);
                //Increasing of rating by 1 after registration
                user.setRating(1);
                fillUser(user);
                user = userService.save(user);
            } catch (ServiceSystemException | IOException e) {
                modelMap.put("errorMsg", INTERNAL_ERROR_MESSAGE);
                LOG.error("RegistrationController: System exception - cannot register user in DB! User: {}", user, e);
                return REGISTRATION_MAIN_VIEW;
            }
            modelMap.put("user", user);
            return REDIRECT_STATISTICS_VIEW;
        }
        return REGISTRATION_MAIN_VIEW;
    }

    private void saveImage(String filename, MultipartFile image) throws IOException {
        File file = new File(IMAGES_ROOT_PATH + filename);
        FileUtils.writeByteArrayToFile(file, image.getBytes());
    }

    private boolean validateImage(MultipartFile image, ModelMap modelMap) {
        if (image == null || image.isEmpty()) {
            modelMap.put("errorMsg", "Image was not loaded!");
            return false;
        } else if (image.getContentType() == null || !image.getContentType().equals("image/jpeg")) {
            modelMap.put("errorMsg", "Only JPG images are accepted.");
            return false;
        }
        return true;
    }

    private void fillUser(User user) {
        user.setBirthDate(toSQLDate(user.getBirth()));
        user.setPassword(StringEncoder.encode(user.getPassword()));
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setRole("user");
        user.setStatus("ACTIVE");
        user.setLevel(DifficultyGroup.BEGINNER.toString());
    }
}
