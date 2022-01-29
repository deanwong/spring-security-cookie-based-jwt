package com.asksrc.comments.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author wangding
 * @Date 2022/1/25
 * @see <a href="https://mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression">mykong.com</a>
 */
public class Validator {

    // digit + lowercase char + uppercase char + symbol
    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern PWD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    private static final String EMAIL_REGEX =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]{5,20}$";

    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);

    public static boolean isValidPwd(final String password) {
        Matcher matcher = PWD_PATTERN.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidEmail(final String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidUsername(final String username) {
        Matcher matcher = USERNAME_PATTERN.matcher(username);
        return matcher.matches();
    }

}
