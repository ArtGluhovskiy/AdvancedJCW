package org.art.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.art.dao.utils.DateTimeUtil.defineUserAge;

/**
 * This class is an implementation of user entity.
 */
@Data
@NoArgsConstructor
@DynamicUpdate
@Entity(name = "USERS")
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @GenericGenerator(
            name = "ID_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @Parameter(
                            name = "sequence_name",
                            value = "USER_SEQUENCE"
                    )
            }
    )
    @Column(name = "USER_ID")
    private Long userID;

    @Column(name = "RATING")
    private int rating = 0;

    @Pattern(regexp = "\\b[A-Za-z]{1,20}\\b", message = "Invalid clan name!")
    @Column(name = "CLAN_NAME")
    private String clanName;

    @NotNull
    @Column(name = "LOGIN")
    private String login;

    @NotNull
    @Column(name = "PASSWORD", length = 30)
    private String password;

    @Pattern(regexp = "\\b[A-Za-z]{1,20}\\b", message = "Invalid first name!")
    @Column(name = "F_NAME")
    private String fName;

    @Pattern(regexp = "\\b[A-Za-z]{1,20}\\b", message = "Invalid last name!")
    @Column(name = "L_NAME")
    private String lName;

    @Pattern(regexp = "\\b[a-z][\\w.]+@[a-z]{2,7}.[a-z]{2,3}\\b", message = "Invalid email!")
    @NotNull
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "STATUS")
    private String status;

    @CreationTimestamp
    @Column(name = "REG_DATE", updatable = false)
    private Date regDate;

    @Column(name = "BIRTH_DATE")
    private Date birthDate;

    //String birth from Spring Form
    @Pattern(regexp = "(0[1-9]|1[0-9]|2[0-9]|3[01])-(0[1-9]|1[012])-[0-9]{4}", message = "Invalid birth date format!")
    @Transient
    private String birth;

    @Column(name = "LEVEL")
    private String level;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
               cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<TaskOrder> orders = new ArrayList<>();

    @Transient
    private int age;

    public User(String clan, String login, String password, String fName, String lName, String email, Date regDate,
                String role, String status, Date birthDate, String level) {
        this.clanName = clan;
        this.login = login;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.role = role;
        this.status = status;
        this.regDate = regDate;
        this.birthDate = birthDate;
        this.level = level;
        this.age = defineUserAge(birthDate);
    }

    public int getAge() {
        int age;
        age = defineUserAge(birthDate);
        return age;
    }

    @Override
    public String toString() {
        return new StringBuilder("*** User info: \n")
                .append("* user ID = " + userID + "\n")
                .append("* rating = " + rating + "\n")
                .append("* clan = " + clanName + "\n")
                .append("* login = " + login + "\n")
                .append("* password = " + password + "\n")
                .append("* first name = " + fName + "\n")
                .append("* last name = " + lName + "\n")
                .append("* age = " + defineUserAge(birthDate) + "\n")
                .append("* email = " + email + "\n")
                .append("* registration date = " + regDate + "\n")
                .append("* user role = " + role + "\n")
                .append("* user status = " + status + "\n")
                .append("* user birth date = " + birthDate + "\n")
                .append("* user level = " + level + "\n")
                .append("***")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (rating != user.rating) return false;
        if (clanName != null ? !clanName.equals(user.clanName) : user.clanName != null) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (fName != null ? !fName.equals(user.fName) : user.fName != null) return false;
        if (lName != null ? !lName.equals(user.lName) : user.lName != null) return false;
        return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + rating;
        result = 31 * result + (clanName != null ? clanName.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (fName != null ? fName.hashCode() : 0);
        result = 31 * result + (lName != null ? lName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
