# NHIF API Wrapper - Distribution Summary

## Overview
This document summarizes the distribution configuration for the NHIF API Wrapper library, designed for compatibility with Java 1.8 and GlassFish server environments.

## Build Artifacts

After running `mvn clean package`, the following artifacts are generated:

### 1. Regular Library JAR
- **File**: `target/nhiftz-wrapper-1.0-SNAPSHOT.jar`
- **Purpose**: For use as a library dependency in other projects
- **Contents**: Only your project classes (no dependencies)
- **Use Case**: Ant builds, Maven dependencies, manual classpath management

### 2. Fat JAR (Executable)
- **File**: `target/nhiftz-wrapper-1.0-SNAPSHOT-jar-with-dependencies.jar`
- **Purpose**: Standalone executable with all dependencies included
- **Contents**: Your project classes + all runtime dependencies
- **Use Case**: Direct execution, single-file deployment

### 3. Dependencies Folder
- **Location**: `target/lib/`
- **Purpose**: All runtime dependencies as separate JAR files
- **Contents**: Jackson, SLF4J, Logback, Apache HttpClient, etc.
- **Use Case**: Ant projects that need granular dependency control

### 4. Source JAR
- **File**: `target/nhiftz-wrapper-1.0-SNAPSHOT-sources.jar`
- **Purpose**: Source code for IDE integration and debugging
- **Contents**: All Java source files

### 5. Javadoc JAR
- **File**: `target/nhiftz-wrapper-1.0-SNAPSHOT-javadoc.jar`
- **Purpose**: API documentation
- **Contents**: Generated Javadoc HTML files

## Java Compatibility

### Target Environments
- **Java Version**: 1.8 (Java 8) - **FULLY COMPATIBLE**
- **Server Compatibility**: GlassFish 3.x, 4.x, 5.x
- **JSP Pages**: Compatible with Java EE 6, 7, 8

### Java 1.8 Compatibility Fixes Applied
- ✅ Replaced Java 11+ HTTP Client with Apache HttpClient 4.5.14
- ✅ Replaced Java 9+ `CompletableFuture` factory methods with manual creation
- ✅ Replaced Java 11+ `Files.readString()/writeString()` with byte array methods
- ✅ Fixed Jackson `PropertyNamingStrategies` to use Java 1.8 compatible version
- ✅ Replaced `List.of()` with `Arrays.asList()` and `Collections.emptyList()`
- ✅ Used explicit `TypeReference<T>` instead of diamond operator with anonymous classes

### Dependencies (Java 1.8 Compatible Versions)
- Jackson: 2.12.7 (Java 1.8 compatible)
- SLF4J: 1.7.36 (Java 1.8 compatible)
- Logback: 1.2.12 (Java 1.8 compatible)
- Apache HttpClient: 4.5.14 (replaces Java 11+ HTTP Client)
- Lombok: 1.18.24 (Java 1.8 compatible)

## Usage in Different Build Systems

### Ant Projects
1. Copy `nhiftz-wrapper-1.0-SNAPSHOT.jar` to your project
2. Copy all JARs from `target/lib/` to your project's lib folder
3. Add all JARs to your Ant build classpath

Example Ant build.xml snippet:
```xml
<path id="project.classpath">
    <fileset dir="lib">
        <include name="*.jar"/>
    </fileset>
    <fileset dir="lib/nhif">
        <include name="nhiftz-wrapper-1.0-SNAPSHOT.jar"/>
    </fileset>
</path>
```

### Maven Projects
Add to pom.xml:
```xml
<dependency>
    <groupId>com.oau.nhifapi</groupId>
    <artifactId>nhiftz-wrapper</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### GlassFish Deployment
- Use the regular JAR (`nhiftz-wrapper-1.0-SNAPSHOT.jar`) in your WAR's `WEB-INF/lib/`
- Include dependency JARs from `target/lib/` in the same location
- Or use the fat JAR if you want everything in one file

### JSP Pages
After including the JAR in your project, you can use the API:
```jsp
<%@ page import="com.oau.nhif.client.NhifApiClient" %>
<%
    NhifApiClient client = new NhifApiClient("your-api-key");
    // Use the client...
%>
```

## Build Commands

```bash
# Clean and build all artifacts
mvn clean package

# Build without tests
mvn clean package -DskipTests

# Install to local Maven repository
mvn clean install
```

## Package Structure
- **Main Classes**: `com.oau.nhif.client.*`
- **Models**: `com.oau.nhif.model.*`
- **Exceptions**: `com.oau.nhif.exception.*`
- **Examples**: `com.oau.nhif.example.*`

## Notes
- All dependencies are included in the fat JAR for standalone use
- The regular JAR requires all dependencies to be on the classpath
- Source and Javadoc JARs are generated for development convenience
- The project is configured for maximum compatibility with legacy Java environments