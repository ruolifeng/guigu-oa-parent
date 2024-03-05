package cn.rlfit.filter;

import cn.rlfit.common.jwt.JwtUtils;
import cn.rlfit.common.result.ResponseUtil;
import cn.rlfit.common.result.Result;
import cn.rlfit.custom.CustomUser;
import cn.rlfit.vo.system.LoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 05/03/2024 7:44 PM
 */
@Component
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    public TokenLoginFilter(AuthenticationManager authenticationManager) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        // 指定登录提交的接口和方式
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
    }

    // 获取用户输入的用户名和密码，调用方法进行认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginVo vo = null;
        try {
            vo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(vo.getUsername(), vo.getPassword());
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    //认证成功
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        JwtUtils jwtUtils = new JwtUtils();
        Map map = new HashMap();
        map.put("username", customUser.getSysUser().getUsername());
        map.put("id", customUser.getSysUser().getId());
        String token = jwtUtils.createJwt((new Date(System.currentTimeMillis() + 60 * 60 * 24)), map);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("token", token);
        ResponseUtil.out(response, Result.ok(map2));
    }

    // 认证失败
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response, Result.fail());
    }
}
