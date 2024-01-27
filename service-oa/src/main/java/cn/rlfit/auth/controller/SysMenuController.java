package cn.rlfit.auth.controller;

import cn.rlfit.auth.service.SysMenuService;
import cn.rlfit.common.result.Result;
import cn.rlfit.model.system.SysMenu;
import cn.rlfit.vo.system.AssginMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 27/01/2024 12:16 PM
 */
@Api(tags = "04.角色菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    //    菜单的列表接口
    @ApiOperation("菜单列表获取")
    @GetMapping("/findNodes")
    public Result findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu permission) {
        sysMenuService.save(permission);
        return Result.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public Result updateById(@RequestBody SysMenu permission) {
        sysMenuService.updateById(permission);
        return Result.ok();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }
//查询所有菜单和角色分配的菜单

    @ApiOperation("查询是所有菜单和角色分配的菜单")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId){
        List<SysMenu> list = sysMenuService.findMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @ApiOperation("角色分配菜单")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuVo vo){
        sysMenuService.doAssign(vo);
        return Result.ok();
    }
}
