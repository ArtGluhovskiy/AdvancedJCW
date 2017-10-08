package org.art.web.filters;

import org.art.entities.User;
import org.art.web.command.enums.CommandType;
import org.art.web.handlers.RequestHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.art.web.command.enums.CommandType.*;

@WebFilter(urlPatterns = "/frontController")
public class AuthorizationFilter extends BaseFilter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        CommandType type = RequestHandler.getCommand(req);

        if (ADMIN.equals(type)) {
            String contextPath = req.getContextPath();
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null || !user.getRole().equals("admin")) {
                resp.sendRedirect(contextPath + "/login.jsp");
                return;
            }
        }
        chain.doFilter(req, resp);
    }
}