package org.art.web.controllers;

import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

import static org.art.web.controllers.ControllerConstants.LOGIN_VIEW;
import static org.art.web.controllers.ControllerConstants.RATING_VIEW;

@Controller
@SessionAttributes("user")
@RequestMapping("/rating")
public class RatingController {

    private final UserService userService;

    @Autowired
    public RatingController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showRating(ModelMap modelMap, @ModelAttribute("user") User user) {

        if (user == null || user.getLogin() == null) {
            return LOGIN_VIEW;
        }
        try {
            List<User> userList = userService.getTopUsers(10);
            modelMap.put("topList", userList);
        } catch (ServiceBusinessException e) {
            modelMap.put("errorMsg", "Can't find any users in the application!");
            return RATING_VIEW;
        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
            return RATING_VIEW;
        }
        return RATING_VIEW;
    }

    @ModelAttribute("user")
    public User getUser() {
        return null;
    }
}
