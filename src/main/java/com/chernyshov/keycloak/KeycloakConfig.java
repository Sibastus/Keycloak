package com.chernyshov.keycloak;

import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters;
import org.keycloak.services.filters.KeycloakSessionServletFilter;
import org.keycloak.services.listeners.KeycloakSessionDestroyListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.*;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;

@Configuration
public class KeycloakConfig {

    @Bean
    ServletRegistrationBean keycloakJaxRsApplication(KeycloakServerProperties keycloakServerProperties, DataSource dataSource) throws Exception {

        mockJndiEnvironmet(dataSource);

        EmbeddedKeycloakApplication.keycloakServerProperties = keycloakServerProperties;

        ServletRegistrationBean servlet = new ServletRegistrationBean(new HttpServlet30Dispatcher());
        servlet.addInitParameter("javax.ws.rs.Application", EmbeddedKeycloakApplication.class.getName());
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_SERVLET_MAPPING_PREFIX, keycloakServerProperties.getContextPath());
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_USE_CONTAINER_FORM_PARAMS, "true");
        servlet.addUrlMappings(keycloakServerProperties.getContextPath() + "/*");
        servlet.setLoadOnStartup(1);
        servlet.setAsyncSupported(true);

        return servlet;
    }

    @Bean
    ServletListenerRegistrationBean<KeycloakSessionDestroyListener> keycloakSessionDestroyListener() {
        return new ServletListenerRegistrationBean<>(new KeycloakSessionDestroyListener());
    }

    @Bean
    FilterRegistrationBean keycloakSessionManagement(KeycloakServerProperties keycloakServerProperties) {

        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setName("Keycloak Session Management");
        filter.setFilter(new KeycloakSessionServletFilter());
        filter.addUrlPatterns(keycloakServerProperties.getContextPath() + "/*");

        return filter;
    }



    private void mockJndiEnvironmet(DataSource dataSource) throws NamingException {
        NamingManager.setInitialContextFactoryBuilder((env) -> (environment) -> new InitialContext() {

            @Override
            public Object lookup(Name name) throws NamingException {
                return lookup(name.toString());
            }

            @Override
            public Object lookup(String name) throws NamingException {
                if ("spring/datasource".equals(name)) {
                    return dataSource;
                }
                return null;
            }

            @Override
            public NameParser getNameParser(String name) throws NamingException {
                return CompositeName::new;
            }

            @Override
            public void close() throws NamingException {
                super.close();
            }
        });
    }

}
