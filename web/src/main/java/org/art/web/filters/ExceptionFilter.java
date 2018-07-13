package org.art.web.filters;

import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "Exception Filter",
        description = "Intercepts exceptions during the request processing and redirects request to the main page.",
        urlPatterns = "/*"
)
public class ExceptionFilter extends BaseFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException {
        log.debug("ExceptionFilter: doFilter()");
        try {
            chain.doFilter(req, resp);
        } catch (Exception e) {
            log.error("ExceptionFilter: exception has been caught! Redirect to the 'Main' page...", e);
            resp.sendRedirect(req.getContextPath() + "/main");
        }
    }
}
