package com.lin.admin.common.utils;

import com.lin.admin.system.entity.PasswordStrategy;
import com.lin.admin.system.entity.User;
import com.lin.admin.system.model.UserVO;

import java.text.MessageFormat;
import java.util.regex.Pattern;

/**
 * 密码校验
 */
public class PasswordValidation {
    public static String Number = ".*\\d+.*";

    public static String Letters_a = ".*[a-z].*";

    public static String Letters_A = ".*[A-Z].*";

    /**
     * 字母+数字(+可选)
     */
    public static final Pattern COMPLEXITY_COMPLEX_PATTERN = Pattern.compile("^(?!\\D+$)(?![^a-zA-Z]+$)\\S+$");
    /**
     * 大写字母+小写字母+数字(+可选)
     */
    public static final Pattern COMPLEXITY_COMPLEXCASESENSITIVE_PATTERN = Pattern.compile("^(?!\\D+$)(?![^a-z]+$)(?![^A-Z]+$)\\S+$");

    /**
     * 特殊符号
     */
    public static final String SPECIAL_CHARS = "~!@#$%^&*()+=|{}:;\"'.,?<>\\[\\]/\\\\_-";

    /**
     * 大写字母+小写字母+数字+特殊符号(+可选)
     */
    public static final Pattern COMPLEXITY_COMPLEXCASESENSITIVESPECIAL_PATTERN = Pattern.compile(MessageFormat.format("^(?=.*[{0}])(?=.*[{1}])(?=.*[{2}])(?=.*[{3}])\\S+$", "\\d", "a-z", "A-Z", SPECIAL_CHARS));

    public static String Range = "^([A-Z]|[a-z]|[0-9]|[%s]){%s,20}$";

    /**
     * 必须混合使用字母和数字
     */
    private static final Integer Complex = 1;
    /**
     * 必须混合使用大小写字母和数字
     */
    private static final Integer ComplexCaseSensitive = 2;
    /**
     * 必须混合使用大小写字母、数字和特殊符号
     */
    private static final Integer ComplexCaseSensitiveSpecial = 3;

    public static String passwordValidation(UserVO uservo, User user, PasswordStrategy passwordStrategy) {
        String passWord = null;
        if (uservo == null) {
            passWord = user.getPassword();
        } else if (user == null) {
            passWord = uservo.getPwdNew();
        }

        if (passwordStrategy == null || passWord == null) {
            return "";
        }

        int minLength = passwordStrategy.getMinLength();
        if (passWord.length() < minLength) {
            return "密码长度必须大于等于 '" + minLength + "'!";
        }
        Integer complexity = passwordStrategy.getComplexity();
        if (Complex.equals(complexity)) {
            if (passWord.matches(PasswordValidation.Number)
                    && (passWord.matches(PasswordValidation.Letters_a) || passWord.matches(PasswordValidation.Letters_A))) {
                return "";
            } else {
                return "密码必须混合使用字母和数字！";
            }
        } else if (ComplexCaseSensitive.equals(complexity)) {
            if (passWord.matches(PasswordValidation.Number)
                    && passWord.matches(PasswordValidation.Letters_a)
                    && passWord.matches(PasswordValidation.Letters_A)) {
                return "";
            } else {
                return "密码必须混合使用大小写字母和数字！";
            }
        } else if (ComplexCaseSensitiveSpecial.equals(complexity)) {
            if (passWord.matches(PasswordValidation.Number)
                    && passWord.matches(PasswordValidation.Letters_a)
                    && passWord.matches(PasswordValidation.Letters_A)
                    && passWord.replaceAll("[a-z]*[A-Z]*\\d*\\s*", "").length() != 0) {
                return "";
            } else {
                return "密码必须混合使用大小写字母、数字和特殊符号！";
            }
        } else {
            return "";
        }
    }

    public static String passwordValidation(UserVO user, PasswordStrategy passwordStrategy) {
        if (passwordStrategy == null) {
            return "";
        }
        return passwordValidation(user, null, passwordStrategy);
    }

    public static String passwordValidation(User user, PasswordStrategy passwordStrategy) {
        if (passwordStrategy == null) {
            return "";
        }
        return passwordValidation(null, user, passwordStrategy);
    }

}
