package org.art.web.controllers;

import org.art.entities.JavaTask;
import org.art.entities.User;
import org.art.services.TaskOrderService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceCompilationException;
import org.art.services.exceptions.ServiceSystemException;
import org.art.services.impl.ResultsAnalyzer;
import org.art.services.impl.StringCompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import static org.art.web.controllers.ControllerConstants.*;

@Controller
@SessionAttributes(names = {"code", "user", "task"})
@RequestMapping("/compile")
public class JavaCompilerController {

    private final TaskOrderService orderService;

    @Autowired
    public JavaCompilerController(TaskOrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String compile(ModelMap modelMap,
                          @ModelAttribute("task") JavaTask task,
                          @ModelAttribute("user") User user,
                          @RequestParam("code") String code) {

        StringCompilerService stringCompiler = new StringCompilerService(false);
        StringCompilerService.TaskResults compResult;
        modelMap.remove("errorMsg");
        try {
            compResult = stringCompiler.compileTask(task, code);
        } catch (ServiceCompilationException e) {
            modelMap.put("code", code);
            modelMap.put("title", "Result_fail");
            modelMap.put("errorMsg", "Some problems with compilation.<br>Try to fix it!");
            return COMPILATION_FAILED_VIEW;
        }
        if (compResult.getMethodResult() == null) {
            modelMap.put("code", code);
            modelMap.put("title", "Result_fail");
            modelMap.put("errorMsg", "Your code is not compiling.<br>Try to fix it!");
            return COMPILATION_FAILED_VIEW;
        }
        boolean result = ResultsAnalyzer.analyzeResults(task, compResult);
        if (result) {
            modelMap.put("elapsedTime", compResult.getElapsedTime());
            modifyUserData(modelMap);
            if (modelMap.get("errorMsg") != null) {
                return ERROR_VIEW;
            }
            modelMap.put("title", "Result_success");
            modelMap.remove("code");
            return COMPILATION_SUCCESS_VIEW;
        } else {
            modelMap.put("code", code);
            modelMap.put("title", "Result_fail");
            modelMap.put("errorMsg", "It seems that your algorithm is not efficient or even incorrect!<br>Try to fix it!");
            return COMPILATION_FAILED_VIEW;
        }
    }

    /**
     * Creation of a new task order in database with a new "NOT SOLVED" task;
     * user rating increase; task popularity increase etc.
     */
    private void modifyUserData(ModelMap modelMap) {
        User user = (User) modelMap.get("user");
        JavaTask task = (JavaTask) modelMap.get("task");
        long execTime = (long) modelMap.get("elapsedTime");
        try {
            orderService.createNewOrder(user, task, execTime);
        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
            return;
        } catch (ServiceBusinessException e) {
            /*NOP*/
            //"NOT SOLVED" task order wasn't created.
            //There are no more tasks to solve.
            //Method ignores this exception at THIS stage.
            //ServiceBusinessException will be thrown and catch later.
        }
    }

    @ModelAttribute("task")
    public User getJavaTask() {
        return null;
    }

    @ModelAttribute("user")
    public User getUser() {
        return null;
    }
}
