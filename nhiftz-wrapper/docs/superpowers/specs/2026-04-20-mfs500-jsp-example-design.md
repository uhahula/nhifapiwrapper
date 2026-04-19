# MFS500 + JSP biometric card authorization example

**Status:** Draft — awaiting user approval
**Date:** 2026-04-20
**Author:** brainstormed with Claude

## Problem

NHIF Tanzania providers who use the `nhiftz-wrapper` library want to authorize
member cards with live fingerprint capture from a Mantra MFS500 scanner. Their
typical environment is:

- Central Tomcat 8.5.87 on CentOS serving JSP pages to receptionists
- Receptionists on Windows PCs with the MFS500 plugged into their own desktop
- Java 17 on both server and client

This spec defines an end-to-end worked example — two new Maven modules that
ship alongside `nhiftz-wrapper` — that other integrators can copy.

## Non-goals

- Productionising a hospital reception kiosk (no installers, no service
  wrappers beyond documentation).
- Supporting facial biometrics (`biometricMethod = FACIAL`). Fingerprint only
  in this example; the same pattern extends trivially.
- Demonstrating practitioner login and POC reference flows. The example
  covers card authorization end-to-end; the two other biometric flows are
  already covered textually in `BiometricVerificationExample`.
- Automated device-level tests (need a physical scanner).

## Architecture

Two new Maven modules, siblings of `nhiftz-wrapper\`:

```
nhiftz-wrapper/                    (existing library, unchanged)
nhiftz-wrapper-morfin-agent/       (new, runnable fat JAR)
nhiftz-wrapper-jsp-example/        (new, WAR)
```

### Topology

```
+----------------------------+         +--------------------------+
| Receptionist Windows PC    |         | Central CentOS server    |
|                            |         |                          |
|  Chrome/Edge -----------+  |  HTTPS  |  Tomcat 8.5.87           |
|                         +---------->  nhiftz-wrapper-jsp        |
|                         |  |         |   -example.war           |
|  fetch(http://          |  |         |     uses nhiftz-wrapper  |
|   localhost:8765) <---+ |  |         +-----------+--------------+
|                       v |  |                     |
|  morfin-agent.jar -----+ |  |                     | HTTPS
|   Morfin_Auth JNI       |  |                     v
|   native libs (bundled) |  |            +--------+---------+
|                         |  |            | NHIF test env    |
|  MFS500 USB ------------+  |            | test.nhif.or.tz  |
+----------------------------+            +------------------+
```

The agent runs on each reception PC because the Morfin Auth Java SDK is a
JNI wrapper — it must execute in the same JVM that the USB scanner is
attached to, and it does not ship an HTTP/RD-service mode. The central
Tomcat never touches the SDK; it only receives the base64 template the
browser already collected.

Browsers treat `http://localhost` as a secure context, so an HTTPS central
page can `fetch("http://localhost:8765/...")` without mixed-content
blocking. This is the same mechanism Mantra's Aadhaar RD-Service relies on.

## Module 1: `nhiftz-wrapper-morfin-agent`

### Purpose

Small always-on Java process on each reception PC. Exposes a local HTTP API
that wraps the Morfin Auth SDK.

### Packaging

- `<packaging>jar</packaging>`, `maven-shade-plugin` produces
  `target/morfin-agent-1.0.0.jar` with `Morfin_Auth.jar` shaded inside.
- Native libraries (`win/x64/*.dll`, `linux/x86_64/*.so`, etc.) are already
  bundled inside `Morfin_Auth.jar` and extracted at runtime by the SDK's
  `NativeUtils` class — no `java.library.path` wrangling.
- Main class: `com.oau.nhif.morfin.agent.Main`.
- Run with: `java -jar morfin-agent-1.0.0.jar [--port 8765]`.
- Windows auto-start: shortcut in `shell:startup`. Documented in README.

### HTTP API

All JSON. Bound to `127.0.0.1` only. CORS headers:
`Access-Control-Allow-Origin: *`,
`Access-Control-Allow-Methods: GET, POST, OPTIONS`,
`Access-Control-Allow-Headers: Content-Type`.

| Method | Path        | Purpose |
|--------|-------------|---------|
| `GET`  | `/status`   | `{ready, deviceModel, serial, sdkVersion}` or `{ready:false, reason}` |
| `POST` | `/capture`  | body `{finger, timeoutMs, minQuality, templateFormat}`; returns `{template, quality, nfiq, fpCode}` or `{error}` |
| `POST` | `/shutdown` | graceful exit (optional, for installers) |

`finger` values match NHIF's `fpCode`: `R_THUMB`, `R_INDEX`, `R_MIDDLE`,
`R_RING`, `R_LITTLE`, `L_THUMB`, `L_INDEX`, `L_MIDDLE`, `L_RING`,
`L_LITTLE`. Mapped internally to `com.mantra.morfinauth.enums.FingerPostion`.

