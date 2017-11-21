package org.art.services.validators;

import org.art.entities.User;
import org.art.services.UserService;
import org.art.services.exceptions.ServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User data validator (for password, first name, last name,
 * login, email etc.)
 */
public class UserValidator {

    static ApplicationContext context;
    static UserService userService;

    static {
        context = new ClassPathXmlApplicationContext("beans-services.xml");
        userService = context.getBean("userServiceImpl", UserService.class);
    }

    /**
     * User data validation
     *
     * @param fName user's first name
     * @param lName user's last name
     * @param login user's login
     * @param password user's password
     * @param email user's email
     * @param birth user's birth
     * @return error validation message in case of incorrect data
     */
    public static String validateUserData(String fName, String lName, String login, String password, String email, String birth) {
        if (!validateName(fName)) {
            return "Invalid first name!";
        }
        if (!validateName(lName)) {
            return "Invalid last name!";
        }
        if (!validatePassword(password)) {
            return "Invalid login or password!";
        }
        if (!validateEmail(email)) {
            return "Invalid email!";
        }
        if (!validateBirth(birth)) {
            return "Invalid birth date";
        }
        if (!validateLogin(login)) {
            System.out.println("Invalid login!");
            return "Invalid login or password!";
        }
        return null;
    }

    /**
     * First and last name validation
     *
     * @param name user's first or last name
     * @return false in case of incorrect data
     */
    public static boolean validateName(String name) {
        if (name == null || "".equals(name)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\b[a-z]{1,20}\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * Login validation
     *
     * @param login user's login
     * @return false in case of incorrect data
     */
    public static boolean validateLogin(String login) {

        if (login == null || "".equals(login)) {
            return false;
        }
        User user;
        try {
            user = userService.getUserByLogin(login);
        } catch (ServiceException e) {
            //User with such login was not found. That's OK!
            return true;
        }
        //User with such login already exists
        return false;
    }

    /**
     * User's password validation
     *
     * @param password user's password
     * @return false in case of incorrect data
     */
    public static boolean validatePassword(String password) {
        if (password == null || "".equals(password)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\S]{1,20}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            System.out.println("Password incorrect!!!");
            return false;
        }
        return true;
    }

    /**
     * User's email validation
     *
     * @param email user's email
     * @return false in case of incorrect data
     */
    public static boolean validateEmail(String email) {
        if (email == null || "".equals(email)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\b[a-z][\\w.]+@[a-z]{2,7}.[a-z]{2,3}\\b");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * User's birth date validation
     *
     * @param birth user's birth date
     * @return false in case of incorrect data
     */
    public static boolean validateBirth(String birth) {
        if (birth == null || "".equals(birth)) {
            return false;
        }
        Pattern pattern = Pattern.compile("(0[1-9]|1[0-9]|2[0-9]|3[01])-(0[1-9]|1[012])-[0-9]{4}");
        Matcher matcher = pattern.matcher(birth);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }
}
