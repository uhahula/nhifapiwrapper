package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.NhifApiClientFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class NhifClientContextListener implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(NhifClientContextListener.class.getName());

    public static final String CLIENT_ATTR = "nhifClient";
    public static final String CONFIG_ATTR = "nhifConfig";

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        ServletContext ctx = ev.getServletContext();
        // Read each key from OS env var first, then fall back to a JVM system
        // property with the same name. This lets GlassFish admins use
        //   asadmin create-jvm-options "-DNHIF_AUTH_URL=..."
        // instead of editing machine-level environment variables.
        Optional<NhifConfig> maybeConfig = NhifConfig.fromEnv(k -> {
            String v = System.getenv(k);
            return (v != null && !v.isEmpty()) ? v : System.getProperty(k);
        });
        if (maybeConfig.isEmpty()) {
            LOG.severe("NHIF_* not set (env vars or -D system properties) - /health will report unconfigured");
            return;
        }
        NhifConfig cfg = maybeConfig.get();
        ctx.setAttribute(CONFIG_ATTR, cfg);
        try {
            NhifApiClient client = NhifApiClientFactory.createClient(
                cfg.authUrl(), cfg.serviceUrl(),
                cfg.clientId(), cfg.clientSecret(), cfg.username());
            ctx.setAttribute(CLIENT_ATTR, client);
            LOG.info("NhifApiClient initialized for " + cfg.authUrl());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to build NhifApiClient", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        NhifApiClient c = (NhifApiClient) ev.getServletContext().getAttribute(CLIENT_ATTR);
        if (c != null) {
            try { c.close(); } catch (Exception ignored) {}
        }
    }
}
