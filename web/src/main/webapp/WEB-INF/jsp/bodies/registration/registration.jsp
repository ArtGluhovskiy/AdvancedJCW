<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags/form" %>

<!--Welcome-->
<div class="container">
    <div class="row" style="text-align: center;">
        <div class="content">
            <div class="slogan">
                <h1>Fill in the registration form</h1>
            </div>
        </div>
    </div>
</div>

<div class="container">

    <%--<script>--%>
        <%--var contextPath = ${pageContext.request.contextPath};--%>
    <%--</script>--%>

    <%--<!--Form-->--%>
    <%--<form class="registration regform" action="${pageContext.request.contextPath}/registration"--%>
          <%--method="post">--%>
        <%--<img src="${pageContext.request.contextPath}/assets/img/male-circle.png">--%>
        <%--<div class="place_error">--%>
            <%--<c:if test="${not empty errorMsg}">--%>
                <%--<div class="error"><p>${errorMsg}</p></div>--%>
            <%--</c:if>--%>
        <%--</div>--%>
        <%--<div class="dws-input">--%>
            <%--<input type="text" name="login" placeholder="Enter login" maxlength="30" class="input_login">--%>
        <%--</div>--%>
        <%--<div class="dws-input">--%>
            <%--<input type="text" name="fname" placeholder="First name" maxlength="20" class="input_fname">--%>
        <%--</div>--%>
        <%--<div class="dws-input">--%>
            <%--<input type="text" name="lname" placeholder="Last name" maxlength="20" class="input_lname">--%>
        <%--</div>--%>
        <%--<div class="dws-input">--%>
            <%--<input type="text" name="birth" placeholder="Birth date: DD-MM-YYYY" class="input_date">--%>
        <%--</div>--%>
        <%--<div class="dws-input">--%>
            <%--<input type="text" name="email" placeholder="Your email" maxlength="30" class="input_email">--%>
        <%--</div>--%>
        <%--<div class="dws-input">--%>
            <%--<input type="text" name="clan_name" placeholder="Clan name" maxlength="20" class="input_clan_name">--%>
        <%--</div>--%>
        <%--<div class="dws-input">--%>
            <%--<input type="password" name="password" placeholder="Enter password" maxlength="20" class="input_password">--%>
        <%--</div>--%>
        <%--<input class="dws-submit" type="submit" name="submit" value="ENTER">--%>
    <%--</form>--%>


        <!--Form 2.0-->
        <s:form class="registration regform" name="user" modelAttribute="user" action="${pageContext.request.contextPath}/registration"
              method="post" enctype="multipart/form-data">
            <img src="${pageContext.request.contextPath}/assets/img/male-circle.png">
            <div class="place_error">
                <s:errors path="login"/>
                <s:errors path="FName"/>
                <s:errors path="LName"/>
                <s:errors path="birth"/>
                <s:errors path="email"/>
                <s:errors path="clanName"/>
                <s:errors path="password"/>
                <c:if test="${not empty errorMsg}">
                    <div class="error"><p>${errorMsg}</p></div>
                </c:if>
            </div>
                <fieldset>
                    <div class="dws-input">
                        <s:input id="login" type="text" path="login" name="login" placeholder="Enter login" maxlength="30" class="input_login"/>
                    </div>
                    <div class="dws-input">
                        <s:input id="fname" type="text" path="FName" name="fname" placeholder="First name" maxlength="20" class="input_fname"/>
                    </div>
                    <div class="dws-input">
                        <s:input id="lname" type="text" path="LName" name="lname" placeholder="Last name" maxlength="20" class="input_lname"/>
                    </div>
                    <div class="dws-input">
                        <s:input id="birth" type="text" path="birth" name="birth" placeholder="Birth date: DD-MM-YYYY" class="input_date"/>
                    </div>
                    <div class="dws-input">
                        <s:input id="email" type="text" path="email" name="email" placeholder="Your email" maxlength="30" class="input_email"/>
                    </div>
                    <div class="dws-input">
                        <s:input id="clan_name" type="text" path="clanName" name="clan_name" placeholder="Clan name" maxlength="20" class="input_clan_name"/>
                    </div>
                    <div class="dws-input">
                        <s:input id="password" type="password" path="password" name="password" placeholder="Enter password" maxlength="20" class="input_password"/>
                    </div>
                    <div class="dws-input avatar">
                        <label for="avatar">Avatar:</label>
                        <input name="avatar" type="file"/>
                    </div>
                    <input class="dws-submit" type="submit" name="submit" value="ENTER">
                </fieldset>
        </s:form>
</div>


