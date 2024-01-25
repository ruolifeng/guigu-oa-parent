package cn.rlfit.auth.controller;

import cn.rlfit.common.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    //    login
    @PostMapping("login")
    public Result login() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", "admin-token");
        return Result.ok(map);
    }

    //    info
    @GetMapping("info")
    public Result info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", "admin");
        map.put("avatar", "https://www.rlfit.cn/wp-content/uploads/2024/01/apple-touch-icon.png");
        map.put("introduction", "fdsgjdgdfhgkjdhfyg");
        return Result.ok(map);
    }

    //    logout
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }
}
