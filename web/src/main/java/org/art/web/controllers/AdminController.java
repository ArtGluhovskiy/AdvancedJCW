package org.art.web.controllers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import static org.art.web.controllers.ControllerConstants.ADMIN_VIEW;
import static org.art.web.controllers.ControllerConstants.INTERNAL_ERROR_MESSAGE;

@Controller
@RequestMapping(value = "/admin")
@SessionAttributes(names = {"user", "taskList", "userInfo", "orderList"})
public class AdminController {

    private static final Logger LOG = LogManager.getLogger(AdminController.class);

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
        LOG.debug("AdminController: adminPage()");

        @SuppressWarnings("unchecked")
        List<JavaTask> taskList = (List<JavaTask>) modelMap.get("taskList");
        if (taskList == null) {
            readAllTasks(modelMap);
        }
        String taskID = (String) modelMap.get("taskID");
        if (StringUtils.isNotBlank(taskID)) {
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
        long taskID = NumberUtils.toLong(req.getParameter("taskID"));
        String diffGroup = req.getParameter("diffGroup");
        String shortDescr = req.getParameter("shortDescr");
        String elapsedTime = req.getParameter("elapsedTime");
        String popularity = req.getParameter("popularity");
        try {
            task = taskService.get(taskID);
        } catch (ServiceSystemException e) {
            modelMap.put("updateErrorMsg", INTERNAL_ERROR_MESSAGE);
            LOG.error("AdminController: ServiceSystemException - cannot get task from DB! Task ID: {}", taskID, e);
            return;
        } catch (ServiceBusinessException e) {
            modelMap.put("updateErrorMsg", "Can't find task in DB");
            LOG.info("AdminController: ServiceBusinessException - cannot find task in DB! Task ID: {}", taskID, e);
            return;
        }
        if (StringUtils.isNotBlank(diffGroup)) {
            task.setDifficultyGroup(diffGroup);
        }
        if (StringUtils.isNotBlank(shortDescr)) {
            task.setShortDescr(shortDescr);
        }
        if (StringUtils.isNotBlank(elapsedTime)) {
            int elTime = NumberUtils.toInt(elapsedTime);
            task.setElapsedTime(elTime);
        }
        if (StringUtils.isNotBlank(popularity)) {
            int pop = NumberUtils.toInt(popularity);
            task.setPopularity(pop);
        }
        try {
            taskService.update(task);
            modelMap.put("updateStatusMsg", "Task was successfully updated!");
        } catch (ServiceSystemException e) {
            modelMap.put("updateErrorMsg", INTERNAL_ERROR_MESSAGE);
            LOG.error("AdminController: ServiceSystemException - cannot update task in DB! Task ID: {}", taskID, e);
        } catch (ServiceBusinessException e) {
            modelMap.put("updateErrorMsg", "Cannot update task!");
            LOG.info("AdminController: ServiceBusinessException - cannot update task in DB! Task ID: {}", taskID, e);
        }
    }

    private void readAllTasks(ModelMap modelMap) {
        try {
            List<JavaTask> taskList = taskService.getAll();
            modelMap.put("taskList", taskList);
        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", INTERNAL_ERROR_MESSAGE);
            LOG.error("AdminController: ServiceSystemException - cannot get all tasks from DB!", e);
        } catch (ServiceBusinessException e) {
            modelMap.put("errorMsg", "There is no tasks in database");
            LOG.info("AdminController: ServiceBusinessException - there are no tasks in DB! Task ID: {}", e);
        }
    }

    private void readUserInfo(ModelMap modelMap, HttpServletRequest req) {
        Long userID = NumberUtils.toLong(req.getParameter("userID"));
        try {
            User user = userService.get(userID);
            List<OrderDTO> orderList = orderService.getAllUserSolvedTaskOrders(user.getUserID());
            modelMap.put("userInfo", user);
            modelMap.put("orderList", orderList);
        } catch (ServiceSystemException e) {
            modelMap.remove("userInfo");
            modelMap.remove("orderList");
            modelMap.put("userErrorMsg", INTERNAL_ERROR_MESSAGE);
            LOG.error("AdminController: ServiceSystemException - cannot get all solved tasks from DB! User ID: {}", userID, e);
        } catch (ServiceBusinessException e) {
            modelMap.remove("userInfo");
            modelMap.remove("orderList");
            modelMap.put("userErrorMsg", "There is no user in database");
            LOG.info("AdminController: ServiceBusinessException - there are no solved tasks in DB! User ID: {}", userID, e);
        }
    }

    private void updateUser(ModelMap modelMap, HttpServletRequest req) {
        User user;
        long userID = NumberUtils.toLong(req.getParameter("updateUserID"));
        String level = req.getParameter("level");
        String role = req.getParameter("role");
        String status = req.getParameter("status");
        String rating = req.getParameter("rating");
        try {
            user = userService.get(userID);
        } catch (ServiceSystemException e) {
            modelMap.put("userUpdateErrorMsg", INTERNAL_ERROR_MESSAGE);
            LOG.error("AdminController: ServiceSystemException - cannot get user from DB! User ID: {}", userID, e);
            return;
        } catch (ServiceBusinessException e) {
            modelMap.put("userUpdateErrorMsg", "Cannot find user in DB");
            LOG.info("AdminController: ServiceBusinessException - cannot find user in DB! User ID: {}", userID, e);
            return;
        }
        if (StringUtils.isNotBlank(level)) {
            user.setLevel(level);
        }
        if (StringUtils.isNotBlank(role)) {
            user.setRole(role);
        }
        if (StringUtils.isNotBlank(status)) {
            user.setStatus(status);
        }
        if (StringUtils.isNotBlank(rating)) {
            int rat = NumberUtils.toInt(rating);
            user.setRating(rat);
        }
        try {
            userService.update(user);
            modelMap.put("userUpdateStatusMsg", "User was successfully updated!");
        } catch (ServiceSystemException e) {
            modelMap.put("userUpdateErrorMsg", INTERNAL_ERROR_MESSAGE);
            LOG.error("AdminController: ServiceSystemException - cannot update user in DB! User ID: {}", userID, e);
        } catch (ServiceBusinessException e) {
            modelMap.put("userUpdateErrorMsg", "Cannot update user!");
            LOG.info("AdminController: ServiceBusinessException - cannot update user in DB! User ID: {}", userID, e);
        }
    }
}
