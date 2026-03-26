package com.mysite.aem.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(service = { Servlet.class })
@SlingServletPaths("/bin/fetchSitePages")
public class FetchSitePagesServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String rootPath = req.getParameter("path");
        ResourceResolver resolver = req.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        
        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        JsonArray pagesArray = new JsonArray();

        if (rootPath != null && pageManager != null) {
            Page rootPage = pageManager.getPage(rootPath);
            
            if (rootPage != null) {
                // Start recursive collection
                collectPages(rootPage, pagesArray);
                
                responseJson.addProperty("status", "success");
                responseJson.addProperty("totalFound", pagesArray.size());
                responseJson.add("pages", pagesArray);
            } else {
                responseJson.addProperty("status", "error");
                responseJson.addProperty("message", "Root path is not a valid AEM Page");
            }
        } else {
            responseJson.addProperty("status", "error");
            responseJson.addProperty("message", "Missing path parameter");
        }

        resp.getWriter().write(gson.toJson(responseJson));
    }

    /**
     * Recursive method to traverse the page tree
     */
    private void collectPages(Page parentPage, JsonArray jsonArray) {
        Iterator<Page> children = parentPage.listChildren();
        while (children.hasNext()) {
            Page child = children.next();
            
            JsonObject pageData = new JsonObject();
            pageData.addProperty("title", child.getTitle() != null ? child.getTitle() : child.getName());
            pageData.addProperty("path", child.getPath());
            pageData.addProperty("template", child.getTemplate() != null ? child.getTemplate().getPath() : "N/A");
            
            jsonArray.add(pageData);

            // Recursively call for the next level
            collectPages(child, jsonArray);
        }
    }
}