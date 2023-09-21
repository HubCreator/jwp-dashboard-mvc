package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String NOT_FOUND_VIEW = "404.jsp";

    private HandlerMappings handlerMappings;
    private HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
    }

    public DispatcherServlet(HandlerMappings handlerMappings, HandlerExecutor handlerExecutor) {
        this.handlerMappings = handlerMappings;
        this.handlerExecutor = handlerExecutor;
    }

    @Override
    public void init() {
        handlerMappings = new HandlerMappings();
        handlerExecutor = new HandlerExecutor();
        handlerMappings.init();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            String viewName = findViewName(request, response);

            move(viewName, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private String findViewName(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = handlerMappings.getHandler(request);
        if (handler == null) {
            return NOT_FOUND_VIEW;
        }
        return handlerExecutor.execute(request, response, handler);
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}