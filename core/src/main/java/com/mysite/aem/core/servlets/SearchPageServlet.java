/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.mysite.aem.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.apache.sling.servlets.annotations.SlingServletPaths;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = { Servlet.class })

@SlingServletPaths("/bin/searchpath")

@ServiceDescription("Servlet to search the path")

public class SearchPageServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        
        ResourceResolver resolver = req.getResourceResolver();
        String searchPath = req.getParameter("path");
        
        // 1. Set Content Type
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 2. Initialize GSON and a JsonObject
        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();

        if (searchPath != null) {
            Resource targetResource = resolver.getResource(searchPath);
            
            if (targetResource != null) {
                responseJson.addProperty("status", "success");
                responseJson.addProperty("path", searchPath);
                responseJson.addProperty("resourceType", targetResource.getResourceType());
                
                Resource jcrContent = targetResource.getChild("jcr:content");
                if (jcrContent != null) {
                    String title = jcrContent.getValueMap().get("jcr:title", String.class);
                    responseJson.addProperty("pageTitle", title != null ? title : "No Title");
                } else {
                    responseJson.addProperty("pageTitle", "N/A (No jcr:content)");
                }
            } else {
                responseJson.addProperty("status", "error");
                responseJson.addProperty("message", "Path not found: " + searchPath);
            }
        } else {
            responseJson.addProperty("status", "error");
            responseJson.addProperty("message", "Missing 'path' parameter");
        }

        // 3. Write the JSON to the response
        resp.getWriter().write(gson.toJson(responseJson));
    }
}
