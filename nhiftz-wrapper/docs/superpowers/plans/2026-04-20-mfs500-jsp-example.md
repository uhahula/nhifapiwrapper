# MFS500 + JSP biometric authorization — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Ship two runnable example modules — a local Windows/Linux agent that bridges the Mantra MFS500 scanner to the browser, and a central JSP webapp on Tomcat 8.5.87 that uses the captured fingerprint to authorize NHIF cards — both consuming the existing `nhiftz-wrapper` library.

**Architecture:** Browser on receptionist PC loads the central JSP page over HTTPS; the page's JavaScript `fetch()`es `http://localhost:8765/capture` on the same PC (always a secure context in modern browsers), relays the base64 ANSI 378 template in a hidden form field, and POSTs to `AuthorizeServlet`, which invokes the `nhiftz-wrapper` client's `authorizeCardWithBiometric` against the NHIF test environment.

**Tech Stack:** Java 17, JDK built-in `com.sun.net.httpserver` (agent), Servlet 3.1 + JSP 2.3 + JSTL 1.2 (webapp, `javax.*`), Maven, JUnit 5.8.2, Mockito 4.11.0, `maven-shade-plugin` for the agent fat JAR, `maven-war-plugin` for the webapp, Mantra `Morfin_Auth.jar` 1.0.0.19.

**Repo layout produced:** Both modules sit at the repo root (`C:/project/nhifapi/nhifapiwrapper/`), as siblings of the existing `nhiftz-wrapper/` library.

**Spec:** `nhiftz-wrapper/docs/superpowers/specs/2026-04-20-mfs500-jsp-example-design.md`

---

## Preamble: reusable shell alias

Every Maven command in this plan assumes you have set this alias in the current bash shell (per `nhiftz-wrapper/CLAUDE.md`):

```bash
alias mvn='JAVA_HOME="/c/Program Files/Java/jdk-22" "/c/Program Files/Java/jdk-22/bin/java" -classpath "/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/boot/plexus-classworlds-2.8.0.jar" -Dclassworlds.conf="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/bin/m2.conf" -Dmaven.home="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn" -Dmaven.multiModuleProjectDirectory="." org.codehaus.plexus.classworlds.launcher.Launcher'
```

Run this once at the start of each shell session. It shadows any real `mvn` on PATH and works because the project's own CLAUDE.md documents this as the only reliable Maven invocation on the dev box.

---

## Part A — Local Morfin Agent

Repo path: `C:/project/nhifapi/nhifapiwrapper/nhiftz-wrapper-morfin-agent/`

### Task A1: Module scaffold and Morfin_Auth JAR install

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/pom.xml`
- Create: `nhiftz-wrapper-morfin-agent/.gitignore`

- [ ] **Step 1: Install Morfin_Auth.jar to local Maven repo**

This is a one-time bootstrap so the pom can depend on it as a normal coordinate.

```bash
mvn install:install-file \
  -Dfile=nhiftz-wrapper/mfs500/Morfin_Auth_Linux_Win_Java_1.0.0.19/Libs/Morfin_Auth.jar \
  -DgroupId=com.mantra.morfinauth \
  -DartifactId=morfin-auth \
  -Dversion=1.0.0.19 \
  -Dpackaging=jar
```

Expected output ends with `BUILD SUCCESS`.

- [ ] **Step 2: Create the module `pom.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.oau.nhif</groupId>
    <artifactId>nhiftz-wrapper-morfin-agent</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>NHIF TZ Wrapper — Morfin Auth Local Agent</name>
    <description>Localhost HTTP bridge exposing the Mantra MFS500 scanner
      to browsers on the same PC.</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <junit.version>5.8.2</junit.version>
        <mockito.version>4.11.0</mockito.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.mantra.morfinauth</groupId>
            <artifactId>morfin-auth</artifactId>
            <version>1.0.0.19</version>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>morfin-agent-${project.version}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals><goal>shade</goal></goals>
                        <configuration>
                            <createDependencyReducedPom>false</createDependencyReducedPom>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>com.oau.nhif.morfin.agent.Main</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 3: Create `.gitignore`**

```
target/
*.iml
.idea/
.nbbuild.xml
nbproject/private/
```

- [ ] **Step 4: Verify the scaffold compiles**

```bash
cd nhiftz-wrapper-morfin-agent && mvn compile
```

Expected: `BUILD SUCCESS`. Nothing to compile yet, but it proves the pom + dependency resolution works.

- [ ] **Step 5: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/
git commit -m "agent: scaffold nhiftz-wrapper-morfin-agent module"
```

---

### Task A2: `Json` writer utility (TDD)

Hand-rolled JSON so the shaded JAR stays small. Only needs to emit simple flat objects and read flat request bodies.

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/util/Json.java`
- Create: `nhiftz-wrapper-morfin-agent/src/test/java/com/oau/nhif/morfin/agent/util/JsonTest.java`

- [ ] **Step 1: Write the failing test**

```java
package com.oau.nhif.morfin.agent.util;

import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    @Test
    void writesFlatObjectWithStringsIntsBooleans() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("ready", true);
        m.put("quality", 72);
        m.put("deviceModel", "MFS500");
        assertEquals(
            "{\"ready\":true,\"quality\":72,\"deviceModel\":\"MFS500\"}",
            Json.writeObject(m));
    }

    @Test
    void escapesQuotesAndBackslashesInStringValues() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("reason", "device said \"no\" \\ goodbye");
        assertEquals(
            "{\"reason\":\"device said \\\"no\\\" \\\\ goodbye\"}",
            Json.writeObject(m));
    }

    @Test
    void writesNullValuesAsJsonNull() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("serial", null);
        assertEquals("{\"serial\":null}", Json.writeObject(m));
    }

    @Test
    void parsesFlatObjectFromRequestBody() {
        String body = "{\"timeoutMs\":10000,\"minQuality\":60,\"templateFormat\":\"ANSI_V378\"}";
        Map<String, String> out = Json.readFlatObject(body);
        assertEquals("10000", out.get("timeoutMs"));
        assertEquals("60", out.get("minQuality"));
        assertEquals("ANSI_V378", out.get("templateFormat"));
    }

    @Test
    void parsesEmptyObject() {
        assertTrue(Json.readFlatObject("{}").isEmpty());
    }

    @Test
    void parserRejectsNested() {
        assertThrows(IllegalArgumentException.class,
            () -> Json.readFlatObject("{\"a\":{\"b\":1}}"));
    }
}
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
cd nhiftz-wrapper-morfin-agent && mvn test -Dtest=JsonTest
```

Expected: compile error — `Json` class does not exist.

- [ ] **Step 3: Implement `Json`**

```java
package com.oau.nhif.morfin.agent.util;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Json {
    private Json() {}

    public static String writeObject(Map<String, ?> map) {
        StringBuilder sb = new StringBuilder(64);
        sb.append('{');
        boolean first = true;
        for (Map.Entry<String, ?> e : map.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append('"').append(escape(e.getKey())).append("\":");
            Object v = e.getValue();
            if (v == null) sb.append("null");
            else if (v instanceof Boolean) sb.append(v);
            else if (v instanceof Number) sb.append(v);
            else sb.append('"').append(escape(v.toString())).append('"');
        }
        sb.append('}');
        return sb.toString();
    }

    public static Map<String, String> readFlatObject(String body) {
        String s = body.trim();
        if (s.isEmpty() || s.charAt(0) != '{' || s.charAt(s.length() - 1) != '}')
            throw new IllegalArgumentException("not a JSON object: " + body);
        Map<String, String> out = new LinkedHashMap<>();
        int i = 1;
        int end = s.length() - 1;
        while (i < end) {
            while (i < end && Character.isWhitespace(s.charAt(i))) i++;
            if (i >= end) break;
            if (s.charAt(i) != '"')
                throw new IllegalArgumentException("expected key quote at " + i);
            int keyStart = ++i;
            while (i < end && s.charAt(i) != '"') i++;
            String key = s.substring(keyStart, i);
            i++; // skip closing quote
            while (i < end && s.charAt(i) != ':') i++;
            i++; // skip colon
            while (i < end && Character.isWhitespace(s.charAt(i))) i++;
            String value;
            if (s.charAt(i) == '"') {
                int vStart = ++i;
                StringBuilder v = new StringBuilder();
                while (i < end && s.charAt(i) != '"') {
                    if (s.charAt(i) == '\\' && i + 1 < end) {
                        char n = s.charAt(i + 1);
                        if (n == '"') v.append('"');
                        else if (n == '\\') v.append('\\');
                        else v.append(s.charAt(i)).append(n);
                        i += 2;
                    } else {
                        v.append(s.charAt(i));
                        i++;
                    }
                }
                value = v.toString();
                i++; // skip closing quote
            } else if (s.charAt(i) == '{' || s.charAt(i) == '[') {
                throw new IllegalArgumentException("nested values not supported");
            } else {
                int vStart = i;
                while (i < end && s.charAt(i) != ',' && !Character.isWhitespace(s.charAt(i))) i++;
                value = s.substring(vStart, i);
            }
            out.put(key, value);
            while (i < end && (Character.isWhitespace(s.charAt(i)) || s.charAt(i) == ',')) i++;
        }
        return out;
    }

    private static String escape(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') sb.append("\\\"");
            else if (c == '\\') sb.append("\\\\");
            else if (c == '\n') sb.append("\\n");
            else if (c == '\r') sb.append("\\r");
            else if (c == '\t') sb.append("\\t");
            else sb.append(c);
        }
        return sb.toString();
    }
}
```

