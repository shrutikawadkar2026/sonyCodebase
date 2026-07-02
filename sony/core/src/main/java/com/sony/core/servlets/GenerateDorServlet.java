//package com.sony.core.servlets;
//
//import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.servlets.SlingAllMethodsServlet;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//
//import javax.servlet.Servlet;
//import javax.servlet.ServletException;
//import java.io.IOException;
//
//@Component(
//        service = Servlet.class,
//        property = {
//                "sling.servlet.paths=/bin/generatedor",
//                "sling.servlet.methods=POST"
//        }
//)
//public class GenerateDorServlet extends SlingAllMethodsServlet {
//
//    @Reference
//    private DoRService doRService;
//
//    @Override
//    protected void doPost(SlingHttpServletRequest request,
//                          SlingHttpServletResponse response)
//            throws ServletException, IOException {
//
//        // Adaptive Form path
//        String formPath = request.getParameter("formPath");
//
//        // Submitted data (XML)
//        String data = request.getParameter("data");
//
//        Resource formResource =
//                request.getResourceResolver().getResource(formPath);
//
//        DoROptions options = new DoROptions();
//        options.setFormResource(formResource);
//        options.setData(data);
//
//        DoRResult result = doRService.render(options);
//
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition",
//                "attachment; filename=customer.pdf");
//
//        response.getOutputStream().write(result.getContent());
//    }
//}