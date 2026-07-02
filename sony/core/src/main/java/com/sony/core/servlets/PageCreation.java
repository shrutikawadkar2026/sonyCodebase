package com.sony.core.servlets;


import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/nodeDetails")
public class PageCreation extends SlingAllMethodsServlet {


    @Override
    protected void doGet( SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        // super.doGet(request, response);
        Resource root = request.getResourceResolver().getResource("/content/sony/us/en");
        if (root == null) {
            response.getWriter().write("Path not found");
            return;
        }
        printPages(root, response);
    }
    private void printPages(Resource resource, SlingHttpServletResponse response) throws IOException
    {

        Iterator<Resource> children= resource.listChildren();
        while(children.hasNext())
        {
            Resource page = children.next();
            Resource content = page.getChild("jcr:content");
            String title = "";
            if(content!=null)
            {
                String name=page.getName();
                String path=page.getPath();
                title=content.getValueMap().get("jcr:title", "No title");


                response.getWriter().write("\nName= "+name+"\n");
                response.getWriter().write("path="+path+"\n");
                response.getWriter().write("Title= "+title+"\n\n");
            }
            printPages(page,response);
        }

    }

    private static final String BASE_PATH = "/content/sony/us/en";
    private static final String NODE_TYPE = "nt:unstructured";



    @Override
    protected void doPost(SlingHttpServletRequest request,
                          SlingHttpServletResponse response) throws IOException {

        ResourceResolver resolver = request.getResourceResolver();

        try {

            Resource parent = resolver.getResource(BASE_PATH);

            if (parent == null) {
                response.getWriter().write("Parent path not found");
                return;
            }

            Map<String, Object> properties = new HashMap<>();
            properties.put("jcr:primaryType", NODE_TYPE);
            properties.put("title", "Demo Node");
            properties.put("type", "custom-node");

            Resource newNode = resolver.create(parent, "demoNode", properties);

            resolver.commit();

            response.getWriter().write("Node created at: " + newNode.getPath());

        } catch (Exception e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}