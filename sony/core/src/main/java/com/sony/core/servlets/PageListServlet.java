package com.sony.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/pagelist",
                "sling.servlet.methods=GET"
        }
)
public class PageListServlet extends SlingSafeMethodsServlet {

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(
            SlingHttpServletRequest request,
            SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        ResourceResolver resolver =
                request.getResourceResolver();

        Session session =
                resolver.adaptTo(Session.class);

        Map<String, String> map =
                new HashMap<>();

        map.put("path", "/content/we-retail/us/en");
        map.put("type", "cq:Page");


        Query query = queryBuilder.createQuery(
                PredicateGroup.create(map),
                session);

        SearchResult result = query.getResult();

        StringBuilder json =
                new StringBuilder("[");

        boolean first = true;

        try {

            for (Hit hit : result.getHits()) {

                Resource resource =
                        resolver.getResource(hit.getPath());

                if (resource == null) {
                    continue;
                }

                Resource contentResource =
                        resource.getChild("jcr:content");

                if (contentResource == null) {
                    continue;
                }

                ValueMap vm =
                        contentResource.getValueMap();

                String title =
                        vm.get("jcr:title", "");

                if (!first) {
                    json.append(",");
                }

                json.append("{")
                        .append("\"title\":\"")
                        .append(title)
                        .append("\",")
                        .append("\"path\":\"")
                        .append(hit.getPath())
                        .append("\"")
                        .append("}");

                first = false;
            }

            json.append("]");

            response.getWriter().write(json.toString());

        } catch (Exception e) {

            response.getWriter().write(
                    "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}