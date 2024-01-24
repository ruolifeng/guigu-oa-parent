package cn.rlfit.auth;

import cn.rlfit.auth.mapper.SysRoleMapper;
import cn.rlfit.auth.service.SysRoleService;
import cn.rlfit.model.system.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest //spring提供的测试注解，包含基本上所有的测试包
public class TestMpDemo1 {


    //注入
    @Autowired
    private SysRoleMapper mapper;

    @Autowired
    SysRoleService service;

    @Test
    public void getAll() {
        List<SysRole> list = mapper.selectList(null); //查询所有数据并返回成一个集合
        System.out.println(list);
    }

    //插入
    @Test
    public void add() {
        mapper.insert(new SysRole("张三", "sadhf", "新增加"));
    }

    //更新
    @Test
    public void update() {
//        根据id查询
        SysRole role = mapper.selectById(9);
//        设置修改的值
        role.setRoleName("李四");
//        调用方法进行最终的修改
        mapper.updateById(role);
    }

    //逻辑删除
    @Test
    public void deleteById() {
        int i = mapper.deleteById(9);
    }


    //    批量删除
    @Test
    public void testDeleteBatchIds() {
        mapper.deleteBatchIds(Arrays.asList(1, 2));
    }

    //    条件查询
    @Test
    public void testQuery01() {
//        创建querywrapper对象，调用方法封装条件

        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", "张三");
//        使用mapper执行查询操作
        List<SysRole> list = mapper.selectList(wrapper);
//        最终查询的时候什么都没有查询到，因为是被我们删除了
        System.out.println(list);
    }

    //     使用lambda表达式查询
    @Test
    public void lambdaQuery() {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getRoleName, "总经理");
    }

    //    service方法
    @Test
    public void SGetAll() {
        List<SysRole> list = service.list();
        System.out.println(list);
    }
}
