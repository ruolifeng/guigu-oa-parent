package cn.rlfit.auth.controller;

import cn.rlfit.auth.service.SysMenuService;
import cn.rlfit.auth.service.SysUserService;
import cn.rlfit.common.config.exception.GuiguException;
import cn.rlfit.common.jwt.JwtUtils;
import cn.rlfit.common.result.Result;
import cn.rlfit.common.util.MD5Util;
import cn.rlfit.model.system.SysUser;
import cn.rlfit.vo.system.LoginVo;
import cn.rlfit.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 25/01/2024 12:30 PM
 */


@Api(tags = "02.后台登录管理")
@RestController //返回json数据
@RequestMapping("/admin/system/index")
public class indexController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    //    login
    @PostMapping("login")
    public Result login(@RequestBody LoginVo vo) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", "admin-token");
//        修改登录接口，由于需要进行鉴权处理，所以在之后的每一次登录过程中都需要判断用户是否登录，所以我们需要将第一次登录获取的用户信息生成一个、token存储在cookie中
//        之后的每一次访问都需要携带这个数据进行访问，查询redis判断用户是否处于登录状态
//        根据用户名称获取数据库中的密码
//        判断查询到的用户是否是空
//        将密码和用户输入的密码进行比较
//        将用户id和用户名转换成token数据返回客户端
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SysUser> userByUsername = wrapper.eq(SysUser::getUsername, vo.getUsername());
        SysUser oneUser = sysUserService.getOne(wrapper);
        if (Objects.isNull(oneUser)) throw new GuiguException(201, "用户名或密码错误");
        String md5Upper = MD5Util.md5Upper(vo.getPassword(), "gdfguefvsdjfgjSHJKDGFJgjhsdg#@$");
        if (!(oneUser.getPassword().equals(md5Upper))) throw new GuiguException(202, "用户名或密码错误");
        // 判断用户是否被禁用
        if (oneUser.getStatus().intValue() == 0) throw new GuiguException(202, "用户被禁用");
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", oneUser.getId());
        userMap.put("username", oneUser.getUsername());
        String token = jwtUtils.createJwt((new Date(System.currentTimeMillis() + 60 * 60 * 24)), userMap);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.ok(map);
    }

    //    info
    @GetMapping("getInfo")
    public Result info(HttpServletRequest request) {
//        用户登录成功之后获取请求头中携带的token信息，解析获取用户id，根据用户id查询菜单列表和用户可以操作的按钮列表
        String token = request.getHeader("token");
//        使用jwt解析获取用户id
        Map<String, Object> userMap = jwtUtils.getPayLoadALSOExcludeExpAndIat(token);
//        根据id获取用户信息
        Object userId = userMap.get("id");
        if (StringUtils.isEmpty(userId)) throw new RuntimeException("错误的token");
        Long userIdLong = userId instanceof Integer ? Long.parseLong(userId.toString()) : (Long) userId;
        SysUser user = sysUserService.getById(userIdLong);
//        根据用户id查询用户可以操作的菜单列表
        List<RouterVo> router = sysMenuService.getUserMenuListById(userIdLong);
//        根据用户id获取用户可以操作的按钮的列表
        List<String> perms = sysMenuService.getUserPermsById(userIdLong);
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", user.getName());
        map.put("avatar", user.getHeadUrl());
        map.put("introduction", user.getDescription());
        map.put("routers", router);
        map.put("buttons", perms);
        return Result.ok(map);
    }

    //    logout
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }
}