- [ ] **Step 4: Run the test and verify it passes**

```bash
mvn test -Dtest=JsonTest
```

Expected: `Tests run: 6, Failures: 0`.

- [ ] **Step 5: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/src/
git commit -m "agent: add minimal hand-rolled JSON writer/reader"
```

---

### Task A3: `FingerCodes` validation (TDD)

Used by the webapp's servlet and by the browser. Lives in the agent module because both modules share the constant list and the agent bundles `/status` metadata that echoes the codes back for UI population.

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/util/FingerCodes.java`
- Create: `nhiftz-wrapper-morfin-agent/src/test/java/com/oau/nhif/morfin/agent/util/FingerCodesTest.java`

- [ ] **Step 1: Write the failing test**

```java
package com.oau.nhif.morfin.agent.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FingerCodesTest {

    @Test
    void exposesAllTenNhifCodes() {
        assertEquals(10, FingerCodes.ALL.size());
        assertTrue(FingerCodes.ALL.contains("R_INDEX"));
        assertTrue(FingerCodes.ALL.contains("L_LITTLE"));
    }

    @Test
    void isValidAcceptsAllKnownCodes() {
        for (String c : FingerCodes.ALL) assertTrue(FingerCodes.isValid(c));
    }

    @Test
    void isValidRejectsUnknownInputs() {
        assertFalse(FingerCodes.isValid("THUMB"));
        assertFalse(FingerCodes.isValid(null));
        assertFalse(FingerCodes.isValid(""));
    }

    @Test
    void humanNameRendersReadable() {
        assertEquals("Right Index", FingerCodes.humanName("R_INDEX"));
        assertEquals("Left Little", FingerCodes.humanName("L_LITTLE"));
    }
}
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
mvn test -Dtest=FingerCodesTest
```

Expected: compile error.

- [ ] **Step 3: Implement `FingerCodes`**

```java
package com.oau.nhif.morfin.agent.util;

import java.util.List;

public final class FingerCodes {
    private FingerCodes() {}

    public static final List<String> ALL = List.of(
        "R_THUMB", "R_INDEX", "R_MIDDLE", "R_RING", "R_LITTLE",
        "L_THUMB", "L_INDEX", "L_MIDDLE", "L_RING", "L_LITTLE"
    );

    public static boolean isValid(String code) {
        return code != null && ALL.contains(code);
    }

    public static String humanName(String code) {
        if (!isValid(code)) return code;
        String side = code.startsWith("R_") ? "Right " : "Left ";
        String finger = code.substring(2);
        return side + finger.charAt(0) + finger.substring(1).toLowerCase();
    }
}
```

- [ ] **Step 4: Run the test and verify it passes**

```bash
mvn test -Dtest=FingerCodesTest
```

Expected: `Tests run: 4, Failures: 0`.

- [ ] **Step 5: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/src/
git commit -m "agent: add FingerCodes validation helper"
```

---

### Task A4: `MorfinDevice` interface and fake

Handlers must be unit-testable without a physical scanner. We introduce an interface `MorfinDevice` with a `FakeMorfinDevice` for tests. The real JNI-backed `MorfinDeviceImpl` comes in A5.

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/device/MorfinDevice.java`
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/device/DeviceStatus.java`
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/device/CaptureResult.java`
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/device/CaptureException.java`
- Create: `nhiftz-wrapper-morfin-agent/src/test/java/com/oau/nhif/morfin/agent/device/FakeMorfinDevice.java`
- Create: `nhiftz-wrapper-morfin-agent/src/test/java/com/oau/nhif/morfin/agent/device/FakeMorfinDeviceTest.java`

- [ ] **Step 1: Write the failing test**

```java
package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.enums.TemplateFormat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FakeMorfinDeviceTest {

    @Test
    void defaultFakeIsReadyAndReturnsCannedTemplate() throws Exception {
        FakeMorfinDevice fake = new FakeMorfinDevice();
        DeviceStatus s = fake.status();
        assertTrue(s.ready());
        assertEquals("MFS500", s.deviceModel());

        CaptureResult r = fake.capture(5000, 60, TemplateFormat.ANSI_V378);
        assertNotNull(r.templateBase64());
        assertEquals(72, r.quality());
        assertEquals(2, r.nfiq());
    }

    @Test
    void fakeCanBeConfiguredToReportNotReady() {
        FakeMorfinDevice fake = new FakeMorfinDevice().withReady(false, "DEVICE_NOT_CONNECTED");
        DeviceStatus s = fake.status();
        assertFalse(s.ready());
        assertEquals("DEVICE_NOT_CONNECTED", s.reason());
    }

    @Test
    void fakeCanBeConfiguredToThrowOnCapture() {
        FakeMorfinDevice fake = new FakeMorfinDevice().withCaptureFailure(CaptureException.Kind.TIMEOUT);
        CaptureException ex = assertThrows(CaptureException.class,
            () -> fake.capture(1000, 60, TemplateFormat.ANSI_V378));
        assertEquals(CaptureException.Kind.TIMEOUT, ex.kind());
    }
}
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
mvn test -Dtest=FakeMorfinDeviceTest
```

Expected: compile errors — none of the classes exist.

- [ ] **Step 3: Implement the interface and value types**

`MorfinDevice.java`:

```java
package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.enums.TemplateFormat;

public interface MorfinDevice extends AutoCloseable {
    DeviceStatus status();
    CaptureResult capture(int timeoutMs, int minQuality, TemplateFormat format) throws CaptureException;
    @Override void close();
}
```

`DeviceStatus.java`:

```java
package com.oau.nhif.morfin.agent.device;

public record DeviceStatus(
    boolean ready,
    String deviceModel,
    String serial,
    String sdkVersion,
    String reason
) {
    public static DeviceStatus notReady(String reason) {
        return new DeviceStatus(false, null, null, null, reason);
    }
    public static DeviceStatus ready(String deviceModel, String serial, String sdkVersion) {
        return new DeviceStatus(true, deviceModel, serial, sdkVersion, null);
    }
}
```

`CaptureResult.java`:

```java
package com.oau.nhif.morfin.agent.device;

public record CaptureResult(String templateBase64, int quality, int nfiq) {}
```

`CaptureException.java`:

```java
package com.oau.nhif.morfin.agent.device;

public class CaptureException extends Exception {
    public enum Kind { DEVICE_NOT_CONNECTED, TIMEOUT, LOW_QUALITY, BUSY, SDK_ERROR }

    private final Kind kind;

    public CaptureException(Kind kind, String message) {
        super(message);
        this.kind = kind;
    }
    public Kind kind() { return kind; }
}
```

- [ ] **Step 4: Implement the fake**

`src/test/java/com/oau/nhif/morfin/agent/device/FakeMorfinDevice.java`:

```java
package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.enums.TemplateFormat;
import java.util.Base64;

public class FakeMorfinDevice implements MorfinDevice {
    private boolean ready = true;
    private String notReadyReason;
    private CaptureException.Kind captureFailure;

    public FakeMorfinDevice withReady(boolean ready, String reason) {
        this.ready = ready;
        this.notReadyReason = reason;
        return this;
    }

    public FakeMorfinDevice withCaptureFailure(CaptureException.Kind kind) {
        this.captureFailure = kind;
        return this;
    }

    @Override
    public DeviceStatus status() {
        if (!ready) return DeviceStatus.notReady(notReadyReason);
        return DeviceStatus.ready("MFS500", "FAKE-SERIAL-1", "1.0.0.19-fake");
    }

    @Override
    public CaptureResult capture(int timeoutMs, int minQuality, TemplateFormat format) throws CaptureException {
        if (captureFailure != null)
            throw new CaptureException(captureFailure, "fake failure: " + captureFailure);
        String b64 = Base64.getEncoder().encodeToString("FAKE_TEMPLATE_DATA".getBytes());
        return new CaptureResult(b64, 72, 2);
    }

    @Override
    public void close() { /* noop */ }
}
```

- [ ] **Step 5: Run the test and verify it passes**

```bash
mvn test -Dtest=FakeMorfinDeviceTest
```

Expected: `Tests run: 3, Failures: 0`.

- [ ] **Step 6: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/src/
git commit -m "agent: introduce MorfinDevice interface and test fake"
```

---

### Task A5: `MorfinDeviceImpl` — real SDK wrapper

