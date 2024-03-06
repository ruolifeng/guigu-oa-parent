package cn.rlfit.auth.service.impl;

import cn.rlfit.auth.service.SysMenuService;
import cn.rlfit.custom.CustomUser;
import cn.rlfit.custom.UserDetailsService;
import cn.rlfit.model.system.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 02/02/2024 4:02 PM
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserServiceImpl sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserByUserName(username);
        if (null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if (sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }
        // 根据用户id查询用户操作的权限数据
        List<String> userPermsList = sysMenuService.getUserPermsById(sysUser.getId());
        // 创建集合用于封装最终的数据
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        // 将查询出的集合进行封装最终进行返回
        for (String perms : userPermsList) {
            authorityList.add(new SimpleGrantedAuthority(perms.trim()));
        }
        return new CustomUser(sysUser, authorityList);
    }
}
