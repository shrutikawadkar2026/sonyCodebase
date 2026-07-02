package com.sony.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.json.JSONException;
import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "sony/components/pincode",
        methods = HttpConstants.METHOD_GET,
        extensions = "json"
)
public class HdfcPersonalDetailPincode extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws IOException {


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pincode = request.getParameter("pincode");

        JSONObject json = new JSONObject();

        if (pincode == null || pincode.isEmpty()) {

            try {
                json.put("success", false);

                json.put("message", "Pincode is required");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        } else {
            try{

            switch (pincode) {

                case "440001":

                        json.put("success", true);

                    json.put("pincode", pincode);
                    json.put("city", "Nagpur");
                    json.put("state", "Maharashtra");
                    break;

                case "411001":
                    json.put("success", true);
                    json.put("pincode", pincode);
                    json.put("city", "Pune");
                    json.put("state", "Maharashtra");
                    break;

                case "400001":
                    json.put("success", true);
                    json.put("pincode", pincode);
                    json.put("city", "Mumbai");
                    json.put("state", "Maharashtra");
                    break;

                case "560001":
                    json.put("success", true);
                    json.put("pincode", pincode);
                    json.put("city", "Bengaluru");
                    json.put("state", "Karnataka");
                    break;

                default:
                    json.put("success", false);
                    json.put("message", "Pincode not found");
            }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        response.getWriter().write(json.toString());
    }
}