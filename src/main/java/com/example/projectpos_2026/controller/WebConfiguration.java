package com.example.projectpos_2026.controller;

import jakarta.servlet.http.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.config.annotation.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                HttpSession session = request.getSession();
                String csrf = (String) session.getAttribute("csrf");
                if (csrf == null) { csrf = UUID.randomUUID().toString(); session.setAttribute("csrf", csrf); }
                request.setAttribute("csrf", csrf);
                if ("POST".equals(request.getMethod())) {
                    String supplied = request.getParameter("_csrf");
                    if (supplied == null || !MessageDigest.isEqual(csrf.getBytes(StandardCharsets.UTF_8), supplied.getBytes(StandardCharsets.UTF_8))) {
                        response.sendError(403, "Your session expired. Reload the page and try again."); return false;
                    }
                }
                return true;
            }
        });
    }
}