No unit tests (requires the physical MFS500). Smoke-tested in A10. Mantra SDK uses `int` return codes; we translate to exceptions.

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/device/MorfinDeviceImpl.java`
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/device/NoOpCallback.java`

- [ ] **Step 1: Create the no-op SDK callback**

The Mantra `MorfinAuth` constructor requires a callback. We pass a no-op
because the agent uses synchronous `AutoCapture`, not the async callback
path.

```java
package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.MorfinAuth_Callback;
import com.mantra.morfinauth.enums.DeviceDetection;
import com.mantra.morfinauth.enums.FingerPostion;

public class NoOpCallback implements MorfinAuth_Callback {
    @Override public void OnDeviceDetection(String deviceModel, DeviceDetection detection) {}
    @Override public void OnPreview(int width, int height, byte[] image) {}
    @Override public void OnComplete(int quality, int nfiq, int errorCode) {}
    @Override public void OnFingerPostionDetection(int retry, FingerPostion position) {}
}
```

These signatures were verified against `Morfin_Auth.jar` 1.0.0.19 via
`javap -p com.mantra.morfinauth.MorfinAuth_Callback`. If a future SDK
version breaks them, repeat that command and adjust.

- [ ] **Step 2: Create `MorfinDeviceImpl`**

```java
package com.oau.nhif.morfin.agent.device;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import com.mantra.morfinauth.MorfinAuthNative;
import com.mantra.morfinauth.enums.DeviceModel;
import com.mantra.morfinauth.enums.TemplateFormat;

import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class MorfinDeviceImpl implements MorfinDevice {
    private static final Logger LOG = Logger.getLogger(MorfinDeviceImpl.class.getName());
    private static final DeviceModel MODEL = DeviceModel.MFS500;

    private final MorfinAuth sdk;
    private final ReentrantLock captureLock = new ReentrantLock();

    private volatile DeviceInfo deviceInfo;
    private volatile boolean initialized;

    public MorfinDeviceImpl() {
        this.sdk = new MorfinAuth(new NoOpCallback());
        startBackgroundInit();
    }

    private void startBackgroundInit() {
        Thread t = new Thread(this::initLoop, "morfin-init");
        t.setDaemon(true);
        t.start();
    }

    private void initLoop() {
        while (!initialized) {
            try {
                if (sdk.IsDeviceConnected(MODEL)) {
                    DeviceInfo info = new DeviceInfo();
                    int rc = sdk.Init(MODEL, null, info);
                    if (rc == 0) {
                        this.deviceInfo = info;
                        this.initialized = true;
                        LOG.info("MFS500 initialized: serial=" + info.SerialNo);
                        return;
                    }
                    LOG.warning("Init returned " + rc + ": " + sdk.GetErrorMessage(rc));
                }
            } catch (Throwable t) {
                LOG.warning("init attempt failed: " + t.getMessage());
            }
            try { Thread.sleep(2000); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    @Override
    public DeviceStatus status() {
        if (!initialized || deviceInfo == null)
            return DeviceStatus.notReady("DEVICE_NOT_CONNECTED");
        return DeviceStatus.ready(MODEL.name(), deviceInfo.SerialNo, sdk.GetSDKVersion());
    }

    @Override
    public CaptureResult capture(int timeoutMs, int minQuality, TemplateFormat format) throws CaptureException {
        if (!initialized)
            throw new CaptureException(CaptureException.Kind.DEVICE_NOT_CONNECTED, "device not initialized");
        if (!captureLock.tryLock())
            throw new CaptureException(CaptureException.Kind.BUSY, "another capture in progress");
        try {
            int[] quality = new int[1];
            int[] nfiq = new int[1];
            int rc = sdk.AutoCapture(minQuality, timeoutMs, quality, nfiq);
            if (rc != 0) {
                String msg = sdk.GetErrorMessage(rc);
                CaptureException.Kind kind = classify(rc, msg);
                throw new CaptureException(kind, "AutoCapture rc=" + rc + ": " + msg);
            }
            int bufSize = deviceInfo.Width * deviceInfo.Height;
            byte[] buf = new byte[bufSize];
            int[] len = new int[]{bufSize};
            int tr = sdk.GetTemplate(buf, len, format);
            if (tr != 0)
                throw new CaptureException(CaptureException.Kind.SDK_ERROR,
                    "GetTemplate rc=" + tr + ": " + sdk.GetErrorMessage(tr));
            byte[] trimmed = new byte[len[0]];
            System.arraycopy(buf, 0, trimmed, 0, len[0]);
            return new CaptureResult(
                Base64.getEncoder().encodeToString(trimmed),
                quality[0], nfiq[0]);
        } finally {
            captureLock.unlock();
        }
    }

    private CaptureException.Kind classify(int rc, String msg) {
        String m = (msg == null ? "" : msg.toLowerCase());
        if (m.contains("timeout")) return CaptureException.Kind.TIMEOUT;
        if (m.contains("quality")) return CaptureException.Kind.LOW_QUALITY;
        if (rc == MorfinAuthNative.DEVICE_NOT_INITIALIZED)
            return CaptureException.Kind.DEVICE_NOT_CONNECTED;
        return CaptureException.Kind.SDK_ERROR;
    }

    @Override
    public void close() {
        try { sdk.StopCapture(); } catch (Throwable ignored) {}
        try { sdk.Uninit(); } catch (Throwable ignored) {}
    }
}
```

- [ ] **Step 3: Compile the module**

```bash
cd nhiftz-wrapper-morfin-agent && mvn compile
```

Expected: `BUILD SUCCESS`. If the `NoOpCallback` method signatures don't
match, open `src/main/java/com/oau/nhif/morfin/agent/device/NoOpCallback.java`
and adjust based on what `javap -p com.mantra.morfinauth.MorfinAuth_Callback`
reports against the installed JAR.

- [ ] **Step 4: Run existing tests to confirm nothing broke**

```bash
mvn test
```

Expected: all A2/A3/A4 tests still pass.

- [ ] **Step 5: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/src/
git commit -m "agent: add real MorfinDeviceImpl wrapping the JNI SDK"
```

---

### Task A6: `StatusHandler` (TDD)

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/http/StatusHandler.java`
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/http/CorsFilter.java`
- Create: `nhiftz-wrapper-morfin-agent/src/test/java/com/oau/nhif/morfin/agent/http/StatusHandlerTest.java`

- [ ] **Step 1: Write the failing test**

We drive `HttpHandler` directly against a real in-memory `HttpServer`.

```java
package com.oau.nhif.morfin.agent.http;

import com.oau.nhif.morfin.agent.device.FakeMorfinDevice;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

class StatusHandlerTest {

    private HttpServer server;

    @AfterEach
    void tearDown() { if (server != null) server.stop(0); }

    private int startWith(StatusHandler h) throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/status", h);
        server.start();
        return server.getAddress().getPort();
    }

    @Test
    void reportsReadyWhenDeviceReady() throws Exception {
        int port = startWith(new StatusHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/status")).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"ready\":true"));
        assertTrue(r.body().contains("\"deviceModel\":\"MFS500\""));
    }

    @Test
    void reportsNotReadyWhenDeviceUnavailable() throws Exception {
        FakeMorfinDevice fake = new FakeMorfinDevice().withReady(false, "DEVICE_NOT_CONNECTED");
        int port = startWith(new StatusHandler(fake));
        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/status")).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"ready\":false"));
        assertTrue(r.body().contains("\"reason\":\"DEVICE_NOT_CONNECTED\""));
    }

    @Test
    void rejectsNonGet() throws Exception {
        int port = startWith(new StatusHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/status"))
                .POST(HttpRequest.BodyPublishers.noBody()).build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(405, r.statusCode());
    }
}
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
mvn test -Dtest=StatusHandlerTest
```

Expected: compile error — `StatusHandler` does not exist.

- [ ] **Step 3: Implement `CorsFilter` (used by every handler)**

```java
package com.oau.nhif.morfin.agent.http;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class CorsFilter extends Filter {
    @Override public String description() { return "CORS"; }

    @Override
    public void doFilter(HttpExchange ex, Chain chain) throws IOException {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(204, -1);
            ex.close();
            return;
        }
        chain.doFilter(ex);
    }
}
```

- [ ] **Step 4: Implement `StatusHandler`**

```java
package com.oau.nhif.morfin.agent.http;

import com.oau.nhif.morfin.agent.device.DeviceStatus;
import com.oau.nhif.morfin.agent.device.MorfinDevice;
import com.oau.nhif.morfin.agent.util.Json;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatusHandler implements HttpHandler {
    private final MorfinDevice device;

    public StatusHandler(MorfinDevice device) { this.device = device; }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }
        DeviceStatus s = device.status();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ready", s.ready());
        body.put("deviceModel", s.deviceModel());
        body.put("serial", s.serial());
        body.put("sdkVersion", s.sdkVersion());
        body.put("reason", s.reason());
        byte[] out = Json.writeObject(body).getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(200, out.length);
        ex.getResponseBody().write(out);
        ex.close();
    }
}
```

- [ ] **Step 5: Run the test and verify it passes**

```bash
mvn test -Dtest=StatusHandlerTest
```

Expected: `Tests run: 3, Failures: 0`.

- [ ] **Step 6: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/src/
git commit -m "agent: add StatusHandler and CorsFilter"
```

