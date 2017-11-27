package org.art.web.controllers;

import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.web.auth.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(names = {"user", "pageName"})
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private UserService userService;

    public static final String LOGIN = "login/main";

    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.GET})
    public String loginPage(ModelMap modelMap,
                            @RequestParam(value = "login") String login,
                            @RequestParam(value = "password") String password) {

        modelMap.put("pageName", "login");

        if ("".equals(login) || "".equals(password) || login == null || password == null) {
            modelMap.put("errorMsg", "Invalid login or password!");
            return LOGIN;
        }
        User user;
        try {
            user = userService.getUserByLogin(login);
        } catch (ServiceBusinessException e) {
            //If no user with such login was found
            modelMap.put("errorMsg", "Invalid login or password!");
            return LOGIN;

        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
            return LOGIN;
        }
        if (user.getPassword().equals(Encoder.encode(password)) && user.getStatus().equals("ACTIVE")) {
            modelMap.put("user", user);
            if (user.getRole().equals("admin")) {
                return "redirect:admin";
            }
            return "redirect:statistics";

        } else if (user.getStatus().equals("NOT ACTIVE")) {
            modelMap.put("errorMsg", "User with such login and password is not active now! Please, contact with technical support!");
            return LOGIN;
        } else {
            //In case of incorrect password
            modelMap.put("errorMsg", "Invalid login or password!");
            return LOGIN;
        }
    }
}

