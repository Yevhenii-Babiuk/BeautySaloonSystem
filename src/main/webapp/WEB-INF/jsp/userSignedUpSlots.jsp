<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty sessionScope.language}">
    <c:set var="language" value="${applicationScope.language}" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="textContent"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="userCabinet.signedUpSlots"/></title>
</head>
<body>

<div><c:import url="userControl.jsp"/></div>

<div>

    <%--Printing slot sign up result--%>
    <c:if test="${not empty actionResult}">
        <div class="d-flex justify-content-center">
            <h4><fmt:message key="${actionResult}"/></h4>
        </div>
    </c:if>

    <div class="d-flex justify-content-center my-md-5">
        <h1><fmt:message key="userCabinet.signedUpSlots"/></h1>
    </div>

    <c:choose>
        <c:when test="${not empty slots}">
            <div class="bg-semi-transparent">
                <table class="table table-hover table-sm">
                    <tr>
                        <th class="text-center"><fmt:message key="slotSearch.result.canceling"/></th>
                        <th class="text-center"><fmt:message key="slotSearch.result.date"/></th>
                        <th class="text-center"><fmt:message key="slotSearch.result.time"/></th>
                        <th class="text-center"><fmt:message key="slotSearch.result.master"/></th>
                        <th class="text-center"><fmt:message key="slotSearch.result.procedure"/></th>
                        <th class="text-center"><fmt:message key="slotSearch.result.feedback"/></th>
                    </tr>


                    <c:forEach var="slot" items="${slots}">
                        <tr>
                            <td>
                                <form action="${contextPath}/user/cancelSignedUpSlot.do" method="post">
                                    <input type="text" name="slotId" value="${slot.slot.id}" hidden>
                                    <button class="btn brown-button"><fmt:message
                                            key="slotSearch.result.canceling"/></button>
                                </form>
                            </td>
                            <td>
                                    ${slot.slot.date}
                            </td>
                            <td>
                                    ${slot.slot.startTime} - ${slot.slot.endTime}
                            </td>
                            <td>
                                    ${slot.master.firstName} ${slot.master.lastName}
                            </td>
                            <td>
                                <c:if test="${language eq 'en'}">
                                    ${slot.procedure.nameEn}
                                </c:if>
                                <c:if test="${language eq 'ru'}">
                                    ${slot.procedure.nameRus}
                                </c:if>
                                <c:if test="${language eq 'ua'}">
                                    ${slot.procedure.nameUkr}
                                </c:if>
                            </td>

                            <c:if test="${not empty slot.feedback.text}">
                                <td style="text-align: left">
                                        ${slot.feedback.text}
                                </td>
                            </c:if>
                            <c:if test="${empty slot.feedback.text}">
                                <td>
                                    <form action="${contextPath}/user/addFeedback" method="post">
                                        <input type="text" name="slotId" value="${slot.slot.id}" hidden>
                                        <button class="btn brown-button"><fmt:message
                                                key="slotSearch.result.addFeedback"/></button>
                                    </form>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <br>
            <%------------------------- Pagination part -------------------------------%>

            <nav>
                <ul class="pagination justify-content-center">
                        <%--For displaying Previous link except for the 1st page --%>
                    <c:choose>
                        <c:when test="${currentPage != 1}">
                            <li class="page-item">
                                <a class="page-link text-dark"
                                   href="${contextPath}/user/signedUpSlots?page=${currentPage - 1} "><fmt:message
                                        key="pagination.previous"/> </a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="page-item disabled">
                                <a class="page-link text-dark"
                                   href="${contextPath}/user/signedUpSlots?page=${currentPage - 1}"><fmt:message
                                        key="pagination.previous"/> </a>
                            </li>
                        </c:otherwise>
                    </c:choose>

                        <%--For displaying Page numbers.
                        The when condition does not display a link for the current page--%>

                    <c:forEach begin="1" end="${pagesQuantity}" var="i">
                        <c:choose>
                            <c:when test="${currentPage eq i}">
                                <li class="page-item">
                                    <div class="page-link text-dark">${i}</div>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link text-dark"
                                                         href="${contextPath}/user/signedUpSlots?page=${i}">${i}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>


                        <%--For displaying Next link --%>
                    <c:choose>
                        <c:when test="${currentPage lt pagesQuantity}">
                            <li class="page-item">
                                <a class="page-link text-dark"
                                   href="${contextPath}/user/signedUpSlots?page=${currentPage + 1}"><fmt:message
                                        key="pagination.next"/> </a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="page-item disabled">
                                <a class="page-link text-dark"
                                   href="${contextPath}/user/signedUpSlots?page=${currentPage + 1}"><fmt:message
                                        key="pagination.next"/> </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </nav>
        </c:when>
        <c:otherwise>
            <div class="d-flex justify-content-center">
                <h3><fmt:message key="noData"/></h3>
            </div>
        </c:otherwise>
    </c:choose>

</div>

</body>
</html>
