package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void init() {
        handlerMappings.add(new AnnotationHandlerMapping());
        handlerMappings.add(new ManualHandlerMapping());
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }
}
