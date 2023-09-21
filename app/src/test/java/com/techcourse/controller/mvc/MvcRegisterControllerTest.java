package com.techcourse.controller.mvc;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MvcRegisterControllerTest extends ControllerTest {

    private static final MvcRegisterController mvcRegisterController = new MvcRegisterController();

    @Nested
    class RegisterUser {
        @Test
        void getHandlerIfRegisterUser() throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/register");
            when(request.getMethod()).thenReturn("POST");
            Method registerUser = MvcRegisterController.class.newInstance().getClass()
                    .getDeclaredMethod("registerUser", HttpServletRequest.class, HttpServletResponse.class);

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

            // then
            assertThat(handlerExecution.getMethod()).isEqualTo(handlerExecution.getMethod()).isEqualTo(registerUser);
        }

        @Test
        void registerUser() {
            //given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getParameter("account")).thenReturn("moomin");
            when(request.getParameter("password")).thenReturn("password");
            when(request.getParameter("email")).thenReturn("moomin@gmail.com");

            //when
            ModelAndView modelAndView = mvcRegisterController.registerUser(request, null);

            //then
            assertAll(
                    () -> assertThat(InMemoryUserRepository.findByAccount("moomin")).isPresent(),
                    () -> assertThat(modelAndView.getView()).isEqualTo("redirect:/index.jsp")
            );
        }
    }

    @Nested
    class ViewRegister {
        @Test
        void getHandlerIfViewRegister() throws Exception {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn("/register");
            when(request.getMethod()).thenReturn("GET");
            Method viewRegister = MvcRegisterController.class.newInstance().getClass()
                    .getDeclaredMethod("viewRegister", HttpServletRequest.class, HttpServletResponse.class);

            // when
            HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

            // then
            assertThat(handlerExecution.getMethod()).isEqualTo(handlerExecution.getMethod()).isEqualTo(viewRegister);
        }

        @Test
        void viewRegister() {
            //given, when
            ModelAndView modelAndView = mvcRegisterController.viewRegister(null, null);

            //then
            assertThat(modelAndView.getView()).isEqualTo("/register.jsp");
        }
    }
}