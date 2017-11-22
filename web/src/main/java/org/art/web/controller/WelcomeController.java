package org.art.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcomePage(ModelMap model) {
        model.put("greeting", "Hello person");
        model.put("hello", "heellllo");
        return "welcome";
    }
}
