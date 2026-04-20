package com.oau.nhif.morfin.agent;

import com.oau.nhif.morfin.agent.device.MorfinDevice;
import com.oau.nhif.morfin.agent.device.MorfinDeviceImpl;
import com.oau.nhif.morfin.agent.http.CaptureHandler;
import com.oau.nhif.morfin.agent.http.CorsFilter;
import com.oau.nhif.morfin.agent.http.ShutdownHandler;
import com.oau.nhif.morfin.agent.http.StatusHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        int port = 8765;
        for (int i = 0; i < args.length - 1; i++) {
            if ("--port".equals(args[i])) port = Integer.parseInt(args[i + 1]);
        }

        MorfinDevice device = new MorfinDeviceImpl();

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        CorsFilter cors = new CorsFilter();

        AtomicReference<HttpServer> serverRef = new AtomicReference<>(server);

        HttpContext c1 = server.createContext("/status", new StatusHandler(device));
        c1.getFilters().add(cors);
        HttpContext c2 = server.createContext("/capture", new CaptureHandler(device));
        c2.getFilters().add(cors);
        HttpContext c3 = server.createContext("/shutdown", new ShutdownHandler(() -> {
            HttpServer s = serverRef.get();
            if (s != null) s.stop(0);
            device.close();
        }));
        c3.getFilters().add(cors);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("shutdown hook: closing device");
            device.close();
        }, "morfin-agent-shutdown"));

        server.start();
        LOG.info("Morfin agent listening on http://127.0.0.1:" + port);
    }
}
