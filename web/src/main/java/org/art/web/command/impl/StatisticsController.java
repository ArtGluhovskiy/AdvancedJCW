package org.art.web.command.impl;

import org.art.dto.OrderDTO;
import org.art.entities.User;
import org.art.services.JavaTaskService;
import org.art.services.TaskOrderService;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.services.impl.TaskOrderServiceImpl;
import org.art.web.command.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class StatisticsController implements Controller {

    static ApplicationContext context;
    static TaskOrderService orderService;

    static {
        context = new ClassPathXmlApplicationContext("beans-services.xml");
        orderService = context.getBean("taskOrderServiceImpl", TaskOrderService.class);
    }


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("Statistics controller");
        List<OrderDTO> orderList = null;
        req.removeAttribute("errorMsg");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        try {
            orderList = orderService.getAllUserSolvedTaskOrders(user.getUserID());
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