`templateFormat` values: `ANSI_V378` (default), `FMR_V2005`, `FMR_V2011`.
NHIF accepts base64 of any of these; `ANSI_V378` is the safe default.

Error codes returned in the `error` field:
`DEVICE_NOT_CONNECTED`, `TIMEOUT`, `LOW_QUALITY`, `BUSY`, `SDK_ERROR`.

### Classes

- `Main` — parses args, starts `HttpServer`, registers shutdown hook.
- `MorfinDevice` — thin wrapper around `MorfinAuth`. Methods:
  `init()`, `captureAnsiTemplate(FingerPostion, int timeoutMs, int minQuality)`,
  `status()`, `close()`. Single in-flight capture enforced via `ReentrantLock`.
- `StatusHandler`, `CaptureHandler`, `ShutdownHandler` — `HttpHandler`
  implementations; each ~30 lines.
- `Json` — 40-line hand-rolled JSON writer (no Jackson dep to keep the
  shaded JAR small).
- `FingerCodes` — map NHIF string codes ↔ `FingerPostion` enum.

### Lifecycle

- On startup: HTTP server binds immediately. `MorfinDevice.init()` runs on
  a background thread and retries `MorfinAuth.Init(DeviceModel.Auto, "", info)`
  with 2-second backoff until it succeeds. `/status` reflects current state
  and returns `{ready:false, reason: "DEVICE_NOT_CONNECTED"}` until init
  succeeds.
- Per capture: acquire lock, `AutoCapture(minQuality, timeoutMs, q, nfiq)`,
  `GetTemplate(buf, len, TemplateFormat.ANSI_V378)`, base64-encode, release
  lock.
- On JVM shutdown hook: `Uninit()`.

### Logging

`java.util.logging` to `%USERPROFILE%\.morfin-agent\agent.log`
(Windows) or `~/.morfin-agent/agent.log` (Linux), size-based rotation
(10 MB × 5 files). The base64 template is never logged.

## Module 2: `nhiftz-wrapper-jsp-example`

### Packaging

- `<packaging>war</packaging>`
- `<maven.compiler.source>17</maven.compiler.source>`
- Servlet 3.1 API / JSP 2.3 API (both `<scope>provided</scope>`)
- JSTL 1.2

### Dependencies

```xml
<dependency>
  <groupId>com.oau.nhif</groupId>
  <artifactId>nhiftz-wrapper</artifactId>
  <version>1.4.0</version>
</dependency>
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>javax.servlet-api</artifactId>
  <version>3.1.0</version>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>javax.servlet.jsp</groupId>
  <artifactId>javax.servlet.jsp-api</artifactId>
  <version>2.3.3</version>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>jstl</artifactId>
  <version>1.2</version>
</dependency>
```

No Morfin Auth dependency — the server never handles the SDK.

### Configuration

Credentials come from environment variables, read at
`ServletContextListener.contextInitialized` time:

| Var                  | Example                               |
|----------------------|---------------------------------------|
| `NHIF_AUTH_URL`      | `https://test.nhif.or.tz`             |
| `NHIF_SERVICE_URL`   | `https://test.nhif.or.tz/servicehub`  |
| `NHIF_CLIENT_ID`     | `11014`                               |
| `NHIF_CLIENT_SECRET` | `ntbzRGbrwwHj8Jwd7bbPsg==`            |
| `NHIF_USERNAME`      | `Mtundi`                              |

Missing/empty values → client not initialized; `/health` reports
`unconfigured`; `/authorize` returns 503.

### Pages

- `WEB-INF/web.xml` — Servlet 3.1, registers context listener and servlets.
- `index.jsp` — the reception form:
  - Card number (text)
  - Finger dropdown (10 NHIF codes)
  - Visit type dropdown (populated from `client.getVisitTypes()` at page
    render time, cached for 1 hour in a `ServletContext` attribute; falls
    back to a hardcoded list `[{1, "Normal"}]` if the API call fails)
  - Referral number (optional)
  - Remarks
  - "Check scanner" button → probes `http://localhost:8765/status`
  - "Capture & Authorize" button → captures, then submits form
  - `<div id="scanner-status">` feedback area
- `result.jsp` — renders `CardAuthorizationResponse`:
  - Green banner `APPROVED` or red `REJECTED`
  - Authorization # (big), member #
  - Patient name, gender, DOB
  - Scheme name, expiry date
  - `statusDescription` on rejection
  - "New authorization" link back to `index.jsp`
- `error.jsp` — generic error page; stacktrace shown only when
  `debug=true` context-param is set.

### Servlets (`com.oau.nhif.jspexample.web`)

- `AuthorizeServlet` (`POST /authorize`)
  - Reads form params + hidden `imageData` field
  - Builds `CardAuthorizationRequest` via the wrapper's fluent API,
    `biometricMethod = "FINGERPRINT"`
  - `client.authorizeCardWithBiometric(req).get()` — blocking here is
    appropriate, the receptionist is waiting on the response
  - Forwards to `result.jsp` on success or `error.jsp` on exception
