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

import static org.art.web.controllers.ControllerConstants.*;

@Controller
@SessionAttributes(names = {"user", "pageName"})
@RequestMapping(value = "/login")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public String loginPage(ModelMap modelMap,
                            @RequestParam(value = "login") String login,
                            @RequestParam(value = "password") String password) {

        modelMap.put("pageName", "login");
        if (EMPTY.equals(login) || "".equals(password) || login == null || password == null) {
            modelMap.put("errorMsg", "Invalid login or password!");
            return LOGIN_VIEW;
        }
        User user;
        try {
            user = userService.getUserByLogin(login);
        } catch (ServiceBusinessException e) {
            //If no user with such login was found
            modelMap.put("errorMsg", "Invalid login or password!");
            return LOGIN_VIEW;

        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
            return LOGIN_VIEW;
        }
        if (user.getPassword().equals(Encoder.encode(password)) && user.getStatus().equals("ACTIVE")) {
            modelMap.put("user", user);
            if (user.getRole().equals("admin")) {
                return "redirect:admin";
            }
            return REDIRECT_STATISTICS_VIEW;

        } else if (user.getStatus().equals("NOT ACTIVE")) {
            modelMap.put("errorMsg", "User with such login and password is not active now! Please, contact with technical support!");
            return LOGIN_VIEW;
        } else {
            //In case of incorrect password
            modelMap.put("errorMsg", "Invalid login or password!");
            return LOGIN_VIEW;
        }
    }
}

