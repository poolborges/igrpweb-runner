package io.github.igrpweb.runner.quarkus;

import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.api.ServletInfo;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.FileSystemAccess;
import io.vertx.ext.web.handler.StaticHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import nosi.core.filter.AuthenticationFilter;
import nosi.core.filter.EncodingFilter;
import nosi.core.filter.ThreadLocalFilter;
import nosi.core.listener.BasicListener;
import nosi.core.servlet.IgrpServlet;

@ApplicationScoped
public class QuarkusWebConfig implements ServletExtension {

    public static String uploadDirectory = System.getProperty("user.home") + "/igrp";

    @Override
    public void handleDeployment(DeploymentInfo deploymentInfo, ServletContext servletContext) {
        String encodingFilterName = findOrAddFilter(deploymentInfo, "EncodingFilter", EncodingFilter.class);
        deploymentInfo.getFilters().get(encodingFilterName).addInitParam("encoding", "UTF-8");
        deploymentInfo.addFilterUrlMapping(encodingFilterName, "/app/*", DispatcherType.REQUEST);
        deploymentInfo.addFilterUrlMapping(encodingFilterName, "/webapps/*", DispatcherType.REQUEST);

        String threadLocalFilterName = findOrAddFilter(deploymentInfo, "ThreadLocalFilter", ThreadLocalFilter.class);
        deploymentInfo.addFilterUrlMapping(threadLocalFilterName, "/app/*", DispatcherType.REQUEST);
        deploymentInfo.addFilterUrlMapping(threadLocalFilterName, "/webapps/*", DispatcherType.REQUEST);

        String authFilterName = findOrAddFilter(deploymentInfo, "AuthenticationFilter", AuthenticationFilter.class);
        deploymentInfo.addFilterUrlMapping(authFilterName, "/app/*", DispatcherType.REQUEST);
        deploymentInfo.addFilterUrlMapping(authFilterName, "/webapps/*", DispatcherType.REQUEST);

        ServletInfo existingIgrpServlet = deploymentInfo.getServlets().values().stream()
                .filter(s -> IgrpServlet.class.equals(s.getServletClass()))
                .findFirst()
                .orElse(null);

        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                null,
                50L * 1024 * 1024,
                50L * 1024 * 1024,
                0
        );

        if (existingIgrpServlet != null) {
            if (!existingIgrpServlet.getMappings().contains("/app/*")) {
                existingIgrpServlet.addMapping("/app/*");
            }
            if (!existingIgrpServlet.getMappings().contains("/webapps/*")) {
                existingIgrpServlet.addMapping("/webapps/*");
            }
            existingIgrpServlet.setLoadOnStartup(1)
                    .setMultipartConfig(multipartConfig);
        } else {
            ServletInfo igrpServlet = Servlets.servlet("IGRP", IgrpServlet.class)
                    .addMappings("/app/*", "/webapps/*")
                    .setLoadOnStartup(1)
                    .setMultipartConfig(multipartConfig);
            deploymentInfo.addServlet(igrpServlet);
        }

        boolean hasBasicListener = deploymentInfo.getListeners().stream()
                .anyMatch(l -> BasicListener.class.equals(l.getListenerClass()));
        if (!hasBasicListener) {
            deploymentInfo.addListener(new ListenerInfo(BasicListener.class));
        }
    }

    private String findOrAddFilter(DeploymentInfo deploymentInfo, String defaultName, Class<? extends jakarta.servlet.Filter> filterClass) {
        FilterInfo existing = deploymentInfo.getFilters().values().stream()
                .filter(f -> filterClass.equals(f.getFilterClass()))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            return existing.getName();
        }
        FilterInfo filterInfo = Servlets.filter(defaultName, filterClass);
        deploymentInfo.addFilter(filterInfo);
        return defaultName;
    }

    public void addResourceHandlers(@Observes Router router) {
        router.get("/").handler(rc -> rc.redirect("/app?from=RedirectorQuarkus"));

        StaticHandler igrpClasspathHandler = StaticHandler.create("META-INF/resources/images/IGRP")
                .setCachingEnabled(true)
                .setMaxAgeSeconds(3600);

        StaticHandler igrpUploadHandler = StaticHandler.create(FileSystemAccess.ROOT, uploadDirectory)
                .setCachingEnabled(true)
                .setMaxAgeSeconds(3600);

        router.route("/resources/images/IGRP/*").handler(igrpClasspathHandler);
        router.route("/images/images/IGRP/*").handler(igrpClasspathHandler);
        router.route("/IGRP/images/IGRP/*").handler(igrpClasspathHandler);
        router.route("/images/IGRP/*").handler(igrpClasspathHandler);
        router.route("/images/IGRP/*").handler(igrpUploadHandler);
    }
}
