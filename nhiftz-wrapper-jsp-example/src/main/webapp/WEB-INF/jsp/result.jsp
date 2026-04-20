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
