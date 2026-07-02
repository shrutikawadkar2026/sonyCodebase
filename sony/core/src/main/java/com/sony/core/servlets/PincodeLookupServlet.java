package com.sony.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

@Component(
        service = Servlet.class,
        property = {
                //"sling.servlet.resourceTypes=sony/components/pincodelookup",
                "sling.servlet.path=/bin/abc",
                "sling.servlet.methods=GET",
                "sling.servlet.extensions=json"
        }
)
public class PincodeLookupServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet( SlingHttpServletRequest request,  SlingHttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
        JSONObject obj = null;
        try {
            obj.put("a", "b");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write(obj.toString());

    }
}


