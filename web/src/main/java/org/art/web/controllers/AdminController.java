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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaTaskService taskService;

    @Autowired
    private TaskOrderService orderService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String adminPage(ModelMap modelMap, HttpServletRequest request) {

        List<JavaTask> taskList;
        taskList = (List<JavaTask>) modelMap.get("taskList");
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
        return "admin";
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
            modelMap.put("updateErrorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
            return;
        } catch (ServiceBusinessException e) {
            modelMap.put("updateErrorMsg", "Can't find task in DB");
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
            //TODO: Danger!!!
            taskService.update(task);
            modelMap.put("updateStatusMsg", "Task was successfully updated!");
        } catch (ServiceSystemException e) {
            modelMap.put("updateErrorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            modelMap.put("updateErrorMsg", "Cannot update task!");
        }
    }

    private void readAllTasks(ModelMap modelMap) {
        List<JavaTask> taskList;
        try {
            taskList = taskService.getAll();
            modelMap.put("taskList", taskList);
        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            modelMap.put("errorMsg", "There is no tasks in database");
        }
    }

    private void readUserInfo(ModelMap modelMap, HttpServletRequest req) {
        List<OrderDTO> orderList;
        User user;
        Long userID = Long.parseLong(req.getParameter("userID"));
        try {
            user = userService.get(userID);
            orderList = orderService.getAllUserSolvedTaskOrders(user.getUserID());
            modelMap.put("userInfo", user);
            modelMap.put("orderList", orderList);
        } catch (ServiceSystemException e) {
            modelMap.remove("userInfo");
            modelMap.remove("orderList");
            modelMap.put("userErrorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
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
            modelMap.put("userUpdateErrorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
            return;
        } catch (ServiceBusinessException e) {
            modelMap.put("userUpdateErrorMsg", "Can't find user in DB");
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
            modelMap.put("userUpdateStatusMsg", "User was successfully updated!");
        } catch (ServiceSystemException e) {
            modelMap.put("userUpdateErrorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
        } catch (ServiceBusinessException e) {
            modelMap.put("userUpdateErrorMsg", "Cannot update user!");
        }
    }
}
