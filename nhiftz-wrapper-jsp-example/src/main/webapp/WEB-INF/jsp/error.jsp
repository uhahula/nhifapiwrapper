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
