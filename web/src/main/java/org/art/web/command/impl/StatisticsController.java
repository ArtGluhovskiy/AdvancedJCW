package org.art.web.command.impl;

import org.art.dto.OrderDTO;
import org.art.entities.User;
import org.art.services.TaskOrderService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.services.impl.TaskOrderServiceImpl;
import org.art.web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class StatisticsController implements Controller {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("Statistics controller");
        List<OrderDTO> orderList = null;
        req.removeAttribute("errorMsg");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        TaskOrderService taskOrderService = TaskOrderServiceImpl.getInstance();
        try {
            orderList = taskOrderService.getUserTaskOrders(user.getUserID());
        } catch (ServiceSystemException e) {
            req.setAttribute("errorMsg", SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            req.setAttribute("errorMsg", "Unfortunately, no task orders were found!");
        }
        session.setAttribute("orderList", orderList);
        String contextPath = req.getContextPath();
        req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
        return;
    }
}
