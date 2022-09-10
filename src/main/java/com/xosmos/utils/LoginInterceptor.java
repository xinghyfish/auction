package com.xosmos.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String username = (String) request.getSession().getAttribute("customerName");
        String adminID = (String) request.getSession().getAttribute("adminID");
        String auctioneerName = (String) request.getSession().getAttribute("auctioneerName");

        if (username == null && adminID == null && auctioneerName == null) {
            request.getRequestDispatcher("/customer/login").forward(request, response);
            // not pass
            System.out.println("NO!");
            return false;
        }
        // pass
        else return true;
    }
}
