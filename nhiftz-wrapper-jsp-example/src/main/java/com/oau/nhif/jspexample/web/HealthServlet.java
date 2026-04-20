package com.oau.nhif.jspexample.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/health")
public class HealthServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object client = req.getServletContext().getAttribute(NhifClientContextListener.CLIENT_ATTR);
        NhifConfig cfg = (NhifConfig) req.getServletContext().getAttribute(NhifClientContextListener.CONFIG_ATTR);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        if (client == null) {
            resp.setStatus(503);
            out.print("{\"wrapperConfigured\":false,\"message\":\"Check NHIF_* env vars\"}");
            return;
        }
        resp.setStatus(200);
        out.print("{\"wrapperConfigured\":true"
            + ",\"authUrl\":\""   + esc(cfg.authUrl())   + "\""
            + ",\"serviceUrl\":\"" + esc(cfg.serviceUrl()) + "\""
            + ",\"clientId\":\""  + esc(cfg.clientId())  + "\""
            + "}");
    }

    private static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
