package org.art.web.controllers;

import org.art.entities.JavaTask;
import org.art.entities.User;
import org.art.services.JavaTaskService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceException;
import org.art.services.exceptions.ServiceSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import static org.art.web.controllers.ControllerConstants.*;

@Controller
@RequestMapping("/application")
@SessionAttributes(names = {"user", "errorMsg", "task"})
public class ApplicationController {

    private final JavaTaskService taskService;

    @Autowired
    public ApplicationController(JavaTaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public String applicationPage(ModelMap modelMap,
                                  @ModelAttribute("user") User user,
                                  @ModelAttribute("errorMsg") String errorMsg) {
        if (user == null || user.getLogin() == null) {
            return LOGIN_VIEW;
        }
        JavaTask javaTask;
        try {
            //If no tasks were solved
            if (user.getRating() == 1) {
                //Requiring of the first task for user (with task ID > 0) after registration
                if (!hasNotSolvedTask(user)) {
                    javaTask = taskService.getNextTaskByDiffGroup(user, 0);
                } else {
                    javaTask = taskService.getNotSolvedTask(user);
                }
            } else {
                javaTask = taskService.getNotSolvedTask(user);
            }
        } catch (ServiceBusinessException e) {
            modelMap.put("errorMsg", "We can't find a new task for you.<br>It seems that you solved all of them!");
            modelMap.put("prevPage", "statistics");
            return ERROR_VIEW;
        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
            modelMap.put("prevPage", "statistics");
            return ERROR_VIEW;
        }
        modelMap.remove("errorMsg");
        modelMap.put("task", javaTask);
        return APPLICATION_VIEW;
    }

    private boolean hasNotSolvedTask(User user) {
        try {
            taskService.getNotSolvedTask(user);
        } catch (ServiceException e) {
            //Either some system problems or user has no solved task in the database
            return false;
        }
        return true;
    }

    @ModelAttribute("user")
    public User getUser() {
        return null;
    }

    @ModelAttribute("errorMsg")
    public String getErrorMdg() {
        return null;
    }
}
