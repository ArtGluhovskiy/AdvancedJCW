package org.art.entities;

import lombok.Data;

import java.sql.Date;

import static org.art.dao.util.DateTimeUtil.defineUserAge;

/**
 * This class is an implementation of user entity
 */
@Data
public class User {

    private long userID;
    private int rating = 0;
    private String clanName;
    private String login;
    private String password;
    private String fName;
    private String lName;
    private String email;
    private String role;
    private String status;
    private Date regDate;
    private Date birthDate;
    private String level;
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
