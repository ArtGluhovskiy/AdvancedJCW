package org.art.web.filters;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.art.web.filters.EncodingFilter.DEFAULT_ENCODING;

@WebFilter(
        filterName = "Encoding Filter",
        description = "Adds content type and charset headers to the response.",
        urlPatterns = "/*",
        initParams = @WebInitParam(name = "encoding", value = DEFAULT_ENCODING)
)
public class EncodingFilter extends BaseFilter {

    public static final String DEFAULT_ENCODING = "UTF-8";

    private String encoding = DEFAULT_ENCODING;

    @Override
    public void init(FilterConfig config) {
        log.debug("EncodingFilter: init()");
        String encodingParam = config.getInitParameter("encoding");
        if (StringUtils.isNotBlank(encodingParam)) {
            encoding = encodingParam;
        }
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        log.debug("EncodingFilter: doFilter()");
        req.setCharacterEncoding(encoding);
        resp.setContentType("text/html; charset=" + encoding);
        resp.setCharacterEncoding(encoding);
        resp.setHeader("Content-Language", "UTF-8");
        chain.doFilter(req, resp);
    }
}
