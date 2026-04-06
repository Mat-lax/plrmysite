package com.mysite.aem.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Model(
    adaptables = SlingHttpServletRequest.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ArticleBreadcrumbModel {

    // This is the "magic" fix. It always grabs the page from the URL context,
    // even if this component is rendered inside an Experience Fragment.
    @ScriptVariable
    private Page currentPage;

    private List<Page> breadcrumbs;

    @PostConstruct
    protected void init() {
        breadcrumbs = new ArrayList<>();

        // No need for complex Optional chaining here! 
        // AEM gives us the correct currentPage object via @ScriptVariable.
        if (currentPage != null) {
            Page tempPage = currentPage;
            
            while (tempPage != null) {
                // Logic check: Ensure we don't include the 'root' or 
                // non-content nodes if your site structure requires it.
                breadcrumbs.add(tempPage);
                tempPage = tempPage.getParent();
            }
            
            // Reverse so it goes: Home > Category > Article
            Collections.reverse(breadcrumbs);
        }
    }

    public List<Page> getBreadcrumbs() {
        return breadcrumbs;
    }
}