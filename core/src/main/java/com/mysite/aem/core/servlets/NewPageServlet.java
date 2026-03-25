package com.mysite.aem.core.servlets;

// CHANGE THIS: Use the WCM API Page, not the Core Component Model
import com.day.cq.wcm.api.Page;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletPaths("/bin/latestpage")
public class NewPageServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(final SlingHttpServletRequest request, 
    		final SlingHttpServletResponse response) throws IOException {
    	
    	//Here the servlet is dealing with current context (main focus on currentpath or currentresource)
    	final Resource resource = request.getResource();
    	
    	response.setContentType("text/plain");
    	response.getWriter().write("Servlet by path example");
    	
    	
    }
 
}