package com.sony.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import org.apache.sling.servlets.annotations.SlingServletResourceTypes;

import org.json.JSONArray;
import org.json.JSONObject;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/club",
                "sling.servlet.methods=GET"
        }
)
public class ClubServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        try {

            String club = request.getParameter("club");

            String cfPath = getCFPath(club);

            ResourceResolver resolver = request.getResourceResolver();

            Resource res = resolver.getResource(cfPath + "/jcr:content/data/master");


            JSONArray activities = null;
            if (res != null) {

                ValueMap vm = res.getValueMap();

                String raw = vm.get("activities", String.class);

                activities = new JSONArray();

                if (raw != null) {

                    raw = raw.replace("\n", "");

                    // split by </p>
                    String[] parts = raw.split("</p>");

                    for (String part : parts) {

                        part = part.replace("<p>", "").trim();

                        if (!part.isEmpty()) {
                            activities.put(part);
                        }
                    }
                }
            }

            JSONObject result = new JSONObject();
            result.put("activities", activities);

            response.getWriter().write(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"CF fetch failed\"}");
        }
    }

    // 🔥 CLUB → CF MAPPING
    private String getCFPath(String club) {

        if ("Sports Club".equals(club)) {
            return "/content/dam/core-components-examples/library/sports";
        }

        if ("Arts Club".equals(club)) {
            return "/content/dam/core-components-examples/library/club2";
        }

        if ("Musical Club".equals(club)) {
            return "/content/dam/core-components-examples/library/club3";
        }

        return "";
    }
}