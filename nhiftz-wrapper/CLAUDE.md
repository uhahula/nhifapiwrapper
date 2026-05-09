# NHIF API Wrapper - Development Guide

## Build Commands

Maven daemon (mvnd) has issues with JAVA_HOME on this system. Use embedded Maven directly:

### Quick compile
```bash
JAVA_HOME="/c/Program Files/Java/jdk-22" "/c/Program Files/Java/jdk-22/bin/java" -classpath "/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/boot/plexus-classworlds-2.8.0.jar" -Dclassworlds.conf="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/bin/m2.conf" -Dmaven.home="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn" -Dmaven.multiModuleProjectDirectory="." org.codehaus.plexus.classworlds.launcher.Launcher compile
```

### Build all JARs (compile + package + javadoc + sources + fat JAR)
```bash
JAVA_HOME="/c/Program Files/Java/jdk-22" "/c/Program Files/Java/jdk-22/bin/java" -classpath "/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/boot/plexus-classworlds-2.8.0.jar" -Dclassworlds.conf="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/bin/m2.conf" -Dmaven.home="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn" -Dmaven.multiModuleProjectDirectory="." org.codehaus.plexus.classworlds.launcher.Launcher package -DskipTests
```

### Run an example class
```bash
JAVA_HOME="/c/Program Files/Java/jdk-22" "/c/Program Files/Java/jdk-22/bin/java" -classpath "/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/boot/plexus-classworlds-2.8.0.jar" -Dclassworlds.conf="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/bin/m2.conf" -Dmaven.home="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn" -Dmaven.multiModuleProjectDirectory="." org.codehaus.plexus.classworlds.launcher.Launcher compile exec:java -Dexec.mainClass="com.oau.nhif.example.ClassName"
```

### Install to local Maven repo
```bash
JAVA_HOME="/c/Program Files/Java/jdk-22" "/c/Program Files/Java/jdk-22/bin/java" -classpath "/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/boot/plexus-classworlds-2.8.0.jar" -Dclassworlds.conf="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn/bin/m2.conf" -Dmaven.home="/c/tools/maven-mvnd-1.0.2-windows-amd64/mvn" -Dmaven.multiModuleProjectDirectory="." org.codehaus.plexus.classworlds.launcher.Launcher install -DskipTests
```

## Output Artifacts
JARs go to `target/`:
- `nhiftz-wrapper-<version>.jar` — library JAR
- `nhiftz-wrapper-<version>-jar-with-dependencies.jar` — fat JAR with all deps
- `nhiftz-wrapper-<version>-javadoc.jar` — Javadoc
- `nhiftz-wrapper-<version>-sources.jar` — Source code

## Test Environment
- Auth URL: `https://test.nhif.or.tz`
- Service URL: `https://test.nhif.or.tz/servicehub`
- Client ID: `11014`, Username: `Mtundi`

## Key Notes
- `VerifyCardRequest.verifierID` and `cardTypeID` must be Integer (not String)
- VerifyCard (`/api/Verification/VerifyCard`) is now NHIF's blessed authorize path.
  Use `client.verifyCardForAuthorization(VerifyCardRequest)` — returns the same
  `CardAuthorizationResponse` shape as AuthorizeCard. The older
  `verifyCard(...) -> CardVerification` overload is kept for backward compat.
  (NHIF previously had a NullReferenceException on this endpoint; fixed
  per Dickson 2026-05-09.)
- Card verifier list comes from `/api/Verification/GetCardVerifiers`
  (`client.getCardVerifiers()`). Use it to populate verifier dropdowns.
- NHIF wants the BIOMETRIC IMAGE in `imageData`, not the fingerprint
  template. WSQ is recommended; PNG is allowed but not natively produced
  by the Mantra MFS500 SDK (would require a RAW->PNG conversion step).
- Token is cached to `~/.nhif_token.json` and auto-refreshed
