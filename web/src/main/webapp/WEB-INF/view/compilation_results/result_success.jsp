<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>
<div class="container">
    <form class="result_form_success" action="frontController?command=application" method="post">
        <i class="fa fa-thumbs-up fa-5x" aria-hidden="true"></i>
        <div class="result_success">
            <h2><i class="fa fa-child fa-2x" aria-hidden="true"></i> The task was successfully completed!</h2>
            <h3>Execution time of your algorithm: ${requestScope.elapsedTime} mcs</h3>
            <h3>

                <c:choose>
                    <c:when test="${requestScope.elapsedTime le sessionScope.task.elapsedTime}">
                        It's OK!
                    </c:when>
                </c:choose>

            </h3>
        </div>
        <input class="dws-submit" type="submit" name="submit" value="GO TO THE NEXT TASK">
    </form>
</div>
</body>
</html>
