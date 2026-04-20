package com.oau.nhif.jspexample.web;

import org.junit.jupiter.api.Test;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthServletTest {

    @Test
    void reportsUnconfiguredWhenNoClientAttribute() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        ServletContext ctx = mock(ServletContext.class);
        when(req.getServletContext()).thenReturn(ctx);
        when(ctx.getAttribute(NhifClientContextListener.CLIENT_ATTR)).thenReturn(null);
        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        new HealthServlet().doGet(req, resp);

        verify(resp).setStatus(503);
        assertTrue(sw.toString().contains("\"wrapperConfigured\":false"));
    }

    @Test
    void reportsConfiguredWithNonSecretValuesWhenClientPresent() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        ServletContext ctx = mock(ServletContext.class);
        when(req.getServletContext()).thenReturn(ctx);
        when(ctx.getAttribute(NhifClientContextListener.CLIENT_ATTR)).thenReturn(new Object());
        when(ctx.getAttribute(NhifClientContextListener.CONFIG_ATTR)).thenReturn(
            new NhifConfig("auth", "svc", "11014", "SECRET", "Mtundi"));
        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        new HealthServlet().doGet(req, resp);

        verify(resp).setStatus(200);
        String body = sw.toString();
        assertTrue(body.contains("\"wrapperConfigured\":true"));
        assertTrue(body.contains("\"clientId\":\"11014\""));
        assertFalse(body.contains("SECRET"));
        assertFalse(body.contains("Mtundi"));
    }
}
