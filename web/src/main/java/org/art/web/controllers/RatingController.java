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

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Controller
@SessionAttributes("user")
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private UserService userService;

    static final String RATING = "rating";
    static final String LOGIN = "login/main";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String showRating(ModelMap modelMap,
                             @ModelAttribute("user") User user) throws IOException, ServletException {

        if (user == null || user.getLogin() == null) {
            return LOGIN;
        }

        try {
            List<User> userList = userService.getTopUsers(10);
            modelMap.put("topList", userList);
        } catch (ServiceBusinessException e) {
            modelMap.put("errorMsg", "Can't find any users in the application!");
            return RATING;
        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
            return RATING;
        }
        return RATING;
    }

    @ModelAttribute("user")
    public User getUser() {
        return null;
    }
}
