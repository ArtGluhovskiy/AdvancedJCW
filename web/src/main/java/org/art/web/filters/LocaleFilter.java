package org.art.web.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/frontController")
public class LocaleFilter extends BaseFilter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String locale = req.getParameter("locale");
        if (locale != null && !locale.isEmpty()) {
            req.getSession().setAttribute("locale", locale);
        } else {
            req.getSession().setAttribute("locale", "en");
        }
        chain.doFilter(req, resp);
    }
}
