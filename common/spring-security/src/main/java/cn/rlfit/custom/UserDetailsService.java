package cn.rlfit.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 02/02/2024 4:01 PM
 */
@Component
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
