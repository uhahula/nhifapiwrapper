# NHIF TZ Wrapper - JSP Example

Reception-desk JSP webapp that authorizes NHIF cards using a fingerprint
captured on the client PC via the Morfin Agent sibling module.

## Architecture

```
Browser  --->  https://nhif.hospital.local/nhiftz-wrapper-jsp-example/
   |
   +-- fetch -->  http://127.0.0.1:8765  (local Morfin agent on the same PC)
```

The WAR is stateless. The local agent must be running on the
receptionist's PC; see `../nhiftz-wrapper-morfin-agent/README.md`.

## Configuration (environment variables)

Set on the Tomcat host (e.g. in `/etc/sysconfig/tomcat` on CentOS):

```bash
NHIF_AUTH_URL=https://test.nhif.or.tz
NHIF_SERVICE_URL=https://test.nhif.or.tz/servicehub
NHIF_CLIENT_ID=11014
NHIF_CLIENT_SECRET=ntbzRGbrwwHj8Jwd7bbPsg==
NHIF_USERNAME=Mtundi
```

Restart Tomcat after changing them.

## Build

```bash
cd nhiftz-wrapper-jsp-example
mvn package
# produces target/nhiftz-wrapper-jsp-example.war
```

## Deploy

1. Ensure `nhiftz-wrapper-1.4.0.jar` is installed to the same Maven repo
   the WAR was built against. Tomcat needs no extra JARs - all deps are in
   `WEB-INF/lib/`.
2. Copy the WAR to `$CATALINA_HOME/webapps/`.
3. Tomcat 8.5.87 auto-expands it. Browse to
   `https://your-server/nhiftz-wrapper-jsp-example/`.

## Endpoints

- `GET  /`           - reception form (rendered by `IndexServlet` + `index.jsp`)
- `POST /authorize`  - processes the form, calls NHIF, forwards to `result.jsp`
- `GET  /health`     - JSON status: `{wrapperConfigured, authUrl, serviceUrl, clientId}`

## Security

- Run Tomcat behind HTTPS in production.
- `NHIF_CLIENT_SECRET` must not be logged; only the webapp should read it.
- The fingerprint template (`imageData`) is never logged.
