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

    <div id="scanner-status" class="status-banner loading">Checking scanner...</div>

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

        <button type="button" id="capture-btn" disabled>Capture &amp; Authorize</button>
    </form>
</main>
<script src="${pageContext.request.contextPath}/js/capture.js"></script>
</body>
</html>
