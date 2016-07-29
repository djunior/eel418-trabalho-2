package com.djunior.eel418trabalho2;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.djunior.eel418trabalho2.BuscaResource.class);
        resources.add(com.djunior.eel418trabalho2.CatalogResource.class);
        resources.add(com.djunior.eel418trabalho2.UploadResource.class);
    }
    
}
