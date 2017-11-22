<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

    <fmt:setLocale value="${sessionScope.locale}"/>
    <fmt:setBundle basename="messages" var="i18n"/>
    <title><fmt:message bundle="${i18n}" key="${title}"/></title>

    <!-- Bootstrap -->
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link href="assets/css/jsw_style.css" rel="stylesheet">
    <link href="assets/css/font-awesome.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans|Paytone+One|Ropa+Sans" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <link rel="shortcut icon" href="../../../favicon.ico" type="image/x-icon">
</head>

<body>

<%@include file="../common/header_unlog.jsp" %>

<c:choose>
    <c:when test="${title eq 'main.title'}">
        <%@include file="../main_page/main.jsp" %>
    </c:when>
    <c:when test="${title eq 'login.title'}">
        <%@include file="../login/main.jsp" %>
    </c:when>
    <c:when test="${title eq 'application.title'}">
        <%@include file="../application/application.jsp" %>
    </c:when>
    <c:when test="${title eq 'Result_success'}">
        <%@include file="../compilation_results/result_success.jsp" %>
    </c:when>
    <c:when test="${title eq 'Result_fail'}">
        <%@include file="../compilation_results/result_fail.jsp" %>
    </c:when>
    <c:when test="${title eq 'statistics.title'}">
        <%@include file="../statistics/statistics.jsp" %>
    </c:when>
    <c:when test="${title eq 'rating.title'}">
        <%@include file="../rating/rating.jsp" %>
    </c:when>
    <c:when test="${title eq 'admin.title'}">
        <%@include file="../admin/admin.jsp" %>
    </c:when>
</c:choose>

<%@include file="../common/footer.jsp" %>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="assets/js/jquery-3.2.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="assets/js/bootstrap.min.js"></script>
<script>
    $('.carousel').carousel({
        interval: 3000
    });
</script>
</body>
</html>
