package org.art.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    static final String REDIRECT_MAIN = "redirect:main";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String execute(HttpSession session) throws IOException, ServletException {
        session.invalidate();
        return REDIRECT_MAIN;
    }
}
