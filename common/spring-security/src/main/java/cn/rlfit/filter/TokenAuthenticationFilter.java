package cn.rlfit.filter;

import cn.rlfit.common.jwt.JwtUtils;
import cn.rlfit.common.result.ResponseUtil;
import cn.rlfit.common.result.Result;
import cn.rlfit.common.result.ResultCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public TokenAuthenticationFilter() {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("uri:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCode.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            JwtUtils jwtUtils = new JwtUtils();
            String useruame = (String) jwtUtils.getPayLoadALSOExcludeExpAndIat(token).get("username");
            logger.info("useruame:"+useruame);
            if (!StringUtils.isEmpty(useruame)) {
                return new UsernamePasswordAuthenticationToken(useruame, null, Collections.emptyList());
            }
        }
        return null;
    }
}