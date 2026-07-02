package com.sony.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.Servlet;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "sony/components/channel",
        methods = HttpConstants.METHOD_GET,
        extensions = "json"
)
public class ChannelServlet extends SlingSafeMethodsServlet {

    private static final String JSON_DAM_PATH =
            "/content/dam/hdfc/channel.json";

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Resource jsonResource =
                request.getResourceResolver().getResource(JSON_DAM_PATH);

        if (jsonResource == null) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"JSON file not found\"}"
            );
            return;
        }

        Resource contentResource = jsonResource.getChild("jcr:content/renditions/original/jcr:content");

        if (contentResource == null) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Unable to read JSON file\"}"
            );
            return;
        }

        InputStream inputStream =
                contentResource.adaptTo(InputStream.class);

        if (inputStream == null) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"InputStream is null\"}"
            );
            return;
        }

        String jsonData =
                IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        response.getWriter().write(jsonData);
    }
}