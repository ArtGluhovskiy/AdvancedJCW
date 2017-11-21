package org.art.web.command.impl;

import org.art.entities.JavaTask;
import org.art.entities.User;
import org.art.services.JavaTaskService;
import org.art.services.TaskOrderService;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.services.impl.JavaTaskServiceImpl;
import org.art.web.command.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ApplicationController implements Controller {

    static ApplicationContext context;
    static JavaTaskService taskService;

    static {
        context = new ClassPathXmlApplicationContext("beans-services.xml");
        taskService = context.getBean("javaTaskServiceImpl", JavaTaskService.class);
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
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
            req.setAttribute("errorMsg", "We can't find a new task for you.<br>It seems that you solved all of them!");
            session.setAttribute("prevPage", "Statistics");
            req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
            return;
        } catch (ServiceSystemException e) {
            req.setAttribute("errorMsg", SERVER_ERROR_MESSAGE);
            session.setAttribute("prevPage", "Statistics");
            req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
            return;
        }
        session.removeAttribute("errorMsg");
        session.setAttribute("task", javaTask);
        req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
        return;
    }

    private boolean hasNotSolvedTask(User user) {
        try {
            taskService.getNotSolvedTask(user);
        } catch (ServiceSystemException e) {
            //User has no solved task in database
            return false;
        } catch (ServiceBusinessException e) {
            //System problems (mean the same as "user has no solved task in database")
            return false;
        }
        return true;
    }
}
