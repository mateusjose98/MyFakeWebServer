package com.github.mateusjose98.util;

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
}
