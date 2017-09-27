<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<!--Welcome-->
<div class="container container_user">
    <div class="content_user">
        <div class="welcome_user">
            <h2><i class="fa fa-user-circle-o" aria-hidden="true"></i>  Admin page</h2>
        </div>
    </div>
</div>


<div class="container tasks">

    <!--Table-->
    <table class="table tasks_table">
        <h4>Java task table:</h4>
        <c:if test="${not empty requestScope.errorMsg}"><h4 class="error">${requestScope.errorMsg}</h4></c:if>
        <thead align="right">
        <tr>
            <th>Task ID</th>
            <th>Difficulty level</th>
            <th>Short description</th>
            <th>Execution time, mcs</th>
            <th>Popularity</th>
            <th>Registration date</th>
        </tr>
        </thead>

        <c:forEach items="${taskList}" var="task">
            <tr>
                <td align="left">${task.taskID}</td>
                <td align="left">${task.difficultyGroup}</td>
                <td align="left">${task.shortDescr}</td>
                <td align="left">${task.elapsedTime}</td>
                <td align="left">${task.popularity}</td>
                <td align="left">${task.regDate}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<div class="container">
    <form class="form_admin" action="frontController?command=admin" method="post">
        <h4 style="color: #d9d9d9;">Update task by ID:</h4>
        <c:if test="${not empty requestScope.updateErrorMsg}">
            <h4 class="error">${requestScope.updateErrorMsg}</h4>
        </c:if>
        <c:if test="${not empty requestScope.updateStatusMsg}">
            <h4 class="status">${requestScope.updateStatusMsg}</h4>
        </c:if>
        <div class="dws-input">
            <input type="text" name="taskID" placeholder="Task ID">
        </div>
        <div class="dws-input">
            <select size="1" name="diffGroup">
                <option disabled>Select difficulty group:</option>
                <option selected value="BEGINNER">BEGINNER</option>
                <option value="EXPERIENCED">EXPERIENCED</option>
                <option value="EXPERT">EXPERT</option>
            </select></p>
        </div>
        <div class="dws-input">
            <input type="text" name="shortDescr" placeholder="Short description">
        </div>
        <div class="dws-input">
            <input type="text" name="elapsedTime" placeholder="Execution time">
        </div>
        <div class="dws-input">
            <input type="text" name="popularity" placeholder="Popularity">
        </div>
        <input class="dws-submit" type="submit" name="submit" value="UPDATE TASK">
        <br/>
    </form>
</div>

<div class="container">
    <form class="form_admin" action="frontController?command=admin" method="post">
        <h4 style="color: #d9d9d9;">Show user info:</h4>
        <div class="dws-input">
            <input type="text" name="userID" placeholder="User ID">
        </div>
        <input class="dws-submit" type="submit" name="submit" value="USER ID">
        <br/>
    </form>
</div>

<div class="container tasks">
    <table class="table tasks_table">
        <h4>User info:</h4>
        <c:if test="${not empty requestScope.userErrorMsg}"><h4 class="error">${requestScope.userErrorMsg}</h4></c:if>
        <thead align="right">
        <tr>
            <th>User ID</th>
            <th>Login</th>
            <th>Level</th>
            <th>Rating</th>
            <th>Age</th>
            <th>Registration date</th>
            <th>Status</th>
            <th>Role</th>
            <th>Email</th>
            <th>Clan name</th>
        </tr>
        </thead>
            <tr>
                <td align="left">${userInfo.userID}</td>
                <td align="left">${userInfo.login}</td>
                <td align="left">${userInfo.level}</td>
                <td align="left">${userInfo.rating}</td>
                <td align="left">${userInfo.age}</td>
                <td align="left">${userInfo.regDate}</td>
                <td align="left">${userInfo.status}</td>
                <td align="left">${userInfo.role}</td>
                <td align="left">${userInfo.email}</td>
                <td align="left">${userInfo.clanName}</td>
            </tr>
    </table>

    <h4>User task orders:</h4>
    <br>
    <table class="table table-condensed my_table">
        <tr class="table_head">
            <th>Order ID</th>
            <th>Level</th>
            <th>Popularity</th>
            <th>Short description</th>
            <th>Registration date</th>
            <th>Execution time, mcs</th>
            <th>Recommended exec. time, mcs</th>
            <th>Order status</th>
        </tr>

        <c:forEach items="${orderList}" var="orderDTO">
            <tr class="${orderDTO.orderStatus}">
                <td>${orderDTO.orderID}</td>
                <td align="left">${orderDTO.diffGroup}</td>
                <td>${orderDTO.taskPopularity}</td>
                <td align="left" class="small_column">${orderDTO.shortDesc}</td>
                <td class="small_column">${orderDTO.regDate}</td>
                <td>${orderDTO.execTime}</td>
                <td>${orderDTO.elapsedTime}</td>
                <td>${orderDTO.orderStatus}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<div class="container">
    <form class="form_admin" action="frontController?command=admin" method="post">
        <h4 style="color: #d9d9d9;">Update user by ID:</h4>
        <c:if test="${not empty requestScope.userUpdateErrorMsg}">
            <h4 class="error">${requestScope.userUpdateErrorMsg}</h4>
        </c:if>
        <c:if test="${not empty requestScope.userUpdateStatusMsg}">
            <h4 class="status">${requestScope.userUpdateStatusMsg}</h4>
        </c:if>
        <div class="dws-input">
            <input type="text" name="updateUserID" placeholder="User ID">
        </div>
        <div class="dws-input">
            <select size="1" name="level">
                <option disabled>Select level:</option>
                <option selected value="BEGINNER">BEGINNER</option>
                <option value="EXPERIENCED">EXPERIENCED</option>
                <option value="EXPERT">EXPERT</option>
            </select></p>
        </div>
        <div class="dws-input">
            <input type="text" name="role" placeholder="user/admin">
        </div>
        <div class="dws-input">
            <input type="text" name="status" placeholder="Change status">
        </div>
        <div class="dws-input">
            <input type="text" name="rating" placeholder="User rating">
        </div>
        <input class="dws-submit" type="submit" name="submit" value="UPDATE USER">
        <br/>

    </form>
</div>

</body>
</html>
