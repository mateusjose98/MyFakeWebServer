package com.github.mateusjose98.http;

import com.github.mateusjose98.util.HttpMethod;

import java.util.HashMap;
import java.util.logging.Logger;

public class MyRequest {

    private HttpMethod httpMethod;
    private String resourcePath;
    private String fullPath;
    private HashMap<String, String> headers;
    private String body;
    private HashMap<String, String> queryParams;

    public static final Logger LOGGER = Logger.getLogger(MyRequest.class.getName());

    public MyRequest(String body, HttpMethod httpMethod, String fullPath) {
        LOGGER.info("Creating new request object ...");
        this.httpMethod = httpMethod;
        this.headers = new HashMap<>();
        this.body = body;
        this.fullPath = fullPath;
        buildResourcePath();
        buildQueryParams(fullPath.split("\\?").length > 1 ? fullPath.split("\\?")[1] : "");
    }

    public MyRequest() {
        this.headers = new HashMap<>();
        queryParams = new HashMap<>();
    }

    private void buildResourcePath() {
        String[] path = fullPath.split("\\?");
        this.resourcePath = path[0];
    }

    private void buildQueryParams(String queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return;
        }
        this.queryParams = new HashMap<>();
        String[] params = queryParams.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            this.queryParams.put(keyValue[0], keyValue[1]);
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HashMap<String, String> getQueryParams() {
        return queryParams;
    }

    public void addParameter(String key, String value) {
        this.queryParams.put(key, value);
    }

    public String getParameter(String key) {
        return this.queryParams.get(key);
    }

    @Override
    public String toString() {
        return "MyRequest{" +
                "httpMethod=" + httpMethod +
                ", resourcePath='" + resourcePath + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", queryParams=" + queryParams +
                '}';
    }
}
