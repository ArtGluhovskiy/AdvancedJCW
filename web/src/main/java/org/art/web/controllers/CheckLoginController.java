package org.art.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.web.json.model.CheckLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.art.web.controllers.ControllerConstants.RESPONSE_STATUS_FAIL;
import static org.art.web.controllers.ControllerConstants.RESPONSE_STATUS_SUCCESS;

@RestController
@RequestMapping("/checkLogin")
public class CheckLoginController {

    private static final Logger LOG = LogManager.getLogger(CheckLoginController.class);

    private final UserService userService;

    @Autowired
    public CheckLoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = "application/json")
    @ResponseBody
    public CheckLoginResponse checkLogin(@RequestParam("login") String login) {
        LOG.debug("CheckLoginController: checkLogin()");
        try {
            User user = userService.getUserByLogin(login);
        } catch (ServiceBusinessException e) {
            //User with such login doesn't exist in the database. It's OK!
            return new CheckLoginResponse(RESPONSE_STATUS_SUCCESS);
        } catch (ServiceSystemException e) {
            LOG.error("CheckLoginController: ServiceSystemException - cannot get user by login from DB! User login: {}", login, e);
        }
        return new CheckLoginResponse(RESPONSE_STATUS_FAIL);
    }
}
