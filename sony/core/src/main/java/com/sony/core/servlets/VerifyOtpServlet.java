package com.sony.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/verifyotp")
public class VerifyOtpServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws ServletException, IOException {


    }

    @Override
    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        try {

            String enteredOtp = request.getParameter("otp");

            HttpSession session = request.getSession();

            String storedOtp =
                    (String) session.getAttribute("otp");

            Long otpTime =
                    (Long) session.getAttribute("otpTime");

            if (storedOtp == null || otpTime == null) {

                response.getWriter().write(
                        "{\"status\":\"failure\",\"message\":\"OTP not found\"}"
                );
                return;
            }

            long currentTime = System.currentTimeMillis();

            if ((currentTime - otpTime) > 300000) {

                session.removeAttribute("otp");
                session.removeAttribute("otpTime");

                response.getWriter().write(
                        "{\"status\":\"expired\",\"message\":\"OTP expired\"}"
                );
                return;
            }

            if (storedOtp.equals(enteredOtp)) {

                session.removeAttribute("otp");
                session.removeAttribute("otpTime");

                response.getWriter().write(
                        "{\"status\":\"success\",\"message\":\"OTP verified successfully\"}"
                );

            } else {

                response.getWriter().write(
                        "{\"status\":\"failure\",\"message\":\"Invalid OTP\"}"
                );
            }

        } catch (Exception e) {

            response.getWriter().write(
                    "{\"status\":\"failure\",\"message\":\"Verification failed\"}"
            );
        }
    }
}