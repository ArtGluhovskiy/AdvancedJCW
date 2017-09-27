package org.art.web.command.impl;

import org.art.entities.DifficultyGroup;
import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceSystemException;
import org.art.services.impl.UserServiceImpl;
import org.art.web.auth.Encoder;
import org.art.web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

import static org.art.dao.util.DateTimeUtil.toSQLDate;
import static org.art.services.validators.UserValidator.*;

public class RegistrationController implements Controller {

    UserService userService = UserServiceImpl.getInstance();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String fName = req.getParameter("fname");
        String lName = req.getParameter("lname");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String clanName = req.getParameter("clan_name");
        String birth = req.getParameter("birth");

        String errorMsg = validateUserData(fName, lName, login, password, email, birth);
        if (errorMsg != null) {
            req.setAttribute("errorMsg", errorMsg);
            req.getRequestDispatcher(REGISTRATION_PAGE).forward(req, resp);
            return;
        }
        if ("".equals(clanName)) {
            clanName = "alone";
        }
        User user = new User(clanName, login, Encoder.encode(password), fName, lName, email, new Date(System.currentTimeMillis()),
                       "user", "ACTIVE", toSQLDate(birth), DifficultyGroup.BEGINNER.toString());
        try {
            //Increasing of rating by 1 after registration
            user.setRating(1);
            user = userService.save(user);
        } catch (ServiceSystemException e) {
            req.setAttribute("errorMsg", SERVER_ERROR_MESSAGE);
            req.getRequestDispatcher(REGISTRATION_PAGE).forward(req, resp);
            return;
        }
        String contextPath = req.getContextPath();
        req.getSession().setAttribute("user", user);
        resp.sendRedirect(contextPath + "/frontController?command=statistics");
        return;
    }
}
