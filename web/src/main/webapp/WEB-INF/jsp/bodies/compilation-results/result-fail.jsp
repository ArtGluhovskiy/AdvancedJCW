<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container">
    <form class="result_form_fail" action="${pageContext.request.contextPath}/application" method="post">
        <i class="fa fa-thumbs-down fa-5x" aria-hidden="true"></i>
        <div class="result_success">
            <h2><i class="fa fa-frown-o fa-2x" aria-hidden="true"></i>  ${errorMsg}</h2>
        </div>
        <br>
        <input class="dws-submit" type="submit" name="submit" value="RETURN">
    </form>
</div>


