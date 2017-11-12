package org.art.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.sql.Date;

/**
 * This class is an implementation of task order entity
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "TASK_ORDERS")
public class TaskOrder {


    public TaskOrder(String status) {
        this.status = status;
    }

    public TaskOrder(String status, User user, JavaTask task) {
        this.status = status;
        this.user = user;
        this.javaTask = task;
    }

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @GenericGenerator(
            name = "ID_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @Parameter(
                            name = "sequence_name",
                            value = "ORDER_SEQUENCE"
                    )
            }
    )
    @Column(name = "ORDER_ID")
    private Long orderID;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TASK_ID")
    private JavaTask javaTask;

    @CreationTimestamp
    @Column(name = "REG_DATE")
    private Date regDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "EXEC_TIME")
    private long execTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Override
    public String toString() {
        return new StringBuilder("*** TaskOrder {\n")
                .append("* order ID = " + orderID + "\n")
                .append("* registration date = " + regDate + "\n")
                .append("* status = " + status + "\n")
                .append("***")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskOrder order = (TaskOrder) o;

        if (execTime != order.execTime) return false;
        if (orderID != null ? !orderID.equals(order.orderID) : order.orderID != null) return false;
        if (javaTask != null ? !javaTask.equals(order.javaTask) : order.javaTask != null) return false;
        if (status != null ? !status.equals(order.status) : order.status != null) return false;
        return user != null ? user.equals(order.user) : order.user == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (orderID != null ? orderID.hashCode() : 0);
        result = 31 * result + (javaTask != null ? javaTask.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (int) (execTime ^ (execTime >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
