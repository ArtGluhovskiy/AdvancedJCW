package org.art.web.controllers;

import org.art.dto.OrderDTO;
import org.art.entities.JavaTask;
import org.art.entities.User;
import org.art.services.JavaTaskService;
import org.art.services.TaskOrderService;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.art.web.controllers.ControllerConstants.*;

@Controller
@RequestMapping(value = "/admin")
@SessionAttributes(names = {"user", "taskList", "userInfo", "orderList"})
public class AdminController {

    private final UserService userService;

    private final JavaTaskService taskService;

    private final TaskOrderService orderService;

    @Autowired
    public AdminController(UserService userService, JavaTaskService taskService, TaskOrderService orderService) {
        this.userService = userService;
        this.taskService = taskService;
        this.orderService = orderService;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String adminPage(ModelMap modelMap, HttpServletRequest request) {

        @SuppressWarnings("unchecked")
        List<JavaTask> taskList = (List<JavaTask>) modelMap.get("taskList");
        if (taskList == null) {
            readAllTasks(modelMap);
        }
        String taskID = (String) modelMap.get("taskID");
        if (taskID != null && !"".equals(taskID)) {
            updateTask(modelMap, request);
            readAllTasks(modelMap);
        }
        String userID = (String) modelMap.get("userID");
        if (userID != null) {
            readUserInfo(modelMap, request);
        }
        String updateUserID = (String) modelMap.get("updateUserID");
        if (updateUserID != null && !"".equals(updateUserID)) {
            updateUser(modelMap, request);
        }
        return ADMIN_VIEW;
    }

    private void updateTask(ModelMap modelMap, HttpServletRequest req) {
        JavaTask task;
        long taskID = Long.parseLong(req.getParameter("taskID"));
        String diffGroup = req.getParameter("diffGroup");
        String shortDescr = req.getParameter("shortDescr");
        String elapsedTime = req.getParameter("elapsedTime");
        String popularity = req.getParameter("popularity");
        try {
            task = taskService.get(taskID);
        } catch (ServiceSystemException e) {
            modelMap.put("updateErrorMsg", SERVER_ERROR_MESSAGE);
            return;
        } catch (ServiceBusinessException e) {
            modelMap.put("updateErrorMsg", "Can't find task in DB");
            return;
        }
        if (!EMPTY.equals(diffGroup)) {
            task.setDifficultyGroup(diffGroup);
        }
        if (!EMPTY.equals(shortDescr)) {
            task.setShortDescr(shortDescr);
        }
        if (!EMPTY.equals(elapsedTime)) {
            int elTime = Integer.parseInt(elapsedTime);
            task.setElapsedTime(elTime);
        }
        if (!EMPTY.equals(popularity)) {
            int pop = Integer.parseInt(popularity);
            task.setPopularity(pop);
        }
        try {
            taskService.update(task);
            modelMap.put("updateStatusMsg", "Task was successfully updated!");
        } catch (ServiceSystemException e) {
            modelMap.put("updateErrorMsg", SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            modelMap.put("updateErrorMsg", "Cannot update task!");
        }
    }

    private void readAllTasks(ModelMap modelMap) {
        try {
            List<JavaTask> taskList = taskService.getAll();
            modelMap.put("taskList", taskList);
        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            modelMap.put("errorMsg", "There is no tasks in database");
        }
    }

    private void readUserInfo(ModelMap modelMap, HttpServletRequest req) {
        Long userID = Long.parseLong(req.getParameter("userID"));
        try {
            User user = userService.get(userID);
            List<OrderDTO> orderList = orderService.getAllUserSolvedTaskOrders(user.getUserID());
            modelMap.put("userInfo", user);
            modelMap.put("orderList", orderList);
        } catch (ServiceSystemException e) {
            modelMap.remove("userInfo");
            modelMap.remove("orderList");
            modelMap.put("userErrorMsg", SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            modelMap.remove("userInfo");
            modelMap.remove("orderList");
            modelMap.put("userErrorMsg", "There is no user in database");
        }
    }

    private void updateUser(ModelMap modelMap, HttpServletRequest req) {
        User user;
        long userID = Long.parseLong(req.getParameter("updateUserID"));
        String level = req.getParameter("level");
        String role = req.getParameter("role");
        String status = req.getParameter("status");
        String rating = req.getParameter("rating");
        try {
            user = userService.get(userID);
        } catch (ServiceSystemException e) {
            modelMap.put("userUpdateErrorMsg", SERVER_ERROR_MESSAGE);
            return;
        } catch (ServiceBusinessException e) {
            modelMap.put("userUpdateErrorMsg", "Can't find user in DB");
            return;
        }
        if (!EMPTY.equals(level)) {
            user.setLevel(level);
        }
        if (!EMPTY.equals(role)) {
            user.setRole(role);
        }
        if (!EMPTY.equals(status)) {
            user.setStatus(status);
        }
        if (!EMPTY.equals(rating)) {
            int rat = Integer.parseInt(rating);
            user.setRating(rat);
        }
        try {
            userService.update(user);
            modelMap.put("userUpdateStatusMsg", "User was successfully updated!");
        } catch (ServiceSystemException e) {
            modelMap.put("userUpdateErrorMsg", SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            modelMap.put("userUpdateErrorMsg", "Cannot update user!");
        }
    }
}
