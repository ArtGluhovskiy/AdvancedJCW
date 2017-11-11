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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    private JavaTask javaTask;

    @CreationTimestamp
    @Column(name = "REG_DATE")
    private Date regDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "EXEC_TIME")
    private long execTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public TaskOrder(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringBuilder("*** TaskOrder {\n")
                .append("* order ID = " + orderID + "\n")
                .append("* registration date = " + regDate + "\n")
                .append("* status = " + status + "\n")
                .append("***")
                .toString();
    }
}
