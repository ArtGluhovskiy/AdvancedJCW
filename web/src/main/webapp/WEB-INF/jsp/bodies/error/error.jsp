<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container error_container">
    <form class="error_form" action="${prevPage}" method="post">
        <i class="fa fa-exclamation-triangle fa-5x" aria-hidden="true"></i>
        <div class="error_block">
            <h2>${errorMsg}</h2>
            <h3>Drink a cup of coffee now  <i class="fa fa-coffee fa-2x" aria-hidden="true"></i><br><br>or play the "BattleShip" game  <a href="${pageContext.request.contextPath}/battleship" style="color:white"><i class="fa fa-ship fa-2x" aria-hidden="true"></i></a><br><br> and try later!</h3>
        </div>
        <br>
        <input class="dws-submit" type="submit" name="submit" value="RETURN">
    </form>
</div>


