package cn.rlfit.custom;

import cn.rlfit.common.util.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 02/02/2024 3:42 PM
 */
public class CustomMd5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.md5Upper(rawPassword.toString(), "gdfguefvsdjfgjSHJKDGFJgjhsdg#@$");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Util.md5Upper(rawPassword.toString(), "gdfguefvsdjfgjSHJKDGFJgjhsdg#@$"));
    }
}
