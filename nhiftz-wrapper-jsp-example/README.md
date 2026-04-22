# NHIF TZ Wrapper - JSP Example

Reception-desk JSP webapp that authorizes NHIF cards using a fingerprint
captured on the client PC via Mantra's MidFingerAuth Client Service.

## Architecture

```
  [Receptionist PC]                        [Remote CentOS/Windows server]
  +-------------------+                    +-----------------------------+
  |  Browser          |  (1) GET /         |  Tomcat / GlassFish         |
  |  (on nhif.svc/*)  | ------------------>|    nhiftz-wrapper-jsp-example|
  |                   |                    |      IndexServlet -> index.jsp|
  |                   |  (5) POST /authorize (cardNo, fpCode, imageData)  |
  |                   | ------------------>|      AuthorizeServlet       |
  |                   |                    |        |                    |
  |                   |                    |        | (6) NhifApiClient  |
  |                   |                    |        v                    |
  |  capture.js       |                    |  +--------------------+     |
  |    (2) fetch      |                    |  | test.nhif.or.tz     |     |
  |    localhost:8010 |                    |  | (Auth + ServiceHub) |     |
  |    /midfingerauth |                    |  +--------------------+     |
  |        |          |                    |        |                    |
  |        v (3) device driver             |        | (7) JSON response   |
  |  +--------------+ |                    |        v                    |
  |  | MFS100/500   | |                    |      result.jsp            |
  |  +--------------+ |                    |                             |
  |    (4) ANSI_V378  |                    +-----------------------------+
  +-------------------+
```

Flow:

1. Receptionist opens the webapp hosted on the hospital server.
2. `capture.js` (running in the browser) calls `https://localhost:8010/midfingerauth/*` on the *receptionist's own PC*. `localhost` resolves to the PC the browser is running on, not the webapp server, so the browser talks to the Mantra client service installed locally.
3. Mantra's service drives the USB fingerprint reader (MFS100/MFS500).
4. Mantra returns a base64 ANSI_V378 template; `capture.js` stores it in the hidden `imageData` form field.
5. The browser submits the form (cardNo + fpCode + imageData + visitTypeID ...) to `POST /authorize` **on the server**.
6. `AuthorizeServlet` uses `NhifApiClient` (from the `nhiftz-wrapper` library) to call the NHIF Auth + ServiceHub APIs over HTTPS.
7. The server renders `result.jsp` with the NHIF authorization response.

The WAR itself is stateless. Each receptionist PC must have Mantra's MidFingerAuth Client Service installed and running; the webapp server never talks to the scanner. Reference test page shipped by Mantra:
`C:\Program Files\Mantra\MidFingerAuth\MidFingerAuthClientService\test\MIDFingerAuthClientServiceTest.htm`.

### Client-side prerequisites

1. Install *MidFingerAuth Client Service* (ships with the MFS100/MFS500 driver).
2. Run `ConfigMantraMIDFingerAuthClientService.exe` and add this webapp's
   origin (e.g. `https://nhif.hospital.local`) to the CORS whitelist.
3. Trust the service's self-signed cert for `https://localhost:8010` in the
   receptionist's browser; otherwise the fetch calls fail silently.

The capture flow used by `capture.js`:
`connecteddevicelist` -> `initdevice` -> `capture` -> `gettemplate`
(format `ANSI_V378`) -> set `imageData` -> submit form ->
`uninitdevice` on page unload.

## Configuration

The webapp reads five keys on startup. For each key it first checks the OS
environment variable, then falls back to a JVM system property with the same
name. That means you can pick whichever fits your platform:

| Key                   | Required | Example                                  |
| --------------------- | -------- | ---------------------------------------- |
| `NHIF_AUTH_URL`       | yes      | `https://test.nhif.or.tz`                |
| `NHIF_SERVICE_URL`    | yes      | `https://test.nhif.or.tz/servicehub`     |
| `NHIF_CLIENT_ID`      | yes      | `11014`                                  |
| `NHIF_CLIENT_SECRET`  | yes      | `...`                                    |
| `NHIF_USERNAME`       | yes      | `Mtundi`                                 |

If any key is missing, `/health` returns `503 {"wrapperConfigured":false,...}`
and `POST /authorize` returns `503 Server not configured - check NHIF_* env vars`.

## Quick start (local dev on Windows)

Pick the helper that matches how you run the webapp locally.

### NetBeans + bundled GlassFish (or any shell-launched server)

