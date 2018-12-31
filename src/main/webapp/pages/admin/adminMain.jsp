<%--Created by Yaroslav Bodyak on 11.12.2018--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="spec" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="/WEB-INF/tlds/tag.tld" prefix="myTag" %>
<html>
<head>
    <title>Admin page</title>
    <link rel="stylesheet" type="text/css" href="<spec:url value="/css/adminMain.css"/>"/>

</head>
<body>
<div class="wrapperWelcomeInfo">
    <div class="welcomeElement">
        Administration service of TIMETRACKER.

        Hello ADMIN, <c:out value="${sessionScope.adminName.firstName} ${sessionScope.adminName.surName}"/>!
    </div>
</div>
<%--Table overview users activity--%>
<div class="wrapperPageData">
    <fieldset>
        <legend align="center">OVERVIEW USERS ACTIVITIES</legend>
        <div class="activityInfoForm">
            <table>
                <col width="200">
                <col width="100">
                <col width="200">
                <col width="230">
                <tr>
                    <th align="left">USERS</th>
                    <th>ACTIVITIES</th>
                    <th>REQUEST FROM CLIENT</th>
                    <th align="left">NOTICE</th>
                </tr>
                <c:forEach items="${sessionScope.userList}" var="user">
                    <c:if test="${user.userType.userType=='client'}">
                        <tr>
                            <td>
                                <c:out value="${user.firstName} ${user.surName}"/>
                            </td>
                            <td>
                                <form class="formElement" name="actionForm" method="POST"
                                      action="controller">
                                    <div class="wrapperButtons">
                                        <input type="hidden" name="userId" value="${user.userId}"/>
                                        <input type="hidden" name="command" value="overviewClient"/>
                                        <input class="buttonElement" type="submit" value="overview"
                                               style="height:20px; width:70px"/>
                                    </div>
                                </form>
                            </td>

                            <td>
                                <table>
                                    <tr>
                                        <td>
                                            <c:set var="flag" value="false"/>
                                            <c:if test="${user.requestAdd=='true'}">
                                                <button class="mockButton red">add new activity</button>
                                                <c:set var="flag" value="true"/>
                                            </c:if>
                                            <c:if test="${flag == 'false'}">
                                                <button class="mockButton blue">add new activity</button>
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:set var="flag" value="false"/>
                                            <c:forEach items="${sessionScope.trackingList}" var="tracking">
                                                <c:if test="${tracking.user.surName==user.surName &&
                                                 tracking.userRequest == 'REMOVE'}">
                                                    <c:set var="flag" value="true"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:choose>
                                                <c:when test="${flag == 'true'}">
                                                    <button class="mockButton red">remove finished activity</button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button class="mockButton blue">remove finished activity</button>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td>
                                <c:set var="flag" value="false"/>
                                <c:forEach items="${sessionScope.trackingList}" var="tracking">
                                    <c:if test="${tracking.user.userId==user.userId && (tracking.userRequest == 'REMOVE'||
                                    tracking.userRequest == 'ADD' || user.requestAdd == 'true')}">
                                        <c:set var="flag" value="true"/>
                                    </c:if>
                                </c:forEach>
                                <c:choose>
                                    <c:when test="${flag == 'true'}">
                                        client waiting for response...
                                    </c:when>
                                    <c:otherwise>
                                        no request from client
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
    </fieldset>
    <%--Table available activity--%>
    <div class="wrapperTableActivityAdmin">
        <fieldset>
            <legend align="center">AVAILABLE ACTIVITIES</legend>
            <div class="activityInfoForm">
                <table style=width:330px>
                    <col width="100">
                    <tr>
                        <td width="350" align="left">
                            <form class="formElement" name="actionForm" method="POST"
                                  action="controller">
                                <div class="wrapperButtons">
                                    <input type="hidden" name="command" value="createActivity"/>
                                    <input class="buttonElement" type="submit" value="add new activity"
                                           style="height:20px; width:110px"/>
                                    <input class="inputElement" type="text" name="activityName" value=""
                                           style="height:20px; width:220px"/>
                                </div>
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:forEach items="${sessionScope.activityAdminList}" var="activity">
                                ${activity.activityName}<br>
                            </c:forEach>
                        </td>
                    </tr>
                </table>
                <table style=width:330px>
                    <tr>
                        <td>
                            <div>
                                </br>${operationMessage}
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Custom Tag: <myTag:getSizeActivityList/>
                        </td>
                    </tr>
                </table>
            </div>
        </fieldset>
    </div>
</div>

<!--LOGOUT-->
<div class="logoutElement">
    <form name="logout" method="POST" action="controller">
        <input type="hidden" name="command" value="logout"/>
        <input type="submit" value="Log Out"/>
    </form>
</div>
</body>
</html>