- `HealthServlet` (`GET /health`)
  - Returns JSON `{wrapperConfigured, authUrl, serviceUrl, clientId}` —
    never leaks secret or username.

### Browser-side JS (`webapp/js/capture.js`)

~80 lines, vanilla.

- On page load: `fetch("http://localhost:8765/status", {signal: AbortSignal.timeout(1000)})`
  - OK → green "Scanner ready — MFS500 (s/n …)"
  - Fail → red banner with "Is the Morfin Agent running?" + link to the
    agent's startup docs
- On "Capture & Authorize" click:
  1. Disable button, show "Place finger on scanner…"
  2. `fetch("http://localhost:8765/capture", {method:"POST", body: JSON.stringify({finger, timeoutMs:10000, minQuality:60, templateFormat:"ANSI_V378"})})`
  3. On `{template,…}` → set hidden `imageData` input, submit form
  4. On `{error}` → show human-readable message, re-enable button
  5. On network error → "Cannot reach local scanner service."

## Data flow (card authorization)

1. Receptionist opens `https://nhif.hospital.local/nhiftz-wrapper-jsp-example/`
2. JS probes `http://localhost:8765/status` → green light
3. Receptionist types card number, picks `R_INDEX`, clicks capture
4. JS POSTs to `/capture`; agent runs `AutoCapture` → `GetTemplate(ANSI_V378)` → base64
5. JS puts base64 into hidden `imageData` field, form submits to
   `POST /authorize` on the central server
6. `AuthorizeServlet` builds `CardAuthorizationRequest` and calls
   `authorizeCardWithBiometric` on the wrapper
7. Wrapper calls NHIF's `/api/Verification/AuthorizeCard`
8. Response rendered in `result.jsp`

## Error handling matrix

| Failure                         | Detected by           | User sees                                            |
|---------------------------------|-----------------------|------------------------------------------------------|
| Agent not running               | Browser `fetch`       | "Cannot reach local scanner — start Morfin Agent"    |
| MFS500 unplugged                | Agent `/capture`      | "Plug in the MFS500 and retry"                       |
| Capture timeout                 | Agent `/capture`      | "Didn't detect a finger — try again"                 |
| Low-quality scan                | Agent `/capture`      | "Scan quality too low — clean finger and retry"      |
| Concurrent capture in flight    | Agent `/capture`      | "Another capture is already running"                 |
| NHIF rejects card               | `result.jsp`          | Red banner with NHIF `statusDescription`             |
| NHIF API network error          | Servlet catches       | `error.jsp` with correlation ID                      |
| Missing env vars                | `HealthServlet` + 503 | "Server not configured — check NHIF_* env vars"      |

## Security

- Agent binds to `127.0.0.1` only — not reachable from other LAN hosts.
- No auth on the agent; OS login is the trust boundary. Documented as a
  known limitation.
- Central webapp should run behind HTTPS in production.
- Servlets never log the `imageData` field; a log filter redacts it.

## Testing plan

- **Unit tests** (small, pure Java):
  - `FingerCodes` mapping both directions
  - JSON encoder round-trip
  - `AuthorizeServlet` param parsing with mocked `NhifApiClient`
- **Manual integration tests** (with MFS500 attached):
  1. Windows dev box: `curl` the agent's `/status` and `/capture`, verify
     base64 template returned.
  2. Local Tomcat 8.5.87 on Windows + agent on same box: full form flow
     with the known test card `101502314766`.
  3. CentOS staging Tomcat + Windows client browser: same flow
     cross-origin; confirms localhost fetch works from HTTPS page.

## Repo layout delivered

```
nhiftz-wrapper-morfin-agent/
  pom.xml
  README.md                       # install Morfin_Auth jar, run, auto-start
  src/main/java/com/oau/nhif/morfin/agent/
    Main.java
    MorfinDevice.java
    handlers/StatusHandler.java
    handlers/CaptureHandler.java
    handlers/ShutdownHandler.java
    util/FingerCodes.java
    util/Json.java
  src/test/java/...               # FingerCodes, Json tests

nhiftz-wrapper-jsp-example/
  pom.xml
  README.md                       # env vars, deploy to Tomcat 8.5.87
  src/main/java/com/oau/nhif/jspexample/web/
    NhifClientContextListener.java
    AuthorizeServlet.java
    HealthServlet.java
  src/main/webapp/
    index.jsp
    result.jsp
    error.jsp
    js/capture.js
    css/app.css
    WEB-INF/web.xml

docs/superpowers/specs/2026-04-20-mfs500-jsp-example-design.md   # this file
```

## Open questions / deferred

- None that block building the example. The live NHIF test environment
  will tell us quickly if `ANSI_V378` is the right template flavour; if
  not, the agent exposes `templateFormat` on the capture request so the
  JS can switch to `FMR_V2011` without touching Java.
