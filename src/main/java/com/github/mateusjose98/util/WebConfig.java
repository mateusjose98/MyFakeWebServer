package com.github.mateusjose98.util;

import com.github.mateusjose98.http.Controller;

import java.util.HashMap;

public class WebConfig {
    public static final String WEB_ROOT = "src/main/resources/web";
    public static HashMap<String, String> MIME_TYPES = new HashMap<String, String>() {{
        put("html", "text/html");
        put("css", "text/css");
        put("js", "text/javascript");
        put("png", "image/png");
        put("jpg", "image/jpeg");
        put("jpeg", "image/jpeg");
        put("gif", "image/gif");
        put("ico", "image/x-icon");
    }};

    public static HashMap<Integer, String> STATUS_CODES = new HashMap<Integer, String>() {{
        put(200, "OK");
        put(404, "Not Found");
        put(500, "Internal Server Error");
    }};

    public static HashMap<String, Class> ROUTES = new HashMap<String, Class>() {{
        put("/app", Controller.class);
    }};
}
