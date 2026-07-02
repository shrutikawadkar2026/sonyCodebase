package com.sony.core.servlets;

import com.sony.core.services.EmailService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

import org.apache.sling.servlets.annotations.SlingServletPaths;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/testmail")
public class MailServlet extends SlingAllMethodsServlet {

    @Reference
    private EmailService emailService;

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        emailService.sendEmail(
                email,
                "AEM Test Mail",
                "<h2>Hello from AEM</h2>"
        );

        response.getWriter().write("Mail Sent");
    }
}