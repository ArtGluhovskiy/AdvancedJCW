package org.art.dto;

import lombok.Data;

import java.util.Date;

/**
 * OrderDTO is actually the mixture of data from
 * database tables ("users", "java_tasks", "task_orders")
 */
@Data
public class OrderDTO {

    private long orderID;
    private long userID;
    private int taskPopularity;
    private long elapsedTime;            //Recommended execution time of the algorithm
    private long execTime;               //Execution time of the user algorithm
    private String login;
    private String diffGroup;
    private String shortDesc;
    private String orderStatus;
    private Date regDate;
    private Date taskRegDate;


    public OrderDTO(long userID, String login, String diffGroup, String shortDesc, Date regDate, String orderStatus,
                    Date taskRegDate, int taskPopularity, long elapsedTime, long execTime, long orderID) {
        this.userID = userID;
        this.login = login;
        this.diffGroup = diffGroup;
        this.shortDesc = shortDesc;
        this.regDate = regDate;
        this.orderStatus = orderStatus;
        this.taskRegDate = taskRegDate;
        this.taskPopularity = taskPopularity;
        this.elapsedTime = elapsedTime;
        this.execTime = execTime;
        this.orderID = orderID;
    }

    @Override
    public String toString() {
        return new StringBuilder("*** OrderDTO info: \n")
                         .append("* order ID = " + orderID + "\n")
                         .append("* user ID = " + userID + "\n")
                         .append("* login = " + login + "\n")
                         .append("* group of difficulty = " + diffGroup + "\n")
                         .append("* short task description = " + shortDesc + "\n")
                         .append("* order registration date = " + regDate + "\n")
                         .append("* task registration date = " + taskRegDate + "\n")
                         .append("* task popularity = " + taskPopularity + "\n")
                         .append("* execution time = " + elapsedTime + "\n")
                         .append("* user execution time = " + execTime + "\n")
                         .append("***")
                         .toString();
    }
}
