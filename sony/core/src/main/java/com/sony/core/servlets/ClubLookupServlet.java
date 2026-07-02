package com.sony.core.servlets;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.resourceTypes=sony/components/club",
                "sling.servlet.methods=GET",
                "sling.servlet.extensions=json"
        }
)
public class ClubLookupServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws IOException {

        String selectedClub = request.getParameter("club");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            URL url = new URL(
                    "https://api.jsonbin.io/v3/qs/6a1e6cb5da38895dfe79f273"
            );

            HttpsURLConnection connection =
                    (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()
                            )
                    );

            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();

            JSONObject rootObject =
                    new JSONObject(result.toString());

            JSONObject recordObject =
                    rootObject.getJSONObject("record");

            JSONArray clubs =
                    recordObject.getJSONArray("clubs");

            JSONObject responseObject =
                    new JSONObject();

            boolean found = false;

            for (int i = 0; i < clubs.length(); i++) {

                JSONObject club =
                        clubs.getJSONObject(i);

                if (selectedClub.equals(
                        club.getString("club"))) {

                    responseObject.put(
                            "activities",
                            club.getJSONArray("activities")
                    );

                    found = true;
                    break;
                }
            }

            if (!found) {

                responseObject.put(
                        "message",
                        "Club not found"
                );

                responseObject.put(
                        "activities",
                        new JSONArray()
                );
            }

            response.getWriter()
                    .write(responseObject.toString());

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().write(
                    "{\"error\":\"Something went wrong\"}"
            );
        }
    }
}