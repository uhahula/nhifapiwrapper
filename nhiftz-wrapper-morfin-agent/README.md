# Morfin Auth Local Agent

Runs on each reception PC. Exposes the Mantra MFS500 scanner to browsers
on that PC via `http://localhost:8765`. The central NHIF JSP webapp's
page fetches from this agent to get a fingerprint template.

## One-time setup

1. Install the Mantra scanner driver on the PC (separate installer from
   Mantra; not bundled here). Plug in the MFS500 and verify Windows Device
   Manager lists it under "Biometric devices".
2. Install `Morfin_Auth.jar` to your local Maven repo once so the build
   can resolve the dependency:

   ```bash
   mvn install:install-file \
     -Dfile=../nhiftz-wrapper/mfs500/Morfin_Auth_Linux_Win_Java_1.0.0.19/Libs/Morfin_Auth.jar \
     -DgroupId=com.mantra.morfinauth \
     -DartifactId=morfin-auth \
     -Dversion=1.0.0.19 \
     -Dpackaging=jar
   ```

## Build

```bash
cd nhiftz-wrapper-morfin-agent
mvn package
# produces target/morfin-agent-1.0.0.jar
```

Native libraries (Windows DLLs, Linux `.so`) are bundled inside
`Morfin_Auth.jar` and extracted at runtime — no `java.library.path`
configuration required.

## Run

```bash
java -jar target/morfin-agent-1.0.0.jar
# optional: --port 8765
```

The agent binds to `127.0.0.1` only. It is not reachable from other
machines on the LAN.

## API

- `GET  /status`   — `{ready, deviceModel, serial, sdkVersion}` or `{ready:false, reason}`
- `POST /capture`  — body `{timeoutMs, minQuality, templateFormat}`; returns `{template, quality, nfiq}` or `{error, message}`
- `POST /shutdown` — clean exit (for installers)

## Windows auto-start at login

1. Press `Win+R`, type `shell:startup`, hit Enter.
2. Create a shortcut in that folder with target:
   `javaw -jar "C:\Program Files\MorfinAgent\morfin-agent-1.0.0.jar"`.
3. Copy the built JAR to that path.

## Linux auto-start (systemd user unit)

```ini
# ~/.config/systemd/user/morfin-agent.service
[Unit]
Description=Morfin Auth Local Agent

[Service]
ExecStart=/usr/bin/java -jar /opt/morfin-agent/morfin-agent-1.0.0.jar
Restart=on-failure

[Install]
WantedBy=default.target
```

```bash
systemctl --user enable --now morfin-agent
```

## Security

No authentication on the HTTP API. Trust boundary is the OS login — any
process running as the logged-in user can capture fingerprints. For
kiosks, lock the PC when the receptionist steps away.
