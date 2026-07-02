package com.sony.core.servlets;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.Query;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.result.SearchResult;
import com.day.cq.search.result.Hit;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.servlet.Servlet;
import javax.jcr.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=/bin/cfSearch",
        "sling.servlet.methods=GET"
})
public class DemoServlet extends SlingSafeMethodsServlet {

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String keyword = request.getParameter("keyword"); // Query param for keyword
        String modelPath = request.getParameter("model"); // Optional: Filter by CF Model

        Map<String, String> map = new HashMap<>();
        map.put("path", "/content/dam/products");
        map.put("type", "dam:Asset");

        // Predicates specifically for Content Fragments
        map.put("1_property", "jcr:content/contentFragment");
        map.put("1_property.value", "true");

        // Full-text search for the keyword
        if (keyword != null && !keyword.isEmpty()) {
            map.put("fulltext", keyword);
            // Optional: specify relative path to search within master variation data
            map.put("fulltext.relPath", "jcr:content/data/master");
        }

        // Optional: Filter by specific Content Fragment Model
        if (modelPath != null && !modelPath.isEmpty()) {
            map.put("2_property", "jcr:content/data/cq:model");
            map.put("2_property.value", modelPath);
        }

        Session session = request.getResourceResolver().adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();

        // Build JSON output
        JsonObject jsonResponse = new JsonObject();
        JsonArray items = new JsonArray();

        for (Hit hit : result.getHits()) {
            try {
                JsonObject item = new JsonObject();
                item.addProperty("path", hit.getPath());
                item.addProperty("title", hit.getTitle());
                items.add(item);
            } catch (Exception ignored) {}
        }

        jsonResponse.add("results", items);
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(jsonResponse));
    }
}
