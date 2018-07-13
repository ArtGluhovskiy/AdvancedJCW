package org.art.web.controllers;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.web.auth.StringEncoder;
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

    private static final Logger LOG = LogManager.getLogger(LoginController.class);

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public String loginPage(ModelMap modelMap,
                            @RequestParam(value = "login") String login,
                            @RequestParam(value = "password") String password) {

        LOG.debug("LoginController: loginPage()");
        modelMap.put("pageName", "login");
        if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
            modelMap.put("errorMsg", "Invalid login or password!");
            return LOGIN_VIEW;
        }
        User user;
        try {
            user = userService.getUserByLogin(login);
        } catch (ServiceBusinessException e) {
            modelMap.put("errorMsg", "Invalid login or password!");
            LOG.info("LoginController: ServiceBusinessException - cannot find user with such login in DB! User login: {}", login, e);
            return LOGIN_VIEW;

        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", INTERNAL_ERROR_MESSAGE);
            LOG.error("LoginController: ServiceSystemException - cannot find user with such login in DB! User login: {}", login, e);
            return LOGIN_VIEW;
        }
        if (user.getPassword().equals(StringEncoder.encode(password)) && user.getStatus().equals("ACTIVE")) {
            modelMap.put("user", user);
            if (user.getRole().equals("admin")) {
                return REDIRECT_ADMIN_VIEW;
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