---

### Task A7: `CaptureHandler` (TDD)

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/http/CaptureHandler.java`
- Create: `nhiftz-wrapper-morfin-agent/src/test/java/com/oau/nhif/morfin/agent/http/CaptureHandlerTest.java`

- [ ] **Step 1: Write the failing test**

```java
package com.oau.nhif.morfin.agent.http;

import com.oau.nhif.morfin.agent.device.CaptureException;
import com.oau.nhif.morfin.agent.device.FakeMorfinDevice;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class CaptureHandlerTest {

    private HttpServer server;

    @AfterEach
    void tearDown() { if (server != null) server.stop(0); }

    private int startWith(CaptureHandler h) throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/capture", h);
        server.start();
        return server.getAddress().getPort();
    }

    private HttpResponse<String> postJson(int port, String body) throws Exception {
        return HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/capture"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build(),
            HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void returnsTemplateOnSuccess() throws Exception {
        int port = startWith(new CaptureHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = postJson(port,
            "{\"timeoutMs\":5000,\"minQuality\":60,\"templateFormat\":\"ANSI_V378\"}");
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"template\":\""));
        assertTrue(r.body().contains("\"quality\":72"));
        assertTrue(r.body().contains("\"nfiq\":2"));
    }

    @Test
    void usesDefaultsWhenFieldsMissing() throws Exception {
        int port = startWith(new CaptureHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = postJson(port, "{}");
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"template\":\""));
    }

    @Test
    void returnsErrorJsonOnTimeout() throws Exception {
        FakeMorfinDevice fake = new FakeMorfinDevice()
            .withCaptureFailure(CaptureException.Kind.TIMEOUT);
        int port = startWith(new CaptureHandler(fake));
        HttpResponse<String> r = postJson(port, "{}");
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("\"error\":\"TIMEOUT\""));
    }

    @Test
    void rejectsNonPost() throws Exception {
        int port = startWith(new CaptureHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/capture")).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(405, r.statusCode());
    }

    @Test
    void rejectsInvalidTemplateFormat() throws Exception {
        int port = startWith(new CaptureHandler(new FakeMorfinDevice()));
        HttpResponse<String> r = postJson(port, "{\"templateFormat\":\"BOGUS\"}");
        assertEquals(400, r.statusCode());
    }
}
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
mvn test -Dtest=CaptureHandlerTest
```

Expected: compile error — `CaptureHandler` not defined.

- [ ] **Step 3: Implement `CaptureHandler`**

```java
package com.oau.nhif.morfin.agent.http;

import com.mantra.morfinauth.enums.TemplateFormat;
import com.oau.nhif.morfin.agent.device.CaptureException;
import com.oau.nhif.morfin.agent.device.CaptureResult;
import com.oau.nhif.morfin.agent.device.MorfinDevice;
import com.oau.nhif.morfin.agent.util.Json;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class CaptureHandler implements HttpHandler {
    private final MorfinDevice device;

    public CaptureHandler(MorfinDevice device) { this.device = device; }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }
        String body = readBody(ex.getRequestBody());
        Map<String, String> req = body.isEmpty()
            ? Map.of()
            : Json.readFlatObject(body);

        int timeoutMs = parseInt(req.get("timeoutMs"), 10000);
        int minQuality = parseInt(req.get("minQuality"), 60);
        String formatStr = req.getOrDefault("templateFormat", "ANSI_V378");

        TemplateFormat format;
        try { format = TemplateFormat.valueOf(formatStr); }
        catch (IllegalArgumentException iae) {
            reply(ex, 400, Map.of("error", "INVALID_TEMPLATE_FORMAT"));
            return;
        }

        Map<String, Object> out = new LinkedHashMap<>();
        try {
            CaptureResult r = device.capture(timeoutMs, minQuality, format);
            out.put("template", r.templateBase64());
            out.put("quality", r.quality());
            out.put("nfiq", r.nfiq());
            reply(ex, 200, out);
        } catch (CaptureException ce) {
            out.put("error", ce.kind().name());
            out.put("message", ce.getMessage());
            reply(ex, 200, out);
        }
    }

    private static int parseInt(String s, int fallback) {
        if (s == null || s.isEmpty()) return fallback;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return fallback; }
    }

    private static String readBody(InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
    }

    private static void reply(HttpExchange ex, int status, Map<String, ?> body) throws IOException {
        byte[] out = Json.writeObject(body).getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(status, out.length);
        ex.getResponseBody().write(out);
        ex.close();
    }
}
```

- [ ] **Step 4: Run the test and verify it passes**

```bash
mvn test -Dtest=CaptureHandlerTest
```

Expected: `Tests run: 5, Failures: 0`.

- [ ] **Step 5: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/src/
git commit -m "agent: add CaptureHandler"
```

---

### Task A8: `ShutdownHandler` (TDD)

Optional endpoint for installer uninstalls. Binds to localhost so it can't be triggered remotely.

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/http/ShutdownHandler.java`
- Create: `nhiftz-wrapper-morfin-agent/src/test/java/com/oau/nhif/morfin/agent/http/ShutdownHandlerTest.java`

- [ ] **Step 1: Write the failing test**

```java
package com.oau.nhif.morfin.agent.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ShutdownHandlerTest {

    private HttpServer server;

    @AfterEach
    void tearDown() { if (server != null) server.stop(0); }

    @Test
    void invokesShutdownCallbackOnPost() throws Exception {
        AtomicBoolean called = new AtomicBoolean(false);
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/shutdown", new ShutdownHandler(() -> called.set(true)));
        server.start();
        int port = server.getAddress().getPort();

        HttpResponse<String> r = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/shutdown"))
                .POST(HttpRequest.BodyPublishers.noBody()).build(),
            HttpResponse.BodyHandlers.ofString());
        assertEquals(200, r.statusCode());
        Thread.sleep(100);
        assertTrue(called.get());
    }
}
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
mvn test -Dtest=ShutdownHandlerTest
```

Expected: compile error.

- [ ] **Step 3: Implement `ShutdownHandler`**

```java
package com.oau.nhif.morfin.agent.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ShutdownHandler implements HttpHandler {
    private final Runnable callback;

    public ShutdownHandler(Runnable callback) { this.callback = callback; }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }
        byte[] body = "{\"shuttingDown\":true}".getBytes();
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(200, body.length);
        ex.getResponseBody().write(body);
        ex.close();
        new Thread(callback, "agent-shutdown").start();
    }
}
```

- [ ] **Step 4: Run the test and verify it passes**

```bash
mvn test -Dtest=ShutdownHandlerTest
```

Expected: `Tests run: 1, Failures: 0`.

- [ ] **Step 5: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/src/
git commit -m "agent: add ShutdownHandler"
```

---

### Task A9: `Main` — wire up the HTTP server

No tests (pure wiring). Verified via the smoke test in A10.

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/src/main/java/com/oau/nhif/morfin/agent/Main.java`

- [ ] **Step 1: Implement `Main`**

```java
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
```

- [ ] **Step 2: Compile**

```bash
cd nhiftz-wrapper-morfin-agent && mvn compile
```

Expected: `BUILD SUCCESS`.

- [ ] **Step 3: Run full tests**

```bash
mvn test
```

Expected: all A2/A3/A4/A6/A7/A8 tests pass.

- [ ] **Step 4: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/src/
git commit -m "agent: wire Main with HttpServer, CORS, shutdown hook"
```

---

### Task A10: Build the fat JAR and smoke test

**Files:**
- None new; exercises the shade plugin.

- [ ] **Step 1: Package**

```bash
cd nhiftz-wrapper-morfin-agent && mvn package -DskipTests
```

Expected: `target/morfin-agent-1.0.0.jar` created.

- [ ] **Step 2: Verify the shaded JAR contains Morfin classes and native libs**

```bash
unzip -l target/morfin-agent-1.0.0.jar | grep -E 'mantra/morfinauth/MorfinAuth\.class|win/x64/Morfin_Auth\.dll|linux/x86_64/libMorfin_Auth\.so'
```

Expected: at least three matching lines.

- [ ] **Step 3: Smoke test `/status` without a device**

Run agent in background:

```bash
"/c/Program Files/Java/jdk-22/bin/java" -jar target/morfin-agent-1.0.0.jar &
AGENT_PID=$!
sleep 3
curl -sS http://127.0.0.1:8765/status
```

Expected body: `{"ready":false,"deviceModel":null,"serial":null,"sdkVersion":null,"reason":"DEVICE_NOT_CONNECTED"}`

Kill the agent:

```bash
kill $AGENT_PID
```

- [ ] **Step 4: (If device available) Smoke test `/capture`**

