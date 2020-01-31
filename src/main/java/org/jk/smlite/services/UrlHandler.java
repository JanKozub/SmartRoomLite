package org.jk.smlite.services;

import javax.servlet.http.HttpServletRequest;

public class UrlHandler {
    public static String getLastElementOfUrl(HttpServletRequest request) {
        String[] urlArray = request.getRequestURI().split("/");
        return urlArray[urlArray.length - 1];
    }
}
