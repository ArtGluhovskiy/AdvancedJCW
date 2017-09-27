package org.art.web.command.impl;

import com.google.gson.Gson;
import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.services.impl.UserServiceImpl;
import org.art.web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CheckLoginController implements Controller {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        UserService userService = UserServiceImpl.getInstance();
        PrintWriter writer = resp.getWriter();
        String login = req.getParameter("login");
        System.out.println(login);
        try {
            User user = userService.getUserByLogin(login);
            System.out.println(user);
        } catch (ServiceBusinessException e) {
            //User with such login doesn't exist in the database. It's OK!
            writer.write(new Gson().toJson("OK"));
            return;
        } catch (ServiceSystemException e) {
            //NOP
        }
        writer.write(new Gson().toJson("FAIL"));
    }
}
