<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="content">
            <div class="short_task_description">
                <h3>${sessionScope.task.shortDescr}</h3>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <div class="row">
        <div class="col-lg-6 col-md-6 description">
            <div class="area_name">Task description:</div>
            <br>
            <div class="task_description">
                <p>${sessionScope.task.description}</p>
            </div>
            <div class="area_name">Task information:</div>
            <br>
            <div class="task_information">
                &#8226; Group of difficulty:  *** ${sessionScope.task.difficultyGroup} ***<br>
                &#8226; Execution time (for test sample):  *** ${sessionScope.task.elapsedTime} ms ***<br>
                &#8226; Task value:  *** ${sessionScope.task.value} ***<br>
                &#8226; Task popularity:  *** ${sessionScope.task.popularity} ***<br>
                &#8226; Topics:  *** ${sessionScope.task.topics} ***<br>
            </div>
        </div>

        <div class="col-lg-6 col-md-6 col-sm-4 col-xs-2 code_field">
            <div class="area_name"><i class="fa fa-code" aria-hidden="true"></i> Code area. Type your code here:</div>
            <br>
            <c:choose>
                <c:when test="${empty sessionScope.code}">
                    <textarea form="my_form" name="code">
public class ${sessionScope.task.className} {
        ${sessionScope.task.methodString}

                    // *** Algorithm implementation ***
        }
}
                </c:when>
                <c:otherwise>
                    <textarea form="my_form" name="code">
${sessionScope.code}
                    </c:otherwise></c:choose>
            </textarea>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-6 col-md-6 output">
            <div class="area_name">Personal information:</div>
            <br>
            <div class="user_information">
                <p>&#8226; Age:  ${sessionScope.user.age}</p>
                <p>&#8226; Your ID:  ${sessionScope.user.userID}</p>
                <p>&#8226; Login:  ${sessionScope.user.login}</p>
                <p>&#8226; Your rating:  ${sessionScope.user.rating}</p>
                <p>&#8226; Registration date:  ${sessionScope.user.regDate}</p>
            </div>
            <form action="frontController?command=compiler" method="post" id="my_form">
                <input type="submit" class="submit" value="Get results">
            </form>
        </div>

        <div class="col-lg-6 col-md-6 col-sm-4 col-xs-2 test_example">
            <div class="area_name">Here your can see a test sample for this task:</div>
            <br>
            <div class="test_description">
                ${sessionScope.task.testInfo}
            </div>
        </div>
    </div>
</div>
</body>
</html>
