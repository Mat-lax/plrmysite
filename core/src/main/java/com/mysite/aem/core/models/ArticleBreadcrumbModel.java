package com.mysite.aem.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Model(adaptables = SlingHttpServletRequest.class)
public class ArticleBreadcrumbModel {

    @SlingObject
    private SlingHttpServletRequest request;

    private List<Page> breadcrumbs;

    @PostConstruct
    protected void init() {
        breadcrumbs = new ArrayList<>();

        Page currentPage = Optional.ofNullable(request)
                .map(req -> req.getResourceResolver().adaptTo(PageManager.class))
                .flatMap(pageManager -> Optional.ofNullable(pageManager.getContainingPage(request.getResource())))
                .orElse(null);

        if (currentPage != null) {
            Page tempPage = currentPage;
            while (tempPage != null) {
                breadcrumbs.add(tempPage);
                tempPage = tempPage.getParent();
            }
            Collections.reverse(breadcrumbs);
        }
    }

    public List<Page> getBreadcrumbs() {
        return breadcrumbs;
    }
}