Plug in MFS500. Rerun agent. Wait 3 seconds for init loop.

```bash
curl -sS http://127.0.0.1:8765/status
# expect "ready":true and a real serial
curl -sS -X POST -H 'Content-Type: application/json' \
  -d '{"timeoutMs":15000,"minQuality":60,"templateFormat":"ANSI_V378"}' \
  http://127.0.0.1:8765/capture
# place finger when prompted by the scanner LEDs; expect {"template":"...","quality":NN,"nfiq":N}
```

If no device: skip this step and mark it as "deferred to final integration test".

- [ ] **Step 5: Commit (nothing to commit — this task only verifies build output)**

```bash
echo "A10 complete: fat JAR builds and /status responds correctly"
```

---

### Task A11: Agent README

**Files:**
- Create: `nhiftz-wrapper-morfin-agent/README.md`

- [ ] **Step 1: Write the README**

```markdown
# Morfin Auth Local Agent

Runs on each reception PC. Exposes the Mantra MFS500 scanner to browsers
on that PC via `http://localhost:8765`. The central NHIF JSP webapp's
page fetches from this agent to get a fingerprint template.

## One-time setup

1. **Install the Mantra scanner driver** on the PC (separate installer
   from Mantra; not bundled here). Plug in the MFS500 and verify Windows
   Device Manager lists it under "Biometric devices".
2. **Install `Morfin_Auth.jar` to your local Maven repo** once (the build
   needs this to resolve the dependency):

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

Native libraries (Windows DLLs, Linux .so) are bundled inside
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

- `GET  /status`  — `{ready, deviceModel, serial, sdkVersion}` or `{ready:false, reason}`
- `POST /capture` — body `{timeoutMs, minQuality, templateFormat}`; returns `{template, quality, nfiq}` or `{error, message}`
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
```

- [ ] **Step 2: Commit**

```bash
cd .. && git add nhiftz-wrapper-morfin-agent/README.md
git commit -m "agent: document setup, build, run, auto-start"
```

---

## Part B — Central JSP Webapp

Repo path: `C:/project/nhifapi/nhifapiwrapper/nhiftz-wrapper-jsp-example/`

### Task B1: WAR module scaffold

**Files:**
- Create: `nhiftz-wrapper-jsp-example/pom.xml`
- Create: `nhiftz-wrapper-jsp-example/.gitignore`
- Create: `nhiftz-wrapper-jsp-example/src/main/webapp/WEB-INF/web.xml`

- [ ] **Step 1: Install the `nhiftz-wrapper` library to local Maven repo**

If not already done:

```bash
cd nhiftz-wrapper && mvn install -DskipTests
cd ..
```

Expected: `BUILD SUCCESS`.

- [ ] **Step 2: Create the WAR module `pom.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.oau.nhif</groupId>
    <artifactId>nhiftz-wrapper-jsp-example</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>

    <name>NHIF TZ Wrapper — JSP Example</name>
    <description>Reception-desk JSP webapp that authorizes NHIF cards
      via a fingerprint captured on the local Morfin agent.</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <junit.version>5.8.2</junit.version>
        <mockito.version>4.11.0</mockito.version>
    </properties>

    <dependencies>
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

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>nhiftz-wrapper-jsp-example</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <failOnMissingWebXml>true</failOnMissingWebXml>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0</version>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 3: Create `.gitignore`**

```
target/
*.iml
.idea/
nbproject/private/
```

- [ ] **Step 4: Create `web.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                             http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <display-name>NHIF TZ JSP Example</display-name>

    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

    <error-page>
        <exception-type>java.lang.Throwable</exception-type>
        <location>/error.jsp</location>
    </error-page>

    <context-param>
        <param-name>debug</param-name>
        <param-value>false</param-value>
    </context-param>
</web-app>
```

- [ ] **Step 5: Verify the scaffold packages**

```bash
cd nhiftz-wrapper-jsp-example && mvn package
```

Expected: `target/nhiftz-wrapper-jsp-example.war` created, `BUILD SUCCESS`.

- [ ] **Step 6: Commit**

```bash
cd .. && git add nhiftz-wrapper-jsp-example/
git commit -m "webapp: scaffold nhiftz-wrapper-jsp-example WAR module"
```

---

### Task B2: `NhifClientContextListener` (TDD)

Reads env vars, builds one `NhifApiClient`, stashes it in `ServletContext`.

**Files:**
- Create: `nhiftz-wrapper-jsp-example/src/main/java/com/oau/nhif/jspexample/web/NhifClientContextListener.java`
- Create: `nhiftz-wrapper-jsp-example/src/main/java/com/oau/nhif/jspexample/web/NhifConfig.java`
- Create: `nhiftz-wrapper-jsp-example/src/test/java/com/oau/nhif/jspexample/web/NhifConfigTest.java`

- [ ] **Step 1: Write the failing test**

We test the pure `NhifConfig.fromEnv(Function<String,String>)` — the
listener itself is trivial wiring and not unit-tested.

```java
package com.oau.nhif.jspexample.web;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class NhifConfigTest {

    @Test
    void buildsConfigFromCompleteEnv() {
        Map<String, String> env = Map.of(
            "NHIF_AUTH_URL",      "https://test.nhif.or.tz",
            "NHIF_SERVICE_URL",   "https://test.nhif.or.tz/servicehub",
            "NHIF_CLIENT_ID",     "11014",
            "NHIF_CLIENT_SECRET", "secret==",
            "NHIF_USERNAME",      "Mtundi"
        );
        NhifConfig cfg = NhifConfig.fromEnv(env::get).orElseThrow();
        assertEquals("https://test.nhif.or.tz", cfg.authUrl());
        assertEquals("11014", cfg.clientId());
        assertEquals("Mtundi", cfg.username());
    }

    @Test
    void returnsEmptyWhenAnyRequiredVarMissing() {
        Map<String, String> env = Map.of(
            "NHIF_AUTH_URL", "https://test.nhif.or.tz",
            "NHIF_CLIENT_ID", "11014"
        );
        assertTrue(NhifConfig.fromEnv(env::get).isEmpty());
    }

    @Test
    void emptyStringTreatedAsMissing() {
        Map<String, String> env = Map.of(
            "NHIF_AUTH_URL",      "https://test.nhif.or.tz",
            "NHIF_SERVICE_URL",   "https://test.nhif.or.tz/servicehub",
            "NHIF_CLIENT_ID",     "",
            "NHIF_CLIENT_SECRET", "secret==",
            "NHIF_USERNAME",      "Mtundi"
        );
        assertTrue(NhifConfig.fromEnv(env::get).isEmpty());
    }
}
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
cd nhiftz-wrapper-jsp-example && mvn test -Dtest=NhifConfigTest
```

Expected: compile error.

- [ ] **Step 3: Implement `NhifConfig`**

```java
package com.oau.nhif.jspexample.web;

import java.util.Optional;
import java.util.function.Function;

public record NhifConfig(
    String authUrl,
    String serviceUrl,
    String clientId,
    String clientSecret,
    String username
) {
    public static Optional<NhifConfig> fromEnv(Function<String, String> lookup) {
        String auth = val(lookup, "NHIF_AUTH_URL");
        String svc = val(lookup, "NHIF_SERVICE_URL");
        String cid = val(lookup, "NHIF_CLIENT_ID");
        String sec = val(lookup, "NHIF_CLIENT_SECRET");
        String usr = val(lookup, "NHIF_USERNAME");
        if (auth == null || svc == null || cid == null || sec == null || usr == null)
            return Optional.empty();
        return Optional.of(new NhifConfig(auth, svc, cid, sec, usr));
    }

    private static String val(Function<String, String> lookup, String key) {
        String v = lookup.apply(key);
        return (v == null || v.isEmpty()) ? null : v;
    }
}
```

- [ ] **Step 4: Implement the listener (no unit tests — wiring only)**

```java
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
        Optional<NhifConfig> maybeConfig = NhifConfig.fromEnv(System::getenv);
        if (maybeConfig.isEmpty()) {
            LOG.severe("NHIF_* environment variables not set — /health will report unconfigured");
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
```

- [ ] **Step 5: Run the test and verify it passes**

```bash
mvn test -Dtest=NhifConfigTest
```

Expected: `Tests run: 3, Failures: 0`.

- [ ] **Step 6: Commit**

```bash
cd .. && git add nhiftz-wrapper-jsp-example/src/
git commit -m "webapp: add NhifConfig and ServletContext listener"
```

---

### Task B3: `HealthServlet` (TDD)

**Files:**
- Create: `nhiftz-wrapper-jsp-example/src/main/java/com/oau/nhif/jspexample/web/HealthServlet.java`
- Create: `nhiftz-wrapper-jsp-example/src/test/java/com/oau/nhif/jspexample/web/HealthServletTest.java`

- [ ] **Step 1: Write the failing test**

```java
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
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
mvn test -Dtest=HealthServletTest
```

Expected: compile error.

