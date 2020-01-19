<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>

<fmt:setLocale value="${sessionScope.language}" />
<fmt:setBundle basename="textContent" />

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="${contextPath}/resources/css/bootstrap.css" type='text/css'>
<link rel="stylesheet" href="${contextPath}/resources/css/bootstrap-grid.css" type='text/css'>
<link rel="stylesheet" href="${contextPath}/resources/css/SaloonSystem.css" type='text/css'>
<script src="${contextPath}/resources/js/jquery-3.4.1.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>


<!--Navbar-->
<nav class="navbar navbar-expand-lg navbar-custom">

    <a class="navbar-brand" href="${contextPath}/title">
        <img src="${contextPath}/resources/img/saloon-icon.png" width="30" height="30" alt="">
    </a>

    <button class="navbar-toggler orange-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <div class="navbar-nav mr-auto">
            <c:choose>
                <c:when test="${sessionScope.loggedInUser != null}">
                        <span id="navbar-span" class="navbar-text">
                            ${sessionScope.loggedInUser.firstName} ${sessionScope.loggedInUser.lastName}
                        </span>

                    <a class="nav-item nav-link" href="${contextPath}/cabinet">
                        <fmt:message key="header.cabinet"/>
                    </a>


                    <a class="nav-item nav-link" href="${contextPath}/logout" onclick="return confirm('<fmt:message key="logout.confirm"/>')">
                        <fmt:message key="logout.button"/>
                    </a>

                </c:when>
                <c:otherwise>

                    <a class="nav-item nav-link" href="${contextPath}/login">
                        <fmt:message key="header.login"/>
                    </a>

                </c:otherwise>
            </c:choose>
        </div>

        <div class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                ${language}
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                <c:forEach var="languageOption" items="${applicationScope.languages}">
                    <c:if test="${sessionScope.language ne languageOption.code}">
                        <a class="dropdown-item" href="${contextPath}/changeLanguage?chosenLanguage=${languageOption}">${languageOption.name}</a>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>

</nav>