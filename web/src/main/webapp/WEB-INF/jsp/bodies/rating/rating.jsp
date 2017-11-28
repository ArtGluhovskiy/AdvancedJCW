<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--Welcome-->
<div class="container container_user">
    <div class="content_user">
        <div class="welcome_user">
            <h2><i class="fa fa-users" aria-hidden="true"></i>  Users top-10 list</h2>
        </div>
    </div>
</div>

<div class="container rating">

    <!--Table-->
    <table class="table rating_table">
        <thead align="right">
        <tr>
            <th>Rating</th>
            <th>User</th>
            <th>Clan</th>
            <th>Rating</th>
        </tr>
        </thead>

        <c:forEach items="${topList}" var="user">
        <tr>
            <td align="left">${user.rating}</td>
            <td align="left">${user.login}</td>
            <td align="left">${user.clanName}</td>
            <td align="left"><div class="rating_line" style="width: ${user.rating}0px; text-align: left;">${user.rating}</div></td>
        </tr>

        </c:forEach>
    </table>

</div>

