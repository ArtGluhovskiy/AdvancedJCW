<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="container-fluid">
    <div class="row-fluid">
        <!--Header-->
        <header>
            <nav class="navbar navbar-default menu">
                <div class="container">
                    <!-- Brand and toggle get grouped for better mobile display -->
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                                data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="${pageContext.request.contextPath}/main"><img src="${pageContext.request.contextPath}/assets/img/my_jcw_logo.png" height="150" width="150"></a>
                    </div>

                    <!-- Collect the nav links, forms, and other content for toggling -->
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul class="nav navbar-nav">
                            <li><a href="${pageContext.request.contextPath}/main"><i class="fa fa-home" aria-hidden="true"></i>  <spring:message code="nav.home"/></a></li>
                            <c:if test="${user.role ne 'admin'}">
                                <li><a href="${pageContext.request.contextPath}/statistics"><spring:message code="nav.statistics"/></a></li>
                            </c:if>
                            <li><a href="${pageContext.request.contextPath}/rating"><spring:message code="nav.rating"/></a></li>
                            <c:if test="${user.role ne 'admin'}">
                                <li class="active-link"><a href="${pageContext.request.contextPath}/application"><spring:message code="nav.application"/></a></li>
                            </c:if>
                            <c:if test="${user.role eq 'admin'}">
                                <li class="active-link"><a href="${pageContext.request.contextPath}/admin"><spring:message code="nav.admin"/></a></li>
                            </c:if>

                        </ul>
                        <ul class="nav navbar-nav navbar-right user-hello icon">
                            <c:if test="${not empty sessionScope.user.login}">
                            <li><spring:message code="nav.hello"/>, ${user.FName}</li>
                            <li><a href="${pageContext.request.contextPath}/logout"><spring:message code="nav.logout"/></a></li>
                            </c:if>

                            <li><button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fa fa-globe" aria-hidden="true"></i>
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <c:url var="path" value="${pageName}"></c:url>
                                <li><a href="${path}?locale=ru">ru</a></li>
                                <li><a href="${path}?locale=en">en</a></li>
                            </ul>
                            </li>
                            <li id="btn-battleship">
                                <a id="battle" href="${pageContext.request.contextPath}/battleship"><i class="fa fa-ship" aria-hidden="true"></i></a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
    </div>
</div>

