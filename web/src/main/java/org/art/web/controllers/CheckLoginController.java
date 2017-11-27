package org.art.web.controllers;

import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.web.json.CheckLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.io.IOException;

@RestController
@RequestMapping("/checkLogin")
public class CheckLoginController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "", produces = "application/json")
    @ResponseBody
    public CheckLoginResponse checkLogin(@RequestParam("login") String login) throws IOException, ServletException {
        try {
            User user = userService.getUserByLogin(login);
        } catch (ServiceBusinessException e) {
            //User with such login doesn't exist in the database. It's OK!
            return new CheckLoginResponse("OK");
        } catch (ServiceSystemException e) {
            //NOP
        }
        return new CheckLoginResponse("FAIL");
    }
}
