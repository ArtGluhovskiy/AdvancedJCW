package org.art.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import static org.art.web.controllers.ControllerConstants.MAIN_VIEW;

@Controller
@RequestMapping("/main")
@SessionAttributes("pageName")
public class MainController {

    private static final Logger LOG = LogManager.getLogger(MainController.class);

    @GetMapping
    public String welcomePage(ModelMap modelMap) {
        LOG.debug("MainController: welcomePage(...)");
        modelMap.put("pageName", MAIN_VIEW);
        return MAIN_VIEW;
    }
}
