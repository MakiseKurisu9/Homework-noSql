package com.example.articleManagement.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
     * 加密密码
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    public static String encodePassword(String rawPassword) {
        // 使用 BCrypt 生成盐值并加密密码
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    /**
     * 验证密码是否匹配
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后的密码
     * @return 如果密码匹配返回 true，否则返回 false
     */
    public static boolean match(String rawPassword, String encodedPassword) {
        // 使用 BCrypt 的 matches 方法来验证密码是否匹配
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
