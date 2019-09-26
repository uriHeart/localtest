package com.argo.common.configuration;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Arrays;

public class ArgoServletRegistrationBean extends ServletRegistrationBean<DispatcherServlet> {

    public ArgoServletRegistrationBean(String name, Class<?> configClass, String... paths) {
        super();

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

        applicationContext.register(configClass);
        dispatcherServlet.setApplicationContext(applicationContext);

        this.setName(name);
        this.setServlet(dispatcherServlet);
        this.setUrlMappings(Arrays.asList(paths));
    }
}
