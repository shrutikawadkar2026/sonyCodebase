package com.sony.core.servlets;



import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/pagedetails")
public class HelloServlet extends SlingAllMethodsServlet {



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

    private static final String PARAM_NAME = "name";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_PARENT_PATH = "parentPath";
    private static final String PARAM_TEMPLATE_PATH = "templatePath";

    @Override
    protected void doPost( SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        //super.doPost(request, response);

        String name=request.getParameter(PARAM_NAME);
        String title=request.getParameter(PARAM_TITLE);
      //  String parentPath="/content/sony/us/en";
        String parentPath=request.getParameter(PARAM_PARENT_PATH);
        String templatePath=request.getParameter(PARAM_TEMPLATE_PATH);
        ResourceResolver resolver=request.getResourceResolver();
        try{
            PageManager  pageManager=resolver.adaptTo(PageManager.class);
            if (pageManager==null)
            {
                response.getWriter().write("Page Manager not available");
                return;
            }
          //  String templatePath="/conf/sony/settings/wcm/templates/page-content";
            Page page=pageManager.create(parentPath, name, templatePath, title);

            resolver.commit();
            response.getWriter().write("Page created successfully");


        }
        catch(Exception e)
        {
            response.getWriter().write(("error occurred "+e.getMessage()));
        }

    }
}