- [ ] **Step 3: Implement `HealthServlet`**

```java
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
```

- [ ] **Step 4: Run the test and verify it passes**

```bash
mvn test -Dtest=HealthServletTest
```

Expected: `Tests run: 2, Failures: 0`.

- [ ] **Step 5: Commit**

```bash
cd .. && git add nhiftz-wrapper-jsp-example/src/
git commit -m "webapp: add HealthServlet"
```

---

### Task B4: `VisitTypeCache` (TDD)

Populates the visit-type dropdown. One hour TTL.

**Files:**
- Create: `nhiftz-wrapper-jsp-example/src/main/java/com/oau/nhif/jspexample/web/VisitTypeCache.java`
- Create: `nhiftz-wrapper-jsp-example/src/test/java/com/oau/nhif/jspexample/web/VisitTypeCacheTest.java`

- [ ] **Step 1: Write the failing test**

`VisitType` (in `nhiftz-wrapper`) uses a no-arg constructor plus setters,
and `getVisitTypeID()` returns `Integer` (not `int`). Our tests and
fallback list build instances accordingly.

```java
package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.model.VisitType;
import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class VisitTypeCacheTest {

    private static VisitType vt(int id, String name) {
        VisitType v = new VisitType();
        v.setVisitTypeID(id);
        v.setVisitTypeName(name);
        return v;
    }

    @Test
    void fetchesOnceWithinTtl() {
        AtomicInteger calls = new AtomicInteger();
        VisitTypeCache cache = new VisitTypeCache(() -> {
            calls.incrementAndGet();
            return CompletableFuture.completedFuture(List.of(vt(1, "Normal")));
        }, Clock.systemUTC(), Duration.ofHours(1));

        assertEquals(1, cache.get().size());
        assertEquals(1, cache.get().size());
        assertEquals(1, calls.get());
    }

    @Test
    void refreshesAfterTtl() {
        AtomicInteger calls = new AtomicInteger();
        Instant start = Instant.parse("2026-04-20T00:00:00Z");
        Clock[] nowBox = { Clock.fixed(start, ZoneOffset.UTC) };
        Clock later = Clock.fixed(start.plusSeconds(3601), ZoneOffset.UTC);
        Clock clock = new Clock() {
            @Override public Instant instant() { return nowBox[0].instant(); }
            @Override public java.time.ZoneId getZone() { return ZoneOffset.UTC; }
            @Override public Clock withZone(java.time.ZoneId z) { return this; }
        };
        VisitTypeCache cache = new VisitTypeCache(() -> {
            calls.incrementAndGet();
            return CompletableFuture.completedFuture(List.of(vt(1, "Normal")));
        }, clock, Duration.ofHours(1));

        cache.get();
        nowBox[0] = later;
        cache.get();
        assertEquals(2, calls.get());
    }

    @Test
    void fallsBackToHardcodedOnFetchFailure() {
        VisitTypeCache cache = new VisitTypeCache(() -> {
            CompletableFuture<List<VisitType>> f = new CompletableFuture<>();
            f.completeExceptionally(new RuntimeException("boom"));
            return f;
        }, Clock.systemUTC(), Duration.ofHours(1));

        List<VisitType> result = cache.get();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getVisitTypeID().intValue());
        assertEquals("Normal", result.get(0).getVisitTypeName());
    }
}
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
mvn test -Dtest=VisitTypeCacheTest
```

Expected: compile error.

- [ ] **Step 3: Implement `VisitTypeCache`**

```java
package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.model.VisitType;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisitTypeCache {
    private static final Logger LOG = Logger.getLogger(VisitTypeCache.class.getName());

    private static List<VisitType> fallback() {
        VisitType v = new VisitType();
        v.setVisitTypeID(1);
        v.setVisitTypeName("Normal");
        return List.of(v);
    }
    private static final List<VisitType> FALLBACK = fallback();

    private final Supplier<CompletableFuture<List<VisitType>>> source;
    private final Clock clock;
    private final Duration ttl;

    private volatile List<VisitType> cached;
    private volatile Instant expiresAt = Instant.MIN;

    public VisitTypeCache(Supplier<CompletableFuture<List<VisitType>>> source,
                          Clock clock, Duration ttl) {
        this.source = source;
        this.clock = clock;
        this.ttl = ttl;
    }

    public synchronized List<VisitType> get() {
        if (cached != null && clock.instant().isBefore(expiresAt)) return cached;
        try {
            cached = source.get().get();
            expiresAt = clock.instant().plus(ttl);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "getVisitTypes failed, using fallback", e);
            cached = FALLBACK;
            expiresAt = clock.instant().plus(Duration.ofMinutes(5));
        }
        return cached;
    }
}
```

- [ ] **Step 4: Run the test and verify it passes**

```bash
mvn test -Dtest=VisitTypeCacheTest
```

Expected: `Tests run: 3, Failures: 0`.

- [ ] **Step 5: Commit**

```bash
cd .. && git add nhiftz-wrapper-jsp-example/src/
git commit -m "webapp: add VisitTypeCache with TTL and fallback"
```

---

### Task B5: `AuthorizeServlet` (TDD)

**Files:**
- Create: `nhiftz-wrapper-jsp-example/src/main/java/com/oau/nhif/jspexample/web/AuthorizeServlet.java`
- Create: `nhiftz-wrapper-jsp-example/src/test/java/com/oau/nhif/jspexample/web/AuthorizeServletTest.java`

- [ ] **Step 1: Write the failing test**

```java
package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.model.CardAuthorizationRequest;
import com.oau.nhif.client.model.CardAuthorizationResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizeServletTest {

    private HttpServletRequest mockReq(
            String cardNo, String fpCode, String visitTypeID, String imageData,
            NhifApiClient client) {
        HttpServletRequest req = mock(HttpServletRequest.class);
        ServletContext ctx = mock(ServletContext.class);
        when(req.getServletContext()).thenReturn(ctx);
        when(ctx.getAttribute(NhifClientContextListener.CLIENT_ATTR)).thenReturn(client);
        when(req.getParameter("cardNo")).thenReturn(cardNo);
        when(req.getParameter("fpCode")).thenReturn(fpCode);
        when(req.getParameter("visitTypeID")).thenReturn(visitTypeID);
        when(req.getParameter("imageData")).thenReturn(imageData);
        when(req.getParameter("referralNo")).thenReturn("");
        when(req.getParameter("remarks")).thenReturn("Biometric verified visit");
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(anyString())).thenReturn(rd);
        return req;
    }

    @Test
    void forwardsToResultOnApproval() throws Exception {
        NhifApiClient client = mock(NhifApiClient.class);
        CardAuthorizationResponse resp = new CardAuthorizationResponse();
        resp.setAuthorizationStatus("APPROVED");
        resp.setAuthorizationNo("AUTH-123");
        resp.setFullName("Jane Doe");
        when(client.authorizeCardWithBiometric(any()))
            .thenReturn(CompletableFuture.completedFuture(resp));

        HttpServletRequest req = mockReq("101502314766", "R_INDEX", "1", "BASE64DATA==", client);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);

        new AuthorizeServlet().doPost(req, httpResp);

        ArgumentCaptor<CardAuthorizationRequest> cap = ArgumentCaptor.forClass(CardAuthorizationRequest.class);
        verify(client).authorizeCardWithBiometric(cap.capture());
        assertEquals("101502314766", cap.getValue().getCardNo());
        assertEquals("FINGERPRINT",  cap.getValue().getBiometricMethod());
        assertEquals("R_INDEX",      cap.getValue().getFpCode());
        assertEquals("BASE64DATA==", cap.getValue().getImageData());
        assertEquals(1,              cap.getValue().getVisitTypeID());

        verify(req).setAttribute("response", resp);
        verify(req).getRequestDispatcher("/result.jsp");
    }

    @Test
    void returns503WhenClientNotConfigured() throws Exception {
        HttpServletRequest req = mockReq("101502314766", "R_INDEX", "1", "BASE64DATA==", null);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);

        new AuthorizeServlet().doPost(req, httpResp);

        verify(httpResp).sendError(eq(503), contains("NHIF_"));
    }

    @Test
    void returns400OnMissingImageData() throws Exception {
        NhifApiClient client = mock(NhifApiClient.class);
        HttpServletRequest req = mockReq("101502314766", "R_INDEX", "1", "", client);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);

        new AuthorizeServlet().doPost(req, httpResp);

        verify(httpResp).sendError(eq(400), contains("imageData"));
        verify(client, never()).authorizeCardWithBiometric(any());
    }

    @Test
    void returns400OnUnknownFpCode() throws Exception {
        NhifApiClient client = mock(NhifApiClient.class);
        HttpServletRequest req = mockReq("101502314766", "THUMB", "1", "BASE64DATA==", client);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);

        new AuthorizeServlet().doPost(req, httpResp);

        verify(httpResp).sendError(eq(400), contains("fpCode"));
    }
}
```

- [ ] **Step 2: Run the test and verify it fails**

