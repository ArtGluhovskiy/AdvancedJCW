package org.art.web.command.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.entities.JavaTask;
import org.art.entities.User;
import org.art.services.TaskOrderService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceCompilationException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.services.impl.ResultsAnalyzer;
import org.art.services.impl.StringCompilerService;
import org.art.services.impl.TaskOrderServiceImpl;
import org.art.web.command.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CompilerController implements Controller {

    public static final Logger log = LogManager.getLogger(CompilerController.class);

    /**
     * {@code CompilerController} is responsible for code compilation
     * and result analyzing
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        JavaTask task = (JavaTask) session.getAttribute("task");
        String code = req.getParameter("code");
        StringCompilerService stringCompiler = new StringCompilerService(false);
        StringCompilerService.TaskResults compResult;
        try {
            compResult = stringCompiler.compileTask(task, code);
        } catch (ServiceCompilationException e) {
            session.setAttribute("code", code);
            req.setAttribute("title", "Result_fail");
            req.setAttribute("errorMsg", "Some problems with compilation.<br>Try to fix it!");
            req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
            return;
        }
        if (compResult.getMethodResult() == null) {
            session.setAttribute("code", code);
            req.setAttribute("title", "Result_fail");
            req.setAttribute("errorMsg", "Your code is not compiling.<br>Try to fix it!");
            req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
            return;
        }
        boolean result = ResultsAnalyzer.analyzeResults(task, compResult);
        if (result) {
            req.setAttribute("elapsedTime", compResult.getElapsedTime());
            modifyUserData(session, req, resp);
            req.setAttribute("title", "Result_success");
            session.removeAttribute("code");
            req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
            return;
        } else {
            session.setAttribute("code", code);
            req.setAttribute("title", "Result_fail");
            req.setAttribute("errorMsg", "It seems that your algorithm is not efficient or even incorrect!<br>Try to fix it!");
            req.getRequestDispatcher(MAIN_PAGE).forward(req, resp);
            return;
        }
    }

    /**
     * Creation of a new task order in database with a new "NOT SOLVED" task;
     * user rating increase; task popularity increase etc.
     */
    private void modifyUserData(HttpSession session, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskOrderService orderService = TaskOrderServiceImpl.getInstance();
        User user = (User) session.getAttribute("user");
        JavaTask task = (JavaTask) session.getAttribute("task");
        long execTime = (long) req.getAttribute("elapsedTime");
        try {
            orderService.createNewOrder(user, task, execTime);
        } catch (ServiceSystemException e) {
            req.setAttribute("errorMsg", SERVER_ERROR_MESSAGE);
            req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
            return;
        } catch (ServiceBusinessException e) {
            /*NOP*/
            //This exception is thrown if no "NOT SOLVED" task order was created.
            //It means that there are no more tasks to solve.
            //Method ignores this exception at THIS stage.
            //ServiceBusinessException will be thrown and catch later.
        }
    }
}
