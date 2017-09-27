package org.art.web.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {

    String MAIN_PAGE = "/WEB-INF/view/layouts/default.jsp";
    String LOGIN_PAGE = "/login.jsp";
    String REGISTRATION_PAGE = "/registration.jsp";
    String STATISTICS_PAGE = "/statistics.jsp";
    String ERROR_PAGE = "/error.jsp";

    String SERVER_ERROR_MESSAGE = "We have some system problems on server! Please, try later.";

    void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;
}
