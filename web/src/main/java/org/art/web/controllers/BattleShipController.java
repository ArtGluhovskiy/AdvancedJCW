package org.art.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.art.web.controllers.ControllerConstants.BATTLESHIP_VIEW;

@Controller
@RequestMapping("/battleship")
public class BattleShipController {

    private static final Logger LOG = LogManager.getLogger(BattleShipController.class);

    @GetMapping
    public String battleShipPage() {
        LOG.debug("BattleShipController: battleShipPage()");
        return BATTLESHIP_VIEW;
    }
}