```bash
mvn test -Dtest=AuthorizeServletTest
```

Expected: compile error — `AuthorizeServlet` not defined.

- [ ] **Step 3: Implement `AuthorizeServlet`**

```java
package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.model.CardAuthorizationRequest;
import com.oau.nhif.client.model.CardAuthorizationResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/authorize")
public class AuthorizeServlet extends HttpServlet {

    private static final List<String> VALID_FP_CODES = List.of(
        "R_THUMB", "R_INDEX", "R_MIDDLE", "R_RING", "R_LITTLE",
        "L_THUMB", "L_INDEX", "L_MIDDLE", "L_RING", "L_LITTLE"
    );

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        NhifApiClient client = (NhifApiClient) req.getServletContext().getAttribute(NhifClientContextListener.CLIENT_ATTR);
        if (client == null) {
            resp.sendError(503, "Server not configured — check NHIF_* env vars");
            return;
        }

        String cardNo     = req.getParameter("cardNo");
        String fpCode     = req.getParameter("fpCode");
        String imageData  = req.getParameter("imageData");
        String visitIdStr = req.getParameter("visitTypeID");
        String referralNo = req.getParameter("referralNo");
        String remarks    = req.getParameter("remarks");

        if (imageData == null || imageData.isEmpty()) {
            resp.sendError(400, "missing imageData (did the local scanner capture fail?)");
            return;
        }
        if (!VALID_FP_CODES.contains(fpCode)) {
            resp.sendError(400, "invalid fpCode: " + fpCode);
            return;
        }
        int visitTypeID;
        try { visitTypeID = Integer.parseInt(visitIdStr); }
        catch (Exception e) { resp.sendError(400, "invalid visitTypeID"); return; }

        CardAuthorizationRequest request = new CardAuthorizationRequest()
            .cardNo(cardNo)
            .biometricMethod("FINGERPRINT")
            .fpCode(fpCode)
            .imageData(imageData)
            .visitTypeID(visitTypeID)
            .referralNo(referralNo == null ? "" : referralNo)
            .remarks(remarks == null ? "" : remarks);

        try {
            CardAuthorizationResponse response = client.authorizeCardWithBiometric(request).get();
            req.setAttribute("response", response);
            req.getRequestDispatcher("/result.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("exception", e);
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
```

NOTE: if `CardAuthorizationRequest`'s fluent methods aren't named exactly
as above, verify against `nhiftz-wrapper/src/main/java/com/oau/nhif/client/model/CardAuthorizationRequest.java`
and adjust the builder chain. The method signatures were confirmed
against commit `b8ca307` of the library.

- [ ] **Step 4: Run the test and verify it passes**

```bash
mvn test -Dtest=AuthorizeServletTest
```

Expected: `Tests run: 4, Failures: 0`.

- [ ] **Step 5: Commit**

```bash
cd .. && git add nhiftz-wrapper-jsp-example/src/
git commit -m "webapp: add AuthorizeServlet"
```

---

### Task B6: JSP pages and stylesheet

Manual verification only — JSPs render correctly when deployed to Tomcat.

**Files:**
- Create: `nhiftz-wrapper-jsp-example/src/main/webapp/index.jsp`
- Create: `nhiftz-wrapper-jsp-example/src/main/webapp/result.jsp`
- Create: `nhiftz-wrapper-jsp-example/src/main/webapp/error.jsp`
- Create: `nhiftz-wrapper-jsp-example/src/main/webapp/css/app.css`
- Create: `nhiftz-wrapper-jsp-example/src/main/java/com/oau/nhif/jspexample/web/IndexServlet.java`
- Create: `nhiftz-wrapper-jsp-example/src/main/java/com/oau/nhif/jspexample/web/VisitTypeCacheProvider.java`

- [ ] **Step 1: Create `VisitTypeCacheProvider` to hand the cache to JSPs**

```java
package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.NhifApiClient;

import javax.servlet.ServletContext;
import java.time.Clock;
import java.time.Duration;

public final class VisitTypeCacheProvider {
    private VisitTypeCacheProvider() {}

    public static final String ATTR = "visitTypeCache";

    public static VisitTypeCache get(ServletContext ctx) {
        VisitTypeCache existing = (VisitTypeCache) ctx.getAttribute(ATTR);
        if (existing != null) return existing;

        NhifApiClient client = (NhifApiClient) ctx.getAttribute(NhifClientContextListener.CLIENT_ATTR);
        if (client == null) return null;

        VisitTypeCache cache = new VisitTypeCache(
            () -> {
                try { return client.getVisitTypes(); }
                catch (Exception e) {
                    java.util.concurrent.CompletableFuture<java.util.List<com.oau.nhif.client.model.VisitType>> f
                        = new java.util.concurrent.CompletableFuture<>();
                    f.completeExceptionally(e);
                    return f;
                }
            }, Clock.systemUTC(), Duration.ofHours(1));
        ctx.setAttribute(ATTR, cache);
        return cache;
    }
}
```

- [ ] **Step 2: Create `IndexServlet` to populate JSP model**

```java
package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.model.VisitType;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class IndexServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        VisitTypeCache cache = VisitTypeCacheProvider.get(req.getServletContext());
        List<VisitType> visitTypes = (cache == null) ? List.of() : cache.get();
        req.setAttribute("visitTypes", visitTypes);
        req.setAttribute("fpCodes", List.of(
            "R_THUMB", "R_INDEX", "R_MIDDLE", "R_RING", "R_LITTLE",
            "L_THUMB", "L_INDEX", "L_MIDDLE", "L_RING", "L_LITTLE"));
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
```

Move the JSP to `WEB-INF/jsp/` so it is only served through the servlet.

- [ ] **Step 3: Create `WEB-INF/jsp/index.jsp`**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>NHIF Card Authorization</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<main class="card">
    <h1>NHIF card authorization</h1>

    <div id="scanner-status" class="status-banner loading">Checking scanner…</div>

    <form id="auth-form" method="post" action="${pageContext.request.contextPath}/authorize">
        <label>Card number
            <input type="text" name="cardNo" required pattern="[0-9]+" placeholder="e.g. 101502314766">
        </label>

        <label>Finger
            <select name="fpCode" required>
                <c:forEach var="f" items="${fpCodes}">
                    <option value="${f}"><c:out value="${f}"/></option>
                </c:forEach>
            </select>
        </label>

        <label>Visit type
            <select name="visitTypeID" required>
                <c:choose>
                    <c:when test="${not empty visitTypes}">
                        <c:forEach var="v" items="${visitTypes}">
                            <option value="${v.visitTypeID}"><c:out value="${v.visitTypeName}"/></option>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <option value="1">Normal</option>
                    </c:otherwise>
                </c:choose>
            </select>
        </label>

        <label>Referral number (optional)
            <input type="text" name="referralNo">
        </label>

        <label>Remarks
            <input type="text" name="remarks" value="Biometric verified visit">
        </label>

        <input type="hidden" name="imageData" id="imageData">

        <button type="button" id="capture-btn">Capture & Authorize</button>
    </form>
</main>
<script src="${pageContext.request.contextPath}/js/capture.js"></script>
</body>
</html>
```

- [ ] **Step 4: Create `result.jsp`**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Authorization result</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<main class="card">
    <c:choose>
        <c:when test="${response.authorizationStatus eq 'APPROVED'}">
            <div class="banner approved">APPROVED</div>
        </c:when>
        <c:otherwise>
            <div class="banner rejected">REJECTED</div>
        </c:otherwise>
    </c:choose>

    <dl class="details">
        <dt>Authorization #</dt><dd><c:out value="${response.authorizationNo}"/></dd>
        <dt>Patient</dt>       <dd><c:out value="${response.fullName}"/></dd>
        <dt>Member #</dt>      <dd><c:out value="${response.membershipNo}"/></dd>
        <dt>Scheme</dt>        <dd><c:out value="${response.schemeName}"/></dd>
        <dt>DOB</dt>           <dd><c:out value="${response.dateOfBirth}"/></dd>
        <dt>Expires</dt>       <dd><c:out value="${response.expiryDate}"/></dd>
        <c:if test="${not empty response.statusDescription}">
          <dt>Note</dt>        <dd><c:out value="${response.statusDescription}"/></dd>
        </c:if>
    </dl>

    <a class="button" href="${pageContext.request.contextPath}/">New authorization</a>
</main>
</body>
</html>
```

- [ ] **Step 5: Create `error.jsp`**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<main class="card">
    <div class="banner rejected">Error</div>
    <p><c:out value="${requestScope.exception.message}"/></p>
    <c:if test="${initParam.debug eq 'true'}">
        <pre><%
            Throwable t = (Throwable) request.getAttribute("exception");
            if (t != null) t.printStackTrace(new java.io.PrintWriter(out));
        %></pre>
    </c:if>
    <a class="button" href="${pageContext.request.contextPath}/">Back</a>
