package io.github.igrpweb.runner.spboot;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContextListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nosi.core.filter.AuthenticationFilter;
import nosi.core.filter.EncodingFilter;
import nosi.core.filter.ThreadLocalFilter;
import nosi.core.listener.BasicListener;
import nosi.core.servlet.IgrpServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 *
 * @author poolb
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    
    public static String uploadDirectory = System.getProperty("user.home") + "/igrp";
    
    @Bean
    public SimpleUrlHandlerMapping customFaviconHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE);
        mapping.setUrlMap(Collections.singletonMap(
          "/favicon.ico", faviconRequestHandler()));
        return mapping;
    }

    @Bean
    protected ResourceHttpRequestHandler faviconRequestHandler() {
        ResourceHttpRequestHandler requestHandler
          = new ResourceHttpRequestHandler();
        ClassPathResource classPathResource 
          = new ClassPathResource("/META-INF/resources/static/");
        List<Resource> locations = Arrays.asList(classPathResource);
        requestHandler.setLocations(locations);
        return requestHandler;
    }

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/jsp/");
        bean.setSuffix(".jsp");
        return bean;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(viewResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/images/IGRP/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/images/IGRP/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        
        registry.addResourceHandler("/images/IGRP/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/images/IGRP/",
                        //"classpath:/images/", 
                        //"/images/",
                        "file:" + uploadDirectory + "/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        
        //
        registry.addResourceHandler("/IGRP/images/IGRP/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/images/IGRP/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public FilterRegistrationBean<ThreadLocalFilter> threadLocalFilter() {
        FilterRegistrationBean<ThreadLocalFilter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ThreadLocalFilter());
        registrationBean.addUrlPatterns("/app/*");
        registrationBean.addUrlPatterns("/webapps/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<EncodingFilter> encodingFilter() {
        FilterRegistrationBean<EncodingFilter> registrationBean
                = new FilterRegistrationBean<>();
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("encoding", "UTF-8");
        registrationBean.setFilter(new EncodingFilter());
        registrationBean.setInitParameters(initParameters);
        registrationBean.addUrlPatterns("/app/*");
        registrationBean.addUrlPatterns("/webapps/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/app/*");
        registrationBean.addUrlPatterns("/webapps/*");
        registrationBean.setOrder(3);
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean igrpServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(
                new IgrpServlet(), "/app/*", "/webapps/*");

        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setFileSizeThreshold(DataSize.ofMegabytes(0));
        factory.setMaxFileSize(DataSize.ofMegabytes(50));
        factory.setMaxRequestSize(DataSize.ofMegabytes(50));

        MultipartConfigElement multipartConfig = factory.createMultipartConfig();
        bean.setMultipartConfig(multipartConfig);
        bean.setLoadOnStartup(1);
        return bean;
    }

    @Bean
    public ServletListenerRegistrationBean basicListnerBean() {
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(new BasicListener());
        return bean;
    }

}
