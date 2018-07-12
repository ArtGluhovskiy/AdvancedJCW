package org.art.web.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This abstract class just overrides methods of Filter
 * interface providing more convenient usage after its
 * extending by different Filter implementations.
 */
public abstract class BaseFilter implements Filter {

    protected Logger log = LogManager.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) {
        //Empty implementation (should be overridden if needed)
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    protected abstract void doFilter(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                                     FilterChain filterChain) throws IOException, ServletException;

    @Override
    public void destroy() {
        //Empty implementation (should be overridden if needed)
    }
}
