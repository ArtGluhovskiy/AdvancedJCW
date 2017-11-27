package org.art.web.controllers;

import org.art.dto.OrderDTO;
import org.art.entities.User;
import org.art.services.TaskOrderService;
import org.art.services.exceptions.ServiceBusinessException;
import org.art.services.exceptions.ServiceSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes(names = {"user", "errorMsg"})
@RequestMapping(value = "/statistics")
public class StatisticsController {

    @Autowired
    private TaskOrderService orderService;

    static final String STATISTICS = "statistics";
    static final String LOGIN = "login/main";

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String statisticsPage(ModelMap modelMap,
                           @ModelAttribute(value = "user") User user) {

        if (user.getLogin() == null || user == null) {
            return LOGIN;
        }

        List<OrderDTO> orderList;
        modelMap.remove("errorMsg");

        try {
            orderList = orderService.getAllUserSolvedTaskOrders(user.getUserID());
        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", ControllerConstants.SERVER_ERROR_MESSAGE);
            return STATISTICS;
        } catch (ServiceBusinessException e) {
            modelMap.put("message", "You have no task orders yet! ");
            return STATISTICS;
        }
        modelMap.put("orderList", orderList);
        return STATISTICS;
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
