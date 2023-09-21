package com.techcourse.controller.mvc;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;

@Controller
public class MvcForwardController {

    private static final Logger log = LoggerFactory.getLogger(MvcForwardController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView viewIndex(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView("/index.jsp");
    }
}