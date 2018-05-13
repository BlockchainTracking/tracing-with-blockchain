package cn.edu.nju.software.ui.Filter;


import cn.edu.nju.software.ui.bizservice.impl.ServerCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Daniel
 * @since 2018/5/13 0:17
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/admin/*", filterName = "loginFilter")
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("login");
        if (obj == null)
            ((HttpServletResponse) servletResponse).sendRedirect("/login.html");
    }

    @Override
    public void destroy() {

    }
}
