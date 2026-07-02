package com.sony.core.servlets;

import com.sony.core.services.EmailService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/sendotp")
public class SendOtpServlet extends SlingAllMethodsServlet {

    @Reference
    private EmailService emailService;

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

            String email = request.getParameter("email");

            if (email == null || email.trim().isEmpty()) {

                response.getWriter().write(
                        "{\"status\":\"failure\",\"message\":\"Email is required\"}"
                );
                return;
            }

            int otp = 100000 + new Random().nextInt(900000);

            HttpSession session = request.getSession();

            session.setAttribute("email", email);
            session.setAttribute("otp", String.valueOf(otp));
            session.setAttribute("otpTime", System.currentTimeMillis());

            String body =
                    "<html><body>"
                            + "<h2>Email Verification</h2>"
                            + "<p>Your OTP is: <b>" + otp + "</b></p>"
                            + "<p>This OTP is valid for 5 minutes.</p>"
                            + "</body></html>";

            emailService.sendEmail(
                    email,
                    "OTP Verification",
                    body
            );

            response.getWriter().write(
                    "{\"status\":\"success\",\"message\":\"OTP sent successfully\"}"
            );

        } catch (Exception e) {

            response.getWriter().write(
                    "{\"status\":\"failure\",\"message\":\"Failed to send OTP\"}"
            );
        }
    }
}