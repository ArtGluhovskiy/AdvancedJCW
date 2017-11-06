package org.art.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.sql.Date;

import static org.art.dao.utils.DateTimeUtil.defineUserAge;

/**
 * This class is an implementation of user entity
 */
@Data
@NoArgsConstructor
@DynamicUpdate
@Entity(name = "USERS")
public class User {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @GenericGenerator(
            name = "ID_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @Parameter(
                            name = "sequence_name",
                            value = "JCW_SEQUENCE"
                    )
            }
    )
    @Column(name = "USER_ID")
    private Long userID;

    @Column(name = "RATING")
    private int rating = 0;

    @Column(name = "CLAN_NAME")
    private String clanName;

    @NotNull
    @Column(name = "LOGIN")
    private String login;

    @NotNull
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "F_NAME")
    private String fName;

    @Column(name = "L_NAME")
    private String lName;

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

    @Column(name = "LEVEL")
    private String level;

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
