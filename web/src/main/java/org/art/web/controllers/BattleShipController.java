package org.art.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/battleship")
public class BattleShipController {

    static final String BATTLESHIP = "battleship";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String battleShipPage() {
        return BATTLESHIP;
    }
}
