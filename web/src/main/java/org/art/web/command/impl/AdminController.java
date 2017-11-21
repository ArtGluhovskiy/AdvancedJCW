package org.art.web.command.impl;

import org.art.dto.OrderDTO;
import org.art.entities.JavaTask;
import org.art.entities.User;
import org.art.services.JavaTaskService;
import org.art.services.TaskOrderService;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.web.command.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class AdminController implements Controller {

    static ApplicationContext context;
    static UserService userService;
    static JavaTaskService taskService;
    static TaskOrderService orderService;

    static {
        context = new ClassPathXmlApplicationContext("beans-services.xml");
        userService = context.getBean("userServiceImpl", UserService.class);
        taskService = context.getBean("javaTaskServiceImpl", JavaTaskService.class);
        orderService = context.getBean("taskOrderServiceImpl", TaskOrderService.class);
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<JavaTask> taskList;
        HttpSession session = req.getSession();
        taskList = (List<JavaTask>) session.getAttribute("taskList");
        if (taskList == null) {
            readAllTasks(req);
        }
        String taskID = req.getParameter("taskID");
        if (taskID != null && !"".equals(taskID)) {
            updateTask(req);
            readAllTasks(req);
        }
        String userID = req.getParameter("userID");
        if (userID != null) {
            readUserInfo(req);
        }
        String updateUserID = req.getParameter("updateUserID");
        if (updateUserID != null && !"".equals(updateUserID)) {
            updateUser(req);
        }
        req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
        return;
    }

    private void updateTask(HttpServletRequest req) {
        JavaTask task;
        long taskID = Long.parseLong(req.getParameter("taskID"));
        String diffGroup = req.getParameter("diffGroup");
        String shortDescr = req.getParameter("shortDescr");
        String elapsedTime = req.getParameter("elapsedTime");
        String popularity = req.getParameter("popularity");
        try {
            task = taskService.get(taskID);
        } catch (ServiceSystemException e) {
            req.setAttribute("updateErrorMsg", SERVER_ERROR_MESSAGE);
            return;
        } catch (ServiceBusinessException e) {
            req.setAttribute("updateErrorMsg", "Can't find task in DB");
            return;
        }
        if (!"".equals(diffGroup)) {
            task.setDifficultyGroup(diffGroup);
        }
        if (!"".equals(shortDescr)) {
            task.setShortDescr(shortDescr);
        }
        if (!"".equals(elapsedTime)) {
            int elTime = Integer.parseInt(elapsedTime);
            task.setElapsedTime(elTime);
        }
        if (!"".equals(popularity)) {
            int pop = Integer.parseInt(popularity);
            task.setPopularity(pop);
        }
        try {
            taskService.update(task);
            req.setAttribute("updateStatusMsg", "Task was successfully updated!");
        } catch (ServiceSystemException e) {
            System.out.println("I am in catch!!!");
            req.setAttribute("updateErrorMsg", SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            req.setAttribute("updateErrorMsg", "Cannot update task!");
        }
    }

    private void readAllTasks(HttpServletRequest req) {
        List<JavaTask> taskList;
        try {
            taskList = taskService.getAll();
            req.getSession().setAttribute("taskList", taskList);
        } catch (ServiceSystemException e) {
            req.setAttribute("errorMsg", SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            req.setAttribute("errorMsg", "There is no tasks in database");
        }
    }

    private void readUserInfo(HttpServletRequest req) {
        List<OrderDTO> orderList;
        User user;
        Long userID = Long.parseLong(req.getParameter("userID"));
        try {
            user = userService.get(userID);
            orderList = orderService.getAllUserSolvedTaskOrders(user.getUserID());
            req.getSession().setAttribute("userInfo", user);
            req.getSession().setAttribute("orderList", orderList);
        } catch (ServiceSystemException e) {
            req.getSession().removeAttribute("userInfo");
            req.getSession().removeAttribute("orderList");
            req.setAttribute("userErrorMsg", SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            req.getSession().removeAttribute("userInfo");
            req.getSession().removeAttribute("orderList");
            req.setAttribute("userErrorMsg", "There is no user in database");
        }
    }

    private void updateUser(HttpServletRequest req) {
        User user;
        long userID = Long.parseLong(req.getParameter("updateUserID"));
        String level = req.getParameter("level");
        String role = req.getParameter("role");
        String status = req.getParameter("status");
        String rating = req.getParameter("rating");
        try {
            user = userService.get(userID);
        } catch (ServiceSystemException e) {
            req.setAttribute("userUpdateErrorMsg", SERVER_ERROR_MESSAGE);
            return;
        } catch (ServiceBusinessException e) {
            req.setAttribute("userUpdateErrorMsg", "Can't find user in DB");
            return;
        }
        if (!"".equals(level)) {
            user.setLevel(level);
        }
        if (!"".equals(role)) {
            user.setRole(role);
        }
        if (!"".equals(status)) {
            user.setStatus(status);
        }
        if (!"".equals(rating)) {
            int rat = Integer.parseInt(rating);
            user.setRating(rat);
        }
        try {
            userService.update(user);
            req.setAttribute("userUpdateStatusMsg", "User was successfully updated!");
        } catch (ServiceSystemException e) {
            req.setAttribute("userUpdateErrorMsg", SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            req.setAttribute("userUpdateErrorMsg", "Cannot update user!");
        }
    }
}
