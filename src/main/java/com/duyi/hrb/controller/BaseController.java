package com.duyi.hrb.controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseController {


    public void writeJsonp(HttpServletResponse resp, String callback, String result) throws IOException {
        resp.getWriter().write(callback + "(" + result + ")");
    }
}
