<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--Welcome-->
<div class="container container_user">
    <div class="content_user">
        <div class="welcome_user">
            <h2><i class="fa fa-smile-o" aria-hidden="true"></i> Welcome, ${user.FName} ${user.LName}</h2>
        </div>
    </div>
</div>

<div class="container">
    <div class="col-lg-4 col-md-4 user_box1">
        <h4><i class="fa fa-user-circle" aria-hidden="true"></i> User information:</h4>
        <image id="avatar-src" src="${pageContext.request.contextPath}/displayImage?image=${user.login}.jpg"/>
            <br/>
        <p><i class="fa fa-circle-o" aria-hidden="true"></i> Your ID: ${user.userID}</p>
        <p><i class="fa fa-circle-o" aria-hidden="true"></i> Login: ${user.login}</p>
        <p><i class="fa fa-circle-o" aria-hidden="true"></i> Your clan: ${user.clanName}</p>
        <p><i class="fa fa-circle-o" aria-hidden="true"></i> Age: ${user.age}</p>
        <p><i class="fa fa-circle-o" aria-hidden="true"></i> Email: ${user.email}</p>
        <p><i class="fa fa-circle-o" aria-hidden="true"></i> Registration date: ${user.regDate}</p>
        <p><i class="fa fa-circle-o" aria-hidden="true"></i> Level: ${user.level}</p>
        <p><i class="fa fa-circle-o" aria-hidden="true"></i> Your rating: ${user.rating}</p>
    </div>

    <!--Table-->
    <div class="col-lg-8 col-md-8 user_box2">
        <h4><i class="fa fa-window-restore" aria-hidden="true"></i> Task orders:</h4>
        <div class="place_message">
            <c:if test="${not empty message}">
                <h4>${message}</h4>
            </c:if>
        </div>

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

        <%--<%for (int i = 0; i < 5; i++) {--%>
            <%--out.println("Hello from JRebel!!!");--%>
        <%--}%>--%>

        <form class="form_user" action="${pageContext.request.contextPath}/application" method="post">
            <input class="dws_submit_next_task" type="submit" name="submit"
                   value="<c:choose><c:when test="${not empty errorMsg}">GET MY FIRST TASK</c:when><c:otherwise>TO THE NEXT TASK</c:otherwise></c:choose>"/>
        </form>
    </div>
</div>

