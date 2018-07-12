package org.art.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

import static org.art.web.controllers.ControllerConstants.REDIRECT_MAIN;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @RequestMapping(method = RequestMethod.GET)
    public String execute(HttpSession session) {
        session.invalidate();
        return REDIRECT_MAIN;
    }
}