</main>
</body>
</html>
```

- [ ] **Step 6: Create `css/app.css`**

```css
* { box-sizing: border-box; }
body { font: 16px/1.4 system-ui, sans-serif; background: #f5f6f8; margin: 0; padding: 2rem; }
.card { max-width: 520px; margin: 0 auto; background: #fff; padding: 1.5rem;
        border-radius: 8px; box-shadow: 0 1px 4px rgba(0,0,0,.08); }
h1 { margin-top: 0; font-size: 1.25rem; }
label { display: block; margin-bottom: .9rem; }
label input, label select { display: block; width: 100%; margin-top: .25rem;
        padding: .5rem .6rem; font: inherit; border: 1px solid #ccd; border-radius: 4px; }
button, .button { display: inline-block; background: #2b6cb0; color: #fff;
        padding: .6rem 1rem; border: 0; border-radius: 4px; font: inherit;
        text-decoration: none; cursor: pointer; }
button:disabled { background: #9bb3ce; cursor: not-allowed; }
.status-banner { padding: .5rem .75rem; margin-bottom: 1rem; border-radius: 4px; font-size: .9rem; }
.status-banner.loading { background: #fff4cc; color: #6b4b00; }
.status-banner.ready   { background: #d4efd4; color: #16541c; }
.status-banner.error   { background: #fbdcdc; color: #7a1d1d; }
.banner { padding: 1rem; font-weight: bold; color: #fff; text-align: center;
        border-radius: 4px; margin-bottom: 1rem; }
.banner.approved { background: #2b8a3e; }
.banner.rejected { background: #b91c1c; }
.details { display: grid; grid-template-columns: auto 1fr; column-gap: 1rem; row-gap: .25rem; }
.details dt { color: #555; }
.details dd { margin: 0; }
pre { background: #eef; padding: .5rem; overflow: auto; font-size: .8rem; }
```

- [ ] **Step 7: Package and verify WAR contents**

```bash
cd nhiftz-wrapper-jsp-example && mvn package -DskipTests
unzip -l target/nhiftz-wrapper-jsp-example.war | grep -E 'index\.jsp|result\.jsp|error\.jsp|app\.css'
```

Expected: all four listed.

- [ ] **Step 8: Commit**

```bash
cd .. && git add nhiftz-wrapper-jsp-example/src/
git commit -m "webapp: add JSP pages, CSS, and IndexServlet"
```

---

### Task B7: `capture.js` — browser-side scanner bridge

Manual-only verification. This file is what the receptionist's browser
runs to talk to the local agent.

**Files:**
- Create: `nhiftz-wrapper-jsp-example/src/main/webapp/js/capture.js`

- [ ] **Step 1: Implement `capture.js`**

```javascript
(function () {
  const AGENT_BASE = "http://127.0.0.1:8765";
  const statusEl   = document.getElementById("scanner-status");
  const button     = document.getElementById("capture-btn");
  const form       = document.getElementById("auth-form");
  const imageData  = document.getElementById("imageData");

  function setBanner(cls, text) {
    statusEl.className = "status-banner " + cls;
    statusEl.textContent = text;
  }

  async function probeAgent() {
    try {
      const ctrl = new AbortController();
      const t = setTimeout(() => ctrl.abort(), 1500);
      const r = await fetch(AGENT_BASE + "/status", { signal: ctrl.signal });
      clearTimeout(t);
      const body = await r.json();
      if (body.ready) {
        setBanner("ready", "Scanner ready — " + body.deviceModel + " (s/n " + body.serial + ")");
        button.disabled = false;
      } else {
        setBanner("error", "Scanner not ready: " + (body.reason || "unknown"));
        button.disabled = true;
      }
    } catch (e) {
      setBanner("error", "Cannot reach local scanner service (agent not running?)");
      button.disabled = true;
    }
  }

  async function captureAndSubmit() {
    button.disabled = true;
    setBanner("loading", "Place finger on scanner…");
    try {
      const r = await fetch(AGENT_BASE + "/capture", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ timeoutMs: 15000, minQuality: 60, templateFormat: "ANSI_V378" })
      });
      const body = await r.json();
      if (body.error) {
        setBanner("error", "Capture failed: " + body.error + (body.message ? " — " + body.message : ""));
        button.disabled = false;
        return;
      }
      imageData.value = body.template;
      setBanner("ready", "Captured (quality " + body.quality + ", nfiq " + body.nfiq + "). Submitting…");
      form.submit();
    } catch (e) {
      setBanner("error", "Capture error: " + e.message);
      button.disabled = false;
    }
  }

  button.addEventListener("click", captureAndSubmit);
  probeAgent();
})();
```

- [ ] **Step 2: Rebuild and verify the JS is in the WAR**

```bash
cd nhiftz-wrapper-jsp-example && mvn package -DskipTests
unzip -p target/nhiftz-wrapper-jsp-example.war js/capture.js | head -5
```

Expected: first line `(function () {`.

- [ ] **Step 3: Commit**

```bash
cd .. && git add nhiftz-wrapper-jsp-example/src/main/webapp/js/capture.js
git commit -m "webapp: add browser-side scanner bridge capture.js"
```

---

### Task B8: Webapp README

**Files:**
- Create: `nhiftz-wrapper-jsp-example/README.md`

- [ ] **Step 1: Write the README**

```markdown
# NHIF TZ Wrapper — JSP Example

Reception-desk JSP webapp that authorizes NHIF cards using a fingerprint
captured on the client PC via the Morfin Agent sibling module.

## Architecture

```
Browser  ──────────────────  https://nhif.hospital.local/nhiftz-wrapper-jsp-example/
   │
   └── fetch  ──  http://127.0.0.1:8765  (local Morfin agent on the same PC)
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
   the WAR was built against. Tomcat needs no extra JARs — all deps are in
   `WEB-INF/lib/`.
2. Copy the WAR to `$CATALINA_HOME/webapps/`.
3. Tomcat 8.5.87 auto-expands it. Browse to
   `https://your-server/nhiftz-wrapper-jsp-example/`.

## Endpoints

- `GET  /`           — reception form (`index.jsp`)
- `POST /authorize`  — processes the form, calls NHIF, forwards to `result.jsp`
- `GET  /health`     — JSON status: `{wrapperConfigured, authUrl, serviceUrl, clientId}`

## Security

- Run Tomcat behind HTTPS in production.
- `NHIF_CLIENT_SECRET` must not be logged; only the webapp should read it.
- The fingerprint template (`imageData`) is never logged.
```

- [ ] **Step 2: Commit**

```bash
cd .. && git add nhiftz-wrapper-jsp-example/README.md
git commit -m "webapp: document configuration, build, deploy"
```

---

## Part C — End-to-end verification

### Task C1: Full manual integration test

No files created. Exercises both modules together against the NHIF test
environment. This task is deferred until a physical MFS500 is available.

- [ ] **Step 1: Start the agent on the dev Windows box (scanner attached)**

```bash
cd nhiftz-wrapper-morfin-agent
"/c/Program Files/Java/jdk-22/bin/java" -jar target/morfin-agent-1.0.0.jar &
sleep 3
curl -sS http://127.0.0.1:8765/status
```

Expected: `{"ready":true,"deviceModel":"MFS500","serial":"…","sdkVersion":"…"}`.

- [ ] **Step 2: Set env vars and start Tomcat 8.5.87 on the dev box**

Set the five `NHIF_*` env vars per the README, then start Tomcat and
deploy the WAR to `webapps/`.

- [ ] **Step 3: Verify `/health` shows configured**

```bash
curl -sS http://localhost:8080/nhiftz-wrapper-jsp-example/health
```

Expected: `{"wrapperConfigured":true,"authUrl":"https://test.nhif.or.tz",...}`.

- [ ] **Step 4: Walk the happy path in a browser**

Open `http://localhost:8080/nhiftz-wrapper-jsp-example/`.

Confirm:
- Green "Scanner ready" banner within 2 seconds
- Card number `101502314766` (test card from CLAUDE.md)
- `R_INDEX` selected
- Normal visit
- Click "Capture & Authorize"
- Scanner LED prompts for finger; place finger
- Page forwards to `result.jsp` with `APPROVED` or a NHIF-issued rejection
- Patient name and scheme rendered

- [ ] **Step 5: Walk the unplugged-scanner path**

Unplug MFS500. Reload `index.jsp`. Confirm red "Cannot reach local
scanner service" banner and the capture button is disabled.

- [ ] **Step 6: Walk the CentOS cross-origin path**

Deploy the WAR to the CentOS staging Tomcat 8.5.87. From a Windows PC
with the agent running, load the CentOS URL in Chrome. Confirm the JS
reaches `http://127.0.0.1:8765` despite the page origin being the CentOS
server — this proves the mixed-content/localhost secure-context behaviour
that the spec relies on.

- [ ] **Step 7: Tag the release**

```bash
git tag -a mfs500-jsp-example-1.0.0 -m "MFS500 JSP example shipped"
echo "End-to-end verified against NHIF test environment"
```
