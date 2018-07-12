package org.art.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import static org.art.web.controllers.ControllerConstants.MAIN_VIEW;

@Controller
@RequestMapping("/main")
@SessionAttributes("pageName")
public class MainController {

    @RequestMapping(method = RequestMethod.GET)
    public String welcomePage(ModelMap modelMap) {
        modelMap.put("pageName", MAIN_VIEW);
        return MAIN_VIEW;
    }
}
