package com.mysite.aem.core.models;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, 
       adapters = ContactCardsModel.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactCardsModel {

    private static final Logger LOG = LoggerFactory.getLogger(ContactCardsModel.class);

    @SlingObject
    private ResourceResolver resourceResolver;

    // Use Object to prevent injection failure if the JCR stores a single string vs an array
    @ValueMapValue(name = "contactFragmentPaths")
    private Object contactPathsObject;

    private List<Contact> contacts = new ArrayList<>();

    @PostConstruct
    protected void init() {
        String[] paths = getPathsAsArray();
        
        if (paths != null && resourceResolver != null) {
            for (String path : paths) {
                Resource fragmentResource = resourceResolver.getResource(path);
                if (fragmentResource != null) {
                    ContentFragment fragment = fragmentResource.adaptTo(ContentFragment.class);
                    if (fragment != null) {
                        contacts.add(new Contact(
                            getElementValue(fragment, "name"),         
                            getElementValue(fragment, "role"),         
                            getElementValue(fragment, "profileimage"), 
                            getElementValue(fragment, "gmail"),        
                            getElementValue(fragment, "phone"),
                            getElementValue(fragment, "country")
                        ));
                    }
                }
            }
        }
    }

    /**
     * Helper to safely convert the multifield object to a String array.
     */
    private String[] getPathsAsArray() {
        if (contactPathsObject instanceof String[]) {
            return (String[]) contactPathsObject;
        } else if (contactPathsObject instanceof String) {
            return new String[]{(String) contactPathsObject};
        }
        return null;
    }

    private String getElementValue(ContentFragment fragment, String elementName) {
        return Optional.ofNullable(fragment.getElement(elementName))
                .map(ContentElement::getContent)
                .orElse("");
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public static class Contact {
        private final String name;
        private final String role;
        private final String profileimage;
        private final String gmail;
        private final String phone;
        private final String country;

        public Contact(String name, String role, String profileimage, String gmail, String phone, String country) {
            this.name = name;
            this.role = role;
            this.profileimage = profileimage;
            this.gmail = gmail;
            this.phone = phone;
            this.country = country;
        }

        public String getName() { return name; }
        public String getRole() { return role; }
        public String getProfileimage() { return profileimage; }
        public String getGmail() { return gmail; }
        public String getPhone() { return phone; }
        public String getCountry() { return country; }
    }
}