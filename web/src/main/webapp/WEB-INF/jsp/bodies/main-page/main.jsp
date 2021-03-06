<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!--Welcome-->
<div class="container">
    <div class="row">
        <div class="content">
            <div class="slogan">
                <h2>Welcome to Java Code Wars</h2>

                <!-- For JRebel test -->
                <%--<h2>Hello from JRebel: ${jrebel}</h2>--%>
                <%--<h2>Page name: ${pageName}</h2>--%>

            </div>
        </div>
    </div>
</div>

<div class="container">
    <!--Carousel-->
    <div class="col-lg-8 col-md-8 col-sm-6 col-xs-4">
        <div id="locations carousel slide" class="carousel slide" data-ride="carousel">
            <div class="carousel-inner">
                <div class="item active">
                    <img src="assets/img/img1.png">
                </div>
                <div class="item">
                    <img src="assets/img/img2.png">
                </div>
                <div class="item">
                    <img src="assets/img/img3.png">
                </div>
            </div>
        </div>
    </div>

    <!--Form-->
    <div class="col-lg-4 col-md-4 col-sm-3 col-xs-2">
        <form class="form" action="${pageContext.request.contextPath}/login" method="post">
            <img src="assets/img/male-circle.png">
            <div class="dws-input">
                <input type="text" name="login" placeholder="Enter login" maxlength="30">
            </div>
            <div class="dws-input">
                <input type="password" name="password" placeholder="Enter password" maxlength="20">
            </div>
            <input class="dws-submit" type="submit" name="submit" value="ENTER">
            <br/>
            <a href="${pageContext.request.contextPath}/registration/main">Registration</a>
        </form>
    </div>
</div>
