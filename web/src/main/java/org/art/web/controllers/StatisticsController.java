package org.art.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import static org.art.web.controllers.ControllerConstants.LOGIN_VIEW;
import static org.art.web.controllers.ControllerConstants.INTERNAL_ERROR_MESSAGE;
import static org.art.web.controllers.ControllerConstants.STATISTICS_VIEW;

@Controller
@SessionAttributes(names = {"user", "errorMsg"})
@RequestMapping(value = "/statistics")
public class StatisticsController {

    private static final Logger LOG = LogManager.getLogger(StatisticsController.class);

    private final TaskOrderService orderService;

    @Autowired
    public StatisticsController(TaskOrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String statisticsPage(ModelMap modelMap, @ModelAttribute(value = "user") User user) {
        LOG.debug("StatisticsController: statisticsPage(...)");
        if (user == null || user.getLogin() == null) {
            return LOGIN_VIEW;
        }
        List<OrderDTO> orderList;
        modelMap.remove("errorMsg");
        try {
            orderList = orderService.getAllUserSolvedTaskOrders(user.getUserID());
        } catch (ServiceSystemException e) {
            modelMap.put("errorMsg", INTERNAL_ERROR_MESSAGE);
            LOG.error("StatisticsController: ServiceSystemException - cannot get solved tasks from DB! User ID: {}", user.getUserID(), e);
            return STATISTICS_VIEW;
        } catch (ServiceBusinessException e) {
            modelMap.put("message", "You have no task orders yet! ");
            LOG.info("StatisticsController: ServiceBusinessException - cannot get solved tasks from DB! User ID: {}", user.getUserID(), e);
            return STATISTICS_VIEW;
        }
        modelMap.put("orderList", orderList);
        return STATISTICS_VIEW;
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
