package cn.rlfit.auth.controller;

import cn.rlfit.auth.service.SysUserService;
import cn.rlfit.common.result.Result;
import cn.rlfit.model.system.SysUser;
import cn.rlfit.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @description: some desc
 * @author: sunjianrong
 * @email: sunruolifeng@gmail.com
 * @date: 26/01/2024 3:37 PM
 */
@Api(tags = "03.用户接口管理")
@RestController
@RequestMapping("/admin/system/user")
public class SysUserController {

    @Autowired
    private SysUserService service;

//    用户的条件分页查询

    @ApiOperation("01.用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit,
                        SysUserQueryVo vo) {
//        创建page对象
        Page<SysUser> pageParam = new Page<>(page, limit);
//        封装条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
//        根据名字进行过滤
        String keyword = vo.getKeyword();
//        根据注册时间进行过滤
        String createTimeBegin = vo.getCreateTimeBegin();
//        根据注册时间进行过滤
        String createTimeEnd = vo.getCreateTimeEnd();
        if (!StringUtils.isEmpty(keyword)) wrapper.like(SysUser::getUsername, keyword);
        if (!StringUtils.isEmpty(createTimeBegin)) wrapper.eq(SysUser::getCreateTime, createTimeBegin);
        if (!StringUtils.isEmpty(createTimeEnd)) wrapper.le(SysUser::getCreateTime, createTimeEnd);
//        条件查询
        Page<SysUser> sysUserPage = service.page(pageParam, wrapper);
        return Result.ok(sysUserPage);
    }

    @ApiOperation(value = "02.获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = service.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "03.保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        service.save(user);
        return Result.ok();
    }

    @ApiOperation(value = "04.更新用户")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        service.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "05.删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        service.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "06.更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        service.updateStatus(id, status);
        return Result.ok();
    }
}