User-level env vars work here — NetBeans is launched by you, so it inherits them.

```bat
REM Command Prompt:
deploy\setup-dev-env.bat
```

```powershell
# PowerShell:
.\deploy\setup-dev-env.ps1
```

Close NetBeans, reopen it, press **F6**.

### Standalone Tomcat on Windows (including Windows service installs)

User-level env vars are **not** reliable here — the Tomcat service runs under
`LocalSystem` and won't inherit your per-user environment. Write Tomcat's own
`setenv.bat` instead (idempotent, preserves other content, restart optional):

```powershell
.\deploy\setup-tomcat-win.ps1 `
  -CatalinaHome 'C:\apache-tomcat-8.5.87' `
  -Restart
```

Override any value via parameters (`-AuthUrl`, `-ClientSecret`, etc.).
If your service isn't named `Tomcat8`, pass `-ServiceName <name>`.

### Verify

Hit `/health` on whatever context you deployed under:

```
http://localhost:8080/nhiftz-wrapper-jsp-example/health
http://localhost:8080/demo/health
```

Expected: `{"wrapperConfigured":true,...}`. If you still get
`503 Server not configured - check NHIF_* env vars`, the Tomcat process didn't
pick up the values - make sure you actually restarted it after running the helper.

### Do the values persist?

Yes. `setup-dev-env.*` writes to the Windows registry (User scope), and
`setup-tomcat-win.ps1` writes `setenv.bat` to disk. Both survive OS reboots
and Tomcat restarts. Re-run the helper any time credentials change.

## Build

```bash
cd nhiftz-wrapper-jsp-example
mvn package
# produces target/nhiftz-wrapper-jsp-example.war
```

## Deploy

Two helper scripts are in `deploy/`. Both are idempotent and safe to re-run.

### GlassFish 7 (Windows dev/test box)

Uses `asadmin create-jvm-options -DNHIF_*=...` so the config lives inside
`domain.xml`; no machine-wide environment variables required.

```powershell
cd nhiftz-wrapper-jsp-example
.\deploy\deploy-glassfish.ps1 `
  -War          .\target\nhiftz-wrapper-jsp-example.war `
  -AuthUrl      'https://test.nhif.or.tz' `
  -ServiceUrl   'https://test.nhif.or.tz/servicehub' `
  -ClientId     '11014' `
  -ClientSecret 'ntbzRGbrwwHj8Jwd7bbPsg==' `
  -Username     'Mtundi' `
  -HealthUrl    'http://localhost:8080/nhiftz-wrapper-jsp-example/health'
```

### Tomcat on CentOS / RHEL (production)

Reads the five values from a local env-file, writes them into
`$CATALINA_BASE/bin/setenv.sh` between managed markers, redeploys the WAR,
and restarts the `tomcat` systemd service.

```bash
# 1) On the CentOS box, copy the template and fill it in:
cp deploy/nhif.env.example /etc/nhif/nhif.prod.env
chmod 600 /etc/nhif/nhif.prod.env
$EDITOR /etc/nhif/nhif.prod.env

# 2) Deploy:
sudo CONFIG_FILE=/etc/nhif/nhif.prod.env \
     deploy/deploy-tomcat-centos.sh /path/to/nhiftz-wrapper-jsp-example.war
```

Environment overrides the script accepts:
`CATALINA_HOME` (default `/usr/share/tomcat`),
`CATALINA_BASE`,
`SERVICE_NAME` (default `tomcat`),
`CONTEXT_NAME` (default `nhiftz-wrapper-jsp-example`).

After the script exits cleanly, `/nhiftz-wrapper-jsp-example/health` returns
`200 {"wrapperConfigured":true,...}`.

### What the server calls

Once `/authorize` is hit, `AuthorizeServlet` uses `NhifApiClient` to POST the
fingerprint template to NHIF's ServiceHub (`/api/verification/authorize-card`).
The server does not need any of the Mantra binaries installed - those live on
the receptionist's PC.

## Endpoints

- `GET  /`           - reception form (rendered by `IndexServlet` + `index.jsp`)
- `POST /authorize`  - processes the form, calls NHIF, forwards to `result.jsp`
- `GET  /health`     - JSON status: `{wrapperConfigured, authUrl, serviceUrl, clientId}`

## Security

- Run Tomcat behind HTTPS in production.
- `NHIF_CLIENT_SECRET` must not be logged; only the webapp should read it.
- The fingerprint template (`imageData`) is never logged.
