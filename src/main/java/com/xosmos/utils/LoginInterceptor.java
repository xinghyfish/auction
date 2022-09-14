package com.xosmos.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Integer customerID = (Integer) request.getSession().getAttribute("customerID");
        String adminID = (String) request.getSession().getAttribute("adminID");
        Integer auctioneerID = (Integer) request.getSession().getAttribute("auctioneerID");

        if (customerID == null && adminID == null && auctioneerID == null) {
            String domain = request.getRequestURI().split("/")[1];
            request.getRequestDispatcher("/" + domain + "/login").forward(request, response);
            // not pass
            System.err.println("refuse to visit cite [" + request.getRequestURL() + "]");
            return false;
        }
        // pass
        else return true;
    }
}
