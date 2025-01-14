package com.app.global.adapter;

import java.util.Enumeration;

public class FilterConfigAdapter implements javax.servlet.FilterConfig {

    private final jakarta.servlet.FilterConfig jakartaFilterConfig;

    public FilterConfigAdapter(jakarta.servlet.FilterConfig jakartaFilterConfig) {
        this.jakartaFilterConfig = jakartaFilterConfig;
    }

    @Override
    public String getFilterName() {
        return jakartaFilterConfig.getFilterName();
    }

    @Override
    public javax.servlet.ServletContext getServletContext() {
        return new ServletContextAdapter(jakartaFilterConfig.getServletContext());
    }

    @Override
    public String getInitParameter(String name) {
        return jakartaFilterConfig.getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return jakartaFilterConfig.getInitParameterNames();
    }
}
