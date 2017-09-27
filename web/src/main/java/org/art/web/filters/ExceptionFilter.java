package org.art.web.filters;

import org.apache.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "encodingFilter", urlPatterns = "/*")
public class ExceptionFilter extends BaseFilter {

    public static final Logger log = Logger.getLogger(ExceptionFilter.class);
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, resp);
            System.out.println("Out of filter!");
        } catch (Exception e) {
            log.info("Exception in Exception filter", e);
            resp.sendRedirect(req.getContextPath() + "/frontController?command=main");
        }
    }
}
