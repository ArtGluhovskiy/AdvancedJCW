package org.art.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.art.web.controllers.ControllerConstants.BATTLESHIP_VIEW;

@Controller
@RequestMapping("/battleship")
public class BattleShipController {

    @RequestMapping(method = RequestMethod.GET)
    public String battleShipPage() {
        return BATTLESHIP_VIEW;
    }
}
