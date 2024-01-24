package cn.rlfit.auth.controller;

import cn.rlfit.auth.service.SysRoleService;
import cn.rlfit.common.result.Result;
import cn.rlfit.model.system.SysRole;
import cn.rlfit.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "01.角色管理接口")
@RestController //融合注解，交给spring进行管理，返回json数据
@RequestMapping("/admin/system/sysRole") //本类中基本的路径
public class SysRoleController {

    @Autowired
    SysRoleService service; //注入service

    /**
     * 查询所有角色
     *
     * @return 查询的所有角色的json数据
     */
//    @GetMapping("/findAll")
//    public List<SysRole> findAll() {
//        List<SysRole> list = service.list();
//        return list;
//    }
    @ApiOperation("01.查询所有角色")
    @GetMapping("/findAll")
    public Result<List<SysRole>> findAll() {
        List<SysRole> list = service.list();
        return Result.ok(list);
    }

    /**
     * 调用service的方法进行实现
     *
     * @param page  当前页
     * @param limit 每页显示的记录条数
     * @param vo    条件对象
     * @return 查询结果
     */
    @ApiOperation("02.条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result<Page<SysRole>> pageQueryRole(@PathVariable Long page,
                                               @PathVariable Long limit,
                                               SysRoleQueryVo vo) {
//        创建一个配置对象，传递参数
        Page<SysRole> pageParam = new Page<>(page, limit);
//        封装条件值，判断条件值是否是空，不是空值，则进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = vo.getRoleName();
//        封装的时候先判断该值是否为空，如果是空就不会进行封装
        if (!StringUtils.isEmpty(roleName)) wrapper.like(SysRole::getRoleName, roleName);
//        调用方法实现查询
        Page<SysRole> paged = service.page(pageParam, wrapper);
        return Result.ok(paged);
    }

    /**
     * 请求参数以JSON格式发送给后端
     *
     * @param role 需要添加的角色
     * @return 是否添加成功
     */
    @ApiOperation("03.添加角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole role) {
//        调用方法实现添加
        boolean isSuccess = service.save(role);
        if (isSuccess) return Result.ok();
        return Result.fail();
    }

    //    修改角色

    /**
     * @param id 传入需要查询的角色id
     * @return 返回查询结果
     */
    @ApiOperation("04.根据id查询角色")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysRole role = service.getById(id);
        return Result.ok(role);
    }

    /**
     * @param role 传入需要修改的角色
     * @return 返回修改结果
     */
    @ApiOperation("05.修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole role) {
        boolean isSuccess = service.updateById(role);
        if (isSuccess) return Result.ok();
        return Result.fail();
    }

    //    根据id删除

    /**
     * @param id 传入需要删除的角色id
     * @return 返回删除结果
     */
    @ApiOperation("06.删除角色")
    @DeleteMapping("remove/{id}")
    public Result removeById(@PathVariable Long id) {
        boolean isSuccess = service.removeById(id);
        if (isSuccess) return Result.ok();
        return Result.fail();

    }

    /**
     * @param idList 使用list结合接受前端传递的数组格式数据
     * @return 返回删除结果
     */
    @ApiOperation("07.批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean isSuccess = service.removeByIds(idList);
        if (isSuccess) return Result.ok();
        return Result.fail();
    }
}
