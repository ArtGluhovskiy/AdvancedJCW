<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
        <%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

        <%--<fmt:setLocale value="${sessionScope.locale}"/>--%>
        <%--<fmt:setBundle basename="messages" var="i18n"/>--%>
        <%--<title><fmt:message bundle="${i18n}" key="${title}"/></title>--%>
        <title><spring:message code="main.title"/></title>

        <!-- Bootstrap -->
        <link href="${pageContext.request.contextPath}/assets/css/bootstrap.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/jsw_style.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/font-awesome.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/battleship.css" rel="stylesheet" >
        <link href="https://fonts.googleapis.com/css?family=Open+Sans|Paytone+One|Ropa+Sans" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Inconsolata" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <link rel="shortcut icon" href="../../../favicon.ico" type="image/x-icon">
    </head>

    <body>

        <script>
            var contextPath = '${pageContext.request.contextPath}';
        </script>

        <tiles:insertAttribute name="header"/>
        <tiles:insertAttribute name="body"/>
        <tiles:insertAttribute name="footer"/>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/jquery-3.2.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/utils.js"></script>
        <script>
            $('.carousel').carousel({
                interval: 3000
            });
        </script>
    </body>
</html>
