package org.art.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

import static org.art.web.controllers.ControllerConstants.REDIRECT_MAIN_VIEW;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private static final Logger LOG = LogManager.getLogger(LogoutController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String logout(HttpSession session) {
        LOG.debug("LogoutController: logout()");
        session.invalidate();
        return REDIRECT_MAIN_VIEW;
    }
}
