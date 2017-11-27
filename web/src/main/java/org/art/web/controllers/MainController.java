package org.art.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/main")
@SessionAttributes("pageName")
public class MainController {

    static final String MAIN = "main";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String welcomePage(ModelMap modelMap) {
        modelMap.put("pageName", MAIN);
        return MAIN;
    }
}
